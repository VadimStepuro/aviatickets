package com.stepuro.aviatickets.services;

import com.stepuro.aviatickets.api.dto.UserDto;
import com.stepuro.aviatickets.api.dto.UserResponse;

import java.util.List;
import java.util.UUID;

public interface UserService {
    List<UserResponse> findAll();

    UserDto getByLogin(String login);
    UserResponse getByLoginResponse(String login);

    UserResponse findById(UUID id);

    UserResponse create(UserDto userDto);

    UserResponse edit(UserDto userDto);

    void delete(UUID id);
}
