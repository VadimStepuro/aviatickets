package com.stepuro.aviatickets.api.dto;

import com.stepuro.aviatickets.models.PurchasedTicket;
import com.stepuro.aviatickets.models.Role;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface PurchasedTicketMapper {
    PurchasedTicketMapper INSTANCE = Mappers.getMapper(PurchasedTicketMapper.class);

    PurchasedTicket purchasedTicketDtoToPurchasedTicket(PurchasedTicketDto purchasedTicketDto);

    PurchasedTicketDto purchasedTicketToPurchasedTicketDto(PurchasedTicket purchasedTicket);
}
