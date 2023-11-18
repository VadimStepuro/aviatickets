package com.stepuro.aviatickets.api.dto;

import com.stepuro.aviatickets.models.Aircompany;
import com.stepuro.aviatickets.models.Airplane;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface AirplaneMapper {
    AirplaneMapper INSTANCE = Mappers.getMapper(AirplaneMapper.class);

    Airplane airplaneDtoToAirplane(AirplaneDto airplaneDto);

    AirplaneDto airplaneToAirplaneDto(Airplane airplane);

}
