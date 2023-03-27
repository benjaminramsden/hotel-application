package test.java;

import main.java.api.AdminResource;
import main.java.model.*;
import org.junit.jupiter.api.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.List;
import java.util.Set;

public class TestAdminResource {
    private static AdminResource adminResource;
    private final PrintStream standardOut = System.out;
    private final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();

    @BeforeAll
    static void setup() {
        adminResource = AdminResource.getInstance();
    }

    @BeforeEach
    void init() {
        adminResource.populateTestData();
        System.setOut(new PrintStream(outputStreamCaptor));
    }

    @AfterEach
    public void tearDown() {
        System.setOut(standardOut);
        adminResource.clearAllData();
    }

    @Test
    public void addRooms_singular() {
        IRoom newRoom = new Room("1000", 25.0, RoomType.DOUBLE);
        adminResource.addRoom(List.of(newRoom));
        Assertions.assertTrue(adminResource.getAllRooms().contains(newRoom));
    }

    @Test
    public void addRooms_multiple() {
        List<IRoom> newRooms = List.of(new Room("1000", 25.0, RoomType.DOUBLE),
                                 new Room("2000", 0.0, RoomType.SINGLE));
        adminResource.addRoom(newRooms);
        Assertions.assertTrue(adminResource.getAllRooms().containsAll(newRooms));
    }

    @Test
    public void getAllRooms() {
        Assertions.assertEquals(Set.of(
                new Room("101", 100.0, RoomType.SINGLE),
                new FreeRoom("102", RoomType.DOUBLE),
                new FreeRoom("103", RoomType.DOUBLE),
                new Room("104", 250.0, RoomType.SINGLE)),
                adminResource.getAllRooms());
    }

    @Test
    public void getAllCustomers() {
        Assertions.assertTrue(adminResource.getAllCustomers().containsAll(
                List.of(new Customer("name_unused", "name_unchecked", "a@b.com"),
                        new Customer("not_used", "not_required_to_be_equal", "b@b.com"))));
    }

    @Test
    public void displayAllReservations() {
        adminResource.displayAllReservations();

        Assertions.assertEquals(
        "[Reservation details: Customer: Customer details: Name: B B Email: b@b.com Room: Room " +
                "details: 104 Price: 250.0 Type: SINGLE Check-in: 2022-12-07 Check-out: 2022-12-09]\n" +
                "[Reservation details: Customer: Customer details: Name: A B Email: a@b.com Room: Room " +
                "details: 101 Price: 100.0 Type: SINGLE Check-in: 2022-12-03 Check-out: 2022-12-05, " +
                "Reservation details: Customer: Customer details: Name: A B Email: a@b.com Room: Room " +
                "details: 102 Price: FREE  Type: DOUBLE Check-in: 2022-12-07 Check-out: 2022-12-09]",
                outputStreamCaptor.toString().trim());
    }
}
