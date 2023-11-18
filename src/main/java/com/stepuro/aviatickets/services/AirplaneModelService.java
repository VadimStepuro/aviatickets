package com.stepuro.aviatickets.services;

import com.stepuro.aviatickets.api.dto.AirplaneMapper;
import com.stepuro.aviatickets.api.dto.AirplaneModelDto;
import com.stepuro.aviatickets.api.dto.AirplaneModelMapper;
import com.stepuro.aviatickets.api.exeptions.ResourceNotFoundException;
import com.stepuro.aviatickets.models.Airplane;
import com.stepuro.aviatickets.models.AirplaneModel;
import com.stepuro.aviatickets.repositories.AirplaneModelRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
public class AirplaneModelService {
    @Autowired
    private AirplaneModelRepository airplaneModelRepository;

    public List<AirplaneModelDto> findAll(){
        return  airplaneModelRepository
                .findAll()
                .stream()
                .map(AirplaneModelMapper.INSTANCE::airplaneModelToAirplaneModelDto)
                .collect(Collectors.toList());
    }

    public AirplaneModelDto findById(UUID id) {
        return AirplaneModelMapper
                .INSTANCE
                .airplaneModelToAirplaneModelDto(airplaneModelRepository
                        .findById(id)
                        .orElseThrow(() -> new ResourceNotFoundException("Airplane model with id " + id + " not found")));
    }

    public AirplaneModelDto create(AirplaneModelDto airplaneModelDto){
        return AirplaneModelMapper
                .INSTANCE
                .airplaneModelToAirplaneModelDto(airplaneModelRepository.save(AirplaneModelMapper
                        .INSTANCE
                        .airplaneModelDtoToAirplaneModel(airplaneModelDto)));
    }

    public AirplaneModelDto edit(AirplaneModelDto airplaneModel) {
        Optional<AirplaneModel> otionalAirplane = airplaneModelRepository.findById(airplaneModel.getId());
        AirplaneModel findedAirplaneModel = otionalAirplane
                .orElseThrow(() -> new ResourceNotFoundException("Airplane model with id " + airplaneModel.getId() + " not found"));

        findedAirplaneModel.setName(airplaneModel.getName());
        findedAirplaneModel.setBusinessSeats(airplaneModel.getBusinessSeats());
        findedAirplaneModel.setWeight(airplaneModel.getWeight());
        findedAirplaneModel.setEconomySeats(airplaneModel.getEconomySeats());

        return AirplaneModelMapper
                .INSTANCE
                .airplaneModelToAirplaneModelDto(airplaneModelRepository.save(findedAirplaneModel));
    }

    public void delete(UUID id){
        airplaneModelRepository.deleteById(id);
    }
}
