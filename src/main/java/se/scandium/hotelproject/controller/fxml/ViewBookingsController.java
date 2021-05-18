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
@FxmlView("room_bookings.fxml")
public class ViewBookingsController {


}
