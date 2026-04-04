package com.motchecker.mot_reminder.dto.request;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CarRequestDTO {

    @NotBlank(message = "License plate is required")
    @Pattern(regexp = "^(([ABCDEFHIJKLMNPRSTUVXYZ]|[0-9])-?){5,8}$", message = "Invalid format. Check license plate.")
    private String licensePlate;

    @NotNull(message = "Expiry date is required")
    @FutureOrPresent(message = "Expiry date must be today or in the future")
    private LocalDate motExpiryDate;
}