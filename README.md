# Airline Reservation System Backend

A real-world, business-logic-rich Spring Boot backend for an Airline Reservation System. This project demonstrates a layered architecture with entities, repositories, services, and RESTful APIs for managing flights, bookings, payments, airports, aircraft, and passengers.

## Features
- Flight search, booking, and management
- Airport and aircraft management
- Booking and payment processing
- Business rules (seat availability, refund policy, pricing, etc.)
- Exception handling and validation
- H2 in-memory database for development

## Architecture
- **Spring Boot** (REST API)
- **Spring Data JPA** (Persistence)
- **H2 Database** (Development)

### Main Layers
- **Entity Layer:** JPA entities for Flight, Booking, Airport, Aircraft, Passenger, Payment
- **Repository Layer:** JPA repositories with custom queries
- **Service Layer:** Business logic, validation, and orchestration
- **Controller Layer:** REST endpoints for all operations

## UML Diagram

```mermaid
classDiagram
    class Airport {
        Long id
        String code
        String name
        String city
        String country
        String timezone
        Double latitude
        Double longitude
    }
    class Aircraft {
        Long id
        String registration
        String type
        String manufacturer
        String model
        Integer totalSeats
        Integer economySeats
        Integer businessSeats
        Integer firstClassSeats
        Integer yearOfManufacture
        LocalDateTime lastMaintenance
        LocalDateTime nextMaintenance
        AircraftStatus status
    }
    class Flight {
        Long id
        String flightNumber
        Airport departureAirport
        Airport arrivalAirport
        LocalDateTime departureTime
        LocalDateTime arrivalTime
        Aircraft aircraft
        FlightStatus status
        Integer totalSeats
        Integer availableSeats
        BigDecimal basePrice
        BigDecimal businessClassPrice
        BigDecimal firstClassPrice
        String gate
        String terminal
    }
    class Booking {
        Long id
        String bookingReference
        Flight flight
        SeatClass seatClass
        Integer numberOfPassengers
        BigDecimal totalPrice
        BookingStatus status
        String seatNumbers
        String specialRequests
        LocalDateTime bookingDate
        LocalDateTime lastModified
        String userFirstName
        String userLastName
        String userEmail
        String userPhoneNumber
    }
    class Passenger {
        Long id
        String firstName
        String lastName
        String passportNumber
        LocalDate dateOfBirth
        Gender gender
        PassengerType type
        String email
        String phoneNumber
        String nationality
        String seatNumber
        String mealPreference
        String specialAssistance
        Booking booking
    }
    class Payment {
        Long id
        String transactionId
        Booking booking
        BigDecimal amount
        PaymentMethod method
        PaymentStatus status
        String cardLastFourDigits
        String cardType
        String currency
    }
```

## Getting Started

### Prerequisites
- Java 17+
- Maven 3.8+

### Setup
1. **Clone the repository:**
   ```bash
   git clone <your-repo-url>
   cd <project-directory>
   ```
2. **Build the project:**
   ```bash
   mvn clean install
   ```
3. **Run the application:**
   ```bash
   mvn spring-boot:run
   ```
4. **Access H2 Console:**
   - URL: `http://localhost:8080/h2-console`
   - JDBC URL: `jdbc:h2:mem:airlinedb`
   - User: `sa`, Password: (leave blank or use your config)

## API Endpoints
- REST endpoints for flight, booking, payment, airport, and aircraft management are available under `/api/*`.

## Configuration
- All configuration is in `src/main/resources/application.properties`.

## Testing
- Run tests with:
  ```bash
  mvn test
  ```

## License
MIT

---

**UML Diagram**

```mermaid
classDiagram
    class User {
        Long id
        String username
        String email
        String password
        String firstName
        String lastName
        Role role
        String phoneNumber
        String address
        LocalDateTime createdAt
        LocalDateTime updatedAt
        +getFullName()
        +isAdmin()
    }
    class Airport {
        Long id
        String code
        String name
        String city
        String country
        String timezone
        Double latitude
        Double longitude
        LocalDateTime createdAt
        LocalDateTime updatedAt
        +getFullLocation()
    }
    class Aircraft {
        Long id
        String registration
        String type
        String manufacturer
        String model
        Integer totalSeats
        Integer economySeats
        Integer businessSeats
        Integer firstClassSeats
        Integer yearOfManufacture
        LocalDateTime lastMaintenance
        LocalDateTime nextMaintenance
        AircraftStatus status
        LocalDateTime createdAt
        LocalDateTime updatedAt
        +getFullDescription()
    }
    class Flight {
        Long id
        String flightNumber
        Airport departureAirport
        Airport arrivalAirport
        LocalDateTime departureTime
        LocalDateTime arrivalTime
        Aircraft aircraft
        FlightStatus status
        Integer totalSeats
        Integer availableSeats
        BigDecimal basePrice
        BigDecimal businessClassPrice
        BigDecimal firstClassPrice
        String gate
        String terminal
        LocalDateTime createdAt
        LocalDateTime updatedAt
        +getFlightDuration()
        +isAvailable()
        +bookSeat()
        +cancelSeat()
    }
    class Booking {
        Long id
        String bookingReference
        User user
        Flight flight
        SeatClass seatClass
        Integer numberOfPassengers
        BigDecimal totalPrice
        BookingStatus status
        String seatNumbers
        String specialRequests
        LocalDateTime bookingDate
        LocalDateTime lastModified
        +getRefundAmount()
        +isFullyPaid()
    }
    class Passenger {
        Long id
        String firstName
        String lastName
        String passportNumber
        LocalDate dateOfBirth
        Gender gender
        PassengerType type
        String email
        String phoneNumber
        String nationality
        String seatNumber
        String mealPreference
        String specialAssistance
        LocalDateTime createdAt
        LocalDateTime updatedAt
        Booking booking
        +getFullName()
    }
    class Payment {
        Long id
        String transactionId
        Booking booking
        BigDecimal amount
        PaymentMethod method
        PaymentStatus status
        String cardLastFourDigits
        String cardType
        String currency
        String description
        String failureReason
        LocalDateTime paymentDate
        LocalDateTime createdAt
        LocalDateTime updatedAt
        +isCompleted()
    }
    User "1" -- "*" Booking : makes
    Booking "*" -- "1" Flight : for
    Booking "*" -- "1" User : by
    Booking "1" -- "*" Passenger : has
    Booking "1" -- "*" Payment : paidBy
    Flight "*" -- "1" Airport : departsFrom
    Flight "*" -- "1" Airport : arrivesAt
    Flight "*" -- "1" Aircraft : uses
``` 
