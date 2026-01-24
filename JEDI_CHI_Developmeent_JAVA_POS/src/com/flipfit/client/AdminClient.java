package com.flipfit.client;

import com.flipfit.bean.GymCenter;
import com.flipfit.bean.GymOwner;
import com.flipfit.business.GymAdminImpl;
import com.flipfit.business.GymAdminInterface;
import com.flipfit.validation.GymCenterValidation;
import com.flipfit.validation.ValidationResult;

import java.util.List;
import java.util.Scanner;

public class AdminClient {
    private final GymAdminInterface adminService = new GymAdminImpl();

    public void AdminPage(Scanner in) {
        int choice;
        do {
            System.out.println("\n--- Admin Menu ---");
            System.out.println("1. List pending gym centers");
            System.out.println("2. List verified gym centers");
            System.out.println("3. List pending partners (owners)");
            System.out.println("4. List verified partners (owners)");
            System.out.println("5. Validate gym (approve)");
            System.out.println("6. Withdraw permission (gym)");
            System.out.println("7. Search gyms by location");
            System.out.println("0. Logout");
            System.out.print("Choice: ");
            choice = readInt(in);
            if (choice == -1) {
                if (in.hasNextLine()) in.nextLine();
                System.out.println("Invalid input.");
                continue;
            }
            if (in.hasNextLine()) in.nextLine();

            switch (choice) {
                case 1:
                    listPendingCenters();
                    break;
                case 2:
                    listVerifiedCenters();
                    break;
                case 3:
                    listPendingPartners();
                    break;
                case 4:
                    listVerifiedPartners();
                    break;
                case 5:
                    validateGym(in);
                    break;
                case 6:
                    withdrawPermission(in);
                    break;
                case 7:
                    searchGyms(in);
                    break;
                case 0:
                    System.out.println("Logged out.");
                    return;
                default:
                    System.out.println("Invalid choice.");
            }
        } while (choice != 0);
    }

    private void listPendingCenters() {
        List<GymCenter> list = adminService.listPendingGymCenters();
        if (list.isEmpty()) {
            System.out.println("No pending gym centers.");
            return;
        }
        System.out.println("\nPending gym centers:");
        for (GymCenter g : list) {
            System.out.println("  " + g.getGymId() + " | " + g.getName() + " | " + g.getLocation());
        }
    }

    private void listVerifiedCenters() {
        List<GymCenter> list = adminService.listVerifiedGymCenters();
        if (list.isEmpty()) {
            System.out.println("No verified gym centers.");
            return;
        }
        System.out.println("\nVerified gym centers:");
        for (GymCenter g : list) {
            System.out.println("  " + g.getGymId() + " | " + g.getName() + " | " + g.getLocation());
        }
    }

    private void listPendingPartners() {
        List<GymOwner> list = adminService.listPendingPartners();
        if (list.isEmpty()) {
            System.out.println("No pending partners.");
            return;
        }
        System.out.println("\nPending partners:");
        for (GymOwner o : list) {
            System.out.println("  " + o.getEmailId() + " | " + o.getOwnerName());
        }
    }

    private void listVerifiedPartners() {
        List<GymOwner> list = adminService.listVerifiedPartners();
        if (list.isEmpty()) {
            System.out.println("No verified partners.");
            return;
        }
        System.out.println("\nVerified partners:");
        for (GymOwner o : list) {
            System.out.println("  " + o.getEmailId() + " | " + o.getOwnerName());
        }
    }

    private void validateGym(Scanner in) {
        System.out.print("Gym ID to validate: ");
        String gymId = in.next().trim();
        ValidationResult vr = GymCenterValidation.validateGymId(gymId);
        if (!vr.isValid()) { System.out.println(vr.getFirstError()); return; }
        adminService.validateGym(gymId);
        System.out.println("Gym " + gymId + " validated.");
    }

    private void withdrawPermission(Scanner in) {
        System.out.print("Gym ID to withdraw permission: ");
        String gymId = in.next().trim();
        ValidationResult vr = GymCenterValidation.validateGymId(gymId);
        if (!vr.isValid()) { System.out.println(vr.getFirstError()); return; }
        adminService.withdrawPermission(gymId);
        System.out.println("Permission withdrawn for " + gymId + ".");
    }

    private void searchGyms(Scanner in) {
        System.out.print("Location (e.g. Bangalore, Bellandur): ");
        String loc = in.nextLine().trim();
        List<GymCenter> list = adminService.searchGymsByLocation(loc);
        if (list.isEmpty()) {
            System.out.println("No gyms found.");
            return;
        }
        System.out.println("\nGyms:");
        for (GymCenter g : list) {
            System.out.println("  " + g.getGymId() + " | " + g.getName() + " | " + g.getLocation()
                + " | Validated: " + g.isValidated());
        }
    }

    private static int readInt(Scanner in) {
        try {
            return in.nextInt();
        } catch (Exception e) {
            return -1;
        }
    }
}
