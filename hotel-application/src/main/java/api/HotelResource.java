package main.java.api;

import main.java.model.Customer;
import main.java.model.IRoom;
import main.java.model.Reservation;
import main.java.model.RoomType;
import main.java.service.CustomerService;
import main.java.service.ReservationService;

import java.time.LocalDate;
import java.util.Collection;

public final class HotelResource {

    private HotelResource() {
    }

    private static class HotelResourceSingletonFactory {
        private static final HotelResource instance = new HotelResource();
    }

    public static HotelResource getInstance() {
        return HotelResourceSingletonFactory.instance;
    }

    public Customer getCustomer(String email) {
        return CustomerService.getInstance().getCustomer(email);
    }

    public void createACustomer(String email, String firstName, String lastName) {
        CustomerService.getInstance().addCustomer(email, firstName, lastName);
    }

    public Collection<Reservation> getCustomerReservations(Customer customer) {
        return ReservationService.getInstance().getCustomersReservation(customer);
    }

    public IRoom getRoom(String roomNumber) {
        return ReservationService.getInstance().getARoom(roomNumber);
    }

    public Reservation bookARoom(String customerEmail, IRoom room, LocalDate checkInDate, LocalDate checkOutDate) {
        Customer customer = CustomerService.getInstance().getCustomer(customerEmail);
        return ReservationService.getInstance().reserveARoom(customer, room, checkInDate, checkOutDate);
    }

    public Collection<IRoom> findRooms(LocalDate checkInDate, LocalDate checkOutDate, RoomType roomType) {
        return ReservationService.getInstance().findRooms(checkInDate, checkOutDate, roomType);
    }
}
