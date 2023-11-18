package com.stepuro.aviatickets.services;

import com.stepuro.aviatickets.api.dto.AirportDto;
import com.stepuro.aviatickets.api.dto.AirportMapper;
import com.stepuro.aviatickets.api.exeptions.ResourceNotFoundException;
import com.stepuro.aviatickets.models.Airplane;
import com.stepuro.aviatickets.models.Airport;
import com.stepuro.aviatickets.repositories.AirportRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
public class AirportService {
    @Autowired
    private AirportRepository airportRepository;

    public List<AirportDto> findAll(){
        return airportRepository
                .findAll()
                .stream()
                .map(AirportMapper.INSTANCE::airportToAirportDto)
                .collect(Collectors.toList());
    }

    public AirportDto findById(UUID id) {
        Airport airport = airportRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Airport with id " + id + " not found"));
        return AirportMapper.INSTANCE.airportToAirportDto(airport);
    }

    public AirportDto create(AirportDto airportdto){
        Airport airport = AirportMapper.INSTANCE.airportDtoToAirport(airportdto);

        airportdto = AirportMapper.INSTANCE.airportToAirportDto(airportRepository.save(airport));
        return airportdto;
    }

    public AirportDto edit(AirportDto airportDto) {
        Airport findedAirport = airportRepository
                .findById(airportDto.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Airport with id " + airportDto.getId() + " not found"));

        findedAirport.setName(airportDto.getName());
        findedAirport.setCountry(airportDto.getCountry());
        findedAirport.setCity(airportDto.getCity());
        findedAirport.setCapacity(airportDto.getCapacity());
        findedAirport.setRunwayNumber(airportDto.getRunwayNumber());

        return AirportMapper
                .INSTANCE
                .airportToAirportDto(airportRepository.save(findedAirport));
    }

    public void delete(UUID id){
        airportRepository.deleteById(id);
    }

}
