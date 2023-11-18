package com.stepuro.aviatickets.services;

import com.stepuro.aviatickets.api.dto.*;
import com.stepuro.aviatickets.api.exeptions.NoTicketsLeftException;
import com.stepuro.aviatickets.api.exeptions.ResourceNotFoundException;
import com.stepuro.aviatickets.models.FlightClass;
import com.stepuro.aviatickets.models.PurchasedTicket;
import com.stepuro.aviatickets.models.Ticket;
import com.stepuro.aviatickets.models.User;
import com.stepuro.aviatickets.repositories.PurchasedTicketRepository;
import com.stepuro.aviatickets.repositories.TicketRepository;
import com.stepuro.aviatickets.repositories.UserRepository;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
public class PurchasedTicketService {
    @Autowired
    private PurchasedTicketRepository purchasedTicketRepository;

    @Autowired
    private TicketRepository ticketRepository;
    @Autowired
    private UserRepository userRepository;

    public List<PurchasedTicketDto> findAll() {
        return purchasedTicketRepository
                .findAll()
                .stream()
                .map(PurchasedTicketMapper.INSTANCE::purchasedTicketToPurchasedTicketDto)
                .collect(Collectors.toList());
    }

    public PurchasedTicketDto findById(UUID id){
        PurchasedTicket purchasedTicket = purchasedTicketRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Purchased ticket with id " + id + " not found"));
        return PurchasedTicketMapper.INSTANCE.purchasedTicketToPurchasedTicketDto(purchasedTicket);
    }

    public List<PurchasedTicketDto> findAllByUserLogin(String login){
        User user = userRepository.findByLogin(login).orElseThrow(() -> new ResourceNotFoundException("User with login " + login + " not found"));
        List<PurchasedTicket> purchasedTickets = purchasedTicketRepository.findAllByUser(user);
        return purchasedTickets
                .stream()
                .map(PurchasedTicketMapper.INSTANCE::purchasedTicketToPurchasedTicketDto)
                .collect(Collectors.toList());
    }

    public List<PurchasedTicketDto> findAllByTicket(TicketDto ticketDto){
        Ticket ticket = TicketMapper.INSTANCE.ticketDtoToTicket(ticketDto);
        List<PurchasedTicket> purchasedTickets = purchasedTicketRepository.findAllByTicket(ticket);

        return purchasedTickets
                .stream()
                .map(PurchasedTicketMapper.INSTANCE::purchasedTicketToPurchasedTicketDto)
                .collect(Collectors.toList());
    }

    public PurchasedTicketDto create(PurchasedTicketDto purchasedTicketDto){
        PurchasedTicket purchasedTicket = PurchasedTicketMapper
                .INSTANCE
                .purchasedTicketDtoToPurchasedTicket(purchasedTicketDto);

        UUID id = purchasedTicket.getTicket().getId();

        Optional<Ticket> optionalTicket = ticketRepository.findById(id);
        Ticket ticket = optionalTicket
                .orElseThrow(()-> new ResourceNotFoundException("Ticket with id " + id + " not found"));

        if(isThereFreeSeats(ticket))
        {
            purchasedTicket = purchasedTicketRepository.save(purchasedTicket);
            return PurchasedTicketMapper
                    .INSTANCE
                    .purchasedTicketToPurchasedTicketDto(purchasedTicket);
        }

        throw new NoTicketsLeftException();


    }

    public boolean isThereFreeSeats(Ticket ticket){
        int businessSeats = ticket.getFlight().getAirplane().getModel().getBusinessSeats();
        int economySeats = ticket.getFlight().getAirplane().getModel().getEconomySeats();

        if(ticket.getFlightClass() == FlightClass.BUSINESS)
            return purchasedTicketRepository.countByTicket(ticket) < businessSeats;
        else
            return purchasedTicketRepository.countByTicket(ticket) < economySeats;
    }

    public PurchasedTicketDto edit(PurchasedTicketDto purchasedTicketDto){
        PurchasedTicket purchasedTicket = purchasedTicketRepository
                .findById(purchasedTicketDto.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Purchased ticket with id " + purchasedTicketDto.getId() + " not found"));
        purchasedTicket.setTicket(TicketMapper.INSTANCE.ticketDtoToTicket(purchasedTicketDto.getTicket()));
        purchasedTicket.setUser(UserMapper.INSTANCE.userDtoToUser(purchasedTicketDto.getUser()));
        purchasedTicket.setDate(purchasedTicketDto.getDate());
        return PurchasedTicketMapper
                .INSTANCE
                .purchasedTicketToPurchasedTicketDto(purchasedTicketRepository.save(purchasedTicket));
    }

    public void delete(UUID id){
        purchasedTicketRepository.deleteById(id);
    }
}
