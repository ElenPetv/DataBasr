module com.elen.dbmanager.databasemanager {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;

    opens com.elen.dbmanager to javafx.fxml;
    exports com.elen.dbmanager;
}