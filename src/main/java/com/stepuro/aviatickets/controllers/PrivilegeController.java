package com.stepuro.aviatickets.controllers;

import com.stepuro.aviatickets.api.annotations.Loggable;
import com.stepuro.aviatickets.api.dto.FlightDto;
import com.stepuro.aviatickets.api.dto.FlightMapper;
import com.stepuro.aviatickets.api.dto.PrivilegeDto;
import com.stepuro.aviatickets.api.dto.PrivilegeMapper;
import com.stepuro.aviatickets.api.exeptions.ResourceNotFoundException;
import com.stepuro.aviatickets.models.Flight;
import com.stepuro.aviatickets.models.Privilege;
import com.stepuro.aviatickets.services.PrivilegeService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
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
public class PrivilegeController {
        @Autowired
        private PrivilegeService privilegeService;

        @Loggable
        @GetMapping("/privileges")
        @PreAuthorize("hasAuthority('READ_PRIVILEGE_PRIVILEGE')")
        public ResponseEntity<List<PrivilegeDto>> getAllPrivileges(){
            List<PrivilegeDto> privileges = privilegeService.findAll();

            if(privileges.isEmpty())
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);


            return new ResponseEntity<>(privileges, HttpStatus.OK);
        }

        @Loggable
        @GetMapping("/privileges/{id}")
        @PreAuthorize("hasAuthority('READ_PRIVILEGE_PRIVILEGE')")
        public ResponseEntity<PrivilegeDto> getPrivilegeById(@PathVariable("id") UUID id)  {
            PrivilegeDto privilege = privilegeService.findById(id);

            return new ResponseEntity<>(privilege, HttpStatus.OK);
        }

        @Loggable
        @PostMapping("/privileges")
        @PreAuthorize("hasAuthority('WRITE_FLIGHT_PRIVILEGE')")
        public ResponseEntity<PrivilegeDto> createPrivilege(@RequestBody @Valid PrivilegeDto privilegeDto){
            privilegeDto = privilegeService.create(privilegeDto);
            return new ResponseEntity<>(privilegeDto, HttpStatus.CREATED);
        }

        @Loggable
        @PutMapping("/privileges")
        @PreAuthorize("hasAuthority('WRITE_FLIGHT_PRIVILEGE')")
        public ResponseEntity<PrivilegeDto> updatePrivilege(@RequestBody @Valid PrivilegeDto privilegeDto){
            privilegeDto = privilegeService.edit(privilegeDto);

            return new ResponseEntity<>(privilegeDto, HttpStatus.OK);
        }

        @Loggable
        @DeleteMapping("/privileges/{id}")
        @PreAuthorize("hasAuthority('DELETE_FLIGHT_PRIVILEGE')")
        public void deletePrivilege(@PathVariable("id") UUID id){
            privilegeService.delete(id);
        }
}
