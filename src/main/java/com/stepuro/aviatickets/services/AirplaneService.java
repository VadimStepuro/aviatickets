package com.stepuro.aviatickets.services;

import com.stepuro.aviatickets.api.dto.AirplaneDto;

import java.util.List;
import java.util.UUID;

public interface AirplaneService {
    List<AirplaneDto> findAll();

    AirplaneDto findById(UUID id);

    AirplaneDto create(AirplaneDto airplaneDto);
    AirplaneDto edit(AirplaneDto airplaneDto);

    void delete(UUID id);
}
