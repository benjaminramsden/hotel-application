package main.java.api;

import main.java.model.*;
import main.java.service.CustomerService;
import main.java.service.ReservationService;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

public class AdminResource {

    private AdminResource() {
    }

    private static class AdminResourceSingletonFactory {
        private static final AdminResource instance = new AdminResource();
    }

    public static AdminResource getInstance() {
        return AdminResource.AdminResourceSingletonFactory.instance;
    }

    public void addRoom(List<IRoom> rooms) {
        rooms.forEach(ReservationService.getInstance()::addRoom);
    }

    public Collection<IRoom> getAllRooms() {
        return ReservationService.getInstance().getAllRooms();
    }

    public Collection<Customer> getAllCustomers() {
        return CustomerService.getInstance().getAllCustomers();
    }

    public void displayAllReservations() {
        ReservationService.getInstance().printAllReservation();
    }

    public void populateTestData() {
        // Add rooms
        List<IRoom> rooms = List.of(
                new Room("101", 100.0, RoomType.SINGLE),
                new FreeRoom("102", RoomType.DOUBLE),
                new FreeRoom("103", RoomType.DOUBLE),
                new Room("104", 250.0, RoomType.SINGLE));

        // Check whether already populated
            if (getAllRooms().containsAll(rooms)) {
            System.out.println("Test data already populated.");
            return;
        }

        AdminResource.getInstance().addRoom(rooms);

        // Add customers
        HotelResource.getInstance().createACustomer("a@b.com", "A", "B");
        HotelResource.getInstance().createACustomer("b@b.com", "B", "B");
        HotelResource.getInstance().createACustomer("c@b.com", "C", "B");

        // Add reservations
        HotelResource.getInstance().bookARoom(
                "a@b.com",
                HotelResource.getInstance().getRoom("101"),
                LocalDate.parse("2022-12-03"),
                LocalDate.parse("2022-12-05"));
        HotelResource.getInstance().bookARoom(
                "a@b.com",
                HotelResource.getInstance().getRoom("102"),
                LocalDate.parse("2022-12-07"),
                LocalDate.parse("2022-12-09"));
        HotelResource.getInstance().bookARoom(
                "b@b.com",
                HotelResource.getInstance().getRoom("104"),
                LocalDate.parse("2022-12-07"),
                LocalDate.parse("2022-12-09"));
    }

    public void clearAllData() {
        purgeAllReservationsAndRooms();
        purgeAllCustomers();
    }

    private void purgeAllReservationsAndRooms() {
        ReservationService.getInstance().purgeAllReservationsAndRooms();
    }

    private void purgeAllCustomers() {
        CustomerService.getInstance().purgeAllCustomers();
    }
}
