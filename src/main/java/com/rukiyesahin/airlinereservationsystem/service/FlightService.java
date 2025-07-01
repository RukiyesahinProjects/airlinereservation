package com.rukiyesahin.airlinereservationsystem.service;



import com.rukiyesahin.airlinereservationsystem.entity.Flight;
import com.rukiyesahin.airlinereservationsystem.entity.Airport;
import com.rukiyesahin.airlinereservationsystem.entity.Aircraft;
import com.rukiyesahin.airlinereservationsystem.repository.FlightRepository;
import com.rukiyesahin.airlinereservationsystem.repository.AirportRepository;
import com.rukiyesahin.airlinereservationsystem.repository.AircraftRepository;
import com.rukiyesahin.airlinereservationsystem.exception.FlightNotFoundException;
import com.rukiyesahin.airlinereservationsystem.exception.InvalidFlightDataException;
import com.rukiyesahin.airlinereservationsystem.exception.FlightNotAvailableException;
import com.rukiyesahin.airlinereservationsystem.exception.AircraftNotAvailableException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.Duration;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class FlightService {

    @Autowired
    private FlightRepository flightRepository;

    @Autowired
    private AirportRepository airportRepository;

    @Autowired
    private AircraftRepository aircraftRepository;

    @Value("${airline.booking.min-hours-before-departure:2}")
    private int minHoursBeforeDeparture;

    @Value("${airline.pricing.base-price:100.0}")
    private BigDecimal basePrice;

    @Value("${airline.pricing.business-class-multiplier:2.5}")
    private BigDecimal businessClassMultiplier;

    @Value("${airline.pricing.first-class-multiplier:4.0}")
    private BigDecimal firstClassMultiplier;

    // Create new flight
    public Flight createFlight(Flight flight) {
        validateFlightData(flight);
        validateFlightSchedule(flight);
        validateAircraftAvailability(flight);

        // Set default prices if not provided
        if (flight.getBusinessClassPrice() == null) {
            flight.setBusinessClassPrice(flight.getBasePrice().multiply(businessClassMultiplier));
        }
        if (flight.getFirstClassPrice() == null) {
            flight.setFirstClassPrice(flight.getBasePrice().multiply(firstClassMultiplier));
        }

        // Set available seats equal to total seats initially
        if (flight.getAvailableSeats() == null) {
            flight.setAvailableSeats(flight.getTotalSeats());
        }

        return flightRepository.save(flight);
    }

    // Get flight by ID
    public Flight getFlightById(Long id) {
        return flightRepository.findById(id).orElseThrow(() -> new FlightNotFoundException("Flight not found with ID: " + id));
    }

    // Get flight by flight number
    public Flight getFlightByNumber(String flightNumber) {
        return flightRepository.findByFlightNumber(flightNumber)
                .orElseThrow(() -> new FlightNotFoundException("Flight not found with number: " + flightNumber));
    }

    // Get all flights
    public List<Flight> getAllFlights() {
        return flightRepository.findAll();
    }

    // Get available flights
    public List<Flight> getAvailableFlights() {
        return flightRepository.findUpcomingAvailableFlights(LocalDateTime.now());
    }

    // Search flights by route and date
    public List<Flight> searchFlights(String departureCode, String arrivalCode, LocalDateTime departureDate) {
        Airport departure = airportRepository.findByCode(departureCode)
                .orElseThrow(() -> new FlightNotFoundException("Departure airport not found: " + departureCode));
        Airport arrival = airportRepository.findByCode(arrivalCode)
                .orElseThrow(() -> new FlightNotFoundException("Arrival airport not found: " + arrivalCode));

        LocalDateTime endDate = departureDate.plusDays(1);
        return flightRepository.findAvailableFlights(departure, arrival, departureDate, endDate);
    }

    // Search flights by route and date range
    public List<Flight> searchFlightsByDateRange(String departureCode, String arrivalCode,
                                                 LocalDateTime startDate, LocalDateTime endDate) {
        return flightRepository.findAvailableFlightsByRouteAndDate(departureCode, arrivalCode, startDate, endDate);
    }

    // Get flights by status
    public List<Flight> getFlightsByStatus(Flight.FlightStatus status) {
        return flightRepository.findByStatus(status);
    }

    // Get flights departing soon
    public List<Flight> getFlightsDepartingSoon() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime soon = now.plusHours(2);
        return flightRepository.findFlightsDepartingSoon(now, soon);
    }

    // Get flights by price range
    public List<Flight> getFlightsByPriceRange(BigDecimal minPrice, BigDecimal maxPrice) {
        return flightRepository.findByBasePriceBetween(minPrice, maxPrice);
    }

    // Get affordable flights
    public List<Flight> getAffordableFlights(BigDecimal maxPrice) {
        return flightRepository.findAffordableFlights(maxPrice);
    }

    // Get flights by airport
    public List<Flight> getFlightsByAirport(String airportCode) {
        Airport airport = airportRepository.findByCode(airportCode)
                .orElseThrow(() -> new FlightNotFoundException("Airport not found: " + airportCode));
        return flightRepository.findFlightsByAirport(airport);
    }

    // Get flights by aircraft
    public List<Flight> getFlightsByAircraft(Long aircraftId) {
        return flightRepository.findByAircraftId(aircraftId);
    }

    // Update flight
    public Flight updateFlight(Long id, Flight flightDetails) {
        Flight flight = getFlightById(id);

        // Update fields if provided
        if (flightDetails.getDepartureTime() != null) {
            validateDepartureTime(flightDetails.getDepartureTime());
            flight.setDepartureTime(flightDetails.getDepartureTime());
        }
        if (flightDetails.getArrivalTime() != null) {
            flight.setArrivalTime(flightDetails.getArrivalTime());
        }
        if (flightDetails.getStatus() != null) {
            flight.setStatus(flightDetails.getStatus());
        }
        if (flightDetails.getGate() != null) {
            flight.setGate(flightDetails.getGate());
        }
        if (flightDetails.getTerminal() != null) {
            flight.setTerminal(flightDetails.getTerminal());
        }
        if (flightDetails.getBasePrice() != null) {
            flight.setBasePrice(flightDetails.getBasePrice());
        }
        if (flightDetails.getBusinessClassPrice() != null) {
            flight.setBusinessClassPrice(flightDetails.getBusinessClassPrice());
        }
        if (flightDetails.getFirstClassPrice() != null) {
            flight.setFirstClassPrice(flightDetails.getFirstClassPrice());
        }

        return flightRepository.save(flight);
    }

    // Cancel flight
    public Flight cancelFlight(Long id) {
        Flight flight = getFlightById(id);
        if (!flight.canBeCancelled()) {
            throw new FlightNotAvailableException("Flight cannot be cancelled");
        }
        flight.setStatus(Flight.FlightStatus.CANCELLED);
        return flightRepository.save(flight);
    }

    // Delay flight
    public Flight delayFlight(Long id, Duration delay) {
        Flight flight = getFlightById(id);
        if (!flight.canBeDelayed()) {
            throw new FlightNotAvailableException("Flight cannot be delayed");
        }
        flight.setDepartureTime(flight.getDepartureTime().plus(delay));
        flight.setArrivalTime(flight.getArrivalTime().plus(delay));
        flight.setStatus(Flight.FlightStatus.DELAYED);
        return flightRepository.save(flight);
    }

    // Book seat on flight
    public void bookSeat(Long flightId) {
        Flight flight = getFlightById(flightId);
        if (!flight.isAvailable()) {
            throw new FlightNotAvailableException("Flight is not available for booking");
        }
        flight.bookSeat();
        flightRepository.save(flight);
    }

    // Cancel seat on flight
    public void cancelSeat(Long flightId) {
        Flight flight = getFlightById(flightId);
        flight.cancelSeat();
        flightRepository.save(flight);
    }

    // Get flight price for seat class
    public BigDecimal getFlightPrice(Long flightId, Flight.SeatClass seatClass) {
        Flight flight = getFlightById(flightId);
        return flight.getPriceForClass(seatClass);
    }

    // Get flights by country
    public List<Flight> getFlightsByCountry(String country) {
        return flightRepository.findFlightsByCountry(country);
    }

    // Get flights by city
    public List<Flight> getFlightsByCity(String city) {
        return flightRepository.findFlightsByCity(city);
    }

    // Get business class flights
    public List<Flight> getBusinessClassFlights(BigDecimal maxPrice) {
        return flightRepository.findBusinessClassFlights(maxPrice);
    }

    // Get first class flights
    public List<Flight> getFirstClassFlights(BigDecimal maxPrice) {
        return flightRepository.findFirstClassFlights(maxPrice);
    }

    // Get fully booked flights
    public List<Flight> getFullyBookedFlights() {
        return flightRepository.findFullyBookedFlights();
    }

    // Get overbooked flights
    public List<Flight> getOverbookedFlights() {
        return flightRepository.findOverbookedFlights();
    }

    // Get overdue flights
    public List<Flight> getOverdueFlights() {
        return flightRepository.findOverdueFlights(LocalDateTime.now());
    }

    // Count scheduled flights in period
    public long countScheduledFlightsInPeriod(LocalDateTime startDate, LocalDateTime endDate) {
        return flightRepository.countScheduledFlightsInPeriod(startDate, endDate);
    }

    // Business validation methods
    private void validateFlightData(Flight flight) {
        if (flight.getFlightNumber() == null || flight.getFlightNumber().trim().isEmpty()) {
            throw new InvalidFlightDataException("Flight number is required");
        }
        if (flight.getDepartureAirport() == null) {
            throw new InvalidFlightDataException("Departure airport is required");
        }
        if (flight.getArrivalAirport() == null) {
            throw new InvalidFlightDataException("Arrival airport is required");
        }
        if (flight.getDepartureTime() == null) {
            throw new InvalidFlightDataException("Departure time is required");
        }
        if (flight.getArrivalTime() == null) {
            throw new InvalidFlightDataException("Arrival time is required");
        }
        if (flight.getTotalSeats() == null || flight.getTotalSeats() <= 0) {
            throw new InvalidFlightDataException("Total seats must be positive");
        }
        if (flight.getBasePrice() == null || flight.getBasePrice().compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidFlightDataException("Base price must be positive");
        }

        // Validate that departure and arrival airports are different
        if (flight.getDepartureAirport().equals(flight.getArrivalAirport())) {
            throw new InvalidFlightDataException("Departure and arrival airports must be different");
        }

        // Validate that arrival time is after departure time
        if (flight.getArrivalTime().isBefore(flight.getDepartureTime())) {
            throw new InvalidFlightDataException("Arrival time must be after departure time");
        }
    }

    private void validateFlightSchedule(Flight flight) {
        LocalDateTime now = LocalDateTime.now();

        // Check if departure time is in the future
        if (flight.getDepartureTime().isBefore(now)) {
            throw new InvalidFlightDataException("Departure time must be in the future");
        }

        // Check minimum time before departure for booking
        Duration timeUntilDeparture = Duration.between(now, flight.getDepartureTime());
        if (timeUntilDeparture.toHours() < minHoursBeforeDeparture) {
            throw new InvalidFlightDataException("Flight must be scheduled at least " + minHoursBeforeDeparture + " hours in advance");
        }
    }

    private void validateAircraftAvailability(Flight aircraft) {
        if (aircraft.getAircraft() != null) {
            Aircraft aircraftEntity = aircraftRepository.findById(aircraft.getAircraft().getId())
                    .orElseThrow(() -> new AircraftNotAvailableException("Aircraft not found"));

            if (!aircraftEntity.isActive()) {
                throw new AircraftNotAvailableException("Aircraft is not available for flights");
            }

            if (aircraftEntity.needsMaintenance()) {
                throw new AircraftNotAvailableException("Aircraft needs maintenance");
            }
        }
    }

    private void validateDepartureTime(LocalDateTime departureTime) {
        LocalDateTime now = LocalDateTime.now();
        if (departureTime.isBefore(now)) {
            throw new InvalidFlightDataException("Departure time cannot be in the past");
        }
    }
}