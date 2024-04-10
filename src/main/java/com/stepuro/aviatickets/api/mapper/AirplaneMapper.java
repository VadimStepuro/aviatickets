package com.stepuro.aviatickets.api.mapper;

import com.stepuro.aviatickets.api.dto.AirplaneDto;
import com.stepuro.aviatickets.models.Airplane;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface AirplaneMapper {
    AirplaneMapper INSTANCE = Mappers.getMapper(AirplaneMapper.class);

    Airplane airplaneDtoToAirplane(AirplaneDto airplaneDto);

    AirplaneDto airplaneToAirplaneDto(Airplane airplane);

}
