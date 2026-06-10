package org.catalog_app.services;

import org.catalog_app.entities.TagEntity;
import org.catalog_app.error.NotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import org.catalog_app.repositories.TagRepository;

import java.util.List;
import java.util.stream.StreamSupport;

@Service
public class TagService {
    private final TagRepository repository;

    public TagService(TagRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public List<TagEntity> getAll(Long userId) {
        if (userId == null) {
            throw new IllegalArgumentException("User ID must not be null");
        }

        return StreamSupport.stream(repository.findByUserId(userId).spliterator(), false).toList();
    }
    @Transactional
    public TagEntity get(Long userId, Long id) {
        if (userId == null) {
            throw new IllegalArgumentException("User ID must not be null");
        }
        return repository.findByUserIdAndId(userId, id)
                .orElseThrow(() -> new NotFoundException(TagEntity.class, id));
    }

    @Transactional
    public TagEntity create(Long userId, TagEntity entity) {
        if (userId == null) {
            throw new IllegalArgumentException("User ID must not be null");
        }
        if (entity == null) {
            throw new IllegalArgumentException("Entity is null");
        }
        entity.setUserId(userId);
        return repository.save(entity);
    }
    @Transactional
    public TagEntity update(Long userId, Long id,  TagEntity entity) {
        if (userId == null) {
            throw new IllegalArgumentException("User ID must not be null");
        }

        TagEntity el = repository.findById(id)
                .orElseThrow(() -> new NotFoundException(TagEntity.class, id));
        el.setName(entity.getName());
        el.setUpdatedAt(entity.getUpdatedAt());
        return repository.save(el);
    }

    @Transactional
    public TagEntity delete(Long userId, Long id) {
        if (userId == null) {
            throw new IllegalArgumentException("User ID must not be null");
        }

        final TagEntity existsEntity = get(userId, id);
        repository.delete(existsEntity);
        return existsEntity;
    }
}
