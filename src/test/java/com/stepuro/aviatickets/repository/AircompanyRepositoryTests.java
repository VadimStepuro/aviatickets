package com.stepuro.aviatickets.repository;

import com.stepuro.aviatickets.models.Aircompany;
import com.stepuro.aviatickets.repositories.AircompanyRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class AircompanyRepositoryTests {
    @Autowired
    private AircompanyRepository aircompanyRepository;

    @Test
    public void AircompanyRepository_Save_ReturnsSavedModel(){
        //Arrange
        Aircompany aircompany = Aircompany.builder()
                .name("BritainAirlines")
                .country("Britain").build();

        //Act
        Aircompany savedAircompany = aircompanyRepository.save(aircompany);

        //Assert
        assertNotNull(savedAircompany);
        assertEquals(aircompany.getName(), savedAircompany.getName());
        assertEquals(aircompany.getCountry(), savedAircompany.getCountry());
    }

    @Test
    public void AircompanyRepository_FindAll_ReturnsAllModels(){
        Aircompany aircompany1 = Aircompany.builder()
                .name("BritainAirlines")
                .country("Britain").build();
        Aircompany aircompany2 = Aircompany.builder()
                .name("RussianAirlines")
                .country("Russia").build();
        Aircompany aircompany3 = Aircompany.builder()
                .name("BelgiumAirlines")
                .country("Belgium").build();
        Aircompany aircompany4 = Aircompany.builder()
                .name("GermanyAirlines")
                .country("Germany").build();

        aircompanyRepository.save(aircompany1);
        aircompanyRepository.save(aircompany2);
        aircompanyRepository.save(aircompany3);
        aircompanyRepository.save(aircompany4);

        List<Aircompany> aircompanies = aircompanyRepository.findAll();

        assertNotNull(aircompanies);
        assertEquals(4, aircompanies.size());
    }

    @Test
    public void AircompanyRepository_FindById_ReturnsModel(){
        Aircompany aircompany1 = Aircompany.builder()
                .name("BritainAirlines")
                .country("Britain").build();
        Aircompany aircompany2 = Aircompany.builder()
                .name("RussianAirlines")
                .country("Russia").build();

        Aircompany savedAircompany1 = aircompanyRepository.save(aircompany1);
        aircompanyRepository.save(aircompany2);


        Aircompany aircompany = aircompanyRepository.findById(savedAircompany1.getId()).get();

        assertNotNull(aircompany);
        assertEquals(aircompany1.getId(), aircompany.getId());
    }

    @Test
    public void AircompanyRepository_FindByName_ReturnsModel(){
        Aircompany aircompany1 = Aircompany.builder()
                .name("BritainAirlines")
                .country("Britain").build();
        Aircompany aircompany2 = Aircompany.builder()
                .name("RussianAirlines")
                .country("Russia").build();

        Aircompany savedAircompany1 = aircompanyRepository.save(aircompany1);
        aircompanyRepository.save(aircompany2);

        Aircompany firstByName = aircompanyRepository.findFirstByName(savedAircompany1.getName());

        assertNotNull(firstByName);
        assertEquals(savedAircompany1.getId(), firstByName.getId());
        assertEquals(savedAircompany1.getName(), firstByName.getName());
        assertEquals(savedAircompany1.getCountry(), firstByName.getCountry());
    }

    @Test
    public void AircompanyRepository_Update_ChangesModel(){
        Aircompany aircompany1 = Aircompany.builder()
                .name("BritainAirlines")
                .country("Britain").build();
        

        Aircompany savedAircompany = aircompanyRepository.save(aircompany1);

        savedAircompany.setCountry("Belarus");

        Aircompany updatedAircompany = aircompanyRepository.save(savedAircompany);

        assertNotNull(updatedAircompany);
        assertEquals(savedAircompany.getId(), updatedAircompany.getId());
        assertEquals(savedAircompany.getName(), updatedAircompany.getName());
        assertEquals(savedAircompany.getCountry(), updatedAircompany.getCountry());
    }

    @Test
    public void AircompanyRepository_Remove_RemovesModel(){
        Aircompany aircompany1 = Aircompany.builder()
                .name("BritainAirlines")
                .country("Britain").build();

        Aircompany savedAircompany = aircompanyRepository.save(aircompany1);

        aircompanyRepository.deleteById(savedAircompany.getId());

        Optional<Aircompany> aircompany = aircompanyRepository.findById(savedAircompany.getId());


        assertTrue(aircompany.isEmpty());
    }
}
