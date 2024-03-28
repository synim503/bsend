package com.bsend.bsend.controller;

import java.net.URL;
import java.util.ResourceBundle;

import com.bsend.bsend.handlers.home.StartCheckButtonHandler;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;

public class HomeController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TextField countThreads;

    @FXML
    private TextArea smtpRelayArea;

    @FXML
    private Button startCheck;

    @FXML
    private void initialize() {
        countThreads.addEventFilter(KeyEvent.KEY_TYPED, new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if (!Character.isDigit(event.getCharacter().charAt(0))) {
                    event.consume();
                }
            }
        });

        startCheck.setOnAction(new StartCheckButtonHandler(countThreads,smtpRelayArea,startCheck));
    }

}
