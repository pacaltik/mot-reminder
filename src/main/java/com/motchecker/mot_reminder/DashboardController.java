package com.motchecker.mot_reminder;

import com.motchecker.mot_reminder.model.User;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DashboardController {

    @GetMapping("/dashboard")
    public String showDashboard(HttpSession session, Model model) {
        // Retrieve user from session
        User user = (User) session.getAttribute("loggedInUser");

        // Security check: If no user is logged in, kick them out
        if (user == null) {
            return "redirect:/login";
        }

        // Pass user info to HTML
        model.addAttribute("user", user);
        return "dashboard";
    }
}