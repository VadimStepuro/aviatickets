package com.stepuro.aviatickets.controllers;

import com.stepuro.aviatickets.api.annotations.Loggable;
import com.stepuro.aviatickets.api.dto.AircompanyDto;
import com.stepuro.aviatickets.api.dto.AircompanyMapper;
import com.stepuro.aviatickets.api.exeptions.ResourceNotFoundException;
import com.stepuro.aviatickets.models.Aircompany;
import com.stepuro.aviatickets.services.AircompanyService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1")
public class AircompanyController {
    @Autowired
    private AircompanyService aircompanyService;

    @Loggable
    @GetMapping("/aircompanies")
    @PreAuthorize("hasAuthority('READ_AIRCOMPANY_PRIVILEGE') || !isAuthenticated()")
    public ResponseEntity<List<AircompanyDto>> getAllAircompanies(){
        List<AircompanyDto> aircompanies = aircompanyService.findAll();
        if(aircompanies.isEmpty())
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        return new ResponseEntity<>(aircompanies, HttpStatus.OK);
    }


    @Loggable
    @GetMapping("/aircompanies/{id}")
    @PreAuthorize("hasAuthority('READ_AIRCOMPANY_PRIVILEGE') || !isAuthenticated()")
    public ResponseEntity<AircompanyDto> getAllAircompanyById(@PathVariable("id") UUID id) {
        AircompanyDto aircompany = aircompanyService.findById(id);

        return new ResponseEntity<>(aircompany, HttpStatus.OK);
    }

    @Loggable
    @PostMapping("/aircompanies")
    @PreAuthorize("hasAuthority('WRITE_AIRCOMPANY_PRIVILEGE')")
    public ResponseEntity<AircompanyDto> createAircompany(@RequestBody @Valid AircompanyDto aircompanyDto){
        aircompanyDto = aircompanyService.create(aircompanyDto);
        return new ResponseEntity<>(aircompanyDto, HttpStatus.CREATED);
    }

    @Loggable
    @PutMapping("/aircompanies")
    @PreAuthorize("hasAuthority('WRITE_AIRCOMPANY_PRIVILEGE')")
    public ResponseEntity<AircompanyDto> updateAircompany(@RequestBody @Valid AircompanyDto aircompanyDto) throws ResourceNotFoundException{
        aircompanyDto = aircompanyService.edit(aircompanyDto);

        return new ResponseEntity<>(aircompanyDto, HttpStatus.OK);
    }

    @Loggable
    @DeleteMapping("/aircompanies/{id}")
    @PreAuthorize("hasAuthority('DELETE_AIRCOMPANY_PRIVILEGE')")
    public void deleteAircompany(@PathVariable("id") UUID id){
        aircompanyService.delete(id);
    }
}
