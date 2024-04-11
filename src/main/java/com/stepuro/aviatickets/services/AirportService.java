package com.stepuro.aviatickets.services;

import com.stepuro.aviatickets.api.dto.AirportDto;

import java.util.List;
import java.util.UUID;

public interface AirportService {
    List<AirportDto> findAll();

    AirportDto findById(UUID id);

    AirportDto create(AirportDto airportdto);

    AirportDto edit(AirportDto airportDto);

    void delete(UUID id);
}
