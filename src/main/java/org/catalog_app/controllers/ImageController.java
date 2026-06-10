package org.catalog_app.controllers;

import jakarta.transaction.Transactional;
import org.catalog_app.configurations.Constants;
import org.catalog_app.dtos.ImageDto;
import org.catalog_app.entities.ImageEntity;
import org.catalog_app.repositories.ImageRepository;
import org.catalog_app.services.ImageService;
import org.modelmapper.ModelMapper;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(Constants.API_URL + "/image")
public class ImageController {
    private final ImageRepository repository;
    private final ImageService service;
    private final ModelMapper modelMapper;

    public ImageController(ImageRepository repository, ImageService service, ModelMapper modelMapper) {
        this.repository = repository;
        this.service = service;
        this.modelMapper = modelMapper;
    }

    @Transactional
    private ImageDto toDto(ImageEntity ent) {
        var dto = modelMapper.map(ent, ImageDto.class);
        return dto;
    }

    @Transactional
    private ImageEntity toEntity(ImageDto dto) {
        var ent = modelMapper.map(dto, ImageEntity.class);
        return ent;
    }


    @PostMapping
    public ImageDto create(@RequestBody @Valid ImageDto dto) {
        var ent = new ImageEntity();
        ent.setItemId(dto.getItemId());
        ent.setUserId(dto.getUserId());
        ent.setIsMain(dto.getIsMain());
        ent.setUrl(dto.getUrl());
        ent.setUploadedAt(dto.getUploadedAt());
        return toDto(repository.save(ent));
    }

    @GetMapping
    public List<ImageDto> getAll() {
        return service.getAll().stream().map(this::toDto).toList();
    }

    @GetMapping("/{id}")
    public ImageDto get(@PathVariable(name = "id") Long id) {
        return toDto(service.get(id));
    }

    @PutMapping("/{id}")
    public ImageDto update(@PathVariable(name = "id") Long id, @RequestBody ImageDto dto) {
        return toDto(service.update(id, toEntity(dto)));
    }

    @DeleteMapping("/{id}")
    public ImageDto delete(@PathVariable(name = "id") Long id) {
        return toDto(service.delete(id));
    }
}
