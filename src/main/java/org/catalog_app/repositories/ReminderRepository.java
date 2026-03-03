package org.catalog_app.repositories;

import org.catalog_app.entities.ReminderEntity;
import org.catalog_app.entities.TagEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReminderRepository extends JpaRepository<ReminderEntity, Long> {
    List<ReminderEntity> findByUserId(Long userId);
}
