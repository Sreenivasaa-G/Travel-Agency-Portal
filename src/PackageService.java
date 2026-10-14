import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class PackageService {

    private static final Scanner sc = new Scanner(System.in);

    // Display all travel packages
    public static void viewPackages() {

        String sql = "SELECT * FROM packages";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            System.out.println("\n========== AVAILABLE TRAVEL PACKAGES ==========");

            boolean found = false;

            while (rs.next()) {
                found = true;

                System.out.println("\nPackage ID: " + rs.getInt("package_id"));
                System.out.println("Destination: " + rs.getString("destination"));
                System.out.println("Duration: " + rs.getInt("duration") + " days");
                System.out.printf("Price: Rs. %.2f%n", rs.getDouble("price"));
                System.out.println("Available Seats: " + rs.getInt("available_seats"));
                System.out.println("-----------------------------------------------");
            }

            if (!found) {
                System.out.println("No travel packages available.");
            }

        } catch (SQLException e) {
            System.out.println("Error retrieving packages: " + e.getMessage());
        }
    }

    // Search packages by destination
    public static void searchDestination() {

        System.out.print("\nEnter destination to search: ");
        String destination = sc.nextLine().trim();

        String sql = "SELECT * FROM packages WHERE destination LIKE ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, "%" + destination + "%");

            try (ResultSet rs = ps.executeQuery()) {

                boolean found = false;

                System.out.println("\n========== SEARCH RESULTS ==========");

                while (rs.next()) {
                    found = true;

                    System.out.println("\nPackage ID: " + rs.getInt("package_id"));
                    System.out.println("Destination: " + rs.getString("destination"));
                    System.out.println("Duration: " + rs.getInt("duration") + " days");
                    System.out.printf("Price: Rs. %.2f%n", rs.getDouble("price"));
                    System.out.println("Available Seats: " + rs.getInt("available_seats"));
                }

                if (!found) {
                    System.out.println("No packages found for that destination.");
                }
            }

        } catch (SQLException e) {
            System.out.println("Search failed: " + e.getMessage());
        }
    }
}