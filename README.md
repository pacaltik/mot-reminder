Welcome to MOT Reminder README!

This is very simple showcase of Java app for reminding You to visit regualar inspection of with vehicle.
App is set by default to email you 30 days before your inspection. Check runs every day at 9:00 AM.

For Docker use:

docker run --name mot-mysql -e MYSQL_ROOT_PASSWORD=root -e MYSQL_DATABASE=mot_reminder_db -p 3306:3306 -d mysql:8.0

Login if another server settings: 
//spring.datasource.username=root
//spring.datasource.password=root

Thank you for seeing my code
with regards,
Michal Pacalt
