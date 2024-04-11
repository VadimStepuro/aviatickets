package com.stepuro.aviatickets.services;

import com.stepuro.aviatickets.api.dto.FindTicketRequest;
import com.stepuro.aviatickets.api.dto.TicketDto;

import java.util.List;
import java.util.UUID;

public interface TicketService {
    List<TicketDto> findAll();
    TicketDto findById(UUID id);

    List<TicketDto> findAllByCitiesAndDate(FindTicketRequest request);

    TicketDto create(TicketDto ticketDto);

    TicketDto edit(TicketDto ticketDto);

    void delete(UUID id);
}
