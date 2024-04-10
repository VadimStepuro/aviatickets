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
public class PrivilegeDto {
    private UUID id;
    @NotBlank
    private String name;

}