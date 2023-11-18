package com.stepuro.aviatickets.controllers;

import com.stepuro.aviatickets.api.annotations.Loggable;
import com.stepuro.aviatickets.api.dto.AircompanyDto;
import com.stepuro.aviatickets.api.dto.AirplaneDto;
import com.stepuro.aviatickets.api.dto.AirplaneMapper;
import com.stepuro.aviatickets.api.dto.AirplaneModelDto;
import com.stepuro.aviatickets.api.exeptions.ResourceNotFoundException;
import com.stepuro.aviatickets.models.Airplane;
import com.stepuro.aviatickets.services.AircompanyService;
import com.stepuro.aviatickets.services.AirplaneModelService;
import com.stepuro.aviatickets.services.AirplaneService;
import jakarta.validation.Valid;
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
public class AirplaneController {
    @Autowired
    private AirplaneService airplaneService;

    @Autowired
    private AirplaneModelService airplaneModelService;

    @Autowired
    private AircompanyService aircompanyService;

    @Loggable
    @GetMapping("/airplanes")
    @PreAuthorize("hasAuthority('READ_AIRPLANE_PRIVILEGE') || !isAuthenticated()")
    public ResponseEntity<List<AirplaneDto>> getAllAirplanes(){
        List<AirplaneDto> airplanes = airplaneService.findAll();

        if(airplanes.isEmpty())
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);

        return new ResponseEntity<>(airplanes, HttpStatus.OK);
    }

    @Loggable
    @GetMapping("/airplanes/{id}")
    @PreAuthorize("hasAuthority('READ_AIRPLANE_PRIVILEGE') || !isAuthenticated()")
    public ResponseEntity<AirplaneDto> getAirplaneById(@PathVariable("id") UUID id) {
        AirplaneDto airplane = airplaneService.findById(id);

        return new ResponseEntity<>(airplane, HttpStatus.OK);
    }

    @Loggable
    @PostMapping("/airplanes")
    @PreAuthorize("hasAuthority('WRITE_AIRPLANE_PRIVILEGE')")
    public ResponseEntity<AirplaneDto> createAirplane(@RequestBody @Valid AirplaneDto airplaneDto){
        airplaneDto = airplaneService.create(airplaneDto);
        return new ResponseEntity<>(airplaneDto, HttpStatus.CREATED);
    }

    @Loggable
    @PutMapping("/airplanes")
    @PreAuthorize("hasAuthority('WRITE_AIRPLANE_PRIVILEGE')")
    public ResponseEntity<AirplaneDto> updateAirplane(@RequestBody @Valid AirplaneDto airplaneDto) {
        airplaneDto = airplaneService.edit(airplaneDto);

        return new ResponseEntity<>(airplaneDto, HttpStatus.OK);
    }

    @Loggable
    @DeleteMapping("/airplanes/{id}")
    @PreAuthorize("hasAuthority('DELETE_AIRPLANE_PRIVILEGE')")
    public void deleteAirplane(@PathVariable("id") UUID id){
        airplaneService.delete(id);
    }
}
