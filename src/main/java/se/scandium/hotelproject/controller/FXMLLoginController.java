package se.scandium.hotelproject.controller;

import com.jfoenix.controls.JFXButton;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import se.scandium.hotelproject.controller.util.SceneSwapper;
import se.scandium.hotelproject.entity.User;
import se.scandium.hotelproject.entity.UserType;
import se.scandium.hotelproject.exception.UserNotFoundException;
import se.scandium.hotelproject.service.UserService;

import java.awt.event.MouseEvent;

import static se.scandium.hotelproject.controller.util.FXMLResources.*;


//https://docs.oracle.com/javafx/2/get_started/fxml_tutorial.htm#CIHHGHJJ
@Component
public class FXMLLoginController {

    UserService userService;

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @FXML
    private Text actionTarget;

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Button loginButton;

    @FXML
    private JFXButton testJFX;

    @FXML
    private void initialize() {
        loginButton.setOnAction(event -> {
            String username = usernameField.getText();
            String pwd = passwordField.getText();
            try {
                User user = userService.authentication(username, pwd);
                // open welcome page
                if (user.getUserInfo().getUserType() == UserType.ADMINISTRATOR)
                    loadAdminControl(event);
                else
                    loadReceptionControl(event);
            } catch (UserNotFoundException e) {
                System.out.println("##### UserNotFoundException: " + e.getMessage());
                actionTarget.setText(e.getMessage());
            }
        });
    }

    private void loadAdminControl(ActionEvent actionEvent) {
        Platform.runLater(() -> SceneSwapper.getInstance().swapScene(ADMIN_PANEL, loginButton.getScene().getWindow()));
    }

    private void loadReceptionControl(ActionEvent actionEvent) {
        Platform.runLater(() -> SceneSwapper.getInstance().swapScene(RECEPTION_PANEL, loginButton.getScene().getWindow()));
    }
}
