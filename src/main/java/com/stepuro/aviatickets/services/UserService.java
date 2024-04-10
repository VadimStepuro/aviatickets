package com.stepuro.aviatickets.services;

import com.stepuro.aviatickets.api.dto.*;
import com.stepuro.aviatickets.api.exeptions.ResourceNotFoundException;
import com.stepuro.aviatickets.api.mapper.RoleMapper;
import com.stepuro.aviatickets.api.mapper.UserMapper;
import com.stepuro.aviatickets.api.mapper.UserResponseMapper;
import com.stepuro.aviatickets.models.User;
import com.stepuro.aviatickets.repositories.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
public class UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    public List<UserResponse> findAll(){
        return userRepository
                .findAll()
                .stream()
                .map(UserResponseMapper.INSTANCE::userToResponse)
                .collect(Collectors.toList());
    }

    public UserDto getByLogin(String login)  {
        User user = userRepository
                .findByLogin(login)
                .orElseThrow(() -> new ResourceNotFoundException("User with login " + login + " not found"));
        return UserMapper.INSTANCE.userToUserDto(user);
    }

    public UserResponse getByLoginResponse(String login)  {
        User user = userRepository
                .findByLogin(login)
                .orElseThrow(() -> new ResourceNotFoundException("User with login " + login + " not found"));
        return UserResponseMapper.INSTANCE.userToResponse(user);
    }

    public UserResponse findById(UUID id) {
        User user = userRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User with id " + id + " not found"));
        return UserResponseMapper.INSTANCE.userToResponse(user);
    }

    public UserResponse create(UserDto userDto){

        userDto.setPassword(passwordEncoder.encode(userDto.getPassword()));
        User user = userRepository.save(UserMapper.INSTANCE.userDtoToUser(userDto));
        return UserResponseMapper.INSTANCE.userToResponse(user);
    }

    public UserResponse edit(UserDto userDto) {
        User findedUser = userRepository
                .findById(userDto.getId())
                .orElseThrow(() -> new ResourceNotFoundException("User with id " + userDto.getId() + " not found"));
        findedUser.setLogin(userDto.getLogin());
        findedUser.setBirthdate(userDto.getBirthdate());
        findedUser.setPassword(passwordEncoder.encode(userDto.getPassword()));
        findedUser.setName(userDto.getName());
        findedUser.setRoles(userDto
                .getRoles()
                .stream()
                .map(RoleMapper.INSTANCE::roleDtoToRole)
                .collect(Collectors.toList()));
        findedUser.setEmail(userDto.getEmail());
        findedUser = userRepository.save(findedUser);
        return UserResponseMapper.INSTANCE.userToResponse(findedUser);
    }

    public void delete(UUID id){
        userRepository.deleteById(id);
    }
}
