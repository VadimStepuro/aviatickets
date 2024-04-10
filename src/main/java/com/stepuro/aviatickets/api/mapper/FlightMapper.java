package com.stepuro.aviatickets.api.mapper;

import com.stepuro.aviatickets.api.dto.FlightDto;
import com.stepuro.aviatickets.models.Flight;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface FlightMapper {
    FlightMapper INSTANCE = Mappers.getMapper(FlightMapper.class);

    Flight flightDtoToFlight(FlightDto flightDto);

    FlightDto flightToFlightDto(Flight flight);

}
