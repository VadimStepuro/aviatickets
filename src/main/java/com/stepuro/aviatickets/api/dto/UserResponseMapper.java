package com.stepuro.aviatickets.api.dto;

import com.stepuro.aviatickets.models.User;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserResponseMapper {
    UserResponseMapper INSTANCE = Mappers.getMapper(UserResponseMapper.class);

    UserResponse userDtoToResponse(UserDto userDto);
    UserDto userResponseToDto(UserResponse userResponse);

    User responseToUser(UserResponse userResponse);

    UserResponse userToResponse(User user);

}
