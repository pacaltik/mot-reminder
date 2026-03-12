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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class CarServiceImpl implements CarService {

    private static final Logger logger = LoggerFactory.getLogger(CarServiceImpl.class);

    private final MotRecordRepository motRecordRepository;
    private final CarRepository carRepository;
    private final UserRepository userRepository;
    private final CarMapper carMapper;

    // Constructor Injection (No @Autowired)
    public CarServiceImpl(MotRecordRepository motRecordRepository, CarRepository carRepository, UserRepository userRepository, CarMapper carMapper) {
        this.motRecordRepository = motRecordRepository;
        this.carRepository = carRepository;
        this.userRepository = userRepository;
        this.carMapper = carMapper;
    }

    @Override
    public void addCar(CarRequestDTO carRequestDTO, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Car car = carMapper.toEntity(carRequestDTO);
        
        if (carRepository.findByLicensePlate(car.getLicensePlate()).isPresent()) {
            throw new RuntimeException("Car with this license plate already exists");
        }
        
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
    @Transactional
    public void addMotRecord(Long carId, LocalDate date, boolean passed, String notes) {
        Car car = carRepository.findById(carId)
                .orElseThrow(() -> new RuntimeException("Car not found"));

        // new mot record
        MotRecord record = new MotRecord(car, date, passed, notes);

        // mot saved
        motRecordRepository.save(record);

        if (passed) {

            car.setMotExpiryDate(date.plusYears(2));
            carRepository.save(car);
        }
    }

    @Override
    public List<CarResponseDTO> getUserCars(Long userId) {
        List<Car> cars = carRepository.findByUserId(userId);

        // Convert all Entities to DTOs
        return cars.stream()
                .map(carMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

}
