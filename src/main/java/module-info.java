module com.bsend.bsend {
    requires javafx.controls;
    requires javafx.fxml;
    requires jakarta.mail;


    opens com.bsend.bsend to javafx.fxml;
    exports com.bsend.bsend;
    exports com.bsend.bsend.controller;
    opens com.bsend.bsend.controller to javafx.fxml;
}