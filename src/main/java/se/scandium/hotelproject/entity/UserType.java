package se.scandium.hotelproject.entity;

public enum UserType {
    ADMINISTRATOR(1, "administrator"),
    RECEPTION(2, "reception");

    private final int code;
    private final String value;

    UserType(int code, String value) {
        this.code = code;
        this.value = value;
    }

    public int getCode() {
        return code;
    }

    public String getValue() {
        return value;
    }
}
