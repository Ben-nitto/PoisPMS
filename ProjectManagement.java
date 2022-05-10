import java.sql.Connection;
import java.sql.SQLException;
import java.util.Scanner;

// This program consists out of:
// 1. The main class, the main menu loop where all the methods will be called.
// 2. ProgramFeatures class - this class contains the methods that are being called in the main class.
// 3. Project class and the persons class for object creation

public class ProjectManagement {

    public static void main(String[] args) {

        try {
            ProgramFeatures features = new ProgramFeatures();
            Project project = new Project();
            Persons persons = new Persons();
            Scanner input = new Scanner(System.in);

            // Connection object to connect to the PoisPMS database, via the jdbc:mysql: channel on localhost (this PC)
            Connection myConnection = features.serverConnection();

            // Displaying welcome message
            System.out.println("Welcome to Poised Project Management System: ");
            System.out.println("Please choose from below options to create a new project or to update project details");

            // menu options
            int menuOptions = 0;
            while (menuOptions != 12) {

                // Displaying menu options
                System.out.println("""
                                            
                        Please choose from below option (enter 1-3), enter 3 to exit:\s
                        1. Capture personnel details\s
                        2. Capture project details\s
                        3. Update personnel details\s
                        4. Update project details\s
                        5. Finalise a project\s
                        6. View projects that's not finalised\s
                        7. View finalised projects\s
                        8. View overdue projects\s
                        9. View personnel\s
                        10. View all projects\s
                        11. Search project\s
                        12. Exit program
                        """);

                System.out.print("Enter options here: ");
                menuOptions = Integer.parseInt(input.nextLine());

                // Setting variables for project info
                switch (menuOptions) {
                    case 1 -> persons.capturePersonnelDetails();

                    case 2 -> project.captureProject(myConnection);

                    case 3 -> features.updatePersonnel();

                    case 4 -> features.updateProjectDetails(myConnection);

                    case 5 -> features.finalise();

                    case 6 -> features.notFinalisedProjects(myConnection);

                    case 7 -> features.finalisedProjects(myConnection);

                    case 8 -> features.overDueProjects(myConnection);

                    case 9 -> features.viewPersonnel();

                    case 10 -> features.viewAllProjects(myConnection);

                    case 11 -> features.searchProjects();

                    case 12 -> System.out.println("Goodbye!!!");

                    default -> System.out.println("Please select a valid menu options");
                }
            }
            // Closing connection
            myConnection.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}