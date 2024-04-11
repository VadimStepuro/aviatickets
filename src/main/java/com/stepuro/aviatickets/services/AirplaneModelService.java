package com.stepuro.aviatickets.services;

import com.stepuro.aviatickets.api.dto.AirplaneModelDto;

import java.util.List;
import java.util.UUID;

public interface AirplaneModelService {
    List<AirplaneModelDto> findAll();

    AirplaneModelDto findById(UUID id);

    AirplaneModelDto create(AirplaneModelDto airplaneModelDto);
    AirplaneModelDto edit(AirplaneModelDto airplaneModel);

    void delete(UUID id);
}
