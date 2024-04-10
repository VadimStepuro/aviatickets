package com.stepuro.aviatickets.controllers;

import com.stepuro.aviatickets.api.annotations.Loggable;
import com.stepuro.aviatickets.api.dto.FindTicketRequest;
import com.stepuro.aviatickets.api.dto.TicketDto;
import com.stepuro.aviatickets.services.FlightService;
import com.stepuro.aviatickets.services.TicketService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1")
public class TicketController {
        @Autowired
        private TicketService ticketService;

        @Autowired
        private FlightService flightService;

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
