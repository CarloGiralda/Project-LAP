package com.example.zone.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "subscription", uniqueConstraints = @UniqueConstraint(columnNames = {"zoneId", "username"}))
public class Subscription {
    @SequenceGenerator(name = "sub_seq", sequenceName = "sub_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sub_seq")
    @Id
    private Long id;
    private Long zoneId;
    private String username;

    public Subscription(Long zone_id, String username) {
        this.zoneId = zone_id;
        this.username = username;
    }
}
