package com.motchecker.mot_reminder.service;

import com.motchecker.mot_reminder.dto.request.CarRequestDTO;
import com.motchecker.mot_reminder.dto.response.CarResponseDTO;
import java.time.LocalDate;
import java.util.List;

public interface CarService {
    void addCar(CarRequestDTO carRequestDTO, Long userId);
    void deleteCar(Long carId, Long userId);
    void addMotRecord(Long carId, LocalDate date, boolean passed, String notes);
    List<CarResponseDTO> getUserCars(Long userId);
}