module com.example.demoproject {
    requires javafx.controls;
    requires javafx.fxml;
    requires mysql.connector.j;
            
        requires org.controlsfx.controls;
            requires com.dlsc.formsfx;
    requires java.sql;

    opens com.example.demoproject to javafx.fxml;
    exports com.example.demoproject;
}