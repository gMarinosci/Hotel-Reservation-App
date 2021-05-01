package se.scandium.hotelproject.controller.fxml;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXHamburger;
import com.jfoenix.controls.JFXPopup;
import com.jfoenix.controls.JFXRippler;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Modality;
import javafx.stage.Stage;
import net.rgielen.fxweaver.core.FxWeaver;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import se.scandium.hotelproject.controller.fxml.singleton.UserHolder;
import se.scandium.hotelproject.controller.fxml.view.UserView;
import se.scandium.hotelproject.service.UserService;

@Component
@FxmlView("/fxml/admin_panel.fxml")
public class AdminController {

    private UserView userView;
    private final UserService userService;
    private final FxWeaver fxWeaver;

    @Autowired
    public AdminController(UserService userService, FxWeaver fxWeaver) {
        this.userService = userService;
        this.fxWeaver = fxWeaver;
    }

    @FXML
    private Label screenTitleText;
    @FXML
    private JFXButton addRoomButton;
    @FXML
    private JFXButton showRoomButton;
    @FXML
    private JFXButton addUserButton;
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
        showRoomButton.setOnAction(this::loadRoomListControlInDialog);
        addUserButton.setOnAction(this::loadUserControlInDialog);
    }

    @FXML
    void loadPopup() {
        popup = new JFXPopup(fxWeaver.loadView(PopupController.class));
        burger.setOnMouseClicked((e) -> popup.show(rippler, JFXPopup.PopupVPosition.TOP, JFXPopup.PopupHPosition.RIGHT));
    }

    @FXML
    void loadAddRoomControlInDialog(ActionEvent event) {
        Scene scene = new Scene(fxWeaver.loadView(RoomController.class));
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setScene(scene);
        stage.showAndWait();
    }

    @FXML
    void loadRoomListControlInDialog(ActionEvent event) {
        Scene scene = new Scene(fxWeaver.loadView(RoomListController.class));
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setScene(scene);
        stage.showAndWait();
    }

    @FXML
    void loadUserControlInDialog(ActionEvent event) {
        Scene scene = new Scene(fxWeaver.loadView(UserController.class));
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setScene(scene);
        stage.showAndWait();
    }

}
