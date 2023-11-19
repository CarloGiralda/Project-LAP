package com.example.CarSearch.repository;

import com.example.CarSearch.model.Car;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.data.jpa.domain.Specification.where;

@DataJpaTest
@Testcontainers
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class CarRepoTest {
    @Container
    PostgreSQLContainer myContainer = new PostgreSQLContainer("postgres:latest")
            .withDatabaseName("postgres")
            .withUsername("postgres")
            .withPassword("carlo");

    @Autowired
    private CarRepo carRepo;

    @Test
    public void shouldRetrieveCars() {
        // creation of the expected result
        Car car = new Car("PK543WQ", 2002L, "EURO4", "Benzina", "Tesla", 4L,
                "S", "nothing", "Roma", Car.Size.Small, 4L, Car.Engine.Electric, Car.Transmission.Automatic);
        List<Car> expectedCars = new ArrayList<Car>();
        expectedCars.add(car);

        List<Car> actualCars = carRepo.findAll(where(CarSpecifications.equalYear(2023L))
                .and(CarSpecifications.equalPollLvl("EURO4"))
                .and(CarSpecifications.equalFuel("Benzina"))
                .and(CarSpecifications.equalBrand("Tesla"))
                .and(CarSpecifications.equalPassengers(4L))
                .and(CarSpecifications.likeModel("S"))
                .and(CarSpecifications.equalPosition("Roma"))
                .and(CarSpecifications.equalSize(Car.Size.Small))
                .and(CarSpecifications.equalDoor(4L))
                .and(CarSpecifications.equalEngine(Car.Engine.Electric))
                .and(CarSpecifications.equalTransmission(Car.Transmission.Automatic)));

        for (int i = 0; i < expectedCars.size(); i++) {
            assertThat(actualCars.get(i)).usingRecursiveComparison().ignoringFields("cid").isEqualTo(expectedCars.get(i));
        }
    }
}
