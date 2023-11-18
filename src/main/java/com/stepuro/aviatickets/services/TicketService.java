package com.stepuro.aviatickets.services;

import com.stepuro.aviatickets.api.dto.*;
import com.stepuro.aviatickets.api.exeptions.ResourceNotFoundException;
import com.stepuro.aviatickets.models.Role;
import com.stepuro.aviatickets.models.Ticket;
import com.stepuro.aviatickets.models.User;
import com.stepuro.aviatickets.repositories.TicketRepository;
import com.stepuro.aviatickets.repositories.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
public class TicketService {
        @Autowired
        private TicketRepository ticketRepository;

        @Autowired
        private UserRepository userRepository;

        public List<TicketDto> findAll(){
            return ticketRepository
                    .findAll()
                    .stream()
                    .map(TicketMapper.INSTANCE::ticketToTicketDto)
                    .collect(Collectors.toList());
        }

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

        public TicketDto create(TicketDto ticketDto){
            Ticket ticket = ticketRepository.save(TicketMapper.INSTANCE.ticketDtoToTicket(ticketDto));
            return TicketMapper.INSTANCE.ticketToTicketDto(ticket);
        }

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

        public void delete(UUID id){
            ticketRepository.deleteById(id);
        }
}

