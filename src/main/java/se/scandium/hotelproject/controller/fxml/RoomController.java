package se.scandium.hotelproject.controller.fxml;

import com.jfoenix.controls.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ToggleGroup;
import org.springframework.stereotype.Controller;

import java.awt.*;

@Controller
public class RoomController {
    @FXML
    private JFXTextField name;
    @FXML
    private JFXTextField price;
    @FXML
    private JFXComboBox<String> type;
    @FXML
    private JFXTextField hotel;
    @FXML
    private JFXRadioButton reserveY;
    @FXML
    private JFXRadioButton reserveN;
    @FXML
    private JFXTextField test;
    @FXML
    private JFXTextArea description;
    @FXML
    private JFXButton addButton;

    @FXML
    private ToggleGroup group;
    @FXML
    void initialize() {
        addButton.setOnAction(this::addRoomAction);
        type.getItems().add("Single");
        type.getItems().add("Double");
        type.getItems().add("Triple");
        type.getItems().add("Quad");
        type.getItems().add("Queen");
        type.getItems().add("King");
        hotel.setText("HOTEL NAME");
        description= new JFXTextArea("");
    }

    private void addRoomAction(ActionEvent event) {
        if (validateRoom())
            addButton.getScene().getWindow().hide();
        group = new ToggleGroup();
        reserveY.setToggleGroup(group);
        reserveN.setToggleGroup(group);
    }

    private boolean validateRoom() {
        String name = this.name.getText();
        String price = this.price.getText();
        String type = this.type.getTypeSelector();
        String hotel = this.hotel.getText();
        String reserveY = this.reserveY.getText();
        String reserveN = this.reserveN.getText();
        String location = this.test.getText();
        String description = this.description.getText();
        return true;
    }

}
