package org.catalog_app.services;

import jakarta.transaction.Transactional;
import org.catalog_app.entities.ImageEntity;
import org.catalog_app.error.NotFoundException;
import org.catalog_app.repositories.ImageRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.StreamSupport;

@Service
public class ImageService {
    private final ImageRepository repository;

    public ImageService(ImageRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public List<ImageEntity> getAll() {
        return StreamSupport.stream(repository.findAll().spliterator(), false).toList();
    }
    @Transactional
    public ImageEntity get(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new NotFoundException(ImageEntity.class, id));
    }

    @Transactional
    public ImageEntity create(ImageEntity entity) {
        if (entity == null) {
            throw new IllegalArgumentException("Entity is null");
        }
        return repository.save(entity);
    }
    @Transactional
    public ImageEntity update(Long id,  ImageEntity entity) {
        ImageEntity el = repository.findById(id)
                .orElseThrow(() -> new NotFoundException(ImageEntity.class, id));
        el.setIsMain(entity.getIsMain());
        el.setUrl(entity.getUrl());
        el.setItemId(entity.getItemId());
        return repository.save(el);
    }

    @Transactional
    public ImageEntity delete(Long id) {
        final ImageEntity existsEntity = get(id);
        repository.delete(existsEntity);
        return existsEntity;
    }
}
