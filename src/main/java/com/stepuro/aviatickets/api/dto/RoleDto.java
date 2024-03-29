package com.stepuro.aviatickets.api.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class RoleDto {
    private UUID id;

    @NotBlank
    private String name;

    private List<PrivilegeDto> privilegeList;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<PrivilegeDto> getPrivilegeList() {
        return privilegeList;
    }

    public void setPrivilegeList(List<PrivilegeDto> privilegeList) {
        this.privilegeList = privilegeList;
    }
}
