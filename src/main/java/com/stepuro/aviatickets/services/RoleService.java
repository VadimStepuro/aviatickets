package com.stepuro.aviatickets.services;

import com.stepuro.aviatickets.api.dto.PrivilegeMapper;
import com.stepuro.aviatickets.api.dto.RoleDto;
import com.stepuro.aviatickets.api.dto.RoleMapper;
import com.stepuro.aviatickets.api.exeptions.ResourceNotFoundException;
import com.stepuro.aviatickets.models.Privilege;
import com.stepuro.aviatickets.models.Role;
import com.stepuro.aviatickets.repositories.RoleRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
public class RoleService {
        @Autowired
        private RoleRepository roleRepository;

        public List<RoleDto> findAll(){
            return roleRepository
                    .findAll()
                    .stream()
                    .map(RoleMapper.INSTANCE::roleToRoleDto)
                    .collect(Collectors.toList());
        }

        public RoleDto findById(UUID id) {
            Role role = roleRepository
                    .findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Role with id " + id + " not found"));

            return RoleMapper.INSTANCE.roleToRoleDto(role);
        }

    public RoleDto findByName(String name) {
        Role role = roleRepository
                .findByName(name)
                .orElseThrow(() -> new ResourceNotFoundException("Role with name " + name + " not found"));

        return RoleMapper.INSTANCE.roleToRoleDto(role);
    }

        public RoleDto create(RoleDto roleDto){
            Role role = RoleMapper.INSTANCE.roleDtoToRole(roleDto);

            role = roleRepository.save(role);

            return RoleMapper.INSTANCE.roleToRoleDto(role);
        }

        public RoleDto edit(RoleDto roleDto) {
            Role findedRole = roleRepository
                    .findById(roleDto.getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Role with id " + roleDto.getId() + " not found"));

            findedRole.setName(roleDto.getName());
            findedRole
                    .setPrivilegeList(roleDto
                            .getPrivilegeList()
                            .stream()
                            .map(PrivilegeMapper.INSTANCE::privilegeDtoToPrivilege)
                            .collect(Collectors.toList()));
            return RoleMapper
                    .INSTANCE
                    .roleToRoleDto(roleRepository.save(findedRole));
        }

        public void delete(UUID id){
            roleRepository.deleteById(id);
        }
}
