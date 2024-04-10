package com.stepuro.aviatickets.controllers;

import com.stepuro.aviatickets.api.annotations.Loggable;
import com.stepuro.aviatickets.api.dto.FindTicketRequest;
import com.stepuro.aviatickets.api.dto.FlightDto;
import com.stepuro.aviatickets.api.dto.TicketDto;
import com.stepuro.aviatickets.api.dto.error.ApiError;
import com.stepuro.aviatickets.models.FlightClass;
import com.stepuro.aviatickets.services.FlightService;
import com.stepuro.aviatickets.services.TicketService;
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
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1")
public class TicketController {
    @Autowired
    private TicketService ticketService;

    
    @Autowired
    private FlightService flightService;
    
    @PostMapping("/tickets/generate")
    public void generate() throws ParseException {
        TicketDto ticketDto = new TicketDto();
        Random random = new Random();
        DecimalFormat decfor = new DecimalFormat("0.00");
        NumberFormat format = NumberFormat.getInstance(Locale.getDefault());
        for (FlightDto flight:flightService.findAll()) {
            ticketDto.setFlight(flight);
            ticketDto.setFlightClass(FlightClass.ECONOMY);
            Number num = format.parse(decfor.format(200 + random.nextDouble() * 2800));
            ticketDto.setCost(num.doubleValue());
            ticketService.create(ticketDto);

            num = format.parse(decfor.format(600 + random.nextDouble() * 4400));
            ticketDto.setCost(num.doubleValue());
            ticketDto.setFlight(flight);
            ticketDto.setFlightClass(FlightClass.BUSINESS);
            ticketService.create(ticketDto);
        }
    }

    @Operation(summary = "Get all TicketDtos")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "All found TicketDtos",
                    content = { @Content(mediaType = "application/json",
                            array = @ArraySchema(
                                    schema = @Schema(implementation = TicketDto.class)))}),
            @ApiResponse(responseCode = "204", description = "No TicketDto found",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiError.class))}) })
    @Loggable
    @GetMapping("/tickets")
    @PreAuthorize("hasAuthority('READ_TICKET_PRIVILEGE') || !isAuthenticated()")
    public ResponseEntity<List<TicketDto>> getAllTickets(){
        List<TicketDto> tickets = ticketService.findAll();

        if(tickets.isEmpty())
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);

        return new ResponseEntity<>(tickets, HttpStatus.OK);
    }

    @Operation(summary = "Get TicketDto by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found TicketDto by id",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = TicketDto.class))}),
            @ApiResponse(responseCode = "404", description = "TicketDto not found",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiError.class))}) })
    @Loggable
    @GetMapping("/tickets/{id}")
    @PostAuthorize("hasAuthority('READ_TICKET_PRIVILEGE') || !isAuthenticated()")
    public ResponseEntity<TicketDto> getTicketById(@PathVariable("id") UUID id)  {
        TicketDto ticketDto = ticketService.findById(id);

        return new ResponseEntity<>(ticketDto, HttpStatus.OK);
    }

    @Operation(summary = "Get all TicketDtos by city and date")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "All found TicketDtos",
                    content = { @Content(mediaType = "application/json",
                            array = @ArraySchema(
                                    schema = @Schema(implementation = TicketDto.class)))}),
            @ApiResponse(responseCode = "204", description = "No TicketDto found",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiError.class))}) })
    @Loggable
    @PostMapping("/tickets/by_cities_and_date")
    @PostAuthorize("hasAuthority('READ_TICKET_PRIVILEGE') || !isAuthenticated()")
    public ResponseEntity<List<TicketDto>> findTicketByCitiesAndDate(@RequestBody @Valid FindTicketRequest request){
        List<TicketDto> ticketDtos = ticketService.findAllByCitiesAndDate(request);

        if(ticketDtos.isEmpty())
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);

        return new ResponseEntity<>(ticketDtos, HttpStatus.OK);
    }

    @Operation(summary = "Create TicketDto")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Created TicketDto",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = TicketDto.class))}),
            @ApiResponse(responseCode = "400", description = "Invalid TicketDto",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiError.class))}) })
    @Loggable
    @PostMapping("/tickets")
    @PostAuthorize("hasAuthority('WRITE_TICKET_PRIVILEGE')")
    public ResponseEntity<TicketDto> createTicket(@RequestBody @Valid TicketDto ticketDto){
        ticketDto = ticketService.create(ticketDto);
        return new ResponseEntity<>(ticketDto, HttpStatus.CREATED);
    }

    @Operation(summary = "Edit TicketDto")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Edited TicketDto",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = TicketDto.class))}),
            @ApiResponse(responseCode = "400", description = "Invalid TicketDto",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiError.class))}),
            @ApiResponse(responseCode = "404", description = "TicketDto not found",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiError.class))}) })
    @Loggable
    @PutMapping("/tickets")
    @PostAuthorize("hasAuthority('WRITE_TICKET_PRIVILEGE')")
    public ResponseEntity<TicketDto> updateTicket(@RequestBody @Valid TicketDto ticketDto) {
        ticketDto = ticketService.edit(ticketDto);

        return new ResponseEntity<>(ticketDto, HttpStatus.OK);
    }

    @Operation(summary = "Delete TicketDto by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Deletes TicketDto by id",
                    content = { @Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "404", description = "TicketDto not found",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiError.class))}) })
    @Loggable
    @DeleteMapping("/tickets/{id}")
    @PostAuthorize("hasAuthority('DELETE_TICKET_PRIVILEGE')")
    public void deleteTicket(@PathVariable("id") UUID id){
        ticketService.delete(id);
    }
}
