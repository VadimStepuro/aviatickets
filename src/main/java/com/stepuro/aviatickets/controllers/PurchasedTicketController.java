package com.stepuro.aviatickets.controllers;

import com.stepuro.aviatickets.api.annotations.Loggable;
import com.stepuro.aviatickets.api.dto.PurchasedTicketDto;
import com.stepuro.aviatickets.api.dto.error.ApiError;
import com.stepuro.aviatickets.services.PurchasedTicketService;
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

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1")
public class PurchasedTicketController {
    @Autowired
    private PurchasedTicketService purchasedTicketService;

    @Operation(summary = "Get all PurchasedTicketDtos")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "All found PurchasedTicketDtos",
                    content = { @Content(mediaType = "application/json",
                            array = @ArraySchema(
                                    schema = @Schema(implementation = PurchasedTicketDto.class)))}),
            @ApiResponse(responseCode = "204", description = "No PurchasedTicketDto found",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiError.class))}) })
    @Loggable
    @GetMapping("/purchased_tickets")
    @PreAuthorize("hasAuthority('READ_USER_PRIVILEGE')")
    public ResponseEntity<List<PurchasedTicketDto>> getAllPurchasedTickets(){
        List<PurchasedTicketDto> purchasedTicketDtos = purchasedTicketService.findAll();

        if(purchasedTicketDtos.isEmpty())
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);

        return new ResponseEntity<>(purchasedTicketDtos, HttpStatus.OK);
    }

    @Operation(summary = "Get PurchasedTicketDto by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found PurchasedTicketDto by id",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = PurchasedTicketDto.class))}),
            @ApiResponse(responseCode = "404", description = "PurchasedTicketDto not found",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiError.class))}) })
    @Loggable
    @GetMapping("/purchased_tickets/{id}")
    @PreAuthorize("isAuthenticated()")
    @PostAuthorize("hasAuthority('READ_PURCHASED_TICKET_PRIVILEGE') || returnObject.getBody().getUser().getLogin() == authentication.principal.getLogin()")
    public ResponseEntity<PurchasedTicketDto> getPurchasedTicketById(@PathVariable("id")UUID uuid){
        PurchasedTicketDto byId = purchasedTicketService.findById(uuid);
        return new ResponseEntity<>(byId, HttpStatus.OK);
    }

    @Operation(summary = "Get PurchasedTicketDto by login")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found PurchasedTicketDto by login",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = PurchasedTicketDto.class))}),
            @ApiResponse(responseCode = "404", description = "PurchasedTicketDto not found",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiError.class))}) })
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

    @Operation(summary = "Create PurchasedTicketDto")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Created PurchasedTicketDto",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = PurchasedTicketDto.class))}),
            @ApiResponse(responseCode = "400", description = "Invalid PurchasedTicketDto",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiError.class))}) })
    @Loggable
    @PostMapping("/purchased_tickets")
    @PreAuthorize("isAuthenticated()")
    @PostAuthorize("hasAuthority('WRITE_PURCHASED_TICKET_PRIVILEGE') || returnObject.getBody().getUser().getLogin() == authentication.principal.getLogin()")
    public ResponseEntity<PurchasedTicketDto> createPurchasedTicket(@RequestBody @Valid PurchasedTicketDto purchasedTicketDto){
        purchasedTicketDto = purchasedTicketService.create(purchasedTicketDto);
        return new ResponseEntity<>(purchasedTicketDto, HttpStatus.OK);
    }

    @Operation(summary = "Edit PurchasedTicketDto")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Edited PurchasedTicketDto",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = PurchasedTicketDto.class))}),
            @ApiResponse(responseCode = "400", description = "Invalid PurchasedTicketDto",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiError.class))}),
            @ApiResponse(responseCode = "404", description = "PurchasedTicketDto not found",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiError.class))}) })
    @Loggable
    @PutMapping("/purchased_tickets")
    @PostAuthorize("hasAuthority('WRITE_PURCHASED_TICKET_PRIVILEGE')")
    public ResponseEntity<PurchasedTicketDto> updatePurchasedTicket(@RequestBody @Valid PurchasedTicketDto purchasedTicketDto){
        purchasedTicketDto = purchasedTicketService.edit(purchasedTicketDto);
        return new ResponseEntity<>(purchasedTicketDto, HttpStatus.OK);
    }

    @Operation(summary = "Delete PurchasedTicketDto by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Deletes PurchasedTicketDto by id",
                    content = { @Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "404", description = "PurchasedTicketDto not found",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiError.class))}) })
    @Loggable
    @DeleteMapping("/purchased_tickets/{id}")
    @PostAuthorize("hasAuthority('DELETE_PURCHASED_TICKET_PRIVILEGE')")
    public void deletePurchasedTicket(@PathVariable("id") UUID id){
        purchasedTicketService.delete(id);
    }

}