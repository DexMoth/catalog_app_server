package org.catalog_app.controllers;

import org.catalog_app.configurations.Constants;
import org.catalog_app.dtos.CategoryDto;
import org.catalog_app.entities.CategoryEntity;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.web.bind.annotation.*;
import org.catalog_app.repositories.CategoryRepository;
import org.catalog_app.services.CategoryService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(Constants.API_URL + "/category")
public class CategoryController {
    private final CategoryRepository repository;
    private final CategoryService service;
    private final ModelMapper modelMapper;

    public CategoryController(CategoryRepository repository, CategoryService service, ModelMapper modelMapper) {
        this.repository = repository;
        this.service = service;
        this.modelMapper = modelMapper;
    }

    @Transactional
    protected CategoryDto toDto(CategoryEntity ent) {
        var dto = modelMapper.map(ent, CategoryDto.class);
        return dto;
    }

    @Transactional
    protected CategoryEntity toEntity(CategoryDto dto) {
        var ent = modelMapper.map(dto, CategoryEntity.class);
        return ent;
    }


    @PostMapping
    public CategoryDto create(@RequestBody @Valid CategoryDto dto) {
        var ent = new CategoryEntity();
        ent.setName(dto.getName());
        return toDto(repository.save(ent));
    }

    @GetMapping
    public List<CategoryDto> getAll() {
        return service.getAll().stream().map(this::toDto).toList();
    }

    @GetMapping("/{id}")
    public CategoryDto get(@PathVariable(name = "id") Long id) {
        return toDto(service.get(id));
    }

    @PutMapping("/{id}")
    public CategoryDto update(@PathVariable(name = "id") Long id, @RequestBody CategoryDto dto) {
        return toDto(service.update(id, toEntity(dto)));
    }

    @DeleteMapping("/{id}")
    public CategoryDto delete(@PathVariable(name = "id") Long id) {
        return toDto(service.delete(id));
    }
}
