package com.stepuro.aviatickets.services;

import com.stepuro.aviatickets.api.dto.*;
import com.stepuro.aviatickets.api.exeptions.ResourceNotFoundException;
import com.stepuro.aviatickets.api.mapper.FlightMapper;
import com.stepuro.aviatickets.api.mapper.TicketMapper;
import com.stepuro.aviatickets.models.Ticket;
import com.stepuro.aviatickets.repositories.TicketRepository;
import com.stepuro.aviatickets.repositories.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
public class TicketService {
    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private UserRepository userRepository;

    @Cacheable(value = "tickets")
    public List<TicketDto> findAll(){
        return ticketRepository
                .findAll()
                .stream()
                .map(TicketMapper.INSTANCE::ticketToTicketDto)
                .collect(Collectors.toList());
    }
    @Cacheable(cacheNames = "ticket", key = "#id")
    public TicketDto findById(UUID id) {
        Ticket ticket = ticketRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Ticket with id " + id + " not found"));
        return TicketMapper.INSTANCE.ticketToTicketDto(ticket);
    }

    public List<TicketDto> findAllByCitiesAndDate(FindTicketRequest request){
        return ticketRepository
                .findAllByFlightDepartureAirportCityAndFlightArrivalAirportCityAndFlightDepartureDateBetween(
                        request.getDepartureCity(),
                        request.getArrivalCity(),
                        request.getStartDate(),
                        request.getEndDate())
                .stream()
                .map(TicketMapper.INSTANCE::ticketToTicketDto)
                .collect(Collectors.toList());
    }
    @Caching(
            put = {
                    @CachePut(cacheNames = "ticket", key = "#ticketDto.id")
            },
            evict = {
                    @CacheEvict(cacheNames = "tickets", allEntries = true)
            })
    public TicketDto create(TicketDto ticketDto){
        Ticket ticket = ticketRepository.save(TicketMapper.INSTANCE.ticketDtoToTicket(ticketDto));
        return TicketMapper.INSTANCE.ticketToTicketDto(ticket);
    }

    @Caching(
            put = {
                    @CachePut(cacheNames = "ticket", key = "#ticketDto.id")
            },
            evict = {
                    @CacheEvict(cacheNames = "tickets", allEntries = true)
            })
    public TicketDto edit(TicketDto ticketDto) {
        Ticket findedTicket = ticketRepository
                .findById(ticketDto.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Ticket with id " + ticketDto.getId() + " not found"));

        findedTicket.setCost(ticketDto.getCost());
        findedTicket.setFlight(FlightMapper.INSTANCE.flightDtoToFlight(ticketDto.getFlight()));
        findedTicket.setFlightClass(ticketDto.getFlightClass());

        return TicketMapper
                .INSTANCE
                .ticketToTicketDto(ticketRepository.save(findedTicket));
    }

    @Caching(
            evict = {
                    @CacheEvict(cacheNames = "tickets", allEntries = true),
                    @CacheEvict(cacheNames = "ticket", key = "#id")
            }
    )
    public void delete(UUID id){
        ticketRepository.deleteById(id);
    }
}

