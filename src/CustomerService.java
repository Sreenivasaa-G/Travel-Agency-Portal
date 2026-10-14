import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Scanner;

public class CustomerService {

    private static final Scanner sc = new Scanner(System.in);

    public static void registerCustomer() {

        System.out.println("\n===== CUSTOMER REGISTRATION =====");

        System.out.print("Enter customer name: ");
        String name = sc.nextLine();

        System.out.print("Enter email: ");
        String email = sc.nextLine();

        System.out.print("Enter phone number: ");
        String phone = sc.nextLine();

        String sql = "INSERT INTO customers (name, email, phone) VALUES (?, ?, ?)";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, name);
            ps.setString(2, email);
            ps.setString(3, phone);

            int rows = ps.executeUpdate();

            if (rows > 0) {
                System.out.println("\nCustomer registered successfully!");
            }

        } catch (SQLException e) {
            System.out.println("Registration failed: " + e.getMessage());
        }
    }
}