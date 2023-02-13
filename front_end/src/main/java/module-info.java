module com.example.front_end {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.front_end to javafx.fxml;
    exports com.example.front_end;
}