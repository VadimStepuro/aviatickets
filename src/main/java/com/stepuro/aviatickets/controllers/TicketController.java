package com.stepuro.aviatickets.controllers;

import com.stepuro.aviatickets.api.annotations.Loggable;
import com.stepuro.aviatickets.api.dto.*;
import com.stepuro.aviatickets.api.exeptions.ResourceNotFoundException;
import com.stepuro.aviatickets.models.FlightClass;
import com.stepuro.aviatickets.models.Role;
import com.stepuro.aviatickets.models.Ticket;
import com.stepuro.aviatickets.services.FlightService;
import com.stepuro.aviatickets.services.TicketService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.*;
import java.util.stream.Collectors;

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

        @Loggable
        @GetMapping("/tickets")
        @PreAuthorize("hasAuthority('READ_TICKET_PRIVILEGE') || !isAuthenticated()")
        public ResponseEntity<List<TicketDto>> getAllTickets(){
            List<TicketDto> tickets = ticketService.findAll();

            if(tickets.isEmpty())
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);

            return new ResponseEntity<>(tickets, HttpStatus.OK);
        }

        @Loggable
        @GetMapping("/tickets/{id}")
        @PostAuthorize("hasAuthority('READ_TICKET_PRIVILEGE') || !isAuthenticated()")
        public ResponseEntity<TicketDto> getTicketById(@PathVariable("id") UUID id)  {
            TicketDto ticketDto = ticketService.findById(id);

            return new ResponseEntity<>(ticketDto, HttpStatus.OK);
        }

        @Loggable
        @PostMapping("/tickets/by_cities_and_date")
        @PostAuthorize("hasAuthority('READ_TICKET_PRIVILEGE') || !isAuthenticated()")
        public ResponseEntity<List<TicketDto>> findTicketByCitiesAndDate(@RequestBody @Valid FindTicketRequest request){
            List<TicketDto> ticketDtos = ticketService.findAllByCitiesAndDate(request);

            if(ticketDtos.isEmpty())
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);

            return new ResponseEntity<>(ticketDtos, HttpStatus.OK);
        }
        @Loggable
        @PostMapping("/tickets")
        @PostAuthorize("hasAuthority('WRITE_TICKET_PRIVILEGE')")
        public ResponseEntity<TicketDto> createTicket(@RequestBody @Valid TicketDto ticketDto){
            ticketDto = ticketService.create(ticketDto);
            return new ResponseEntity<>(ticketDto, HttpStatus.CREATED);
        }
        @Loggable
        @PutMapping("/tickets")
        @PostAuthorize("hasAuthority('WRITE_TICKET_PRIVILEGE')")
        public ResponseEntity<TicketDto> updateTicket(@RequestBody @Valid TicketDto ticketDto) {
            ticketDto = ticketService.edit(ticketDto);

            return new ResponseEntity<>(ticketDto, HttpStatus.OK);
        }
        @Loggable
        @DeleteMapping("/tickets/{id}")
        @PostAuthorize("hasAuthority('DELETE_TICKET_PRIVILEGE')")
        public void deleteTicket(@PathVariable("id") UUID id){
            ticketService.delete(id);
        }
}
