package com.stepuro.aviatickets.api.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.List;
import java.util.UUID;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class RoleDto {
    private UUID id;

    @NotBlank
    private String name;

    private List<PrivilegeDto> privilegeList;

}
