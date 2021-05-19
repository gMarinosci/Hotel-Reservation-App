package se.scandium.hotelproject.controller.fxml;

import com.jfoenix.controls.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;
import net.rgielen.fxweaver.core.FxWeaver;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import se.scandium.hotelproject.dto.CustomerDto;
import se.scandium.hotelproject.dto.RoomDto;
import se.scandium.hotelproject.entity.Gender;
import se.scandium.hotelproject.entity.RoomType;
import se.scandium.hotelproject.exception.RecordNotFoundException;
import se.scandium.hotelproject.service.CustomerService;
import se.scandium.hotelproject.service.RoomService;

import java.util.List;

@Component
@FxmlView("/fxml/list_customer.fxml")
public class CustomerListController {

    private CustomerService customerService;
    private final FxWeaver fxWeaver;
    private CustomerDto selectedCustomerDto;
    private List<CustomerDto> customerDtoList;
    private ObservableList<CustomerDto> data;

    @Autowired
    public CustomerListController(FxWeaver fxWeaver, CustomerService customerService) {
        this.fxWeaver = fxWeaver;
        this.customerService = customerService;
    }

    @FXML
    private TableView<CustomerDto> customerDtoTableView;
    @FXML
    private TableColumn<CustomerDto, Integer> idColumn;
    @FXML
    private TableColumn<CustomerDto, String> nameColumn;
    @FXML
    private TableColumn<CustomerDto, String> surnameColumn;
    @FXML
    private TableColumn<CustomerDto, Integer> ageColumn;
    @FXML
    private TableColumn<CustomerDto, Gender> genderColumn;
    @FXML
    private TableColumn<CustomerDto, String> countryColumn;
    @FXML
    private TableColumn<CustomerDto, String> cityColumn;
    @FXML
    private TableColumn<CustomerDto, String> streetColumn;
    @FXML
    private TableColumn<CustomerDto, String> zipCodeColumn;
    @FXML
    private JFXButton deleteButton;
    @FXML
    private Text errorTextForm;
    @FXML
    private Text errorTextTable;


    @FXML
    private JFXTextField nameField;
    @FXML
    private JFXTextField surnameField;
    @FXML
    private JFXTextField ageField;
    @FXML
    private JFXComboBox<Gender> genderComboBox;
    @FXML
    private JFXTextField countryField;
    @FXML
    private JFXTextField cityField;
    @FXML
    private JFXTextField streetField;
    @FXML
    private JFXTextField zipCodeField;
    @FXML
    private JFXButton updateButton;
    @FXML
    private JFXButton addBookingButton;

