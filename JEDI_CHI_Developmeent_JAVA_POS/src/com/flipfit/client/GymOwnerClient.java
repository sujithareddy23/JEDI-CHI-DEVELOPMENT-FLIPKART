package com.flipfit.client;

import java.util.Scanner;

public class GymOwnerClient {
    public void registerGymOwner(Scanner in, String email) {
        System.out.println("\n--- Gym Owner Registration ---");
        System.out.println("Registering owner with email: " + email);
        System.out.println("Registration request sent to Admin.");
    }

    public void gymOwnerPage(Scanner in, String email) {
        System.out.println("\n--- Gym Owner Dashboard ---");
        System.out.println("Logged in as: " + email);
        System.out.println("1. Add Gym Center");
        System.out.println("2. View Slots");
        System.out.println("3. Logout");
    }

	
}