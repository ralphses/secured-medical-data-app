package com.clicks.securedmedicaldataapp;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;

import java.io.IOException;

public class SecuredMedApp extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(SecuredMedApp.class.getResource("welcome.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 900, 600);
        stage.setTitle("Secured File Encryption");
        stage.setScene(scene);
        stage.setOnCloseRequest(winEvent -> new Alert(Alert.AlertType.CONFIRMATION, "Exit Application?")
                .showAndWait()
                .ifPresent(present -> {
                    if (present == ButtonType.OK) stage.close();
                    else winEvent.consume();
                })
        );
        stage.setResizable(false);

        stage.show();

    }
    
    public static void main(String[] args) {
        launch();
    }
}