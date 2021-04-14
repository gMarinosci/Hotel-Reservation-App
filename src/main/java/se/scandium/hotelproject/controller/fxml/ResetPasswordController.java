package se.scandium.hotelproject.controller.fxml;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.PasswordField;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import se.scandium.hotelproject.controller.fxml.view.UserHolder;
import se.scandium.hotelproject.controller.fxml.view.UserView;
import se.scandium.hotelproject.entity.User;
import se.scandium.hotelproject.entity.UserType;
import se.scandium.hotelproject.exception.ArgumentInvalidException;
import se.scandium.hotelproject.exception.UserNotFoundException;
import se.scandium.hotelproject.service.UserService;

import java.io.IOException;

import static se.scandium.hotelproject.controller.util.FXMLResources.ADMIN_PANEL;
import static se.scandium.hotelproject.controller.util.FXMLResources.RECEPTION_PANEL;

@Controller
public class ResetPasswordController {

    private UserView userView;
    private UserService userService;

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @FXML
    private Text errorText;

    @FXML
    private Text username;

    @FXML
    private JFXPasswordField passwordField;

    @FXML
    private JFXPasswordField rePasswordField;

    @FXML
    private JFXButton submitButton;

    @FXML
    private void initialize() {
        userView = UserHolder.getInstance().getUserView();
        username.setText(userView.getUsername());

        submitButton.setOnAction(event -> {
            String password = passwordField.getText();
            String rePassword = rePasswordField.getText();
            String username = userView.getUsername();
            boolean isValid = true;
            if (password.trim().length() == 0 || rePassword.trim().length() == 0) {
                errorText.setText("invalid param.");
                isValid = false;
            }
            if (!password.equals(rePassword)) {
                errorText.setText("Password and Re-Password must be same");
                isValid = false;

            }
            if (isValid) {
                try {
                    userService.resetPassword(username, password, rePassword);
                    if (userView.getUserType() == UserType.ADMINISTRATOR.getCode())
                        loadControl(ADMIN_PANEL);
                    else
                        loadControl(RECEPTION_PANEL);

                } catch (ArgumentInvalidException e) {
                    System.out.println("##### ArgumentInvalidException: " + e.getMessage());
                    errorText.setText(e.getMessage());
                } catch (UserNotFoundException e) {
                    System.out.println("##### UserNotFoundException: " + e.getMessage());
                    errorText.setText(e.getMessage());
                } catch (Exception e) {
                    System.out.println("##### Exception: " + e.getMessage());
                    e.printStackTrace();
                    showErrorAlert("INTERNAL_ERROR");
                }
            }

        });
    }

    private void loadControl(String fxmlName) {
        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlName));
        Parent node = null;
        try {
            node = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
            showErrorAlert(e.getMessage());
        }
        if (node != null) {
            Scene scene = new Scene(node);
            stage.setScene(scene);
            stage.show();
            submitButton.getScene().getWindow().hide();
        }
    }


    private void showErrorAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR, message, ButtonType.CLOSE);
        alert.setTitle("Error");
        alert.show();
    }


}
