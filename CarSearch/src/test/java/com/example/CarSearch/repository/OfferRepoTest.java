package com.example.CarSearch.repository;

import com.example.CarSearch.model.Offer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.data.jpa.domain.Specification.where;

@DataJpaTest
@Testcontainers
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class OfferRepoTest {
    PostgreSQLContainer myContainer = new PostgreSQLContainer("postgres:latest")
            .withDatabaseName("postgres")
            .withUsername("postgres")
            .withPassword("carlo");

    @Autowired
    private OfferRepo offerRepo;

    @Test
    public void shouldRetrieveOffers() {
        // creation of the expected result
        Offer offer = new Offer(LocalDate.of(2020,10,10), LocalDate.of(2020,10,10), "10.5");
        List<Offer> expectedOffers = new ArrayList<Offer>();
        expectedOffers.add(offer);
        expectedOffers.add(offer);

        List<Offer> actualOffers = offerRepo.findAll(where(OfferSpecifications.equalAvailable(true))
                .and(OfferSpecifications.equalFromDate(LocalDate.of(2020,10,10)))
                .and(OfferSpecifications.equalToDate(LocalDate.of(2020,10,10)))
                .and(OfferSpecifications.equalPricePerDay("10.5")));

        for (int i = 0; i < actualOffers.size(); i++) {
            assertThat(actualOffers.get(i)).usingRecursiveComparison().ignoringFields("oid").isEqualTo(expectedOffers.get(i));
        }
    }
}
