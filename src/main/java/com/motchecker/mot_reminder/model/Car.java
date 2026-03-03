package com.motchecker.mot_reminder.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

@Entity
@Table(name = "cars")
@SuppressWarnings("unused")
public class Car {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "License plate is required")
    // Regex: Začíná písmenem/číslem, obsahuje písmena/čísla/mezery/pomlčky a má délku 5-10 znaků
    @Pattern(regexp = "^[A-Z0-9][A-Z0-9 \\-]{3,8}[A-Z0-9]$", message = "Invalid format. Use uppercase letters, numbers, spaces, or dashes (e.g., 1A1 1234)")
    @Column(nullable = false)
    private String licensePlate;

    @NotNull(message = "Expiry date is required")
    @FutureOrPresent(message = "Expiry date must be today or in the future")
    @Column(nullable = false)
    private LocalDate motExpiryDate;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user; // link to the owner of the car

    // Constructors
    public Car() {}

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getLicensePlate() { return licensePlate; }
    public void setLicensePlate(String licensePlate) { this.licensePlate = licensePlate; }

    public LocalDate getMotExpiryDate() { return motExpiryDate; }
    public void setMotExpiryDate(LocalDate motExpiryDate) { this.motExpiryDate = motExpiryDate; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
}