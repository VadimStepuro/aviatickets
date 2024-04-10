package com.stepuro.aviatickets.controllers;

import com.stepuro.aviatickets.api.annotations.Loggable;
import com.stepuro.aviatickets.api.dto.AirplaneModelDto;
import com.stepuro.aviatickets.api.dto.error.ApiError;
import com.stepuro.aviatickets.services.AirplaneModelService;
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
public class AirplaneModelController {
    @Autowired
    private AirplaneModelService airplaneModelService;

    @Operation(summary = "Get all AirplaneModelDtos")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "All found AirplaneModelDtos",
                    content = { @Content(mediaType = "application/json",
                            array = @ArraySchema(
                                    schema = @Schema(implementation = AirplaneModelDto.class)))}),
            @ApiResponse(responseCode = "204", description = "No AirplaneModelDto found",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiError.class))}) })
    @Loggable
    @GetMapping("/airplane_models")
    @PreAuthorize("hasAuthority('READ_AIRPLANE_PRIVILEGE') || !isAuthenticated()")
    public ResponseEntity<List<AirplaneModelDto>> getAllAirplaneModels(){
        List<AirplaneModelDto> airplaneModels = airplaneModelService.findAll();
        if(airplaneModels.isEmpty())
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);

        return new ResponseEntity<>(airplaneModels, HttpStatus.OK);
    }

    @Operation(summary = "Get AirplaneModelDto by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found AirplaneModelDto by id",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = AirplaneModelDto.class))}),
            @ApiResponse(responseCode = "404", description = "AirplaneModelDto not found",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiError.class))}) })
    @Loggable
    @GetMapping("/airplane_models/{id}")
    @PreAuthorize("hasAuthority('READ_AIRPLANE_PRIVILEGE') || !isAuthenticated()")
    public ResponseEntity<AirplaneModelDto> getAirplaneModelById(@PathVariable("id") UUID id){
        AirplaneModelDto airplaneModel = airplaneModelService.findById(id);

        return new ResponseEntity<>(airplaneModel, HttpStatus.OK);
    }

    @Operation(summary = "Create AirplaneModelDto")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Created AirplaneModelDto",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = AirplaneModelDto.class))}),
            @ApiResponse(responseCode = "400", description = "Invalid AirplaneModelDto",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiError.class))}) })
    @Loggable
    @PostMapping("/airplane_models")
    @PreAuthorize("hasAuthority('WRITE_AIRPLANE_MODEL_PRIVILEGE')")
    public ResponseEntity<AirplaneModelDto> createAirplaneModel(@RequestBody @Valid AirplaneModelDto airplaneModelDto){
        airplaneModelDto = airplaneModelService.create(airplaneModelDto);

        return new ResponseEntity<>(airplaneModelDto, HttpStatus.CREATED);
    }

    @Operation(summary = "Edit AirplaneModelDto")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Edited AirplaneModelDto",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = AirplaneModelDto.class))}),
            @ApiResponse(responseCode = "400", description = "Invalid AirplaneModelDto",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiError.class))}),
            @ApiResponse(responseCode = "404", description = "AirplaneModelDto not found",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiError.class))}) })
    @Loggable
    @PutMapping("/airplane_models")
    @PreAuthorize("hasAuthority('WRITE_AIRPLANE_MODEL_PRIVILEGE')")
    public ResponseEntity<AirplaneModelDto> updateAirplaneModel(@RequestBody @Valid AirplaneModelDto airplaneModelDto){
        airplaneModelDto = airplaneModelService.edit(airplaneModelDto);

        return new ResponseEntity<>(airplaneModelDto, HttpStatus.OK);
    }
    
    @Operation(summary = "Delete AirplaneModelDto by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Deletes AirplaneModelDto by id",
                    content = {@Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "404", description = "AirplaneModelDto not found",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiError.class))}) })
    @Loggable
    @DeleteMapping("/airplane_models/{id}")
    @PreAuthorize("hasAuthority('DELETE_AIRPLANE_MODEL_PRIVILEGE')")
    public void deleteAirplaneModel(@PathVariable("id") UUID id){
        airplaneModelService.delete(id);
    }
}
