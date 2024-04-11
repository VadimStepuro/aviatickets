package com.stepuro.aviatickets.services;

import com.stepuro.aviatickets.api.dto.RoleDto;

import java.util.List;
import java.util.UUID;

public interface RoleService {
    List<RoleDto> findAll();

    RoleDto findById(UUID id);

    RoleDto findByName(String name);

    RoleDto create(RoleDto roleDto);

    RoleDto edit(RoleDto roleDto);

    void delete(UUID id);
}
