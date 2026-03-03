package com.motchecker.mot_reminder.service.impl;

import com.motchecker.mot_reminder.dto.request.CarRequestDTO;
import com.motchecker.mot_reminder.dto.response.CarResponseDTO;
import com.motchecker.mot_reminder.mapper.CarMapper;
import com.motchecker.mot_reminder.model.Car;
import com.motchecker.mot_reminder.model.User;
import com.motchecker.mot_reminder.repository.CarRepository;
import com.motchecker.mot_reminder.repository.UserRepository;
import com.motchecker.mot_reminder.service.CarService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class CarServiceImpl implements CarService {

    private static final Logger logger = LoggerFactory.getLogger(CarServiceImpl.class);

    private final CarRepository carRepository;
    private final UserRepository userRepository;
    private final CarMapper carMapper;

    // Constructor Injection (No @Autowired)
    public CarServiceImpl(CarRepository carRepository, UserRepository userRepository, CarMapper carMapper) {
        this.carRepository = carRepository;
        this.userRepository = userRepository;
        this.carMapper = carMapper;
    }

    @Override
    public void addCar(CarRequestDTO carRequestDTO, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Car car = carMapper.toEntity(carRequestDTO);
        car.setUser(user);

        carRepository.saveAndFlush(car);

        logger.info("Car added successfully for user ID: {}", userId);
    }

    @Override
    public void deleteCar(Long carId, Long userId) {
        Car car = carRepository.findById(carId)
                .orElseThrow(() -> new RuntimeException("Car not found"));

        // Business logic validation: Does the car belong to the user?
        if (!car.getUser().getId().equals(userId)) {
            logger.warn("Security breach attempt: User {} tried to delete car {}", userId, carId);
            throw new RuntimeException("You are not authorized to delete this car");
        }

        carRepository.delete(car);
        logger.info("Car {} deleted successfully", carId);
    }

    @Override
    public List<CarResponseDTO> getUserCars(Long userId) {
        List<Car> cars = carRepository.findByUserId(userId); // You need to add this method in CarRepository!

        // Convert all Entities to DTOs
        return cars.stream()
                .map(carMapper::toResponseDTO)
                .collect(Collectors.toList());
    }
}