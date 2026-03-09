package com.motchecker.mot_reminder.controller;

import com.motchecker.mot_reminder.dto.response.CarResponseDTO;
import com.motchecker.mot_reminder.model.MotRecord;
import com.motchecker.mot_reminder.model.User;
import com.motchecker.mot_reminder.repository.MotRecordRepository;
import com.motchecker.mot_reminder.service.CarService;
import jakarta.servlet.http.HttpSession;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Controller
public class DashboardController {

    private final CarService carService;
    private final MotRecordRepository motRecordRepository;

    public DashboardController(CarService carService, MotRecordRepository motRecordRepository) {
        this.carService = carService;
        this.motRecordRepository = motRecordRepository;
    }

    @GetMapping("/dashboard")
    public String showDashboard(HttpSession session, Model model) {
        User user = (User) session.getAttribute("loggedInUser");
        if (user == null) {
            return "redirect:/login";
        }

        model.addAttribute("user", user);

        model.addAttribute("cars", carService.getUserCars(user.getId()));

        List<CarResponseDTO> userCars = carService.getUserCars(user.getId());
        model.addAttribute("cars", userCars);

        if (!userCars.isEmpty()) {
            Long firstCarId = userCars.getFirst().getId();
            List<MotRecord> history = motRecordRepository.findByCarIdOrderByInspectionDateDesc(firstCarId);
            model.addAttribute("motHistory", history);
        } else {
            model.addAttribute("motHistory", new ArrayList<>());
        }

        return "dashboard";
    }
    @PostMapping("/add-mot-record")
    public String addMotRecord(@RequestParam Long carId,
                               @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
                               @RequestParam boolean passed,
                               @RequestParam String notes) {

        carService.addMotRecord(carId, date, passed, notes);
        return "redirect:/dashboard";
    }
}