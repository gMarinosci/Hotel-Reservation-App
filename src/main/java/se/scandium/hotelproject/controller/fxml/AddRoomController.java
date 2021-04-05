package se.scandium.hotelproject.controller.fxml;

import com.jfoenix.controls.JFXTextField;
import javafx.fxml.FXML;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import org.springframework.stereotype.Controller;

@Controller
public class AddRoomController {

    @FXML
    private AnchorPane formRoot;

    @FXML
    private JFXTextField roomNameField;

    @FXML
    private JFXTextField roomTestField;

    @FXML
    void onReset(MouseEvent event) {
        if (event.getButton() == MouseButton.PRIMARY) {
            roomNameField.clear();
            roomTestField.clear();
        }
    }

    @FXML
    void onSubmit(MouseEvent event) {

    }

    @FXML
    void toPersonControl(MouseEvent event) {

    }

    @FXML
    void initialize() {

    }


}
