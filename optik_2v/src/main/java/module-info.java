module com.dtc.optik_2v {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;

    requires com.jfoenix;
    requires gson;

    //para peticiones http
    requires unirest.java;

    opens com.dtc.optik_2v to javafx.fxml;
    opens com.dtc.optik_2v.model to gson;
    opens com.dtc.optik_2v.controller to javafx.fxml;
    exports com.dtc.optik_2v.model;
    exports com.dtc.optik_2v;
    exports com.dtc.optik_2v.controller;
}