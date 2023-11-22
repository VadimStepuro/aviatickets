package com.stepuro.aviatickets.api.dto;

import com.stepuro.aviatickets.models.Flight;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface FlightMapper {
    FlightMapper INSTANCE = Mappers.getMapper(FlightMapper.class);

    Flight flightDtoToFlight(FlightDto flightDto);

    FlightDto flightToFlightDto(Flight flight);

}
