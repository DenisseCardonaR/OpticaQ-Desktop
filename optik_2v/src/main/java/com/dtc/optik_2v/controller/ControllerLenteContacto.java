package com.dtc.optik_2v.controller;

import com.dtc.optik_2v.model.Armazon;
import com.dtc.optik_2v.model.Lente_Contacto;
import com.dtc.optik_2v.model.Producto;
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
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;

import java.net.URL;
import java.sql.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

public class ControllerLenteContacto implements Initializable{

    @FXML
    private TableView<Lente_Contacto> tblvLenteContacto;

    @FXML
    private TableColumn<Lente_Contacto, String> tblcNombre;

    @FXML
    private TableColumn<Lente_Contacto, String> tblcMarca;

    @FXML
    private TableColumn<Lente_Contacto, Double> tblcPrecioCompra;

    @FXML
    private TableColumn<Lente_Contacto, Double> tblcPrecioVenta;

    @FXML
    private TableColumn<Lente_Contacto, Integer> tblcExistencia;

    @FXML
    private TextField txtFiltro;

    @FXML
    private JFXTextField txtNombre;

    @FXML
    private JFXTextField txtMarca;

    @FXML
    private JFXTextField txtQueratometria;

    @FXML
    private JFXTextField txtPrecioCompra;

    @FXML
    private JFXTextField txtPrecioVenta;

    @FXML
    private ImageView imgLenteContacto;

    @FXML
    private JFXButton btnCargarImagen;

    @FXML
    private JFXButton btnExaminar;

    @FXML
    private Label lblArchivo;

    @FXML
    private JFXTextArea txtaB64;

    @FXML
    private JFXTextField txtExistencia;

    @FXML
    private JFXTextField txtCodigoLentesContacto;

    @FXML
    private JFXTextField txtCodigoProducto;

    @FXML
    private JFXTextField txtCodigoBarras;

    Array[]lentes;
    Gson gson = null;
    HttpResponse<JsonNode> apiResponse = null;
    List<Lente_Contacto> listaLenteContacto;
    ObservableList<Lente_Contacto> listaLentesContacto;
    private int posicionLente;
    public List<Lente_Contacto> getall() throws UnirestException {
        try {
            HttpResponse<JsonNode> apiResponse = Unirest.get("http://dtcdev.com.mx/optik_dtc/api/lente/getAll").asJson();;
            String json = apiResponse.getBody().toString();
            Lente_Contacto[] dato = new Gson().fromJson(json, Lente_Contacto[].class);
            listaLenteContacto = new ArrayList(Arrays.asList(dato));
            return listaLenteContacto;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listaLenteContacto;
    }

    public void save() throws UnirestException{
        Lente_Contacto l = new Lente_Contacto();
        Producto p = new Producto();
        if (txtCodigoProducto.getText().length() < 1) {
            l.setIdLenteContacto(0);
            p.setIdProducto(0);
        } else {
            l.setIdLenteContacto(Integer.parseInt(txtCodigoLentesContacto.getText()));
            p.setIdProducto(Integer.parseInt(txtCodigoProducto.getText()));
        }
        p.setCodigoBarras("");
        p.setNombre(txtNombre.getText());
        p.setMarca(txtMarca.getText());
        p.setPrecioCompra(Double.parseDouble(txtPrecioCompra.getText()));
        p.setPrecioVenta(Double.parseDouble(txtPrecioVenta.getText()));
        p.setExistencias(Integer.parseInt(txtExistencia.getText()));
        l.setProducto(p);
        l.setKeratometria(Integer.parseInt(txtQueratometria.getText()));
        l.setFotografia("");
        try {
            String datos = "";
            gson = new Gson();
            datos = gson.toJson(l);
            System.out.println(datos);
            apiResponse = Unirest.post("http://dtcdev.com.mx/optik_dtc/api/lente/save")
                    .field("datosLenteContacto",datos).asJson();
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
        txtCodigoLentesContacto.setText("");
        txtQueratometria.setText("");
    }
    public void llenarTabla() throws UnirestException {
        tblcNombre.setCellValueFactory(param -> new SimpleObjectProperty<>(param.getValue().getProducto().getNombre()));
        tblcMarca.setCellValueFactory(param -> new SimpleObjectProperty<>(param.getValue().getProducto().getMarca()));
        tblcPrecioCompra.setCellValueFactory(param -> new SimpleObjectProperty<>(param.getValue().getProducto().getPrecioCompra()));
        tblcPrecioVenta.setCellValueFactory(param -> new SimpleObjectProperty<>(param.getValue().getProducto().getPrecioVenta()));
        tblcExistencia.setCellValueFactory(param -> new SimpleObjectProperty<>(param.getValue().getProducto().getExistencias()));

        listaLentesContacto = FXCollections.observableArrayList(getall());
        tblvLenteContacto.setItems(listaLentesContacto);
    }
    public Lente_Contacto getTabla(){
        if (tblvLenteContacto != null){
            List<Lente_Contacto> tabla = tblvLenteContacto.getSelectionModel().getSelectedItems();
            if (tabla.size() == 1){
                final Lente_Contacto competicionSeleccionada = tabla.get(0);
                return competicionSeleccionada;
            }
        }return null;
    }
    private void ponerLenteContacto(){
        final Lente_Contacto lente_contacto = getTabla();
        posicionLente = listaLenteContacto.indexOf(lente_contacto);
        if (lente_contacto != null){
            txtNombre.setText(lente_contacto.getProducto().getNombre());
            txtMarca.setText(lente_contacto.getProducto().getMarca());

            txtPrecioCompra.setText(String.valueOf(lente_contacto.getProducto().getPrecioCompra()));
            txtPrecioVenta.setText(String.valueOf(lente_contacto.getProducto().getPrecioVenta()));
            txtQueratometria.setText(String.valueOf(lente_contacto.getKeratometria()));

            txtCodigoLentesContacto.setText(String.valueOf(lente_contacto.getIdLenteContacto()));
            txtCodigoProducto.setText(String.valueOf(lente_contacto.getProducto().getIdProducto()));
        }
    }

    private final ListChangeListener<Lente_Contacto> selectorLenteContacto = new ListChangeListener<Lente_Contacto>() {
        @Override
        public void onChanged(ListChangeListener.Change<? extends Lente_Contacto> l){ ponerLenteContacto();}
    };

    public void remove(){
        HttpResponse<JsonNode> apiresponse = null;
        String lente = new Gson().toJson(tblvLenteContacto.getSelectionModel().getSelectedItem());

        try{
            apiResponse = Unirest.post("http://dtcdev.com.mx/optik_dtc/api/lente/remove")
                    .header("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8")
                    .field("datosLenteContacto", lente).asJson();
            llenarTabla();
            limpiarFormulario();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        final ObservableList<Lente_Contacto> tablaLenteContactoSel = tblvLenteContacto.getSelectionModel().getSelectedItems();
        tablaLenteContactoSel.addListener(selectorLenteContacto);

        try {
            llenarTabla();
        } catch (UnirestException e) {
            throw new RuntimeException(e);
        }
    }
}



