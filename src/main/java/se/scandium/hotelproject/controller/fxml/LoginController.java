package se.scandium.hotelproject.controller.fxml;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import se.scandium.hotelproject.controller.fxml.view.UserHolder;
import se.scandium.hotelproject.controller.fxml.view.UserView;
import se.scandium.hotelproject.entity.Authority;
import se.scandium.hotelproject.entity.User;
import se.scandium.hotelproject.entity.UserType;
import se.scandium.hotelproject.exception.UserNotFoundException;
import se.scandium.hotelproject.service.UserService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.stream.Collectors;

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
               User user = userService.authentication(username, pwd);
               setUserView(user);
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

    private void setUserView(User user){
        UserHolder holder = UserHolder.getInstance();
        UserView userView= new UserView();
        userView.setUsername(user.getUsername());
        userView.setActive(user.isActive());
        userView.setFirstName(user.getUserInfo().getFirstName());
        userView.setLastName(user.getUserInfo().getLastName());
        userView.setUserType(user.getUserInfo().getUserType().getCode());
        userView.setAuthorities(user.getAuthorities().stream().map(Authority::getId).collect(Collectors.toList()));
        holder.setUserView(userView);
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
                case ADMIN_PANEL:
                    stage.setTitle("Admin Panel");
                    stage.setScene(new Scene(node, 1200, 800));
                    break;
                case RECEPTION_PANEL:
                    stage.setTitle("Reception panel");
                    stage.setScene(new Scene(node, 1200, 800));
                    break;
                case RESET_PWD_PANEL:
                    stage.setTitle("Reset Password");
                    stage.setScene(new Scene(node, 500, 350));

                    ResetPasswordController resetPasswordController = loader.getController();
                    resetPasswordController.setUserService(userService);
                    break;

                default: showErrorAlert("INTERNAL_ERROR");
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
