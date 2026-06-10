package org.catalog_app.services;

import org.catalog_app.entities.CategoryEntity;
import org.catalog_app.error.NotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import org.catalog_app.repositories.CategoryRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.StreamSupport;

@Service
public class CategoryService {
    private final CategoryRepository repository;
    private static final List<String> DEFAULT_CATEGORIES = List.of(
            "одежда",
            "книги",
            "мебель",
            "посуда",
            "медицина",
            "уборка",
            "техника",
            "творчество",
            "аксессуары",
            "детское",
            "безопасность",
            "хоз. инструменты",
            "текстиль",
            "хранение",
            "декор",
            "канцелярия",
            "хобби",
            "электроника"
    );

    @Transactional
    public void createDefaultCategoriesForUser(Long userId) {
        for (String categoryName : DEFAULT_CATEGORIES) {
            try {
                CategoryEntity category = new CategoryEntity();
                category.setName(categoryName);
                category.setUserId(userId);
                category.setCreatedAt(LocalDateTime.now());
                category.setUpdatedAt(LocalDateTime.now());
                repository.save(category);
                System.out.println("Создана категория: " + categoryName + " для пользователя " + userId);
            } catch (Exception e) {
                System.out.println("Ошибка при создании категории " + categoryName + ": " + e.getMessage());
            }
        }
    }

    public CategoryService(CategoryRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public List<CategoryEntity> getAll(Long userId) {
        if (userId == null) {
            throw new IllegalArgumentException("User ID must not be null");
        }

        return StreamSupport.stream(repository.findByUserId(userId).spliterator(), false).toList();
    }
    @Transactional
    public CategoryEntity get(Long userId, Long id) {
        if (userId == null) {
            throw new IllegalArgumentException("User ID must not be null");
        }

        return repository.findByUserIdAndId(userId, id)
                .orElseThrow(() -> new NotFoundException(CategoryEntity.class, id));
    }

    @Transactional
    public CategoryEntity create(Long userId, CategoryEntity entity) {
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
    public CategoryEntity update(Long userId, Long id,  CategoryEntity entity) {
        if (userId == null) {
            throw new IllegalArgumentException("User ID must not be null");
        }

        CategoryEntity el = repository.findById(id)
                .orElseThrow(() -> new NotFoundException(CategoryEntity.class, id));
        el.setName(entity.getName());
        el.setUpdatedAt(entity.getUpdatedAt());
        return repository.save(el);
    }

    @Transactional
    public CategoryEntity delete(Long userId, Long id) {
        final CategoryEntity existsEntity = get(userId, id);
        repository.delete(existsEntity);
        return existsEntity;
    }
}
