package com.stepuro.aviatickets.controllers;

import com.stepuro.aviatickets.api.annotations.Loggable;
import com.stepuro.aviatickets.api.dto.PurchasedTicketDto;
import com.stepuro.aviatickets.api.dto.TicketDto;
import com.stepuro.aviatickets.services.PurchasedTicketService;
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
public class PurchasedTicketController {
    @Autowired
    private PurchasedTicketService purchasedTicketService;

    @Loggable
    @GetMapping("/purchased_tickets")
    @PreAuthorize("hasAuthority('READ_USER_PRIVILEGE')")
    public ResponseEntity<List<PurchasedTicketDto>> getAllPurchasedTickets(){
        List<PurchasedTicketDto> purchasedTicketDtos = purchasedTicketService.findAll();

        if(purchasedTicketDtos.isEmpty())
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);

        return new ResponseEntity<>(purchasedTicketDtos, HttpStatus.OK);
    }


    @Loggable
    @GetMapping("/purchased_tickets/{id}")
    @PreAuthorize("isAuthenticated()")
    @PostAuthorize("hasAuthority('READ_PURCHASED_TICKET_PRIVILEGE') || returnObject.getBody().getUser().getLogin() == authentication.principal.getLogin()")
    public ResponseEntity<PurchasedTicketDto> getPurchasedTicketById(@PathVariable("id")UUID uuid){
        PurchasedTicketDto byId = purchasedTicketService.findById(uuid);
        return new ResponseEntity<>(byId, HttpStatus.OK);
    }

    @Loggable
    @GetMapping("/purchased_tickets/by_login/{login}")
    @PreAuthorize("isAuthenticated()")
    @PostAuthorize("hasAuthority('READ_PURCHASED_TICKET_PRIVILEGE') || #login == authentication.principal.getLogin()")
    public ResponseEntity<List<PurchasedTicketDto>> getPurchasedTicketsByUserLogin(@PathVariable("login") String login){
        List<PurchasedTicketDto> purchasedTicketDtos = purchasedTicketService.findAllByUserLogin(login);

        if(purchasedTicketDtos.isEmpty())
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);

        return new ResponseEntity<>(purchasedTicketDtos, HttpStatus.OK);
     }

    @Loggable
    @PostMapping("/purchased_tickets")
    @PreAuthorize("isAuthenticated()")
    @PostAuthorize("hasAuthority('WRITE_PURCHASED_TICKET_PRIVILEGE') || returnObject.getBody().getUser().getLogin() == authentication.principal.getLogin()")
    public ResponseEntity<PurchasedTicketDto> createPurchasedTicket(@RequestBody @Valid PurchasedTicketDto purchasedTicketDto){
        purchasedTicketDto = purchasedTicketService.create(purchasedTicketDto);
        return new ResponseEntity<>(purchasedTicketDto, HttpStatus.OK);
    }

    @Loggable
    @PutMapping("/purchased_tickets")
    @PostAuthorize("hasAuthority('WRITE_PURCHASED_TICKET_PRIVILEGE')")
    public ResponseEntity<PurchasedTicketDto> updatePurchasedTicket(@RequestBody @Valid PurchasedTicketDto purchasedTicketDto){
        purchasedTicketDto = purchasedTicketService.edit(purchasedTicketDto);
        return new ResponseEntity<>(purchasedTicketDto, HttpStatus.OK);
    }

    @Loggable
    @DeleteMapping("/purchased_tickets/{id}")
    @PostAuthorize("hasAuthority('DELETE_PURCHASED_TICKET_PRIVILEGE')")
    public void deletePurchasedTicket(@PathVariable("id") UUID id){
        purchasedTicketService.delete(id);
    }

}