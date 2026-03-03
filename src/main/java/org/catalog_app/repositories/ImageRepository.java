package org.catalog_app.repositories;

import org.catalog_app.entities.ImageEntity;
import org.catalog_app.entities.TagEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ImageRepository extends JpaRepository<ImageEntity, Long> {
    List<ImageEntity> findByUserId(Long userId);
}
