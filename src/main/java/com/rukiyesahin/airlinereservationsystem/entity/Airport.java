package com.rukiyesahin.airlinereservationsystem.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "airports")
public class Airport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Airport code is required")
    @Size(min = 3, max = 3, message = "Airport code must be exactly 3 characters")
    @Column(unique = true, nullable = false, length = 3)
    private String code;

    @NotBlank(message = "Airport name is required")
    private String name;

    @NotBlank(message = "City is required")
    private String city;

    @NotBlank(message = "Country is required")
    private String country;

    private String timezone;
    private Double latitude;
    private Double longitude;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Relationships
    @OneToMany(mappedBy = "departureAirport", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Flight> departureFlights = new ArrayList<>();

    @OneToMany(mappedBy = "arrivalAirport", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Flight> arrivalFlights = new ArrayList<>();

    // Constructors
    public Airport() {}

    public Airport(String code, String name, String city, String country) {
        this.code = code;
        this.name = name;
        this.city = city;
        this.country = country;
    }

    // JPA Callbacks
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }

    public String getCountry() { return country; }
    public void setCountry(String country) { this.country = country; }

    public String getTimezone() { return timezone; }
    public void setTimezone(String timezone) { this.timezone = timezone; }

    public Double getLatitude() { return latitude; }
    public void setLatitude(Double latitude) { this.latitude = latitude; }

    public Double getLongitude() { return longitude; }
    public void setLongitude(Double longitude) { this.longitude = longitude; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    public List<Flight> getDepartureFlights() { return departureFlights; }
    public void setDepartureFlights(List<Flight> departureFlights) { this.departureFlights = departureFlights; }

    public List<Flight> getArrivalFlights() { return arrivalFlights; }
    public void setArrivalFlights(List<Flight> arrivalFlights) { this.arrivalFlights = arrivalFlights; }

    // Business methods
    public String getFullName() {
        return name + " (" + code + ")";
    }

    public String getLocation() {
        return city + ", " + country;
    }

    public boolean isInternational() {
        // Example: treat "US" as domestic, others as international
        return !"US".equalsIgnoreCase(country);
    }

    public boolean isDomestic() {
        return "US".equalsIgnoreCase(country);
    }

    public boolean isMajorHub() {
        // Example logic: more than 100 departures or arrivals
        return (departureFlights != null && departureFlights.size() > 100)
                || (arrivalFlights != null && arrivalFlights.size() > 100);
    }

    public boolean hasCustoms() {
        return isInternational();
    }

    public boolean hasImmigration() {
        return isInternational();
    }

    // Enum for airport type (optional, for future use)
    public enum AirportType {
        INTERNATIONAL, DOMESTIC, REGIONAL, MILITARY
    }
}