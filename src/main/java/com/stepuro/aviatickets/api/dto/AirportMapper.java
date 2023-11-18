package com.stepuro.aviatickets.api.dto;

import com.stepuro.aviatickets.models.Airport;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface AirportMapper {
    AirportMapper INSTANCE = Mappers.getMapper(AirportMapper.class);

    Airport airportDtoToAirport(AirportDto airportDto);

    AirportDto airportToAirportDto(Airport airport);

}
