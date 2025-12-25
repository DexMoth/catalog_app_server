package org.catalog_app.controllers;

import org.catalog_app.configurations.Constants;
import org.catalog_app.dtos.CategoryDto;
import org.catalog_app.dtos.ItemDto;
import org.catalog_app.dtos.TagDto;
import org.catalog_app.entities.CategoryEntity;
import org.catalog_app.entities.ItemEntity;
import jakarta.transaction.Transactional;
import org.catalog_app.entities.TagEntity;
import org.modelmapper.ModelMapper;
import org.springframework.web.bind.annotation.*;
import org.catalog_app.repositories.ItemRepository;
import org.catalog_app.services.ItemService;

import javax.validation.Valid;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping(Constants.API_URL + "/item")
public class ItemController {
    private final ItemRepository repository;
    private final ItemService service;
    private final ModelMapper modelMapper;

    public ItemController(ItemRepository repository, ItemService service, ModelMapper modelMapper) {
        this.repository = repository;
        this.service = service;
        this.modelMapper = modelMapper;
    }

    @Transactional
    private ItemDto toDto(ItemEntity ent) {
        if (ent == null) return null;

        ItemDto dto = new ItemDto();
        dto.setId(ent.getId());
        dto.setName(ent.getName());
        dto.setDescription(ent.getDescription());
        dto.setImagePath(ent.getImagePath());

        if (ent.getParent() != null) {
            dto.setParentId(ent.getParent().getId());
        }

        if (ent.getCategories() != null) {
            Set<CategoryDto> categories = ent.getCategories().stream()
                    .map(categoryEntity -> modelMapper.map(categoryEntity, CategoryDto.class))
                    .collect(Collectors.toSet());
            dto.setCategories(categories);
        }

        if (ent.getTags() != null) {
            Set<TagDto> tags = ent.getTags().stream()
                    .map(tagEnt -> modelMapper.map(tagEnt, TagDto.class))
                    .collect(Collectors.toSet());
            dto.setTags(tags);
        }
        return dto;
    }

    @Transactional
    private ItemEntity toEntity(ItemDto dto) {
        if (dto == null) return null;

        ItemEntity entity = new ItemEntity();
        entity.setId(dto.getId());
        entity.setName(dto.getName());
        entity.setDescription(dto.getDescription());
        entity.setImagePath(dto.getImagePath());

        if (dto.getParentId() != null) {
            ItemEntity parent = new ItemEntity();
            parent.setId(dto.getParentId());
            entity.setParent(parent);
        }
        if (dto.getCategories() != null) {
            Set<CategoryEntity> categories = dto.getCategories().stream()
                    .map(categoryDto -> modelMapper.map(categoryDto, CategoryEntity.class))
                    .collect(Collectors.toSet());
            entity.setCategories(categories);
        }
        if (dto.getTags() != null) {
            Set<TagEntity> tags = dto.getTags().stream()
                    .map(tagDto -> modelMapper.map(tagDto, TagEntity.class))
                    .collect(Collectors.toSet());
            entity.setTags(tags);
        }
        return entity;
    }


    @PostMapping
    public ItemDto create(@RequestBody @Valid ItemDto dto) {
        return toDto(service.create(toEntity(dto)));
    }

    @GetMapping
    public List<ItemDto> getAll(
            @RequestParam(required = false) Long category,
            @RequestParam(required = false) Long tag,
            @RequestParam(required = false) String search) {

        return service.getAll(category, tag, search)
                .stream()
                .map( this::toDto)
                .toList();
    }

    @GetMapping("/roots")
    public List<ItemDto> getAllWithoutParent() {
        return service.getAllWithoutParent()
                .stream()
                .map(this::toDto)
                .toList();
    }

    @GetMapping("/{id}/children")
    public List<ItemDto> getChildren(
            @PathVariable(name = "id") Long id) {
        return service.findChildren(id)
                .stream()
                .map(this::toDto)
                .toList();
    }

    @GetMapping("/{id}")
    public ItemDto get(@PathVariable(name = "id") Long id) {
        return toDto(service.get(id));
    }

    @PutMapping("/{id}")
    public ItemDto update(@PathVariable(name = "id") Long id, @RequestBody ItemDto dto) {
        return toDto(service.update(id, toEntity(dto)));
    }

    @DeleteMapping("/{id}")
    public ItemDto delete(@PathVariable(name = "id") Long id) {
        return toDto(service.delete(id));
    }
}
