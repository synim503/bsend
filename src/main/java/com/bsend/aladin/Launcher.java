package com.bsend.aladin;

import com.bsend.aladin.theme.NordLight;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Launcher extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        Application.setUserAgentStylesheet(new NordLight().getUserAgentStylesheet());
        FXMLLoader fxmlLoader = new FXMLLoader(Launcher.class.getResource("home.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("BSender - sender from smtp");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }


}