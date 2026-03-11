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
    private final TagService service;
    private final ModelMapper modelMapper;

    public TagController(TagService service, ModelMapper modelMapper) {
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
    public TagDto create(
            @RequestParam(name = "userId") Long userId,
            @RequestBody @Valid TagDto dto) {
        var ent = new TagEntity();
        ent.setName(dto.getName());
        ent.setCreatedAt(dto.getCreatedAt());
        ent.setUserId(dto.getUserId());
        return toDto(service.create(userId, toEntity(dto)));
    }

    @GetMapping
    public List<TagDto> getAll(
            @RequestParam(name = "userId") Long userId) {
        return service.getAll(userId)
                .stream()
                .map(this::toDto)
                .toList();
    }

    @GetMapping("/{id}")
    public TagDto get(
            @RequestParam(name = "userId") Long userId,
            @PathVariable(name = "id") Long id) {
        return toDto(service.get(userId, id));
    }

    @PutMapping("/{id}")
    public TagDto update(
            @RequestParam(name = "userId") Long userId,
            @PathVariable(name = "id") Long id, @RequestBody TagDto dto) {
        return toDto(service.update(userId, id, toEntity(dto)));
    }

    @DeleteMapping("/{id}")
    public TagDto delete(
            @RequestParam(name = "userId") Long userId,
            @PathVariable(name = "id") Long id) {
        return toDto(service.delete(userId, id));
    }
}
