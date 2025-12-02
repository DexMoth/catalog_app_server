package org.catalog_app.controllers;

import org.catalog_app.configurations.Constants;
import org.catalog_app.dtos.ItemDto;
import org.catalog_app.entities.ItemEntity;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.web.bind.annotation.*;
import org.catalog_app.repositories.ItemRepository;
import org.catalog_app.services.ItemService;

import javax.validation.Valid;
import java.util.List;

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
        var dto = modelMapper.map(ent, ItemDto.class);
        return dto;
    }

    @Transactional
    private ItemEntity toEntity(ItemDto dto) {
        var ent = modelMapper.map(dto, ItemEntity.class);
        return ent;
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
                .map(this::toDto)
                .toList();
    }

    @GetMapping("/{id}")
    public ItemDto get(@PathVariable(name = "id") Long id) {
        return toDto(service.get(id));
    }

    @GetMapping("/{id}/children")
    public List<ItemDto> getChildren(
            @PathVariable(name = "id") Long id) {
        return service.findChildren(id)
                .stream()
                .map(this::toDto)
                .toList();
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
