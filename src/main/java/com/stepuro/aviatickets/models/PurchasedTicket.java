package com.stepuro.aviatickets.models;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
import java.util.UUID;

@Setter
@Getter
@Entity
@Table(name = "purchased_ticket", schema = "public")
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class PurchasedTicket {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "ticket_id", referencedColumnName = "id")
    private Ticket ticket;

    @Column(name = "date")
    private Date date;

}
