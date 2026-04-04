package com.motchecker.mot_reminder.dto.response;

import com.motchecker.mot_reminder.enums.CarStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CarResponseDTO {
    private Long id;
    private String licensePlate;
    private LocalDate motExpiryDate;
    private CarStatus status;

    public long getDaysRemaining() {
        if (motExpiryDate == null) return 0;
        return java.time.temporal.ChronoUnit.DAYS.between(java.time.LocalDate.now(), motExpiryDate);
    }
}