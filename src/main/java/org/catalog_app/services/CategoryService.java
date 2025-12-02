package org.catalog_app.services;

import org.catalog_app.entities.CategoryEntity;
import org.catalog_app.error.NotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import org.catalog_app.repositories.CategoryRepository;

import java.util.List;
import java.util.stream.StreamSupport;

@Service
public class CategoryService {
    private final CategoryRepository repository;

    public CategoryService(CategoryRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public List<CategoryEntity> getAll() {
        return StreamSupport.stream(repository.findAll().spliterator(), false).toList();
    }
    @Transactional
    public CategoryEntity get(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new NotFoundException(CategoryEntity.class, id));
    }

    @Transactional
    public CategoryEntity create(CategoryEntity entity) {
        if (entity == null) {
            throw new IllegalArgumentException("Entity is null");
        }
        return repository.save(entity);
    }
    @Transactional
    public CategoryEntity update(Long id,  CategoryEntity entity) {
        CategoryEntity el = repository.findById(id)
                .orElseThrow(() -> new NotFoundException(CategoryEntity.class, id));
        el.setName(entity.getName());
        el.setUpdatedAt(entity.getUpdatedAt());
        return repository.save(el);
    }

    @Transactional
    public CategoryEntity delete(Long id) {
        final CategoryEntity existsEntity = get(id);
        repository.delete(existsEntity);
        return existsEntity;
    }
}
