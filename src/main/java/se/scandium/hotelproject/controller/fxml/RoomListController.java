package se.scandium.hotelproject.controller.fxml;

import com.jfoenix.controls.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import javafx.stage.Window;
import net.rgielen.fxweaver.core.FxWeaver;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import se.scandium.hotelproject.dto.RoomDto;
import se.scandium.hotelproject.entity.RoomType;
import se.scandium.hotelproject.exception.RecordNotFoundException;
import se.scandium.hotelproject.service.RoomService;

import java.util.List;

@Component
@FxmlView("/fxml/list_room.fxml")
public class RoomListController {

    private RoomService roomService;
    private final FxWeaver fxWeaver;
    private RoomDto selectedRoomDto;
    private List<RoomDto> roomDtoList;
    private ObservableList<RoomDto> data;

    @Autowired
    public RoomListController(FxWeaver fxWeaver, RoomService roomService) {
        this.fxWeaver = fxWeaver;
        this.roomService = roomService;
    }

    @FXML
    private TableView<RoomDto> roomDtoTableView;
    @FXML
    private TableColumn<RoomDto, Integer> idColumn;
    @FXML
    private TableColumn<RoomDto, String> nameColumn;
    @FXML
    private TableColumn<RoomDto, Integer> priceColumn;
    @FXML
    private TableColumn<RoomDto, String> typeColumn;
    @FXML
    private TableColumn<RoomDto, Integer> sizeColumn;
    @FXML
    private TableColumn<RoomDto, String> reserveColumn;
    @FXML
    private TableColumn<RoomDto, String> locationColumn;
    @FXML
    private TableColumn<RoomDto, String> bedsColumn;
    @FXML
    private TableColumn<RoomDto, String> descriptionColumn;
    @FXML
    private JFXButton deleteButton;
    @FXML
    private Text errorTextForm;
    @FXML
    private Text errorTextTable;


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
    private JFXButton updateButton;
    @FXML
    private ToggleGroup group;

