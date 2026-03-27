package org.catalog_app.controllers;

import org.catalog_app.configurations.Constants;
import org.catalog_app.dtos.CategoryDto;
import org.catalog_app.dtos.ItemDto;
import org.catalog_app.dtos.TagDto;
import org.catalog_app.entities.CategoryEntity;
import org.catalog_app.entities.ItemEntity;
import jakarta.transaction.Transactional;
import org.catalog_app.entities.TagEntity;
import org.catalog_app.error.NotFoundException;
import org.catalog_app.repositories.CategoryRepository;
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
    private final CategoryRepository categoryRepository;
    private final ItemService service;
    private final ModelMapper modelMapper;

    public ItemController(ItemRepository repository, CategoryRepository categoryRepository, ItemService service, ModelMapper modelMapper) {
        this.repository = repository;
        this.categoryRepository = categoryRepository;
        this.service = service;
        this.modelMapper = modelMapper;
    }

    @Transactional
    private ItemDto toDto(ItemEntity ent) {
        if (ent == null) return null;

        ItemDto dto = new ItemDto();
        dto.setId(ent.getId());
        dto.setName(ent.getName());
        dto.setImagePath(ent.getImagePath());
        dto.setDescription(ent.getDescription());
        dto.setUserId(ent.getUserId());
        dto.setCreatedAt(ent.getCreatedAt());
        dto.setUpdatedAt(ent.getUpdatedAt());

        if (ent.getParent() != null) {
            dto.setParentId(ent.getParent().getId());
        }

        if (ent.getCategory() != null) {
            dto.setCategory(ent.getCategory().getId());
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
        entity.setUserId(dto.getUserId());

        if (dto.getParentId() != null) {
            ItemEntity parent = new ItemEntity();
            parent.setId(dto.getParentId());
            entity.setParent(parent);
        }
        if (dto.getCategory() != null) {
            CategoryEntity category = categoryRepository.findById(dto.getCategory())
                    .orElseThrow(() -> new NotFoundException("Category not found with id" + dto.getCategory()));
            entity.setCategory(category);
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
    public ItemDto create(
            @RequestParam(name = "userId") Long userId,
            @RequestBody @Valid ItemDto dto) {
        return toDto(service.create(userId, toEntity(dto)));
    }

    @GetMapping
    public List<ItemDto> getAll(
            @RequestParam(name = "userId") Long userId,
            @RequestParam(required = false) Long category,
            @RequestParam(required = false) Long tag,
            @RequestParam(required = false) String search) {

        return service.getAll(userId, category, tag, search)
                .stream()
                .map( this::toDto)
                .toList();
    }

    @GetMapping("/roots")
    public List<ItemDto> getAllWithoutParent(
            @RequestParam(name = "userId") Long userId) {
        return service.getAllWithoutParent(userId)
                .stream()
                .map(this::toDto)
                .toList();
    }

    @GetMapping("/{id}/children")
    public List<ItemDto> getChildren(
            @RequestParam(name = "userId") Long userId,
            @PathVariable(name = "id") Long id) {
        return service.findChildren(userId, id)
                .stream()
                .map(this::toDto)
                .toList();
    }

    @GetMapping("/{id}")
    public ItemDto get(
            @RequestParam(name = "userId") Long userId,
            @PathVariable(name = "id") Long id) {
        return toDto(service.get(userId, id));
    }

    @PutMapping("/{id}")
    public ItemDto update(
            @RequestParam(name = "userId") Long userId,
            @PathVariable(name = "id") Long id,
            @RequestBody ItemDto dto) {
        return toDto(service.update(userId, id , toEntity(dto)));
    }

    @DeleteMapping("/{id}")
    public ItemDto delete(
            @RequestParam(name = "userId") Long userId,
            @PathVariable(name = "id") Long id) {
        return toDto(service.delete(userId, id));
    }
}
