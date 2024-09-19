package com.jeanespin.flightsfx;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class FlightsFX extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("FXMLMainView.fxml"));
        Parent root = fxmlLoader.load();

        FXMLMainViewController controller = (FXMLMainViewController)fxmlLoader.getController();
        controller.setOnCloseListener(stage);

        stage.setTitle("FlightsFX");
        stage.setScene(new Scene(root, 650, 400));
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}