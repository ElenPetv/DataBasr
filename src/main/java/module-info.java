module com.elen.dbmanager {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires io.reactivex.rxjava3;
    requires org.pdfsam.rxjavafx;

    opens com.elen.dbmanager to javafx.fxml;
    exports com.elen.dbmanager;
}