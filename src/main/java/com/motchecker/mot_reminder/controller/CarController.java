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
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
                         HttpSession session,
                         RedirectAttributes redirectAttributes) {

        User user = (User) session.getAttribute("loggedInUser");
        if (user == null) return "redirect:/login";

        // input error
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Invalid input data. Please check your form.");
            return "redirect:/dashboard"; // Vracíme ho zpět na dashboard s chybou
        }

        try {
            carService.addCar(carRequestDTO, user.getId());

            // success
            redirectAttributes.addFlashAttribute("successMessage", "Car successfully added to your garage!");

        } catch (DataIntegrityViolationException e) {
            // license plate error
            redirectAttributes.addFlashAttribute("errorMessage", "Error: A car with this license plate already exists!");

        } catch (RuntimeException e) {
            // other errors
            redirectAttributes.addFlashAttribute("errorMessage", "Error: " + e.getMessage());
        }

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