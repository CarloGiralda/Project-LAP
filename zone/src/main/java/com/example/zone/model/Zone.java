package com.example.zone.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.ZoneId;

@Setter
@Getter
@NoArgsConstructor
@Entity
@Table(name = "zone", uniqueConstraints = @UniqueConstraint(columnNames = {"center_latitude", "center_latitude", "radius"}))
public class Zone {
    @SequenceGenerator(name = "zone_seq", sequenceName = "zone_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "zone_seq")
    @Id
    private Long id;
    private String locationName;
    private double center_latitude;
    private double center_longitude;
    private double radius;

    public Zone(String locationName,double center_latitude, double center_longitude, double radius) {
        this.locationName = locationName;
        this.center_latitude = center_latitude;
        this.center_longitude = center_longitude;
        this.radius = radius;
    }
}
