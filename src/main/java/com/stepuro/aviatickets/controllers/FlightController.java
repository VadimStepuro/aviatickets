package com.stepuro.aviatickets.controllers;

import com.stepuro.aviatickets.api.annotations.Loggable;
import com.stepuro.aviatickets.api.dto.FlightDto;
import com.stepuro.aviatickets.api.dto.FlightRequest;
import com.stepuro.aviatickets.models.CityCount;
import com.stepuro.aviatickets.services.AirplaneService;
import com.stepuro.aviatickets.services.AirportService;
import com.stepuro.aviatickets.services.FlightService;
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
public class FlightController {

        @Autowired
        private FlightService flightService;

        @Autowired
        private AirplaneService airplaneService;
        @Autowired
        private AirportService airportService;

        @Loggable
        @GetMapping("/flights")
        @PreAuthorize("hasAuthority('READ_FLIGHT_PRIVILEGE') || !isAuthenticated()")
        public ResponseEntity<List<FlightDto>> getAllFlights(){
            List<FlightDto> flights = flightService.findAll();
            if(flights.isEmpty())
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            return new ResponseEntity<>(flights, HttpStatus.OK);
        }

        @Loggable
        @GetMapping("/flights/{id}")
        @PreAuthorize("hasAuthority('READ_FLIGHT_PRIVILEGE') || !isAuthenticated()")
        public ResponseEntity<FlightDto> getFlightById(@PathVariable("id") UUID id){
            FlightDto flight = flightService.findById(id);

            return new ResponseEntity<>(flight, HttpStatus.OK);
        }

        @Loggable
        @GetMapping("/flights/concrete")
        @PreAuthorize("hasAuthority('READ_FLIGHT_PRIVILEGE') || !isAuthenticated()")
        public ResponseEntity<List<FlightDto>> getFlightsByDepartureAndArrivalAirport(@RequestBody FlightRequest request){
            List<FlightDto> flightDtos = flightService.findAllByDepartureAirportAndArrivalAirport(request.getDepartureAirport(), request.getArrivalAirport());

            if(flightDtos.isEmpty())
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);

            return new ResponseEntity<>(flightDtos, HttpStatus.OK);

        }

        @Loggable
        @GetMapping("/flights/top_cities")
        @PreAuthorize("hasAuthority('READ_FLIGHT_PRIVILEGE') || !isAuthenticated()")
        public ResponseEntity<List<CityCount>> getTopCities(){
            List<CityCount> flightDtos = flightService.findTop10Cities();

            if(flightDtos.isEmpty())
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);

            return new ResponseEntity<>(flightDtos, HttpStatus.OK);
        }

        @Loggable
        @PostMapping("/flights")
        @PreAuthorize("hasAuthority('WRITE_FLIGHT_PRIVILEGE')")
        public ResponseEntity<FlightDto> createFlight(@RequestBody @Valid FlightDto flightDto){
            flightDto = flightService.create(flightDto);
            return new ResponseEntity<>(flightDto, HttpStatus.CREATED);
        }

        @Loggable
        @PutMapping("/flights")
        @PreAuthorize("hasAuthority('WRITE_FLIGHT_PRIVILEGE')")
        public ResponseEntity<FlightDto> updateFlight(@RequestBody @Valid FlightDto flightDto){
            flightDto = flightService.edit(flightDto);

            return new ResponseEntity<>(flightDto, HttpStatus.OK);
        }

        @Loggable
        @DeleteMapping("/flights/{id}")
        @PreAuthorize("hasAuthority('DELETE_FLIGHT_PRIVILEGE')")
        public void deleteFlight(@PathVariable("id") UUID id){
            flightService.delete(id);
        }
}

