package se.scandium.hotelproject.controller.fxml.singleton;

import se.scandium.hotelproject.dto.HotelDto;

public class HotelHolder {

    private HotelDto hotelDto;
    private final static HotelHolder INSTANCE = new HotelHolder();

    private HotelHolder() {
    }

    public static HotelHolder getInstance() {
        return INSTANCE;
    }

    public HotelDto getHotelDto() {
        return hotelDto;
    }

    public void setHotelDto(HotelDto hotelDto) {
        System.out.println("hotelDto = " + hotelDto);
        this.hotelDto = hotelDto;
    }
}
