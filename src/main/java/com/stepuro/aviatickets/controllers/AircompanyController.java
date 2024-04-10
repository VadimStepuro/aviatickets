package com.stepuro.aviatickets.controllers;

import com.stepuro.aviatickets.api.annotations.Loggable;
import com.stepuro.aviatickets.api.dto.AircompanyDto;
import com.stepuro.aviatickets.api.dto.error.ApiError;
import com.stepuro.aviatickets.api.exeptions.ResourceNotFoundException;
import com.stepuro.aviatickets.services.AircompanyService;
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
public class AircompanyController {
    @Autowired
    private AircompanyService aircompanyService;

    @Operation(summary = "Get all aircompanyDtos")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "All found aircompanyDtos",
                    content = { @Content(mediaType = "application/json",
                            array = @ArraySchema(
                                    schema = @Schema(implementation = AircompanyDto.class)))}),
            @ApiResponse(responseCode = "204", description = "No aircompanyDto found",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiError.class))}) })
    @Loggable
    @GetMapping("/aircompanies")
    @PreAuthorize("hasAuthority('READ_AIRCOMPANY_PRIVILEGE') || !isAuthenticated()")
    public ResponseEntity<List<AircompanyDto>> getAllAircompanies(){
        List<AircompanyDto> aircompanies = aircompanyService.findAll();
        if(aircompanies.isEmpty())
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        return new ResponseEntity<>(aircompanies, HttpStatus.OK);
    }


    @Operation(summary = "Get aircompanyDto by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found aircompanyDto by id",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = AircompanyDto.class))}),
            @ApiResponse(responseCode = "404", description = "aircompanyDto not found",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiError.class))}) })
    @Loggable
    @GetMapping("/aircompanies/{id}")
    @PreAuthorize("hasAuthority('READ_AIRCOMPANY_PRIVILEGE') || !isAuthenticated()")
    public ResponseEntity<AircompanyDto> getAllAircompanyById(@PathVariable("id") UUID id) {
        AircompanyDto aircompany = aircompanyService.findById(id);

        return new ResponseEntity<>(aircompany, HttpStatus.OK);
    }

    @Operation(summary = "Create aircompanyDto")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Created aircompanyDto",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = AircompanyDto.class))}),
            @ApiResponse(responseCode = "400", description = "Invalid aircompanyDto",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiError.class))}) })
    @Loggable
    @PostMapping("/aircompanies")
    @PreAuthorize("hasAuthority('WRITE_AIRCOMPANY_PRIVILEGE')")
    public ResponseEntity<AircompanyDto> createAircompany(@RequestBody @Valid AircompanyDto aircompanyDto){
        aircompanyDto = aircompanyService.create(aircompanyDto);
        return new ResponseEntity<>(aircompanyDto, HttpStatus.CREATED);
    }

    @Operation(summary = "Edit aircompanyDto")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Edited aircompanyDto",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = AircompanyDto.class))}),
            @ApiResponse(responseCode = "400", description = "Invalid aircompanyDto",
                    content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = ApiError.class))}),
            @ApiResponse(responseCode = "404", description = "aircompanyDto not found",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiError.class))}) })
    @Loggable
    @PutMapping("/aircompanies")
    @PreAuthorize("hasAuthority('WRITE_AIRCOMPANY_PRIVILEGE')")
    public ResponseEntity<AircompanyDto> updateAircompany(@RequestBody @Valid AircompanyDto aircompanyDto) throws ResourceNotFoundException{
        aircompanyDto = aircompanyService.edit(aircompanyDto);

        return new ResponseEntity<>(aircompanyDto, HttpStatus.OK);
    }

    @Operation(summary = "Delete aircompanyDto by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Deletes aircompanyDto by id",
                    content = { @Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "404", description = "aircompanyDto not found",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiError.class))}) })
    @Loggable
    @DeleteMapping("/aircompanies/{id}")
    @PreAuthorize("hasAuthority('DELETE_AIRCOMPANY_PRIVILEGE')")
    public void deleteAircompany(@PathVariable("id") UUID id){
        aircompanyService.delete(id);
    }
}
