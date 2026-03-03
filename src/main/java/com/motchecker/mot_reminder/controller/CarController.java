package com.motchecker.mot_reminder.controller;

import com.motchecker.mot_reminder.dto.request.CarRequestDTO;
import com.motchecker.mot_reminder.model.User;
import com.motchecker.mot_reminder.service.CarService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
public class CarController {

    private final CarService carService;

    // Constructor Injection
    public CarController(CarService carService) {
        this.carService = carService;
    }

    @GetMapping("/add-car")
    public String showAddCarForm(Model model, HttpSession session) {
        if (session.getAttribute("loggedInUser") == null) return "redirect:/login";

        // Passing empty DTO to the view, NOT the Entity!
        model.addAttribute("car", new CarRequestDTO());
        return "add-car";
    }

    @PostMapping("/add-car")
    public String addCar(@Valid @ModelAttribute("car") CarRequestDTO carRequestDTO,
                         BindingResult bindingResult,
                         HttpSession session) {

        User user = (User) session.getAttribute("loggedInUser");
        if (user == null) return "redirect:/login";

        if (bindingResult.hasErrors()) {
            return "add-car";
        }

        // The Controller delegates all business logic to the Service
        carService.addCar(carRequestDTO, user.getId());

        return "redirect:/dashboard";
    }

    @PostMapping("/delete-car")
    public String deleteCar(@RequestParam Long carId, HttpSession session) {
        User user = (User) session.getAttribute("loggedInUser");
        if (user == null) return "redirect:/login";

        // The Controller delegates all security checks and logic to the Service
        carService.deleteCar(carId, user.getId());

        return "redirect:/dashboard";
    }
}