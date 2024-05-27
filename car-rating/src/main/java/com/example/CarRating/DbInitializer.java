package com.example.CarRating;

import com.example.CarRating.model.CarRating;
import com.example.CarRating.repository.CarRatingRepo;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Slf4j
@Component
@AllArgsConstructor
public class DbInitializer implements CommandLineRunner {

    private final CarRatingRepo carRatingRepo;


    @Override
    public void run(String... args) throws Exception {

        /*carRatings.add(new CarRating(5L, LocalDate.now(), "Excellent car!", 1L, 3L));
        carRatings.add(new CarRating(4L, LocalDate.now(), "Good performance.", 2L, 3L));
        carRatings.add(new CarRating(3L, LocalDate.now(), "Average experience.", 3L, 3L));
        carRatings.add(new CarRating(2L, LocalDate.now(), "Needs improvement.", 1L, 3L));
        carRatings.add(new CarRating(1L, LocalDate.now(), "Very poor.", 2L, 3L));
        */

        String[] desc = {"Excellent car!","Good performance.","Average experience.","Needs improvement.","Very poor."};


        for (long i = 1; i<= 7; i++){

            List<Long> alreadyInserted = new ArrayList<>();
            for(long j = 5; j > 0; j--){

                if(!alreadyInserted.contains((j%3) + 1)){
                    carRatingRepo.save(new CarRating(j, LocalDate.now(), desc[desc.length - (int) j], (j % 3) + 1, i));
                    alreadyInserted.add((j%3) + 1);
                }

            }
        }
    }
}
