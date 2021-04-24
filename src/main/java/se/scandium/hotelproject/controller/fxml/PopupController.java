package se.scandium.hotelproject.controller.fxml;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import net.rgielen.fxweaver.core.FxWeaver;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import se.scandium.hotelproject.service.UserService;

@Component
@FxmlView("/fxml/popup.fxml")
public class PopupController {

    private final UserService userService;
    private final FxWeaver fxWeaver;

    @Autowired
    public PopupController(UserService userService, FxWeaver fxWeaver) {
        this.userService = userService;
        this.fxWeaver = fxWeaver;
    }

    @FXML
    void exitClickHandler() {
        Platform.exit();
    }

    @FXML
    void initialize() {
    }

    @FXML
    void resetPWDClickHandler() {
        Scene scene = new Scene(fxWeaver.loadView(ResetPasswordScreenController.class));
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        ResetPasswordScreenController resetPasswordScreenController = fxWeaver.getBean(ResetPasswordScreenController.class);
        resetPasswordScreenController.setResetFirstStep(false);

        stage.setScene(scene);
        stage.showAndWait();
    }


}
