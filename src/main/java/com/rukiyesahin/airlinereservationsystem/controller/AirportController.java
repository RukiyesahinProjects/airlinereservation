package com.rukiyesahin.airlinereservationsystem.controller;

import com.rukiyesahin.airlinereservationsystem.entity.Airport;
import com.rukiyesahin.airlinereservationsystem.service.AirportService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/airports")
@CrossOrigin(origins = "*")
public class AirportController {

    @Autowired
    private AirportService airportService;

    // Create new airport
    @PostMapping
    public ResponseEntity<Airport> createAirport(@Valid @RequestBody Airport airport) {
        Airport createdAirport = airportService.createAirport(airport);
        return new ResponseEntity<>(createdAirport, HttpStatus.CREATED);
    }

    // Get airport by ID
    @GetMapping("/{id}")
    public ResponseEntity<Airport> getAirportById(@PathVariable Long id) {
        Airport airport = airportService.getAirportById(id);
        return ResponseEntity.ok(airport);
    }

    // Get airport by code
    @GetMapping("/code/{code}")
    public ResponseEntity<Airport> getAirportByCode(@PathVariable String code) {
        Airport airport = airportService.getAirportByCode(code);
        return ResponseEntity.ok(airport);
    }

    // Get all airports
    @GetMapping
    public ResponseEntity<List<Airport>> getAllAirports() {
        List<Airport> airports = airportService.getAllAirports();
        return ResponseEntity.ok(airports);
    }

    // Get active airports
    @GetMapping("/active")
    public ResponseEntity<List<Airport>> getActiveAirports() {
        List<Airport> airports = airportService.getActiveAirports();
        return ResponseEntity.ok(airports);
    }

    // Get airports by country
    @GetMapping("/country/{country}")
    public ResponseEntity<List<Airport>> getAirportsByCountry(@PathVariable String country) {
        List<Airport> airports = airportService.getAirportsByCountry(country);
        return ResponseEntity.ok(airports);
    }

    // Get airports by city
    @GetMapping("/city/{city}")
    public ResponseEntity<List<Airport>> getAirportsByCity(@PathVariable String city) {
        List<Airport> airports = airportService.getAirportsByCity(city);
        return ResponseEntity.ok(airports);
    }

    // Get airports by type
   /* @GetMapping("/type/{type}")
    public ResponseEntity<List<Airport>> getAirportsByType(@PathVariable Airport.AirportType type) {
        List<Airport> airports = airportService.getAirportsByType(type);
        return ResponseEntity.ok(airports);
    }*/

    // Search airports by name
    @GetMapping("/search")
    public ResponseEntity<List<Airport>> searchAirportsByName(@RequestParam String name) {
        List<Airport> airports = airportService.searchAirportsByName(name);
        return ResponseEntity.ok(airports);
    }

    // Get international airports
    @GetMapping("/international")
    public ResponseEntity<List<Airport>> getInternationalAirports() {
        List<Airport> airports = airportService.getInternationalAirports();
        return ResponseEntity.ok(airports);
    }

    // Get domestic airports
    @GetMapping("/domestic")
    public ResponseEntity<List<Airport>> getDomesticAirports() {
        List<Airport> airports = airportService.getDomesticAirports();
        return ResponseEntity.ok(airports);
    }

    // Get major airports
    @GetMapping("/major")
    public ResponseEntity<List<Airport>> getMajorAirports() {
        List<Airport> airports = airportService.getMajorAirports();
        return ResponseEntity.ok(airports);
    }

    // Get regional airports
    @GetMapping("/regional")
    public ResponseEntity<List<Airport>> getRegionalAirports() {
        List<Airport> airports = airportService.getRegionalAirports();
        return ResponseEntity.ok(airports);
    }

    // Update airport
    @PutMapping("/{id}")
    public ResponseEntity<Airport> updateAirport(@PathVariable Long id, @Valid @RequestBody Airport airportDetails) {
        Airport updatedAirport = airportService.updateAirport(id, airportDetails);
        return ResponseEntity.ok(updatedAirport);
    }

    // Toggle airport status
    @PutMapping("/{id}/toggle-status")
    public ResponseEntity<Airport> toggleAirportStatus(@PathVariable Long id) {
        Airport airport = airportService.toggleAirportStatus(id);
        return ResponseEntity.ok(airport);
    }

    // Delete airport
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAirport(@PathVariable Long id) {
        airportService.deleteAirport(id);
        return ResponseEntity.noContent().build();
    }

    // Count airports by country
    @GetMapping("/count/country/{country}")
    public ResponseEntity<Long> countAirportsByCountry(@PathVariable String country) {
        long count = airportService.countAirportsByCountry(country);
        return ResponseEntity.ok(count);
    }

    // Count airports by type
   /* @GetMapping("/count/type/{type}")
    public ResponseEntity<Long> countAirportsByType(@PathVariable Airport.AirportType type) {
        long count = airportService.countAirportsByType(type);
        return ResponseEntity.ok(count);
    }*/

    // Check if airport code exists
    @GetMapping("/exists/code/{code}")
    public ResponseEntity<Boolean> airportCodeExists(@PathVariable String code) {
        boolean exists = airportService.airportCodeExists(code);
        return ResponseEntity.ok(exists);
    }

    // Get airports with customs
    @GetMapping("/customs")
    public ResponseEntity<List<Airport>> getAirportsWithCustoms() {
        List<Airport> airports = airportService.getAirportsWithCustoms();
        return ResponseEntity.ok(airports);
    }

    // Get airports with immigration
    @GetMapping("/immigration")
    public ResponseEntity<List<Airport>> getAirportsWithImmigration() {
        List<Airport> airports = airportService.getAirportsWithImmigration();
        return ResponseEntity.ok(airports);
    }

    // Get hub airports
    @GetMapping("/hubs")
    public ResponseEntity<List<Airport>> getHubAirports() {
        List<Airport> airports = airportService.getHubAirports();
        return ResponseEntity.ok(airports);
    }

    // Get airports by city and country
    @GetMapping("/city/{city}/country/{country}")
    public ResponseEntity<List<Airport>> getAirportsByCityAndCountry(
            @PathVariable String city,
            @PathVariable String country) {
        List<Airport> airports = airportService.getAirportsByCityAndCountry(city, country);
        return ResponseEntity.ok(airports);
    }

    // Get all countries
    @GetMapping("/countries")
    public ResponseEntity<List<String>> getAllCountries() {
        List<String> countries = airportService.getAllCountries();
        return ResponseEntity.ok(countries);
    }

    // Get airports with coordinates
    @GetMapping("/with-coordinates")
    public ResponseEntity<List<Airport>> getAirportsWithCoordinates() {
        List<Airport> airports = airportService.getAirportsWithCoordinates();
        return ResponseEntity.ok(airports);
    }

    // Get inactive airports
    @GetMapping("/inactive")
    public ResponseEntity<List<Airport>> getInactiveAirports() {
        List<Airport> airports = airportService.getInactiveAirports();
        return ResponseEntity.ok(airports);
    }

    // Get airports by country ordered by city
    @GetMapping("/country/{country}/ordered")
    public ResponseEntity<List<Airport>> getAirportsByCountryOrdered(@PathVariable String country) {
        List<Airport> airports = airportService.getAirportsByCountryOrderByCity(country);
        return ResponseEntity.ok(airports);
    }
}