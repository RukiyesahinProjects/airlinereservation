package com.rukiyesahin.airlinereservationsystem.controller;

import com.rukiyesahin.airlinereservationsystem.entity.Flight;
import com.rukiyesahin.airlinereservationsystem.service.FlightService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.Duration;
import java.util.List;

@RestController
@RequestMapping("/api/flights")
@CrossOrigin(origins = "*")
public class FlightController {

    @Autowired
    private FlightService flightService;

    // Create new flight
    @PostMapping
    public ResponseEntity<Flight> createFlight(@Valid @RequestBody Flight flight) {
        Flight createdFlight = flightService.createFlight(flight);
        return new ResponseEntity<>(createdFlight, HttpStatus.CREATED);
    }

    // Get flight by ID
    @GetMapping("/{id}")
    public ResponseEntity<Flight> getFlightById(@PathVariable Long id) {
        Flight flight = flightService.getFlightById(id);
        return ResponseEntity.ok(flight);
    }

    // Get flight by flight number
    @GetMapping("/number/{flightNumber}")
    public ResponseEntity<Flight> getFlightByNumber(@PathVariable String flightNumber) {
        Flight flight = flightService.getFlightByNumber(flightNumber);
        return ResponseEntity.ok(flight);
    }

    // Get all flights
    @GetMapping
    public ResponseEntity<List<Flight>> getAllFlights() {
        List<Flight> flights = flightService.getAllFlights();
        return ResponseEntity.ok(flights);
    }

    // Get available flights
    @GetMapping("/available")
    public ResponseEntity<List<Flight>> getAvailableFlights() {
        List<Flight> flights = flightService.getAvailableFlights();
        return ResponseEntity.ok(flights);
    }

    // Search flights by route and date
    @GetMapping("/search")
    public ResponseEntity<List<Flight>> searchFlights(
            @RequestParam String departureCode,
            @RequestParam String arrivalCode,
            @RequestParam String departureDate) {
        LocalDateTime date = LocalDateTime.parse(departureDate);
        List<Flight> flights = flightService.searchFlights(departureCode, arrivalCode, date);
        return ResponseEntity.ok(flights);
    }

    // Search flights by route and date range
    @GetMapping("/search/range")
    public ResponseEntity<List<Flight>> searchFlightsByDateRange(
            @RequestParam String departureCode,
            @RequestParam String arrivalCode,
            @RequestParam String startDate,
            @RequestParam String endDate) {
        LocalDateTime start = LocalDateTime.parse(startDate);
        LocalDateTime end = LocalDateTime.parse(endDate);
        List<Flight> flights = flightService.searchFlightsByDateRange(departureCode, arrivalCode, start, end);
        return ResponseEntity.ok(flights);
    }

    // Get flights by status
    @GetMapping("/status/{status}")
    public ResponseEntity<List<Flight>> getFlightsByStatus(@PathVariable Flight.FlightStatus status) {
        List<Flight> flights = flightService.getFlightsByStatus(status);
        return ResponseEntity.ok(flights);
    }

    // Get flights departing soon
    @GetMapping("/departing-soon")
    public ResponseEntity<List<Flight>> getFlightsDepartingSoon() {
        List<Flight> flights = flightService.getFlightsDepartingSoon();
        return ResponseEntity.ok(flights);
    }

    // Get flights by price range
    @GetMapping("/price-range")
    public ResponseEntity<List<Flight>> getFlightsByPriceRange(
            @RequestParam BigDecimal minPrice,
            @RequestParam BigDecimal maxPrice) {
        List<Flight> flights = flightService.getFlightsByPriceRange(minPrice, maxPrice);
        return ResponseEntity.ok(flights);
    }

    // Get affordable flights
    @GetMapping("/affordable")
    public ResponseEntity<List<Flight>> getAffordableFlights(@RequestParam BigDecimal maxPrice) {
        List<Flight> flights = flightService.getAffordableFlights(maxPrice);
        return ResponseEntity.ok(flights);
    }

    // Get flights by airport
    @GetMapping("/airport/{airportCode}")
    public ResponseEntity<List<Flight>> getFlightsByAirport(@PathVariable String airportCode) {
        List<Flight> flights = flightService.getFlightsByAirport(airportCode);
        return ResponseEntity.ok(flights);
    }

    // Get flights by aircraft
    @GetMapping("/aircraft/{aircraftId}")
    public ResponseEntity<List<Flight>> getFlightsByAircraft(@PathVariable Long aircraftId) {
        List<Flight> flights = flightService.getFlightsByAircraft(aircraftId);
        return ResponseEntity.ok(flights);
    }

