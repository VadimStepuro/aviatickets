package com.stepuro.aviatickets.api.mapper;

import com.stepuro.aviatickets.api.dto.UserDto;
import com.stepuro.aviatickets.models.User;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    User userDtoToUser(UserDto userDto);

    UserDto userToUserDto(User user);

}
