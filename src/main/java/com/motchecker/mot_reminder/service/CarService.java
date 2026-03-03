package com.motchecker.mot_reminder.service;

import com.motchecker.mot_reminder.dto.request.CarRequestDTO;
import com.motchecker.mot_reminder.dto.response.CarResponseDTO;
import java.util.List;

public interface CarService {
    void addCar(CarRequestDTO carRequestDTO, Long userId);
    void deleteCar(Long carId, Long userId);
    List<CarResponseDTO> getUserCars(Long userId);
}