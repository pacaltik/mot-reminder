package com.motchecker.mot_reminder.mapper;

import com.motchecker.mot_reminder.dto.request.CarRequestDTO;
import com.motchecker.mot_reminder.dto.response.CarResponseDTO;
import com.motchecker.mot_reminder.enums.CarStatus;
import com.motchecker.mot_reminder.model.Car;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Component
public class CarMapper {

    // Converts DTO from Controller into Entity for Database
    public Car toEntity(CarRequestDTO dto) {
        Car car = new Car();

        String cleanSpz = dto.getLicensePlate().toUpperCase().replaceAll("[ -]", "");
        car.setLicensePlate(cleanSpz);

        car.setMotExpiryDate(dto.getMotExpiryDate());
        return car;
    }

    // Converts Entity from Database into DTO for View
    public CarResponseDTO toResponseDTO(Car car) {
        CarResponseDTO dto = new CarResponseDTO();
        dto.setId(car.getId());
        dto.setLicensePlate(car.getLicensePlate());
        dto.setMotExpiryDate(car.getMotExpiryDate());

        // Business logic for display status belongs to the mapper or service, not HTML!
        long daysUntilMot = ChronoUnit.DAYS.between(LocalDate.now(), car.getMotExpiryDate());

        if (daysUntilMot < 0) {
            dto.setStatus(CarStatus.EXPIRED);
        } else if (daysUntilMot <= 7) {
            dto.setStatus(CarStatus.EXPIRING_IN_WEEK);
        } else if (daysUntilMot <= 30) {
            dto.setStatus(CarStatus.EXPIRING_IN_MONTH);
        } else {
            dto.setStatus(CarStatus.VALID);
        }
        return dto;
    }
}