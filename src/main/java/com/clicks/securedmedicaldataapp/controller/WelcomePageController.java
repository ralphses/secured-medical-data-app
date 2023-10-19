package com.clicks.securedmedicaldataapp.controller;

import com.clicks.securedmedicaldataapp.SecuredMedApp;
import com.clicks.securedmedicaldataapp.utils.StageSwitcher;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;

public class WelcomePageController {

    @FXML
    void toDecode(ActionEvent event) {
        FXMLLoader fxmlLoader = new FXMLLoader(SecuredMedApp.class.getResource("decode.fxml"));
        StageSwitcher.toPage(fxmlLoader, event, 600,400, "Encode Data");
    }

    @FXML
    void toEncode(ActionEvent event) {
        FXMLLoader fxmlLoader = new FXMLLoader(SecuredMedApp.class.getResource("encode.fxml"));
        StageSwitcher.toPage(fxmlLoader, event, 900,600, "Encode Data");
    }
}
