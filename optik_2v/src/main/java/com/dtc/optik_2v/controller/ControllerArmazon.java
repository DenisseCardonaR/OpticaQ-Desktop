package com.dtc.optik_2v.controller;

import com.dtc.optik_2v.ControllerPrincipal;
import com.dtc.optik_2v.model.Armazon;
import com.dtc.optik_2v.model.Producto;
import com.google.gson.Gson;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXColorPicker;
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
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

public class ControllerArmazon implements Initializable {

    @FXML
    private TableView<Armazon> tblvArmazon;

    @FXML
    private TableColumn<Armazon, String> tblcNombre;

    @FXML
    private TableColumn<Armazon, String> tblcMarca;

    @FXML
    private TableColumn<Armazon, Double> tblcPrecioCompra;

    @FXML
    private TableColumn<Armazon, Double> tblcPrecioVenta;

    @FXML
    private TableColumn<Armazon, Integer> tblcExistencia;

    @FXML
    private TextField txtFiltro;

    @FXML
    private JFXTextField txtNombre;

    @FXML
    private JFXTextField txtMarca;

    @FXML
    private JFXTextField txtModelo;

    @FXML
    private JFXTextField txtPrecioCompra;

    @FXML
    private JFXTextField txtPrecioVenta;

    @FXML
    private JFXTextArea txtaDescripcion;

    @FXML
    private JFXTextField txtDimensiones;

    @FXML
    private JFXTextField txtExistencia;

    @FXML
    private JFXColorPicker cpColor;

    @FXML
    private JFXTextField txtCodigoArmazon;

    @FXML
    private JFXTextField txtCodigoProducto;

    @FXML
    private ImageView imgArmazon;

    @FXML
    private JFXButton btnCargarImagen;

    @FXML
    private JFXButton btnExaminar;

    @FXML
    private Label lblArchivo;

    @FXML
    private JFXTextArea txtaB64;
    Array[] armazones;
    Gson gson = null;
    List<Armazon> listaArmazon;
    HttpResponse<JsonNode> apiResponse = null;
    ObservableList<Armazon> listaArmazones;

