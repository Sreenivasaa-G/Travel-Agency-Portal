import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class BookingService {

    private static final Scanner sc = new Scanner(System.in);

    // Book a travel package
    public static void bookPackage() {

        System.out.println("\n===== BOOK TRAVEL PACKAGE =====");

        System.out.print("Enter Customer ID: ");
        int customerId = sc.nextInt();

        System.out.print("Enter Package ID: ");
        int packageId = sc.nextInt();

        System.out.print("Enter Number of Persons: ");
        int persons = sc.nextInt();

        if (persons <= 0) {
            System.out.println("Number of persons must be greater than zero.");
            return;
        }

        String customerSQL =
                "SELECT customer_id FROM customers WHERE customer_id = ?";

        String packageSQL =
                "SELECT destination, price, available_seats " +
                "FROM packages WHERE package_id = ? FOR UPDATE";

        String bookingSQL =
                "INSERT INTO bookings " +
                "(customer_id, package_id, persons, total_amount, booking_status) " +
                "VALUES (?, ?, ?, ?, 'CONFIRMED')";

        String updateSeatsSQL =
                "UPDATE packages SET available_seats = available_seats - ? " +
                "WHERE package_id = ? AND available_seats >= ?";

        try (Connection con = DBConnection.getConnection()) {

            con.setAutoCommit(false);

            try {
                // 1. Verify customer exists
                try (PreparedStatement ps = con.prepareStatement(customerSQL)) {

                    ps.setInt(1, customerId);

                    try (ResultSet rs = ps.executeQuery()) {
                        if (!rs.next()) {
                            throw new SQLException("Customer ID does not exist.");
                        }
                    }
                }

                // 2. Lock package row and check availability
                String destination;
                double price;
                int availableSeats;

                try (PreparedStatement ps = con.prepareStatement(packageSQL)) {

                    ps.setInt(1, packageId);

                    try (ResultSet rs = ps.executeQuery()) {

                        if (!rs.next()) {
                            throw new SQLException("Package ID does not exist.");
                        }

                        destination = rs.getString("destination");
                        price = rs.getDouble("price");
                        availableSeats = rs.getInt("available_seats");
                    }
                }

                if (availableSeats < persons) {
                    throw new SQLException(
                            "Only " + availableSeats + " seats are available."
                    );
                }

                // 3. Calculate total amount
                double totalAmount = price * persons;

                // 4. Update available seats
                try (PreparedStatement ps = con.prepareStatement(updateSeatsSQL)) {

                    ps.setInt(1, persons);
                    ps.setInt(2, packageId);
                    ps.setInt(3, persons);

                    int rows = ps.executeUpdate();

                    if (rows != 1) {
                        throw new SQLException("Unable to update available seats.");
                    }
                }

                // 5. Insert booking
                int bookingId;

                try (PreparedStatement ps = con.prepareStatement(
                        bookingSQL,
                        PreparedStatement.RETURN_GENERATED_KEYS)) {

                    ps.setInt(1, customerId);
                    ps.setInt(2, packageId);
                    ps.setInt(3, persons);
                    ps.setDouble(4, totalAmount);

                    ps.executeUpdate();

                    try (ResultSet rs = ps.getGeneratedKeys()) {

                        if (!rs.next()) {
                            throw new SQLException("Booking ID generation failed.");
                        }

                        bookingId = rs.getInt(1);
                    }
                }

                // 6. Commit transaction
                con.commit();

                System.out.println("\nBooking successful!");
                System.out.println("Booking ID: " + bookingId);
                System.out.println("Destination: " + destination);
                System.out.println("Persons: " + persons);
                System.out.printf("Total Amount: Rs. %.2f%n", totalAmount);
                System.out.println("Status: CONFIRMED");

            } catch (SQLException e) {

                con.rollback();

                System.out.println("\nBooking failed: " + e.getMessage());
                System.out.println("No booking changes were saved.");

            } finally {
                con.setAutoCommit(true);
            }

        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        bookPackage();
    }
}