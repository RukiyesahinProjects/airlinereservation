package com.rukiyesahin.airlinereservationsystem.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "passengers")
public class Passenger {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "First name is required")
    private String firstName;

    @NotBlank(message = "Last name is required")
    private String lastName;

    @NotBlank(message = "Passport number is required")
    @Column(unique = true)
    private String passportNumber;

    @NotNull(message = "Date of birth is required")
    @Past(message = "Date of birth must be in the past")
    private LocalDate dateOfBirth;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Enumerated(EnumType.STRING)
    private PassengerType type = PassengerType.ADULT;

    @Email(message = "Email should be valid")
    private String email;

    private String phoneNumber;
    private String nationality;
    private String seatNumber;
    private String mealPreference;
    private String specialAssistance;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "booking_id")
    private Booking booking;

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
    public Passenger() {}

    public Passenger(String firstName, String lastName, String passportNumber,
                     LocalDate dateOfBirth, Gender gender) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.passportNumber = passportNumber;
        this.dateOfBirth = dateOfBirth;
        this.gender = gender;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getPassportNumber() { return passportNumber; }
    public void setPassportNumber(String passportNumber) { this.passportNumber = passportNumber; }

    public LocalDate getDateOfBirth() { return dateOfBirth; }
    public void setDateOfBirth(LocalDate dateOfBirth) { this.dateOfBirth = dateOfBirth; }

    public Gender getGender() { return gender; }
    public void setGender(Gender gender) { this.gender = gender; }

    public PassengerType getType() { return type; }
    public void setType(PassengerType type) { this.type = type; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }

    public String getNationality() { return nationality; }
    public void setNationality(String nationality) { this.nationality = nationality; }

    public String getSeatNumber() { return seatNumber; }
    public void setSeatNumber(String seatNumber) { this.seatNumber = seatNumber; }

    public String getMealPreference() { return mealPreference; }
    public void setMealPreference(String mealPreference) { this.mealPreference = mealPreference; }

    public String getSpecialAssistance() { return specialAssistance; }
    public void setSpecialAssistance(String specialAssistance) { this.specialAssistance = specialAssistance; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    public Booking getBooking() { return booking; }
    public void setBooking(Booking booking) { this.booking = booking; }

    // Business methods
    public String getFullName() {
        return firstName + " " + lastName;
    }

    public int getAge() {
        if (dateOfBirth == null) return 0;
        return LocalDate.now().getYear() - dateOfBirth.getYear();
    }

    public boolean isAdult() {
        return getAge() >= 18;
    }

    public boolean isChild() {
        return getAge() >= 2 && getAge() < 12;
    }

    public boolean isInfant() {
        return getAge() < 2;
    }

    public boolean isSenior() {
        return getAge() >= 65;
    }

    public boolean hasSpecialAssistance() {
        return specialAssistance != null && !specialAssistance.trim().isEmpty();
    }

    public boolean hasMealPreference() {
        return mealPreference != null && !mealPreference.trim().isEmpty();
    }

    public boolean isAssignedSeat() {
        return seatNumber != null && !seatNumber.trim().isEmpty();
    }

    public boolean isEligibleForSeniorDiscount() {
        return isSenior() && PassengerType.ADULT.equals(type);
    }

    public boolean isEligibleForChildDiscount() {
        return isChild() && PassengerType.CHILD.equals(type);
    }

    public boolean isEligibleForInfantDiscount() {
        return isInfant() && PassengerType.INFANT.equals(type);
    }

    public enum Gender {
        MALE, FEMALE, OTHER
    }

    public enum PassengerType {
        ADULT, CHILD, INFANT, SENIOR
    }
}
