package com.motchecker.mot_reminder.model;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.List;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

@Entity
@Table(name = "cars")
@SQLDelete(sql = "UPDATE cars SET active = false WHERE id=?")
@SQLRestriction("active = true")
@SuppressWarnings("unused")
public class Car {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "License plate is required")
    // Regex
    @Pattern(regexp = "^[A-Z0-9][A-Z0-9 \\-]{3,8}[A-Z0-9]$", message = "Invalid format. Use uppercase letters, numbers, spaces, or dashes (e.g., 1A1 1234)")
    @Column(name = "license_plate", nullable = false, unique = true)
    private String licensePlate;

    @NotNull(message = "Expiry date is required")
    @FutureOrPresent(message = "Expiry date must be today or in the future")
    @Column(nullable = false)
    private LocalDate motExpiryDate;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user; // link to the owner of the car

    @Column(nullable = false)
    private boolean active = true;

    @Enumerated(EnumType.STRING)

    // 1:N
    @OneToMany(mappedBy = "car", cascade = CascadeType.ALL, orphanRemoval = true)
    private java.util.List<MotRecord> motHistory = new java.util.ArrayList<>();

    // Constructors
    public Car() {
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLicensePlate() {
        return licensePlate;
    }

    public void setLicensePlate(String licensePlate) {
        this.licensePlate = licensePlate;
    }

    public LocalDate getMotExpiryDate() {
        return motExpiryDate;
    }

    public void setMotExpiryDate(LocalDate motExpiryDate) {
        this.motExpiryDate = motExpiryDate;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public List<MotRecord> getMotHistory() {
        return motHistory;
    }

    public void setMotHistory(List<MotRecord> motHistory) {
        this.motHistory = motHistory;
    }
}