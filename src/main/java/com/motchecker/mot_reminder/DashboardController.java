package com.motchecker.mot_reminder;

import com.motchecker.mot_reminder.model.User;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import com.motchecker.mot_reminder.repository.CarRepository; // Přidej import
import com.motchecker.mot_reminder.model.Car;
import java.util.List;

@Controller
public class DashboardController {

    // Inject CarRepository
    @Autowired
    private CarRepository carRepository;

    @GetMapping("/dashboard")
    public String showDashboard(HttpSession session, Model model) {
        User user = (User) session.getAttribute("loggedInUser");
        if (user == null) {
            return "redirect:/login";
        }

        model.addAttribute("user", user);

        // load user cars
        List<Car> myCars = carRepository.findByUser(user);
        model.addAttribute("cars", myCars);

        return "dashboard";
    }
}