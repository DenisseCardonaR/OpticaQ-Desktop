package com.dtc.optik_2v.controller;

import com.dtc.optik_2v.ControllerPrincipal;
import com.dtc.optik_2v.model.Accesorio;
import com.dtc.optik_2v.model.Armazon;
import com.dtc.optik_2v.model.Producto;
import com.google.gson.Gson;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.sql.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

public class ControllerAccesorio implements Initializable {
    @FXML
    private TableView<Accesorio> tblvAccesorios;

    @FXML
    private TableColumn<Accesorio, String> tblcNombre;

    @FXML
    private TableColumn<Accesorio, String> tblcMarca;

    @FXML
    private TableColumn<Accesorio, Double> tblcPrecioCompra;

    @FXML
    private TableColumn<Accesorio, Double> tblcPrecioVenta;

    @FXML
    private TableColumn<Accesorio, Integer> tblcExistencia;

    @FXML
    private TextField txtFiltro;

    @FXML
    private JFXTextField txtNombre;

    @FXML
    private JFXTextField txtMarca;

    @FXML
    private JFXTextField txtExistencia;

    @FXML
    private JFXTextField txtPrecioCompra;

    @FXML
    private JFXTextField txtPrecioVenta;

    @FXML
    private JFXTextField txtCodigoAccesorio;

    @FXML
    private JFXTextField txtCodigoProducto;

    @FXML
    private JFXButton btnGuardar;

    @FXML
    private JFXButton btnEliminar;

    @FXML
    private JFXButton btnNuevo;

