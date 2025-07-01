package com.rukiyesahin.airlinereservationsystem.repository;

import com.rukiyesahin.airlinereservationsystem.entity.Aircraft;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface AircraftRepository extends JpaRepository<Aircraft, Long> {

    Optional<Aircraft> findByRegistration(String registration);

    List<Aircraft> findByStatus(Aircraft.AircraftStatus status);

    List<Aircraft> findByManufacturer(String manufacturer);

    List<Aircraft> findByModel(String model);

    List<Aircraft> findByManufacturerAndModel(String manufacturer, String model);

    List<Aircraft> findByYearOfManufacture(Integer year);

    List<Aircraft> findByYearOfManufactureBetween(Integer startYear, Integer endYear);

    @Query("SELECT a FROM Aircraft a WHERE a.status = 'ACTIVE'")
    List<Aircraft> findActiveAircraft();

    @Query("SELECT a FROM Aircraft a WHERE a.status = 'MAINTENANCE'")
    List<Aircraft> findAircraftInMaintenance();

    @Query("SELECT a FROM Aircraft a WHERE a.nextMaintenance <= :date")
    List<Aircraft> findAircraftNeedingMaintenance(@Param("date") LocalDateTime date);

    @Query("SELECT a FROM Aircraft a WHERE SIZE(a.flights) > 0")
    List<Aircraft> findAircraftWithFlights();

    @Query("SELECT a FROM Aircraft a WHERE SIZE(a.flights) = 0")
    List<Aircraft> findAircraftWithoutFlights();

    @Query("SELECT a FROM Aircraft a WHERE a.totalSeats >= :minSeats")
    List<Aircraft> findAircraftByMinSeats(@Param("minSeats") Integer minSeats);

    @Query("SELECT a FROM Aircraft a WHERE a.totalSeats BETWEEN :minSeats AND :maxSeats")
    List<Aircraft> findAircraftBySeatRange(@Param("minSeats") Integer minSeats,
                                           @Param("maxSeats") Integer maxSeats);

    @Query("SELECT COUNT(a) FROM Aircraft a WHERE a.status = :status")
    long countAircraftByStatus(@Param("status") Aircraft.AircraftStatus status);

    @Query("SELECT a FROM Aircraft a WHERE a.yearOfManufacture >= :year")
    List<Aircraft> findModernAircraft(@Param("year") Integer year);

    @Query("SELECT a FROM Aircraft a WHERE a.lastMaintenance IS NULL OR a.lastMaintenance < :date")
    List<Aircraft> findAircraftNeedingMaintenanceCheck(@Param("date") LocalDateTime date);

    @Query("SELECT a FROM Aircraft a WHERE a.manufacturer = :manufacturer ORDER BY a.model")
    List<Aircraft> findAircraftByManufacturerOrderByModel(@Param("manufacturer") String manufacturer);

    @Query("SELECT DISTINCT a.manufacturer FROM Aircraft a ORDER BY a.manufacturer")
    List<String> findAllManufacturers();

    @Query("SELECT DISTINCT a.model FROM Aircraft a ORDER BY a.model")
    List<String> findAllModels();
}