module com.clicks.securedmedicaldataapp {
    requires javafx.controls;
    requires javafx.fxml;
    requires ignitium.crypto;
    requires java.desktop;
    requires ignitium.assets;
    requires ignitium.core;
    requires ignitium.cfg;
    requires jsr305;

    opens com.clicks.securedmedicaldataapp to javafx.fxml;
    opens com.clicks.securedmedicaldataapp.controller to javafx.fxml;
    exports com.clicks.securedmedicaldataapp;
    exports com.clicks.securedmedicaldataapp.controller;
}