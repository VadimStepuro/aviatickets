package com.stepuro.aviatickets.controllers;

import com.stepuro.aviatickets.api.annotations.Loggable;
import com.stepuro.aviatickets.api.dto.AirplaneDto;
import com.stepuro.aviatickets.api.dto.error.ApiError;
import com.stepuro.aviatickets.services.AircompanyService;
import com.stepuro.aviatickets.services.AirplaneModelService;
import com.stepuro.aviatickets.services.AirplaneService;
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
public class AirplaneController {
    @Autowired
    private AirplaneService airplaneService;

    @Autowired
    private AirplaneModelService airplaneModelService;

    @Autowired
    private AircompanyService aircompanyService;

    @Operation(summary = "Get all AirplaneDtos")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "All found AirplaneDtos",
                    content = { @Content(mediaType = "application/json",
                            array = @ArraySchema(
                                    schema = @Schema(implementation = AirplaneDto.class)))}),
            @ApiResponse(responseCode = "204", description = "No AirplaneDto found",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiError.class))}) })
    @Loggable
    @GetMapping("/airplanes")
    @PreAuthorize("hasAuthority('READ_AIRPLANE_PRIVILEGE') || !isAuthenticated()")
    public ResponseEntity<List<AirplaneDto>> getAllAirplanes(){
        List<AirplaneDto> airplanes = airplaneService.findAll();

        if(airplanes.isEmpty())
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);

        return new ResponseEntity<>(airplanes, HttpStatus.OK);
    }

    @Operation(summary = "Get AirplaneDto by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found AirplaneDto by id",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = AirplaneDto.class))}),
            @ApiResponse(responseCode = "404", description = "AirplaneDto not found",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiError.class))}) })
    @Loggable
    @GetMapping("/airplanes/{id}")
    @PreAuthorize("hasAuthority('READ_AIRPLANE_PRIVILEGE') || !isAuthenticated()")
    public ResponseEntity<AirplaneDto> getAirplaneById(@PathVariable("id") UUID id) {
        AirplaneDto airplane = airplaneService.findById(id);

        return new ResponseEntity<>(airplane, HttpStatus.OK);
    }

    @Operation(summary = "Create AirplaneDto")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Created AirplaneDto",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = AirplaneDto.class))}),
            @ApiResponse(responseCode = "400", description = "Invalid AirplaneDto",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiError.class))}) })
    @Loggable
    @PostMapping("/airplanes")
    @PreAuthorize("hasAuthority('WRITE_AIRPLANE_PRIVILEGE')")
    public ResponseEntity<AirplaneDto> createAirplane(@RequestBody @Valid AirplaneDto airplaneDto){
        airplaneDto = airplaneService.create(airplaneDto);
        return new ResponseEntity<>(airplaneDto, HttpStatus.CREATED);
    }

    @Operation(summary = "Edit AirplaneDto")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Edited AirplaneDto",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = AirplaneDto.class))}),
            @ApiResponse(responseCode = "400", description = "Invalid AirplaneDto",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiError.class))}),
            @ApiResponse(responseCode = "404", description = "AirplaneDto not found",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiError.class))}) })
    @Loggable
    @PutMapping("/airplanes")
    @PreAuthorize("hasAuthority('WRITE_AIRPLANE_PRIVILEGE')")
    public ResponseEntity<AirplaneDto> updateAirplane(@RequestBody @Valid AirplaneDto airplaneDto) {
        airplaneDto = airplaneService.edit(airplaneDto);

        return new ResponseEntity<>(airplaneDto, HttpStatus.OK);
    }

    @Operation(summary = "Delete AirplaneDto by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Deletes AirplaneDto by id",
                    content = {@Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "404", description = "AirplaneDto not found",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiError.class))}) })
    @Loggable
    @DeleteMapping("/airplanes/{id}")
    @PreAuthorize("hasAuthority('DELETE_AIRPLANE_PRIVILEGE')")
    public void deleteAirplane(@PathVariable("id") UUID id){
        airplaneService.delete(id);
    }
}
