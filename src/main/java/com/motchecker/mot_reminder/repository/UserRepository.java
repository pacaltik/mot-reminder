package com.motchecker.mot_reminder.repository;

import com.motchecker.mot_reminder.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    // custom query to find a user by email
    User findByEmail(String email);
}