package se.scandium.hotelproject;

import com.jfoenix.assets.JFoenixResources;
import com.jfoenix.svg.SVGGlyphLoader;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

import java.io.IOException;


public class HotelProject extends Application {
    private ConfigurableApplicationContext applicationContext;
    private Parent root;

    @Override
    public void init() throws IOException {
        applicationContext = new SpringApplicationBuilder(HotelProjectSpringApplication.class).run();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/login.fxml"));
        fxmlLoader.setControllerFactory(applicationContext::getBean);
        root = fxmlLoader.load();
    }

    @Override
    public void stop() {
        applicationContext.close();
        Platform.exit();
    }

    @Override
    public void start(Stage primaryStage) {
        new Thread(() -> {
            try {
                SVGGlyphLoader.loadGlyphsFont(HotelProject.class.getResourceAsStream("/fonts/icomoon.svg"),
                        "icomoon.svg");
            } catch (IOException ioExc) {
                ioExc.printStackTrace();
            }
        }).start();
        primaryStage.setTitle("Welcome");

        Scene scene = new Scene(root, 600, 400);
        final ObservableList<String> stylesheets = scene.getStylesheets();
        stylesheets.addAll(JFoenixResources.load("css/jfoenix-fonts.css").toExternalForm(),
                JFoenixResources.load("css/jfoenix-design.css").toExternalForm(),
                HotelProject.class.getResource("/css/jfoenix-main-demo.css").toExternalForm());
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
