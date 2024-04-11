package com.stepuro.aviatickets.services;

import com.stepuro.aviatickets.api.dto.AircompanyDto;

import java.util.List;
import java.util.UUID;

public interface AircompanyService {
    List<AircompanyDto> findAll();
    AircompanyDto findById(UUID id);

    AircompanyDto create(AircompanyDto aircompany);

    AircompanyDto edit(AircompanyDto aircompany);

    void delete(UUID id);
}
