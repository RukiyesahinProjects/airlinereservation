package com.rukiyesahin.airlinereservationsystem.controller;

import com.rukiyesahin.airlinereservationsystem.entity.Aircraft;
import com.rukiyesahin.airlinereservationsystem.service.AircraftService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/aircraft")
@CrossOrigin(origins = "*")
public class AircraftController {

    @Autowired
    private AircraftService aircraftService;

    // Create new aircraft
    @PostMapping
    public ResponseEntity<Aircraft> createAircraft(@RequestBody Aircraft aircraft) {
        return ResponseEntity.ok(aircraftService.createAircraft(aircraft));
    }

    // Get aircraft by ID
    @GetMapping("/{id}")
    public ResponseEntity<Aircraft> getAircraftById(@PathVariable Long id) {
        return ResponseEntity.ok(aircraftService.getAircraftById(id));
    }

    // Get aircraft by registration
    @GetMapping("/registration/{registration}")
    public ResponseEntity<Aircraft> getAircraftByRegistration(@PathVariable String registration) {
        return ResponseEntity.ok(aircraftService.getAircraftByRegistration(registration));
    }

    // Get all aircraft
    @GetMapping
    public ResponseEntity<List<Aircraft>> getAllAircraft() {
        return ResponseEntity.ok(aircraftService.getAllAircraft());
    }

    // Get aircraft by status
    @GetMapping("/status/{status}")
    public ResponseEntity<List<Aircraft>> getAircraftByStatus(@PathVariable Aircraft.AircraftStatus status) {
        return ResponseEntity.ok(aircraftService.getAircraftByStatus(status));
    }

    // Get aircraft by manufacturer
    @GetMapping("/manufacturer/{manufacturer}")
    public ResponseEntity<List<Aircraft>> getAircraftByManufacturer(@PathVariable String manufacturer) {
        return ResponseEntity.ok(aircraftService.getAircraftByManufacturer(manufacturer));
    }

    // Get aircraft by model
    @GetMapping("/model/{model}")
    public ResponseEntity<List<Aircraft>> getAircraftByModel(@PathVariable String model) {
        return ResponseEntity.ok(aircraftService.getAircraftByModel(model));
    }

    // Get aircraft by manufacturer and model
    @GetMapping("/manufacturer/{manufacturer}/model/{model}")
    public ResponseEntity<List<Aircraft>> getAircraftByManufacturerAndModel(
            @PathVariable String manufacturer, @PathVariable String model) {
        return ResponseEntity.ok(aircraftService.getAircraftByManufacturerAndModel(manufacturer, model));
    }

    // Get aircraft by year of manufacture
    @GetMapping("/year/{year}")
    public ResponseEntity<List<Aircraft>> getAircraftByYear(@PathVariable Integer year) {
        return ResponseEntity.ok(aircraftService.getAircraftByYear(year));
    }

    // Get aircraft by year range
    @GetMapping("/year-range")
    public ResponseEntity<List<Aircraft>> getAircraftByYearRange(
            @RequestParam Integer startYear, @RequestParam Integer endYear) {
        return ResponseEntity.ok(aircraftService.getAircraftByYearRange(startYear, endYear));
    }

    // Get active aircraft
    @GetMapping("/active")
    public ResponseEntity<List<Aircraft>> getActiveAircraft() {
        return ResponseEntity.ok(aircraftService.getActiveAircraft());
    }

    // Get aircraft in maintenance
    @GetMapping("/maintenance")
    public ResponseEntity<List<Aircraft>> getAircraftInMaintenance() {
        return ResponseEntity.ok(aircraftService.getAircraftInMaintenance());
    }

    // Get aircraft needing maintenance
    @GetMapping("/needing-maintenance")
    public ResponseEntity<List<Aircraft>> getAircraftNeedingMaintenance(@RequestParam String date) {
        return ResponseEntity.ok(aircraftService.getAircraftNeedingMaintenance(LocalDateTime.parse(date)));
    }

    // Get aircraft with flights
    @GetMapping("/with-flights")
    public ResponseEntity<List<Aircraft>> getAircraftWithFlights() {
        return ResponseEntity.ok(aircraftService.getAircraftWithFlights());
    }

