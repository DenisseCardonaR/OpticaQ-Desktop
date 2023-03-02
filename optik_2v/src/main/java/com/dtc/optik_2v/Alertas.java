package com.dtc.optik_2v;

import javafx.scene.control.Alert;

public class Alertas {

    public void guardar(String txt){
        Alert guardar = new Alert(Alert.AlertType.CONFIRMATION, "Se guardo el "+txt+" correctamente");
        guardar.show();
    }

    public void eliminar(String text){
        Alert eliminar = new Alert(Alert.AlertType.CONFIRMATION, "Se elimino el "+text+" correctamente");
        eliminar.show();
    }

    public void warning(String text){
        Alert warning = new Alert(Alert.AlertType.WARNING, text);
        warning.show();
    }
}
