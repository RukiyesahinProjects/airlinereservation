package com.rukiyesahin.airlinereservationsystem.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "payments")
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String transactionId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "booking_id", nullable = false)
    private Booking booking;

    @NotNull(message = "Amount is required")
    @Positive(message = "Amount must be positive")
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    private PaymentMethod method;

    @Enumerated(EnumType.STRING)
    private PaymentStatus status = PaymentStatus.PENDING;

    private String cardLastFourDigits;
    private String cardType;
    private String currency = "USD";
    private String description;
    private String failureReason;
    private LocalDateTime paymentDate;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        if (transactionId == null) {
            transactionId = generateTransactionId();
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    // Constructors
    public Payment() {}

    public Payment(Booking booking, BigDecimal amount, PaymentMethod method) {
        this.booking = booking;
        this.amount = amount;
        this.method = method;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTransactionId() { return transactionId; }
    public void setTransactionId(String transactionId) { this.transactionId = transactionId; }

    public Booking getBooking() { return booking; }
    public void setBooking(Booking booking) { this.booking = booking; }

    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }

    public PaymentMethod getMethod() { return method; }
    public void setMethod(PaymentMethod method) { this.method = method; }

    public PaymentStatus getStatus() { return status; }
    public void setStatus(PaymentStatus status) { this.status = status; }

    public String getCardLastFourDigits() { return cardLastFourDigits; }
    public void setCardLastFourDigits(String cardLastFourDigits) { this.cardLastFourDigits = cardLastFourDigits; }

    public String getCardType() { return cardType; }
    public void setCardType(String cardType) { this.cardType = cardType; }

    public String getCurrency() { return currency; }
    public void setCurrency(String currency) { this.currency = currency; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getFailureReason() { return failureReason; }
    public void setFailureReason(String failureReason) { this.failureReason = failureReason; }

    public LocalDateTime getPaymentDate() { return paymentDate; }
    public void setPaymentDate(LocalDateTime paymentDate) { this.paymentDate = paymentDate; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    // Business methods
    private String generateTransactionId() {
        return "TXN" + System.currentTimeMillis() + String.format("%04d", (int)(Math.random() * 10000));
    }

    public boolean isCompleted() {
        return PaymentStatus.COMPLETED.equals(status);
    }

    public boolean isFailed() {
        return PaymentStatus.FAILED.equals(status);
    }

    public boolean isPending() {
        return PaymentStatus.PENDING.equals(status);
    }

    public boolean isRefunded() {
        return PaymentStatus.REFUNDED.equals(status);
    }

    public boolean isCancelled() {
        return PaymentStatus.CANCELLED.equals(status);
    }

    public void markAsCompleted() {
        this.status = PaymentStatus.COMPLETED;
        this.paymentDate = LocalDateTime.now();
    }

    public void markAsFailed(String reason) {
        this.status = PaymentStatus.FAILED;
        this.failureReason = reason;
    }

    public void markAsRefunded() {
        this.status = PaymentStatus.REFUNDED;
    }

    public void markAsCancelled() {
        this.status = PaymentStatus.CANCELLED;
    }

    public boolean isCardPayment() {
        return PaymentMethod.CREDIT_CARD.equals(method) || PaymentMethod.DEBIT_CARD.equals(method);
    }

    public boolean isDigitalPayment() {
        return PaymentMethod.PAYPAL.equals(method) || PaymentMethod.APPLE_PAY.equals(method) ||
                PaymentMethod.GOOGLE_PAY.equals(method);
    }

    public String getMaskedCardNumber() {
        if (cardLastFourDigits != null && cardLastFourDigits.length() == 4) {
            return "**** **** **** " + cardLastFourDigits;
        }
        return "**** **** **** ****";
    }

    public String getFormattedAmount() {
        return currency + " " + amount.toString();
    }

    public boolean isLargePayment() {
        return amount.compareTo(BigDecimal.valueOf(1000)) > 0;
    }

    public boolean requiresApproval() {
        return isLargePayment() || isCardPayment();
    }

    public enum PaymentMethod {
        CREDIT_CARD, DEBIT_CARD, PAYPAL, APPLE_PAY, GOOGLE_PAY, BANK_TRANSFER, CASH
    }

    public enum PaymentStatus {
        PENDING, COMPLETED, FAILED, CANCELLED, REFUNDED, DISPUTED
    }
}
