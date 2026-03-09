package org.catalog_app.controllers;

import jakarta.transaction.Transactional;
import org.catalog_app.configurations.Constants;
import org.catalog_app.dtos.RecurrenceRuleDto;
import org.catalog_app.entities.RecurrenceRuleEntity;
import org.catalog_app.repositories.RecurrenceRuleRepository;
import org.catalog_app.services.RecurrenceRuleService;
import org.modelmapper.ModelMapper;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(Constants.API_URL + "/recurrence_rule")
public class RecurrenceRuleController {
    private final RecurrenceRuleRepository repository;
    private final RecurrenceRuleService service;
    private final ModelMapper modelMapper;

    public RecurrenceRuleController(RecurrenceRuleRepository repository, RecurrenceRuleService service, ModelMapper modelMapper) {
        this.repository = repository;
        this.service = service;
        this.modelMapper = modelMapper;
    }

    @Transactional
    private RecurrenceRuleDto toDto(RecurrenceRuleEntity ent) {
        var dto = modelMapper.map(ent, RecurrenceRuleDto.class);
        return dto;
    }

    @Transactional
    private RecurrenceRuleEntity toEntity(RecurrenceRuleDto dto) {
        var ent = modelMapper.map(dto, RecurrenceRuleEntity.class);
        return ent;
    }


    @PostMapping
    public RecurrenceRuleDto create(@RequestBody @Valid RecurrenceRuleDto dto) {
//        var a = toEntity(dto);
//        var e = toDto(service.create(toEntity(dto)));
//         return toDto(service.create(toEntity(dto)));
        var ent = new RecurrenceRuleEntity();

        ent.setFrequency(dto.getFrequency());
        ent.setIntervalValue(dto.getIntervalValue());

        ent.setIntervalValue(dto.getIntervalValue());
        ent.setUntilType(dto.getUntilType());
        ent.setUntilDate(dto.getUntilDate());
        ent.setOccurrencesCount(dto.getOccurrencesCount());

        ent.setMonday(dto.getMonday());
        ent.setTuesday(dto.getTuesday());
        ent.setWednesday(dto.getWednesday());
        ent.setThursday(dto.getThursday());
        ent.setFriday(dto.getFriday());
        ent.setSaturday(dto.getSaturday());
        ent.setSunday(dto.getSunday());

        ent.setMonthDay(dto.getMonthDay());
        ent.setMonthWeek(dto.getMonthWeek());
        ent.setMonthWeekday(dto.getMonthWeekday());

        ent.setYearDay(dto.getYearDay());
        ent.setYearMonth(dto.getYearMonth());

        ent.setDescription(dto.getDescription());
        ent.setCreatedAt(dto.getCreatedAt());
        ent.setUpdatedAt(dto.getUpdatedAt());
        return toDto(repository.save(ent));
    }

    @GetMapping
    public List<RecurrenceRuleDto> getAll() {
        return service.getAll().stream().map(this::toDto).toList();
    }

    @GetMapping("/{id}")
    public RecurrenceRuleDto get(@PathVariable(name = "id") Long id) {
        return toDto(service.get(id));
    }

    @PutMapping("/{id}")
    public RecurrenceRuleDto update(@PathVariable(name = "id") Long id, @RequestBody RecurrenceRuleDto dto) {
        return toDto(service.update(id, toEntity(dto)));
    }

    @DeleteMapping("/{id}")
    public RecurrenceRuleDto delete(@PathVariable(name = "id") Long id) {
        return toDto(service.delete(id));
    }
}