    private void loadDateTable() {
        customerDtoList = customerService.getAll();
        // Create table columns
        idColumn = new TableColumn<>("Id");
        nameColumn = new TableColumn<>("Name");
        surnameColumn = new TableColumn<>("Surname");
        ageColumn = new TableColumn<>("Age");
        genderColumn = new TableColumn<>("Gender");
        countryColumn = new TableColumn<>("Country");
        cityColumn = new TableColumn<>("City");
        streetColumn = new TableColumn<>("Street");
        zipCodeColumn = new TableColumn<>("Zip-code");

        idColumn.setCellValueFactory(new PropertyValueFactory<>("Id"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("Name"));
        surnameColumn.setCellValueFactory(new PropertyValueFactory<>("Surname"));
        ageColumn.setCellValueFactory(new PropertyValueFactory<>("Age"));
        genderColumn.setCellValueFactory(new PropertyValueFactory<>("Gender"));
        countryColumn.setCellValueFactory(new PropertyValueFactory<>("Country"));
        cityColumn.setCellValueFactory(new PropertyValueFactory<>("City"));
        streetColumn.setCellValueFactory(new PropertyValueFactory<>("Street"));
        zipCodeColumn.setCellValueFactory(new PropertyValueFactory<>("Zip-Code"));

        data = FXCollections.observableArrayList(customerDtoList);
        customerDtoTableView.setItems(data);
        //customerDtoTableView.getItems().addAll(customerDtoList);

        customerDtoTableView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                System.out.println("newSelection = " + newSelection);
                this.selectedCustomerDto = newSelection;
                setSelectedDataToUpdateForm();
            }
        });
    }

    @FXML
    void initialize() {
        setGender();
        loadDateTable();
        deleteButton.setOnAction(this::deleteCustomerAction);
        updateButton.setOnAction(this::updateCustomerAction);
        addBookingButton.setOnAction(this::loadBookingDialog);
    }

    @FXML
    private void loadBookingDialog(ActionEvent event) {
        Scene scene = new Scene(fxWeaver.loadView(BookRoomController.class));
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setScene(scene);
        stage.showAndWait();
    }

    private void setSelectedDataToUpdateForm() {
        if (this.selectedCustomerDto != null) {
            nameField.setText(selectedCustomerDto.getFirstName());
            surnameField.setText(selectedCustomerDto.getLastName());
            ageField.setText(String.valueOf(selectedCustomerDto.getAge()));
            countryField.setText(selectedCustomerDto.getCountry());
            cityField.setText(selectedCustomerDto.getCity());
            streetField.setText(selectedCustomerDto.getStreet());
            zipCodeField.setText(selectedCustomerDto.getZipCode());
        }
    }

    private void updateCustomerAction(ActionEvent event) {
        if (validateAndBuildCustomerData()) {
            try {
                System.out.println("selectedCustomerDto = " + selectedCustomerDto);
                customerService.saveOrUpdate(selectedCustomerDto);
                selectedCustomerDto = null;
                data.clear();
                resetUpdateForm();
                loadDateTable();
            } catch (RecordNotFoundException e) {
                e.printStackTrace();
                errorTextForm.setText("RecordNotFoundException");
                showAlert(Alert.AlertType.ERROR, updateButton.getScene().getWindow(), "Error!", errorTextForm.getText());
            }
        }
    }


    private void resetUpdateForm() {
        nameField.setText(null);
        surnameField.setText(null);
        ageField.setText(null);
        genderComboBox.setItems(null);
        countryField.setText(null);
        cityField.setText(null);
        streetField.setText(null);
        zipCodeField.setText(null);
        errorTextForm.setText(null);
    }

    private boolean validateAndBuildCustomerData() {

        if (this.selectedCustomerDto == null){
            errorTextForm.setText("customer is not selected");
            showAlert(Alert.AlertType.WARNING, updateButton.getScene().getWindow(), "Warning!", errorTextForm.getText());
            return false;
        }

        String name = this.nameField.getText();
        if (name.trim().length() == 0) {
            errorTextForm.setText("Name is not valid");
            showAlert(Alert.AlertType.WARNING, updateButton.getScene().getWindow(), "Warning!", errorTextForm.getText());
            return false;
        }
        selectedCustomerDto.setFirstName(name);

        String surname = this.surnameField.getText();
        if (surname.trim().length() == 0) {
            errorTextForm.setText("Surname is not valid");
            showAlert(Alert.AlertType.WARNING, updateButton.getScene().getWindow(), "Warning!", errorTextForm.getText());
            return false;
        }
        selectedCustomerDto.setLastName(surname);

        String age = this.ageField.getText();
        if (age.trim().length() == 0) {
            errorTextForm.setText("Age is not valid");
            showAlert(Alert.AlertType.WARNING, updateButton.getScene().getWindow(), "Warning!", errorTextForm.getText());
            return false;
        }
        try {
            selectedCustomerDto.setAge(Integer.parseInt(age));
        } catch (NumberFormatException e) {
            errorTextForm.setText("Age is not valid");
            showAlert(Alert.AlertType.WARNING, updateButton.getScene().getWindow(), "Warning!", errorTextForm.getText());
            return false;
        }

        Gender gender = this.genderComboBox.getValue();
        if (gender == null) {
            errorTextForm.setText("Gender is not valid");
            showAlert(Alert.AlertType.WARNING, updateButton.getScene().getWindow(), "Warning!", errorTextForm.getText());
            return false;
        }
        selectedCustomerDto.setGender(gender);

        String country = this.countryField.getText();
        if (country.trim().length() == 0) {
            errorTextForm.setText("Country is not valid");
            showAlert(Alert.AlertType.WARNING, updateButton.getScene().getWindow(), "Warning!", errorTextForm.getText());
            return false;
        }
        selectedCustomerDto.setCountry(country);

        String city = this.cityField.getText();
        if (city.trim().length() == 0) {
            errorTextForm.setText("City is not valid");
            showAlert(Alert.AlertType.WARNING, updateButton.getScene().getWindow(), "Warning!", errorTextForm.getText());
            return false;
        }
        selectedCustomerDto.setCity(city);

        String street = this.streetField.getText();
        if (street.trim().length() == 0) {
            errorTextForm.setText("Street is not valid");
            showAlert(Alert.AlertType.WARNING, updateButton.getScene().getWindow(), "Warning!", errorTextForm.getText());
            return false;
        }
        selectedCustomerDto.setStreet(street);

        String zipCode = this.zipCodeField.getText();
        if (zipCode.trim().length() == 0) {
            errorTextForm.setText("Zip code is not valid");
            showAlert(Alert.AlertType.WARNING, updateButton.getScene().getWindow(), "Warning!", errorTextForm.getText());
            return false;
        }
        selectedCustomerDto.setZipCode(zipCode);

        return true;
    }

    private void setGender() {
        genderComboBox.getItems().add(Gender.FEMALE);
        genderComboBox.getItems().add(Gender.MALE);
        genderComboBox.getItems().add(Gender.NON_BINARY);
    }

    private void deleteCustomerAction(ActionEvent event) {
        try {
            if (selectedCustomerDto != null) {
                customerService.deleteById(selectedCustomerDto.getId());
                selectedCustomerDto = null;
                data.clear();
                loadDateTable();
                resetUpdateForm();
            } else {
                errorTextTable.setText("data is not selected");
                showAlert(Alert.AlertType.ERROR, deleteButton.getScene().getWindow(), "Warning!", errorTextTable.getText());
            }
        } catch (RecordNotFoundException e) {
            e.printStackTrace();
            errorTextTable.setText("RecordNotFoundException");
            showAlert(Alert.AlertType.ERROR, deleteButton.getScene().getWindow(), "Error!", errorTextTable.getText());
        }
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


