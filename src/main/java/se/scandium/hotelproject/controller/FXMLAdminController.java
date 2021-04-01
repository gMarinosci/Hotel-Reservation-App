package se.scandium.hotelproject.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import se.scandium.hotelproject.exception.UserNotFoundException;
import se.scandium.hotelproject.service.UserService;


//https://docs.oracle.com/javafx/2/get_started/fxml_tutorial.htm#CIHHGHJJ
@Component
public class FXMLAdminController {

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
    protected void handleSubmitButtonAction(ActionEvent event) {
        String username = usernameField.getText();
        String pwd = passwordField.getText();
        try {
            userService.authentication(username, pwd);
            // open welcome page
        } catch (UserNotFoundException e) {
            System.out.println("##### UserNotFoundException: " + e.getMessage());
            actionTarget.setText(e.getMessage());
        }
    }
}
