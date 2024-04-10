package com.stepuro.aviatickets.services;

import com.stepuro.aviatickets.api.dto.AircompanyDto;
import com.stepuro.aviatickets.api.mapper.AircompanyMapper;
import com.stepuro.aviatickets.api.exeptions.ResourceNotFoundException;
import com.stepuro.aviatickets.models.Aircompany;
import com.stepuro.aviatickets.repositories.AircompanyRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
public class AircompanyService {
    @Autowired
    private AircompanyRepository aircompanyRepository;

    public List<AircompanyDto> findAll(){
        return aircompanyRepository
                .findAll()
                .stream()
                .map(AircompanyMapper.INSTANCE::aircompanyToAircompanyDto)
                .collect(Collectors.toList());
    }

    public AircompanyDto findById(UUID id){
        return AircompanyMapper
                .INSTANCE
                .aircompanyToAircompanyDto(aircompanyRepository
                        .findById(id)
                        .orElseThrow(() -> new ResourceNotFoundException("Aircompany with id " + id + " not found")));
    }

    public AircompanyDto create(AircompanyDto aircompany){
        return AircompanyMapper
                .INSTANCE
                .aircompanyToAircompanyDto(aircompanyRepository
                        .save(AircompanyMapper
                                .INSTANCE
                                .aircompanyDtoToAircompany(aircompany)));
    }

    public AircompanyDto edit(AircompanyDto aircompany) {
        Optional<Aircompany> aircompany1 = aircompanyRepository.findById(aircompany.getId());
        Aircompany aircompany2 = aircompany1.orElseThrow(() -> new ResourceNotFoundException("Aircompany with id " + aircompany.getId() + " not found"));
        aircompany2.setCountry(aircompany.getCountry());
        aircompany2.setName(aircompany.getName());
        return AircompanyMapper
                .INSTANCE
                .aircompanyToAircompanyDto(aircompanyRepository.save(aircompany2));
    }


    public void delete(UUID id){
        aircompanyRepository.deleteById(id);
    }
}
