package com.bsend.aladin.controller;

import java.net.URL;
import java.util.ResourceBundle;

import com.bsend.aladin.handlers.home.StartCheckButtonHandler;
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
    private TextField port;

    @FXML
    private TextArea smtpRelayArea;

    @FXML
    private Button startCheck;

    @FXML
    private TextArea subject;

    @FXML
    private TextArea text;

    @FXML
    private TextArea proxyList;

    @FXML
    private void initialize() {
        countThreads.addEventFilter(KeyEvent.KEY_TYPED, event -> {
            if (!Character.isDigit(event.getCharacter().charAt(0))) {
                event.consume();
            }
        });

        port.addEventFilter(KeyEvent.KEY_TYPED, event -> {
            if (!Character.isDigit(event.getCharacter().charAt(0))) {
                event.consume();
            }
        });

        startCheck.setOnAction(new StartCheckButtonHandler(countThreads,port,smtpRelayArea,startCheck,subject,text,proxyList));
    }

}
