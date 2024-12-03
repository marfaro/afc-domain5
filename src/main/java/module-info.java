module edu.gmu.cs321 {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;

    opens edu.gmu.cs321 to javafx.fxml;
    exports edu.gmu.cs321;

    requires org.apache.poi.ooxml; 
}
