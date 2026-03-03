package com.motchecker.mot_reminder.repository;

import com.motchecker.mot_reminder.model.Car;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface CarRepository extends JpaRepository<Car, Long> {

    List<Car> findByUserId(Long userId);

}