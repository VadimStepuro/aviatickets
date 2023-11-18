package com.stepuro.aviatickets.controllers;

import com.stepuro.aviatickets.api.annotations.Loggable;
import com.stepuro.aviatickets.api.dto.AirplaneDto;
import com.stepuro.aviatickets.api.dto.AirplaneMapper;
import com.stepuro.aviatickets.api.dto.AirplaneModelDto;
import com.stepuro.aviatickets.api.dto.AirplaneModelMapper;
import com.stepuro.aviatickets.api.exeptions.ResourceNotFoundException;
import com.stepuro.aviatickets.models.Airplane;
import com.stepuro.aviatickets.models.AirplaneModel;
import com.stepuro.aviatickets.services.AirplaneModelService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.ResourceAccessException;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1")
public class AirplaneModelController {
    @Autowired
    private AirplaneModelService airplaneModelService;

    @Loggable
    @GetMapping("/airplane_models")
    @PreAuthorize("hasAuthority('READ_AIRPLANE_PRIVILEGE') || !isAuthenticated()")
    public ResponseEntity<List<AirplaneModelDto>> getAllAirplaneModels(){
        List<AirplaneModelDto> airplaneModels = airplaneModelService.findAll();
        if(airplaneModels.isEmpty())
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);

        return new ResponseEntity<>(airplaneModels, HttpStatus.OK);
    }

    @Loggable
    @GetMapping("/airplane_models/{id}")
    @PreAuthorize("hasAuthority('READ_AIRPLANE_PRIVILEGE') || !isAuthenticated()")
    public ResponseEntity<AirplaneModelDto> getAirplaneModelById(@PathVariable("id") UUID id){
        AirplaneModelDto airplaneModel = airplaneModelService.findById(id);

        return new ResponseEntity<>(airplaneModel, HttpStatus.OK);
    }

    @Loggable
    @PostMapping("/airplane_models")
    @PreAuthorize("hasAuthority('WRITE_AIRPLANE_MODEL_PRIVILEGE')")
    public ResponseEntity<AirplaneModelDto> createAirplaneModel(@RequestBody @Valid AirplaneModelDto airplaneModelDto){
        airplaneModelDto = airplaneModelService.create(airplaneModelDto);

        return new ResponseEntity<>(airplaneModelDto, HttpStatus.CREATED);
    }

    @Loggable
    @PutMapping("/airplane_models")
    @PreAuthorize("hasAuthority('WRITE_AIRPLANE_MODEL_PRIVILEGE')")
    public ResponseEntity<AirplaneModelDto> updateAirplaneModel(@RequestBody @Valid AirplaneModelDto airplaneModelDto){
        airplaneModelDto = airplaneModelService.edit(airplaneModelDto);

        return new ResponseEntity<>(airplaneModelDto, HttpStatus.OK);
    }

    @Loggable
    @DeleteMapping("/airplane_models/{id}")
    @PreAuthorize("hasAuthority('DELETE_AIRPLANE_MODEL_PRIVILEGE')")
    public void deleteAirplaneModel(@PathVariable("id") UUID id){
        airplaneModelService.delete(id);
    }
}
