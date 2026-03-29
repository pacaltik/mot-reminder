package com.motchecker.mot_reminder.repository;

import com.motchecker.mot_reminder.model.Car;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface CarRepository extends JpaRepository<Car, Long> {

    List<Car> findByUserId(Long userId);

    @org.springframework.data.jpa.repository.Query(value = "SELECT * FROM cars WHERE license_plate = :licensePlate AND user_id = :userId", nativeQuery = true)
    java.util.Optional<Car> findByLicensePlateAndUserIdIgnoringActive(@org.springframework.data.repository.query.Param("licensePlate") String licensePlate, @org.springframework.data.repository.query.Param("userId") Long userId);
}