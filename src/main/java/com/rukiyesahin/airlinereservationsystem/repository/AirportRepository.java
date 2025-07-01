package com.rukiyesahin.airlinereservationsystem.repository;

import com.rukiyesahin.airlinereservationsystem.entity.Airport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AirportRepository extends JpaRepository<Airport, Long> {

    // Find airport by code
    Optional<Airport> findByCode(String code);

    // Find airports by city
    List<Airport> findByCity(String city);

    // Find airports by country
    List<Airport> findByCountry(String country);

    // Find airports by city and country
    List<Airport> findByCityAndCountry(String city, String country);

    // Search airports by name or city containing search term
    @Query("SELECT a FROM Airport a WHERE a.name LIKE %:searchTerm% OR a.city LIKE %:searchTerm%")
    List<Airport> findByNameOrCityContaining(@Param("searchTerm") String searchTerm);

    // Find all countries
    @Query("SELECT DISTINCT a.country FROM Airport a ORDER BY a.country")
    List<String> findAllCountries();

    // Find airports with coordinates
    @Query("SELECT a FROM Airport a WHERE a.latitude IS NOT NULL AND a.longitude IS NOT NULL")
    List<Airport> findAirportsWithCoordinates();

    // Find active airports (placeholder - would need status field)
    @Query("SELECT a FROM Airport a")
    List<Airport> findActiveAirports();

    // Find inactive airports (placeholder - would need status field)
    @Query("SELECT a FROM Airport a WHERE 1=0")
    List<Airport> findInactiveAirports();

    // Find major hubs (placeholder - would need business logic)
    @Query("SELECT a FROM Airport a")
    List<Airport> findMajorHubs();

    // Count airports by country
    @Query("SELECT COUNT(a) FROM Airport a WHERE a.country = :country")
    long countAirportsByCountry(@Param("country") String country);

    // Count airports by type (placeholder - would need type field)
    @Query("SELECT COUNT(a) FROM Airport a")
    long countAirportsByType(@Param("type") String type);

    // Find airports by country ordered by city
    @Query("SELECT a FROM Airport a WHERE a.country = :country ORDER BY a.city")
    List<Airport> findByCountryOrderByCity(@Param("country") String country);

    // Find airports by name containing
    @Query("SELECT a FROM Airport a WHERE a.name LIKE %:name%")
    List<Airport> findByNameContaining(@Param("name") String name);

    // Find airports by code containing
    @Query("SELECT a FROM Airport a WHERE a.code LIKE %:code%")
    List<Airport> findByCodeContaining(@Param("code") String code);

    // Find airports by timezone
    List<Airport> findByTimezone(String timezone);

    // Find airports with latitude and longitude in range
    @Query("SELECT a FROM Airport a WHERE a.latitude BETWEEN :minLat AND :maxLat AND a.longitude BETWEEN :minLng AND :maxLng")
    List<Airport> findByLatitudeBetweenAndLongitudeBetween(
            @Param("minLat") Double minLat,
            @Param("maxLat") Double maxLat,
            @Param("minLng") Double minLng,
            @Param("maxLng") Double maxLng
    );

    // Find airports created after date
    @Query("SELECT a FROM Airport a WHERE a.createdAt >= :date")
    List<Airport> findByCreatedAtAfter(@Param("date") java.time.LocalDateTime date);

    // Find airports updated after date
    @Query("SELECT a FROM Airport a WHERE a.updatedAt >= :date")
    List<Airport> findByUpdatedAtAfter(@Param("date") java.time.LocalDateTime date);

    // Check if airport exists by code
    boolean existsByCode(String code);

    // Check if airport exists by name
    boolean existsByName(String name);

    // Find airports by multiple codes
    @Query("SELECT a FROM Airport a WHERE a.code IN :codes")
    List<Airport> findByCodeIn(@Param("codes") List<String> codes);

    // Find airports by multiple cities
    @Query("SELECT a FROM Airport a WHERE a.city IN :cities")
    List<Airport> findByCityIn(@Param("cities") List<String> cities);

    // Find airports by multiple countries
    @Query("SELECT a FROM Airport a WHERE a.country IN :countries")
    List<Airport> findByCountryIn(@Param("countries") List<String> countries);
}