Travel Agency Portal using Java, JDBC and MySQL

Student: SREENIVASAA G
Domain: Miscellaneous / Lifestyle
Project Type: Java Mini Project

1. Overview

The Travel Agency Portal is a console-based Java application that manages customer registration, travel package browsing, destination searching, bookings, booking history, and booking cancellation. It uses JDBC (Java Database Connectivity) to connect to a MySQL database.

2. Features

Register customers and store their details in MySQL.

View available travel packages.

Search for destinations by name.

Book a package for one or more travellers.

Calculate the total booking amount.

Check and update package seat availability.

View booking history for a customer.

Cancel confirmed bookings and restore seats.

Use a single menu to access the application's features.

3. Technology Stack

Language: Java

Database: MySQL

Database API: JDBC

Driver: MySQL Connector/J

IDE: Visual Studio Code (or another Java IDE)

4. Project Structure

TravelAgencyPortal/
├── lib/
│   └── mysql-connector-j-<version>.jar
├── src/
│   ├── DBConnection.java
│   ├── CustomerService.java
│   ├── PackageService.java
│   ├── BookingService.java
│   ├── BookingManagement.java
│   └── Main.java
└── out/                       # Compiled .class files (generated)

5. Prerequisites

Install/configure:

A JDK compatible with your Java source code.

MySQL Server.

MySQL Connector/J.

Visual Studio Code or another Java IDE.

Download MySQL Connector/J from the official MySQL website:
https://dev.mysql.com/downloads/connector/j/

Place the downloaded Connector/J JAR file inside the lib folder. The commands below use lib/*, so the exact JAR filename can vary.

6. Database Setup

Connect to your local MySQL server and run the following SQL script. If the database already exists, back up any important data before dropping or recreating tables.

CREATE DATABASE IF NOT EXISTS travel_agency;
USE travel_agency;

CREATE TABLE IF NOT EXISTS customers (
    customer_id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    phone VARCHAR(15) NOT NULL
);

CREATE TABLE IF NOT EXISTS packages (
    package_id INT PRIMARY KEY AUTO_INCREMENT,
    destination VARCHAR(100) NOT NULL,
    duration INT NOT NULL,
    price DECIMAL(10,2) NOT NULL,
    available_seats INT NOT NULL
);

CREATE TABLE IF NOT EXISTS bookings (
    booking_id INT PRIMARY KEY AUTO_INCREMENT,
    customer_id INT NOT NULL,
    package_id INT NOT NULL,
    persons INT NOT NULL,
    total_amount DECIMAL(10,2) NOT NULL,
    booking_status VARCHAR(20) DEFAULT 'CONFIRMED',
    FOREIGN KEY (customer_id) REFERENCES customers(customer_id),
    FOREIGN KEY (package_id) REFERENCES packages(package_id)
);

INSERT INTO packages (destination, duration, price, available_seats)
SELECT 'Ooty', 3, 4500.00, 30
WHERE NOT EXISTS (SELECT 1 FROM packages WHERE destination = 'Ooty');

INSERT INTO packages (destination, duration, price, available_seats)
SELECT 'Kodaikanal', 4, 6000.00, 25
WHERE NOT EXISTS (SELECT 1 FROM packages WHERE destination = 'Kodaikanal');

INSERT INTO packages (destination, duration, price, available_seats)
SELECT 'Goa', 5, 12000.00, 20
WHERE NOT EXISTS (SELECT 1 FROM packages WHERE destination = 'Goa');

INSERT INTO packages (destination, duration, price, available_seats)
SELECT 'Kerala', 4, 8500.00, 15
WHERE NOT EXISTS (SELECT 1 FROM packages WHERE destination = 'Kerala');

INSERT INTO packages (destination, duration, price, available_seats)
SELECT 'Manali', 6, 18000.00, 10
WHERE NOT EXISTS (SELECT 1 FROM packages WHERE destination = 'Manali');

Note: CREATE TABLE IF NOT EXISTS does not update the structure of tables that already exist. If you previously created the tables using this project, you can normally keep them as they are. The sample package inserts above avoid adding the same destination again.

7. Configure the Database Connection

Open src/DBConnection.java and confirm that the connection details match your local MySQL setup:

private static final String URL =
        "jdbc:mysql://localhost:3306/travel_agency";
private static final String USER = "root";
private static final String PASSWORD = "YOUR_MYSQL_PASSWORD";

Replace YOUR_MYSQL_PASSWORD with your local MySQL password. Do not commit real credentials to a public repository. For a shared/public project, use environment variables or another configuration method rather than hard-coding passwords.

8. Compile and Run (Windows PowerShell)

Run these commands from the TravelAgencyPortal project root folder.

Compile all Java files:

javac -cp "lib/*" -d out src/*.java

Run the application:

java -cp "out;lib/*" Main

If the out directory does not exist, create it first:

New-Item -ItemType Directory -Force out

9. Main Menu

The application provides these menu options:

Register Customer

View Travel Packages

Search Destination

Book Travel Package

View Booking History

Cancel Booking

Exit

Choose an option by entering its number in the terminal and follow the prompts.

10. Database Tables

customers

Stores customer ID, name, email, and phone number.

packages

Stores package ID, destination, trip duration, price per person, and available seats.

bookings

Stores booking ID, customer ID, package ID, number of travellers, total amount, and booking status.

11. Testing Checklist

Register a customer and confirm the row in customers.

View all packages.

Search for an existing destination and a destination that does not exist.

Book a package using a valid customer ID and package ID.

Confirm the booking is stored and available seats decrease.

View booking history for the customer.

Cancel a confirmed booking and confirm the status becomes CANCELLED.

Confirm the cancelled booking's seats are restored.

Try invalid IDs and a booking quantity greater than the available seats.

12. Limitations

The current application uses a console interface.

It does not process real online payments.

Authentication and role-based access are not included.

Package management by an administrator is not included unless added separately.

Email and ticket generation are not implemented.

13. Future Enhancements

Add an administrator login and package management.

Create a graphical interface using Java Swing or JavaFX.

Add secure user authentication.

Generate booking receipts or PDF tickets.

Integrate a payment gateway in a production-ready version.

Add input validation and centralized configuration for database credentials.

14. Author

SREENIVASAA G
Project: Travel Agency Portal using JDBC