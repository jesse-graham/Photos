module com.example.photos39 {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.photos39 to javafx.fxml;
    exports com.example.photos39;
}