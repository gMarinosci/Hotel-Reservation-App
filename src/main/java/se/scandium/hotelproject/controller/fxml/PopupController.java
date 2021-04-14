package se.scandium.hotelproject.controller.fxml;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.springframework.stereotype.Controller;

import java.io.IOException;

import static se.scandium.hotelproject.controller.util.FXMLResources.RESET_PWD_SCREEN;

@Controller
public class PopupController {

    @FXML
    void exitClickHandler() {
        Platform.exit();
    }


    @FXML
    void resetPWDClickHandler() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(RESET_PWD_SCREEN));
            Parent parent = fxmlLoader.load();
            Scene scene = new Scene(parent);
            Stage stage = new Stage();

            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(scene);
            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
