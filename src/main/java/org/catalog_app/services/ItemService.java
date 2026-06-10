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
    public List<ItemEntity> getAll(Long userId, Long category, Long tag, String search) {

        if (userId == null) {
            throw new IllegalArgumentException("User ID must not be null");
        }

        List<ItemEntity> result;
        if (category != null && tag != null) {
            result = repository.findByCategoryAndTag(userId, category, tag);
        } else if (category != null) {
            result = repository.findByCategory(userId, category);
        } else if (tag != null) {
            result = repository.findByTag(userId, tag);
        } else if (search != null && !search.trim().isEmpty()) {
            result = repository.findByText(userId, search);
        } else {
            result = repository.findByUserId(userId);
        }
        return result.stream().toList();
    }

    @Transactional
    public List<ItemEntity> getAllWithoutParent(Long userId) {
        if (userId == null) {
            throw new IllegalArgumentException("User ID must not be null");
        }

        List<ItemEntity> result = StreamSupport.stream(repository.findByParentIsNullAndUserId(userId).spliterator(), false).toList();
        return result;
    }

    @Transactional
    public ItemEntity get(Long userId, Long id) {
        if (userId == null) {
            throw new IllegalArgumentException("User ID must not be null");
        }

        return repository.findByUserIdAndId(userId, id)
                .orElseThrow(() -> new NotFoundException(ItemEntity.class, id));
    }

    @Transactional
    public List<ItemEntity> findChildren(Long userId, Long id) {
        if (userId == null) {
            throw new IllegalArgumentException("User ID must not be null");
        }

        return StreamSupport.stream(repository.findChildren(userId, id).spliterator(), false).toList();
    }

    @Transactional
    public ItemEntity create(Long userId, ItemEntity entity) {
        if (userId == null) {
            throw new IllegalArgumentException("User ID must not be null");
        }

        if (entity == null) {
            throw new IllegalArgumentException("Entity is null");
        }
        return repository.save(entity);
    }
    @Transactional
    public ItemEntity update(Long userId, Long id,  ItemEntity entity) {
        if (userId == null) {
            throw new IllegalArgumentException("User ID must not be null");
        }

        ItemEntity el = repository.findById(id)
                .orElseThrow(() -> new NotFoundException(ItemEntity.class, id));
        el.setName(entity.getName());
        el.setImagePath(entity.getImagePath());
        el.setUpdatedAt(entity.getUpdatedAt());
        el.setDescription(entity.getDescription());
        el.setCategory(entity.getCategory());
        el.setTags(entity.getTags());
        el.setParent(entity.getParent());
        el.setEmbedding(entity.getEmbedding());
        return repository.save(el);
    }

    @Transactional
    public ItemEntity delete(Long userId, Long id) {
        if (userId == null) {
            throw new IllegalArgumentException("User ID must not be null");
        }

        final ItemEntity existsEntity = get(userId, id);

        // чистим родителя у потомков этого предмета
        List<ItemEntity> children = repository.findByUserIdAndParentId(userId, id);
        if (existsEntity.getParent() == null) {
            for (ItemEntity child : children) {
                child.setParent(null);
                repository.save(child);
            }
        } else {
            var newParent = existsEntity.getParent();
            for (ItemEntity child : children) {
                child.setParent(newParent);
                repository.save(child);
            }
        }


        repository.delete(existsEntity);
        return existsEntity;
    }
}
