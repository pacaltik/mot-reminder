package com.motchecker.mot_reminder.dto;

import com.motchecker.mot_reminder.dto.response.CarResponseDTO;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CarResponseDTOTest {

    @Test
    void shouldCalculateDaysRemainingCorrectly_whenMotIsInFuture() {
        // Arrange
        CarResponseDTO carDTO = new CarResponseDTO();
        carDTO.setMotExpiryDate(LocalDate.now().plusDays(15)); // MOT ends in 15 days

        // Act
        long daysRemaining = carDTO.getDaysRemaining();

        // Assert
        assertEquals(15, daysRemaining, "The calculation of the remaining days must correspond to the future.");
    }

    @Test
    void shouldReturnNegativeDays_whenMotIsExpired() {
        // Arrange
        CarResponseDTO carDTO = new CarResponseDTO();
        carDTO.setMotExpiryDate(LocalDate.now().minusDays(5)); // MOT is late by 5 days

        // Act
        long daysRemaining = carDTO.getDaysRemaining();

        // Assert
        assertTrue(daysRemaining < 0, "If MOT is expired, days are negative.");
        assertEquals(-5, daysRemaining);
    }

    @Test
    void shouldReturnZero_whenMotExpiryDateIsNull() {
        // Arrange
        CarResponseDTO carDTO = new CarResponseDTO();
        carDTO.setMotExpiryDate(null); // no date

        // Act
        long daysRemaining = carDTO.getDaysRemaining();

        // Assert
        assertEquals(0, daysRemaining, "If the date is missing, the method should return 0 to prevent the application from crashing with a NullPointerException.");
    }
}