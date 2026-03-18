package com.motchecker.mot_reminder.service.impl;

import com.motchecker.mot_reminder.dto.request.CarRequestDTO;
import com.motchecker.mot_reminder.dto.response.CarResponseDTO;
import com.motchecker.mot_reminder.mapper.CarMapper;
import com.motchecker.mot_reminder.model.Car;
import com.motchecker.mot_reminder.model.MotRecord;
import com.motchecker.mot_reminder.model.User;
import com.motchecker.mot_reminder.repository.CarRepository;
import com.motchecker.mot_reminder.repository.MotRecordRepository;
import com.motchecker.mot_reminder.repository.UserRepository;
import com.motchecker.mot_reminder.service.CarService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class CarServiceImpl implements CarService {

    private static final Logger logger = LoggerFactory.getLogger(CarServiceImpl.class);

    private final MotRecordRepository motRecordRepository;
    private final CarRepository carRepository;
    private final UserRepository userRepository;
    private final CarMapper carMapper;

    @Override
    public void addCar(CarRequestDTO carRequestDTO, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        String normalizedPlate = carRequestDTO.getLicensePlate().toUpperCase().replaceAll("[ -]", "");

        // Check if car exists for this user (ignoring active status)
        Optional<Car> existingCarOpt = carRepository.findByLicensePlateAndUserIdIgnoringActive(normalizedPlate, userId);

        if (existingCarOpt.isPresent()) {
            Car existingCar = existingCarOpt.get();
            if (existingCar.isActive()) {
                throw new RuntimeException("You already have a car with this license plate (" + normalizedPlate + ").");
            } else {
                // Restore logic
                existingCar.setActive(true);
                existingCar.setMotExpiryDate(carRequestDTO.getMotExpiryDate());
                carRepository.save(existingCar);
                logger.info("Car restored successfully for user ID: {}", userId);
                return;
            }
        }

        // Check global existence if needed? No, user said "another user can add users1 license plate".
        // So we only care about the current user's cars.

        Car car = carMapper.toEntity(carRequestDTO);
        car.setUser(user);
        car.setLicensePlate(normalizedPlate); // Ensure normalized plate is saved

        carRepository.saveAndFlush(car);

        logger.info("Car added successfully for user ID: {}", userId);
    }

    @Override
    public void deleteCar(Long carId, Long userId) {
        Car car = carRepository.findById(carId)
                .orElseThrow(() -> new RuntimeException("Car not found"));

        if (!car.getUser().getId().equals(userId)) {
            logger.warn("Security breach attempt: User {} tried to delete car {}", userId, carId);
            throw new RuntimeException("You are not authorized to delete this car");
        }

        carRepository.delete(car);
        logger.info("Car {} deleted successfully", carId);
    }

    @Override
    public void addMotRecord(Long carId, LocalDate date, boolean passed, String notes) {
        Car car = carRepository.findById(carId)
                .orElseThrow(() -> new RuntimeException("Car not found"));

        MotRecord record = MotRecord.builder()
                .car(car)
                .inspectionDate(date)
                .passed(passed)
                .notes(notes)
                .build();

        motRecordRepository.save(record);

        if (passed) {
            car.setMotExpiryDate(date.plusYears(2)); // Default to 2 years? Prompt said "add options to add 30 days and 2 years". 
            // The existing logic was `date.plusYears(2)`. I'll keep it for now but the UI will handle the specific date updates.
            carRepository.save(car);
        }
    }

    @Override
    public void updateCarMotExpiry(Long carId, LocalDate newExpiryDate, Long userId) {
        Car car = carRepository.findById(carId)
                .orElseThrow(() -> new RuntimeException("Car not found"));

        if (!car.getUser().getId().equals(userId)) {
            logger.warn("Security breach attempt: User {} tried to update car {}", userId, carId);
            throw new RuntimeException("You are not authorized to update this car");
        }

        car.setMotExpiryDate(newExpiryDate);
        carRepository.save(car);
        logger.info("Car {} MOT expiry updated to {}", carId, newExpiryDate);
    }

    @Override
    public List<CarResponseDTO> getUserCars(Long userId) {
        List<Car> cars = carRepository.findByUserId(userId);

        return cars.stream()
                .map(carMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

}
