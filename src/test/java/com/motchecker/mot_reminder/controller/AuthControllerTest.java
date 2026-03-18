package com.motchecker.mot_reminder.controller;

import com.motchecker.mot_reminder.config.SecurityConfig;
import com.motchecker.mot_reminder.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AuthController.class)
@Import(SecurityConfig.class)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserService userService; // Controller needs Service, so let's mock it

    @Test
    void shouldRedirectToLogin_whenRegistrationIsSuccessful() throws Exception {
        // Act & Assert
        mockMvc.perform(post("/register")
                        .param("email", "test@example.com")
                        .param("password", "StrongPassword123!"))
                .andExpect(status().is3xxRedirection()) // Seeking for HTTP 302 (Redirect)
                .andExpect(redirectedUrl("/login")) // Redirect to log in
                .andExpect(flash().attributeExists("successMessage")); // Expecting success message
    }

    @Test
    void shouldReturnRegisterView_whenValidationFails() throws Exception {
        // Act & Assert (wrong email)
        mockMvc.perform(post("/register")
                        .param("email", "invalid-email") // wrong format
                        .param("password", "123")) // password is too short
                .andExpect(status().isOk()) // HTTP 200 (staying on page)
                .andExpect(view().name("register")) // staying on 'register'
                .andExpect(model().hasErrors()); // BindingResult is expecting errors
    }
}