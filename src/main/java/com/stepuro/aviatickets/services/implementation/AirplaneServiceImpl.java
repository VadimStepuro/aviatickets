package com.stepuro.aviatickets.services.implementation;

import com.stepuro.aviatickets.api.mapper.AircompanyMapper;
import com.stepuro.aviatickets.api.dto.AirplaneDto;
import com.stepuro.aviatickets.api.mapper.AirplaneMapper;
import com.stepuro.aviatickets.api.mapper.AirplaneModelMapper;
import com.stepuro.aviatickets.api.exeptions.ResourceNotFoundException;
import com.stepuro.aviatickets.models.Airplane;
import com.stepuro.aviatickets.repositories.AirplaneRepository;
import com.stepuro.aviatickets.services.AirplaneService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
public class AirplaneServiceImpl implements AirplaneService {
    @Autowired
    private AirplaneRepository airplaneRepository;

    public List<AirplaneDto> findAll(){
        return airplaneRepository
                .findAll()
                .stream()
                .map(AirplaneMapper.INSTANCE::airplaneToAirplaneDto)
                .collect(Collectors.toList());
    }

    public AirplaneDto findById(UUID id) {
        Airplane airplane = airplaneRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Airplane with id " + id + " not found"));
        return AirplaneMapper.INSTANCE.airplaneToAirplaneDto(airplane);
    }

    public AirplaneDto create(AirplaneDto airplaneDto){
        Airplane airplane = AirplaneMapper.INSTANCE.airplaneDtoToAirplane(airplaneDto);
        return AirplaneMapper.INSTANCE.airplaneToAirplaneDto(airplaneRepository.save(airplane));
    }

    public AirplaneDto edit(AirplaneDto airplaneDto) {
        Optional<Airplane> otionalAirplane = airplaneRepository.findById(airplaneDto.getId());

        Airplane findedAirplane = otionalAirplane.orElseThrow(() -> new ResourceNotFoundException("Airplane with id " + airplaneDto.getId() + " not found"));

        findedAirplane.setAircompany(AircompanyMapper
                .INSTANCE
                .aircompanyDtoToAircompany(airplaneDto.getAircompany()));
        findedAirplane.setModel(AirplaneModelMapper
                .INSTANCE
                .airplaneModelDtoToAirplaneModel(airplaneDto.getModel()));

        return AirplaneMapper
                .INSTANCE
                .airplaneToAirplaneDto(airplaneRepository.save(findedAirplane));
    }

    public void delete(UUID id){
        airplaneRepository.deleteById(id);
    }
}
