package org.catalog_app.repositories;

import org.catalog_app.entities.ItemEntity;
import org.catalog_app.entities.TagEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ItemRepository extends JpaRepository<ItemEntity, Long> {

    // найти по категории
    @Query("SELECT DISTINCT i FROM ItemEntity i " +
            "JOIN i.category c " +
            "WHERE i.userId = :userId AND c.id = :category")
    List<ItemEntity> findByCategory(@Param("userId") Long userId,
                                    @Param("category") Long category);

    // найти по тегу
    @Query("SELECT DISTINCT i FROM ItemEntity i " +
            "JOIN i.tags t " +
            "WHERE i.userId = :userId AND t.id = :tag")
    List<ItemEntity> findByTag(@Param("userId") Long userId,
                               @Param("tag") Long tag);

    // найти по тегу и категории
    @Query("SELECT DISTINCT i FROM ItemEntity i " +
            "JOIN i.category c " +
            "JOIN i.tags t " +
            "WHERE i.userId = :userId AND c.id = :category AND t.id = :tag")
    List<ItemEntity> findByCategoryAndTag(@Param("userId") Long userId,
                                          @Param("category") Long category,
                                          @Param("tag") Long tag);

    // найти по тексту (в названии и описании)
    @Query("SELECT i FROM ItemEntity i " +
            "WHERE i.userId = :userId AND " +
            "LOWER(i.name) LIKE LOWER(CONCAT('%', :searchText, '%')) " +
            "OR LOWER(i.description) LIKE LOWER(CONCAT('%', :searchText, '%'))")
    List<ItemEntity> findByText(@Param("userId") Long userId,
                                @Param("searchText") String searchText);

    @Query("SELECT i FROM ItemEntity i " +
            "WHERE i.userId = :userId AND i.parent.id = :parentId")
    List<ItemEntity> findChildren(@Param("userId") Long userId,
                                  @Param("parentId") Long itemId);

    List<ItemEntity> findByUserIdAndParentId(Long userId, Long parentId);
    List<ItemEntity> findByParentIsNullAndUserId(Long userId);
    List<ItemEntity> findByUserId(Long userId);
    Optional<ItemEntity> findByUserIdAndId(Long userId, Long id);
}
