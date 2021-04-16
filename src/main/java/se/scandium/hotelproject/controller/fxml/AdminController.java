package se.scandium.hotelproject.controller.fxml;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXHamburger;
import com.jfoenix.controls.JFXPopup;
import com.jfoenix.controls.JFXRippler;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import se.scandium.hotelproject.controller.fxml.view.UserHolder;
import se.scandium.hotelproject.controller.fxml.view.UserView;
import se.scandium.hotelproject.service.UserService;

import java.io.IOException;

import static se.scandium.hotelproject.controller.util.FXMLResources.ADD_ROOM;
import static se.scandium.hotelproject.controller.util.FXMLResources.DEMO_POPUP;

@Component
public class AdminController {

    private UserService userService;
    private UserView userView;

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @FXML
    private Label screenTitleText;
    @FXML
    private JFXButton addRoomButton;
    @FXML
    private JFXRippler rippler;
    @FXML
    private JFXHamburger burger;

    private JFXPopup popup;


    @FXML
    void initialize() {
        userView = UserHolder.getInstance().getUserView();
        screenTitleText.setText(userView.getScreenTitle());
        addRoomButton.setOnAction(this::loadAddRoomControlInDialog);
    }

    @FXML
    void loadPopup() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(DEMO_POPUP));
            popup = new JFXPopup(fxmlLoader.load());
            PopupController popupController = fxmlLoader.getController();
            popupController.setUserService(userService);
            burger.setOnMouseClicked((e) -> popup.show(rippler, JFXPopup.PopupVPosition.TOP, JFXPopup.PopupHPosition.RIGHT));
        } catch (IOException ioExc) {
            ioExc.printStackTrace();
        }
    }

    @FXML
    void loadAddRoomControlInDialog(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(ADD_ROOM));
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
