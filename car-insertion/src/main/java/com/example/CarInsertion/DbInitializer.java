package com.example.CarInsertion;

import com.example.CarInsertion.init.CarImageLoader;
import com.example.CarInsertion.init.DataInitJson;
import com.example.CarInsertion.model.Car;
import com.example.CarInsertion.model.Offer;
import com.example.CarInsertion.model.Utilities;
import com.example.CarInsertion.repository.OfferRepo;
import com.example.CarInsertion.repository.UtilitiesRepo;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import com.example.CarInsertion.repository.CarRepo;

import java.io.File;
import java.io.IOException;
import java.util.List;

@Slf4j
@Component
@AllArgsConstructor
public class DbInitializer implements CommandLineRunner {

    private final CarRepo carRepository;
    private final OfferRepo offerRepository;
    private final UtilitiesRepo utilitiesRepository;

    @Override
    public void run(String... args) throws Exception {

        // Get the current working directory
        String currentDirectory = System.getProperty("user.dir");

        // Create a File object for the current directory
        File directory = new File(currentDirectory);

        // Get all files and directories in the current directory
        File[] filesAndDirs = directory.listFiles();

        // Iterate through the files and print the names of directories
        if (filesAndDirs != null) {
            for (File file : filesAndDirs) {
                if (file.isDirectory()) {
                    System.out.println("Directory: " + file.getName());
                }
            }
        }


        String[] images = {
                "renault-clio.jpg",
                "bmw.jpg",
                "jeep.jpg",
                "smart-fortwo.jpg",
                "fiat.jpg",
                "toyota.jpg",
                "ford-focus.jpg",
                "toyota-camry.jpg",
                "volkswagen-golf.jpg",
                "honda-accord.jpg"

        };


        // TODO HERE CODE TO INSERT OTHER CARS
        ObjectMapper objectMapper = new ObjectMapper();

        try {

            File jsonFile = new File("/json/data.json");
            DataInitJson cars = objectMapper.readValue(jsonFile, DataInitJson.class);


            offerRepository.deleteAll();
            utilitiesRepository.deleteAll();
            carRepository.deleteAll();

            for(int i = 0; i< cars.getCars().size(); i++){

                offerRepository.save(cars.getCars().get(i).getOffer_oid());
                utilitiesRepository.save(cars.getCars().get(i).getUtilities_utid());


                // load image
                byte[] imageData = CarImageLoader.loadImageFromFile("static/img/" + images[i]);

                cars.getCars().get(i).setImage(imageData);

                carRepository.save(cars.getCars().get(i));
            }


        } catch (Exception e){
            log.info("Cannot init the database: {}", e.getMessage());
        }
    }




}
