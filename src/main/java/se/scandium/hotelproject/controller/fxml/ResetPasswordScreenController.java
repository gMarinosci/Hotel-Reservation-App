package se.scandium.hotelproject.controller.fxml;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
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
import se.scandium.hotelproject.exception.ArgumentInvalidException;
import se.scandium.hotelproject.exception.UserNotFoundException;
import se.scandium.hotelproject.service.UserService;

import static se.scandium.hotelproject.controller.util.FXMLResources.ADMIN_PANEL;
import static se.scandium.hotelproject.controller.util.FXMLResources.RECEPTION_PANEL;

@Component
@FxmlView("/fxml/reset_pwd_screen.fxml")
public class ResetPasswordScreenController {

    private UserView userView;
    private final UserService userService;
    private final FxWeaver fxWeaver;
    private boolean resetFirstStep = true;

    @Autowired
    public ResetPasswordScreenController(UserService userService, FxWeaver fxWeaver) {
        this.userService = userService;
        this.fxWeaver = fxWeaver;
    }

    public void setResetFirstStep(boolean resetFirstStep) {
        this.resetFirstStep = resetFirstStep;
    }

    @FXML
    private Text usernameText;

    @FXML
    private JFXTextField screenTitleField;

    @FXML
    private JFXPasswordField currentPasswordField;

    @FXML
    private JFXPasswordField newPasswordField;

    @FXML
    private JFXPasswordField reNewPasswordField;

    @FXML
    private JFXButton submitButton;

    @FXML
    private Text errorText;

    @FXML
    public void initialize() {
        userView = UserHolder.getInstance().getUserView();
        usernameText.setText(userView.getUsername());
        screenTitleField.setText(userView.getScreenTitle());
        submitButton.setOnAction(this::resetPasswordScreenAction);
    }

    private void resetPasswordScreenAction(ActionEvent actionEvent) {
        if (formValidation()) {
            try {
                userService.authentication(userView.getUsername(), currentPasswordField.getText());
                userService.resetPasswordUpdateScreen(userView.getUsername(), currentPasswordField.getText(), newPasswordField.getText(), screenTitleField.getText());
                userView.setScreenTitle(screenTitleField.getText());
                if (resetFirstStep)
                    if (userView.getUserType() == UserType.ADMINISTRATOR.getCode())
                        loadControl(ADMIN_PANEL);
                    else
                        loadControl(RECEPTION_PANEL);
                else {
                    System.exit(0);
                }

            } catch (ArgumentInvalidException e) {
                System.out.println("##### ArgumentInvalidException: " + e.getMessage());
                errorText.setText(e.getMessage());
                showAlert(Alert.AlertType.WARNING, submitButton.getScene().getWindow(), "Error!", errorText.getText());
            } catch (UserNotFoundException e) {
                System.out.println("##### UserNotFoundException: " + e.getMessage());
                errorText.setText(e.getMessage());
                showAlert(Alert.AlertType.ERROR, submitButton.getScene().getWindow(), "Warning", e.getMessage());
            } catch (Exception e) {
                System.out.println("##### Exception: " + e.getMessage());
                e.printStackTrace();
                showAlert(Alert.AlertType.ERROR, submitButton.getScene().getWindow(), "Internal Error!", "Internal Error!");
            }
        }
    }


    private boolean formValidation() {
        String screenTitle = screenTitleField.getText();
        String currentPassword = currentPasswordField.getText();
        String newPassword = newPasswordField.getText();
        String reNewPassword = reNewPasswordField.getText();
        if (screenTitle.trim().length() == 0) {
            errorText.setText("Please enter screen title");
            showAlert(Alert.AlertType.ERROR, submitButton.getScene().getWindow(), "Validation Error!", "Please enter screen title");
            return false;
        }
        if (currentPassword.trim().length() == 0) {
            errorText.setText("Please enter current password");
            showAlert(Alert.AlertType.ERROR, submitButton.getScene().getWindow(), "Validation Error!", "Please enter current password");
            return false;
        }
        if (newPassword.trim().length() == 0) {
            errorText.setText("Please enter new password");
            showAlert(Alert.AlertType.ERROR, submitButton.getScene().getWindow(), "Validation Error!", "Please enter new password");
            return false;
        }

        if (reNewPassword.trim().length() == 0) {
            errorText.setText("Please enter repeat new password");
            showAlert(Alert.AlertType.ERROR, submitButton.getScene().getWindow(), "Validation Error!", "Please enter repeat new password");
            return false;
        }

        if (!newPassword.equals(reNewPassword)) {
            errorText.setText("password and repeat password are not same");
            showAlert(Alert.AlertType.ERROR, submitButton.getScene().getWindow(), "Validation Error!", "password and repeat password are not same");
            return false;
        }
        return true;
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
            default:
                showAlert(Alert.AlertType.ERROR, submitButton.getScene().getWindow(), "Internal Error!", "Internal Error!");
        }
        stage.show();
        submitButton.getScene().getWindow().hide();
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
