package com.dtc.optik_2v;

import com.jfoenix.controls.JFXButton;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;

public class ControllerPrincipal {

    @FXML
    private AnchorPane contenedor_principal;

    @FXML
    private JFXButton btnCerrarSesion;

    @FXML
    private JFXButton btnEmpleados;

    @FXML
    private JFXButton btnClientes;

    @FXML
    private JFXButton btnAccesorios;

    @FXML
    private JFXButton btnArmazon;

    @FXML
    private JFXButton btnLentesContacto;

    @FXML
    private JFXButton btnSoluciones;
    @FXML
    private BorderPane pane;

    //Pane view;
    AnchorPane view;

    public void principal() {
        limpiarPanel();

        AnchorPane obtener = getView("imagenPrincipal");
        AnchorPane pane1 = new AnchorPane(obtener);

        AnchorPane.setTopAnchor(obtener, 0.0);
        AnchorPane.setBottomAnchor(obtener, 0.0);
        AnchorPane.setLeftAnchor(obtener, 0.0);
        AnchorPane.setRightAnchor(obtener, 0.0);

        pane.setCenter(pane1);
    }

    public void empleados() {
        limpiarPanel();

        AnchorPane obtener = getView("empleado");
        AnchorPane pane1 = new AnchorPane(obtener);

        AnchorPane.setTopAnchor(obtener, 0.0);
        AnchorPane.setBottomAnchor(obtener, 0.0);
        AnchorPane.setLeftAnchor(obtener, 0.0);
        AnchorPane.setRightAnchor(obtener, 0.0);

        pane.setCenter(pane1);
    }

    public void clientes() {
        limpiarPanel();

        AnchorPane obtener = getView("cliente");
        AnchorPane pane1 = new AnchorPane(obtener);

        AnchorPane.setTopAnchor(obtener, 0.0);
        AnchorPane.setBottomAnchor(obtener, 0.0);
        AnchorPane.setLeftAnchor(obtener, 0.0);
        AnchorPane.setRightAnchor(obtener, 0.0);

        pane.setCenter(pane1);
    }

    public void accesorio() {
        limpiarPanel();

        AnchorPane obtener = getView("accesorio");
        AnchorPane pane1 = new AnchorPane(obtener);

        AnchorPane.setTopAnchor(obtener, 0.0);
        AnchorPane.setBottomAnchor(obtener, 0.0);
        AnchorPane.setLeftAnchor(obtener, 0.0);
        AnchorPane.setRightAnchor(obtener, 0.0);

        pane.setCenter(pane1);
    }

    public void armazon() {
        limpiarPanel();

        AnchorPane obtener = getView("armazon");
        AnchorPane pane1 = new AnchorPane(obtener);

        AnchorPane.setTopAnchor(obtener, 0.0);
        AnchorPane.setBottomAnchor(obtener, 0.0);
        AnchorPane.setLeftAnchor(obtener, 0.0);
        AnchorPane.setRightAnchor(obtener, 0.0);

        pane.setCenter(pane1);
    }

    public void solucion() {
        limpiarPanel();

        AnchorPane obtener = getView("solucion");
        AnchorPane pane1 = new AnchorPane(obtener);

        AnchorPane.setTopAnchor(obtener, 0.0);
        AnchorPane.setBottomAnchor(obtener, 0.0);
        AnchorPane.setLeftAnchor(obtener, 0.0);
        AnchorPane.setRightAnchor(obtener, 0.0);

        pane.setCenter(pane1);
    }

    public void lente_contacto() {
        limpiarPanel();

        AnchorPane obtener = getView("lente_contacto");
        AnchorPane pane1 = new AnchorPane(obtener);

        AnchorPane.setTopAnchor(obtener, 0.0);
        AnchorPane.setBottomAnchor(obtener, 0.0);
        AnchorPane.setLeftAnchor(obtener, 0.0);
        AnchorPane.setRightAnchor(obtener, 0.0);

        pane.setCenter(pane1);
    }

    public void limpiarPanel(){
        pane.getChildren().clear();
    }

    private AnchorPane getView(String fileName) {
        try {
            URL file = HelloApplication.class.getResource("admin/" + fileName + ".fxml");
            if (file == null) {
                throw new java.io.FileNotFoundException("Archivo no encontrado");
            }

            view = new FXMLLoader().load(file);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return view;
    }

}