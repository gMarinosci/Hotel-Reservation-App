package se.scandium.hotelproject.controller.fxml;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.Window;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import se.scandium.hotelproject.controller.fxml.view.UserHolder;
import se.scandium.hotelproject.controller.fxml.view.UserView;
import se.scandium.hotelproject.entity.Authority;
import se.scandium.hotelproject.entity.User;
import se.scandium.hotelproject.entity.UserType;
import se.scandium.hotelproject.exception.UserNotFoundException;
import se.scandium.hotelproject.service.UserService;

import java.io.IOException;
import java.util.stream.Collectors;

import static se.scandium.hotelproject.controller.util.FXMLResources.*;


@Component
public class LoginController {

    private UserService userService;

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @FXML
    private Text errorText;

    @FXML
    private JFXTextField usernameField;

    @FXML
    private JFXPasswordField passwordField;

    @FXML
    private JFXButton loginButton;


    @FXML
    private void initialize() {
        loginButton.setOnAction(event -> {
            String username = usernameField.getText();
            String pwd = passwordField.getText();
            try {
                UserView user = userService.authentication(username, pwd);
                setUserViewToUserHolder(user);
                if (user.isActive()) {
                    if (user.getUserType() == UserType.ADMINISTRATOR.getCode())
                        loadControl(ADMIN_PANEL);
                    else
                        loadControl(RECEPTION_PANEL);
                } else
                    loadControl(RESET_PWD_SCREEN);
            } catch (UserNotFoundException e) {
                System.out.println("##### UserNotFoundException: " + e.getMessage());
                errorText.setText(e.getMessage());
                showAlert(Alert.AlertType.ERROR, loginButton.getScene().getWindow(), "Warning", e.getMessage());
            }catch (Exception e) {
                System.out.println("##### Exception: " + e.getMessage());
                errorText.setText(e.getMessage());
                showAlert(Alert.AlertType.ERROR, loginButton.getScene().getWindow(), "Internal Error!", e.getMessage());
            }
        });
    }

    private void setUserViewToUserHolder(UserView userView) {
        UserHolder holder = UserHolder.getInstance();
        holder.setUserView(userView);
    }

    private void loadControl(String fxmlName) {
        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlName));
        Parent node = null;
        try {
            node = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, loginButton.getScene().getWindow(), "Internal Error!", e.getMessage());
        }
        if (node != null) {
            switch (fxmlName) {
                case ADMIN_PANEL:
                    stage.setTitle("Admin Panel");
                    stage.setScene(new Scene(node, 1200, 800));
                    AdminController adminController = loader.getController();
                    adminController.setUserService(userService);

                    break;
                case RECEPTION_PANEL:
                    stage.setTitle("Reception panel");
                    stage.setScene(new Scene(node, 1200, 800));
                    break;
                case RESET_PWD_SCREEN:
                    stage.setTitle("Reset Password");
                    stage.setScene(new Scene(node));

                    ResetPasswordScreenController resetPasswordScreenController = loader.getController();
                    resetPasswordScreenController.setUserService(userService);
                    break;
                default:
                    showAlert(Alert.AlertType.ERROR, loginButton.getScene().getWindow(), "Internal Error!", "Internal Error!");
            }
            stage.show();
            loginButton.getScene().getWindow().hide();
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
