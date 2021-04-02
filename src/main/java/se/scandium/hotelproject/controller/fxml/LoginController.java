package se.scandium.hotelproject.controller.fxml;

import com.jfoenix.controls.JFXButton;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import se.scandium.hotelproject.entity.User;
import se.scandium.hotelproject.entity.UserType;
import se.scandium.hotelproject.exception.UserNotFoundException;
import se.scandium.hotelproject.service.UserService;

import java.io.IOException;

import static se.scandium.hotelproject.controller.util.FXMLResources.*;


//https://docs.oracle.com/javafx/2/get_started/fxml_tutorial.htm#CIHHGHJJ
@Controller
public class LoginController {

    private UserService userService;

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @FXML
    private Text errorText;

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
                if (user.isActive()) {
                    if (user.getUserInfo().getUserType() == UserType.ADMINISTRATOR)
                        loadControl(ADMIN_PANEL,user);
                    else
                        loadControl(RECEPTION_PANEL,user);
                } else
                    loadControl(RESET_PWD_PANEL,user);
            } catch (UserNotFoundException e) {
                System.out.println("##### UserNotFoundException: " + e.getMessage());
                errorText.setText(e.getMessage());
                showWarningAlert(e.getMessage());
            }
        });
    }


    private void loadControl(String fxmlName,User user) {
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
            switch (fxmlName) {
                case ADMIN_PANEL -> {
                    stage.setTitle("Admin Panel");
                    stage.setScene(new Scene(node, 1200, 800));
                    //stage.setFullScreen(true);

                    AdminController adminController = loader.getController();
                    adminController.setViewObject(user);
                }
                case RECEPTION_PANEL -> {
                    stage.setTitle("Reception panel");
                    stage.setScene(new Scene(node, 1200, 800));

                    ReceptionController receptionController = loader.getController();
                    receptionController.setViewObject(user);
                }
                case RESET_PWD_PANEL -> {
                    stage.setTitle("Reset Password");
                    stage.setScene(new Scene(node, 500, 350));

                    ResetPasswordController resetPasswordController = loader.getController();
                    resetPasswordController.setUserService(userService);
                    resetPasswordController.setViewObject(user);
                }
                default -> showErrorAlert("INTERNAL_ERROR");
            }
            stage.show();
            loginButton.getScene().getWindow().hide();

        }
    }

    private void showWarningAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING, message, ButtonType.CLOSE);
        alert.setTitle("Warning");
        alert.show();
    }

    private void showErrorAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR, message, ButtonType.CLOSE);
        alert.setTitle("Error");
        alert.show();
    }
}
