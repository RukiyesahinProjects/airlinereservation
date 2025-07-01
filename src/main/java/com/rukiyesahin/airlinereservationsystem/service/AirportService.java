package com.rukiyesahin.airlinereservationsystem.service;

import com.rukiyesahin.airlinereservationsystem.entity.Airport;
import com.rukiyesahin.airlinereservationsystem.repository.AirportRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class AirportService {

    @Autowired
    private AirportRepository airportRepository;

    // Create new airport
    public Airport createAirport(Airport airport) {
        return airportRepository.save(airport);
    }

    // Get airport by ID
    public Airport getAirportById(Long id) {
        return airportRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Airport not found with ID: " + id));
    }

    // Get airport by code
    public Airport getAirportByCode(String code) {
        return airportRepository.findByCode(code)
                .orElseThrow(() -> new RuntimeException("Airport not found with code: " + code));
    }

    // Get all airports
    public List<Airport> getAllAirports() {
        return airportRepository.findAll();
    }

    // Get airports by city
    public List<Airport> getAirportsByCity(String city) {
        return airportRepository.findByCity(city);
    }

    // Get airports by country
    public List<Airport> getAirportsByCountry(String country) {
        return airportRepository.findByCountry(country);
    }

    // Get airports by city and country
    public List<Airport> getAirportsByCityAndCountry(String city, String country) {
        return airportRepository.findByCityAndCountry(city, country);
    }

    // Search airports by name or city
    public List<Airport> searchAirports(String searchTerm) {
        return airportRepository.findByNameOrCityContaining(searchTerm);
    }

    // Search airports by name (alias for controller)
    public List<Airport> searchAirportsByName(String name) {
        return searchAirports(name);
    }

    // Get all countries
    public List<String> getAllCountries() {
        return airportRepository.findAllCountries();
    }

    // Get airports with coordinates
    public List<Airport> getAirportsWithCoordinates() {
        return airportRepository.findAirportsWithCoordinates();
    }

    // Get active airports
    public List<Airport> getActiveAirports() {
        return airportRepository.findActiveAirports();
    }

    // Get inactive airports
    public List<Airport> getInactiveAirports() {
        return airportRepository.findInactiveAirports();
    }

    // Get major hubs
    public List<Airport> getMajorHubs() {
        return airportRepository.findMajorHubs();
    }

    // Get airports by type
    public List<Airport> getAirportsByType(Airport type) {
        // This would need a custom query - for now return all airports
        return airportRepository.findAll();
    }

    // Get international airports
    public List<Airport> getInternationalAirports() {
        return airportRepository.findAll().stream()
                .filter(Airport::isInternational)
                .toList();
    }

    // Get domestic airports
    public List<Airport> getDomesticAirports() {
        return airportRepository.findAll().stream()
                .filter(Airport::isDomestic)
                .toList();
    }

    // Get major airports
    public List<Airport> getMajorAirports() {
        return airportRepository.findAll().stream()
                .filter(Airport::isMajorHub)
                .toList();
    }

    // Get regional airports
    public List<Airport> getRegionalAirports() {
        // This would need a custom query - for now return non-major airports
        return airportRepository.findAll().stream()
                .filter(airport -> !airport.isMajorHub())
                .toList();
    }

    // Get airports with customs
    public List<Airport> getAirportsWithCustoms() {
        return airportRepository.findAll().stream()
                .filter(Airport::hasCustoms)
                .toList();
    }

    // Get airports with immigration
    public List<Airport> getAirportsWithImmigration() {
        return airportRepository.findAll().stream()
                .filter(Airport::hasImmigration)
                .toList();
    }

    // Get hub airports
    public List<Airport> getHubAirports() {
        return getMajorAirports();
    }

    // Update airport
    public Airport updateAirport(Long id, Airport airportDetails) {
        Airport airport = getAirportById(id);

        if (airportDetails.getCode() != null) {
            airport.setCode(airportDetails.getCode());
        }
        if (airportDetails.getName() != null) {
            airport.setName(airportDetails.getName());
        }
        if (airportDetails.getCity() != null) {
            airport.setCity(airportDetails.getCity());
        }
        if (airportDetails.getCountry() != null) {
            airport.setCountry(airportDetails.getCountry());
        }
        if (airportDetails.getTimezone() != null) {
            airport.setTimezone(airportDetails.getTimezone());
        }
        if (airportDetails.getLatitude() != null) {
            airport.setLatitude(airportDetails.getLatitude());
        }
        if (airportDetails.getLongitude() != null) {
            airport.setLongitude(airportDetails.getLongitude());
        }

        return airportRepository.save(airport);
    }

    // Toggle airport status (placeholder - would need status field in entity)
    public Airport toggleAirportStatus(Long id) {
        Airport airport = getAirportById(id);
        // This would need a status field in the Airport entity
        return airportRepository.save(airport);
    }

    // Delete airport
    public void deleteAirport(Long id) {
        Airport airport = getAirportById(id);
        airportRepository.delete(airport);
    }

    // Check if airport exists
    public boolean airportExists(String code) {
        return airportRepository.findByCode(code).isPresent();
    }

    // Check if airport code exists (alias for controller)
    public boolean airportCodeExists(String code) {
        return airportExists(code);
    }

    // Get airport count by country
    public long getAirportCountByCountry(String country) {
        return airportRepository.countAirportsByCountry(country);
    }

    // Count airports by country (alias for controller)
    public long countAirportsByCountry(String country) {
        return getAirportCountByCountry(country);
    }

    // Count airports by type
    public long countAirportsByType(Airport.AirportType type) {
        // This would need a custom query - for now return 0
        return 0;
    }

    // Get airports by country ordered by city
    public List<Airport> getAirportsByCountryOrderByCity(String country) {
        return airportRepository.findByCountryOrderByCity(country);
    }
}