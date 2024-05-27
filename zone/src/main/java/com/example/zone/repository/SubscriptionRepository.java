package com.example.zone.repository;

import com.example.zone.model.Subscription;
import com.example.zone.model.Zone;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {

    List<Subscription> findSubscriptionByUsername(String username);

    Subscription findSubscriptionByZoneIdAndUsername(Long zoneId, String username);


}
