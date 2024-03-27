package com.bsend.bsend.handlers.home;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class StartCheckButtonHandler implements EventHandler {
    private TextField countThreads;
    private TextArea smtpRelayArea;
    private Button startCheck;

    public StartCheckButtonHandler(TextField countThreads, TextArea smtpRelayArea, Button startCheck) {
        this.countThreads = countThreads;
        this.smtpRelayArea = smtpRelayArea;
        this.startCheck = startCheck;
    }

    @Override
    public void handle(Event event) {
        String[] lineSmpt = smtpRelayArea.getText().split("\n");
    }
}
