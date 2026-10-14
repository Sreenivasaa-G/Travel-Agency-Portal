import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class BookingManagement {

    private static final Scanner sc = new Scanner(System.in);

    // View booking history
    public static void viewBookingHistory() {

        System.out.print("Enter Customer ID: ");
        int customerId = sc.nextInt();

        String sql =
            "SELECT b.booking_id, p.destination, b.persons, " +
            "b.total_amount, b.booking_status " +
            "FROM bookings b " +
            "JOIN packages p ON b.package_id = p.package_id " +
            "WHERE b.customer_id = ? " +
            "ORDER BY b.booking_id DESC";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, customerId);

            try (ResultSet rs = ps.executeQuery()) {

                boolean found = false;

                System.out.println("\n========== BOOKING HISTORY ==========");

                while (rs.next()) {
                    found = true;

                    System.out.println("\nBooking ID: " +
                            rs.getInt("booking_id"));

                    System.out.println("Destination: " +
                            rs.getString("destination"));

                    System.out.println("Persons: " +
                            rs.getInt("persons"));

                    System.out.printf("Total Amount: Rs. %.2f%n",
                            rs.getDouble("total_amount"));

                    System.out.println("Status: " +
                            rs.getString("booking_status"));
                }

                if (!found) {
                    System.out.println("No bookings found.");
                }
            }

        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    // Cancel a booking
    public static void cancelBooking() {

        System.out.print("Enter Customer ID: ");
        int customerId = sc.nextInt();

        System.out.print("Enter Booking ID to cancel: ");
        int bookingId = sc.nextInt();

        String selectSQL =
            "SELECT package_id, persons FROM bookings " +
            "WHERE booking_id = ? AND customer_id = ? " +
            "AND booking_status = 'CONFIRMED' FOR UPDATE";

        String cancelSQL =
            "UPDATE bookings SET booking_status = 'CANCELLED' " +
            "WHERE booking_id = ? AND customer_id = ? " +
            "AND booking_status = 'CONFIRMED'";

        String seatsSQL =
            "UPDATE packages SET available_seats = available_seats + ? " +
            "WHERE package_id = ?";

        try (Connection con = DBConnection.getConnection()) {

            con.setAutoCommit(false);

            try {
                int packageId;
                int persons;

                // Find and lock the confirmed booking
                try (PreparedStatement ps = con.prepareStatement(selectSQL)) {

                    ps.setInt(1, bookingId);
                    ps.setInt(2, customerId);

                    try (ResultSet rs = ps.executeQuery()) {

                        if (!rs.next()) {
                            throw new SQLException(
                                "Booking not found or already cancelled."
                            );
                        }

                        packageId = rs.getInt("package_id");
                        persons = rs.getInt("persons");
                    }
                }

                // Mark booking as cancelled
                try (PreparedStatement ps = con.prepareStatement(cancelSQL)) {

                    ps.setInt(1, bookingId);
                    ps.setInt(2, customerId);

                    if (ps.executeUpdate() != 1) {
                        throw new SQLException(
                            "Unable to cancel booking."
                        );
                    }
                }

                // Restore available seats
                try (PreparedStatement ps = con.prepareStatement(seatsSQL)) {

                    ps.setInt(1, persons);
                    ps.setInt(2, packageId);

                    if (ps.executeUpdate() != 1) {
                        throw new SQLException(
                            "Unable to restore available seats."
                        );
                    }
                }

                con.commit();

                System.out.println(
                    "Booking cancelled successfully! Seats restored."
                );

            } catch (SQLException e) {

                con.rollback();

                System.out.println("Cancellation failed: " + e.getMessage());

            } finally {
                con.setAutoCommit(true);
            }

        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
        }
    }

    public static void main(String[] args) {

        Scanner menuScanner = new Scanner(System.in);

        while (true) {

            System.out.println("\n===== BOOKING MANAGEMENT =====");
            System.out.println("1. View Booking History");
            System.out.println("2. Cancel Booking");
            System.out.println("3. Exit");
            System.out.print("Enter choice: ");

            int choice = menuScanner.nextInt();

            switch (choice) {
                case 1:
                    viewBookingHistory();
                    break;

                case 2:
                    cancelBooking();
                    break;

                case 3:
                    System.out.println("Exiting Booking Management.");
                    return;

                default:
                    System.out.println("Invalid choice.");
            }
        }
    }
}