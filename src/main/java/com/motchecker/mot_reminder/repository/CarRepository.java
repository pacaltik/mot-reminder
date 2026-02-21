package com.motchecker.mot_reminder.repository;

import com.motchecker.mot_reminder.model.Car;
import com.motchecker.mot_reminder.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface CarRepository extends JpaRepository<Car, Long> {

    // find all cars belonging to a specific user
    List<Car> findByUser(User user);
}