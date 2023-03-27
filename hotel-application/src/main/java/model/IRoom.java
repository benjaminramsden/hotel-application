package main.java.model;

public interface IRoom {
    String getRoomNumber();
    Double getRoomPrice();
    RoomType getRoomType();
    boolean isFree();
}
