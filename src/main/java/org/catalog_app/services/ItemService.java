package org.catalog_app.services;

import org.catalog_app.entities.ItemEntity;
import org.catalog_app.error.NotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import org.catalog_app.repositories.ItemRepository;

import java.util.List;
import java.util.stream.StreamSupport;

@Service
public class ItemService {
    private final ItemRepository repository;

    public ItemService(ItemRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public List<ItemEntity> getAll(Long category, Long tag, String search) {
        List<ItemEntity> result;
        if (category != null && tag != null) {
            result = repository.findByCategoryAndTag(category, tag);
        } else if (category != null) {
            result = repository.findByCategory(category);
        } else if (tag != null) {
            result = repository.findByTag(tag);
        } else if (search != null && !search.trim().isEmpty()) {
            result = repository.findByText(search);
        } else {
            result = StreamSupport.stream(repository.findAll().spliterator(), false).toList();
        }
        return result.stream().toList();
    }

    @Transactional
    public ItemEntity get(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new NotFoundException(ItemEntity.class, id));
    }

    @Transactional
    public List<ItemEntity> findChildren(Long id) {
        return StreamSupport.stream(repository.findChildren(id).spliterator(), false).toList();
    }

    @Transactional
    public ItemEntity create(ItemEntity entity) {
        if (entity == null) {
            throw new IllegalArgumentException("Entity is null");
        }
        return repository.save(entity);
    }
    @Transactional
    public ItemEntity update(Long id,  ItemEntity entity) {
        ItemEntity el = repository.findById(id)
                .orElseThrow(() -> new NotFoundException(ItemEntity.class, id));
        el.setName(entity.getName());
        el.setUpdatedAt(entity.getUpdatedAt());
        el.setDescription(entity.getDescription());
        el.setCategories(entity.getCategories());
        el.setParent(entity.getParent());
        el.setImagePath(entity.getImagePath());
        return repository.save(el);
    }

    @Transactional
    public ItemEntity delete(Long id) {
        final ItemEntity existsEntity = get(id);
        repository.delete(existsEntity);
        return existsEntity;
    }
}
