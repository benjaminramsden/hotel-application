package main.java.service;

import main.java.model.Customer;
import main.java.model.IRoom;
import main.java.model.Reservation;
import main.java.model.RoomType;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public class ReservationService {
    final Set<IRoom> roomSet;
    final Map<Customer, List<Reservation>> reservationMap;

    private ReservationService() {
        roomSet = new HashSet<>();
        reservationMap = new HashMap<>();
    }

    private static final class ReservationServiceHolder {
        private static final ReservationService instance = new ReservationService();
    }

    public static ReservationService getInstance() {
        return ReservationServiceHolder.instance;
    }

    public void addRoom(IRoom room) {
        roomSet.add(room);
    }

    public IRoom getARoom(String roomId) {
        return roomSet.stream().filter(r -> r.getRoomNumber().equals(roomId)).collect(Collectors.toSet()).iterator().next();
    }

    public Reservation reserveARoom(Customer customer, IRoom room, LocalDate checkInDate, LocalDate checkOutDate) {
        Reservation reservation = new Reservation(customer, room, checkInDate, checkOutDate);
        Collection<IRoom> availableRooms = findRooms(checkInDate, checkOutDate, room.getRoomType());
        if (availableRooms.isEmpty() || !availableRooms.contains(room)) {
            throw new IllegalArgumentException("Room specified is not available.");
        }

        if (!reservationMap.containsKey(customer)) {
            List<Reservation> reservationList = new ArrayList<>();
            reservationList.add(reservation);
            reservationMap.put(customer, reservationList);
        } else {
            List<Reservation> existingReservations = reservationMap.get(customer);
            List<Reservation> duplicateReservation = existingReservations.stream().filter(r -> r.equals(reservation)).toList();
            if (!duplicateReservation.isEmpty()) {
                throw new IllegalStateException("Reservation already exists!");
            }
            existingReservations.add(reservation);
            reservationMap.put(customer, existingReservations);
        }
        return reservation;
    }

    public Collection<IRoom> findRooms(LocalDate checkInDate, LocalDate checkOutDate, RoomType roomType) {
        // reservationMap contains all info needed, iterate through this, we will clean this up with a store later.
        List<Reservation> reservationList = reservationMap.values().stream().flatMap(List::stream).toList();

        return findAvailableRooms(reservationList, checkInDate, checkOutDate, roomType);
    }

    private Collection<IRoom> findAvailableRooms(List<Reservation> reservationList, LocalDate checkInDate,
                                           LocalDate checkOutDate, RoomType roomType) {
        // Immediately available rooms are outside of check-in check-out dates.
        List<IRoom> collidingRooms = reservationList
                .stream()
                .filter(r -> ReservationService.collides(r.getCheckInDate(), r.getCheckOutDate(), checkInDate, checkOutDate))
                .map(Reservation::getRoom)
                .toList();

        return getAllRooms()
                .stream()
                .filter(room -> !collidingRooms.contains(room))
                .filter(r -> r.getRoomType() == roomType)
                .collect(Collectors.toSet());
    }

    private static boolean collides(LocalDate checkInDate1,LocalDate checkOutDate1,
                                    LocalDate checkInDate2, LocalDate checkOutDate2) {
        if (checkInDate1.isAfter(checkInDate2)) {
            return collides(checkInDate2, checkOutDate2, checkInDate1, checkOutDate1);
        }
        return checkInDate2.isBefore(checkOutDate1);
    }

    public Set<IRoom> getAllRooms() {
        return roomSet;
    }

    public Collection<Reservation> getCustomersReservation(Customer customer) {
        return reservationMap.get(customer);
    }

    public void printAllReservation() {
        reservationMap.values().forEach(System.out::println);
    }

    public void purgeAllReservationsAndRooms() {
        roomSet.clear();
        reservationMap.clear();
    }
}
