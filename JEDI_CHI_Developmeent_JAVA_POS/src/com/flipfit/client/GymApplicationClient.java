package com.flipfit.client;

import com.flipfit.business.UserServiceImpl;
import com.flipfit.business.UserServiceInterface;
import com.flipfit.constants.RoleConstants;
import com.flipfit.exception.InvalidCredentialsException;
import com.flipfit.validation.InputValidation;
import com.flipfit.validation.ValidationResult;

import java.util.Scanner;

public class GymApplicationClient {

    public static void main(String[] args) {
        mainPage();
    }

    public static void mainPage() {
        Scanner in = new Scanner(System.in);
        int mainChoice;

        do {
            System.out.println("\n--- Welcome to the Flipfit Application for GYM ---");
            System.out.println("1. Login");
            System.out.println("2. Registration of the GymCustomer");
            System.out.println("3. Registration of the GymOwner");
            System.out.println("4. Change Password");
            System.out.println("5. Exit");
            System.out.print("Select an option: ");

            mainChoice = readInt(in);
            if (mainChoice == -1) {
                consumeLine(in);
                System.out.println("Invalid input.");
                continue;
            }
            consumeLine(in);

            switch (mainChoice) {
                case 1:
                    loginMenu(in);
                    break;
                case 2: {
                    CustomerClient customer = new CustomerClient();
                    customer.customerRegistration(in);
                    break;
                }
                case 3: {
                    GymOwnerClient gymOwner = new GymOwnerClient();
                    System.out.print("Enter your email to register: ");
                    String email = in.nextLine().trim();
                    ValidationResult er = InputValidation.validateEmail(email);
                    if (!er.isValid()) {
                        System.out.println(er.getFirstError());
                        break;
                    }
                    gymOwner.registerGymOwner(in, email);
                    break;
                }
                case 4:
                    System.out.println("Change Password – Coming soon.");
                    break;
                case 5:
                    System.out.println("Exiting Application. Thank you!");
                    return;
                default:
                    System.out.println("Invalid selection. Please try again.");
            }
        } while (true);
    }

    private static void loginMenu(Scanner in) {
        System.out.println("\n--- Login ---");
        System.out.print("Username: ");
        String username = in.nextLine().trim();
        System.out.print("Password: ");
        String password = in.nextLine().trim();
        ValidationResult ur = InputValidation.isNotBlank(username, "Username");
        if (!ur.isValid()) { System.out.println(ur.getFirstError()); return; }
        ValidationResult pr = InputValidation.isNotBlank(password, "Password");
        if (!pr.isValid()) { System.out.println(pr.getFirstError()); return; }
        System.out.println("Select Role: 1. GymAdmin 2. GymCustomer 3. GymOwner");
        int roleChoice = readInt(in);
        consumeLine(in);
        ValidationResult rr = InputValidation.validateRoleChoice(roleChoice);
        if (!rr.isValid()) { System.out.println(rr.getFirstError()); return; }

        try {
            UserServiceInterface userService = new UserServiceImpl();
            String role = userService.authenticate(username, password);
            String userId = userService.getLoggedInUserId();

            boolean roleMatches = (roleChoice == 1 && RoleConstants.ADMIN.equals(role))
                    || (roleChoice == 2 && RoleConstants.CUSTOMER.equals(role))
                    || (roleChoice == 3 && RoleConstants.OWNER.equals(role));
            if (!roleMatches) {
                throw new InvalidCredentialsException("Credentials do not match selected role.");
            }

            switch (role) {
            case RoleConstants.ADMIN:
                System.out.println("Welcome Admin: " + userId);
                AdminClient admin = new AdminClient();
                admin.AdminPage(in);
                break;
            case RoleConstants.CUSTOMER:
                System.out.println("Welcome Customer: " + userId);
                CustomerClient customer = new CustomerClient();
                customer.customerMenu(in, userId);
                break;
            case RoleConstants.OWNER:
                System.out.println("Welcome Owner: " + userId);
                GymOwnerClient gymOwner = new GymOwnerClient();
                gymOwner.gymOwnerPage(in, userId);
                break;
            default:
                System.out.println("Unknown role.");
            }
        } catch (InvalidCredentialsException e) {
            System.out.println(e.getMessage());
        }
    }

    private static void consumeLine(Scanner in) {
        if (in.hasNextLine()) in.nextLine();
    }

    private static int readInt(Scanner in) {
        try {
            return in.nextInt();
        } catch (Exception e) {
            return -1;
        }
    }
}
