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
    public List<TagEntity> getAll() {
        return StreamSupport.stream(repository.findAll().spliterator(), false).toList();
    }
    @Transactional
    public TagEntity get(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new NotFoundException(TagEntity.class, id));
    }

    @Transactional
    public TagEntity create(TagEntity entity) {
        if (entity == null) {
            throw new IllegalArgumentException("Entity is null");
        }
        return repository.save(entity);
    }
    @Transactional
    public TagEntity update(Long id,  TagEntity entity) {
        TagEntity el = repository.findById(id)
                .orElseThrow(() -> new NotFoundException(TagEntity.class, id));
        el.setName(entity.getName());
        el.setUpdatedAt(entity.getUpdatedAt());
        return repository.save(el);
    }

    @Transactional
    public TagEntity delete(Long id) {
        final TagEntity existsEntity = get(id);
        repository.delete(existsEntity);
        return existsEntity;
    }
}
