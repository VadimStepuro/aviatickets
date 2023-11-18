package com.stepuro.aviatickets.controllers;

import com.stepuro.aviatickets.api.annotations.Loggable;
import com.stepuro.aviatickets.api.dto.AirplaneModelDto;
import com.stepuro.aviatickets.api.dto.AirplaneModelMapper;
import com.stepuro.aviatickets.api.dto.AirportDto;
import com.stepuro.aviatickets.api.dto.AirportMapper;
import com.stepuro.aviatickets.api.exeptions.ResourceNotFoundException;
import com.stepuro.aviatickets.models.AirplaneModel;
import com.stepuro.aviatickets.models.Airport;
import com.stepuro.aviatickets.services.AirportService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1")
public class AirportController {
    @Autowired
    private AirportService airportService;

    @Loggable
    @GetMapping("/airports")
    @PreAuthorize("hasAuthority('READ_AIRPORT_PRIVILEGE') || !isAuthenticated()")
    public ResponseEntity<List<AirportDto>> getAllAirports(){
        List<AirportDto> airports = airportService.findAll();
        if(airports.isEmpty())
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);

        return new ResponseEntity<>(airports, HttpStatus.OK);
    }

    @Loggable
    @GetMapping("/airports/{id}")
    @PreAuthorize("hasAuthority('READ_AIRPORT_PRIVILEGE') || !isAuthenticated()")
    public ResponseEntity<AirportDto> getAirportById(@PathVariable("id") UUID id){
        AirportDto airport = airportService.findById(id);

        return new ResponseEntity<>(airport, HttpStatus.OK);
    }

    @Loggable
    @PostMapping("/airports")
    @PreAuthorize("hasAuthority('WRITE_AIRPORT_PRIVILEGE')")
    public ResponseEntity<AirportDto> createAirport(@RequestBody @Valid AirportDto airportDto){
        airportDto = airportService.create(airportDto);
        return new ResponseEntity<>(airportDto, HttpStatus.CREATED);
    }

    @Loggable
    @PutMapping("/airports")
    @PreAuthorize("hasAuthority('WRITE_AIRPORT_PRIVILEGE')")
    public ResponseEntity<AirportDto> updateAirport(@RequestBody @Valid AirportDto airportDto) {
        airportDto = airportService.edit(airportDto);

        return new ResponseEntity<>(airportDto, HttpStatus.OK);
    }

    @Loggable
    @DeleteMapping("/airports/{id}")
    @PreAuthorize("hasAuthority('DELETE_AIRPORT_PRIVILEGE')")
    public void deleteAirport(@PathVariable("id") UUID id){
        airportService.delete(id);
    }
}
