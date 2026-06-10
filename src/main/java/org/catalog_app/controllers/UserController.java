package org.catalog_app.controllers;

import jakarta.transaction.Transactional;
import org.catalog_app.configurations.Constants;
import org.catalog_app.dtos.UserDto;
import org.catalog_app.entities.UserEntity;
import org.catalog_app.repositories.UserRepository;
import org.catalog_app.services.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(Constants.API_URL + "/user")
public class UserController {
    private final UserRepository repository;
    private final UserService service;
    private final ModelMapper modelMapper;

    public UserController(UserRepository repository, UserService service, ModelMapper modelMapper) {
        this.repository = repository;
        this.service = service;
        this.modelMapper = modelMapper;
    }

    @Transactional
    private UserDto toDto(UserEntity ent) {
        var dto = modelMapper.map(ent, UserDto.class);
        return dto;
    }

    @Transactional
    private UserEntity toEntity(UserDto dto) {
        var ent = modelMapper.map(dto, UserEntity.class);
        return ent;
    }


    @PostMapping
    public UserDto create(@RequestBody @Valid UserDto dto) {
        var ent = new UserEntity();
        ent.setGoogleId(dto.getGoogleId());
        ent.setEmail(dto.getEmail());
        ent.setName(dto.getName());
        ent.setAvatarUrl(dto.getAvatarUrl());
        ent.setCreatedAt(dto.getCreatedAt());
        return toDto(repository.save(ent));
    }

    @GetMapping
    public List<UserDto> getAll() {
        return service.getAll().stream().map(this::toDto).toList();
    }

    @GetMapping("/{id}")
    public UserDto get(@PathVariable(name = "id") Long id) {
        return toDto(service.get(id));
    }

    @PutMapping("/{id}")
    public UserDto update(@PathVariable(name = "id") Long id, @RequestBody UserDto dto) {
        return toDto(service.update(id, toEntity(dto)));
    }

    @DeleteMapping("/{id}")
    public UserDto delete(@PathVariable(name = "id") Long id) {
        return toDto(service.delete(id));
    }
}
