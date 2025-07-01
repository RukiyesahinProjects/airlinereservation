package com.rukiyesahin.airlinereservationsystem.repository;

import com.rukiyesahin.airlinereservationsystem.entity.Flight;
import com.rukiyesahin.airlinereservationsystem.entity.Airport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface FlightRepository extends JpaRepository<Flight, Long> {

    Optional<Flight> findByFlightNumber(String flightNumber);

    List<Flight> findByDepartureAirport(Airport departureAirport);

    List<Flight> findByArrivalAirport(Airport arrivalAirport);

    List<Flight> findByDepartureAirportAndArrivalAirport(Airport departureAirport, Airport arrivalAirport);

    List<Flight> findByStatus(Flight.FlightStatus status);

    List<Flight> findByDepartureTimeBetween(LocalDateTime startTime, LocalDateTime endTime);

    List<Flight> findByArrivalTimeBetween(LocalDateTime startTime, LocalDateTime endTime);

    List<Flight> findByAvailableSeatsGreaterThan(Integer minSeats);

    List<Flight> findByBasePriceBetween(BigDecimal minPrice, BigDecimal maxPrice);

    List<Flight> findByAircraftId(Long aircraftId);

    @Query("SELECT f FROM Flight f WHERE f.departureAirport = :departure AND f.arrivalAirport = :arrival AND f.departureTime >= :startDate AND f.departureTime <= :endDate AND f.status = 'SCHEDULED'")
    List<Flight> findAvailableFlights(@Param("departure") Airport departure,
                                      @Param("arrival") Airport arrival,
                                      @Param("startDate") LocalDateTime startDate,
                                      @Param("endDate") LocalDateTime endDate);

    @Query("SELECT f FROM Flight f WHERE f.departureAirport.code = :departureCode AND f.arrivalAirport.code = :arrivalCode AND f.departureTime >= :startDate AND f.status = 'SCHEDULED' ORDER BY f.departureTime")
    List<Flight> findFlightsByRoute(@Param("departureCode") String departureCode,
                                    @Param("arrivalCode") String arrivalCode,
                                    @Param("startDate") LocalDateTime startDate);

    @Query("SELECT f FROM Flight f WHERE f.departureTime >= :now AND f.availableSeats > 0 AND f.status = 'SCHEDULED' ORDER BY f.departureTime")
    List<Flight> findUpcomingAvailableFlights(@Param("now") LocalDateTime now);

    @Query("SELECT f FROM Flight f WHERE f.departureTime >= :startDate AND f.departureTime <= :endDate AND f.status = 'SCHEDULED'")
    List<Flight> findScheduledFlightsInPeriod(@Param("startDate") LocalDateTime startDate,
                                              @Param("endDate") LocalDateTime endDate);

    @Query("SELECT f FROM Flight f WHERE f.departureTime < :now AND f.status = 'SCHEDULED'")
    List<Flight> findOverdueFlights(@Param("now") LocalDateTime now);

    @Query("SELECT f FROM Flight f WHERE f.availableSeats = 0 AND f.status = 'SCHEDULED'")
    List<Flight> findFullyBookedFlights();

    @Query("SELECT f FROM Flight f WHERE f.availableSeats < 0")
    List<Flight> findOverbookedFlights();

    @Query("SELECT f FROM Flight f WHERE f.departureTime >= :now AND f.departureTime <= :soon AND f.status = 'SCHEDULED'")
    List<Flight> findFlightsDepartingSoon(@Param("now") LocalDateTime now,
                                          @Param("soon") LocalDateTime soon);

    @Query("SELECT f FROM Flight f WHERE f.basePrice <= :maxPrice AND f.availableSeats > 0 AND f.status = 'SCHEDULED' ORDER BY f.basePrice")
    List<Flight> findAffordableFlights(@Param("maxPrice") BigDecimal maxPrice);

    @Query("SELECT f FROM Flight f WHERE f.departureAirport = :airport OR f.arrivalAirport = :airport ORDER BY f.departureTime")
    List<Flight> findFlightsByAirport(@Param("airport") Airport airport);

    @Query("SELECT f FROM Flight f WHERE f.departureTime >= :startDate AND f.departureTime <= :endDate AND f.status = :status")
    List<Flight> findFlightsByStatusAndPeriod(@Param("startDate") LocalDateTime startDate,
                                              @Param("endDate") LocalDateTime endDate,
                                              @Param("status") Flight.FlightStatus status);

    @Query("SELECT f FROM Flight f WHERE f.availableSeats >= :minSeats AND f.availableSeats <= :maxSeats AND f.status = 'SCHEDULED'")
    List<Flight> findFlightsBySeatAvailability(@Param("minSeats") Integer minSeats,
                                               @Param("maxSeats") Integer maxSeats);

    @Query("SELECT f FROM Flight f WHERE f.departureTime >= :now AND f.status = 'SCHEDULED' ORDER BY f.departureTime LIMIT :limit")
    List<Flight> findNextFlights(@Param("now") LocalDateTime now, @Param("limit") int limit);

    @Query("SELECT f FROM Flight f WHERE f.departureAirport.country = :country OR f.arrivalAirport.country = :country")
    List<Flight> findFlightsByCountry(@Param("country") String country);

    @Query("SELECT f FROM Flight f WHERE f.departureAirport.city = :city OR f.arrivalAirport.city = :city")
    List<Flight> findFlightsByCity(@Param("city") String city);

    @Query("SELECT f FROM Flight f WHERE f.businessClassPrice IS NOT NULL AND f.businessClassPrice <= :maxPrice AND f.availableSeats > 0")
    List<Flight> findBusinessClassFlights(@Param("maxPrice") BigDecimal maxPrice);

    @Query("SELECT f FROM Flight f WHERE f.firstClassPrice IS NOT NULL AND f.firstClassPrice <= :maxPrice AND f.availableSeats > 0")
    List<Flight> findFirstClassFlights(@Param("maxPrice") BigDecimal maxPrice);

    @Query("SELECT f FROM Flight f WHERE f.departureTime >= :startDate AND f.departureTime <= :endDate AND f.availableSeats > 0 ORDER BY f.basePrice")
    List<Flight> findAvailableFlightsByPriceRange(@Param("startDate") LocalDateTime startDate,
                                                  @Param("endDate") LocalDateTime endDate);

    @Query("SELECT COUNT(f) FROM Flight f WHERE f.departureTime >= :startDate AND f.departureTime <= :endDate AND f.status = 'SCHEDULED'")
    long countScheduledFlightsInPeriod(@Param("startDate") LocalDateTime startDate,
                                       @Param("endDate") LocalDateTime endDate);

    @Query("SELECT f FROM Flight f WHERE f.departureTime >= :now AND f.status = 'SCHEDULED' AND f.availableSeats > 0 ORDER BY f.departureTime")
    List<Flight> findUpcomingAvailableFlightsOrdered(@Param("now") LocalDateTime now);

    @Query("SELECT f FROM Flight f WHERE f.departureAirport.code = :departureCode AND f.arrivalAirport.code = :arrivalCode AND f.departureTime >= :startDate AND f.departureTime <= :endDate AND f.availableSeats > 0 ORDER BY f.departureTime")
    List<Flight> findAvailableFlightsByRouteAndDate(@Param("departureCode") String departureCode,
                                                    @Param("arrivalCode") String arrivalCode,
                                                    @Param("startDate") LocalDateTime startDate,
                                                    @Param("endDate") LocalDateTime endDate);

    @Query("SELECT f FROM Flight f WHERE f.departureTime >= :now AND f.status = 'SCHEDULED' AND f.availableSeats > 0 AND f.basePrice <= :maxPrice ORDER BY f.basePrice")
    List<Flight> findAffordableUpcomingFlights(@Param("now") LocalDateTime now,
                                               @Param("maxPrice") BigDecimal maxPrice);
}