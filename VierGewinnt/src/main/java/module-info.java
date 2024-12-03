module org.example.viergewinnt {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;


    opens org.example.viergewinnt to javafx.fxml;
    exports org.example.viergewinnt;
}