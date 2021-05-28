package se.scandium.hotelproject.controller.fxml;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.Window;
import net.rgielen.fxweaver.core.FxWeaver;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import se.scandium.hotelproject.controller.fxml.singleton.UserHolder;
import se.scandium.hotelproject.controller.fxml.view.UserView;
import se.scandium.hotelproject.entity.UserType;
import se.scandium.hotelproject.exception.UserNotFoundException;
import se.scandium.hotelproject.service.UserService;

import static se.scandium.hotelproject.controller.util.FXMLResources.*;


@Component
@FxmlView("/fxml/login.fxml")
public class LoginController {

    private final UserService userService;
    private final FxWeaver fxWeaver;

    @Autowired
    public LoginController(UserService userService, FxWeaver fxWeaver) {
        this.userService = userService;
        this.fxWeaver = fxWeaver;
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
        loginButton.setDefaultButton(true);
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
            } catch (Exception e) {
                System.out.println("##### Exception: " + e.getMessage());
                e.printStackTrace();
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
        switch (fxmlName) {
            case ADMIN_PANEL:
                stage.setTitle("Admin Panel");
                stage.setScene(new Scene(fxWeaver.loadView(AdminController.class), 1200, 800));
                break;
            case RECEPTION_PANEL:
                stage.setTitle("Reception panel");
                stage.setScene(new Scene(fxWeaver.loadView(ReceptionController.class), 1200, 800));
                break;
            case RESET_PWD_SCREEN:
                stage.setTitle("Reset Password");
                stage.setScene(new Scene(fxWeaver.loadView(ResetPasswordScreenController.class)));
                break;
            default:
                showAlert(Alert.AlertType.ERROR, loginButton.getScene().getWindow(), "Internal Error!", "Internal Error!");
        }
        stage.show();
        loginButton.getScene().getWindow().hide();

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
