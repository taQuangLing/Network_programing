module com.example.front_end {
    requires javafx.controls;
    requires javafx.fxml;
    requires lombok;
    requires java.desktop;
    requires java.net.http;
    requires org.apache.httpcomponents.httpcore;
    requires org.apache.httpcomponents.httpclient;
    requires org.apache.httpcomponents.httpmime;


    opens com.example.front_end to javafx.fxml;
    exports com.example.front_end;
    exports com.example.front_end.controller;
    opens com.example.front_end.controller to javafx.fxml;
    exports com.example.front_end.model;
    opens com.example.front_end.model to javafx.fxml;
    exports com.example.front_end.appUtils;
    opens com.example.front_end.appUtils to javafx.fxml;
}