    // Get aircraft without flights
    @GetMapping("/without-flights")
    public ResponseEntity<List<Aircraft>> getAircraftWithoutFlights() {
        return ResponseEntity.ok(aircraftService.getAircraftWithoutFlights());
    }

    // Get aircraft by minimum seats
    @GetMapping("/min-seats/{minSeats}")
    public ResponseEntity<List<Aircraft>> getAircraftByMinSeats(@PathVariable Integer minSeats) {
        return ResponseEntity.ok(aircraftService.getAircraftByMinSeats(minSeats));
    }

    // Get aircraft by seat range
    @GetMapping("/seat-range")
    public ResponseEntity<List<Aircraft>> getAircraftBySeatRange(
            @RequestParam Integer minSeats, @RequestParam Integer maxSeats) {
        return ResponseEntity.ok(aircraftService.getAircraftBySeatRange(minSeats, maxSeats));
    }

    // Get modern aircraft
    @GetMapping("/modern/{year}")
    public ResponseEntity<List<Aircraft>> getModernAircraft(@PathVariable Integer year) {
        return ResponseEntity.ok(aircraftService.getModernAircraft(year));
    }

    // Get aircraft needing maintenance check
    @GetMapping("/needing-maintenance-check")
    public ResponseEntity<List<Aircraft>> getAircraftNeedingMaintenanceCheck(@RequestParam String date) {
        return ResponseEntity.ok(aircraftService.getAircraftNeedingMaintenanceCheck(LocalDateTime.parse(date)));
    }

    // Get aircraft by manufacturer ordered by model
    @GetMapping("/manufacturer/{manufacturer}/ordered-by-model")
    public ResponseEntity<List<Aircraft>> getAircraftByManufacturerOrderByModel(@PathVariable String manufacturer) {
        return ResponseEntity.ok(aircraftService.getAircraftByManufacturerOrderByModel(manufacturer));
    }

    // Get all manufacturers
    @GetMapping("/manufacturers")
    public ResponseEntity<List<String>> getAllManufacturers() {
        return ResponseEntity.ok(aircraftService.getAllManufacturers());
    }

    // Get all models
    @GetMapping("/models")
    public ResponseEntity<List<String>> getAllModels() {
        return ResponseEntity.ok(aircraftService.getAllModels());
    }

    // Update aircraft
    @PutMapping("/{id}")
    public ResponseEntity<Aircraft> updateAircraft(@PathVariable Long id, @RequestBody Aircraft aircraftDetails) {
        return ResponseEntity.ok(aircraftService.updateAircraft(id, aircraftDetails));
    }

    // Delete aircraft
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAircraft(@PathVariable Long id) {
        aircraftService.deleteAircraft(id);
        return ResponseEntity.noContent().build();
    }

    // Check if aircraft exists by registration
    @GetMapping("/exists/{registration}")
    public ResponseEntity<Boolean> aircraftExists(@PathVariable String registration) {
        return ResponseEntity.ok(aircraftService.aircraftExists(registration));
    }

    // Get aircraft count by status
    @GetMapping("/count/status/{status}")
    public ResponseEntity<Long> getAircraftCountByStatus(@PathVariable Aircraft.AircraftStatus status) {
        return ResponseEntity.ok(aircraftService.getAircraftCountByStatus(status));
    }

    // Update aircraft status
    @PutMapping("/{id}/status")
    public ResponseEntity<Aircraft> updateAircraftStatus(
            @PathVariable Long id, @RequestParam Aircraft.AircraftStatus status) {
        return ResponseEntity.ok(aircraftService.updateAircraftStatus(id, status));
    }

    // Schedule maintenance
    @PutMapping("/{id}/schedule-maintenance")
    public ResponseEntity<Aircraft> scheduleMaintenance(
            @PathVariable Long id, @RequestParam String maintenanceDate) {
        return ResponseEntity.ok(aircraftService.scheduleMaintenance(id, LocalDateTime.parse(maintenanceDate)));
    }

    // Complete maintenance
    @PutMapping("/{id}/complete-maintenance")
    public ResponseEntity<Aircraft> completeMaintenance(@PathVariable Long id) {
        return ResponseEntity.ok(aircraftService.completeMaintenance(id));
    }


}