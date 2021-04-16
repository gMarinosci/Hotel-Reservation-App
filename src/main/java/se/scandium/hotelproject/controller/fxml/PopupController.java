package se.scandium.hotelproject.controller.fxml;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import se.scandium.hotelproject.service.UserService;

import java.io.IOException;

import static se.scandium.hotelproject.controller.util.FXMLResources.RESET_PWD_SCREEN;

@Component
public class PopupController {

    UserService userService;

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @FXML
    void exitClickHandler() {
        Platform.exit();
    }

    @FXML
    void resetPWDClickHandler() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(RESET_PWD_SCREEN));
            Parent node = fxmlLoader.load();
            Scene scene = new Scene(node);

            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            ResetPasswordScreenController resetPasswordScreenController = fxmlLoader.getController();
            resetPasswordScreenController.setUserService(userService);
            resetPasswordScreenController.setResetFirstStep(false);

            stage.setScene(scene);
            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
