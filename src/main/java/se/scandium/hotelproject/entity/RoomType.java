package se.scandium.hotelproject.entity;

public enum RoomType {
    Single(1, "A room assigned to one person"),
    Double(2, "A room assigned to two people"),
    Triple(3, "A room assigned to three people"),
    Quad(4, "A room assigned to four people"),
    Queen(5, "A room with a queen-sized bed"),
    King(6, "A room with a king-sized bed.");

    private final int code;
    private final String message;

    RoomType(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
