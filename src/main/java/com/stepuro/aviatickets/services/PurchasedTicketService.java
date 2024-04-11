package com.stepuro.aviatickets.services;

import com.stepuro.aviatickets.api.dto.PurchasedTicketDto;
import com.stepuro.aviatickets.api.dto.TicketDto;
import com.stepuro.aviatickets.models.Ticket;

import java.util.List;
import java.util.UUID;

public interface PurchasedTicketService {
    List<PurchasedTicketDto> findAll();

    PurchasedTicketDto findById(UUID id);

    List<PurchasedTicketDto> findAllByUserLogin(String login);

    List<PurchasedTicketDto> findAllByTicket(TicketDto ticketDto);

    PurchasedTicketDto create(PurchasedTicketDto purchasedTicketDto);

    boolean isThereFreeSeats(Ticket ticket);

    PurchasedTicketDto edit(PurchasedTicketDto purchasedTicketDto);

    void delete(UUID id);
}
