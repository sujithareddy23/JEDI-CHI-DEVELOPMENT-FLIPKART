package com.flipfit.client;

import com.flipfit.bean.GymCenter;
import com.flipfit.bean.GymOwner;
import com.flipfit.bean.Slot;
import com.flipfit.business.GymOwnerImpl;
import com.flipfit.business.GymOwnerInterface;
import com.flipfit.business.GymCenterInterface;
import com.flipfit.business.GymCenterImpl;
import com.flipfit.business.SlotInterface;
import com.flipfit.business.SlotImpl;
import com.flipfit.constants.DemoDataConstants;
import com.flipfit.constants.IdPrefixConstants;
import com.flipfit.dao.GymOwnerDAO;
import com.flipfit.dao.impl.GymOwnerDAOImpl;
import com.flipfit.exception.AlreadyExistsException;
import com.flipfit.exception.ValidationException;
import com.flipfit.validation.OwnerValidation;
import com.flipfit.validation.ValidationResult;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class GymOwnerClient {
    private final GymOwnerInterface ownerService;
    private final GymCenterInterface centerService;
    private final SlotInterface slotService;
    private final GymOwnerDAO ownerDAO;

    public GymOwnerClient() {
        this.ownerService = new GymOwnerImpl();
        this.centerService = new GymCenterImpl();
        this.slotService = new SlotImpl();
        this.ownerDAO = new GymOwnerDAOImpl();
    }

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

        GymOwner owner = new GymOwner();
        owner.setOwnerName(name);
        owner.setEmailId(email);
        owner.setPassword(password);
        owner.setPanNo(pan);
        owner.setGstNo(gst == null ? "" : gst);
        try {
            ownerService.registerOwner(owner);
            System.out.println("Registration request sent. Awaiting Admin validation.");
        } catch (ValidationException | AlreadyExistsException e) {
            System.out.println(e.getMessage());
        }
    }

    public void gymOwnerPage(Scanner in, String ownerEmail) {
        Optional<GymOwner> ownerOpt = ownerDAO.getOwnerByEmail(ownerEmail);
        if (!ownerOpt.isPresent()) {
            System.out.println("Owner not found.");
            return;
        }
        
        GymOwner owner = ownerOpt.get();
        String ownerId = owner.getId();
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

        GymCenter gym = new GymCenter();
        gym.setGymId(gymId);
        gym.setName(name);
        gym.setLocation(loc);
        gym.setContactNo(contact);
        gym.setOwnerId(ownerId);
        List<Slot> slots = createDefaultSlots(gymId, 5);
        gym.setSlotList(slots);
        try {
            ownerService.registerGym(gym);
            System.out.println("Gym registered. Pending Admin validation.");
        } catch (ValidationException e) {
            System.out.println(e.getMessage());
        }
    }

    private List<Slot> createDefaultSlots(String gymId, int capacity) {
        List<Slot> list = new ArrayList<>();
        for (int h = DemoDataConstants.SLOT_AM_START; h < DemoDataConstants.SLOT_AM_END; h++) {
            list.add(createSlot(gymId, h, capacity));
        }
        for (int h = DemoDataConstants.SLOT_PM_START; h < DemoDataConstants.SLOT_PM_END; h++) {
            list.add(createSlot(gymId, h, capacity));
        }
        return list;
    }

    private Slot createSlot(String gymId, int hour, int capacity) {
        Slot slot = new Slot();
        slot.setSlotId(String.format(IdPrefixConstants.SLOT_ID_FORMAT, gymId, hour));
        slot.setGymId(gymId);
        slot.setStartTime(LocalTime.of(hour, 0));
        slot.setEndTime(LocalTime.of(hour + 1, 0));
        slot.setTotalCapacity(capacity);
        return slot;
    }

    private void updateSlots(Scanner in) {
        System.out.print("Gym ID: ");
        String gymId = in.next().trim();
        Optional<GymCenter> gymOpt = centerService.getCenter(gymId);
        if (!gymOpt.isPresent()) {
            System.out.println("Gym not found.");
            return;
        }
        
        GymCenter gym = gymOpt.get();
        List<Slot> slots = slotService.getSlotsByGym(gymId);
        if (slots == null) slots = new ArrayList<>();
        System.out.println("Current slots: " + slots.size() + ". Update capacity? (y/n)");
        String c = in.next().trim();
        if (!c.equalsIgnoreCase("y")) return;
        System.out.print("New capacity per slot: ");
        int cap = readInt(in);
        in.nextLine();
        if (cap <= 0) return;
        for (Slot slot : slots) slot.setTotalCapacity(cap);
        ownerService.updateSlots(gymId, slots);
        System.out.println("Slots updated.");
    }

    private void modifyProfile(Scanner in, String ownerEmail) {
        System.out.print("New name (blank to skip): ");
        String name = in.nextLine().trim();
        System.out.print("New password (blank to skip): ");
        String pwd = in.nextLine().trim();
        ValidationResult vr = OwnerValidation.validateForProfileUpdate(name, pwd);
        if (!vr.isValid()) {
            System.out.println("Validation failed: " + vr.getMessage());
            return;
        }
        GymOwner owner = new GymOwner();
        owner.setEmailId(ownerEmail);
        if (!name.isEmpty()) owner.setOwnerName(name);
        if (!pwd.isEmpty()) owner.setPassword(pwd);
        ownerService.modifyOwnerProfile(owner);
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
