package com.motchecker.mot_reminder.dto.response;

import java.time.LocalDate;
@SuppressWarnings("unused")
public class CarResponseDTO {
    private Long id;
    private String licensePlate;
    private LocalDate motExpiryDate;
    private String status; // e.g., "VALID" or "EXPIRED" - calculated in Service!

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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}