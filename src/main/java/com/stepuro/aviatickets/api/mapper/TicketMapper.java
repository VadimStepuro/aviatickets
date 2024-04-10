package com.stepuro.aviatickets.api.mapper;

import com.stepuro.aviatickets.api.dto.TicketDto;
import com.stepuro.aviatickets.models.Ticket;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface TicketMapper {
    TicketMapper INSTANCE = Mappers.getMapper(TicketMapper.class);

    Ticket ticketDtoToTicket(TicketDto ticketDto);

    TicketDto ticketToTicketDto(Ticket ticket);

}
