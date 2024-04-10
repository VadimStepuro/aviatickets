package com.stepuro.aviatickets.api.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.*;

import java.util.Date;
import java.util.UUID;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class PurchasedTicketDto {
    private UUID id;
    @NotNull
    private UserDto user;
    @NotNull
    private TicketDto ticket;
    @PastOrPresent
    private Date date;

}
