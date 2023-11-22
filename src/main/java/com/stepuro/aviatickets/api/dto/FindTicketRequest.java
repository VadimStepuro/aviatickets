package com.stepuro.aviatickets.api.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
public class FindTicketRequest {
    @NotBlank
    private String departureCity;
    @NotBlank
    private String arrivalCity;
    private Date startDate;
    private Date endDate;
}
