package com.flipfit.client;

import com.flipfit.bean.GymCenter;
import com.flipfit.bean.GymOwner;
import com.flipfit.bean.Slot;
import com.flipfit.business.GymOwnerImpl;
import com.flipfit.business.GymOwnerInterface;
import com.flipfit.data.DataStore;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class GymOwnerClient {
    private final GymOwnerInterface ownerService = new GymOwnerImpl();

    public void registerGymOwner(Scanner in, String email) {
        System.out.println("\n--- Gym Owner Registration ---");
        System.out.print("Owner name: ");
        String name = in.nextLine().trim();
        System.out.print("Password: ");
        String password = in.next().trim();
        System.out.print("PAN: ");
        String pan = in.next().trim();
        System.out.print("GST (optional): ");
        String gst = in.next().trim();

        GymOwner o = new GymOwner();
        o.setOwnerName(name);
        o.setEmailId(email);
        o.setPassword(password);
        o.setPanNo(pan);
        o.setGstNo(gst);
        if (ownerService.registerOwner(o)) {
            System.out.println("Registration request sent. Awaiting Admin validation.");
        } else {
            System.out.println("Email already registered.");
        }
    }

    public void gymOwnerPage(Scanner in, String ownerEmail) {
        GymOwner o = DataStore.getOwners().get(ownerEmail);
        if (o == null) {
            System.out.println("Owner not found.");
            return;
        }
        String ownerId = o.getId();
        int choice;
        do {
            System.out.println("\n--- Gym Owner Dashboard ---");
            System.out.println("1. My centers");
            System.out.println("2. Register new gym");
            System.out.println("3. Update slots (simplified)");
            System.out.println("4. Modify profile");
            System.out.println("0. Logout");
            System.out.print("Choice: ");
            choice = readInt(in);
            in.nextLine();

            switch (choice) {
                case 1:
                    listMyCenters(ownerId);
                    break;
                case 2:
                    registerGym(in, ownerId);
                    break;
                case 3:
                    updateSlots(in);
                    break;
                case 4:
                    modifyProfile(in, ownerEmail);
                    break;
                case 0:
                    System.out.println("Logged out.");
                    return;
                default:
                    System.out.println("Invalid choice.");
            }
        } while (choice != 0);
    }

    private void listMyCenters(String ownerId) {
        List<GymCenter> list = ownerService.getCentersByOwner(ownerId);
        if (list.isEmpty()) {
            System.out.println("No centers registered.");
            return;
        }
        System.out.println("\nYour centers:");
        for (GymCenter g : list) {
            System.out.println("  " + g.getGymId() + " | " + g.getName() + " | " + g.getLocation()
                + " | Validated: " + g.isValidated());
        }
    }

    private void registerGym(Scanner in, String ownerId) {
        System.out.print("Gym ID (e.g. NEW1): ");
        String gymId = in.next().trim();
        in.nextLine();
        System.out.print("Name: ");
        String name = in.nextLine().trim();
        System.out.print("Location (e.g. Bangalore - XYZ): ");
        String loc = in.nextLine().trim();
        System.out.print("Contact: ");
        String contact = in.next().trim();

        GymCenter g = new GymCenter();
        g.setGymId(gymId);
        g.setName(name);
        g.setLocation(loc);
        g.setContactNo(contact);
        g.setOwnerId(ownerId);
        List<Slot> slots = createDefaultSlots(gymId, 5);
        g.setSlotList(slots);
        ownerService.registerGym(g);
        System.out.println("Gym registered. Pending Admin validation.");
    }

    private List<Slot> createDefaultSlots(String gymId, int capacity) {
        List<Slot> list = new ArrayList<>();
        for (int h : new int[] { 6, 7, 8, 18, 19, 20 }) {
            Slot s = new Slot();
            s.setSlotId(gymId + "_" + String.format("%02d", h) + ":00");
            s.setGymId(gymId);
            s.setStartTime(LocalTime.of(h, 0));
            s.setEndTime(LocalTime.of(h + 1, 0));
            s.setTotalCapacity(capacity);
            list.add(s);
        }
        return list;
    }

    private void updateSlots(Scanner in) {
        System.out.print("Gym ID: ");
        String gymId = in.next().trim();
        GymCenter g = DataStore.getCenters().get(gymId);
        if (g == null) {
            System.out.println("Gym not found.");
            return;
        }
        List<Slot> slots = g.getSlotList();
        if (slots == null) slots = new ArrayList<>();
        System.out.println("Current slots: " + slots.size() + ". Update capacity? (y/n)");
        String c = in.next().trim();
        if (!c.equalsIgnoreCase("y")) return;
        System.out.print("New capacity per slot: ");
        int cap = readInt(in);
        in.nextLine();
        if (cap <= 0) return;
        for (Slot s : slots) s.setTotalCapacity(cap);
        ownerService.updateSlots(gymId, slots);
        System.out.println("Slots updated.");
    }

    private void modifyProfile(Scanner in, String ownerEmail) {
        System.out.print("New name (blank to skip): ");
        String name = in.nextLine().trim();
        System.out.print("New password (blank to skip): ");
        String pwd = in.nextLine().trim();
        GymOwner o = new GymOwner();
        o.setEmailId(ownerEmail);
        if (!name.isEmpty()) o.setOwnerName(name);
        if (!pwd.isEmpty()) o.setPassword(pwd);
        ownerService.modifyOwnerProfile(o);
        System.out.println("Profile updated.");
    }

    private static int readInt(Scanner in) {
        try {
            return in.nextInt();
        } catch (Exception e) {
            return -1;
        }
    }
}
