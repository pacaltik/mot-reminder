package com.motchecker.mot_reminder;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling; // Import this!

@SpringBootApplication
@EnableScheduling // Enable the scheduler
public class MotReminderApplication {
	public static void main(String[] args) {
		SpringApplication.run(MotReminderApplication.class, args);
	}
}
