package se.scandium.hotelproject.controller.fxml;


import com.jfoenix.controls.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.text.Text;
import javafx.stage.Window;
import net.rgielen.fxweaver.core.FxWeaver;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import se.scandium.hotelproject.dto.BookingDto;
import se.scandium.hotelproject.entity.PayType;
import se.scandium.hotelproject.entity.Room;
import se.scandium.hotelproject.exception.RecordNotFoundException;
import se.scandium.hotelproject.service.BookingService;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
@FxmlView("/fxml/add_booking.fxml")
public class BookRoomController {

    private final BookingService bookingService;
    private final FxWeaver fxWeaver;
    private BookingDto bookingDto;

    @Autowired
    public BookRoomController(BookingService bookingService, FxWeaver fxWeaver) {
        this.bookingService = bookingService;
        this.fxWeaver = fxWeaver;
    }

    @FXML
    private JFXComboBox<Room> RoomComboBox;
    @FXML
    private JFXDatePicker fromDatePicker;
    @FXML
    private JFXDatePicker toDatePicker;
    @FXML
    private Label breakfastLabel;
    @FXML
    private JFXCheckBox bfCheckBox;
    @FXML
    private Label lunchLabel;
    @FXML
    private JFXCheckBox lnCheckBox;
    @FXML
    private JFXComboBox<PayType> PayTypeComboBox;
    @FXML
    private Label priceLabel;
    @FXML
    private JFXButton addButton;
    @FXML
    private Text errorText;
    @FXML
    void initialize() {
        setPaymentType();
        addButton.setOnAction(this::saveBookingAction);
    }

    private void saveBookingAction(ActionEvent event) {
        if (validateAndBuildData()) {
            try {
                BookingDto savedBookingDto = bookingService.update(bookingDto);
                System.out.println(savedBookingDto);
            } catch (RecordNotFoundException e) {
                e.printStackTrace();
                errorText.setText("BookingNotFoundException");
                showAlert(Alert.AlertType.ERROR, addButton.getScene().getWindow(), "Error!", "UserNotFoundException");
            }
        }
    }


    private boolean validateAndBuildData() {

        LocalDate fromDate = fromDatePicker.getValue();
        LocalDate toDate = toDatePicker.getValue();
        List<LocalDate> bookingDates = new ArrayList<>();
        bookingDates.add(fromDate);
        bookingDates.add(toDate);
        bookingDto.setBookingDays(bookingDates);

        String breakfast = this.bfCheckBox.getText();
        bookingDto.setBreakfast(breakfast.equalsIgnoreCase("Yes"));

        String lunch = this.lnCheckBox.getText();
        bookingDto.setLunch(lunch.equalsIgnoreCase("Yes"));

        PayType payType = this.PayTypeComboBox.getValue();
        if(payType == null) {
            errorText.setText("Payment type is not valid");
            showAlert(Alert.AlertType.WARNING, addButton.getScene().getWindow(), "Warning!", errorText.getText());
            return false;
        }
        bookingDto.setPayType(payType);

        return true;
    }

    private void setPaymentType() {
        PayTypeComboBox.getItems().add(PayType.CASH);
        PayTypeComboBox.getItems().add(PayType.CREDIT_CARD);
    }

    private void showAlert(Alert.AlertType alertType, Window owner, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.initOwner(owner);
        alert.show();
    }
}