    private void loadDateTable() {
        roomDtoList = roomService.getAll();
        // Create table columns
        idColumn = new TableColumn<>("Id");
        nameColumn = new TableColumn<>("Name");
        typeColumn = new TableColumn<>("Type");
        priceColumn = new TableColumn<>("Price");
        sizeColumn = new TableColumn<>("Size");
        reserveColumn = new TableColumn<>("Reserve");
        locationColumn = new TableColumn<>("Location");
        bedsColumn = new TableColumn<>("Beds");
        descriptionColumn = new TableColumn<>("Description");

        idColumn.setCellValueFactory(new PropertyValueFactory<>("Id"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("Name"));
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("Type"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("Price"));
        sizeColumn.setCellValueFactory(new PropertyValueFactory<>("Size"));
        reserveColumn.setCellValueFactory(new PropertyValueFactory<>("Reserve"));
        locationColumn.setCellValueFactory(new PropertyValueFactory<>("Location"));
        bedsColumn.setCellValueFactory(new PropertyValueFactory<>("Beds"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("Description"));

        data = FXCollections.observableArrayList(roomDtoList);
        roomDtoTableView.setItems(data);
        //roomDtoTableView.getItems().addAll(roomDtoList);

        roomDtoTableView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                System.out.println("newSelection = " + newSelection);
                this.selectedRoomDto = newSelection;
                setSelectedDataToUpdateForm();
            }
        });
    }

    @FXML
    void initialize() {
        setRoomType();
        group = new ToggleGroup();
        isReserveRadioButtonY.setToggleGroup(group);
        isReserveRadioButtonN.setToggleGroup(group);

        loadDateTable();
        deleteButton.setOnAction(this::deleteRoomAction);
        updateButton.setOnAction(this::updateRoomAction);
    }

    private void setSelectedDataToUpdateForm() {
        if (this.selectedRoomDto != null) {
            hotelNameField.setText(selectedRoomDto.getHotelDto().getName());
            roomNameField.setText(selectedRoomDto.getName());
            roomPriceField.setText(selectedRoomDto.getPrice() + "");
            roomSizeField.setText(selectedRoomDto.getSize() + "");
            //roomTypeComboBox.setItems(selectedRoomDto.);
            if (selectedRoomDto.isReserve()) {
                isReserveRadioButtonY.setSelected(true);
            } else {
                isReserveRadioButtonN.setSelected(true);
            }
            roomDetailsDescTextArea.setText(selectedRoomDto.getDescription());
            roomDetailsLocationField.setText(selectedRoomDto.getLocation());
            roomDetailsIsBedCheckBox.setSelected(selectedRoomDto.isBeds());
            roomDetailsNumberOfBedsField.setText(selectedRoomDto.getNumberOfBeds() + "");
        }
    }


    private void updateRoomAction(ActionEvent event) {
        if (validateAndBuildRoomData()) {
            try {
                System.out.println("selectedRoomDto = " + selectedRoomDto);
                roomService.saveOrUpdate(selectedRoomDto);
                selectedRoomDto = null;
                data.clear();
                resetUpdateForm();
                loadDateTable();
            } catch (RecordNotFoundException e) {
                e.printStackTrace();
                errorTextForm.setText("RecordNotFoundException");
                showAlert(Alert.AlertType.ERROR, updateButton.getScene().getWindow(), "Error!", errorTextForm.getText());
            }
        }
    }


    private void resetUpdateForm() {
        hotelNameField.setText(null);
        roomNameField.setText(null);
        roomPriceField.setText(null);
        roomSizeField.setText(null);
        roomTypeComboBox.setItems(null);
        isReserveRadioButtonN.setSelected(true);
        roomDetailsDescTextArea.setText(null);
        roomDetailsLocationField.setText(null);
        roomDetailsIsBedCheckBox.setSelected(false);
        roomDetailsNumberOfBedsField.setText(null);
        errorTextForm.setText(null);
    }

    private boolean validateAndBuildRoomData() {

        if (this.selectedRoomDto == null){
            errorTextForm.setText("room is not selected");
            showAlert(Alert.AlertType.WARNING, updateButton.getScene().getWindow(), "Warning!", errorTextForm.getText());
            return false;
        }

        String name = this.roomNameField.getText();
        if (name.trim().length() == 0) {
            errorTextForm.setText("Name is not valid");
            showAlert(Alert.AlertType.WARNING, updateButton.getScene().getWindow(), "Warning!", errorTextForm.getText());
            return false;
        }
        selectedRoomDto.setName(name);

        RoomType type = this.roomTypeComboBox.getValue();
        if (type == null) {
            errorTextForm.setText("Room Type is not valid");
            showAlert(Alert.AlertType.WARNING, updateButton.getScene().getWindow(), "Warning!", errorTextForm.getText());
            return false;
        }
        selectedRoomDto.setType(type);

        String price = this.roomPriceField.getText();
        if (price.trim().length() == 0) {
            errorTextForm.setText("Price is not valid");
            showAlert(Alert.AlertType.WARNING, updateButton.getScene().getWindow(), "Warning!", errorTextForm.getText());
            return false;
        }
        try {
            selectedRoomDto.setPrice(Double.parseDouble(price));
        } catch (NumberFormatException e) {
            errorTextForm.setText("Price is not valid");
            showAlert(Alert.AlertType.WARNING, updateButton.getScene().getWindow(), "Warning!", errorTextForm.getText());
            return false;
        }

        String size = this.roomSizeField.getText();
        if (size.trim().length() == 0) {
            errorTextForm.setText("Size is not valid");
            showAlert(Alert.AlertType.WARNING, updateButton.getScene().getWindow(), "Warning!", errorTextForm.getText());
            return false;
        }
        try {
            selectedRoomDto.setSize(Integer.parseInt(size));
        } catch (NumberFormatException e) {
            errorTextForm.setText("Size is not valid");
            showAlert(Alert.AlertType.WARNING, updateButton.getScene().getWindow(), "Warning!", errorTextForm.getText());
            return false;
        }
        boolean reserveY = this.isReserveRadioButtonY.isSelected();
        selectedRoomDto.setReserve(reserveY);

        String location = this.roomDetailsLocationField.getText();
        if (location.trim().length() == 0) {
            errorTextForm.setText("Location is not valid");
            showAlert(Alert.AlertType.WARNING, updateButton.getScene().getWindow(), "Warning!", errorTextForm.getText());
            return false;
        }
        selectedRoomDto.setLocation(location);

        String isBed = this.roomDetailsIsBedCheckBox.getText();
        selectedRoomDto.setBeds(isBed.equalsIgnoreCase("Yes"));

        String numOfBed = this.roomDetailsNumberOfBedsField.getText();
        if (numOfBed.trim().length() != 0)
            selectedRoomDto.setNumberOfBeds(Integer.parseInt(numOfBed));

        String description = this.roomDetailsDescTextArea.getText();
        selectedRoomDto.setDescription(description);
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

    private void deleteRoomAction(ActionEvent event) {
        try {
            if (selectedRoomDto != null) {
                roomService.deleteById(selectedRoomDto.getId());
                selectedRoomDto = null;
                data.clear();
                loadDateTable();
                resetUpdateForm();
            } else {
                errorTextTable.setText("data is not selected");
                showAlert(Alert.AlertType.ERROR, deleteButton.getScene().getWindow(), "Warning!", errorTextTable.getText());
            }
        } catch (RecordNotFoundException e) {
            e.printStackTrace();
            errorTextTable.setText("RecordNotFoundException");
            showAlert(Alert.AlertType.ERROR, deleteButton.getScene().getWindow(), "Error!", errorTextTable.getText());
        }
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
