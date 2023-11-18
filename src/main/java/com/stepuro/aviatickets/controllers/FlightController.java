package com.stepuro.aviatickets.controllers;

import com.stepuro.aviatickets.api.annotations.Loggable;
import com.stepuro.aviatickets.api.dto.*;
import com.stepuro.aviatickets.api.exeptions.ResourceNotFoundException;
import com.stepuro.aviatickets.models.Airport;
import com.stepuro.aviatickets.models.CityCount;
import com.stepuro.aviatickets.models.Flight;
import com.stepuro.aviatickets.models.FlightClass;
import com.stepuro.aviatickets.services.AirplaneService;
import com.stepuro.aviatickets.services.AirportService;
import com.stepuro.aviatickets.services.FlightService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.ResourceAccessException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1")
public class FlightController {

        @Autowired
        private FlightService flightService;

        @Autowired
        private AirplaneService airplaneService;
        @Autowired
        private AirportService airportService;

        @PostMapping("generate")
        public ResponseEntity<List<FlightDto>> generate(){
            List<AirportDto> airportDtos = airportService.findAll();
            List<AirplaneDto> airplaneDtos = airplaneService.findAll();
            Random random = new Random();
            LocalDateTime startdate = LocalDateTime.now().minusYears(2);
            LocalDateTime endDate = LocalDateTime.now().plusMonths(6);
            ZonedDateTime zdt = ZonedDateTime.of(startdate, ZoneId.systemDefault());
            ZonedDateTime zdt2 = ZonedDateTime.of(endDate, ZoneId.systemDefault());
            long start = zdt.toInstant().toEpochMilli();
            long end = zdt2.toInstant().toEpochMilli();
            FlightDto flightDto = new FlightDto();
            long delta = TimeUnit.HOURS.toMillis(10);
            List<FlightDto> flightDtos = new ArrayList<>();
            for(AirplaneDto airplaneDto: airplaneDtos){
                for(int i = 0; i < airportDtos.size() - 1; i++){
                    long startLong = start + (long) (Math.random() * (end - start));
                    long endLong = startLong + (long) (Math.random() * (delta));
                    flightDto.setAirplane(airplaneDto);
                    flightDto.setDepartureAirport(airportDtos.get(i));
                    flightDto.setArrivalAirport(airportDtos.get(i+1));
                    flightDto.setDepartureDate(new Date(startLong));
                    flightDto.setArrivalDate(new Date(endLong));
                    flightDtos.add(flightService.create(flightDto));
                }
            }
        return new ResponseEntity<>(flightDtos, HttpStatus.OK);
        }

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

