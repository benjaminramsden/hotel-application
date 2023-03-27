package main.java.ui;

import main.java.api.HotelResource;
import main.java.model.IRoom;
import main.java.model.RoomType;
import main.java.service.CustomerService;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Collection;
import java.util.Objects;
import java.util.Scanner;

public class MainMenu implements IMenu {
    private final Scanner s;

    public MainMenu() {
        s = new Scanner(System.in);
    }

    public void displayMenu() {
        System.out.println("--------------------------");
        System.out.println("Main Menu");
        System.out.println("--------------------------");
        System.out.println("1. Find and reserve a room");
        System.out.println("2. See my reservations");
        System.out.println("3. Create an account");
        System.out.println("4. Admin");
        System.out.println("5. Exit");
    }

    public void selectOption() {

        displayMenu();
        while (s.hasNextLine()) {
            switch (s.next()) {
                case "1" -> findAndReserveRoom();
                case "2" -> getCustomerReservations();
                case "3" -> createCustomerAccount();
                case "4" -> {
                    AdminMenu menu = new AdminMenu(s);
                    menu.selectOption();
                }
                case "5" -> {
                    System.out.println("Exiting program.");
                    System.exit(0);
                }
                default -> {
                    System.out.println("Unsupported input, please select an option.");
                }
            }
            displayMenu();
        }
    }

    private void findAndReserveRoom() {
        LocalDate checkInDate;
        LocalDate checkOutDate;
        RoomType roomType;

        System.out.println("Please provide your email");
        String email = s.next();
        try {
            CustomerService.getInstance().getCustomer(email);
        } catch (IllegalArgumentException ex) {
            System.out.println(ex.getMessage());
            return;
        }
        System.out.println("Please provide your check-in date in form yyyy-mm-dd");
        try {
            checkInDate = LocalDate.parse(s.next());
        } catch (DateTimeParseException ex) {
            System.out.println("Input does not match specified format yyyy-mm-dd");
            return;
        }
        try {
            System.out.println("Please provide your check-out date in form yyyy-mm-dd");
            checkOutDate = LocalDate.parse(s.next());
        } catch (DateTimeParseException ex) {
            System.out.println("Input does not match specified format yyyy-mm-dd");
            return;
        }
        System.out.println("Please provide desired room type: SINGLE or DOUBLE");
        try {
            roomType = RoomType.valueOf(s.next());
        } catch (IllegalArgumentException ex) {
            System.out.println("Unrecognised room type, please select either SINGLE or DOUBLE");
            return;
        }
        Collection<IRoom> availableRooms = HotelResource.getInstance().findRooms(checkInDate, checkOutDate, roomType);
        if (availableRooms.isEmpty()) {
            System.out.println("We apologise that there are no rooms available for the selected dates.");
            System.out.println("Would you like to search for available rooms 7 days in the future? (y/n)");
            String confirm = s.next();
            if (!Objects.equals(confirm, "y")) {
                return;
            }
            checkInDate = checkInDate.plusDays(7);
            checkOutDate = checkOutDate.plusDays(7);
            availableRooms = HotelResource.getInstance().findRooms(checkInDate, checkOutDate, roomType);
            if (availableRooms.isEmpty()) {
                System.out.println("We apologise that there are no rooms available for the selected dates.");
                return;
            }
        }
        System.out.println("Available rooms are:");
        for (IRoom availableRoom : availableRooms) {
            System.out.println(availableRoom.toString());
        }

        System.out.println("Which room would you like? Please enter the room number. Select 'Q' to return to menu");
        String chosenRoomNumber = s.next();
        try {
            System.out.println("Reservation booked: " + HotelResource.getInstance().bookARoom(
                    email, HotelResource.getInstance().getRoom(chosenRoomNumber), checkInDate, checkOutDate).toString());
        } catch (IllegalArgumentException ex) {
            System.out.println(ex.getMessage());
        }
    }

    private void getCustomerReservations() {
        System.out.println("Please provide your email");
        String email = s.next();
        try {
            System.out.println(
                    HotelResource.getInstance().getCustomerReservations(HotelResource.getInstance().getCustomer(email)).toString());
        } catch (IllegalArgumentException ex) {
            System.out.println(ex.getMessage());
        }
    }

    private void createCustomerAccount() {
        System.out.println("Please provide your email");
        String email = s.next();
        System.out.println("Please provide your first name");
        String firstName = s.next();
        System.out.println("Please provide your last name");
        String lastName = s.next();
        try {
            HotelResource.getInstance().createACustomer(email, firstName, lastName);
            System.out.println("Customer successfully created");
        } catch (IllegalStateException ex) {
            System.out.println("Found existing account for this email.");
        } catch (IllegalArgumentException ex) {
            System.out.println(ex.getMessage());
        }
    }
}
