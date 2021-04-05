package se.scandium.hotelproject.controller.fxml;

import com.jfoenix.controls.JFXButton;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.springframework.stereotype.Controller;

import static se.scandium.hotelproject.controller.util.FXMLResources.*;

import se.scandium.hotelproject.controller.util.SceneSwapper;
import se.scandium.hotelproject.entity.User;

import java.io.IOException;


@Controller
public class AdminController {

    private User viewObject;

    @FXML
    private JFXButton addRoomButton;


    @FXML
    void initialize() {
        addRoomButton.setOnAction(this::loadAddRoomControlInDialog);
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
            AddRoomController dialogController = fxmlLoader.<AddRoomController>getController();
            //dialogController.setAppMainObservableList(tvObservableList);

            Scene scene = new Scene(parent, 600, 400);
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(scene);
            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadAddRoomControl(ActionEvent actionEvent) {
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
