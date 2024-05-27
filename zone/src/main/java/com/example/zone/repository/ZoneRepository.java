package com.example.zone.repository;

import com.example.zone.model.Zone;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ZoneRepository extends JpaRepository<Zone, Long> {
    @Query("SELECT z FROM Zone z WHERE z.center_latitude = ?1 and z.center_longitude = ?2 and z.radius = ?3")
    List<Zone> findAllByCenterAndRadius(Double center_latitude, Double center_longitude, Double radius);



}
