package se.scandium.hotelproject.controller.fxml;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import javafx.fxml.FXML;
import javafx.scene.text.Text;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import se.scandium.hotelproject.controller.fxml.view.UserHolder;
import se.scandium.hotelproject.service.UserService;

@Controller
public class ResetPasswordScreenController {

    UserService userService;
    private String username;
    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @FXML
    private Text usernameText;

    @FXML
    private JFXTextField screenTitle;

    @FXML
    private JFXPasswordField oldPassword;

    @FXML
    private JFXPasswordField newPassword;

    @FXML
    private JFXPasswordField reNewPassword;

    @FXML
    private JFXButton submit;

    @FXML
    private Text errorMessage;

    @FXML
    public void initialize() {
        username = UserHolder.getInstance().getUserView().getUsername();
        usernameText.setText(username);
        submit.setOnAction(event -> System.out.println("OK "+ username));
    }

}