    // Update flight
    @PutMapping("/{id}")
    public ResponseEntity<Flight> updateFlight(@PathVariable Long id, @Valid @RequestBody Flight flightDetails) {
        Flight updatedFlight = flightService.updateFlight(id, flightDetails);
        return ResponseEntity.ok(updatedFlight);
    }

    // Cancel flight
    @PutMapping("/{id}/cancel")
    public ResponseEntity<Flight> cancelFlight(@PathVariable Long id) {
        Flight flight = flightService.cancelFlight(id);
        return ResponseEntity.ok(flight);
    }

    // Delay flight
    @PutMapping("/{id}/delay")
    public ResponseEntity<Flight> delayFlight(@PathVariable Long id, @RequestBody DelayRequest request) {
        Duration delay = Duration.ofMinutes(request.getDelayMinutes());
        Flight flight = flightService.delayFlight(id, delay);
        return ResponseEntity.ok(flight);
    }

    // Book seat on flight
    @PostMapping("/{id}/book-seat")
    public ResponseEntity<Void> bookSeat(@PathVariable Long id) {
        flightService.bookSeat(id);
        return ResponseEntity.ok().build();
    }

    // Cancel seat on flight
    @PostMapping("/{id}/cancel-seat")
    public ResponseEntity<Void> cancelSeat(@PathVariable Long id) {
        flightService.cancelSeat(id);
        return ResponseEntity.ok().build();
    }

    // Get flight price for seat class
    @GetMapping("/{id}/price/{seatClass}")
    public ResponseEntity<BigDecimal> getFlightPrice(@PathVariable Long id, @PathVariable Flight.SeatClass seatClass) {
        BigDecimal price = flightService.getFlightPrice(id, seatClass);
        return ResponseEntity.ok(price);
    }

    // Get flights by country
    @GetMapping("/country/{country}")
    public ResponseEntity<List<Flight>> getFlightsByCountry(@PathVariable String country) {
        List<Flight> flights = flightService.getFlightsByCountry(country);
        return ResponseEntity.ok(flights);
    }

    // Get flights by city
    @GetMapping("/city/{city}")
    public ResponseEntity<List<Flight>> getFlightsByCity(@PathVariable String city) {
        List<Flight> flights = flightService.getFlightsByCity(city);
        return ResponseEntity.ok(flights);
    }

    // Get business class flights
    @GetMapping("/business-class")
    public ResponseEntity<List<Flight>> getBusinessClassFlights(@RequestParam BigDecimal maxPrice) {
        List<Flight> flights = flightService.getBusinessClassFlights(maxPrice);
        return ResponseEntity.ok(flights);
    }

    // Get first class flights
    @GetMapping("/first-class")
    public ResponseEntity<List<Flight>> getFirstClassFlights(@RequestParam BigDecimal maxPrice) {
        List<Flight> flights = flightService.getFirstClassFlights(maxPrice);
        return ResponseEntity.ok(flights);
    }

    // Get fully booked flights
    @GetMapping("/fully-booked")
    public ResponseEntity<List<Flight>> getFullyBookedFlights() {
        List<Flight> flights = flightService.getFullyBookedFlights();
        return ResponseEntity.ok(flights);
    }

    // Get overbooked flights
    @GetMapping("/overbooked")
    public ResponseEntity<List<Flight>> getOverbookedFlights() {
        List<Flight> flights = flightService.getOverbookedFlights();
        return ResponseEntity.ok(flights);
    }

    // Get overdue flights
    @GetMapping("/overdue")
    public ResponseEntity<List<Flight>> getOverdueFlights() {
        List<Flight> flights = flightService.getOverdueFlights();
        return ResponseEntity.ok(flights);
    }

    // Count scheduled flights in period
    @GetMapping("/count/scheduled")
    public ResponseEntity<Long> countScheduledFlightsInPeriod(
            @RequestParam String startDate,
            @RequestParam String endDate) {
        LocalDateTime start = LocalDateTime.parse(startDate);
        LocalDateTime end = LocalDateTime.parse(endDate);
        long count = flightService.countScheduledFlightsInPeriod(start, end);
        return ResponseEntity.ok(count);
    }

    // DTO classes for request/response
    public static class DelayRequest {
        private int delayMinutes;

        public int getDelayMinutes() { return delayMinutes; }
        public void setDelayMinutes(int delayMinutes) { this.delayMinutes = delayMinutes; }
    }
}