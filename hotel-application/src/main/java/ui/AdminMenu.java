package main.java.ui;

import main.java.api.AdminResource;
import main.java.model.*;

import java.util.Collection;
import java.util.List;
import java.util.Scanner;

public final class AdminMenu implements IMenu {
    private final Scanner scanner;

    public AdminMenu(Scanner scanner) {
        this.scanner = scanner;
    }

    public void displayMenu() {
        System.out.println("--------------------");
        System.out.println("Admin Menu");
        System.out.println("--------------------");
        System.out.println("1. See all customers");
        System.out.println("2. See all rooms");
        System.out.println("3. See all Reservations");
        System.out.println("4. Add a Room");
        System.out.println("5. Add test data");
        System.out.println("6. Back to Main Menu");
    }

    public void selectOption() {

        displayMenu();
        while (scanner.hasNextLine()) {
            switch (scanner.next()) {
                case "1" -> getAllCustomers();
                case "2" -> getAllRooms();
                case "3" -> printAllReservations();
                case "4" -> addARoom();
                case "5" -> populateTestData();
                case "6" -> {
                    return;
                }
                default -> {
                    System.out.println("Unsupported input, please select an option.");
                }
            }
            displayMenu();
        }
    }

    private void getAllCustomers() {
        Collection<Customer> customers = AdminResource.getInstance().getAllCustomers();
        customers.forEach(System.out::println);
    }

    private void getAllRooms() {
        Collection<IRoom> rooms = AdminResource.getInstance().getAllRooms();
        rooms.forEach(System.out::println);
    }

    private void printAllReservations() {
        AdminResource.getInstance().displayAllReservations();
    }

    private void addARoom() {
        RoomType roomType;
        double price;

        System.out.println("Please provide the room number");
        String roomNumber = scanner.next();
        try {
            Integer.parseInt(roomNumber);
        } catch (NumberFormatException ex) {
            System.out.println("Room number provided not recognised as acceptable number, not adding.");
            return;
        }
        System.out.println("Please provide the room type: SINGLE or DOUBLE");
        try {
            roomType = RoomType.valueOf(scanner.next());
        } catch (IllegalArgumentException ex) {
            System.out.println("Unrecognised room type, please select either SINGLE or DOUBLE");
            return;
        }
        System.out.println("Please provide the price for this room");
        String priceAsString = scanner.next();
        try {
            price = Double.parseDouble(priceAsString);
        } catch (NumberFormatException ex) {
            System.out.println("Price provided not recognised as acceptable value, not adding.");
            return;
        }
        try {
            // Would prefer this logic be encapsulated in ReservationService, but instructions stipulate to have addRoom
            // public and take IRoom at the API layer.
            IRoom room;
            if (price > 0.0) {
                room = new Room(roomNumber, price, roomType);
            } else if (price == 0.0) {
                room = new FreeRoom(roomNumber, roomType);
            } else {
                throw new Exception("Negative price not allowed, not adding.");
            }
            AdminResource.getInstance().addRoom(List.of(room));
        } catch (IllegalStateException ex) {
            System.out.println("Room with this number already exists, not adding.");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private void populateTestData() {
        AdminResource.getInstance().populateTestData();
    }
}
