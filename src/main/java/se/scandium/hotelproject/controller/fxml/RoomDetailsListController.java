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
@FxmlView("/fxml/details_room.fxml")
public class RoomDetailsListController {

    private RoomService roomService;
    private final FxWeaver fxWeaver;
    private RoomDto selectedRoomDto;
    private List<RoomDto> roomDtoList;
    private ObservableList<RoomDto> data;

    @Autowired
    public RoomDetailsListController(FxWeaver fxWeaver, RoomService roomService) {
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


    }

    @FXML
    void initialize() {
        loadDateTable();
    }

}
