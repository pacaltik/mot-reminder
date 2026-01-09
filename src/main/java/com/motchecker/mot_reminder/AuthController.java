package com.motchecker.mot_reminder;

import com.motchecker.mot_reminder.model.User;
import com.motchecker.mot_reminder.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import jakarta.servlet.http.HttpSession;

@Controller
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    // 1. Show the registration form HTML
    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        // Create an empty User object to bind form data to
        model.addAttribute("user", new User());
        return "register";
    }

    // 2. Process the form data when user clicks "Submit"
    @PostMapping("/register")
    public String registerUser(User user, Model model) {
        // 1. Check if user already exists
        User existingUser = userRepository.findByEmail(user.getEmail());

        if (existingUser != null) {
            // If exists, add an error message to the model
            model.addAttribute("error", "An account with this email already exists.");
            // Return back to the registration form instead of redirecting
            return "register";
        }

        // 2. If valid, save to database
        userRepository.save(user);

        // 3. Redirect to home (success)
        return "redirect:/";
    }
    // 1. Show Login Page
    @GetMapping("/login")
    public String showLoginForm() {
        return "login";
    }

    // 2. Process Login
    @PostMapping("/login")
    public String loginUser(String email, String password, HttpSession session, Model model) {
        // Find user by email
        User user = userRepository.findByEmail(email);

        // Check if user exists AND password matches
        // (Note: In a real app, use BCrypt to check encrypted passwords!)
        if (user != null && user.getPassword().equals(password)) {
            // SUCCESS: Store user in the session
            session.setAttribute("loggedInUser", user);
            return "redirect:/dashboard";
        } else {
            // FAILURE: Show error
            model.addAttribute("error", "Invalid email or password");
            return "login";
        }
    }

    // 3. Logout
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate(); // Clear the session
        return "redirect:/";
    }
}