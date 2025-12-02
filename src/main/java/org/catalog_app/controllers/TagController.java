package org.catalog_app.controllers;

import org.catalog_app.dtos.TagDto;
import org.catalog_app.entities.TagEntity;
import jakarta.transaction.Transactional;
import org.catalog_app.configurations.Constants;
import org.modelmapper.ModelMapper;
import org.springframework.web.bind.annotation.*;
import org.catalog_app.repositories.TagRepository;
import org.catalog_app.services.TagService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(Constants.API_URL + "/tag")
public class TagController {
    private final TagRepository repository;
    private final TagService service;
    private final ModelMapper modelMapper;

    public TagController(TagRepository repository, TagService service, ModelMapper modelMapper) {
        this.repository = repository;
        this.service = service;
        this.modelMapper = modelMapper;
    }

    @Transactional
    private TagDto toDto(TagEntity ent) {
        var dto = modelMapper.map(ent, TagDto.class);
        return dto;
    }

    @Transactional
    private TagEntity toEntity(TagDto dto) {
        var ent = modelMapper.map(dto, TagEntity.class);
        return ent;
    }


    @PostMapping
    public TagDto create(@RequestBody @Valid TagDto dto) {
        var ent = new TagEntity();
        ent.setName(dto.getName());
        return toDto(repository.save(ent));
    }

    @GetMapping
    public List<TagDto> getAll() {
        return service.getAll().stream().map(this::toDto).toList();
    }

    @GetMapping("/{id}")
    public TagDto get(@PathVariable(name = "id") Long id) {
        return toDto(service.get(id));
    }

    @PutMapping("/{id}")
    public TagDto update(@PathVariable(name = "id") Long id, @RequestBody TagDto dto) {
        return toDto(service.update(id, toEntity(dto)));
    }

    @DeleteMapping("/{id}")
    public TagDto delete(@PathVariable(name = "id") Long id) {
        return toDto(service.delete(id));
    }
}
