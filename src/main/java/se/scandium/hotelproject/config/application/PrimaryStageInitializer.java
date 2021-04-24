package se.scandium.hotelproject.config.application;

import com.jfoenix.assets.JFoenixResources;
import com.jfoenix.svg.SVGGlyphLoader;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.stage.Stage;
import net.rgielen.fxweaver.core.FxWeaver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import se.scandium.hotelproject.HotelProject;
import se.scandium.hotelproject.controller.fxml.LoginController;

import java.io.IOException;

@Component
public class PrimaryStageInitializer implements ApplicationListener<StageReadyEvent> {

    private final FxWeaver fxWeaver;

    @Autowired
    public PrimaryStageInitializer(FxWeaver fxWeaver) {
        this.fxWeaver = fxWeaver;
    }

    @Override
    public void onApplicationEvent(StageReadyEvent event) {
        Stage stage = event.stage;

        new Thread(() -> {
            try {
                SVGGlyphLoader.loadGlyphsFont(HotelProject.class.getResourceAsStream("/fonts/icomoon.svg"),
                        "icomoon.svg");
            } catch (IOException ioExc) {
                ioExc.printStackTrace();
            }
        }).start();
        stage.setTitle("Welcome");

        Scene scene = new Scene(fxWeaver.loadView(LoginController.class), 600, 400);
        final ObservableList<String> stylesheets = scene.getStylesheets();
        stylesheets.addAll(JFoenixResources.load("css/jfoenix-fonts.css").toExternalForm(),
                JFoenixResources.load("css/jfoenix-design.css").toExternalForm(),
                HotelProject.class.getResource("/css/jfoenix-main-demo.css").toExternalForm());
        stage.setScene(scene);
        stage.show();
    }
}
