package com.stepuro.aviatickets.api.dto;

import com.stepuro.aviatickets.models.Airplane;
import com.stepuro.aviatickets.models.Ticket;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Component;

@Mapper
public interface TicketMapper {
    TicketMapper INSTANCE = Mappers.getMapper(TicketMapper.class);

    Ticket ticketDtoToTicket(TicketDto ticketDto);

    TicketDto ticketToTicketDto(Ticket ticket);

}
