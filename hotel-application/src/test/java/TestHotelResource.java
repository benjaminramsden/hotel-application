package test.java;

import main.java.api.AdminResource;
import main.java.api.HotelResource;
import main.java.model.*;
import org.junit.jupiter.api.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

public class TestHotelResource {
    private static AdminResource adminResource;
    private static HotelResource hotelResource;
    private final PrintStream standardOut = System.out;
    private final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();

    @BeforeAll
    static void setup() {
        adminResource = AdminResource.getInstance();
        hotelResource = HotelResource.getInstance();
    }

    @BeforeEach
    void init() {
        System.setOut(new PrintStream(outputStreamCaptor));
        adminResource.populateTestData();
    }

    @AfterEach
    public void tearDown() {
        System.setOut(standardOut);
        adminResource.clearAllData();
    }

    @Test
    public void getCustomer() {
        Customer testCustomer = new Customer("foo", "bar", "a@b.com");
        Assertions.assertEquals(testCustomer, hotelResource.getCustomer("a@b.com"));
    }

    @Test
    public void getCustomer_nonexistent_throws() {
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> hotelResource.getCustomer("foo@bar.com"));
    }

    @Test
    public void createACustomer() {
        String email = "foo@bar.com";
        hotelResource.createACustomer(email, "baz", "qux");

        Assertions.assertNotNull(hotelResource.getCustomer(email));
    }

    @Test
    public void createACustomer_throws_for_duplicates() {
        Assertions.assertThrows(IllegalStateException.class,
                () -> hotelResource.createACustomer("a@b.com", "foo", "bar"));
    }

    @Test
    public void getCustomerReservations() {
        String email = "b@b.com";
        Customer customer = hotelResource.getCustomer(email);
        IRoom existingRoom = hotelResource.getRoom("104");
        Reservation existingReservation = new Reservation(
                customer,
                existingRoom,
                LocalDate.of(2022, 12, 7),
                LocalDate.of(2022, 12, 9));

        Assertions.assertTrue(hotelResource.getCustomerReservations(customer).contains(existingReservation));
    }

    @Test
    public void getRoom() {
        Assertions.assertInstanceOf(IRoom.class, hotelResource.getRoom("101"));
    }

    @Test
    public void getRoom_nonexistent() {
        Assertions.assertThrows(NoSuchElementException.class,
                () -> hotelResource.getRoom("foo"));
    }

    @Test
    public void bookARoom() {
        IRoom existingRoom = hotelResource.getRoom("104");

        Assertions.assertInstanceOf(Reservation.class, hotelResource.bookARoom(
                "a@b.com",
                existingRoom,
                LocalDate.of(2023, 1,10),
                LocalDate.of(2023, 1, 12)));
    }

    @Test
    public void findRooms() {
        List<String> roomNumbers = new ArrayList<>();
        roomNumbers.add("102");
        roomNumbers.add("103");
        Assertions.assertTrue(
                hotelResource.findRooms(
                        LocalDate.of(2023, 1, 1),
                        LocalDate.of(2023, 1, 7),
                        RoomType.DOUBLE).containsAll(roomNumbers.stream().map(num ->hotelResource.getRoom(num)).toList()));
    }
}
