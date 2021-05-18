package se.scandium.hotelproject.controller.fxml;


import com.jfoenix.controls.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import javafx.stage.Window;
import net.rgielen.fxweaver.core.FxWeaver;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import se.scandium.hotelproject.dto.BookingDto;
import se.scandium.hotelproject.dto.CustomerDto;
import se.scandium.hotelproject.dto.RoomDto;
import se.scandium.hotelproject.entity.Booking;
import se.scandium.hotelproject.entity.Customer;
import se.scandium.hotelproject.entity.PayType;
import se.scandium.hotelproject.entity.Room;
import se.scandium.hotelproject.exception.ArgumentInvalidException;
import se.scandium.hotelproject.exception.RecordNotFoundException;
import se.scandium.hotelproject.service.BookingService;
import se.scandium.hotelproject.service.CustomerService;
import se.scandium.hotelproject.service.RoomService;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
@FxmlView("/fxml/add_booking.fxml")
public class BookRoomController {

    private final BookingService bookingService;
    private final CustomerService customerService;
    private final RoomService roomService;
    private final FxWeaver fxWeaver;
    private BookingDto bookingDto;
    private Booking booking;

    @Autowired
    public BookRoomController(BookingService bookingService,CustomerService customerService, RoomService roomService, FxWeaver fxWeaver) {
        this.bookingService = bookingService;
        this.customerService = customerService;
        this.roomService = roomService;
        this.fxWeaver = fxWeaver;
    }

    @FXML
    private JFXComboBox<CustomerDto> CustomerComboBox;
    @FXML
    private JFXComboBox<RoomDto> RoomComboBox;
    @FXML
    private JFXTextField NumberOfPeopleField;
    @FXML
    private DatePicker fromDatePicker;
    @FXML
    private DatePicker toDatePicker;
    @FXML
    private Label breakfastLabel;
    @FXML
    private CheckBox bfCheckBox;
    @FXML
    private Label lunchLabel;
    @FXML
    private CheckBox lnCheckBox;
    @FXML
    private JFXComboBox<PayType> payTypeComboBox;
    @FXML
    private Label priceLabel;
    @FXML
    private Button calculateButton;
    @FXML
    private JFXButton addButton;
    @FXML
    private Text errorText;
    @FXML
    void initialize() {
        bookingDto = new BookingDto();
        setPaymentType();
        setCustomerComboBox();
        setRoomComboBox();
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

        CustomerDto customerDto = CustomerComboBox.getValue();
        if (customerDto == null) {
            errorText.setText("Customer is not valid");
            showAlert(Alert.AlertType.WARNING, addButton.getScene().getWindow(), "Warning!", errorText.getText());
            return false;
        }
        bookingDto.setCustomer(customerDto);

        RoomDto roomDto = RoomComboBox.getValue();
        if (roomDto == null) {
            errorText.setText("Room is not valid");
            showAlert(Alert.AlertType.WARNING, addButton.getScene().getWindow(), "Warning!", errorText.getText());
            return false;
        }
        bookingDto.setRoom(roomDto);

        LocalDate fromDate = fromDatePicker.getValue();
        LocalDate toDate = toDatePicker.getValue();
        if (fromDate.isAfter(toDate)) {
            return false;
        } else {
            try {
                bookingDto.setFromDate(fromDate);
                bookingDto.setToDate(toDate);
                bookingDto.setBookingDays(bookingDto.createBookingDays());
            } catch (ArgumentInvalidException e) {
                e.printStackTrace();
                errorText.setText("Dates are not available");
                showAlert(Alert.AlertType.WARNING, addButton.getScene().getWindow(), "Warning!", errorText.getText());
                return false;
            }
        }
        String breakfast = this.bfCheckBox.getText();
        bookingDto.setBreakfast(breakfast.equalsIgnoreCase("Yes"));

        String lunch = this.lnCheckBox.getText();
        bookingDto.setLunch(lunch.equalsIgnoreCase("Yes"));

        PayType payType = this.payTypeComboBox.getValue();
        if(payType == null) {
            errorText.setText("Payment type is not valid");
            showAlert(Alert.AlertType.WARNING, addButton.getScene().getWindow(), "Warning!", errorText.getText());
            return false;
        }
        bookingDto.setPayType(payType);

        //Double finalPrice = booking.calcFullPrice();
        //priceLabel.setText("Final Price:" + (Double.toString(finalPrice)));

        return true;
    }

    private void setPaymentType() {
        payTypeComboBox.getItems().add(PayType.CASH);
        payTypeComboBox.getItems().add(PayType.CREDIT_CARD);
    }

    private void setCustomerComboBox() {
        CustomerComboBox.getItems().addAll(customerService.getAll());
    }

    private void setRoomComboBox() {
        RoomComboBox.getItems().addAll(roomService.getAll());
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

