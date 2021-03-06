package se.scandium.hotelproject.controller.fxml;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXHamburger;
import com.jfoenix.controls.JFXPopup;
import com.jfoenix.controls.JFXRippler;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Modality;
import javafx.stage.Stage;
import net.rgielen.fxweaver.core.FxWeaver;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import se.scandium.hotelproject.controller.fxml.singleton.UserHolder;
import se.scandium.hotelproject.controller.fxml.view.UserView;
import se.scandium.hotelproject.service.UserService;

@Component
@FxmlView("/fxml/reception_panel.fxml")
public class ReceptionController {

    private UserView userView;
    private final FxWeaver fxWeaver;

    @Autowired
    public ReceptionController(FxWeaver fxWeaver) {
        this.fxWeaver = fxWeaver;
    }

    @FXML
    private Label screenTitleText;
    @FXML
    private JFXButton customerDetailsButton;
    @FXML
    private JFXButton addCustomerButton;
    @FXML
    private JFXButton addBookingButton;
    @FXML
    private JFXRippler rippler;
    @FXML
    private JFXHamburger burger;
    @FXML
    private JFXPopup popup;
    @FXML
    private JFXButton BookingListButton;
    @FXML
    private JFXButton AddBookingButton;

    @FXML
    void initialize() {
        userView = UserHolder.getInstance().getUserView();
        screenTitleText.setText(userView.getScreenTitle());
        customerDetailsButton.setOnAction(this::loadCustomerDetailsControlInDialog);
        addCustomerButton.setOnAction(this::loadAddCustomerControlInDialog);
        BookingListButton.setOnAction(this::loadBookingListInDialog);
        AddBookingButton.setOnAction(this::loadAddBookingInDialog);
    }
    @FXML
    void loadCustomerDetailsControlInDialog(ActionEvent event) {
        System.out.println("Hello world");
        Scene scene = new Scene(fxWeaver.loadView(CustomerListController.class));
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setScene(scene);
        stage.showAndWait();
    }
    @FXML
    void loadPopup() {
        popup = new JFXPopup(fxWeaver.loadView(PopupController.class));
        burger.setOnMouseClicked((e) -> popup.show(rippler, JFXPopup.PopupVPosition.TOP, JFXPopup.PopupHPosition.RIGHT));
    }
    @FXML
    void loadAddCustomerControlInDialog(ActionEvent event) {
        Scene scene = new Scene(fxWeaver.loadView(CustomerController.class));
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setScene(scene);
        stage.showAndWait();
    }
    @FXML
    void loadBookingListInDialog(ActionEvent event) {
        Scene scene = new Scene(fxWeaver.loadView(ViewBookingsController.class));
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setScene(scene);
        stage.showAndWait();
    }

    void loadAddBookingInDialog(ActionEvent event) {
        Scene scene = new Scene(fxWeaver.loadView(BookingController.class));
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setScene(scene);
        stage.showAndWait();
    }
}
