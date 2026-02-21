package com.motchecker.mot_reminder;

import com.motchecker.mot_reminder.model.Car;
import com.motchecker.mot_reminder.model.User;
import com.motchecker.mot_reminder.repository.CarRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import java.time.LocalDate;

@Controller
public class CarController {

    @Autowired
    private CarRepository carRepository;

    // 1. show the "Add Car" form
    @GetMapping("/add-car")
    public String showAddCarForm(HttpSession session) {
        // security check
        if (session.getAttribute("loggedInUser") == null) {
            return "redirect:/login";
        }
        return "add-car";
    }

    // 2. process the form
    @PostMapping("/add-car")
    public String addCar(@RequestParam String licensePlate,
                         @RequestParam String expiryDate,
                         HttpSession session) {

        // retrieve the logged-in user from session
        User user = (User) session.getAttribute("loggedInUser");
        if (user == null) return "redirect:/login";

        // create new car
        Car car = new Car();
        car.setLicensePlate(licensePlate.toUpperCase()); // Store SPZ in uppercase
        car.setMotExpiryDate(LocalDate.parse(expiryDate)); // Convert String to Date
        car.setUser(user); // Assign the owner

        // save to DB
        carRepository.save(car);

        return "redirect:/dashboard";
    }
    // 3. delete a car
    @PostMapping("/delete-car")
    public String deleteCar(@RequestParam Long carId, HttpSession session) {
        User user = (User) session.getAttribute("loggedInUser");
        if (user == null) return "redirect:/login";

        // find the car in DB
        // (orElse(null) prevents crashing if car ID doesn't exist)
        Car car = carRepository.findById(carId).orElse(null);

        // security Check: Does the car exist AND does it belong to the logged-in user?
        if (car != null && car.getUser().getId().equals(user.getId())) {
            carRepository.delete(car);
        }

        return "redirect:/dashboard";
    }
}