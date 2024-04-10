package com.stepuro.aviatickets.controllers;

import com.stepuro.aviatickets.api.annotations.Loggable;
import com.stepuro.aviatickets.api.dto.RoleDto;
import com.stepuro.aviatickets.api.dto.error.ApiError;
import com.stepuro.aviatickets.services.RoleService;
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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1")
public class RoleController {

    @Autowired
    private RoleService roleService;

    @Operation(summary = "Get all RoleDtos")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "All found RoleDtos",
                    content = { @Content(mediaType = "application/json",
                            array = @ArraySchema(
                                    schema = @Schema(implementation = RoleDto.class)))}),
            @ApiResponse(responseCode = "204", description = "No RoleDto found",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiError.class))}) })
    @Loggable
    @GetMapping("/roles")
    @PreAuthorize("hasAuthority('READ_ROLE_PRIVILEGE')")
    public ResponseEntity<List<RoleDto>> getAllRoles(){
        List<RoleDto> roles = roleService.findAll();

        if(roles.isEmpty())
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);


        return new ResponseEntity<>(roles, HttpStatus.OK);
    }

    @Operation(summary = "Get RoleDto by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found RoleDto by id",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = RoleDto.class))}),
            @ApiResponse(responseCode = "404", description = "RoleDto not found",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiError.class))}) })
    @Loggable
    @GetMapping("/roles/{id}")
    @PreAuthorize("hasAuthority('READ_ROLE_PRIVILEGE')")
    public ResponseEntity<RoleDto> getRoleById(@PathVariable("id") UUID id)  {
        RoleDto roleDto = roleService.findById(id);

        return new ResponseEntity<>(roleDto, HttpStatus.OK);
    }

    @Operation(summary = "Create RoleDto")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Created RoleDto",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = RoleDto.class))}),
            @ApiResponse(responseCode = "400", description = "Invalid RoleDto",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiError.class))}) })
    @Loggable
    @PostMapping("/roles")
    @PreAuthorize("hasAuthority('WRITE_ROLE_PRIVILEGE')")
    public ResponseEntity<RoleDto> createRole(@RequestBody @Valid RoleDto roleDto){
        roleDto = roleService.create(roleDto);
        return new ResponseEntity<>(roleDto, HttpStatus.CREATED);
    }

    @Operation(summary = "Edit RoleDto")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Edited RoleDto",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = RoleDto.class))}),
            @ApiResponse(responseCode = "400", description = "Invalid RoleDto",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiError.class))}),
            @ApiResponse(responseCode = "404", description = "RoleDto not found",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiError.class))}) })
    @Loggable
    @PutMapping("/roles")
    @PreAuthorize("hasAuthority('WRITE_ROLE_PRIVILEGE')")
    public ResponseEntity<RoleDto> updateRoles(@RequestBody @Valid RoleDto roleDto) {
        roleDto = roleService.edit(roleDto);

        return new ResponseEntity<>(roleDto, HttpStatus.OK);
    }

    @Operation(summary = "Delete RoleDto by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Deletes RoleDto by id",
                    content = { @Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "404", description = "RoleDto not found",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiError.class))}) })
    @Loggable
    @DeleteMapping("/roles/{id}")
    @PreAuthorize("hasAuthority('DELETE_ROLE_PRIVILEGE')")
    public void deleteRoles(@PathVariable("id") UUID id){
            roleService.delete(id);
        }
}
