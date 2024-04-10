package com.stepuro.aviatickets.api.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import lombok.*;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class UserResponse {
    private UUID id;
    @NotBlank
    private String login;
    @NotBlank
    private String name;
    @Email
    private String email;
    @Past
    private Date birthdate;
    private List<RoleDto> roles;

}
