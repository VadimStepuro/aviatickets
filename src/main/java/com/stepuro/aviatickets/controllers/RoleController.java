package com.stepuro.aviatickets.controllers;

import com.stepuro.aviatickets.api.annotations.Loggable;
import com.stepuro.aviatickets.api.dto.PrivilegeDto;
import com.stepuro.aviatickets.api.dto.PrivilegeMapper;
import com.stepuro.aviatickets.api.dto.RoleDto;
import com.stepuro.aviatickets.api.dto.RoleMapper;
import com.stepuro.aviatickets.api.exeptions.ResourceNotFoundException;
import com.stepuro.aviatickets.models.Privilege;
import com.stepuro.aviatickets.models.Role;
import com.stepuro.aviatickets.services.RoleService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1")
public class RoleController {

        @Autowired
        private RoleService roleService;

        @Loggable
        @GetMapping("/roles")
        @PreAuthorize("hasAuthority('READ_ROLE_PRIVILEGE')")
        public ResponseEntity<List<RoleDto>> getAllRoles(){
            List<RoleDto> roles = roleService.findAll();

            if(roles.isEmpty())
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);


            return new ResponseEntity<>(roles, HttpStatus.OK);
        }

        @Loggable
        @GetMapping("/roles/{id}")
        @PreAuthorize("hasAuthority('READ_ROLE_PRIVILEGE')")
        public ResponseEntity<RoleDto> getRoleById(@PathVariable("id") UUID id)  {
            RoleDto roleDto = roleService.findById(id);

            return new ResponseEntity<>(roleDto, HttpStatus.OK);
        }

        @Loggable
        @PostMapping("/roles")
        @PreAuthorize("hasAuthority('WRITE_ROLE_PRIVILEGE')")
        public ResponseEntity<RoleDto> createRole(@RequestBody @Valid RoleDto roleDto){
            roleDto = roleService.create(roleDto);
            return new ResponseEntity<>(roleDto, HttpStatus.CREATED);
        }

        @Loggable
        @PutMapping("/roles")
        @PreAuthorize("hasAuthority('WRITE_ROLE_PRIVILEGE')")
        public ResponseEntity<RoleDto> updateRoles(@RequestBody @Valid RoleDto roleDto) {
            roleDto = roleService.edit(roleDto);

            return new ResponseEntity<>(roleDto, HttpStatus.OK);
        }
        @Loggable
        @DeleteMapping("/roles/{id}")
        @PreAuthorize("hasAuthority('DELETE_ROLE_PRIVILEGE')")
        public void deleteRoles(@PathVariable("id") UUID id){
            roleService.delete(id);
        }
}
