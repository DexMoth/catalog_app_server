package org.catalog_app.repositories;

import org.catalog_app.entities.ItemEntity;
import org.catalog_app.entities.TagEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TagRepository extends JpaRepository<TagEntity, Long> {
    List<TagEntity> findByUserId(Long userId);
}
