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
    public List<ItemEntity> getAll(Long user, Long category, Long tag, String search) {

        if (user == null) {
            throw new IllegalArgumentException("User ID must not be null");
        }

        List<ItemEntity> result;
        if (category != null && tag != null) {
            result = repository.findByCategoryAndTag(user, category, tag);
        } else if (category != null) {
            result = repository.findByCategory(user, category);
        } else if (tag != null) {
            result = repository.findByTag(user, tag);
        } else if (search != null && !search.trim().isEmpty()) {
            result = repository.findByText(user, search);
        } else {
            result = repository.findByUserId(user);
        }
        return result.stream().toList();
    }

    @Transactional
    public List<ItemEntity> getAllWithoutParent(Long user) {
        List<ItemEntity> result = StreamSupport.stream(repository.findByParentIsNullAndUserId(user).spliterator(), false).toList();
        return result;
    }

    @Transactional
    public ItemEntity  get(Long user, Long id) {
        return repository.findByUserIdAndId(user, id)
                .orElseThrow(() -> new NotFoundException(ItemEntity.class, id));
    }

    @Transactional
    public List<ItemEntity> findChildren(Long user, Long id) {
        return StreamSupport.stream(repository.findChildren(user, id).spliterator(), false).toList();
    }

    @Transactional
    public ItemEntity create(Long user, ItemEntity entity) {
        if (entity == null) {
            throw new IllegalArgumentException("Entity is null");
        }
        return repository.save(entity);
    }
    @Transactional
    public ItemEntity update(Long user, Long id,  ItemEntity entity) {
        ItemEntity el = repository.findById(id)
                .orElseThrow(() -> new NotFoundException(ItemEntity.class, id));
        el.setName(entity.getName());
        el.setUpdatedAt(entity.getUpdatedAt());
        el.setDescription(entity.getDescription());
        el.setCategory(entity.getCategory());
        el.setTags(entity.getTags());
        el.setParent(entity.getParent());;
        return repository.save(el);
    }

    @Transactional
    public ItemEntity delete(Long user, Long id) {
        final ItemEntity existsEntity = get(user, id);
        repository.delete(existsEntity);
        return existsEntity;
    }
}
