package se.scandium.hotelproject.controller.fxml;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import javafx.stage.Window;
import net.rgielen.fxweaver.core.FxWeaver;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import se.scandium.hotelproject.dto.BookingDto;
import se.scandium.hotelproject.dto.CustomerDto;
import se.scandium.hotelproject.dto.RoomDto;
import se.scandium.hotelproject.entity.*;
import se.scandium.hotelproject.exception.ArgumentInvalidException;
import se.scandium.hotelproject.exception.RecordNotFoundException;
import se.scandium.hotelproject.service.BookingService;
import se.scandium.hotelproject.service.CustomerService;
import se.scandium.hotelproject.service.RoomService;


import java.time.LocalDate;
import java.time.Period;
import java.util.List;

@Component
@FxmlView("/fxml/booking_form.fxml")
public class BookingController {

    private final CustomerService customerService;
    private final BookingService bookingService;
    private final RoomService roomService;
    private final FxWeaver fxWeaver;
    private BookingDto bookingDto;
    private CustomerDto customerDto;
    private RoomDto selectedRoomDto;
    private ObservableList<RoomDto> data;

    @Autowired
    public BookingController (CustomerService customerService, BookingService bookingService, RoomService roomService, FxWeaver fxWeaver) {
        this.customerService = customerService;
        this.bookingService = bookingService;
        this.roomService = roomService;
        this.fxWeaver = fxWeaver;
    }

    @FXML
    private Tab customerInfoTab;
    @FXML
    private Tab bookingInfoTab;
    @FXML
    private TableView<RoomDto> availableRoomsTable;
    @FXML
    private TableColumn<RoomDto, Integer> idColumn;
    @FXML
    private TableColumn<RoomDto, String> roomNameColumn;
    @FXML
    private TableColumn<RoomDto, RoomType> roomTypeColumn;
    @FXML
    private TableColumn<RoomDto, String> locationColumn;
    @FXML
    private TableColumn<RoomDto, String> descriptionColumn;
    @FXML
    private TextField firstNameText;
    @FXML
    private TextField lastNameText;
    @FXML
    private Label personalInfoLabel;
    @FXML
    private Label addressLabel;
    @FXML
    private Label priceLabel;
    @FXML
    private Label screenLabel;
    @FXML
    private Label lunchLabel;
    @FXML
    private Label breakfastLabel;
    @FXML
    private DatePicker birthPicker;
    @FXML
    private ComboBox<Gender> genderComboBox;
    @FXML
    private TextField streetText;
    @FXML
    private TextField cityText;
    @FXML
    private TextField countryText;
    @FXML
    private TextField zipCodeText;
    @FXML
    private DatePicker fromDatePicker;
    @FXML
    private DatePicker toDatePicker;
    @FXML
    private TextField peopleText;
    @FXML
    private ComboBox<PayType> payTypeComboBox;
    @FXML
    private CheckBox lnCheckBox;
    @FXML
    private CheckBox bfCheckBox;
    @FXML
    private JFXComboBox<RoomType> roomTypeComboBox;
    @FXML
    private JFXButton saveButton;
    @FXML
    private JFXButton searchButton;
    @FXML
    private Button calcButton;
    @FXML
    private Text errorText;

    @FXML
    void initialize() {
        bookingDto = new BookingDto();
        customerDto = new CustomerDto();
        loadTable();
        setGenderComboBox();
        setPayTypeComboBox();
        setRoomTypeComboBox();
        calcButton.setOnAction(this::calcTotalAmount);
        searchButton.setOnAction(this::showAvailableRooms);
        saveButton.setOnAction(this::saveBooking);


    }

