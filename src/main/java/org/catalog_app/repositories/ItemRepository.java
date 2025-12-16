package org.catalog_app.repositories;

import org.catalog_app.entities.ItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ItemRepository extends JpaRepository<ItemEntity, Long> {

    // найти по категории
    @Query("SELECT DISTINCT i FROM ItemEntity i " +
            "JOIN i.categories c " +
            "WHERE c.id = :category")
    List<ItemEntity> findByCategory(@Param("category") Long category);

    // найти по тегу
    @Query("SELECT DISTINCT i FROM ItemEntity i " +
            "JOIN i.tags t " +
            "WHERE t.id = :tag")
    List<ItemEntity> findByTag(@Param("tag") Long tag);

    // найти по тегу и категории
    @Query("SELECT DISTINCT i FROM ItemEntity i " +
            "JOIN i.categories c " +
            "JOIN i.tags t " +
            "WHERE c.id = :category AND t.id = :tag")
    List<ItemEntity> findByCategoryAndTag(@Param("category") Long category,
                                          @Param("tag") Long tag);

    // найти по тексту (в названии и описании)
    @Query("SELECT i FROM ItemEntity i " +
            "WHERE LOWER(i.name) LIKE LOWER(CONCAT('%', :searchText, '%')) " +
            "OR LOWER(i.description) LIKE LOWER(CONCAT('%', :searchText, '%'))")
    List<ItemEntity> findByText(@Param("searchText") String searchText);

//    @Query(value = """
//    WITH RECURSIVE item_tree AS (
//        SELECT id, name, description, image_path, parent_id, created_at, updated_at
//        FROM items
//        WHERE id = :itemId
//
//        UNION ALL
//
//        SELECT i.id, i.name, i.description, i.image_path, i.parent_id, i.created_at, i.updated_at
//        FROM items i
//        INNER JOIN item_tree it ON i.parent_id = it.id
//    )
//    SELECT * FROM item_tree WHERE id != :itemId
//    """, nativeQuery = true)
    @Query("SELECT i FROM ItemEntity i WHERE i.parent.id = :parentId")
    List<ItemEntity> findChildren(@Param("parentId") Long itemId);

    List<ItemEntity> findByParentId(Long parentId);
    List<ItemEntity> findByParentIsNull();
}
