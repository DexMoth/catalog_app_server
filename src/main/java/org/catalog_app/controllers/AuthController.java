package org.catalog_app.controllers;

import org.catalog_app.configurations.Constants;
import org.catalog_app.dtos.AuthRequest;
import org.catalog_app.dtos.AuthResponse;
import org.catalog_app.dtos.RegisterRequest;
import org.catalog_app.dtos.VerifyCodeRequest;
import org.catalog_app.services.AuthService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(Constants.API_URL + "/auth")
@CrossOrigin(origins = "*")
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public AuthResponse register(@RequestBody RegisterRequest request) {
        return authService.register(request.getUsername(), request.getEmail(), request.getPassword());
    }

    @PostMapping("/verify")
    public AuthResponse verifyCode(@RequestBody VerifyCodeRequest request) {
        return authService.verifyCode(request.getEmail(), request.getCode());
    }

    @PostMapping("/resend-code")
    public AuthResponse resendCode(@RequestBody VerifyCodeRequest request) {
        return authService.resendCode(request.getEmail());
    }

    @PostMapping("/login")
    public AuthResponse login(@RequestBody RegisterRequest request) {
        return authService.login(request.getEmail(), request.getPassword());
    }
}