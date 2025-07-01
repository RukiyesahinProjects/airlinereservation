package com.rukiyesahin.airlinereservationsystem.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "bookings")
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String bookingReference;

    // User information (replacing User entity relationship)
    @NotNull(message = "User ID is required")
    private Long userId;

    @NotBlank(message = "User email is required")
    @Email(message = "Invalid email format")
    private String userEmail;

    @NotBlank(message = "User first name is required")
    private String userFirstName;

    @NotBlank(message = "User last name is required")
    private String userLastName;

    private String userPhoneNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "flight_id", nullable = false)
    private Flight flight;

    @Enumerated(EnumType.STRING)
    private Flight.SeatClass seatClass;

    @Positive(message = "Number of passengers must be positive")
    private Integer numberOfPassengers;

    @NotNull(message = "Total price is required")
    @Positive(message = "Total price must be positive")
    private BigDecimal totalPrice;

    @Enumerated(EnumType.STRING)
    private BookingStatus status = BookingStatus.CONFIRMED;

    private String seatNumbers;
    private String specialRequests;
    private LocalDateTime bookingDate;
    private LocalDateTime lastModified;

    @OneToMany(mappedBy = "booking", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Passenger> passengers = new ArrayList<>();

    @OneToMany(mappedBy = "booking", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Payment> payments = new ArrayList<>();

    @PrePersist
    protected void onCreate() {
        bookingDate = LocalDateTime.now();
        lastModified = LocalDateTime.now();
        if (bookingReference == null) {
            bookingReference = generateBookingReference();
        }
    }

    @PreUpdate
    protected void onUpdate() {
        lastModified = LocalDateTime.now();
    }

    // Constructors
    public Booking() {}

    public Booking(Long userId, String userEmail, String userFirstName, String userLastName,
                   Flight flight, Flight.SeatClass seatClass, Integer numberOfPassengers, BigDecimal totalPrice) {
        this.userId = userId;
        this.userEmail = userEmail;
        this.userFirstName = userFirstName;
        this.userLastName = userLastName;
        this.flight = flight;
        this.seatClass = seatClass;
        this.numberOfPassengers = numberOfPassengers;
        this.totalPrice = totalPrice;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getBookingReference() { return bookingReference; }
    public void setBookingReference(String bookingReference) { this.bookingReference = bookingReference; }

    // User-related getters and setters
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public String getUserEmail() { return userEmail; }
    public void setUserEmail(String userEmail) { this.userEmail = userEmail; }

    public String getUserFirstName() { return userFirstName; }
    public void setUserFirstName(String userFirstName) { this.userFirstName = userFirstName; }

    public String getUserLastName() { return userLastName; }
    public void setUserLastName(String userLastName) { this.userLastName = userLastName; }

    public String getUserPhoneNumber() { return userPhoneNumber; }
    public void setUserPhoneNumber(String userPhoneNumber) { this.userPhoneNumber = userPhoneNumber; }

    public String getUserFullName() {
        return userFirstName + " " + userLastName;
    }

    public Flight getFlight() { return flight; }
    public void setFlight(Flight flight) { this.flight = flight; }

    public Flight.SeatClass getSeatClass() { return seatClass; }
    public void setSeatClass(Flight.SeatClass seatClass) { this.seatClass = seatClass; }

    public Integer getNumberOfPassengers() { return numberOfPassengers; }
    public void setNumberOfPassengers(Integer numberOfPassengers) { this.numberOfPassengers = numberOfPassengers; }

    public BigDecimal getTotalPrice() { return totalPrice; }
    public void setTotalPrice(BigDecimal totalPrice) { this.totalPrice = totalPrice; }

    public BookingStatus getStatus() { return status; }
    public void setStatus(BookingStatus status) { this.status = status; }

    public String getSeatNumbers() { return seatNumbers; }
    public void setSeatNumbers(String seatNumbers) { this.seatNumbers = seatNumbers; }

    public String getSpecialRequests() { return specialRequests; }
    public void setSpecialRequests(String specialRequests) { this.specialRequests = specialRequests; }

    public LocalDateTime getBookingDate() { return bookingDate; }
    public void setBookingDate(LocalDateTime bookingDate) { this.bookingDate = bookingDate; }

    public LocalDateTime getLastModified() { return lastModified; }
    public void setLastModified(LocalDateTime lastModified) { this.lastModified = lastModified; }

    public List<Passenger> getPassengers() { return passengers; }
    public void setPassengers(List<Passenger> passengers) { this.passengers = passengers; }

    public List<Payment> getPayments() { return payments; }
    public void setPayments(List<Payment> payments) { this.payments = payments; }

    // Business methods
    private String generateBookingReference() {
        return "BK" + System.currentTimeMillis() + String.format("%03d", (int)(Math.random() * 1000));
    }

    public boolean isConfirmed() {
        return BookingStatus.CONFIRMED.equals(status);
    }

    public boolean isCancelled() {
        return BookingStatus.CANCELLED.equals(status);
    }

    public boolean isCompleted() {
        return BookingStatus.COMPLETED.equals(status);
    }

    public boolean isPending() {
        return BookingStatus.PENDING.equals(status);
    }

    public boolean isNoShow() {
        return BookingStatus.NO_SHOW.equals(status);
    }

    public boolean canBeCancelled() {
        if (isCancelled() || isCompleted()) return false;
        if (flight == null) return false;
        return !flight.hasDeparted();
    }

    public boolean canBeModified() {
        if (isCancelled() || isCompleted()) return false;
        if (flight == null) return false;
        return !flight.isDepartingSoon();
    }

    public BigDecimal getRefundAmount() {
        if (!canBeCancelled()) return BigDecimal.ZERO;

        // Business logic for refund calculation
        if (flight.isDepartingSoon()) {
            // No refund if departing within 2 hours
            return BigDecimal.ZERO;
        } else if (flight.getDepartureTime().minusHours(24).isAfter(LocalDateTime.now())) {
            // Full refund if more than 24 hours before departure
            return totalPrice;
        } else {
            // 50% refund if between 2-24 hours before departure
            return totalPrice.multiply(BigDecimal.valueOf(0.5));
        }
    }

    public void addPassenger(Passenger passenger) {
        passengers.add(passenger);
        passenger.setBooking(this);
    }

    public void addPayment(Payment payment) {
        payments.add(payment);
        payment.setBooking(this);
    }

    public BigDecimal getTotalPaid() {
        return payments.stream()
                .filter(payment -> Payment.PaymentStatus.COMPLETED.equals(payment.getStatus()))
                .map(Payment::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public boolean isFullyPaid() {
        return getTotalPaid().compareTo(totalPrice) >= 0;
    }

    public BigDecimal getRemainingBalance() {
        return totalPrice.subtract(getTotalPaid());
    }

    public boolean hasSpecialRequests() {
        return specialRequests != null && !specialRequests.trim().isEmpty();
    }

    public boolean isGroupBooking() {
        return numberOfPassengers > 1;
    }

    public boolean isPremiumClass() {
        return Flight.SeatClass.BUSINESS.equals(seatClass) || Flight.SeatClass.FIRST.equals(seatClass);
    }

    public String getRoute() {
        if (flight != null) {
            return flight.getRoute();
        }
        return "Unknown Route";
    }

    public LocalDateTime getDepartureTime() {
        return flight != null ? flight.getDepartureTime() : null;
    }

    public LocalDateTime getArrivalTime() {
        return flight != null ? flight.getArrivalTime() : null;
    }

    public boolean isLastMinuteBooking() {
        if (flight != null && bookingDate != null) {
            long hoursUntilFlight = java.time.Duration.between(bookingDate, flight.getDepartureTime()).toHours();
            return hoursUntilFlight < 24;
        }
        return false;
    }

    public enum BookingStatus {
        PENDING, CONFIRMED, CANCELLED, COMPLETED, NO_SHOW
    }
}