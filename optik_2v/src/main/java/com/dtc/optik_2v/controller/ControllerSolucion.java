package com.dtc.optik_2v.controller;

import com.dtc.optik_2v.ControllerPrincipal;
import com.dtc.optik_2v.model.Accesorio;
import com.dtc.optik_2v.model.Armazon;
import com.dtc.optik_2v.model.Producto;
import com.dtc.optik_2v.model.Solucion;
import com.google.gson.Gson;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextArea;
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

public class ControllerSolucion implements Initializable {

    @FXML
    private TableView<Solucion> tblvSoluciones;

    @FXML
    private TableColumn<Solucion, String> tblcNombre;

    @FXML
    private TableColumn<Solucion, String > tblcMarca;

    @FXML
    private TableColumn<Solucion, Double> tblcPrecioCompra;

    @FXML
    private TableColumn<Solucion, Double> tblcPrecioVenta;

    @FXML
    private TableColumn<Solucion, Integer> tblcExistencia;

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
    private JFXTextArea txtaDescripcion;

    @FXML
    private JFXTextField txtCodigoSolucion;

    @FXML
    private JFXTextField txtCodigoProducto;

    @FXML
    private JFXButton btnGuardar;

    @FXML
    private JFXButton btnEliminar;

    @FXML
    private JFXButton btnNuevo;

    @FXML
    private JFXTextArea txtaB64;
    Array[] soluciones;
    Gson gson = null;
    List<Solucion> listaSolucion;
    HttpResponse<JsonNode> apiResponse = null;
    ObservableList<Solucion> listaSoluciones;

    private int posicionSolucion;


    public List<Solucion> getall() throws UnirestException {
        try {

            HttpResponse<JsonNode> apiResponse = Unirest.get("http://dtcdev.com.mx/optik_dtc/api/solucion/getAll").asJson();
            String json = apiResponse.getBody().toString();
            Solucion[] dato = new Gson().fromJson(json, Solucion[].class);
            listaSolucion = new ArrayList(Arrays.asList(dato));
            return listaSolucion;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listaSolucion;
    }

    public void save() throws UnirestException {
        Solucion s = new Solucion();
        Producto p = new Producto();

        if (txtCodigoProducto.getText().length() < 1) {
            s.setIdSolucion(0);
            p.setIdProducto(0);
        } else {
            s.setIdSolucion(Integer.parseInt(txtCodigoSolucion.getText()));
            p.setIdProducto(Integer.parseInt(txtCodigoProducto.getText()));
        }
        p.setCodigoBarras("");
        p.setNombre(txtNombre.getText());
        p.setMarca(txtMarca.getText());
        p.setPrecioCompra(Double.parseDouble(txtPrecioCompra.getText()));
        p.setPrecioVenta(Double.parseDouble(txtPrecioVenta.getText()));
        p.setExistencias(Integer.parseInt(txtExistencia.getText()));
        p.setEstatus(1);
        s.setProducto(p);

        try {
            String datos;

            gson = new Gson();
            datos = gson.toJson(s);
            System.out.println("Envio: " + datos);
            apiResponse = Unirest.post("http://dtcdev.com.mx/optik_dtc/api/solucion/save")
                    .field("datosSolucion", datos).asJson();

            System.out.println("Respuesta: " + apiResponse.getBody().toString());
            System.out.println("Estatus: " + apiResponse.getStatus());
            llenarTabla();
        } catch (Exception e) {
            e.printStackTrace();

        }
    }

    public void llenarTabla() throws UnirestException {
        tblcNombre.setCellValueFactory(param -> new SimpleObjectProperty<>(param.getValue().getProducto().getNombre()));
        tblcMarca.setCellValueFactory(param -> new SimpleObjectProperty<>(param.getValue().getProducto().getMarca()));
        tblcPrecioCompra.setCellValueFactory(param -> new SimpleObjectProperty<>(param.getValue().getProducto().getPrecioCompra()));
        tblcPrecioVenta.setCellValueFactory(param -> new SimpleObjectProperty<>(param.getValue().getProducto().getPrecioVenta()));
        tblcExistencia.setCellValueFactory(param -> new SimpleObjectProperty<>(param.getValue().getProducto().getExistencias()));

        listaSoluciones = FXCollections.observableArrayList(getall());
        tblvSoluciones.setItems(listaSoluciones);


    }

    public void limpiarFormulario() {
        txtCodigoProducto.setText("");
        txtNombre.setText("");
        txtCodigoSolucion.setText("");
        txtExistencia.setText("");
        txtMarca.setText("");
        txtPrecioCompra.setText("");
        txtPrecioVenta.setText("");
    }

    public Solucion getTabla(){
        if (tblvSoluciones != null){
            List<Solucion> tabla = tblvSoluciones.getSelectionModel().getSelectedItems();
            if (tabla.size() == 1){
                final Solucion competicionSeleccionada = tabla.get(0);
                return competicionSeleccionada;
            }
        }return null;
    }

    private void ponerSolucion() {
        final Solucion solucion = getTabla();
        posicionSolucion = listaSolucion.indexOf(solucion);
        if (solucion != null) {
            txtNombre.setText(solucion.getProducto().getNombre());
            txtMarca.setText(solucion.getProducto().getMarca());

            txtPrecioCompra.setText(String.valueOf(solucion.getProducto().getPrecioCompra()));
            txtPrecioVenta.setText(String.valueOf(solucion.getProducto().getPrecioVenta()));

            txtExistencia.setText(String.valueOf(solucion.getProducto().getExistencias()));
            txtaDescripcion.setText(solucion.getDescripcion());

            txtCodigoSolucion.setText(String.valueOf(solucion.getIdSolucion()));
            txtCodigoProducto.setText(String.valueOf(solucion.getProducto().getIdProducto()));
        }
    }

    private final ListChangeListener<Solucion> selectorSolucion = s -> ponerSolucion();


    public void remove() {
        HttpResponse<JsonNode> apiResponse = null;
        String solucion = new Gson().toJson(tblvSoluciones.getSelectionModel().getSelectedItem());

        try {
            apiResponse = Unirest.post("http://dtcdev.com.mx/optik_dtc/api/solucion/remove")
                    .header("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8")
                    .field("datosSolucion", solucion).asJson();
            llenarTabla();
            limpiarFormulario();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        final ObservableList<Solucion> tablaSolucionSel = tblvSoluciones.getSelectionModel().getSelectedItems();
        tablaSolucionSel.addListener(selectorSolucion);

        try {
            llenarTabla();
        } catch (UnirestException e) {
            throw new RuntimeException(e);
        }
    }
}
