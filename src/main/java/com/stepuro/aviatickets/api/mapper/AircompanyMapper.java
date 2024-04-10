package com.stepuro.aviatickets.api.mapper;

import com.stepuro.aviatickets.api.dto.AircompanyDto;
import com.stepuro.aviatickets.models.Aircompany;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface AircompanyMapper {

    AircompanyMapper INSTANCE = Mappers.getMapper(AircompanyMapper.class);

    Aircompany aircompanyDtoToAircompany(AircompanyDto aircompanyDto);

    AircompanyDto aircompanyToAircompanyDto(Aircompany aircompany);

}
