package se.scandium.hotelproject.controller.fxml;

import com.jfoenix.controls.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.text.Text;
import javafx.stage.Window;
import lombok.SneakyThrows;
import net.rgielen.fxweaver.core.FxWeaver;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import se.scandium.hotelproject.dto.AddressDto;
import se.scandium.hotelproject.dto.CustomerDto;
import se.scandium.hotelproject.entity.Gender;
import se.scandium.hotelproject.exception.RecordNotFoundException;
import se.scandium.hotelproject.service.CustomerService;

@Component

@FxmlView("/fxml/add_customer.fxml")
public class CustomerController {

    private CustomerService customerService;
    private final FxWeaver fxWeaver;
    private CustomerDto customerDto;
    private AddressDto addressDto;

    @Autowired
    public CustomerController(FxWeaver fxWeaver, CustomerService customerService) {
        this.fxWeaver = fxWeaver;
        this.customerService = customerService;
    }

    @FXML
    private JFXTextField NameField;
    @FXML
    private JFXTextField SurnameField;
    @FXML
    private JFXTextField AgeField;
    @FXML
    private JFXComboBox<Gender> GenderComboBox;
    @FXML
    private JFXTextField CountryField;
    @FXML
    private JFXTextField CityField;
    @FXML
    private JFXTextField StreetField;
    @FXML
    private JFXTextField Zip_codeField;
    @FXML
    private JFXButton addButton;
    @FXML
    private Text errorText;

    @FXML
    void initialize() {
        customerDto = new CustomerDto();
        addressDto =  new AddressDto();
        setGender();
        addButton.setOnAction(this::addCustomerAction);
    }

    private void addCustomerAction(ActionEvent event)  {
        if (validateAndBuildCustomerData()) {
            try {
                System.out.println("customerDto = " + customerDto);
                customerService.saveOrUpdate(customerDto);
                addButton.getScene().getWindow().hide();
            } catch (RecordNotFoundException e) {
                e.printStackTrace();
                errorText.setText("RecordNotFoundException");
                showAlert(Alert.AlertType.ERROR, addButton.getScene().getWindow(), "Error!", errorText.getText());
            }
            /*System.out.println("CustomerDto = " + customerDto);
            customerService.saveOrUpdate(customerDto);
            addButton.getScene().getWindow().hide();*/
        }

    }

    private boolean validateAndBuildCustomerData() {
        String name = this.NameField.getText();
        if (name.trim().length() == 0) {
            errorText.setText("name is not Valid");
            showAlert(Alert.AlertType.WARNING, addButton.getScene().getWindow(), "Warning!", "name is not valid");
            return false;
        }
        customerDto.setFirstName(name);

        String surname = this.SurnameField.getText();
        if (surname.trim().length() == 0) {
            errorText.setText("surname is not Valid");
            showAlert(Alert.AlertType.WARNING, addButton.getScene().getWindow(), "Warning!", "surname is not valid");
            return false;
        }
        customerDto.setLastName(surname);

        String age = this.AgeField.getText();
        if (age.trim().length() == 0) {
            errorText.setText("age is not Valid");
            showAlert(Alert.AlertType.WARNING, addButton.getScene().getWindow(), "Warning!", "age is not valid");
            return false;
        }
        try {
            customerDto.setAge(Integer.parseInt(age));
        }catch (NumberFormatException e){
            errorText.setText("age is not valid");
            showAlert(Alert.AlertType.WARNING, addButton.getScene().getWindow(), "Warning!", errorText.getText());
            return false;
        }

        Gender type = this.GenderComboBox.getValue();
        if (type == null) {
            errorText.setText("Gender is not valid");
            showAlert(Alert.AlertType.WARNING, addButton.getScene().getWindow(), "Warning!", errorText.getText());
            return false;
        }
        customerDto.setGender(type);

        String country = this.CountryField.getText();
        if (country.trim().length() == 0) {
            errorText.setText("country is not Valid");
            showAlert(Alert.AlertType.WARNING, addButton.getScene().getWindow(), "Warning!", "country is not valid");
            return false;
        }
        addressDto.setCountry(country);

        String city = this.CityField.getText();
        if (city.trim().length() == 0) {
            errorText.setText("city is not Valid");
            showAlert(Alert.AlertType.WARNING, addButton.getScene().getWindow(), "Warning!", "city is not valid");
            return false;
        }
        addressDto.setCity(city);

        String street = this.StreetField.getText();
        if (street.trim().length() == 0) {
            errorText.setText("street is not Valid");
            showAlert(Alert.AlertType.WARNING, addButton.getScene().getWindow(), "Warning!", "street is not valid");
            return false;
        }
        addressDto.setStreet(street);

        String zip_code = this.Zip_codeField.getText();
        if (zip_code.trim().length() == 0) {
            errorText.setText("zip code is not Valid");
            showAlert(Alert.AlertType.WARNING, addButton.getScene().getWindow(), "Warning!", "zip code is not valid");
            return false;
        }
        addressDto.setZipCode(zip_code);

        customerDto.setAddressDto(addressDto);
        return true;

    }



    private void setGender() {
        GenderComboBox.getItems().add(Gender.MALE);
        GenderComboBox.getItems().add(Gender.FEMALE);
        GenderComboBox.getItems().add(Gender.NON_BINARY);

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
