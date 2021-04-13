package se.scandium.hotelproject.controller.fxml;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXHamburger;
import com.jfoenix.controls.JFXPopup;
import com.jfoenix.controls.JFXRippler;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.springframework.stereotype.Controller;
import se.scandium.hotelproject.entity.User;

import javax.annotation.PostConstruct;
import java.io.IOException;

import static se.scandium.hotelproject.controller.util.FXMLResources.ADD_ROOM;
import static se.scandium.hotelproject.controller.util.FXMLResources.DEMO_POPUP;

@Controller
public class AdminController {

    private User viewObject;

    @FXML
    private JFXButton addRoomButton;

    @FXML
    private JFXRippler rippler;
    @FXML
    private JFXHamburger burger;

    private JFXPopup popup;


    @FXML
    void initialize() {
        addRoomButton.setOnAction(this::loadAddRoomControlInDialog);

        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(DEMO_POPUP));
            popup = new JFXPopup(fxmlLoader.load());
        } catch (IOException ioExc) {
            ioExc.printStackTrace();
        }
        burger.setOnMouseClicked((e) -> popup.show(rippler, JFXPopup.PopupVPosition.TOP, JFXPopup.PopupHPosition.RIGHT));
    }

    public void setViewObject(User viewObject) {
        if (viewObject != null) {
        }
        this.viewObject = viewObject;
    }


    @FXML
    void loadAddRoomControlInDialog(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(ADD_ROOM));
            Parent parent = fxmlLoader.load();
            //RoomController dialogController = fxmlLoader.getController();
            //dialogController.setAppMainObservableList(tvObservableList);

            Scene scene = new Scene(parent);
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(scene);
            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadAddRoomControl(MouseEvent actionEvent) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource(ADD_ROOM));
            Scene dashboard = new Scene(root);
            //This line gets the Stage Information
            Stage window = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            window.setScene(dashboard);
            window.show();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }


}
