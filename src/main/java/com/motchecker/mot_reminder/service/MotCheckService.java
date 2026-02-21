package com.motchecker.mot_reminder.service;

import com.motchecker.mot_reminder.model.Car;
import com.motchecker.mot_reminder.repository.CarRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class MotCheckService {

    @Autowired
    private CarRepository carRepository;

    @Autowired 
    private EmailService emailService;

    // this runs automatically based on the cron expression
    // "0 * * * * *" = run every minute (for testing purposes)
    // "0 0 9 * * *" = run every day at 9:00 AM (for real app)
    @Scheduled(cron = "0 0 9 * * *")
    public void checkCarsForExpiry() {
        System.out.println("--------------------------------------------------");
        System.out.println("STARTING A CAR CHECK...");

        List<Car> allCars = carRepository.findAll();
        LocalDate today = LocalDate.now();
        LocalDate warningDate = today.plusDays(30);

        System.out.println("Today is: " + today);
        System.out.println("Finding cars with expiration: " + warningDate);
        System.out.println("Number of cars: " + allCars.size());

        for (Car car : allCars) {
            System.out.println(" -> Checking car: " + car.getLicensePlate() + ", expiration: " + car.getMotExpiryDate());

            // comparison
            if (car.getMotExpiryDate().isEqual(warningDate)) {
                System.out.println("    !!! MATCH FOUND! Trying to send email on: " + car.getUser().getEmail());

                try {
                    String subject = "MOT notification: " + car.getLicensePlate();
                    String body = "Hello, your car's MOT is about to expire. " + car.getLicensePlate();

                    emailService.sendEmail(car.getUser().getEmail(), subject, body);
                    System.out.println("    >>> Email was succesfully sent.");
                } catch (Exception e) {
                    System.out.println("    !!! Email sending error: " + e.getMessage());
                    e.printStackTrace(); // error quote
                }
            } else {
                System.out.println("  (this car isn't expired yet)");
            }
        }
        System.out.println("--------------------------------------------------");
    }
}