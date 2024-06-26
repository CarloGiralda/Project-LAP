package com.example.carbook.service;


import com.example.carbook.model.booking.*;
import com.example.carbook.model.carSubscription.CarSubscriptionMessage;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;


@Service
@Slf4j
public class BookingService {

    private final BookingRepository bookingRepository;

    private final DiscoveryClientService discoveryClientService;

    private final HashMap<Long, SimplifiedBooking> bookingMap;

    @Autowired
    private RabbitTemplate template;

    @Autowired
    private Queue queue;

    ;

    private final RestTemplate restTemplate;

    @Value("${app.subscription-service-url}")
    private String subscriptionServiceUrl;

    public BookingService(BookingRepository bookingRepository, DiscoveryClientService discoveryClientService, RestTemplate restTemplate) {
        this.bookingRepository = bookingRepository;
        this.discoveryClientService = discoveryClientService;
        this.restTemplate = restTemplate;
        this.bookingMap = new HashMap<>();
    }


    /**
     * Store booking in DB and HashMap and check whether some of the stored bookings
     * is expired, if yes it advertises the notification service to notify all subscribed user
     * that the car is now available**/
    public void setBooking(Booking booking) throws Exception {

        try {

            List<Booking> overlappingBookings =  bookingRepository.findOverlappingBookings(
                    booking.getCid(),
                    booking.getFromDay(),
                    booking.getToDay(),
                    booking.getFromHour(),
                    booking.getToHour());

            List<Booking> overlappingBookingsForUser =  bookingRepository.findOverlappingBookingsForUser(
                    booking.getFromDay(),
                    booking.getToDay(),
                    booking.getFromHour(),
                    booking.getToHour(),
                    booking.getUsername());


            // before saving check not overlapping bookings for the same car
            if (overlappingBookings.isEmpty() && overlappingBookingsForUser.isEmpty()){

                // Store booking on DB
                Booking savedBooking = bookingRepository.save(booking);

                // Store only necessary field in memory
                SimplifiedBooking simplifiedBooking = new SimplifiedBooking(savedBooking.getCid(), savedBooking.getToDay(), savedBooking.getToHour());

                // Store the booking in the hashmap
                bookingMap.put(savedBooking.getBid(),simplifiedBooking);

                // notify renter about
                String renter = getRenterUsername(booking.getCid());

                // define the message to send by email
                BookedCar bookedCar = new BookedCar(booking.getCid(), renter,booking.getUsername());

                // notify renter
                sendEmail(bookedCar);


            }
            else {
                throw new DataIntegrityViolationException("overlapping bookings");
            }

        } catch (DataIntegrityViolationException e){
            log.info("constraints exception");
            throw new Exception(e.getMessage());

        }


    }

