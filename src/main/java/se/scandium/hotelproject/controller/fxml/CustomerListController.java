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
import se.scandium.hotelproject.dto.CustomerDto;
import se.scandium.hotelproject.dto.RoomDto;
import se.scandium.hotelproject.entity.Gender;
import se.scandium.hotelproject.entity.RoomType;
import se.scandium.hotelproject.exception.RecordNotFoundException;
import se.scandium.hotelproject.service.CustomerService;
import se.scandium.hotelproject.service.RoomService;

import java.util.List;

@Component
@FxmlView("/fxml/list_customer.fxml")
public class CustomerListController {

    private CustomerService customerService;
    private final FxWeaver fxWeaver;
    private CustomerDto selectedCustomerDto;
    private List<CustomerDto> customerDtoList;
    private ObservableList<CustomerDto> data;

    @Autowired
    public CustomerListController(FxWeaver fxWeaver, CustomerService customerService) {
        this.fxWeaver = fxWeaver;
        this.customerService = customerService;
    }

    @FXML
    private TableView<CustomerDto> customerDtoTableView;
    @FXML
    private TableColumn<CustomerDto, Integer> idColumn;
    @FXML
    private TableColumn<CustomerDto, String> nameColumn;
    @FXML
    private TableColumn<CustomerDto, String> surnameColumn;
    @FXML
    private TableColumn<CustomerDto, Integer> ageColumn;
    @FXML
    private TableColumn<CustomerDto, Gender> genderColumn;
    @FXML
    private TableColumn<CustomerDto, String> countryColumn;
    @FXML
    private TableColumn<CustomerDto, String> cityColumn;
    @FXML
    private TableColumn<CustomerDto, String> streetColumn;
    @FXML
    private TableColumn<CustomerDto, String> zipCodeColumn;
    @FXML
    private JFXButton deleteButton;
    @FXML
    private Text errorTextForm;
    @FXML
    private Text errorTextTable;


    @FXML
    private JFXTextField nameField;
    @FXML
    private JFXTextField surnameField;
    @FXML
    private JFXTextField ageField;
    @FXML
    private JFXComboBox<Gender> genderComboBox;
    @FXML
    private JFXTextField countryField;
    @FXML
    private JFXTextField cityField;
    @FXML
    private JFXTextField streetField;
    @FXML
    private JFXTextField zipCodeField;
    @FXML
    private JFXButton updateButton;
    @FXML
    private ToggleGroup group;

    private void loadDateTable() {
        customerDtoList = customerService.getAll();
        // Create table columns
        idColumn = new TableColumn<>("Id");
        nameColumn = new TableColumn<>("Name");
        surnameColumn = new TableColumn<>("Surname");
        ageColumn = new TableColumn<>("Age");
        genderColumn = new TableColumn<>("Gender");
        countryColumn = new TableColumn<>("Country");
        cityColumn = new TableColumn<>("City");
        streetColumn = new TableColumn<>("Street");
        zipCodeColumn = new TableColumn<>("Zip-code");

        idColumn.setCellValueFactory(new PropertyValueFactory<>("Id"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("Name"));
        surnameColumn.setCellValueFactory(new PropertyValueFactory<>("Surname"));
        ageColumn.setCellValueFactory(new PropertyValueFactory<>("Age"));
        genderColumn.setCellValueFactory(new PropertyValueFactory<>("Gender"));
        countryColumn.setCellValueFactory(new PropertyValueFactory<>("Country"));
        cityColumn.setCellValueFactory(new PropertyValueFactory<>("City"));
        streetColumn.setCellValueFactory(new PropertyValueFactory<>("Street"));
        zipCodeColumn.setCellValueFactory(new PropertyValueFactory<>("Zip-code"));

        data = FXCollections.observableArrayList(customerDtoList);
        customerDtoTableView.setItems(data);
        //roomDtoTableView.getItems().addAll(roomDtoList);

        customerDtoTableView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                System.out.println("newSelection = " + newSelection);
                this.selectedCustomerDto = newSelection;
                setSelectedDataToUpdateForm();
            }
        });
    }

    @FXML
    void initialize() {
        setRoomType();
        group = new ToggleGroup();
        loadDateTable();
        deleteButton.setOnAction(this::deleteRoomAction);
        updateButton.setOnAction(this::updateRoomAction);
    }

    private void setSelectedDataToUpdateForm() {
        if (this.selectedCustomerDto != null) {
            nameField.setText(selectedCustomerDto.getHotelDto().getName());
            roomNameField.setText(selectedCustomerDto.getName());
            roomPriceField.setText(selectedCustomerDto.getPrice() + "");
            roomSizeField.setText(selectedCustomerDto.getSize() + "");
            //roomTypeComboBox.setItems(selectedRoomDto.);
            roomDetailsDescTextArea.setText(selectedCustomerDto.getDescription());
            roomDetailsLocationField.setText(selectedCustomerDto.getLocation());
            roomDetailsIsBedCheckBox.setSelected(selectedCustomerDto.isBeds());
            roomDetailsNumberOfBedsField.setText(selectedCustomerDto.getNumberOfBeds() + "");
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
