import java.sql.*;
import java.time.LocalDate;
import java.util.*;

public class ProgramFeatures {

    Scanner input = new Scanner(System.in);
    LocalDate myDate = LocalDate.now();

    /**
     * This method connects to the PoisPMS database, via the jdbc:mysql: channel on localhost (this PC)
     *
     * @return Connection object
     * @throws SQLException if connection failed
     */
    public Connection serverConnection() throws SQLException {
        return DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/poispms?useSSL=false",
                "adminpoisdb",
                "P01sPMS2021"
        );
    }

    /**
     * This method creates a table and will be called by searchProjects
     *
     * @param results uses ResultSet to create and display table
     * @throws SQLException if connection failed
     */
    public void makeProjectTable(ResultSet results) throws SQLException {
        System.out.println("\nProject details:\n");

        System.out.format("--------------------------------------------------------------------------------------------" +
                "----------------------------------------------------------------------------------------------" +
                "----------------------------------------%n");
        System.out.printf("| %-4s | %-20s | %-13s | %-13s | %-10s | %-10s | %-20s | %-7s | %-13s | %-10s | %-17s | " +
                        "%-13s | %-10s | %-10s | %-10s | %n", "No", "Name", "Fee", "Paid", "Deadline", "Building", "Address",
                "ERF NO", "Finalised", "F-Date", "Customer", "Proj-manager", "Architect", "Engineer", "Contractor");
        System.out.format("--------------------------------------------------------------------------------------------" +
                "----------------------------------------------------------------------------------------------" +
                "----------------------------------------%n");

        // Loop over the results, printing them all.
        while (results.next()) {
            System.out.printf("| %-4d | %-20s | %-13.2f | %-13.2f | %-10s | %-10s | %-20s | %-7s | %-13s | %-10s | %-17s | " +
                            "%-13s | %-10s | %-10s | %-10s | %n", results.getInt("project_no"),
                    results.getString("project_name"), results.getFloat("project_Fee"),
                    results.getFloat("amount_paid"), results.getString("deadline"),
                    results.getString("building_type"), results.getString("physical_address"),
                    results.getString("erf_number"), results.getString("finalised"),
                    results.getString("finalised_date"), results.getString("customer"),
                    results.getString("project_manager"), results.getString("architect"),
                    results.getString("structural_engineer"), results.getString("contractor"));
        }
        System.out.format("--------------------------------------------------------------------------------------------" +
                "----------------------------------------------------------------------------------------------" +
                "----------------------------------------%n");
    }

    /**
     * This method creates a table and will be called by method personnelTableSearch
     *
     * @param results uses ResultSet to create and display table
     * @throws SQLException if connection failed
     */
    public void makePersonnelTable(ResultSet results) throws SQLException {

        System.out.format("---------------------------------------------------------------------------------" +
                "---------------------------------%n");
        System.out.printf("| %-5s | %-20s | %-15s | %-33s | %-25s |%n", "No", "Name", "Telephone", "Email", "Address");
        System.out.format("----------------------------------------------------------------------------------" +
                "--------------------------------%n");

        // Loop over the results, printing them all.
        while (results.next()) {
            System.out.printf("| %-5d | %-20s | %-15s | %-33s | %-25s |%n", results.getInt("id"),
                    results.getString("name"), results.getString("tel_no"),
                    results.getString("email"), results.getString("address"));
        }
        System.out.format("---------------------------------------------------------------------------------" +
                "---------------------------------%n");

    }

    /**
     * This method allows the user to search for a project by project number and name
     *
     * @throws SQLException if connection failed
     */
    public void searchProjects() throws SQLException {
        Connection connection = serverConnection();
        Statement statement = connection.createStatement();
        ResultSet results;

        int searchOption = 0;
        while (searchOption != 3) {
            System.out.println("""
                    Search by project number or project name
                    1. Search by project number
                    2. Search by project name
                    3. Cancel search""");

            System.out.print("Choice: ");
            searchOption = input.nextInt();
            int searchNo;
            String searchName;

            switch (searchOption) {
                case 1 -> {
                    System.out.print("Project number: ");
                    searchNo = input.nextInt();
                    results = statement.executeQuery(
                            "SELECT project_no, project_name, project_Fee, amount_paid, deadline, " +
                                    "building_type, physical_address, erf_number, finalised, " +
                                    "finalised_date, customer, project_manager, architect, " +
                                    "structural_engineer, contractor From project_details " +
                                    "WHERE project_no = " + searchNo + "");
                    makeProjectTable(results);
                }
                case 2 -> {
                    System.out.println("Project Name: ");
                    searchName = input.nextLine();
                    results = statement.executeQuery(
                            "SELECT project_no, project_name, project_Fee, amount_paid, deadline, " +
                                    "building_type, physical_address, erf_number, finalised, " +
                                    "finalised_date, customer, project_manager, architect, " +
                                    "structural_engineer, contractor From project_details " +
                                    "WHERE project_name = " + searchName + "");
                    makeProjectTable(results);
                }
                case 3 -> System.out.println("Returning to main menu");
                default -> System.out.println("Please choose a valid option");
            }

        }
    }

    /**
     * This method updates personnel details. It will be called by method 'updatePersonnel'
     *
     * @param connection uses a connection object as parameter
     * @param table      uses a string as parameter. This string will specify which table to look up
     * @throws SQLException if connection failed
     */
    public void updateDetails(Connection connection, String table) throws SQLException {

        Scanner input = new Scanner(System.in);
        System.out.println("Select the person who's details to update by entering their id number");
        System.out.print("\nEnter id: ");

        int id = input.nextInt();
        int option = 0;
        String columns;

        while (option != 5) {
            System.out.println("""
                    \nChose from below list which details to update:\s
                    1. Name
                    2. Tell
                    3. Email
                    4. Address
                    5. Return to previous menu""");

            System.out.print("\nChoice: ");
            option = input.nextInt();
            input.nextLine();

            switch (option) {
                case 1 -> {
                    columns = "name";
                    System.out.print("Enter new details: ");
                    String details = input.nextLine();
                    // updating specified column in specified table by id number with variable 'details'
                    PreparedStatement updateFinalised = connection.prepareStatement(
                            "UPDATE " + table + " SET " + columns + " = ? WHERE id = ?");

                    updateFinalised.setString(1, details);
                    updateFinalised.setInt(2, id);
                    updateFinalised.executeUpdate();
                }
                case 2 -> {
                    columns = "tel_no";
                    System.out.print("Enter new details: ");
                    String details = input.nextLine();
                    // updating specified column in specified table by id number with variable 'details'
                    PreparedStatement updateFinalised = connection.prepareStatement(
                            "UPDATE " + table + " SET " + columns + " = ? WHERE id = ?");

                    updateFinalised.setString(1, details);
                    updateFinalised.setInt(2, id);
                    updateFinalised.executeUpdate();
                }
                case 3 -> {
                    columns = "email";
                    System.out.print("Enter new details: ");
                    String details = input.nextLine();
                    // updating specified column in specified table by id number with variable 'details'
                    PreparedStatement updateFinalised = connection.prepareStatement(
                            "UPDATE " + table + " SET " + columns + " = ? WHERE id = ?");

                    updateFinalised.setString(1, details);
                    updateFinalised.setInt(2, id);
                    updateFinalised.executeUpdate();
                }
                case 4 -> {
                    columns = "address";
                    System.out.print("Enter new details: ");
                    String details = input.nextLine();
                    // updating specified column in specified table by id number with variable 'details'
                    PreparedStatement updateFinalised = connection.prepareStatement(
                            "UPDATE " + table + " SET " + columns + " = ? WHERE id = ?");

                    updateFinalised.setString(1, details);
                    updateFinalised.setInt(2, id);
                    updateFinalised.executeUpdate();
                }
                case 5 -> System.out.println("Returning to previous menu");
                default -> System.out.println("Please choose a valid options");
            }
        }
    }

    /**
     * This method will be used to display a selected table. it will be called by 'viewPersonnel' and 'updatePersonnel'
     *
     * @param table uses a string as parameter. The string will specify which table to look up
     * @throws SQLException if connection failed
     */
    public void personnelTableSearch(String table) throws SQLException {
        Connection connection = serverConnection();
        Statement statement = connection.createStatement();
        ResultSet results;

        results = statement.executeQuery("SELECT id, name, tel_no, email, address FROM " + table + "");

        // calling method makePersonnelTable
        makePersonnelTable(results);
    }

    /**
     * This method allows a user to view selected personnel details. It calls method 'personnelTableSearch'
     *
     * @throws SQLException if connection failed
     */
    public void viewPersonnel() throws SQLException {

        int choice = 0;
        String table;

        while (choice != 6) {
            System.out.println("""
                    \nChose from below list:\s
                    1. Customer
                    2. Project manager
                    3. Architect
                    4. Structural engineer
                    5. Contractor
                    6. Return to main menu""");

            System.out.print("\nChoice: ");
            choice = input.nextInt();

            switch (choice) {
                case 1 -> {
                    table = "customer";
                    System.out.println("\nCustomer details");
                    personnelTableSearch(table);
                }
                case 2 -> {
                    table = "project_manager";
                    System.out.println("\nProject manager details");
                    personnelTableSearch(table);
                }
                case 3 -> {
                    table = "architect";
                    System.out.println("\nArchitect details");
                    personnelTableSearch(table);
                }
                case 4 -> {
                    table = "structural_engineer";
                    System.out.println("\nStructural engineer details");
                    personnelTableSearch(table);
                }
                case 5 -> {
                    table = "contractor";
                    System.out.println("\nContractor details");
                    personnelTableSearch(table);
                }
                case 6 -> System.out.println("Returning to main menu");
                default -> System.out.println("Please choose a valid options");
            }
        }
    }

    /**
     * This method updates personnel details. It calls on two methods to perform the updates. 'personnelTableSearch' and
     * 'updateDetails'.
     *
     * @throws SQLException if connection failed
     */
    public void updatePersonnel() throws SQLException {
        Connection connection = serverConnection();

        int choice = 0;
        String table;

        while (choice != 6) {
            System.out.println("""
                    Chose from below list:\s
                    1. Customer
                    2. Project manager
                    3. Architect
                    4. Structural engineer
                    5. Contractor
                    6. exit""");

            System.out.print("\nChoice: ");
            choice = input.nextInt();
            switch (choice) {
                case 1 -> {
                    table = "customer";
                    personnelTableSearch(table);
                    updateDetails(connection, table);
                }
                case 2 -> {
                    table = "project_manager";
                    personnelTableSearch(table);
                    updateDetails(connection, table);
                }
                case 3 -> {
                    table = "architect";
                    personnelTableSearch(table);
                    updateDetails(connection, table);
                }
                case 4 -> {
                    table = "structural_engineer";
                    personnelTableSearch(table);
                    updateDetails(connection, table);
                }
                case 5 -> {
                    table = "contractor";
                    personnelTableSearch(table);
                    updateDetails(connection, table);
                }
                case 6 -> System.out.println("Returning to main menu");
                default -> System.out.println("Please choose a valid options");
            }
        }
    }

    /**
     * This method displays a mini version of all projects
     *
     * @param connection uses a connection object as parameter
     * @throws SQLException if connection failed
     */
    public void projectMiniView(Connection connection) throws SQLException {
        Statement statement = connection.createStatement();
        ResultSet results1;

        results1 = statement.executeQuery("SELECT project_no, project_name, project_fee, amount_paid, deadline FROM project_details");

        System.out.println("\nProject details\n");
        System.out.format("-------------------------------------------------------------------------------------------%n");
        System.out.printf("| %-5s | %-20s | %-20s | %-20s | %-10s |%n", "No", "Project Name", "Project Fee", "Amount Paid", "Deadline");
        System.out.format("-------------------------------------------------------------------------------------------%n");

        // Loop over the results, printing them all.
        while (results1.next()) {
            System.out.printf("| %-5d | %-20s | %-20.2f | %-20.2f | %-10s |%n",
                    results1.getInt("project_no"), results1.getString("project_name"),
                    results1.getFloat("project_fee"), results1.getFloat("amount_paid"),
                    results1.getString("deadline"));
        }
        System.out.format("-------------------------------------------------------------------------------------------%n");
    }

    /**
     * This method generates an invoice
     *
     * @param connection uses a connection object as parameter
     * @param projectNo  uses an int that indicates the project number
     * @throws SQLException if connection failed
     */
    public void createInvoice(Connection connection, int projectNo) throws SQLException {
        Statement statement = connection.createStatement();
        ResultSet results;

        PreparedStatement invStatement = connection.prepareStatement(
                "SELECT * FROM project_details WHERE project_no = ?");

        invStatement.setInt(1, projectNo);
        results = invStatement.executeQuery();

        // assigning values from database to variables projectFee, amountPaid and customer
        // values retrieved below in the body of the if statement
        // and calculating the amount to be paid
        float projectFee;
        float amountPaid;
        float toBePaid = 0;
        String customer = "";

        if (results.next()) {
            projectFee = results.getFloat(3);
            amountPaid = results.getFloat(4);
            toBePaid = projectFee - amountPaid;
            customer = results.getString(11);
        }

        // Creating the invoice
        results = statement.executeQuery(
                "SELECT name, tel_no, email, address FROM customer WHERE name = '" + customer + "' ");

        System.out.println("\nInvoice details for " + customer);
        // Loop over the results, printing them all.
        while (results.next()) {
            System.out.println("Name\t\t:" + results.getString("name") + "\n" +
                    "Telephone\t:" + results.getString("tel_no") + "\n" +
                    "Email\t\t:" + results.getString("email") + "\n" +
                    "Address\t\t:" + results.getString("address"));
        }
        System.out.println("An outstanding amount of R" + toBePaid + " must still be paid");
    }

    /**
     * This method finalises a project
     *
     * @throws SQLException if connection failed
     */
    public void finalise() throws SQLException {
        Connection connection = serverConnection();
        ProgramFeatures features = new ProgramFeatures();
        String myDateString = myDate.toString();

        // calling method to display projects that has not been finalised
        notFinalisedProjects(connection);

        // updating project to finalised
        System.out.println("Choose project by project no to finalise the project");
        System.out.print("Choice: ");
        int projectNo = input.nextInt();
        PreparedStatement updateFinalised = connection.prepareStatement(
                "UPDATE project_details SET finalised = 'finalised' WHERE project_no = ?");

        updateFinalised.setInt(1, projectNo);
        updateFinalised.executeUpdate();

        // inserting the date
        PreparedStatement insertFinalisedDate = connection.prepareStatement(
                "UPDATE project_details SET finalised_date = ? WHERE project_no = ?");

        insertFinalisedDate.setString(1, myDateString);
        insertFinalisedDate.setInt(2, projectNo);
        insertFinalisedDate.executeUpdate();
        System.out.println("project has been marked as finalised");
        features.createInvoice(connection, projectNo);
    }

    /**
     * This method allows the user to update the project fee, amount paid and deadline
     *
     * @param connection uses a connection object as parameter
     * @throws SQLException if connection failed
     */
    public void updateProjectDetails(Connection connection) throws SQLException {
        ProgramFeatures features = new ProgramFeatures();

        // calling method projectMiniView to display all projects
        features.projectMiniView(connection);

        // selecting project number
        System.out.print("\nChoice: ");
        int project_no = input.nextInt();

        int updateChoice = 0;

        while (updateChoice != 4) {

            // requesting user to select from below options to update
            System.out.println("""
                    What would you like to update, Please choose from below list.
                    1. Project fee
                    2. Amount paid
                    3. Deadline
                    4. Exit""");

            String columns;

            System.out.print("\nChoice: ");
            updateChoice = input.nextInt();

            // executing updates from users choice
            switch (updateChoice) {
                case 1 -> {
                    columns = "project_fee";
                    System.out.print("Enter new project fee: ");
                    float newDetails = input.nextFloat();
                    PreparedStatement update = connection.prepareStatement(
                            "UPDATE project_details SET " + columns + " =? WHERE project_no = ?");

                    update.setFloat(1, newDetails);
                    update.setInt(2, project_no);
                    update.executeUpdate();
                    System.out.println("Project fee updated");
                }
                case 2 -> {
                    columns = "amount_paid";
                    System.out.print("Enter new amount paid: ");
                    float newDetails = input.nextFloat();
                    PreparedStatement update = connection.prepareStatement(
                            "UPDATE project_details SET " + columns + " =? WHERE project_no = ?");

                    update.setFloat(1, newDetails);
                    update.setInt(2, project_no);
                    update.executeUpdate();
                    System.out.println("Amount paid updated");
                }
                case 3 -> {
                    input.nextLine();
                    columns = "deadline";
                    System.out.print("Enter new deadline: ");
                    String newDetails = input.nextLine();
                    PreparedStatement update = connection.prepareStatement(
                            "UPDATE project_details SET " + columns + " =? WHERE project_no = ?");

                    update.setString(1, newDetails);
                    update.setInt(2, project_no);
                    update.executeUpdate();
                    System.out.println("Deadline updated");
                }
                case 4 -> System.out.println("exit");
                default -> System.out.println("Please enter a valid option");
            }
        }

    }

    /**
     * This method displays a list of id's and names from selected table and allows the user to choose a name by id,
     * the method returns the selected name
     * This method is used to assign the select name to variables that will be used to create the project details
     *
     * @param connection uses a connection object as parameter
     * @param table      uses a string with the value of the table name as parameter
     * @return a string with the value of selected personnel name
     * @throws SQLException if connection failed
     */
    public String assignPersonnel(Connection connection, String table) throws SQLException {
        Statement statement = connection.createStatement();
        ResultSet results1;

        results1 = statement.executeQuery("SELECT id, name, tel_no, email, address FROM " + table + "");

        // calling makeProjectTable
        makePersonnelTable(results1);

        // requesting user to choose an id from above printed results
        System.out.print("\nchoice: ");
        int Id = input.nextInt();
        ResultSet results2;
        results2 = statement.executeQuery("SELECT name from " + table + " WHERE id = '" + Id + "'");
        String name = "";

        // Loop over the results, printing them all.
        while (results2.next()) {
            name = results2.getString("name");
            System.out.println(name + "has been assigned");
        }
        return name;

    }

    /**
     * This method views all projects and calls the makeFullProjectLayout method
     *
     * @param connection uses a connection object as parameter
     * @throws SQLException if connection failed
     */
    public void viewAllProjects(Connection connection) throws SQLException {
        Statement statement = connection.createStatement();
        ResultSet results;

        results = statement.executeQuery(
                "SELECT project_no, project_name, project_Fee, amount_paid, deadline, " +
                        "building_type, physical_address, erf_number, finalised, " +
                        "finalised_date, customer, project_manager, architect, " +
                        "structural_engineer, contractor From project_details");

        // calling makeProjectTable method
        makeProjectTable(results);
    }

    /**
     * This method displays all projects that's not finalised
     *
     * @param connection uses a connection object as parameter
     * @throws SQLException if connection failed
     */
    public void notFinalisedProjects(Connection connection) throws SQLException {
        Statement statement = connection.createStatement();
        ResultSet results;

        results = statement.executeQuery(
                "SELECT project_no, project_name, project_Fee, amount_paid, deadline, " +
                        "building_type, physical_address, erf_number, finalised, " +
                        "finalised_date, customer, project_manager, architect, " +
                        "structural_engineer, contractor From project_details WHERE finalised = 'not finalised'");

        // calling makeProjectTable method
        makeProjectTable(results);
    }

    /**
     * This method displays projects that have been finalised
     *
     * @param connection uses a connection as parameter
     * @throws SQLException if connection failed
     */
    public void finalisedProjects(Connection connection) throws SQLException {
        Statement statement = connection.createStatement();
        ResultSet results;

        results = statement.executeQuery(
                "SELECT project_no, project_name, project_Fee, amount_paid, deadline, " +
                        "building_type, physical_address, erf_number, finalised, " +
                        "finalised_date, customer, project_manager, architect, " +
                        "structural_engineer, contractor From project_details WHERE finalised = 'finalised'");

        // calling makeProjectTable method
        makeProjectTable(results);

    }

    /**
     * This method displays projects that's overdue
     *
     * @param connection uses a connection as parameter
     * @throws SQLException if connection failed
     */
    public void overDueProjects(Connection connection) throws SQLException {
        Statement statement = connection.createStatement();
        ResultSet results;

        results = statement.executeQuery(
                "SELECT project_no, project_name, project_Fee, amount_paid, deadline, " +
                        "building_type, physical_address, erf_number, finalised, " +
                        "finalised_date, customer, project_manager, architect, " +
                        "structural_engineer, contractor From project_details WHERE deadline < CURRENT_DATE() AND finalised = 'not finalised'");

        // calling makeProjectTable method
        makeProjectTable(results);
    }

}