    private int posicionArmazon;
    public List<Armazon> getall() throws UnirestException {
        try {

            HttpResponse<JsonNode> apiResponse = Unirest.get("http://dtcdev.com.mx/optik_dtc/api/armazon/getAll").asJson();;
            String json = apiResponse.getBody().toString();
            Armazon[] dato = new Gson().fromJson(json, Armazon[].class);
            listaArmazon = new ArrayList(Arrays.asList(dato));
            return listaArmazon;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listaArmazon;
    }

    public void save() throws UnirestException {
        Armazon a = new Armazon();
        Producto p = new Producto();
        if (txtCodigoProducto.getText().length() < 1) {
            a.setIdArmazon(0);
            p.setIdProducto(0);
        } else {
            a.setIdArmazon(Integer.parseInt(txtCodigoArmazon.getText()));
            p.setIdProducto(Integer.parseInt(txtCodigoProducto.getText()));
        }
        p.setCodigoBarras("");
        p.setNombre(txtNombre.getText());
        p.setMarca(txtMarca.getText());
        p.setPrecioCompra(Double.parseDouble(txtPrecioCompra.getText()));
        p.setPrecioVenta(Double.parseDouble(txtPrecioVenta.getText()));
        p.setExistencias(Integer.parseInt(txtExistencia.getText()));
        a.setProducto(p);
        a.setModelo(txtModelo.getText());
        a.setColor(String.valueOf(cpColor.getValue()));
        a.setDimensiones(txtDimensiones.getText());
        a.setDescripcion(txtaDescripcion.getText());
        a.setFotografia("");
        try {
            String datos = "";
            gson = new Gson();
            datos = gson.toJson(a);
            System.out.println(datos);
            apiResponse = Unirest.post("http://dtcdev.com.mx/optik_dtc/api/armazon/save")
                    .field("datosArmazon",datos).asJson();
            System.out.println(apiResponse.getBody().toString());

        }catch (Exception e){
            e.printStackTrace();
        }
        llenarTabla();
        limpiarFormulario();
    }

    public void limpiarFormulario(){
        txtCodigoProducto.setText("");
        txtNombre.setText("");
        txtMarca.setText("");
        txtPrecioCompra.setText("");
        txtPrecioVenta.setText("");
        txtExistencia.setText("");
        txtCodigoArmazon.setText("");
        txtModelo.setText("");
        cpColor.setAccessibleText("");
        txtDimensiones.setText("");
        txtaDescripcion.setText("");
    }

    public void llenarTabla() throws UnirestException {
        tblcNombre.setCellValueFactory(param -> new SimpleObjectProperty<>(param.getValue().getProducto().getNombre()));
        tblcMarca.setCellValueFactory(param -> new SimpleObjectProperty<>(param.getValue().getProducto().getMarca()));
        tblcPrecioCompra.setCellValueFactory(param -> new SimpleObjectProperty<>(param.getValue().getProducto().getPrecioCompra()));
        tblcPrecioVenta.setCellValueFactory(param -> new SimpleObjectProperty<>(param.getValue().getProducto().getPrecioVenta()));
        tblcExistencia.setCellValueFactory(param -> new SimpleObjectProperty<>(param.getValue().getProducto().getExistencias()));

        listaArmazones = FXCollections.observableArrayList(getall());
        tblvArmazon.setItems(listaArmazones);


    }

    public Armazon getTabla(){
        if (tblvArmazon != null){
            List<Armazon> tabla = tblvArmazon.getSelectionModel().getSelectedItems();
            if (tabla.size() == 1){
                final Armazon competicionSeleccionada = tabla.get(0);
                return competicionSeleccionada;
            }
        }return null;
    }

    private void ponerArmazon() {
        final Armazon armazon = getTabla();
        posicionArmazon = listaArmazon.indexOf(armazon);
        if (armazon != null) {
            txtNombre.setText(armazon.getProducto().getNombre());
            txtMarca.setText(armazon.getProducto().getMarca());
            txtModelo.setText(armazon.getModelo());

            txtPrecioCompra.setText(String.valueOf(armazon.getProducto().getPrecioCompra()));
            txtPrecioVenta.setText(String.valueOf(armazon.getProducto().getPrecioVenta()));
            txtDimensiones.setText(armazon.getDimensiones());
            txtExistencia.setText(String.valueOf(armazon.getProducto().getExistencias()));
            txtaDescripcion.setText(armazon.getDescripcion());
            cpColor.setAccessibleText(armazon.getColor());

            txtCodigoArmazon.setText(String.valueOf(armazon.getIdArmazon()));
            txtCodigoProducto.setText(String.valueOf(armazon.getProducto().getIdProducto()));
        }
    }

    private final ListChangeListener<Armazon> selectorArmazon = new ListChangeListener<Armazon>() {
        @Override
        public void onChanged(ListChangeListener.Change<? extends Armazon> a) {
            ponerArmazon();
        }
    };

    public void remove(){
        HttpResponse<JsonNode> apiResponse = null;
        String armazon = new Gson().toJson(tblvArmazon.getSelectionModel().getSelectedItem());

        try{
            apiResponse = Unirest.post("http://dtcdev.com.mx/optik_dtc/api/armazon/remove")
                    .header("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8")
                    .field("datosArmazon",armazon).asJson();
            llenarTabla();
            limpiarFormulario();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    public void cerrar() {
        //ControllerPrincipal cp = new ControllerPrincipal();
        //cp.principal();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        final ObservableList<Armazon> tablaArmazonSel = tblvArmazon.getSelectionModel().getSelectedItems();
        tablaArmazonSel.addListener(selectorArmazon);

        try {
            llenarTabla();
        } catch (UnirestException e) {
            throw new RuntimeException(e);
        }
    }
}
