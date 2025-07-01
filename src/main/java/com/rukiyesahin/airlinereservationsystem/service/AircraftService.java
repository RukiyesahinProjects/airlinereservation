package com.rukiyesahin.airlinereservationsystem.service;

import com.rukiyesahin.airlinereservationsystem.entity.Aircraft;
import com.rukiyesahin.airlinereservationsystem.repository.AircraftRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class AircraftService {

    @Autowired
    private AircraftRepository aircraftRepository;

    // Create new aircraft
    public Aircraft createAircraft(Aircraft aircraft) {
        return aircraftRepository.save(aircraft);
    }

    // Get aircraft by ID
    public Aircraft getAircraftById(Long id) {
        return aircraftRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Aircraft not found with ID: " + id));
    }

    // Get aircraft by registration
    public Aircraft getAircraftByRegistration(String registration) {
        return aircraftRepository.findByRegistration(registration)
                .orElseThrow(() -> new RuntimeException("Aircraft not found with registration: " + registration));
    }

    // Get all aircraft
    public List<Aircraft> getAllAircraft() {
        return aircraftRepository.findAll();
    }

    // Get aircraft by status
    public List<Aircraft> getAircraftByStatus(Aircraft.AircraftStatus status) {
        return aircraftRepository.findByStatus(status);
    }

    // Get aircraft by manufacturer
    public List<Aircraft> getAircraftByManufacturer(String manufacturer) {
        return aircraftRepository.findByManufacturer(manufacturer);
    }

    // Get aircraft by model
    public List<Aircraft> getAircraftByModel(String model) {
        return aircraftRepository.findByModel(model);
    }

    // Get aircraft by manufacturer and model
    public List<Aircraft> getAircraftByManufacturerAndModel(String manufacturer, String model) {
        return aircraftRepository.findByManufacturerAndModel(manufacturer, model);
    }

    // Get aircraft by year of manufacture
    public List<Aircraft> getAircraftByYear(Integer year) {
        return aircraftRepository.findByYearOfManufacture(year);
    }

    // Get aircraft by year range
    public List<Aircraft> getAircraftByYearRange(Integer startYear, Integer endYear) {
        return aircraftRepository.findByYearOfManufactureBetween(startYear, endYear);
    }

    // Get active aircraft
    public List<Aircraft> getActiveAircraft() {
        return aircraftRepository.findActiveAircraft();
    }

    // Get aircraft in maintenance
    public List<Aircraft> getAircraftInMaintenance() {
        return aircraftRepository.findAircraftInMaintenance();
    }

    // Get aircraft needing maintenance
    public List<Aircraft> getAircraftNeedingMaintenance(LocalDateTime date) {
        return aircraftRepository.findAircraftNeedingMaintenance(date);
    }

    // Get aircraft with flights
    public List<Aircraft> getAircraftWithFlights() {
        return aircraftRepository.findAircraftWithFlights();
    }

    // Get aircraft without flights
    public List<Aircraft> getAircraftWithoutFlights() {
        return aircraftRepository.findAircraftWithoutFlights();
    }

    // Get aircraft by minimum seats
    public List<Aircraft> getAircraftByMinSeats(Integer minSeats) {
        return aircraftRepository.findAircraftByMinSeats(minSeats);
    }

    // Get aircraft by seat range
    public List<Aircraft> getAircraftBySeatRange(Integer minSeats, Integer maxSeats) {
        return aircraftRepository.findAircraftBySeatRange(minSeats, maxSeats);
    }

    // Get modern aircraft
    public List<Aircraft> getModernAircraft(Integer year) {
        return aircraftRepository.findModernAircraft(year);
    }

    // Get aircraft needing maintenance check
    public List<Aircraft> getAircraftNeedingMaintenanceCheck(LocalDateTime date) {
        return aircraftRepository.findAircraftNeedingMaintenanceCheck(date);
    }

    // Get aircraft by manufacturer ordered by model
    public List<Aircraft> getAircraftByManufacturerOrderByModel(String manufacturer) {
        return aircraftRepository.findAircraftByManufacturerOrderByModel(manufacturer);
    }

    // Get all manufacturers
    public List<String> getAllManufacturers() {
        return aircraftRepository.findAllManufacturers();
    }

    // Get all models
    public List<String> getAllModels() {
        return aircraftRepository.findAllModels();
    }

    // Update aircraft
    public Aircraft updateAircraft(Long id, Aircraft aircraftDetails) {
        Aircraft aircraft = getAircraftById(id);

        if (aircraftDetails.getRegistration() != null) {
            aircraft.setRegistration(aircraftDetails.getRegistration());
        }
        if (aircraftDetails.getType() != null) {
            aircraft.setType(aircraftDetails.getType());
        }
        if (aircraftDetails.getManufacturer() != null) {
            aircraft.setManufacturer(aircraftDetails.getManufacturer());
        }
        if (aircraftDetails.getModel() != null) {
            aircraft.setModel(aircraftDetails.getModel());
        }
        if (aircraftDetails.getYearOfManufacture() != null) {
            aircraft.setYearOfManufacture(aircraftDetails.getYearOfManufacture());
        }
        if (aircraftDetails.getTotalSeats() != null) {
            aircraft.setTotalSeats(aircraftDetails.getTotalSeats());
        }
        if (aircraftDetails.getStatus() != null) {
            aircraft.setStatus(aircraftDetails.getStatus());
        }
        if (aircraftDetails.getLastMaintenance() != null) {
            aircraft.setLastMaintenance(aircraftDetails.getLastMaintenance());
        }
        if (aircraftDetails.getNextMaintenance() != null) {
            aircraft.setNextMaintenance(aircraftDetails.getNextMaintenance());
        }

        return aircraftRepository.save(aircraft);
    }

    // Delete aircraft
    public void deleteAircraft(Long id) {
        Aircraft aircraft = getAircraftById(id);
        aircraftRepository.delete(aircraft);
    }

    // Check if aircraft exists
    public boolean aircraftExists(String registration) {
        return aircraftRepository.findByRegistration(registration).isPresent();
    }

    // Get aircraft count by status
    public long getAircraftCountByStatus(Aircraft.AircraftStatus status) {
        return aircraftRepository.countAircraftByStatus(status);
    }

    // Update aircraft status
    public Aircraft updateAircraftStatus(Long id, Aircraft.AircraftStatus status) {
        Aircraft aircraft = getAircraftById(id);
        aircraft.setStatus(status);
        return aircraftRepository.save(aircraft);
    }

    // Schedule maintenance
    public Aircraft scheduleMaintenance(Long id, LocalDateTime maintenanceDate) {
        Aircraft aircraft = getAircraftById(id);
        aircraft.setNextMaintenance(maintenanceDate);
        aircraft.setStatus(Aircraft.AircraftStatus.MAINTENANCE);
        return aircraftRepository.save(aircraft);
    }

    // Complete maintenance
    public Aircraft completeMaintenance(Long id) {
        Aircraft aircraft = getAircraftById(id);
        aircraft.setLastMaintenance(LocalDateTime.now());
        aircraft.setStatus(Aircraft.AircraftStatus.ACTIVE);
        return aircraftRepository.save(aircraft);
    }


}