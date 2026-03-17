package org.catalog_app.controllers;

import jakarta.transaction.Transactional;
import org.catalog_app.configurations.Constants;
import org.catalog_app.dtos.ReminderDto;
import org.catalog_app.entities.CategoryEntity;
import org.catalog_app.entities.RecurrenceRuleEntity;
import org.catalog_app.entities.ReminderEntity;
import org.catalog_app.error.NotFoundException;
import org.catalog_app.repositories.RecurrenceRuleRepository;
import org.catalog_app.repositories.ReminderRepository;
import org.catalog_app.services.RecurrenceRuleService;
import org.catalog_app.services.ReminderService;
import org.modelmapper.ModelMapper;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(Constants.API_URL + "/reminder")
public class ReminderController {
    private final ReminderRepository repository;
    private final ReminderService service;
    private final ModelMapper modelMapper;
    private final RecurrenceRuleService recurrenceRuleService;

    public ReminderController(ReminderRepository repository, ReminderService service, ModelMapper modelMapper, RecurrenceRuleService recurrenceRuleService) {
        this.repository = repository;
        this.service = service;
        this.modelMapper = modelMapper;
        this.recurrenceRuleService = recurrenceRuleService;
    }

    @Transactional
    private ReminderDto toDto(ReminderEntity ent) {
        var dto = modelMapper.map(ent, ReminderDto.class);
        return dto;
    }

    @Transactional
    private ReminderEntity toEntity(ReminderDto dto) {
        var ent = modelMapper.map(dto, ReminderEntity.class);
        return ent;
    }


    @PostMapping
    public ReminderDto create(@RequestBody @Valid ReminderDto dto) {
        var ent = new ReminderEntity();
        ent.setTitle(dto.getTitle());
        ent.setDescription(dto.getDescription());
        ent.setMessage(dto.getMessage());

        ent.setItemId(dto.getItemId());
        ent.setUserId(dto.getUserId());

        ent.setReminderDate(dto.getReminderDate());
        ent.setIsActive(dto.getIsActive());

        ent.setCreatedAt(dto.getCreatedAt());
        ent.setUpdatedAt(dto.getUpdatedAt());

        if (dto.getRecurrenceRule() != null) {
            var rule = new RecurrenceRuleEntity();
            rule.setFrequency(dto.getRecurrenceRule().getFrequency());
            rule.setIntervalValue(dto.getRecurrenceRule().getIntervalValue());
            rule.setUntilType(dto.getRecurrenceRule().getUntilType());
            rule.setUntilDate(dto.getRecurrenceRule().getUntilDate());
            recurrenceRuleService.create(rule);

            ent.setRecurrenceRule(rule);
        }

        return toDto(repository.save(ent));
    }

    @GetMapping
    public List<ReminderDto> getAll() {
        return service.getAll().stream().map(this::toDto).toList();
    }

    @GetMapping("/{id}")
    public ReminderDto get(@PathVariable(name = "id") Long id) {
        return toDto(service.get(id));
    }

    @PutMapping("/{id}")
    public ReminderDto update(@PathVariable(name = "id") Long id, @RequestBody ReminderDto dto) {
        return toDto(service.update(id, toEntity(dto)));
    }

    @PutMapping("/{id}/active")
    public ReminderDto updateActive(
            @PathVariable(name = "id") Long id,
            @RequestParam boolean isActive) {
        return toDto(service.updateActive(id, isActive));
    }

    @DeleteMapping("/{id}")
    public ReminderDto delete(@PathVariable(name = "id") Long id) {
        return toDto(service.delete(id));
    }
}
