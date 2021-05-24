package se.scandium.hotelproject.controller.fxml;

import com.jfoenix.controls.*;
import com.sun.prism.shader.AlphaOne_Color_AlphaTest_Loader;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import javafx.stage.Window;
import net.rgielen.fxweaver.core.FxWeaver;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import se.scandium.hotelproject.dto.BookingDto;
import se.scandium.hotelproject.dto.RoomDto;
import se.scandium.hotelproject.entity.Booking;
import se.scandium.hotelproject.exception.RecordNotFoundException;
import se.scandium.hotelproject.service.BookingService;
import se.scandium.hotelproject.service.RoomService;

import java.time.LocalDate;
import java.util.List;

@Component
@FxmlView("/fxml/room_bookings.fxml")
public class ViewBookingsController {

    private BookingService bookingService;
    private final FxWeaver fxWeaver;
    private BookingDto selectedBookingDto;
    private List<BookingDto> bookingDtoList;
    private ObservableList<BookingDto> data;
    private RoomService roomService;

    @Autowired
    public ViewBookingsController(FxWeaver fxWeaver, BookingService bookingService, RoomService roomService) {
        this.fxWeaver = fxWeaver;
        this.bookingService = bookingService;
        this.roomService = roomService;
    }

    @FXML
    private TableView<BookingDto> bookingDtoTableView;
    @FXML
    private TableColumn<BookingDto, Integer> bookingIdColumn;
    @FXML
    private TableColumn<BookingDto, String> customerColumn;
    @FXML
    private TableColumn<BookingDto, String> customerNameColumn;
    @FXML
    private TableColumn<BookingDto, Integer> roomIdColumn;
    @FXML
    private TableColumn<BookingDto, String> roomTypeColumn;
    @FXML
    private TableColumn<BookingDto, Boolean> reservedColumn;
    @FXML
    private TableColumn<BookingDto, LocalDate> startDateColumn;
    @FXML
    private TableColumn<BookingDto, LocalDate> endDateColumn;
    @FXML
    private TableColumn<BookingDto, Boolean> paidColumn;
    @FXML
    private TableColumn<BookingDto, Integer> noOfPersonsColumn;
    @FXML
    private TableColumn<BookingDto, Integer> paymentColumn;
    @FXML
    private TableColumn<BookingDto, Double> fullPriceColumn;
    @FXML
    private TableColumn<BookingDto, Boolean> breakfastColumn;
    @FXML
    private TableColumn<BookingDto, Boolean> lunchColumn;
    @FXML
    private JFXButton refreshButton;
    @FXML
    private ComboBox<RoomDto> roomComboBox;



    private void loadDateTable() {
        bookingDtoList = bookingService.getAlBooking();
        // Create table columns
        bookingIdColumn = new TableColumn<>("Booking ID");
        customerColumn = new TableColumn<>("Customer");
        customerNameColumn = new TableColumn<>("Customer Name");
        roomIdColumn = new TableColumn<>("Room ID");
        roomTypeColumn = new TableColumn<>("Room ID");
        reservedColumn = new TableColumn<>("Reserved");
        startDateColumn = new TableColumn<>("Start Date");
        endDateColumn = new TableColumn<>("End Date");
        paidColumn = new TableColumn<>("Paid");
        noOfPersonsColumn = new TableColumn<>("No of Persons");
        paymentColumn = new TableColumn<>("Pay Method");
        fullPriceColumn = new TableColumn<>("Full Price");
        breakfastColumn = new TableColumn<>("Breakfast");
        lunchColumn = new TableColumn<>("Lunch");

        bookingIdColumn.setCellValueFactory(new PropertyValueFactory<>("Booking ID"));
        customerColumn.setCellValueFactory(new PropertyValueFactory<>("Customer"));
        customerNameColumn.setCellValueFactory(new PropertyValueFactory<>("Customer Name"));
        roomIdColumn.setCellValueFactory(new PropertyValueFactory<>("Room ID"));
        roomTypeColumn.setCellValueFactory(new PropertyValueFactory<>("Room Type"));
//        roomIdColumn.setCellValueFactory(cellData -> {
//
//            final String roomType = cellData.getValue().getRoom().getType().name();
//            return new SimpleStringProperty(roomType);
//        });
        reservedColumn.setCellValueFactory(new PropertyValueFactory<>("Reserved"));
        startDateColumn.setCellValueFactory(new PropertyValueFactory<>("Start Date"));
        endDateColumn.setCellValueFactory(new PropertyValueFactory<>("End Date"));
        paidColumn.setCellValueFactory(new PropertyValueFactory<>("Paid"));
        noOfPersonsColumn.setCellValueFactory(new PropertyValueFactory<>("No of Persons"));
        paymentColumn.setCellValueFactory(new PropertyValueFactory<>("Pay Method"));
        fullPriceColumn.setCellValueFactory(new PropertyValueFactory<>("Full Price"));
        breakfastColumn.setCellValueFactory(new PropertyValueFactory<>("Breakfast"));
        lunchColumn.setCellValueFactory(new PropertyValueFactory<>("Lunch"));

        data = FXCollections.observableArrayList(bookingDtoList);
        bookingDtoTableView.setItems(data);
        //bookingDtoTableView.getItems().addAll(bookingDtoList);

    }

    @FXML
    void initialize() {
        loadDateTable();
        setRoomComboBox();
        refreshButton.setOnAction(this::refreshTableAction);
    }

    private void setRoomComboBox(){
    roomComboBox.getItems().addAll(roomService.getAll());
    }

    private void refreshTableAction(ActionEvent event){
        RoomDto selectedRoomDto = roomComboBox.getValue();
        List<BookingDto> bookingDtoList = bookingService.getBookingListByRoomId(selectedRoomDto.getId());
        data = FXCollections.observableArrayList(bookingDtoList);
        bookingDtoTableView.setItems(data);
    }

}
