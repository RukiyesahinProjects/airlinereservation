package com.rukiyesahin.airlinereservationsystem.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "aircraft")
public class Aircraft {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Aircraft registration is required")
    @Column(unique = true, nullable = false)
    private String registration;

    @NotBlank(message = "Aircraft type is required")
    private String type;

    @NotBlank(message = "Manufacturer is required")
    private String manufacturer;

    @NotBlank(message = "Model is required")
    private String model;

    @Positive(message = "Total seats must be positive")
    private Integer totalSeats;

    @Positive(message = "Economy seats must be positive")
    private Integer economySeats;

    @Positive(message = "Business seats must be positive")
    private Integer businessSeats;

    @Positive(message = "First class seats must be positive")
    private Integer firstClassSeats;

    private Integer yearOfManufacture;
    private LocalDateTime lastMaintenance;
    private LocalDateTime nextMaintenance;

    @Enumerated(EnumType.STRING)
    private AircraftStatus status = AircraftStatus.ACTIVE;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "aircraft", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Flight> flights = new ArrayList<>();

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    // Constructors
    public Aircraft() {}

    public Aircraft(String registration, String type, String manufacturer, String model,
                    Integer totalSeats, Integer economySeats, Integer businessSeats, Integer firstClassSeats) {
        this.registration = registration;
        this.type = type;
        this.manufacturer = manufacturer;
        this.model = model;
        this.totalSeats = totalSeats;
        this.economySeats = economySeats;
        this.businessSeats = businessSeats;
        this.firstClassSeats = firstClassSeats;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getRegistration() { return registration; }
    public void setRegistration(String registration) { this.registration = registration; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public String getManufacturer() { return manufacturer; }
    public void setManufacturer(String manufacturer) { this.manufacturer = manufacturer; }

    public String getModel() { return model; }
    public void setModel(String model) { this.model = model; }

    public Integer getTotalSeats() { return totalSeats; }
    public void setTotalSeats(Integer totalSeats) { this.totalSeats = totalSeats; }

    public Integer getEconomySeats() { return economySeats; }
    public void setEconomySeats(Integer economySeats) { this.economySeats = economySeats; }

    public Integer getBusinessSeats() { return businessSeats; }
    public void setBusinessSeats(Integer businessSeats) { this.businessSeats = businessSeats; }

    public Integer getFirstClassSeats() { return firstClassSeats; }
    public void setFirstClassSeats(Integer firstClassSeats) { this.firstClassSeats = firstClassSeats; }

    public Integer getYearOfManufacture() { return yearOfManufacture; }
    public void setYearOfManufacture(Integer yearOfManufacture) { this.yearOfManufacture = yearOfManufacture; }

    public LocalDateTime getLastMaintenance() { return lastMaintenance; }
    public void setLastMaintenance(LocalDateTime lastMaintenance) { this.lastMaintenance = lastMaintenance; }

    public LocalDateTime getNextMaintenance() { return nextMaintenance; }
    public void setNextMaintenance(LocalDateTime nextMaintenance) { this.nextMaintenance = nextMaintenance; }

    public AircraftStatus getStatus() { return status; }
    public void setStatus(AircraftStatus status) { this.status = status; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    public List<Flight> getFlights() { return flights; }
    public void setFlights(List<Flight> flights) { this.flights = flights; }

    // Business methods
    public String getFullDescription() {
        return manufacturer + " " + model + " (" + registration + ")";
    }

    public boolean isActive() {
        return AircraftStatus.ACTIVE.equals(status);
    }

    public boolean needsMaintenance() {
        return nextMaintenance != null && LocalDateTime.now().isAfter(nextMaintenance);
    }

    public boolean isMaintenanceDueSoon() {
        if (nextMaintenance == null) return false;
        long daysUntilMaintenance = java.time.Duration.between(LocalDateTime.now(), nextMaintenance).toDays();
        return daysUntilMaintenance <= 7 && daysUntilMaintenance >= 0;
    }

    public Integer getSeatsForClass(Flight.SeatClass seatClass) {
        return switch (seatClass) {
            case ECONOMY -> economySeats;
            case BUSINESS -> businessSeats;
            case FIRST -> firstClassSeats;
        };
    }

    public boolean hasSeatsForClass(Flight.SeatClass seatClass, int requiredSeats) {
        Integer availableSeats = getSeatsForClass(seatClass);
        return availableSeats != null && availableSeats >= requiredSeats;
    }

    public int getAge() {
        if (yearOfManufacture == null) return 0;
        return LocalDateTime.now().getYear() - yearOfManufacture;
    }

    public boolean isOldAircraft() {
        return getAge() > 20;
    }

    public void scheduleMaintenance() {
        if (lastMaintenance != null) {
            // Schedule next maintenance 6 months from last maintenance
            nextMaintenance = lastMaintenance.plusMonths(6);
        } else {
            // Schedule maintenance 6 months from now
            nextMaintenance = LocalDateTime.now().plusMonths(6);
        }
    }

    public void performMaintenance() {
        lastMaintenance = LocalDateTime.now();
        scheduleMaintenance();
    }

    public enum AircraftStatus {
        ACTIVE, MAINTENANCE, RETIRED, GROUNDED
    }
}
