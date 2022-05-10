import java.sql.*;
import java.util.Scanner;

public class Project {
    ProgramFeatures features = new ProgramFeatures();
    Scanner input = new Scanner(System.in);

    /**
     * This method creates a project entry into project_details database.
     *
     * @param connection uses a connection object as parameter
     * @throws SQLException if connection failed
     */
    public void captureProject(Connection connection) throws SQLException {

        System.out.println("Choose the customer for this project");
        String cust = "customer";
        String customer = features.assignPersonnel(connection, cust);

        System.out.print("Enter project name: ");
        String projectName = input.nextLine();
        System.out.print("Enter project fee: ");
        float projectFee = Float.parseFloat(input.nextLine());
        System.out.print("Enter amount paid to date: ");
        float amountPaid = Float.parseFloat(input.nextLine());
        System.out.print("Enter project deadline (yyyy-mm-dd): ");
        String deadline = input.nextLine();
        System.out.print("Building Type: ");
        String buildingType = input.nextLine();
        System.out.print("Physical address: ");
        String physicalAddress = input.nextLine();
        System.out.print("ERF Number: ");
        int erfNumber = Integer.parseInt(input.nextLine());
        String finalised = "Not finalised";
        String finalisedDate = "0000-00-00";

        // Checking if projectName is empty. if its empty assigning the customer name and project building type as projectName
        if (projectName.isEmpty() || projectName.isBlank()) {
            projectName = customer + " " + buildingType;
        }

        // assigning project manager to project
        System.out.println("\nAssign the project manager for this project");
        String proj_man = "project_manager";
        String projectManager = features.assignPersonnel(connection, proj_man);

        // assigning architect to project
        System.out.println("\nAssign the architect for this project");
        String arch = "architect";
        String architect = features.assignPersonnel(connection, arch);

        // assigning structural engineer to project
        System.out.println("\nAssign the structural engineer for this project");
        String struct_engin = "structural_engineer";
        String structuralEngineer = features.assignPersonnel(connection, struct_engin);

        // assigning contractor to project
        System.out.println("\nAssign the contractor for this project");
        String contr = "contractor";
        String contractor = features.assignPersonnel(connection, contr);

        PreparedStatement update = connection.prepareStatement(
                "INSERT INTO project_details (project_name, project_Fee, amount_paid, deadline, building_type, " +
                        "physical_address, erf_number, finalised, finalised_date, customer, project_manager, architect, " +
                        "structural_engineer, contractor) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?)");

        update.setString(1, projectName);
        update.setFloat(2, projectFee);
        update.setFloat(3, amountPaid);
        update.setString(4, deadline);
        update.setString(5, buildingType);
        update.setString(6, physicalAddress);
        update.setInt(7, erfNumber);
        update.setString(8, finalised);
        update.setString(9, finalisedDate);
        update.setString(10, customer);
        update.setString(11, projectManager);
        update.setString(12, architect);
        update.setString(13, structuralEngineer);
        update.setString(14, contractor);
        update.executeUpdate();
        System.out.println("\nProject successfully submitted\n");

    }
}
