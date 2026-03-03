package com.motchecker.mot_reminder.service;

import com.motchecker.mot_reminder.model.Car;
import com.motchecker.mot_reminder.repository.CarRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class MotCheckService {

    private static final Logger logger = LoggerFactory.getLogger(MotCheckService.class);


    @Autowired
    private CarRepository carRepository;

    @Autowired 
    private EmailService emailService;

    // "0 * * * * *" = run every minute (for testing)
    // "0 0 9 * * *" = run every day at 9:00 AM (for real app)
    @Scheduled(cron = "0 0 9 * * *")
    public void checkCarsForExpiry() {
        logger.info("--------------------------------------------------");
        logger.info("STARTING A CAR CHECK...");

        List<Car> allCars = carRepository.findAll();
        LocalDate today = LocalDate.now();
        LocalDate warningDate = today.plusDays(30);

        logger.info("Today is: " + today);
        logger.info("Finding cars with expiration: " + warningDate);
        logger.info("Number of cars: " + allCars.size());

        for (Car car : allCars) {
            logger.info(" -> Checking car: " + car.getLicensePlate() + ", expiration: " + car.getMotExpiryDate());

            // comparison
            if (car.getMotExpiryDate().isEqual(warningDate)) {
                logger.info("    !!! MATCH FOUND! Trying to send email on: " + car.getUser().getEmail());

                try {
                    String subject = "MOT notification: " + car.getLicensePlate();
                    String body = "Hello, your car's MOT is about to expire. " + car.getLicensePlate();

                    emailService.sendEmail(car.getUser().getEmail(), subject, body);
                    logger.info("    >>> Email was succesfully sent.");
                } catch (Exception e) {
                    // logger use
                    logger.error("Chyba při odesílání emailu pro auto {}: {}", car.getLicensePlate(), e.getMessage(), e);
                }
            } else {
                logger.info("  (this car isn't expired yet)");
            }
        }
        logger.info("--------------------------------------------------");
    }
}