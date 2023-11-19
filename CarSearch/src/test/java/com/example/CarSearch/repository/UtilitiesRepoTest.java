package com.example.CarSearch.repository;

import com.example.CarSearch.model.Utilities;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.data.jpa.domain.Specification.where;

@DataJpaTest
@Testcontainers
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class UtilitiesRepoTest {
    PostgreSQLContainer myContainer = new PostgreSQLContainer("postgres:latest")
            .withDatabaseName("postgres")
            .withUsername("postgres")
            .withPassword("carlo");

    @Autowired
    private UtilitiesRepo utilitiesRepo;

    @Test
    public void shouldRetrieveUtilities() {
        // creation of the expected result
        Utilities ut = new Utilities(Utilities.Assistant.Android, true, true, true, false,
                true, true, true, true, false, true, true,
                true, "BELLA");
        List<Utilities> expectedUts = new ArrayList<Utilities>();
        expectedUts.add(ut);
        expectedUts.add(ut);

        List<Utilities> actualUts = utilitiesRepo.findAll(where(UtilitiesSpecifications.equalDisplay(true))
                .and(UtilitiesSpecifications.equalAssistant(Utilities.Assistant.Android))
                .and(UtilitiesSpecifications.equalAirConditioning(true))
                .and(UtilitiesSpecifications.equalStartAndStop(true))
                .and(UtilitiesSpecifications.equalNavigationSystem(false))
                .and(UtilitiesSpecifications.equalParkingAssistant(true))
                .and(UtilitiesSpecifications.equalBluetooth(true))
                .and(UtilitiesSpecifications.equalUSBPorts(true))
                .and(UtilitiesSpecifications.equalCDPlayer(true))
                .and(UtilitiesSpecifications.equalRadioAMFM(false))
                .and(UtilitiesSpecifications.equalCruiseControl(true))
                .and(UtilitiesSpecifications.equalParkingCamera(true))
                .and(UtilitiesSpecifications.equalSurroundAudio(true)));

        for (int i = 0; i < expectedUts.size(); i++) {
            assertThat(actualUts.get(i)).usingRecursiveComparison().ignoringFields("utid").isEqualTo(expectedUts.get(i));
        }
    }
}
