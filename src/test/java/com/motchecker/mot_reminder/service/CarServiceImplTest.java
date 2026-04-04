package com.motchecker.mot_reminder.service;

import com.motchecker.mot_reminder.dto.request.CarRequestDTO;
import com.motchecker.mot_reminder.mapper.CarMapper;
import com.motchecker.mot_reminder.model.Car;
import com.motchecker.mot_reminder.model.User;
import com.motchecker.mot_reminder.repository.CarRepository;
import com.motchecker.mot_reminder.repository.MotRecordRepository;
import com.motchecker.mot_reminder.repository.UserRepository;
import com.motchecker.mot_reminder.service.impl.CarServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CarServiceImplTest {

    @Mock
    private MotRecordRepository motRecordRepository;

    @Mock
    private CarRepository carRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private CarMapper carMapper;

    @InjectMocks
    private CarServiceImpl carService;

    @Test
    void shouldAddCarSuccessfully() {
        // Arrange
        CarRequestDTO requestDTO = new CarRequestDTO();
        requestDTO.setLicensePlate("1A1 1111");
        requestDTO.setMotExpiryDate(LocalDate.now().plusYears(2));

        User mockUser = new User();
        mockUser.setId(1L);

        Car mockCar = new Car(); // fake car
        mockCar.setLicensePlate("1A1 1111");
        mockCar.setUser(mockUser);

        when(userRepository.findById(1L)).thenReturn(Optional.of(mockUser));
        when(carRepository.findByLicensePlateAndUserIdIgnoringActive(anyString(), eq(1L))).thenReturn(Optional.empty());
        when(carMapper.toEntity(any(CarRequestDTO.class))).thenReturn(mockCar);

        // Act
        carService.addCar(requestDTO, 1L);

        // Assert
        verify(carRepository, times(1)).saveAndFlush(any(Car.class));
    }

    @Test
    void shouldThrowExceptionWhenDeletingCarOfAnotherUser() {
        // Arrange
        User owner = new User();
        owner.setId(1L);

        Car car = new Car();
        car.setId(100L);
        car.setUser(owner);

        when(carRepository.findById(100L)).thenReturn(Optional.of(car));

        // Act & Assert
        assertThrows(RuntimeException.class, () -> carService.deleteCar(100L, 2L), "The application should prevent the deletion of another car and throw an exception.");

        // We will verify that the car has NOT been deleted from the database.
        verify(carRepository, never()).delete(any(Car.class));
    }
}