    private void loadTable() {
        availableRoomsTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                System.out.println("newSelection = " + newSelection);
                this.selectedRoomDto = newSelection;
            }
        });

        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        roomNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        roomTypeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        locationColumn.setCellValueFactory(new PropertyValueFactory<>("location"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
    }

    private boolean validateData() {

        if (this.selectedRoomDto == null){
            errorText.setText("room is not selected");
            showAlert(Alert.AlertType.WARNING, saveButton.getScene().getWindow(), "Warning!", errorText.getText());
            return false;
        }

        String firstName = this.firstNameText.getText();
        if (firstName.trim().length() == 0) {
            errorText.setText("Enter First Name");
            showAlert(Alert.AlertType.WARNING, saveButton.getScene().getWindow(), "Warning!", errorText.getText());
            return false;
        }
        customerDto.setFirstName(firstName);

        String lastName = this.lastNameText.getText();
        if (lastName.trim().length() == 0) {
            errorText.setText("Enter Last Name");
            showAlert(Alert.AlertType.WARNING, saveButton.getScene().getWindow(), "Warning!", errorText.getText());
            return false;
        }
        customerDto.setLastName(lastName);

        LocalDate dateOfBirth = this.birthPicker.getValue();
        if (dateOfBirth == null) {
            errorText.setText("Select date of birth");
            showAlert(Alert.AlertType.WARNING, saveButton.getScene().getWindow(), "Warning!", errorText.getText());
            return false;
        }
        customerDto.setAge(calcCustomerAge(dateOfBirth));

        Gender gender = this.genderComboBox.getValue();
        if (gender == null) {
            errorText.setText("Select gender");
            showAlert(Alert.AlertType.WARNING, saveButton.getScene().getWindow(), "Warning!", errorText.getText());
            return false;
        }
        customerDto.setGender(gender);

        String street = this.streetText.getText();
        if (street.trim().length() == 0) {
            errorText.setText("Enter Street");
            showAlert(Alert.AlertType.WARNING, saveButton.getScene().getWindow(), "Warning!", errorText.getText());
            return false;
        }
        customerDto.setStreet(street);

        String city = this.cityText.getText();
        if (city.trim().length() == 0) {
            errorText.setText("Enter City");
            showAlert(Alert.AlertType.WARNING, saveButton.getScene().getWindow(), "Warning!", errorText.getText());
            return false;
        }
        customerDto.setCity(city);

        String country = this.countryText.getText();
        if (country.trim().length() == 0) {
            errorText.setText("Enter Country");
            showAlert(Alert.AlertType.WARNING, saveButton.getScene().getWindow(), "Warning!", errorText.getText());
            return false;
        }
        customerDto.setCountry(country);

        String zipCode = this.zipCodeText.getText();
        if (zipCode.trim().length() == 0) {
            errorText.setText("Enter Zip Code");
            showAlert(Alert.AlertType.WARNING, saveButton.getScene().getWindow(), "Warning!", errorText.getText());
            return false;
        }
        customerDto.setZipCode(zipCode);

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
                showAlert(Alert.AlertType.WARNING, saveButton.getScene().getWindow(), "Warning!", errorText.getText());
                return false;
            }
        }

        String  numberOfPeople = this.peopleText.getText();
        if (numberOfPeople.trim().length() == 0) {
            errorText.setText("Enter number of customers");
            showAlert(Alert.AlertType.WARNING, saveButton.getScene().getWindow(), "Warning!", errorText.getText());
            return false;
        }
        bookingDto.setNumberOfPersons(Integer.parseInt(numberOfPeople));

        PayType payType = this.payTypeComboBox.getSelectionModel().getSelectedItem();
        if (payType == null) {
            errorText.setText("Select payment type");
            showAlert(Alert.AlertType.WARNING, saveButton.getScene().getWindow(), "Warning!", errorText.getText());
            return false;
        }
        bookingDto.setPayType(payType);

        boolean lunch = this.lnCheckBox.isSelected();
        bookingDto.setLunch(lunch);

        boolean breakfast = this.bfCheckBox.isSelected();
        bookingDto.setBreakfast(breakfast);

        if (selectedRoomDto == null) {
            errorText.setText("Select an available room");
            showAlert(Alert.AlertType.WARNING, saveButton.getScene().getWindow(), "Warning!", errorText.getText());
            return false;
        }
        bookingDto.setRoom(selectedRoomDto);
        bookingDto.setCustomer(customerDto);

        return true;
    }

    private void resetForm() {
        firstNameText.setText(null);
        lastNameText.setText(null);
        birthPicker.setValue(null);
        genderComboBox.setItems(null);
        streetText.setText(null);
        cityText.setText(null);
        countryText.setText(null);
        zipCodeText.setText(null);
        fromDatePicker.setValue(null);
        toDatePicker.setValue(null);
        peopleText.setText(null);
        payTypeComboBox.setItems(null);
        lnCheckBox.setSelected(false);
        bfCheckBox.setSelected(false);
        priceLabel.setText("Total Amount: ");
    }


    public void setGenderComboBox() {
        genderComboBox.getItems().add(Gender.MALE);
        genderComboBox.getItems().add(Gender.FEMALE);
        genderComboBox.getItems().add(Gender.NON_BINARY);
    }

    public void setPayTypeComboBox() {
        payTypeComboBox.getItems().add(PayType.CASH);
        payTypeComboBox.getItems().add(PayType.CREDIT_CARD);
    }

    public void setRoomTypeComboBox() {
        roomTypeComboBox.getItems().add(RoomType.Single);
        roomTypeComboBox.getItems().add(RoomType.Double);
        roomTypeComboBox.getItems().add(RoomType.Triple);
        roomTypeComboBox.getItems().add(RoomType.Quad);
        roomTypeComboBox.getItems().add(RoomType.King);
        roomTypeComboBox.getItems().add(RoomType.Queen);
    }

    private void saveBooking (ActionEvent event) {

        if (validateData()) {
            try {
                BookingDto savedBookingDto = bookingService.update(bookingDto);
                CustomerDto savedCustomerDto = customerService.saveOrUpdate(customerDto);
                resetForm();
            } catch (RecordNotFoundException e) {
                e.printStackTrace();
                errorText.setText("Record not found");
                showAlert(Alert.AlertType.WARNING, saveButton.getScene().getWindow(), "Warning!", errorText.getText());
            }
        }

    }

    private void showAvailableRooms (ActionEvent event) {
        RoomType roomType = roomTypeComboBox.getValue();
        if (roomType == null) {
            errorText.setText("Select an available room");
            showAlert(Alert.AlertType.WARNING, searchButton.getScene().getWindow(), "Warning!", errorText.getText());
        }
        List<RoomDto> availableRooms = bookingService.searchAvailableRooms(fromDatePicker.getValue(), toDatePicker.getValue(), roomType);
        data = FXCollections.observableArrayList(availableRooms);
        availableRoomsTable.setItems(data);
    }

    private void calcTotalAmount (ActionEvent event) {
        LocalDate fromDate = fromDatePicker.getValue();
        LocalDate toDate = toDatePicker.getValue();
        RoomDto roomDto = selectedRoomDto;
        bookingDto.setRoom(selectedRoomDto);
        bookingDto.setNumberOfPersons(Integer.parseInt(peopleText.getText()));
        bookingDto.setFromDate(fromDate);
        bookingDto.setToDate(toDate);
        bookingDto.setFullPrice(bookingDto.calcFullPrice());
        priceLabel.setText("Total Amount: " + Double.toString(bookingDto.getFullPrice()));

    }

    private void showAlert(Alert.AlertType alertType, Window owner, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.initOwner(owner);
        alert.show();
    }

    public int calcCustomerAge(LocalDate dateOfBirth) {
        LocalDate currentDate = LocalDate.now();
        Period age = Period.between(dateOfBirth, currentDate);
        return age.getYears();
    }
}
