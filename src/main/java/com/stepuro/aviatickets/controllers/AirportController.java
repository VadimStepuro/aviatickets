package com.stepuro.aviatickets.controllers;

import com.stepuro.aviatickets.api.annotations.Loggable;
import com.stepuro.aviatickets.api.dto.AirportDto;
import com.stepuro.aviatickets.api.dto.error.ApiError;
import com.stepuro.aviatickets.services.AirportService;
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
public class AirportController {
    @Autowired
    private AirportService airportService;

    @Operation(summary = "Get all AirportDtos")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "All found AirportDtos",
                    content = { @Content(mediaType = "application/json",
                            array = @ArraySchema(
                                    schema = @Schema(implementation = AirportDto.class)))}),
            @ApiResponse(responseCode = "204", description = "No AirportDto found",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiError.class))}) })
    @Loggable
    @GetMapping("/airports")
    @PreAuthorize("hasAuthority('READ_AIRPORT_PRIVILEGE') || !isAuthenticated()")
    public ResponseEntity<List<AirportDto>> getAllAirports(){
        List<AirportDto> airports = airportService.findAll();
        if(airports.isEmpty())
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);

        return new ResponseEntity<>(airports, HttpStatus.OK);
    }

    @Operation(summary = "Get AirportDto by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found AirportDto by id",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = AirportDto.class))}),
            @ApiResponse(responseCode = "404", description = "AirportDto not found",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiError.class))}) })
    @Loggable
    @GetMapping("/airports/{id}")
    @PreAuthorize("hasAuthority('READ_AIRPORT_PRIVILEGE') || !isAuthenticated()")
    public ResponseEntity<AirportDto> getAirportById(@PathVariable("id") UUID id){
        AirportDto airport = airportService.findById(id);

        return new ResponseEntity<>(airport, HttpStatus.OK);
    }

    @Operation(summary = "Create AirportDto")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Created AirportDto",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = AirportDto.class))}),
            @ApiResponse(responseCode = "400", description = "Invalid AirportDto",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiError.class))}) })
    @Loggable
    @PostMapping("/airports")
    @PreAuthorize("hasAuthority('WRITE_AIRPORT_PRIVILEGE')")
    public ResponseEntity<AirportDto> createAirport(@RequestBody @Valid AirportDto airportDto){
        airportDto = airportService.create(airportDto);
        return new ResponseEntity<>(airportDto, HttpStatus.CREATED);
    }

    @Operation(summary = "Edit AirportDto")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Edited AirportDto",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = AirportDto.class))}),
            @ApiResponse(responseCode = "400", description = "Invalid AirportDto",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiError.class))}),
            @ApiResponse(responseCode = "404", description = "AirportDto not found",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiError.class))}) })
    @Loggable
    @PutMapping("/airports")
    @PreAuthorize("hasAuthority('WRITE_AIRPORT_PRIVILEGE')")
    public ResponseEntity<AirportDto> updateAirport(@RequestBody @Valid AirportDto airportDto) {
        airportDto = airportService.edit(airportDto);

        return new ResponseEntity<>(airportDto, HttpStatus.OK);
    }

    @Operation(summary = "Delete AirportDto by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Deletes AirportDto by id",
                    content = { @Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "404", description = "AirportDto not found",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiError.class))}) })
    @Loggable
    @DeleteMapping("/airports/{id}")
    @PreAuthorize("hasAuthority('DELETE_AIRPORT_PRIVILEGE')")
    public void deleteAirport(@PathVariable("id") UUID id){
        airportService.delete(id);
    }
}
