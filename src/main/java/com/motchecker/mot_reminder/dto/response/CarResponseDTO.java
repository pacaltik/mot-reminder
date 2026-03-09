package com.motchecker.mot_reminder.dto.response;

import com.motchecker.mot_reminder.enums.CarStatus;

import java.time.LocalDate;
@SuppressWarnings("unused")
public class CarResponseDTO {
    private Long id;
    private String licensePlate;
    private LocalDate motExpiryDate;
    private CarStatus status; // e.g., "VALID" or "EXPIRED" - calculated in Service!

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

    public CarStatus getStatus() {
        return status;
    }

    public void setStatus(CarStatus status) {
        this.status = status;
    }

    public long getDaysRemaining() {
        if (motExpiryDate == null) return 0;
        return java.time.temporal.ChronoUnit.DAYS.between(java.time.LocalDate.now(), motExpiryDate);
    }
}