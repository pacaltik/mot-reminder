package com.motchecker.mot_reminder.service;

import com.motchecker.mot_reminder.dto.request.UserRegisterDTO;
import com.motchecker.mot_reminder.model.User;

public interface UserService {
    void registerUser(UserRegisterDTO userRegisterDTO);
    User authenticateUser(String email, String password);
}