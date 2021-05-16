package se.scandium.hotelproject.controller.fxml;

import com.jfoenix.controls.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ToggleGroup;
import javafx.scene.text.Text;
import javafx.stage.Window;
import net.rgielen.fxweaver.core.FxWeaver;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import se.scandium.hotelproject.controller.fxml.singleton.HotelHolder;
import se.scandium.hotelproject.dto.HotelDto;
import se.scandium.hotelproject.dto.RoomDto;
import se.scandium.hotelproject.entity.RoomType;
import se.scandium.hotelproject.exception.RecordNotFoundException;
import se.scandium.hotelproject.service.RoomService;

@Component

@FxmlView("/fxml/add_room.fxml")
@Service
public class RoomController {

    private RoomService roomService;
    private final FxWeaver fxWeaver;
    private boolean resetFirstStep = true;
    private HotelDto hotelDto;
    RoomDto roomDto;

    @Autowired
    public RoomController(FxWeaver fxWeaver, RoomService roomService) {
        this.fxWeaver = fxWeaver;
        this.roomService = roomService;
    }

    @FXML
    private JFXTextField hotelNameField;
    @FXML
    private JFXTextField roomNameField;
    @FXML
    private JFXTextField roomPriceField;
    @FXML
    private JFXTextField roomSizeField;
    @FXML
    private JFXComboBox<RoomType> roomTypeComboBox;
    @FXML
    private JFXRadioButton isReserveRadioButtonY;
    @FXML
    private JFXRadioButton isReserveRadioButtonN;
    @FXML
    private JFXTextArea roomDetailsDescTextArea;
    @FXML
    private JFXTextField roomDetailsLocationField;
    @FXML
    private JFXCheckBox roomDetailsIsBedCheckBox;
    @FXML
    private JFXTextField roomDetailsNumberOfBedsField;
    @FXML
    private JFXButton addButton;
    @FXML
    private Text errorText;

    @FXML
    private ToggleGroup group;

    @FXML
    void initialize() {
        roomDto = new RoomDto();
        hotelDto = HotelHolder.getInstance().getHotelDto();
        roomDto.setHotelDto(hotelDto);
        hotelNameField.setText(roomDto.getHotelDto().getName());
        setRoomType();
        group = new ToggleGroup();
        isReserveRadioButtonY.setToggleGroup(group);
        isReserveRadioButtonN.setToggleGroup(group);
        addButton.setOnAction(this::addRoomAction);
    }

    private void addRoomAction(ActionEvent event) {
        if (validateAndBuildRoomData()) {
            try {
                System.out.println("roomDto = " + roomDto);
                roomService.saveOrUpdate(roomDto);
                addButton.getScene().getWindow().hide();
            } catch (RecordNotFoundException e) {
                e.printStackTrace();
                errorText.setText("RecordNotFoundException");
                showAlert(Alert.AlertType.ERROR, addButton.getScene().getWindow(), "Error!", errorText.getText());
            }
        }

    }

    private boolean validateAndBuildRoomData() {
        String name = this.roomNameField.getText();
        if (name.trim().length() == 0) { // name == null
            errorText.setText("Name is not valid");
            showAlert(Alert.AlertType.WARNING, addButton.getScene().getWindow(), "Warning!", errorText.getText());
            return false;
        }
        roomDto.setName(name);

        RoomType type = this.roomTypeComboBox.getValue();
        if (type == null) {
            errorText.setText("Room Type is not valid");
            showAlert(Alert.AlertType.WARNING, addButton.getScene().getWindow(), "Warning!", errorText.getText());
            return false;
        }
        roomDto.setType(type);

        String price = this.roomPriceField.getText();
        if (price.trim().length() == 0) {
            errorText.setText("Price is not valid");
            showAlert(Alert.AlertType.WARNING, addButton.getScene().getWindow(), "Warning!", errorText.getText());
            return false;
        }
        try {
            roomDto.setPrice(Double.parseDouble(price));
        }catch (NumberFormatException e){
            errorText.setText("Price is not valid");
            showAlert(Alert.AlertType.WARNING, addButton.getScene().getWindow(), "Warning!", errorText.getText());
            return false;
        }

        String size = this.roomSizeField.getText();
        if (size.trim().length() == 0) {
            errorText.setText("Size is not valid");
            showAlert(Alert.AlertType.WARNING, addButton.getScene().getWindow(), "Warning!", errorText.getText());
            return false;
        }
        try {
            roomDto.setSize(Integer.parseInt(size));
        }catch (NumberFormatException e){
            errorText.setText("Size is not valid");
            showAlert(Alert.AlertType.WARNING, addButton.getScene().getWindow(), "Warning!", errorText.getText());
            return false;
        }
        boolean reserveY = this.isReserveRadioButtonY.isSelected();
        roomDto.setReserve(reserveY);

        String location = this.roomDetailsLocationField.getText();
        if (location.trim().length() == 0) {
            errorText.setText("Location is not valid");
            showAlert(Alert.AlertType.WARNING, addButton.getScene().getWindow(), "Warning!", errorText.getText());
            return false;
        }
        roomDto.setLocation(location);

        String isBed = this.roomDetailsIsBedCheckBox.getText();
        roomDto.setBeds(isBed.equalsIgnoreCase("Yes"));

        String numOfBed = this.roomDetailsNumberOfBedsField.getText();
        if (numOfBed.trim().length() != 0)
            roomDto.setNumberOfBeds(Integer.parseInt(numOfBed));

        String description = this.roomDetailsDescTextArea.getText();
        roomDto.setDescription(description);
        return true;
    }

    private void setRoomType() {
        roomTypeComboBox.getItems().add(RoomType.Single);
        roomTypeComboBox.getItems().add(RoomType.Double);
        roomTypeComboBox.getItems().add(RoomType.Triple);
        roomTypeComboBox.getItems().add(RoomType.Quad);
        roomTypeComboBox.getItems().add(RoomType.Queen);
        roomTypeComboBox.getItems().add(RoomType.King);
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
