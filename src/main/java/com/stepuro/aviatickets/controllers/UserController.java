package com.stepuro.aviatickets.controllers;

import com.stepuro.aviatickets.api.annotations.Loggable;
import com.stepuro.aviatickets.api.dto.*;
import com.stepuro.aviatickets.api.exeptions.ResourceNotFoundException;
import com.stepuro.aviatickets.models.Role;
import com.stepuro.aviatickets.models.User;
import com.stepuro.aviatickets.services.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1")
public class UserController {
        @Autowired
        private UserService userService;

        @Loggable
        @PreAuthorize("hasAuthority('READ_USER_PRIVILEGE')")
        @GetMapping("/users")
        public ResponseEntity<List<UserResponse>> getAllUsers(Authentication authentication){
            List<UserResponse> users = userService.findAll();

            if(users.isEmpty())
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);


            return new ResponseEntity<>(users, HttpStatus.OK);
        }

        @Loggable
        @GetMapping("/users/{id}")
        @PreAuthorize("isAuthenticated()")
        @PostAuthorize("hasAuthority('READ_USER_PRIVILEGE') || returnObject.getBody.getLogin() == authentication.principal.getLogin()")
        public ResponseEntity<UserResponse> getUserById(@PathVariable("id") UUID id) {
            UserResponse userResponse = userService.findById(id);

            return new ResponseEntity<>(userResponse, HttpStatus.OK);
        }

        @Loggable
        @GetMapping("/users/login/{login}")
        @PreAuthorize("isAuthenticated()")
        @PostAuthorize("hasAuthority('READ_USER_PRIVILEGE') || returnObject.getBody.getLogin() == authentication.principal.getLogin()")
        public ResponseEntity<UserResponse> getUserByLogin(@PathVariable("login") String login){
            UserResponse userResponse = userService.getByLoginResponse(login);

            return new ResponseEntity<>(userResponse, HttpStatus.OK);
        }
        @Loggable
        @PreAuthorize("hasAuthority('WRITE_USER_PRIVILEGE')")
        @PostMapping("/users")
        public ResponseEntity<UserResponse> createUser(@RequestBody @Valid UserDto userDto){
            UserResponse userResponse = userService.create(userDto);
            return new ResponseEntity<>(userResponse, HttpStatus.CREATED);
        }

        @Loggable
        @PreAuthorize("hasAuthority('WRITE_USER_PRIVILEGE')")
        @PutMapping("/users")
        public ResponseEntity<UserResponse> updateUsers(@RequestBody @Valid UserDto userDto){
            UserResponse userResponse = userService.edit(userDto);

            return new ResponseEntity<>(userResponse, HttpStatus.OK);
        }

        @Loggable
        @DeleteMapping("/users/{id}")
        @PreAuthorize("hasAuthority('DELETE_USER_PRIVILEGE')")
        public void deleteUsers(@PathVariable("id") UUID id){
            userService.delete(id);
        }
}
