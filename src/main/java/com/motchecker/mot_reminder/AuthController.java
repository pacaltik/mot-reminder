package com.motchecker.mot_reminder;

import com.motchecker.mot_reminder.model.User;
import com.motchecker.mot_reminder.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    @Autowired
    private UserRepository userRepository;

    // 1. show the registration form HTML
    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }

    // 2. process the form data when user clicks "Submit"
    @PostMapping("/register")
    public String registerUser(User user, Model model) {
        logger.debug("Registration attempt for email: {}", user.getEmail());

        User existingUser = userRepository.findByEmail(user.getEmail());
        if (existingUser != null) {
            logger.warn("Registration failed: Email {} is already in use.", user.getEmail());
            model.addAttribute("error", "An account with this email already exists.");
            return "register";
        }

        userRepository.save(user);
        logger.info("New user registered successfully: {}", user.getEmail());
        return "redirect:/";
    }
    // 1.show Login Page
    @GetMapping("/login")
    public String showLoginForm() {
        return "login";
    }

    // 2. process Login
    @PostMapping("/login")
    public String loginUser(String email, String password, HttpSession session, Model model) {
        User user = userRepository.findByEmail(email);

        if (user != null && user.getPassword().equals(password)) {
            session.setAttribute("loggedInUser", user);
            logger.info("User logged in successfully: {}", email);
            return "redirect:/dashboard";
        } else {
            logger.warn("Failed login attempt for email: {}", email);
            model.addAttribute("error", "Invalid email or password");
            return "login";
        }
    }

    // 3. Logout
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        User user = (User) session.getAttribute("loggedInUser");
        if (user != null) {
            logger.info("User logged out: {}", user.getEmail());
        }
        session.invalidate();
        return "redirect:/";
    }
}