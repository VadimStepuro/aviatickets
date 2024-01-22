package com.stepuro.aviatickets.service;

import com.stepuro.aviatickets.api.dto.AircompanyDto;
import com.stepuro.aviatickets.models.Aircompany;
import com.stepuro.aviatickets.repositories.AircompanyRepository;
import com.stepuro.aviatickets.services.AircompanyService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
public class AircompanyServiceTests {
    @Mock
    private AircompanyRepository aircompanyRepository;

    @InjectMocks
    private AircompanyService aircompanyService;

    @Test
    public void AircompanyService_Save_ReturnsSavedModel(){

        AircompanyDto aircompanyDto = AircompanyDto.builder()
                .name("BritainAirlines")
                .country("Britain").build();

        Aircompany aircompany = Aircompany.builder()
                .name("BritainAirlines")
                .country("Britain").build();

        Mockito.when(aircompanyRepository.save(Mockito.any(Aircompany.class))).thenReturn(aircompany);

        AircompanyDto savedAircompany = aircompanyService.create(aircompanyDto);

        assertNotNull(savedAircompany);
        assertEquals(aircompanyDto.getName(), savedAircompany.getName());
        assertEquals(aircompanyDto.getCountry(), savedAircompany.getCountry());
    }

    @Test
    public void AircompanyService_Edit_ReturnsEditedModel(){

        AircompanyDto aircompanyDto = AircompanyDto.builder()
                .name("BritainAirlines")
                .country("Britain").build();

        Aircompany aircompany = Aircompany.builder()
                .id(UUID.randomUUID())
                .name("BritainAirlines")
                .country("Britain").build();

        Mockito.when(aircompanyRepository.save(Mockito.any(Aircompany.class))).thenReturn(aircompany);

        AircompanyDto savedAircompany = aircompanyService.create(aircompanyDto);

        savedAircompany.setCountry("Russia");

        Mockito.when(aircompanyRepository.findById(Mockito.any(UUID.class))).thenReturn(Optional.of(aircompany));

        AircompanyDto editedAircompany = aircompanyService.edit(savedAircompany);

        assertNotNull(editedAircompany);
        assertEquals(savedAircompany.getName(), editedAircompany.getName());
        assertEquals(savedAircompany.getCountry(), editedAircompany.getCountry());
    }
}
