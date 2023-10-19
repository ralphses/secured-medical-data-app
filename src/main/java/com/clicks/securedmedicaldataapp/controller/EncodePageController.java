package com.clicks.securedmedicaldataapp.controller;

import com.clicks.securedmedicaldataapp.service.EncryptionService;
import com.clicks.securedmedicaldataapp.utils.StageSwitcher;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;

import java.io.File;
import java.util.Objects;

public class EncodePageController extends Controller{

    private String file;

    @FXML
    private PasswordField confirmSecretKey;

    @FXML
    private Button fileChoser;

    @FXML
    private TextField fileName;


    @FXML
    private ImageView preview;

    @FXML
    private Button submitBtn;

    @FXML
    private PasswordField secretKey;

    @FXML
    private TextArea shortText;

    private boolean isFile = false;
    private String outputPath;

    @FXML
    void choseFile(ActionEvent event) {

        // Show the file dialog and get the selected file
        File selectedFile = browseFile(event);

        if(selectedFile != null) {
            String selectedFileAbsolutePath = selectedFile.getAbsolutePath();

            Image image = new Image(selectedFileAbsolutePath);
            shortText.setText(selectedFileAbsolutePath);
            this.file = selectedFileAbsolutePath;
            preview.setImage(image);

            this.isFile = true;
        }
        else if (!shortText.getText().isEmpty()) {
            this.file = shortText.getText();
        }
    }

    @FXML
    void submit(ActionEvent event) throws Exception {

        if (secretKey.getText().isEmpty() || confirmSecretKey.getText().isEmpty() || outputPath.isEmpty() || shortText.getText().isEmpty() || fileName.getText().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "All fields are required");
            alert.showAndWait();
        }

        else if(!Objects.equals(confirmSecretKey.getText(), secretKey.getText())) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Keys does not match");
            alert.showAndWait();
        }
        else {

            String secKey = secretKey.getText();

            //Todo encode file
            EncryptionService encryptionService = new EncryptionService();

            String destination = outputPath + "/" + fileName.getText() + ".png";

            submitBtn.setText("processing...");
            submitBtn.setTextFill(Color.RED);

            boolean successEncryption = (isFile) ?
                    encryptionService.encodeImage(this.file, destination, secKey) :
                    encryptionService.encodeText(shortText.getText(), secKey, destination);

            if(successEncryption) {
                Alert success = new Alert(Alert.AlertType.INFORMATION, "Encryption successful");
                success.showAndWait().ifPresent(e -> {
                    if(e == ButtonType.OK) StageSwitcher.toWelcomePage(event);
                    else event.consume();
                });
            }
            else  {
                Alert failed = new Alert(Alert.AlertType.ERROR, "Could not complete your encryption.\nMake sure you are uploading a valid file");
                failed.setHeaderText("Encryption Failed");
                failed.setTitle("Encryption Error!");
                failed.showAndWait().ifPresent(e -> {
                    if(e == ButtonType.OK) event.consume();
                });
            }
            submitBtn.setText("Encode Document");
            submitBtn.setTextFill(Color.BLACK);


        }

    }

    @FXML
    void cancel(ActionEvent event) {
        StageSwitcher.toWelcomePage(event);
    }

    @FXML
    void choseOutput(ActionEvent event) {
        File selectedDirectory = browseDirectory(event);
        if (selectedDirectory != null) {
            outputPath = selectedDirectory.getAbsolutePath();
        }
    }



}

