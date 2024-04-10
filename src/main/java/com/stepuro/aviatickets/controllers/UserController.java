package com.stepuro.aviatickets.controllers;

import com.stepuro.aviatickets.api.annotations.Loggable;
import com.stepuro.aviatickets.api.dto.UserDto;
import com.stepuro.aviatickets.api.dto.UserResponse;
import com.stepuro.aviatickets.api.dto.error.ApiError;
import com.stepuro.aviatickets.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1")
public class UserController {
    @Autowired
    private UserService userService;

    @Operation(summary = "Get all UserDtos")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "All found UserDtos",
                    content = { @Content(mediaType = "application/json",
                            array = @ArraySchema(
                                    schema = @Schema(implementation = UserDto.class)))}),
            @ApiResponse(responseCode = "204", description = "No UserDto found",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiError.class))}) })
    @Loggable
    @PreAuthorize("hasAuthority('READ_USER_PRIVILEGE')")
    @GetMapping("/users")
    public ResponseEntity<List<UserResponse>> getAllUsers(Authentication authentication){
        List<UserResponse> users = userService.findAll();

        if(users.isEmpty())
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);


        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @Operation(summary = "Get UserDto by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found UserDto by id",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserDto.class))}),
            @ApiResponse(responseCode = "404", description = "UserDto not found",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiError.class))}) })
    @Loggable
    @GetMapping("/users/{id}")
    @PreAuthorize("isAuthenticated()")
    @PostAuthorize("hasAuthority('READ_USER_PRIVILEGE') || returnObject.getBody.getLogin() == authentication.principal.getLogin()")
    public ResponseEntity<UserResponse> getUserById(@PathVariable("id") UUID id) {
        UserResponse userResponse = userService.findById(id);

        return new ResponseEntity<>(userResponse, HttpStatus.OK);
    }

    @Operation(summary = "Get UserDto by login")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found UserDto by login",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserDto.class))}),
            @ApiResponse(responseCode = "404", description = "UserDto not found",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiError.class))}) })
    @Loggable
    @GetMapping("/users/login/{login}")
    @PreAuthorize("isAuthenticated()")
    @PostAuthorize("hasAuthority('READ_USER_PRIVILEGE') || returnObject.getBody.getLogin() == authentication.principal.getLogin()")
    public ResponseEntity<UserResponse> getUserByLogin(@PathVariable("login") String login){
        UserResponse userResponse = userService.getByLoginResponse(login);

        return new ResponseEntity<>(userResponse, HttpStatus.OK);
    }
    
    @Operation(summary = "Create UserDto")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Created UserDto",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserDto.class))}),
            @ApiResponse(responseCode = "400", description = "Invalid UserDto",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiError.class))}) })
    @Loggable
    @PreAuthorize("hasAuthority('WRITE_USER_PRIVILEGE')")
    @PostMapping("/users")
    public ResponseEntity<UserResponse> createUser(@RequestBody @Valid UserDto userDto){
        UserResponse userResponse = userService.create(userDto);
        return new ResponseEntity<>(userResponse, HttpStatus.CREATED);
    }

    @Operation(summary = "Edit UserDto")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Edited UserDto",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserDto.class))}),
            @ApiResponse(responseCode = "400", description = "Invalid UserDto",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiError.class))}),
            @ApiResponse(responseCode = "404", description = "UserDto not found",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiError.class))}) })
    @Loggable
    @PreAuthorize("hasAuthority('WRITE_USER_PRIVILEGE')")
    @PutMapping("/users")
    public ResponseEntity<UserResponse> updateUsers(@RequestBody @Valid UserDto userDto){
        UserResponse userResponse = userService.edit(userDto);

        return new ResponseEntity<>(userResponse, HttpStatus.OK);
    }

    @Operation(summary = "Delete UserDto by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Deletes UserDto by id",
                    content = { @Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "404", description = "UserDto not found",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiError.class))}) })
    @Loggable
    @DeleteMapping("/users/{id}")
    @PreAuthorize("hasAuthority('DELETE_USER_PRIVILEGE')")
    public void deleteUsers(@PathVariable("id") UUID id){
        userService.delete(id);
    }
}
