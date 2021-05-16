package se.scandium.hotelproject.controller.fxml;

import com.jfoenix.controls.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ToggleGroup;
import javafx.scene.text.Text;
import javafx.stage.Window;
import net.rgielen.fxweaver.core.FxWeaver;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import se.scandium.hotelproject.controller.fxml.singleton.HotelHolder;
import se.scandium.hotelproject.dto.*;
import se.scandium.hotelproject.entity.*;
import se.scandium.hotelproject.exception.RecordNotFoundException;
import se.scandium.hotelproject.exception.UserNotFoundException;
import se.scandium.hotelproject.service.RoomService;
import se.scandium.hotelproject.service.UserService;

@Component
@FxmlView("/fxml/add_user.fxml")
public class UserController {

    private UserService userService;
    private final FxWeaver fxWeaver;
    private UserDto userDto;
    private UserInfoDto userInfoDto;

    @Autowired
    public UserController(FxWeaver fxWeaver, UserService userService) {
        this.fxWeaver = fxWeaver;
        this.userService = userService;
    }
    @FXML
    private JFXTextField UserNameField;
    @FXML
    private JFXTextField FirstNameField;
    @FXML
    private JFXTextField LastNameField;
    @FXML
    private JFXComboBox<UserType> UserTypeComboBox;
    @FXML
    private JFXButton addButton;
    @FXML
    private Text errorText;

    @FXML
    void initialize() {
        userDto = new UserDto();
        userInfoDto = new UserInfoDto();
        setUserType();
        addButton.setOnAction(this::addUserAction);
    }

    private void addUserAction(ActionEvent event) {
        if (validateAndBuildUserData()) {
            try {
                UserDto savedUserDto = userService.saveOrUpdate(userDto);
                System.out.println(savedUserDto);

            } catch (UserNotFoundException e) {
                e.printStackTrace();
                errorText.setText("UserNotFoundException");
                showAlert(Alert.AlertType.ERROR, addButton.getScene().getWindow(), "Error!", "UserNotFoundException");
            }
        }

    }

    private boolean validateAndBuildUserData() {

        String username = UserNameField.getText();
        if (username.trim().length() == 0) {
            errorText.setText("username is not Valid");
            showAlert(Alert.AlertType.WARNING, addButton.getScene().getWindow(), "Warning!", "username is not valid");
            return false;
        }
        userDto.setUsername(username);

        String firstname = FirstNameField.getText();
        if (firstname.trim().length() == 0) {
            errorText.setText("firstname is not Valid");
            showAlert(Alert.AlertType.WARNING, addButton.getScene().getWindow(), "Warning!", "firstname is not valid");
            return false;
        }
        userInfoDto.setFirstName(firstname);

        String lastName = LastNameField.getText();
        if (lastName.trim().length() == 0) {
            errorText.setText("last name is not Valid");
            showAlert(Alert.AlertType.WARNING, addButton.getScene().getWindow(), "Warning!", "last name is not valid");
            return false;
        }
        userInfoDto.setLastName(lastName);

        UserType type = this.UserTypeComboBox.getValue();
        if (type == null) {
            errorText.setText("User type is not valid");
            showAlert(Alert.AlertType.WARNING, addButton.getScene().getWindow(), "Warning!", errorText.getText());
            return false;
        }
        userInfoDto.setUserType(type);
        userDto.setUserInfo(userInfoDto);
        return true;
    }


    private void setUserType(){
        UserTypeComboBox.getItems().add(UserType.RECEPTION);
        UserTypeComboBox.getItems().add(UserType.ADMINISTRATOR);

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
