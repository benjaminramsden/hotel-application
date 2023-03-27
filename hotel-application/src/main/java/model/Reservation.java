package main.java.model;

import java.time.LocalDate;

public record Reservation(Customer customer, IRoom room,
                          LocalDate checkInDate, LocalDate checkOutDate) {

    public LocalDate getCheckInDate() {
        return checkInDate;
    }

    public LocalDate getCheckOutDate() {
        return checkOutDate;
    }

    public IRoom getRoom() {
        return room;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Reservation reservation = (Reservation) o;
        return checkInDate.equals(reservation.checkInDate) &&
                customer.equals(reservation.customer) &&
                checkOutDate.equals(reservation.checkOutDate) &&
                room.equals(reservation.room);
    }

    @Override
    public int hashCode() {
        int result = 17;
        result = 31 * result + (checkInDate != null ? checkInDate.hashCode() : 0);
        result = 31 * result + (checkOutDate != null ? checkOutDate.hashCode() : 0);
        result = 31 * result + (customer != null ? customer.hashCode() : 0);
        result = 31 * result + (room != null ? room.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Reservation details: Customer: " + customer.toString() + " Room: " + room.toString() + " Check-in: " +
                checkInDate + " Check-out: " + checkOutDate;
    }
}
