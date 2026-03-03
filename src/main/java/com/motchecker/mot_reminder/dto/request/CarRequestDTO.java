package com.motchecker.mot_reminder.dto.request;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import java.time.LocalDate;
@SuppressWarnings("unused")
public class CarRequestDTO {

    @NotBlank(message = "License plate is required")
    @Pattern(regexp = "^([a-zA-Z]{3}|[0-9][a-zA-Z][0-9])[ ]?[0-9]{2}[- ]?[0-9]{2}$",
            message = "Invalid format. Use formats like PUN1234, 1A2 3456, pun 12-34")
    private String licensePlate;

    @NotNull(message = "Expiry date is required")
    @FutureOrPresent(message = "Expiry date must be today or in the future")
    private LocalDate motExpiryDate;

    // Getters and Setters
    public String getLicensePlate() { return licensePlate; }
    public void setLicensePlate(String licensePlate) { this.licensePlate = licensePlate; }
    public LocalDate getMotExpiryDate() { return motExpiryDate; }
    public void setMotExpiryDate(LocalDate motExpiryDate) { this.motExpiryDate = motExpiryDate; }
}