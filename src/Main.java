import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        while (true) {

            System.out.println("\n======================================");
            System.out.println("       TRAVEL AGENCY PORTAL");
            System.out.println("======================================");

            System.out.println("1. Register Customer");
            System.out.println("2. View Travel Packages");
            System.out.println("3. Search Destination");
            System.out.println("4. Book Travel Package");
            System.out.println("5. View Booking History");
            System.out.println("6. Cancel Booking");
            System.out.println("7. Exit");

            System.out.print("\nEnter your choice: ");

            if (!sc.hasNextInt()) {
                System.out.println("Please enter a valid number.");
                sc.nextLine();
                continue;
            }

            int choice = sc.nextInt();

            switch (choice) {

                case 1:
                    CustomerService.registerCustomer();
                    break;

                case 2:
                    PackageService.viewPackages();
                    break;

                case 3:
                    PackageService.searchDestination();
                    break;

                case 4:
                    BookingService.bookPackage();
                    break;

                case 5:
                    BookingManagement.viewBookingHistory();
                    break;

                case 6:
                    BookingManagement.cancelBooking();
                    break;

                case 7:
                    System.out.println(
                        "Thank you for using Travel Agency Portal!"
                    );
                    sc.close();
                    return;

                default:
                    System.out.println("Invalid choice. Try again.");
            }
        }
    }
}
//java -cp "out;lib/*" Main