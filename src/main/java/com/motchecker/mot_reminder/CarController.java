package com.motchecker.mot_reminder;

import com.motchecker.mot_reminder.model.Car;
import com.motchecker.mot_reminder.model.User;
import com.motchecker.mot_reminder.repository.CarRepository;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class CarController {

    private static final Logger logger = LoggerFactory.getLogger(CarController.class);

    @Autowired
    private CarRepository carRepository;

    // 1. show the "Add Car" form
    @GetMapping("/add-car")
    public String showAddCarForm(Model model, HttpSession session) {
        if (session.getAttribute("loggedInUser") == null) {
            return "redirect:/login";
        }
        model.addAttribute("car", new Car()); // Prázdné auto pro formulář
        return "add-car";
    }

    // 2. process the form
    @PostMapping("/add-car")
    public String addCar(@Valid @ModelAttribute("car") Car car, BindingResult bindingResult, HttpSession session) {
        User user = (User) session.getAttribute("loggedInUser");
        if (user == null) return "redirect:/login";

        // Pokud SPZ nebo datum nesplňují pravidla, vrať uživatele zpět na formulář
        if (bindingResult.hasErrors()) {
            logger.warn("User {} failed to add a car due to validation errors.", user.getEmail());
            return "add-car";
        }

        // Převedeme SPZ na velká písmena (pro jistotu, kdyby uživatel zadal malá)
        car.setLicensePlate(car.getLicensePlate().toUpperCase());
        car.setUser(user);

        carRepository.save(car);
        logger.info("User {} added a new car with license plate: {}", user.getEmail(), car.getLicensePlate());

        return "redirect:/dashboard";
    }

    // 3. delete a car
    @PostMapping("/delete-car")
    public String deleteCar(@RequestParam Long carId, HttpSession session) {
        User user = (User) session.getAttribute("loggedInUser");
        if (user == null) return "redirect:/login";

        Car car = carRepository.findById(carId).orElse(null);

        if (car != null && car.getUser().getId().equals(user.getId())) {
            carRepository.delete(car);
            logger.info("User {} deleted car with license plate: {}", user.getEmail(), car.getLicensePlate());
        } else if (car != null) {
            logger.warn("SECURITY ALERT: User {} attempted to delete car ID {} belonging to someone else!", user.getEmail(), car.getId());
        } else {
            logger.warn("User {} attempted to delete non-existent car ID: {}", user.getEmail(), carId);
        }

        return "redirect:/dashboard";
    }
}