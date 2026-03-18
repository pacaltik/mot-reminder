# MOT Reminder

> A production-ready Spring Boot web application designed to help users track their vehicle's MOT (Ministry of Transport) test expiry dates and receive automated email notifications.

![Java](https://img.shields.io/badge/Java-21-orange.svg)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.4.3-brightgreen.svg)
![MySQL](https://img.shields.io/badge/MySQL-8.0-blue.svg)
![Docker](https://img.shields.io/badge/Docker-Ready-blue.svg)

## Table of Contents
- [Features](#features)
- [Tech Stack](#tech-stack)
- [Getting Started](#getting-started)

## Features
* **User Authentication:** 
  * Secure registration and login system with database constraints.
* **Garage Management:** 
  * Add, view, and delete vehicles with their license plates and MOT expiry dates.
* **Smart Dashboard:** 
  * Visual color-coded indicators (Green/Orange/Red) showing exact days left until MOT expiration.
* **History Tracking:** 
  * Log past MOT inspections with results (Passed/Failed) and mechanics' notes.
* **Automated Email Notifications:** 
  * A scheduled background job (Cron) that emails users 30, 14, and 7 days before their MOT expires.
* **Data Integrity:** 
  * Exception handling for database constraints (e.g., duplicate license plates or emails) with user-friendly UI feedback.

## Tech Stack
* **Backend:** 
  * Java 21, Spring Boot 3, Spring Data JPA, Spring Mail, Hibernate
* **Frontend:** 
  * Thymeleaf, HTML5, CSS3
* **Database:** 
  * MySQL (containerized via Docker)
* **Testing:** 
  * JUnit 5

##  Getting Started

### Prerequisites
* JDK 21+
* Maven
* Docker (for MySQL database)

### Installation & Setup

1. **Clone the repository**
   ```bash
   git clone [https://github.com/pacaltik/mot-reminder.git](https://github.com/pacaltik/mot-reminder.git)
   cd mot-reminder

2. **Start the Database**
* Ensure Docker is running and start the MySQL container:
    ```bash
   docker run --name mot-mysql -e MYSQL_ROOT_PASSWORD=root -e MYSQL_DATABASE=mot_reminder_db -p 3306:3306 -d mysql:8.0

3. **Environment Variables**
* The application uses environment variables for sensitive data. Configure them in your IDE or export them in your terminal before running:
    ```bash
    export DB_USERNAME=root
    export DB_PASSWORD=password
    export MAIL_USERNAME=mot.reminder.pacalt@seznam.cz
    export MAIL_PASSWORD=

4. **Run the Application**
    ```bash
   mvn spring-boot:run