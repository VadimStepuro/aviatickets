package com.stepuro.aviatickets.services;

import com.stepuro.aviatickets.api.dto.PrivilegeDto;
import com.stepuro.aviatickets.api.dto.PrivilegeMapper;
import com.stepuro.aviatickets.api.exeptions.ResourceNotFoundException;
import com.stepuro.aviatickets.models.Flight;
import com.stepuro.aviatickets.models.Privilege;
import com.stepuro.aviatickets.repositories.PrivilegeRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
public class PrivilegeService {
    @Autowired
    private PrivilegeRepository privelegeRepository;

    public List<PrivilegeDto> findAll(){
        return privelegeRepository
                .findAll()
                .stream()
                .map(PrivilegeMapper.INSTANCE::privilegeToPrivilegeDto)
                .collect(Collectors.toList());
    }

    public PrivilegeDto findById(UUID id) {
        return PrivilegeMapper
                .INSTANCE
                .privilegeToPrivilegeDto(privelegeRepository
                    .findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Privilege with id " + id + " not found")));
    }

    public PrivilegeDto create(PrivilegeDto privilegeDto){
        Privilege privilege = PrivilegeMapper.INSTANCE.privilegeDtoToPrivilege(privilegeDto);
        return PrivilegeMapper
                .INSTANCE
                .privilegeToPrivilegeDto(privelegeRepository.save(privilege));
    }

    public PrivilegeDto edit(PrivilegeDto privilegeDto) {
        Privilege findedPrivilege = privelegeRepository
                .findById(privilegeDto.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Privilege with id " + privilegeDto.getId() + " not found"));

        findedPrivilege.setName(privilegeDto.getName());
        return PrivilegeMapper
                .INSTANCE
                .privilegeToPrivilegeDto(privelegeRepository.save(findedPrivilege));
    }

    public void delete(UUID id){
        privelegeRepository.deleteById(id);
    }

}
