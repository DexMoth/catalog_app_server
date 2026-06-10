package org.catalog_app.services;

import jakarta.transaction.Transactional;
import org.catalog_app.entities.UserEntity;
import org.catalog_app.error.NotFoundException;
import org.catalog_app.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.StreamSupport;

@Service
public class UserService {
    private final UserRepository repository;

    public UserService(UserRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public List<UserEntity> getAll() {
        return StreamSupport.stream(repository.findAll().spliterator(), false).toList();
    }
    @Transactional
    public UserEntity get(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new NotFoundException(UserEntity.class, id));
    }

    @Transactional
    public UserEntity create(UserEntity entity) {
        if (entity == null) {
            throw new IllegalArgumentException("Entity is null");
        }
        return repository.save(entity);
    }
    @Transactional
    public UserEntity update(Long id,  UserEntity entity) {
        UserEntity el = repository.findById(id)
                .orElseThrow(() -> new NotFoundException(UserEntity.class, id));
        el.setName(entity.getName());
        el.setEmail(entity.getEmail());
        el.setGoogleId(entity.getGoogleId());
        el.setAvatarUrl(entity.getAvatarUrl());
        el.setCreatedAt(entity.getCreatedAt());
        return repository.save(el);
    }

    @Transactional
    public UserEntity delete(Long id) {
        final UserEntity existsEntity = get(id);
        repository.delete(existsEntity);
        return existsEntity;
    }
}
