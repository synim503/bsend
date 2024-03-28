module com.bsend.bsend {
    requires javafx.controls;
    requires javafx.fxml;
    requires jakarta.mail;


    opens com.bsend.aladin to javafx.fxml;
    exports com.bsend.aladin;
    exports com.bsend.aladin.controller;
    opens com.bsend.aladin.controller to javafx.fxml;
}