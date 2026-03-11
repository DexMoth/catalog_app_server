package org.catalog_app.services;

import jakarta.transaction.Transactional;
import org.catalog_app.entities.RecurrenceRuleEntity;
import org.catalog_app.entities.ReminderEntity;
import org.catalog_app.error.NotFoundException;
import org.catalog_app.repositories.RecurrenceRuleRepository;
import org.catalog_app.repositories.ReminderRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.StreamSupport;

@Service
public class ReminderService {
    private final ReminderRepository repository;
    private final RecurrenceRuleRepository ruleRepository;

    public ReminderService(ReminderRepository repository, RecurrenceRuleRepository ruleRepository) {
        this.repository = repository;
        this.ruleRepository = ruleRepository;
    }

    @Transactional
    public List<ReminderEntity> getAll() {
        return StreamSupport.stream(repository.findAll().spliterator(), false).toList();
    }
    @Transactional
    public ReminderEntity get(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new NotFoundException(ReminderEntity.class, id));
    }

    @Transactional
    public ReminderEntity create(ReminderEntity entity) {
        if (entity == null) {
            throw new IllegalArgumentException("Entity is null");
        }
        return repository.save(entity);
    }
    @Transactional
    public ReminderEntity update(Long id,  ReminderEntity entity) {
        ReminderEntity el = repository.findById(id)
                .orElseThrow(() -> new NotFoundException(ReminderEntity.class, id));
        el.setTitle(entity.getTitle());
        el.setDescription(entity.getDescription());
        el.setMessage(entity.getMessage());
        el.setReminderDate(entity.getReminderDate());
        el.setIsActive(entity.getIsActive());
        el.setItemId(entity.getItemId());
        el.setUpdatedAt(entity.getUpdatedAt());

        // если получили правило
        if (entity.getRecurrenceRule() != null) {
            // если правило уже есть, мы его меняем
            if (entity.getRecurrenceRule().getId() != null) {
                el.setRecurrenceRule(entity.getRecurrenceRule());
            }
            // если правила нет, создаем
            else {
                var newRule = new RecurrenceRuleEntity();
                newRule.setFrequency(entity.getRecurrenceRule().getFrequency());
                newRule.setIntervalValue(entity.getRecurrenceRule().getIntervalValue());
                newRule.setUntilType(entity.getRecurrenceRule().getUntilType());
                newRule.setUntilDate(entity.getRecurrenceRule().getUntilDate());
                el.setRecurrenceRule(ruleRepository.save(newRule));
            }
        }
        return repository.save(el);
    }

    @Transactional
    public ReminderEntity updateActive(Long id, boolean isActive) {
        ReminderEntity el = repository.findById(id)
                .orElseThrow(() -> new NotFoundException(ReminderEntity.class, id));
        el.setIsActive(isActive);
        return repository.save(el);
    }

    @Transactional
    public ReminderEntity delete(Long id) {
        final ReminderEntity existsEntity = get(id);
        repository.delete(existsEntity);
        return existsEntity;
    }
}
