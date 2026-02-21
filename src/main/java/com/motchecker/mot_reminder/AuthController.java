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

    // 1. show the registration form HTML
    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        // create an empty User object to bind form data to
        model.addAttribute("user", new User());
        return "register";
    }

    // 2. process the form data when user clicks "Submit"
    @PostMapping("/register")
    public String registerUser(User user, Model model) {
        // 1. check if user already exists
        User existingUser = userRepository.findByEmail(user.getEmail());

        if (existingUser != null) {
            // if exists, add an error message to the model
            model.addAttribute("error", "An account with this email already exists.");
            // Return back to the registration form instead of redirecting
            return "register";
        }

        // 2. if valid, save to database
        userRepository.save(user);

        // 3. redirect to home (success)
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
        // find user by email
        User user = userRepository.findByEmail(email);

        // check if user exists AND password matches
        // (note: In a real app, use BCrypt to check encrypted passwords!)
        if (user != null && user.getPassword().equals(password)) {
            // SUCCESS: store user in the session
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
        session.invalidate(); // clear the session
        return "redirect:/";
    }
}