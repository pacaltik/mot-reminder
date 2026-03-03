package com.motchecker.mot_reminder.mapper;

import com.motchecker.mot_reminder.dto.request.UserRegisterDTO;
import com.motchecker.mot_reminder.model.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    // Converts DTO from registration form into Entity
    public User toEntity(UserRegisterDTO dto) {
        User user = new User();
        user.setEmail(dto.getEmail());
        user.setPassword(dto.getPassword());
        return user;
    }
}