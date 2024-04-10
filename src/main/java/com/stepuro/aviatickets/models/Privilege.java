package com.stepuro.aviatickets.models;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;

import java.util.UUID;

@Setter
@Getter
@Entity
@Table(name = "privilege", schema = "public")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class Privilege implements GrantedAuthority {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    private UUID id;
    @Column(name = "name")
    private String name;

    @Override
    public String getAuthority() {
        return getName();
    }
}