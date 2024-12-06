module org.cpuscheduling {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;

    opens org.cpuscheduling to javafx.fxml;
    exports org.cpuscheduling;
}