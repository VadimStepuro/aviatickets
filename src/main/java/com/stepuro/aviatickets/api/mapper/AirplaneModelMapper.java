package com.stepuro.aviatickets.api.mapper;

import com.stepuro.aviatickets.api.dto.AirplaneModelDto;
import com.stepuro.aviatickets.models.AirplaneModel;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface AirplaneModelMapper {
    AirplaneModelMapper INSTANCE = Mappers.getMapper(AirplaneModelMapper.class);

    AirplaneModel airplaneModelDtoToAirplaneModel(AirplaneModelDto airplaneModelDto);

    AirplaneModelDto airplaneModelToAirplaneModelDto(AirplaneModel airplaneModel);

}
