package com.example.carbook.model.booking;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    @Query(value = "SELECT *\n" +
            "FROM bookings b\n" +
            "WHERE b.cid = ?1\n" +
            "  AND (\n" +
            "        (TO_DATE(b.from_day, 'DD-MM-YYYY') < TO_DATE(?3, 'DD-MM-YYYY') AND TO_DATE(b.to_day, 'DD-MM-YYYY') > TO_DATE(?2, 'DD-MM-YYYY'))\n" +
            "        OR\n" +
            "        (TO_DATE(b.from_day, 'DD-MM-YYYY') = TO_DATE(?3, 'DD-MM-YYYY') AND TO_TIMESTAMP(b.from_hour, 'HH24-MI') <= TO_TIMESTAMP(?5, 'HH24-MI'))\n" +
            "        OR\n" +
            "        (TO_DATE(b.to_day, 'DD-MM-YYYY') = TO_DATE(?2, 'DD-MM-YYYY') AND TO_TIMESTAMP(b.to_hour, 'HH24-MI') >= TO_TIMESTAMP(?4, 'HH24-MI'))\n" +
            "      );", nativeQuery = true)
    List<Booking> findOverlappingBookings(
            @Param("cid") Long cid,
            @Param("fromDay") String fromDay,
            @Param("toDay") String toDay,
            @Param("fromHour") String fromHour,
            @Param("toHour") String toHour
    );

    @Query(value = "SELECT *\n" +
            "FROM bookings b\n" +
            "WHERE b.username = ?5\n" +
            "  AND (\n" +
            "        (TO_DATE(b.from_day, 'DD-MM-YYYY') < TO_DATE(?2, 'DD-MM-YYYY') AND TO_DATE(b.to_day, 'DD-MM-YYYY') > TO_DATE(?1, 'DD-MM-YYYY'))\n" +
            "        OR\n" +
            "        (TO_DATE(b.from_day, 'DD-MM-YYYY') = TO_DATE(?2, 'DD-MM-YYYY') AND TO_TIMESTAMP(b.from_hour, 'HH24-MI') <= TO_TIMESTAMP(?4, 'HH24-MI'))\n" +
            "        OR\n" +
            "        (TO_DATE(b.to_day, 'DD-MM-YYYY') = TO_DATE(?1, 'DD-MM-YYYY') AND TO_TIMESTAMP(b.to_hour, 'HH24-MI') >= TO_TIMESTAMP(?3, 'HH24-MI'))\n" +
            "      );", nativeQuery = true)
    List<Booking> findOverlappingBookingsForUser(
            @Param("fromDay") String fromDay,
            @Param("toDay") String toDay,
            @Param("fromHour") String fromHour,
            @Param("toHour") String toHour,
            @Param("username") String username
    );

    ArrayList<Booking> findByUsername(String username);
    ArrayList<Booking> findByCid(Long cid);
    ArrayList<Booking> findByUsernameAndCid(String username, Long Cid);
    ArrayList<Booking> findByUsernameAndMadeDate(String username, String madeDate);
    ArrayList<Booking> findByUsernameAndFromDay(String username, String from);
    ArrayList<Booking> findByUsernameAndToDay(String username, String to);
    void deleteBookingByBid(Long bid);
}