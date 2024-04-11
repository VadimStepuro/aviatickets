package com.stepuro.aviatickets.services;

import com.stepuro.aviatickets.api.dto.PrivilegeDto;

import java.util.List;
import java.util.UUID;

public interface PrivilegeService {
    List<PrivilegeDto> findAll();

    PrivilegeDto findById(UUID id);

    PrivilegeDto create(PrivilegeDto privilegeDto);

    PrivilegeDto edit(PrivilegeDto privilegeDto);

    void delete(UUID id);
}