    private String getRenterUsername(Long cid) {
        // send request to email server
        String carSearchUrl = discoveryClientService.getServiceUrl("CARSEARCH-SERVICE") + "/carsearch/getRenterUsername?id=" + cid;
        log.info("Sending request to carsearch service at {}", carSearchUrl);

        // make the request to email service
        ResponseEntity<String> response = restTemplate.getForEntity(carSearchUrl, String.class);

        if (!response.getStatusCode().is2xxSuccessful()) {
            throw new RuntimeException("cannot get renter username");
        }

        // Parse the JSON response
        String responseBody = response.getBody();
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            JsonNode jsonNode = objectMapper.readTree(responseBody);
            return jsonNode.get("renter").asText();
        } catch (Exception e) {
            throw new RuntimeException("Error parsing JSON response", e);
        }
    }


    private void sendEmail(BookedCar bookedCar){
        // send request to email server
        String emailServiceUrl = discoveryClientService.getServiceUrl("SENDEMAIL-SERVICE") + "/email/sendCarBookedEmail";
        log.info("Sending request to email service at {}", emailServiceUrl);


        // make the request to email service
        ResponseEntity<String> response = restTemplate.postForEntity(emailServiceUrl, bookedCar, String.class);

        if (!response.getStatusCode().is2xxSuccessful()){
            throw new RuntimeException("error in the email sender");
        }

    }


    public void extendBooking(ExtendBookingRequest request) {
        try {
            Long bid = request.getBid();

            // update expiration date on database
            Optional<Booking> referenceBooking = bookingRepository.findById(bid);

            referenceBooking.ifPresent(booking -> {
                booking.setToDay(request.getToDay());
                booking.setToHour(request.getToHour());
                bookingRepository.save(booking);
            } );


            // update booking map with new expiration date
            if (bookingMap.containsKey(bid)){
                bookingMap.get(bid).setToDay(request.getToDay());
                bookingMap.get(bid).setToHour(request.getToHour());
            }


        } catch (Exception e){
            log.info(e.getMessage());
        }

    }

    public ArrayList<Booking> getBooking(String username, String flag, String param){
        System.out.println(flag+param+username);
        return switch (flag) {
            case "username" -> bookingRepository.findByUsername(username);
            case "username&from" -> bookingRepository.findByUsernameAndFromDay(username, param);
            case "username&madeDate" -> bookingRepository.findByUsernameAndToDay(username, param);
            case "username&to" -> bookingRepository.findByUsernameAndMadeDate(username, param);
            case "cid" -> bookingRepository.findByCid(Long.parseLong(param));
            default -> bookingRepository.findByUsernameAndCid(username, Long.parseLong(param));
        };
    }


    @Scheduled(fixedDelay = 10000)
    public void checkSubscribedUser(){

        if (!bookingMap.isEmpty()){
            for(Map.Entry<Long, SimplifiedBooking> set: bookingMap.entrySet()){

                if (expired(set.getValue().getToDay(),set.getValue().getToHour(), true)){
                    log.info("reservation with id {} has expired", set.getKey());

                    // remove from hashmap the booking because now is expired
                    bookingMap.remove(set.getKey());

                    // 1) get the ids of the subscribed user for a car
                    String subscriptionUrl = subscriptionServiceUrl + "/subscription/getSubscribedUser/{carId}";
                    ResponseEntity<String[]> response = restTemplate.getForEntity(subscriptionUrl, String[].class, set.getValue().getCid());
                    if (response.getStatusCode().is2xxSuccessful()) {
                        String[] subscriptions = response.getBody();

                        assert subscriptions != null;

                        for(String user: subscriptions){
                            log.info("user {} subscribed to car {}" ,user, set.getValue().getCid());
                        }

                        // 2) send this message to the queue
                        CarSubscriptionMessage carSubscriptionMessage = new CarSubscriptionMessage(set.getValue().getCid(),subscriptions);
                        template.convertAndSend(queue.getName(), carSubscriptionMessage);
                    }



                }

            }
        }
        else {
            log.info("no booking is stored");
        }

    }



    private static boolean expired(String day, String hour, Boolean timeDelay){
        log.info("CHECK:{},{},{}", day,hour,timeDelay);

        // Parse date and time strings
        LocalDate toDate = LocalDate.parse(day, DateTimeFormatter.ofPattern("dd-MM-yyyy"));
        LocalTime toTimeOfDay = LocalTime.parse(hour, DateTimeFormatter.ofPattern("HH:mm"));

        // Create LocalDateTime by combining date and time
        LocalDateTime toDateTime = LocalDateTime.of(toDate, toTimeOfDay);

        // Get current date and time
        LocalDateTime currentDateTime = LocalDateTime.now();
        log.info("comparing {} with {}", toDateTime, currentDateTime);

        if (timeDelay) {
            long minutesDifference = ChronoUnit.MINUTES.between(toDateTime, currentDateTime);
            return minutesDifference >= 12;
        } else {
            return currentDateTime.isAfter(toDateTime);
        }
    }




    public ArrayList<BookingPreview> getBookingPreview(String username) {
        ArrayList<Booking> bookings = bookingRepository.findByUsername(username);
        ArrayList<BookingPreview> bookingPreviews = new ArrayList<>();
        for (Booking booking: bookings) {
            if (!expired(booking.getToDay(), booking.getToHour(), false)){
                bookingPreviews.add(new BookingPreview(
                        booking.getBid(),
                        booking.getCid(),
                        booking.getFromDay(),
                        booking.getToDay(),
                        booking.getToHour(),
                        booking.getMadeDate())
                );
            }

        }

        return bookingPreviews;
    }

    public ArrayList<BookingPreview> getBookingHistory(String username) {
       try {
           ArrayList<Booking> bookings = bookingRepository.findByUsername(username);

           ArrayList<BookingPreview> bookingHistoryPreviews = new ArrayList<>();
           for (Booking booking: bookings) {

               if (expired(booking.getToDay(), booking.getToHour(), false)){
                   bookingHistoryPreviews.add(new BookingPreview(
                           booking.getBid(),
                           booking.getCid(),
                           booking.getFromDay(),
                           booking.getToDay(),
                           booking.getToHour(),
                           booking.getMadeDate())
                   );
               }

           }
           return bookingHistoryPreviews;
       } catch (Exception e){
           log.info(e.getMessage());
           throw new RuntimeException(e);
       }

    }



    private static boolean isAvailable(String fromDay, String toDay, String fromHour, String toHour){
        // Parse date and time strings
        LocalDate fromDate = LocalDate.parse(fromDay, DateTimeFormatter.ofPattern("dd-MM-yyyy"));
        LocalTime fromTimeOfDay = LocalTime.parse(fromHour, DateTimeFormatter.ofPattern("HH:mm"));

        LocalDate toDate = LocalDate.parse(toDay, DateTimeFormatter.ofPattern("dd-MM-yyyy"));
        LocalTime toTimeOfDay = LocalTime.parse(toHour, DateTimeFormatter.ofPattern("HH:mm"));

        // Create LocalDateTime by combining date and time
        LocalDateTime fromDateTime = LocalDateTime.of(fromDate, fromTimeOfDay);
        LocalDateTime toDateTime = LocalDateTime.of(toDate, toTimeOfDay);

        // Get current date and time
        LocalDateTime currentDateTime = LocalDateTime.now();

        log.info("comparing {} with {}", toDateTime, currentDateTime);
        int fromDateComparison = currentDateTime.toLocalDate().compareTo(fromDateTime.toLocalDate());
        int toDateComparison = currentDateTime.toLocalDate().compareTo(toDateTime.toLocalDate());
        return !(fromDateComparison > 0 && toDateComparison < 0);

    }



    public Boolean getCarAvailability(Long carId) {

        // get the booking by id
        ArrayList<Booking> bookings = bookingRepository.findByCid(carId);

        // if no bookings the car is available
        if (!bookings.isEmpty()) {
            for (Booking booking : bookings) {
                // check availability
                if (!isAvailable(
                        booking.getFromDay(),
                        booking.getToDay(),
                        booking.getFromHour(),
                        booking.getToHour())) {
                    return false;
                }
            }
        }
        return true;


    }

    public void setBookingForAuction(Long cid) throws Exception {

        /*Booking booking = isAvailableForAuction(cid);
        setBooking(booking);*/

    }

    /*private Booking isAvailableForAuction(Long cid){
        // check if car is available from now
        LocalDateTime now = LocalDateTime.now();
        // Round to the nearest hour
        LocalDateTime roundedTime = now.withMinute(0).withSecond(0).withNano(0);

        roundedTime = roundedTime.plusHours(1);

        // Format the date and time
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

        String formattedDate = roundedTime.format(dateFormatter);
        String formattedTime = roundedTime.format(timeFormatter);

        // Output the formatted date and time
        System.out.println("Date: " + formattedDate);
        System.out.println("Time: " + formattedTime);

        //if (isAvailable())

    }*/
}

