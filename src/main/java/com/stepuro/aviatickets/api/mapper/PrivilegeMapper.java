package com.stepuro.aviatickets.api.mapper;

import com.stepuro.aviatickets.api.dto.PrivilegeDto;
import com.stepuro.aviatickets.models.Privilege;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface PrivilegeMapper {
    PrivilegeMapper INSTANCE = Mappers.getMapper(PrivilegeMapper.class);

    Privilege privilegeDtoToPrivilege(PrivilegeDto privilegeDto);

    PrivilegeDto privilegeToPrivilegeDto(Privilege privilege);

}
