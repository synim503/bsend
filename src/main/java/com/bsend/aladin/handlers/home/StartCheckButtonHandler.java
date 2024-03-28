package com.bsend.aladin.handlers.home;

import com.bsend.aladin.core.TaskExecutor;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.LinkedBlockingQueue;

public class StartCheckButtonHandler implements EventHandler {
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

    public StartCheckButtonHandler(TextField countThreads, TextField port, TextArea smtpRelayArea, Button startCheck, TextArea subject, TextArea text, TextArea proxyList) {
        this.countThreads = countThreads;
        this.port = port;
        this.smtpRelayArea = smtpRelayArea;
        this.startCheck = startCheck;
        this.subject = subject;
        this.text = text;
        this.proxyList = proxyList;
    }

    @Override
    public void handle(Event event) {
        startCheck.setDisable(true);
        TaskExecutor taskExecutor = null;
        try {
            taskExecutor = new TaskExecutor(getSmpt(),getPort(),getText(),getSubject(),getCountThread(),  getProxy());
            taskExecutor.run();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        startCheck.setDisable(false);
    }

    private ConcurrentLinkedDeque getProxy() throws InterruptedException {
        String[] proxy = proxyList.getText().split("\n");
        ConcurrentLinkedDeque<String> proxyQueue = new ConcurrentLinkedDeque<>();
        for(String str:proxy){
            proxyQueue.add(str);
        }
        return proxyQueue;
    }
    private String[] getSmpt(){
        return smtpRelayArea.getText().split("\n");
    }

    private String getPort(){
        return port.getText();
    }

    private String getText(){
        return text.getText();
    }

    private String getSubject(){
        return subject.getText();
    }

    private int getCountThread(){
        return Integer.parseInt(countThreads.getText());
    }
}
