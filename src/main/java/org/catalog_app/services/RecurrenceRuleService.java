package org.catalog_app.services;

import jakarta.transaction.Transactional;
import org.catalog_app.entities.RecurrenceRuleEntity;
import org.catalog_app.error.NotFoundException;
import org.catalog_app.repositories.RecurrenceRuleRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.StreamSupport;

@Service
public class RecurrenceRuleService {
    private final RecurrenceRuleRepository repository;

    public RecurrenceRuleService(RecurrenceRuleRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public List<RecurrenceRuleEntity> getAll() {
        return StreamSupport.stream(repository.findAll().spliterator(), false).toList();
    }
    @Transactional
    public RecurrenceRuleEntity get(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new NotFoundException(RecurrenceRuleEntity.class, id));
    }

    @Transactional
    public RecurrenceRuleEntity create(RecurrenceRuleEntity entity) {
        if (entity == null) {
            throw new IllegalArgumentException("Entity is null");
        }
        return repository.save(entity);
    }
    @Transactional
    public RecurrenceRuleEntity update(Long id,  RecurrenceRuleEntity entity) {
        if (!repository.existsById(id)) {
            throw new NotFoundException(RecurrenceRuleEntity.class, id);
        }
        entity.setId(id);
        return repository.save(entity);
    }

    @Transactional
    public RecurrenceRuleEntity delete(Long id) {
        final RecurrenceRuleEntity existsEntity = get(id);
        repository.delete(existsEntity);
        return existsEntity;
    }
}
