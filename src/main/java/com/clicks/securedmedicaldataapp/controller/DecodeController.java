package com.clicks.securedmedicaldataapp.controller;

import com.clicks.securedmedicaldataapp.service.EncryptionService;
import com.clicks.securedmedicaldataapp.utils.StageSwitcher;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;

import java.io.File;

public class DecodeController extends Controller {


    @FXML
    private TextField outName;

    @FXML
    private TextField pathOut;

    @FXML
    private TextField pathSource;

    @FXML
    private TextField secreteKey;


    @FXML
    void browseOut(ActionEvent event) {
        File selectedDirectory = browseDirectory(event);
        pathOut.setText(selectedDirectory.getAbsolutePath());
    }

    @FXML
    void browseSource(ActionEvent event) {
        File file = browseFile(event);
        pathSource.setText(file.getAbsolutePath());
    }

    @FXML
    void submit(ActionEvent event) {

        String filename = outName.getText().trim();
        String outPutPath = pathOut.getText().trim();
        String sourcePath = pathSource.getText().trim();
        String secreteKeyText = secreteKey.getText().trim();

        if(outPutPath.isEmpty() || sourcePath.isEmpty() || secreteKeyText.isEmpty() || filename.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "All fields are required");
            alert.showAndWait();
        }
        EncryptionService encryptionService = new EncryptionService();
        try {
            String destination = outPutPath + "/" + filename;
            encryptionService.decrypt(sourcePath, destination, secreteKeyText);
            showAlert(Alert.AlertType.CONFIRMATION, "Decode completed", true, event);
        }catch (IllegalArgumentException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Incorrect secrete key", true, event);
        }catch (IllegalStateException e) {
            showAlert(Alert.AlertType.ERROR, "Invalid source or destination", false, event);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    private void showAlert(Alert.AlertType type, String message, boolean isSecreteKey, ActionEvent event) {
        Alert alert = new Alert(type, message);
        alert.showAndWait().ifPresent(buttonType -> {
            if (buttonType == ButtonType.OK) {
                if(isSecreteKey) {
                    secreteKey.setText("");
                }
                else {
                    pathOut.setText("");
                    pathSource.setText("");
                }
                StageSwitcher.toWelcomePage(event);
            }
        });
    }

}