    HttpResponse<JsonNode> apiResponse = null;
    Gson gson = null;
    Array[] accesorios;
    List<Accesorio> listaAccesorio;
    ObservableList<Accesorio> listaAccesorios;
    private int posicionAccesorio;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            llenarTabla();
        } catch (UnirestException e) {
            throw new RuntimeException(e);
        }

        final ObservableList<Accesorio> tablaAccesorioSel = tblvAccesorios.getSelectionModel().getSelectedItems();
        tablaAccesorioSel.addListener(selectorAccesorios);
    }


    public List<Accesorio> getall() throws UnirestException {
        try {

            HttpResponse<JsonNode> apiResponse = Unirest.get("http://dtcdev.com.mx/optik_dtc/api/accesorio/getAll").asJson();
            String json = apiResponse.getBody().toString();
            Accesorio[] dato = new Gson().fromJson(json, Accesorio[].class);
            listaAccesorio = new ArrayList(Arrays.asList(dato));
            return listaAccesorio;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listaAccesorio;
    }

    public void llenarTabla() throws UnirestException {
        tblcNombre.setCellValueFactory(param -> new SimpleObjectProperty<>(param.getValue().getProducto().getNombre()));
        tblcMarca.setCellValueFactory(param -> new SimpleObjectProperty<>(param.getValue().getProducto().getMarca()));
        tblcPrecioCompra.setCellValueFactory(param -> new SimpleObjectProperty<>(param.getValue().getProducto().getPrecioCompra()));
        tblcPrecioVenta.setCellValueFactory(param -> new SimpleObjectProperty<>(param.getValue().getProducto().getPrecioVenta()));
        tblcExistencia.setCellValueFactory(param -> new SimpleObjectProperty<>(param.getValue().getProducto().getExistencias()));

        listaAccesorios = FXCollections.observableArrayList(getall());
        tblvAccesorios.setItems(listaAccesorios);


    }

    public void limpiarFormulario() {
        txtCodigoProducto.setText("");
        txtNombre.setText("");
        txtCodigoAccesorio.setText("");
        txtExistencia.setText("");
        txtMarca.setText("");
        txtPrecioCompra.setText("");
        txtPrecioVenta.setText("");
    }

    public void saveAccesorio() throws UnirestException {
        Accesorio a = new Accesorio();
        Producto p = new Producto();

        if (txtCodigoProducto.getText().length() < 1) {
            a.setIdAccesorio(0);
            p.setIdProducto(0);
        } else {
            a.setIdAccesorio(Integer.parseInt(txtCodigoAccesorio.getText()));
            p.setIdProducto(Integer.parseInt(txtCodigoProducto.getText()));
        }
        p.setCodigoBarras("");
        p.setNombre(txtNombre.getText());
        p.setMarca(txtMarca.getText());
        p.setPrecioCompra(Double.parseDouble(txtPrecioCompra.getText()));
        p.setPrecioVenta(Double.parseDouble(txtPrecioVenta.getText()));
        p.setExistencias(Integer.parseInt(txtExistencia.getText()));
        p.setEstatus(1);
        a.setProducto(p);

        try {
            String datos;

            gson = new Gson();
            datos = gson.toJson(a);
            System.out.println("Envio: " + datos);
            apiResponse = Unirest.post("http://dtcdev.com.mx/optik_dtc/api/accesorio/save")
                    .field("datosAccesorio", datos).asJson();

            System.out.println("Respuesta: " + apiResponse.getBody().toString());
            System.out.println("Estatus: " + apiResponse.getStatus());
            llenarTabla();
        } catch (Exception e) {
            e.printStackTrace();

        }
    }

    public void removeAccesorio() throws UnirestException {
        Accesorio a = new Accesorio();
        Producto p = new Producto();

        if (txtCodigoProducto.getText().length() < 1) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Selecciona un Accesorio");
            alert.showAndWait();
        } else {
            a.setIdAccesorio(Integer.parseInt(txtCodigoAccesorio.getText()));
            p.setIdProducto(Integer.parseInt(txtCodigoProducto.getText()));
            p.setCodigoBarras("");
            p.setNombre(txtNombre.getText());
            p.setMarca(txtMarca.getText());
            p.setPrecioCompra(Double.parseDouble(txtPrecioCompra.getText()));
            p.setPrecioVenta(Double.parseDouble(txtPrecioVenta.getText()));
            p.setExistencias(Integer.parseInt(txtExistencia.getText()));
            p.setEstatus(1);
            a.setProducto(p);

            try {
                String datos = "";
                gson = new Gson();
                datos = gson.toJson(a);
                System.out.println(datos);
                apiResponse = Unirest.post("http://localhost:8080/optik/api/accesorio/remove")
                        .field("datosAccesorio", datos).asJson();
                System.out.println(apiResponse.getBody().toString());
                llenarTabla();
                limpiarFormulario();
            } catch (Exception e) {
                e.printStackTrace();
                Alert alert = new Alert(Alert.AlertType.ERROR, apiResponse.getBody().toString());
                alert.showAndWait();
            }
        }

        llenarTabla();

    }

    public void cerrar() {
        //ControllerPrincipal cp = new ControllerPrincipal();
        //cp.principal();
    }

    private final ListChangeListener<Accesorio> selectorAccesorios = a -> ponerAccesorio();

    public Accesorio getTablaAccesorioSeleccionada() {
        if (tblvAccesorios != null) {
            List<Accesorio> tabla = tblvAccesorios.getSelectionModel().getSelectedItems();
            if (tabla.size() == 1) {
                final Accesorio competicionSeleccionada = tabla.get(0);
                return competicionSeleccionada;
            }
        }
        return null;
    }

    private void ponerAccesorio() {
        final Accesorio accesorio = getTablaAccesorioSeleccionada();
        posicionAccesorio = listaAccesorios.indexOf(accesorio);
        if (accesorio != null) {
            txtCodigoProducto.setText(String.valueOf(accesorio.getProducto().getIdProducto()));
            txtCodigoAccesorio.setText(String.valueOf(accesorio.getIdAccesorio()));
            txtNombre.setText(accesorio.getProducto().getNombre());
            txtMarca.setText(accesorio.getProducto().getMarca());
            txtPrecioVenta.setText(String.valueOf(accesorio.getProducto().getPrecioVenta()));
            txtPrecioCompra.setText(String.valueOf(accesorio.getProducto().getPrecioCompra()));
            txtExistencia.setText(String.valueOf(accesorio.getProducto().getExistencias()));
        }
    }

    public void refreshTablaAccesorios() {
        tblcNombre.setCellValueFactory(new PropertyValueFactory<Accesorio, String>("nombre"));
        tblcNombre.setCellValueFactory(new PropertyValueFactory<Accesorio, String>("marca"));
        tblcPrecioCompra.setCellValueFactory(new PropertyValueFactory<Accesorio, Double>("precioCompra"));
        tblcPrecioVenta.setCellValueFactory(new PropertyValueFactory<Accesorio, Double>("precioVenta"));
        tblcExistencia.setCellValueFactory(new PropertyValueFactory<Accesorio, Integer>("existencia"));

        listaAccesorios = FXCollections.observableArrayList();
        tblvAccesorios.setItems(listaAccesorios);
    }

    public void remove(){
        HttpResponse<JsonNode> apiResponse = null;
        String accesorio = new Gson().toJson(tblvAccesorios.getSelectionModel().getSelectedItem());

        try{
            apiResponse = Unirest.post("http://dtcdev.com.mx/optik_dtc/api/accesorio/remove")
                    .header("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8")
                    .field("datosAccesorio",accesorio).asJson();
            llenarTabla();
            limpiarFormulario();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
}
