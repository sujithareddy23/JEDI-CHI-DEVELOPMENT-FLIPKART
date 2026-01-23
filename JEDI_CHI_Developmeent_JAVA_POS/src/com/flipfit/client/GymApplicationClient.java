package com.flipfit.client;

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
            
            mainChoice = in.nextInt();

            if (mainChoice == 1) {
                loginMenu(in);
            } else if (mainChoice == 2) {
                CustomerClient customer = new CustomerClient();
                customer.customerRegistration(in); 
            } else if (mainChoice == 3) {
                GymOwnerClient gymOwner = new GymOwnerClient();
                System.out.print("Enter your email to register: ");
                String email = in.next();
                gymOwner.registerGymOwner(in, email);
            } else if (mainChoice == 4) {
                System.out.println("Redirecting to Change Password...");
            } else if (mainChoice == 5) {
                System.out.println("Exiting Application. Thank you!");
            } else {
                System.out.println("Invalid selection. Please try again.");
            }

        } while (mainChoice != 5);
        in.close();
    }

    private static void loginMenu(Scanner in) {
        System.out.println("\n--- Login ---");
        System.out.print("Username: ");
        String username = in.next();
        System.out.print("Password: ");
        String password = in.next();

        System.out.println("Select Role: 1. GymAdmin 2. GymCustomer 3. GymOwner");
        int roleChoice = in.nextInt();

        switch (roleChoice) {
            case 1:
                System.out.println("Welcome Admin: " + username);
                AdminClient admin = new AdminClient(); 
                admin.AdminPage(in);
                break;
            case 2:
                System.out.println("Welcome Customer: " + username);
                CustomerClient customer = new CustomerClient();
                customer.customerMenu(in);
                break;
            case 3:
                System.out.println("Welcome Owner: " + username);
                GymOwnerClient gymOwner = new GymOwnerClient();
                System.out.print("Enter email to access your profile: ");
                String ownerEmail = in.next();
                gymOwner.gymOwnerPage(in, ownerEmail);
                break;
            default:
                System.out.println("Invalid Role Selection.");
        }
    }
}