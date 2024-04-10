package com.stepuro.aviatickets.api.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.UUID;
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class AircompanyDto {
        private UUID id;
        @NotBlank(message = "field name is mandatory")
        private String name;
        private String country;

}
