package org.catalog_app.services;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.transaction.Transactional;
import org.catalog_app.dtos.AuthResponse;
import org.catalog_app.entities.UserEntity;
import org.catalog_app.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Optional;

@Service
public class AuthService {
    private final UserRepository userRepository;
    private final EmailService emailService;
    private final CategoryService categoryService;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    private final SecretKey secretKey;
    private final long EXPIRATION_TIME = 86400000;

    @Value("${verification.code.expiration:10}")
    private int codeExpirationMinutes;

    public AuthService(UserRepository userRepository, EmailService emailService, CategoryService categoryService) {
        this.userRepository = userRepository;
        this.emailService = emailService;
        this.categoryService = categoryService;
        this.secretKey = Keys.hmacShaKeyFor(
                "mySecretKeyForJWT123456789012345678901234".getBytes(StandardCharsets.UTF_8)
        );
    }

    @Transactional
    public AuthResponse register(String username, String email, String rawPassword) {
        // Проверяем, существует ли пользователь
        Optional<UserEntity> existing = userRepository.findByEmail(email);
        if (existing.isPresent()) {
            UserEntity user = existing.get();
            if (user.isVerified()) {
                return new AuthResponse(false, "Email уже зарегистрирован", null, null, null, null);
            } else {
                // Если не подтверждён - удаляем старого
                userRepository.delete(user);
            }
        }

        // Создаём пользователя
        UserEntity user = new UserEntity();
        user.setName(username);
        user.setEmail(email);
        user.setPasswordHash(passwordEncoder.encode(rawPassword));
        user.setVerified(false);
        user.setCreatedAt(LocalDateTime.now());

        // Генерируем код (6 цифр)
        String code = String.valueOf(100000 + new SecureRandom().nextInt(900000));
        user.setVerificationCode(code);
        user.setCodeExpiresAt(LocalDateTime.now().plusMinutes(codeExpirationMinutes));

        userRepository.save(user);
        // Отправляем код на email
        try {
            emailService.sendVerificationCode(email, code);
            System.out.println("Код отправлен на " + email + ": " + code);
        } catch (Exception e) {
            System.out.println("Ошибка отправки email: " + e.getMessage());
            System.out.println("Код для теста: " + code);
        }

        return new AuthResponse(false, "Код подтверждения отправлен на email", null, null, null, null);
    }

    @Transactional
    public AuthResponse verifyCode(String email, String code) {
        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Пользователь не найден"));

        if (user.isVerified()) {
            return new AuthResponse(false, "Email уже подтверждён", null, null, null, null);
        }

        if (!code.equals(user.getVerificationCode())) {
            return new AuthResponse(false, "Неверный код", null, null, null, null);
        }

        if (user.getCodeExpiresAt().isBefore(LocalDateTime.now())) {
            return new AuthResponse(false, "Код истёк. Запросите новый", null, null, null, null);
        }

        // Активируем пользователя
        user.setVerified(true);
        user.setVerificationCode(null);
        user.setCodeExpiresAt(null);
        userRepository.save(user);

        // Генерируем токен
        String token = generateToken(user.getId(), user.getEmail());

        return new AuthResponse(true, "Email подтверждён", user.getId(), user.getName(), user.getEmail(), token);
    }

    @Transactional
    public AuthResponse resendCode(String email) {
        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Пользователь не найден"));

        if (user.isVerified()) {
            return new AuthResponse(false, "Email уже подтверждён", null, null, null, null);
        }

        // Генерируем новый код
        String newCode = String.valueOf(100000 + new SecureRandom().nextInt(900000));
        user.setVerificationCode(newCode);
        user.setCodeExpiresAt(LocalDateTime.now().plusMinutes(codeExpirationMinutes));
        userRepository.save(user);

        // Отправляем новый код
        try {
            emailService.sendVerificationCode(email, newCode);
            System.out.println("Новый код отправлен на " + email + ": " + newCode);
        } catch (Exception e) {
            System.out.println("Ошибка отправки email: " + e.getMessage());
            System.out.println("Код для теста: " + newCode);
        }

        return new AuthResponse(false, "Новый код отправлен на email", null, null, null, null);
    }

    public AuthResponse login(String email, String rawPassword) {
        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Неверный email или пароль"));

        if (!user.isVerified()) {
            throw new RuntimeException("Email не подтверждён. Проверьте почту");
        }

        if (!passwordEncoder.matches(rawPassword, user.getPasswordHash())) {
            throw new RuntimeException("Неверный email или пароль");
        }

        String token = generateToken(user.getId(), user.getEmail());

        return new AuthResponse(true, "Вход выполнен", user.getId(), user.getName(), user.getEmail(), token);
    }

    private String generateToken(Long userId, String email) {
        return Jwts.builder()
                .setSubject(userId.toString())
                .claim("email", email)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();
    }

    public Long validateToken(String token) {
        try {
            var claims = Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
            return Long.parseLong(claims.getSubject());
        } catch (Exception e) {
            return null;
        }
    }
}