package com.example.CarInsertion;

import java.io.IOException;
import java.io.InputStream;

public class CarImageLoader {

    public static byte[] loadImageFromFile(String imagePath) throws IOException {
        ClassLoader classLoader = CarImageLoader.class.getClassLoader();
        try (InputStream inputStream = classLoader.getResourceAsStream(imagePath)) {
            if (inputStream == null) {
                throw new IOException("File not found: " + imagePath);
            }
            return inputStream.readAllBytes();
        }
    }
}