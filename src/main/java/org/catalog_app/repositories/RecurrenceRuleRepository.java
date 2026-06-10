package org.catalog_app.repositories;

import org.catalog_app.entities.RecurrenceRuleEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RecurrenceRuleRepository extends JpaRepository<RecurrenceRuleEntity, Long> {
}
