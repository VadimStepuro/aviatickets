package com.stepuro.aviatickets.controllers;

import com.stepuro.aviatickets.api.annotations.Loggable;
import com.stepuro.aviatickets.api.dto.PrivilegeDto;
import com.stepuro.aviatickets.api.dto.error.ApiError;
import com.stepuro.aviatickets.services.PrivilegeService;
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
public class PrivilegeController {
    @Autowired
    private PrivilegeService privilegeService;

    @Operation(summary = "Get all PrivilegeDtos")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "All found PrivilegeDtos",
                    content = { @Content(mediaType = "application/json",
                            array = @ArraySchema(
                                    schema = @Schema(implementation = PrivilegeDto.class)))}),
            @ApiResponse(responseCode = "204", description = "No PrivilegeDto found",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiError.class))}) })
    @Loggable
    @GetMapping("/privileges")
    @PreAuthorize("hasAuthority('READ_PRIVILEGE_PRIVILEGE')")
    public ResponseEntity<List<PrivilegeDto>> getAllPrivileges(){
        List<PrivilegeDto> privileges = privilegeService.findAll();

        if(privileges.isEmpty())
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);


        return new ResponseEntity<>(privileges, HttpStatus.OK);
    }

    @Operation(summary = "Get PrivilegeDto by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found PrivilegeDto by id",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = PrivilegeDto.class))}),
            @ApiResponse(responseCode = "404", description = "PrivilegeDto not found",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiError.class))}) })
    @Loggable
    @GetMapping("/privileges/{id}")
    @PreAuthorize("hasAuthority('READ_PRIVILEGE_PRIVILEGE')")
    public ResponseEntity<PrivilegeDto> getPrivilegeById(@PathVariable("id") UUID id)  {
        PrivilegeDto privilege = privilegeService.findById(id);

        return new ResponseEntity<>(privilege, HttpStatus.OK);
    }

    @Operation(summary = "Create PrivilegeDto")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Created PrivilegeDto",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = PrivilegeDto.class))}),
            @ApiResponse(responseCode = "400", description = "Invalid PrivilegeDto",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiError.class))}) })
    @Loggable
    @PostMapping("/privileges")
    @PreAuthorize("hasAuthority('WRITE_FLIGHT_PRIVILEGE')")
    public ResponseEntity<PrivilegeDto> createPrivilege(@RequestBody @Valid PrivilegeDto privilegeDto){
        privilegeDto = privilegeService.create(privilegeDto);
        return new ResponseEntity<>(privilegeDto, HttpStatus.CREATED);
    }

    @Operation(summary = "Edit PrivilegeDto")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Edited PrivilegeDto",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = PrivilegeDto.class))}),
            @ApiResponse(responseCode = "400", description = "Invalid PrivilegeDto",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiError.class))}),
            @ApiResponse(responseCode = "404", description = "PrivilegeDto not found",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiError.class))}) })
    @Loggable
    @PutMapping("/privileges")
    @PreAuthorize("hasAuthority('WRITE_FLIGHT_PRIVILEGE')")
    public ResponseEntity<PrivilegeDto> updatePrivilege(@RequestBody @Valid PrivilegeDto privilegeDto){
        privilegeDto = privilegeService.edit(privilegeDto);

        return new ResponseEntity<>(privilegeDto, HttpStatus.OK);
    }

    @Operation(summary = "Delete PrivilegeDto by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Deletes PrivilegeDto by id",
                    content = { @Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "404", description = "PrivilegeDto not found",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiError.class))}) })
    @Loggable
    @DeleteMapping("/privileges/{id}")
    @PreAuthorize("hasAuthority('DELETE_FLIGHT_PRIVILEGE')")
    public void deletePrivilege(@PathVariable("id") UUID id){
        privilegeService.delete(id);
    }
}
