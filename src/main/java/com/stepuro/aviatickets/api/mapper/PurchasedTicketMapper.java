package com.stepuro.aviatickets.api.mapper;

import com.stepuro.aviatickets.api.dto.PurchasedTicketDto;
import com.stepuro.aviatickets.models.PurchasedTicket;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface PurchasedTicketMapper {
    PurchasedTicketMapper INSTANCE = Mappers.getMapper(PurchasedTicketMapper.class);

    PurchasedTicket purchasedTicketDtoToPurchasedTicket(PurchasedTicketDto purchasedTicketDto);

    PurchasedTicketDto purchasedTicketToPurchasedTicketDto(PurchasedTicket purchasedTicket);
}
