package se.scandium.hotelproject.controller.fxml;


import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import javafx.fxml.FXML;
import net.rgielen.fxweaver.core.FxWeaver;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import se.scandium.hotelproject.service.BookingService;

@Component
@FxmlView("/fxml/add_booking.fxml")
public class BookRoomController {

    private final BookingService bookingService;
    private final FxWeaver fxWeaver;

    @Autowired
    public BookRoomController(BookingService bookingService, FxWeaver fxWeaver) {
        this.bookingService = bookingService;
        this.fxWeaver = fxWeaver;
    }

    @FXML
    private JFXComboBox roomComboBox;
}
