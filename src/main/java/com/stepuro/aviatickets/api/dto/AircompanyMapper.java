package com.stepuro.aviatickets.api.dto;

import com.stepuro.aviatickets.models.Aircompany;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface AircompanyMapper {

    AircompanyMapper INSTANCE = Mappers.getMapper(AircompanyMapper.class);

    Aircompany aircompanyDtoToAircompany(AircompanyDto aircompanyDto);

    AircompanyDto aircompanyToAircompanyDto(Aircompany aircompany);

}
