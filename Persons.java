import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Scanner;

public class Persons {

    ProgramFeatures features = new ProgramFeatures();

    /**
     * This method receives personnel details from user input and inserts it to the database.
     * it will be called in method capturePersonnelDetails
     *
     * @param connection uses a connection object as parameter
     * @param input      uses a scanner object as parameter
     * @param table      uses a string as parameter
     * @throws SQLException if connection failed
     */
    public static void details(Connection connection, Scanner input, String table) throws SQLException {
        // Requesting user input
        System.out.println("\nPlease enter details below");
        System.out.print("Name: ");
        String name = input.nextLine();
        System.out.print("Number: ");
        String telNo = input.nextLine();
        System.out.print("Email Address: ");
        String email = input.nextLine();
        System.out.print("Physical Address: ");
        String address = input.nextLine();

        PreparedStatement update = connection.prepareStatement("INSERT INTO " + table + "(name, tel_no, email, address) VALUES (?,?,?,?)");
        update.setString(1, name);
        update.setString(2, telNo);
        update.setString(3, email);
        update.setString(4, address);
        update.executeUpdate();
        System.out.println("\nDetails submitted\n");
    }

    /**
     * This method allows the user to choose who's details to capture.
     * it inserts the data by calling on method "details"
     *
     * @throws SQLException if connection failed
     */
    public void capturePersonnelDetails() throws SQLException {
        Connection connection = features.serverConnection();
        Scanner input = new Scanner(System.in);


        String menuOption = "";
        String table;
        while (!menuOption.equals("6")) {
            System.out.println("""

                    Who's details will you be entering?\s
                    1. customer
                    2. project manager
                    3. architect
                    4. structural engineer
                    5. contractor
                    6. exit""");

            System.out.print("Choice: ");
            menuOption = input.nextLine();
            switch (menuOption) {
                case "1" -> {
                    table = "customer";
                    details(connection, input, table);
                }
                case "2" -> {
                    table = "project_manager";
                    details(connection, input, table);
                }
                case "3" -> {
                    table = "architect";
                    details(connection, input, table);
                }
                case "4" -> {
                    table = "structural_engineer";
                    details(connection, input, table);
                }
                case "5" -> {
                    table = "contractor";
                    details(connection, input, table);
                }
                case "6" -> System.out.println("Returning to main menu");
                default -> System.out.println("\nPlease enter a valid option\n");
            }
        }
    }
}
