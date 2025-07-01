package com.rukiyesahin.airlinereservationsystem.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "flights")
public class Flight {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Flight number is required")
    @Column(unique = true, nullable = false)
    private String flightNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "departure_airport_id", nullable = false)
    private Airport departureAirport;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "arrival_airport_id", nullable = false)
    private Airport arrivalAirport;

    @NotNull(message = "Departure time is required")
    private LocalDateTime departureTime;

    @NotNull(message = "Arrival time is required")
    private LocalDateTime arrivalTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "aircraft_id")
    private Aircraft aircraft;

    @Enumerated(EnumType.STRING)
    private FlightStatus status = FlightStatus.SCHEDULED;

    @Positive(message = "Total seats must be positive")
    private Integer totalSeats;

    @Positive(message = "Available seats must be positive")
    private Integer availableSeats;

    @NotNull(message = "Base price is required")
    @Positive(message = "Base price must be positive")
    private BigDecimal basePrice;

    private BigDecimal businessClassPrice;
    private BigDecimal firstClassPrice;
    private String gate;
    private String terminal;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "flight", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Booking> bookings = new ArrayList<>();

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        if (availableSeats == null && totalSeats != null) {
            availableSeats = totalSeats;
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    // Constructors
    public Flight() {}

    public Flight(String flightNumber, Airport departureAirport, Airport arrivalAirport,
                  LocalDateTime departureTime, LocalDateTime arrivalTime, Integer totalSeats, BigDecimal basePrice) {
        this.flightNumber = flightNumber;
        this.departureAirport = departureAirport;
        this.arrivalAirport = arrivalAirport;
        this.departureTime = departureTime;
        this.arrivalTime = arrivalTime;
        this.totalSeats = totalSeats;
        this.availableSeats = totalSeats;
        this.basePrice = basePrice;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getFlightNumber() { return flightNumber; }
    public void setFlightNumber(String flightNumber) { this.flightNumber = flightNumber; }

    public Airport getDepartureAirport() { return departureAirport; }
    public void setDepartureAirport(Airport departureAirport) { this.departureAirport = departureAirport; }

    public Airport getArrivalAirport() { return arrivalAirport; }
    public void setArrivalAirport(Airport arrivalAirport) { this.arrivalAirport = arrivalAirport; }

    public LocalDateTime getDepartureTime() { return departureTime; }
    public void setDepartureTime(LocalDateTime departureTime) { this.departureTime = departureTime; }

    public LocalDateTime getArrivalTime() { return arrivalTime; }
    public void setArrivalTime(LocalDateTime arrivalTime) { this.arrivalTime = arrivalTime; }

    public Aircraft getAircraft() { return aircraft; }
    public void setAircraft(Aircraft aircraft) { this.aircraft = aircraft; }

    public FlightStatus getStatus() { return status; }
    public void setStatus(FlightStatus status) { this.status = status; }

    public Integer getTotalSeats() { return totalSeats; }
    public void setTotalSeats(Integer totalSeats) { this.totalSeats = totalSeats; }

    public Integer getAvailableSeats() { return availableSeats; }
    public void setAvailableSeats(Integer availableSeats) { this.availableSeats = availableSeats; }

    public BigDecimal getBasePrice() { return basePrice; }
    public void setBasePrice(BigDecimal basePrice) { this.basePrice = basePrice; }

    public BigDecimal getBusinessClassPrice() { return businessClassPrice; }
    public void setBusinessClassPrice(BigDecimal businessClassPrice) { this.businessClassPrice = businessClassPrice; }

    public BigDecimal getFirstClassPrice() { return firstClassPrice; }
    public void setFirstClassPrice(BigDecimal firstClassPrice) { this.firstClassPrice = firstClassPrice; }

    public String getGate() { return gate; }
    public void setGate(String gate) { this.gate = gate; }

    public String getTerminal() { return terminal; }
    public void setTerminal(String terminal) { this.terminal = terminal; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    public List<Booking> getBookings() { return bookings; }
    public void setBookings(List<Booking> bookings) { this.bookings = bookings; }

    // Business methods
    public Duration getFlightDuration() {
        if (departureTime != null && arrivalTime != null) {
            return Duration.between(departureTime, arrivalTime);
        }
        return null;
    }

    public boolean isAvailable() {
        return FlightStatus.SCHEDULED.equals(status) && availableSeats > 0;
    }

    public boolean isFullyBooked() {
        return availableSeats <= 0;
    }

    public boolean isOverbooked() {
        return availableSeats < 0;
    }

    public boolean isDepartingSoon() {
        if (departureTime == null) return false;
        Duration timeUntilDeparture = Duration.between(LocalDateTime.now(), departureTime);
        return timeUntilDeparture.toHours() <= 2 && timeUntilDeparture.toHours() >= 0;
    }

    public boolean hasDeparted() {
        return departureTime != null && LocalDateTime.now().isAfter(departureTime);
    }

    public boolean hasArrived() {
        return arrivalTime != null && LocalDateTime.now().isAfter(arrivalTime);
    }

    public boolean canBeCancelled() {
        return !hasDeparted() && FlightStatus.SCHEDULED.equals(status);
    }

    public boolean canBeDelayed() {
        return !hasDeparted() && FlightStatus.SCHEDULED.equals(status);
    }

    public BigDecimal getPriceForClass(SeatClass seatClass) {
        return switch (seatClass) {
            case ECONOMY -> basePrice;
            case BUSINESS -> businessClassPrice != null ? businessClassPrice : basePrice.multiply(BigDecimal.valueOf(2.5));
            case FIRST -> firstClassPrice != null ? firstClassPrice : basePrice.multiply(BigDecimal.valueOf(4.0));
        };
    }

    public void bookSeat() {
        if (availableSeats > 0) {
            availableSeats--;
        }
    }

    public void cancelSeat() {
        if (availableSeats < totalSeats) {
            availableSeats++;
        }
    }

    public double getOccupancyRate() {
        if (totalSeats == null || totalSeats == 0) return 0.0;
        return ((double) (totalSeats - availableSeats) / totalSeats) * 100;
    }

    public String getRoute() {
        if (departureAirport != null && arrivalAirport != null) {
            return departureAirport.getCode() + " → " + arrivalAirport.getCode();
        }
        return "Unknown Route";
    }

    public enum FlightStatus {
        SCHEDULED, BOARDING, DEPARTED, ARRIVED, CANCELLED, DELAYED, DIVERTED
    }

    public enum SeatClass {
        ECONOMY, BUSINESS, FIRST
    }
}
