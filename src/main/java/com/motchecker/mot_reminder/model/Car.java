package com.motchecker.mot_reminder.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "cars")
public class Car {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String licensePlate; // license plate

    @Column(nullable = false)
    private LocalDate motExpiryDate; // MOT date of expiration

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