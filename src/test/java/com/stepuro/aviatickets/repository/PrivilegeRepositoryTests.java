package com.stepuro.aviatickets.repository;

import com.stepuro.aviatickets.models.Privilege;
import com.stepuro.aviatickets.repositories.PrivilegeRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class PrivilegeRepositoryTests {

    @Autowired
    private PrivilegeRepository privilegeRepository;

    @Test
    public void PrivilegeRepository_Save_ReturnsSavedModel(){
        Privilege privilege = Privilege.builder().name("Some_Privilege").build();

        Privilege savedPrivilege = privilegeRepository.save(privilege);

        assertNotNull(savedPrivilege);
        assertNotNull(savedPrivilege.getId());
        assertEquals(privilege.getName(), savedPrivilege.getName());
    }

    @Test
    public void PrivilegeRepository_FindByName_ReturnsModel(){
        Privilege privilege = Privilege.builder().name("Some_Privilege").build();

        Privilege savedPrivilege = privilegeRepository.save(privilege);

        Privilege findedPrivilege = privilegeRepository.findByName(savedPrivilege.getName());

        assertNotNull(findedPrivilege);
        assertNotNull(findedPrivilege.getId());
        assertEquals(savedPrivilege.getName(), findedPrivilege.getName());
    }

    @Test
    public void PrivilegeRepository_Update_ReturnsUpdatedModel(){

        Privilege privilege = Privilege.builder().name("Some_Privilege").build();
        Privilege savedPrivilege = privilegeRepository.save(privilege);
        savedPrivilege.setName("OtherPrivilege");

        Privilege updatedPrivilege = privilegeRepository.save(savedPrivilege);



        assertNotNull(updatedPrivilege);
        assertNotNull(updatedPrivilege.getId());
        assertEquals(savedPrivilege.getName(), updatedPrivilege.getName());
    }

    @Test
    public void PrivilegeRepository_Delete_DeletesModel(){

        Privilege privilege = Privilege.builder().name("Some_Privilege").build();
        Privilege savedPrivilege = privilegeRepository.save(privilege);

        privilegeRepository.deleteById(savedPrivilege.getId());
        Optional<Privilege> deletedPrivilege = privilegeRepository.findById(savedPrivilege.getId());

        assertTrue(deletedPrivilege.isEmpty());
    }
}
