package com.stepuro.aviatickets.controllers;

import com.stepuro.aviatickets.api.annotations.Loggable;
import com.stepuro.aviatickets.api.dto.AirplaneDto;
import com.stepuro.aviatickets.api.dto.AirportDto;
import com.stepuro.aviatickets.api.dto.FlightDto;
import com.stepuro.aviatickets.api.dto.FlightRequest;
import com.stepuro.aviatickets.api.dto.error.ApiError;
import com.stepuro.aviatickets.models.CityCount;
import com.stepuro.aviatickets.services.AirplaneService;
import com.stepuro.aviatickets.services.AirportService;
import com.stepuro.aviatickets.services.FlightService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.concurrent.TimeUnit;

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
        List<AirportDto> FlightDtos = airportService.findAll();
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
            for(int i = 0; i < FlightDtos.size() - 1; i++){
                long startLong = start + (long) (Math.random() * (end - start));
                long endLong = startLong + (long) (Math.random() * (delta));
                flightDto.setAirplane(airplaneDto);
                flightDto.setDepartureAirport(FlightDtos.get(i));
                flightDto.setArrivalAirport(FlightDtos.get(i+1));
                flightDto.setDepartureDate(new Date(startLong));
                flightDto.setArrivalDate(new Date(endLong));
                flightDtos.add(flightService.create(flightDto));
            }
        }
    return new ResponseEntity<>(flightDtos, HttpStatus.OK);
    }

    @Operation(summary = "Get all FlightDtos")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "All found FlightDtos",
                content = { @Content(mediaType = "application/json",
                        array = @ArraySchema(
                                schema = @Schema(implementation = FlightDto.class)))}),
        @ApiResponse(responseCode = "204", description = "No FlightDto found",
                content = {@Content(mediaType = "application/json",
                        schema = @Schema(implementation = ApiError.class))}) })
    @Loggable
    @GetMapping("/flights")
    @PreAuthorize("hasAuthority('READ_FLIGHT_PRIVILEGE') || !isAuthenticated()")
    public ResponseEntity<List<FlightDto>> getAllFlights(){
        List<FlightDto> flights = flightService.findAll();
        if(flights.isEmpty())
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        return new ResponseEntity<>(flights, HttpStatus.OK);
    }

    @Operation(summary = "Get FlightDto by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found FlightDto by id",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = FlightDto.class))}),
            @ApiResponse(responseCode = "404", description = "FlightDto not found",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiError.class))}) })
    @Loggable   
    @GetMapping("/flights/{id}")
    @PreAuthorize("hasAuthority('READ_FLIGHT_PRIVILEGE') || !isAuthenticated()")
    public ResponseEntity<FlightDto> getFlightById(@PathVariable("id") UUID id){
        FlightDto flight = flightService.findById(id);

        return new ResponseEntity<>(flight, HttpStatus.OK);
    }

    @Operation(summary = "Get FlightDtos by Departure and Arrival Airport")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "All found FlightDtos by Departure and Arrival Airport without transfer",
                    content = { @Content(mediaType = "application/json",
                            array = @ArraySchema(
                                    schema = @Schema(implementation = FlightDto.class)))}),
            @ApiResponse(responseCode = "204", description = "No FlightDto found",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiError.class))}) })
    @Loggable
    @PutMapping("/flights/concrete/no_transfer")
    @PreAuthorize("hasAuthority('READ_FLIGHT_PRIVILEGE') || !isAuthenticated()")
    public ResponseEntity<List<FlightDto>> getFlightsByDepartureAndArrivalAirportWithoutTransfer(@RequestBody FlightRequest request){
        List<FlightDto> flightDtos = flightService.findAllByDepartureAirportAndArrivalAirportWithoutTransfer(request.getDepartureAirport(), request.getArrivalAirport());

        if(flightDtos.isEmpty())
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);

        return new ResponseEntity<>(flightDtos, HttpStatus.OK);

    }

    @Operation(summary = "Get shortest flights path by Departure and Arrival Airport")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Shortest path of FlightDtos by Departure and Arrival Airport",
                    content = { @Content(mediaType = "application/json",
                            array = @ArraySchema(
                                    schema = @Schema(implementation = FlightDto.class)))}),
            @ApiResponse(responseCode = "204", description = "No Path found",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiError.class))}) })
    @Loggable
    @PutMapping("/flights/concrete")
    @PreAuthorize("hasAuthority('READ_FLIGHT_PRIVILEGE') || !isAuthenticated()")
    public ResponseEntity<List<FlightDto>> findShortestFlightsPathByDepartureAndArrivalAirport(@RequestBody @Valid FlightRequest request){
        List<FlightDto> flightDtos = flightService.findShortestFlightsPathByDepartureAndArrivalAirport(request);

        if(flightDtos.isEmpty())
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);

        return new ResponseEntity<>(flightDtos, HttpStatus.OK);

    }

    @Operation(summary = "Get shortest flights path by Departure and Arrival Airport")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Shortest path of FlightDtos by Departure and Arrival Airport",
                    content = { @Content(mediaType = "application/json",
                            array = @ArraySchema(
                                    schema = @Schema(implementation = FlightDto.class)))}),
            @ApiResponse(responseCode = "204", description = "No Path found",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiError.class))}) })
    @Loggable
    @PutMapping("/flights/concrete/weighted")
    @PreAuthorize("hasAuthority('READ_FLIGHT_PRIVILEGE') || !isAuthenticated()")
    public ResponseEntity<List<FlightDto>> findShortestWeightedFlightsPathByDepartureAndArrivalAirport(@RequestBody @Valid FlightRequest request){
        List<FlightDto> flightDtos = flightService.findShortestWeightedFlightsPathByDepartureAndArrivalAirport(request);

        if(flightDtos.isEmpty())
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);

        return new ResponseEntity<>(flightDtos, HttpStatus.OK);

    }

    @Operation(summary = "Get all possible paths with length less than path length")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of Flight paths with length less than path length",
                    content = { @Content(mediaType = "application/json",
                            array = @ArraySchema(
                                    schema = @Schema(implementation = FlightDto.class)))}),
            @ApiResponse(responseCode = "204", description = "No Path found",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiError.class))}) })
    @Loggable
    @PutMapping("/flights/all_paths/length/{pathLength}")
    @PreAuthorize("hasAuthority('READ_FLIGHT_PRIVILEGE') || !isAuthenticated()")
    public ResponseEntity<List<List<FlightDto>>> findAllPathsWhereLengthLessThenPathLength(@RequestBody @Valid FlightRequest request, @PathVariable int pathLength){
        List<List<FlightDto>> flightPaths = flightService.getFlightPathsWhereLengthLessThenPathLength(request, pathLength);

        if(flightPaths.isEmpty())
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);

        return new ResponseEntity<>(flightPaths, HttpStatus.OK);

    }

    @Operation(summary = "Get total flight graph")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Graph image in png",
                    content = { @Content(mediaType = "application/json",
                            array = @ArraySchema(
                                    schema = @Schema(implementation = Byte.class)))}),
            @ApiResponse(responseCode = "204", description = "No Path found",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiError.class))}) })
    @Loggable
    @PostMapping(
            value = "/flights/graph/total",
            produces = MediaType.IMAGE_PNG_VALUE
    )
    @PreAuthorize("hasAuthority('READ_FLIGHT_PRIVILEGE') || !isAuthenticated()")
    public ResponseEntity<byte[]> createTotalFlightGraphImage() throws IOException {
        return new ResponseEntity<>(flightService.createTotalFlightGraphImage(), HttpStatus.CREATED);
    }

    @Operation(summary = "Get part flight graph")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Graph image in png",
                    content = { @Content(mediaType = "application/json",
                            array = @ArraySchema(
                                    schema = @Schema(implementation = Byte.class)))}),
            @ApiResponse(responseCode = "204", description = "No Path found",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiError.class))}) })
    @Loggable
    @PutMapping(
            value = "/flights/graph/part",
            produces = MediaType.IMAGE_PNG_VALUE)
    @PreAuthorize("hasAuthority('READ_FLIGHT_PRIVILEGE') || !isAuthenticated()")
    public ResponseEntity<byte[]> createFlightImage(@RequestBody @Valid FlightRequest request) throws IOException {
        return new ResponseEntity<>(flightService.createFlightGraphImage(request), HttpStatus.CREATED);
    }

    @Operation(summary = "Get top 10 FlightDtos by popularity")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Top 10 FlightDtos by popularity",
                    content = { @Content(mediaType = "application/json",
                            array = @ArraySchema(
                                    schema = @Schema(implementation = FlightDto.class)))}),
            @ApiResponse(responseCode = "204", description = "No FlightDto found",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiError.class))}) })
    @Loggable
    @GetMapping("/flights/top_cities")
    @PreAuthorize("hasAuthority('READ_FLIGHT_PRIVILEGE') || !isAuthenticated()")
    public ResponseEntity<List<CityCount>> getTopCities(){
        List<CityCount> flightDtos = flightService.findTop10Cities();

        if(flightDtos.isEmpty())
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);

        return new ResponseEntity<>(flightDtos, HttpStatus.OK);
    }

    @Operation(summary = "Create FlightDto")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Created FlightDto",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = FlightDto.class))}),
            @ApiResponse(responseCode = "400", description = "Invalid FlightDto",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiError.class))}) })
    @Loggable
    @PostMapping("/flights")
    @PreAuthorize("hasAuthority('WRITE_FLIGHT_PRIVILEGE')")
    public ResponseEntity<FlightDto> createFlight(@RequestBody @Valid FlightDto flightDto){
        flightDto = flightService.create(flightDto);
        return new ResponseEntity<>(flightDto, HttpStatus.CREATED);
    }

    @Operation(summary = "Edit FlightDto")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Edited FlightDto",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = FlightDto.class))}),
            @ApiResponse(responseCode = "400", description = "Invalid FlightDto",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiError.class))}),
            @ApiResponse(responseCode = "404", description = "FlightDto not found",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiError.class))}) })
    @Loggable
    @PutMapping("/flights")
    @PreAuthorize("hasAuthority('WRITE_FLIGHT_PRIVILEGE')")
    public ResponseEntity<FlightDto> updateFlight(@RequestBody @Valid FlightDto flightDto){
        flightDto = flightService.edit(flightDto);

        return new ResponseEntity<>(flightDto, HttpStatus.OK);
    }

    @Operation(summary = "Delete FlightDto by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Deletes FlightDto by id",
                    content = { @Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "404", description = "FlightDto not found",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiError.class))}) })
    @Loggable
    @DeleteMapping("/flights/{id}")
    @PreAuthorize("hasAuthority('DELETE_FLIGHT_PRIVILEGE')")
    public void deleteFlight(@PathVariable("id") UUID id){
        flightService.delete(id);
    }
}

