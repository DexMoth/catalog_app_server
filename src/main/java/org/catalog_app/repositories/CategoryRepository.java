package org.catalog_app.repositories;

import org.catalog_app.entities.CategoryEntity;
import org.catalog_app.entities.TagEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<CategoryEntity, Long> {
    List<CategoryEntity> findByUserId(Long userId);
    Optional<CategoryEntity> findByUserIdAndId(Long userId, Long id);

    boolean existsByUserIdAndName(Long userId, String categoryName);
}
