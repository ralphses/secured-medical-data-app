package com.clicks.securedmedicaldataapp.controller;

import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;

public class Controller {

    protected File browseDirectory(ActionEvent event) {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("Choose Destination folder");
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        return directoryChooser.showDialog(stage);
    }

    protected File browseFile(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select File");

        // Set an initial directory (optional)
        fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));

        // Add an extension filter (optional)
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Image files (.jpg, .jpeg)", "*.jpg", "*.jpeg", "*.png");
        fileChooser.getExtensionFilters().add(extFilter);

        // Get the stage (window) to show the file dialog
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        // Show the file dialog and get the selected file
        return fileChooser.showOpenDialog(stage);
    }

}
