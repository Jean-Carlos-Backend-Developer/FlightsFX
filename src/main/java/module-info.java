module com.jeanespin.flightsfx {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;

    opens com.jeanespin.flightsfx to javafx.fxml;
    opens com.jeanespin.flightsfx.model to javafx.base;

    exports com.jeanespin.flightsfx;
}