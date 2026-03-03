package com.motchecker.mot_reminder.controller;

import com.motchecker.mot_reminder.model.User;
import com.motchecker.mot_reminder.service.CarService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DashboardController {

    private final CarService carService;

    public DashboardController(CarService carService) {
        this.carService = carService;
    }

    @GetMapping("/dashboard")
    public String showDashboard(HttpSession session, Model model) {
        User user = (User) session.getAttribute("loggedInUser");
        if (user == null) {
            return "redirect:/login";
        }

        model.addAttribute("user", user);

        model.addAttribute("cars", carService.getUserCars(user.getId()));

        return "dashboard";
    }
}