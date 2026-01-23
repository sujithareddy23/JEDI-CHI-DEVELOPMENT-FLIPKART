package com.flipfit.client;

import java.util.Scanner;

public class CustomerClient {
    public void customerRegistration(Scanner in) {
        System.out.println("\n--- Customer Registration ---");
        System.out.print("Enter Name: ");
        String name = in.next();
        System.out.println("Registration successful for " + name);
    }

    public void customerMenu(Scanner in) {
        System.out.println("\n--- Customer Menu ---");
        System.out.println("1. View Gyms");
        System.out.println("2. Book Slot");
        System.out.println("3. Logout");
    }

	public void customerRegistration1(Scanner in) {
		// TODO Auto-generated method stub
		
	}
}