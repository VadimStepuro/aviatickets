package com.stepuro.aviatickets.api.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Data
@Builder
@Getter
@Setter
public class FindTicketRequest {
    @NotBlank
    private String departureCity;
    @NotBlank
    private String arrivalCity;
    private Date startDate;
    private Date endDate;
}
