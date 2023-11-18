package com.stepuro.aviatickets.api.dto;

import com.stepuro.aviatickets.models.Airplane;
import com.stepuro.aviatickets.models.User;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    User userDtoToUser(UserDto userDto);

    UserDto userToUserDto(User user);

}
