package com.dtc.optik_2v.controller;

import com.dtc.optik_2v.Alertas;
import com.dtc.optik_2v.model.Cliente;
import com.dtc.optik_2v.model.Persona;
import com.google.gson.Gson;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDatePicker;
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
import javafx.util.converter.LocalDateStringConverter;

import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ControllerCliente implements Initializable {

    @FXML
    private TableView<Cliente> tblvClientes;

    @FXML
    private TableColumn<Cliente, String> tblcNombre;

    @FXML
    private TableColumn<Cliente, String> tblcEmail;

    @FXML
    private TableColumn<Cliente, String> tblcTelefonoCasa;

    @FXML
    private TableColumn<Cliente, String> tblcTelefonoMovil;

    @FXML
    private TextField txtFiltro;

    @FXML
    private JFXTextField txtNombre;

    @FXML
    private JFXTextField txtApellidoPaterno;

    @FXML
    private JFXTextField txtApellidoMaterno;

    @FXML
    private JFXComboBox<String> cbGenero;

    @FXML
    private JFXDatePicker dpFechaNacimiento;

    @FXML
    private JFXTextField txtCalle;

    @FXML
    private JFXTextField txtColonia;

    @FXML
    private JFXTextField txtNumeroDomicilio;

    @FXML
    private JFXTextField txtCodigoPostal;

    @FXML
    private JFXTextField txtCiudad;

    @FXML
    private JFXTextField txtEstado;

    @FXML
    private JFXTextField txtTelefonoCasa;

    @FXML
    private JFXTextField txtTelefonoMovil;

    @FXML
    private JFXTextField txtCorreoElectronico;

    @FXML
    private JFXTextField txtIdPersona;

    @FXML
    private JFXTextField txtIdCliente;

    @FXML
    private JFXButton btnGuardar;

    @FXML
    private JFXButton btnEliminar;

    @FXML
    private JFXButton btnNuevo;

    Alertas alertas = new Alertas();
    private int posicionCliente;
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    ObservableList<String> listaGeneros;
    List<Cliente> listaCliente;
    ObservableList<Cliente> listaClientes;
    HttpResponse<JsonNode> apiResponse = null;
    Gson gson = null;


    public boolean validarDatos() {
        boolean valido = false;
        // validar el email
        Pattern pattern = Pattern
                .compile("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                        + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");

        // El email a validar
        String email = txtCorreoElectronico.getText();

        Matcher mather = pattern.matcher(email);

        if (mather.find() == true) {
            valido = true;
        }
        return valido;
    }

    public List<Cliente> getall() throws UnirestException {
        try {

            HttpResponse<JsonNode> apiResponse = Unirest.get("http://dtcdev.com.mx/optik_dtc/api/cliente/getAll").asJson();
            ;
            String json = apiResponse.getBody().toString();
            Cliente[] dato = new Gson().fromJson(json, Cliente[].class);
            listaCliente = new ArrayList(Arrays.asList(dato));
            return listaCliente;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listaCliente;
    }

    public void llenarTabla() throws UnirestException {
        tblcNombre.setCellValueFactory(param -> new SimpleObjectProperty<>(param.getValue().getPersona().getNombre()));
        tblcEmail.setCellValueFactory(param -> new SimpleObjectProperty<>(param.getValue().getPersona().getEmail()));
        tblcTelefonoCasa.setCellValueFactory(param -> new SimpleObjectProperty<>(param.getValue().getPersona().getTelCasa()));
        tblcTelefonoMovil.setCellValueFactory(param -> new SimpleObjectProperty<>(param.getValue().getPersona().getTelMovil()));

        listaClientes = FXCollections.observableArrayList(getall());
        tblvClientes.setItems(listaClientes);
    }

    public void limpiar() {
        txtNombre.setText("");
        txtApellidoPaterno.setText("");
        txtApellidoMaterno.setText("");
        listaGeneros = FXCollections.observableArrayList();
        listaGeneros.addAll("Masculino", "Femenino", "Otro");
        cbGenero.setItems(listaGeneros);
        dpFechaNacimiento.setValue(null);
        txtCalle.setText("");
        txtColonia.setText("");
        txtNumeroDomicilio.setText("");
        txtCodigoPostal.setText("");
        txtCiudad.setText("");
        txtEstado.setText("");
        txtTelefonoCasa.setText("");
        txtTelefonoMovil.setText("");
        txtCorreoElectronico.setText("");
        txtIdCliente.setText("");
        txtIdPersona.setText("");
    }

    public Cliente getTabla() {
        if (tblvClientes != null) {
            List<Cliente> tabla = tblvClientes.getSelectionModel().getSelectedItems();
            if (tabla.size() == 1) {
                final Cliente competicionSeleccionada = tabla.get(0);
                return competicionSeleccionada;
            }
        }
        return null;
    }

    private void ponerCliente() {
        final Cliente cliente = getTabla();
        posicionCliente = listaClientes.indexOf(cliente);
        if (cliente != null) {
            txtNombre.setText(cliente.getPersona().getNombre());
            txtApellidoPaterno.setText(cliente.getPersona().getApellidoPaterno());
            txtApellidoMaterno.setText(cliente.getPersona().getApellidoMaterno());
            if (cliente.getPersona().getGenero() == "M") {
                cbGenero.setValue("Masculino");
            } else if (cliente.getPersona().getGenero() == "F") {
                cbGenero.setValue("Femenino");
            } else {
                cbGenero.setValue("Otro");
            }

            dpFechaNacimiento.setValue(LocalDate.parse(getFecha(cliente.getPersona().getFechaNacimiento())));
            txtCalle.setText(cliente.getPersona().getCalle());
            txtColonia.setText(cliente.getPersona().getColonia());
            txtNumeroDomicilio.setText(cliente.getPersona().getNumero());
            txtCodigoPostal.setText(cliente.getPersona().getCp());
            txtCiudad.setText(cliente.getPersona().getCiudad());
            txtEstado.setText(cliente.getPersona().getEstado());

            txtTelefonoCasa.setText(cliente.getPersona().getTelCasa());
            txtTelefonoMovil.setText(cliente.getPersona().getTelMovil());
            txtCorreoElectronico.setText(cliente.getPersona().getEmail());

            txtIdCliente.setText(String.valueOf(cliente.getIdCliente()));
            txtIdPersona.setText(String.valueOf(cliente.getPersona().getIdPersona()));
        }
    }

    //Para seleccionar una celda de la tabla
    private final ListChangeListener<Cliente> selectorClientes = new ListChangeListener<Cliente>() {
        @Override
        public void onChanged(ListChangeListener.Change<? extends Cliente> c) {
            ponerCliente();
        }
    };

    public void save() throws UnirestException {
        Cliente c = new Cliente();
        Persona p = new Persona();
        if (txtIdCliente.getText().length() < 1) {
            c.setIdCliente(0);
            p.setIdPersona(0);
        } else {
            c.setIdCliente(Integer.parseInt(txtIdCliente.getText()));
            p.setIdPersona(Integer.parseInt(txtIdPersona.getText()));
        }
        p.setNombre(txtNombre.getText());
        p.setApellidoPaterno(txtApellidoPaterno.getText());
        p.setApellidoMaterno(txtApellidoMaterno.getText());
        if (cbGenero.getSelectionModel().getSelectedItem().toString() == "Masculino") {
            p.setGenero("M");
        } else if (cbGenero.getSelectionModel().getSelectedItem().toString() == "Femenino") {
            p.setGenero("F");
        } else {
            p.setGenero("O");
        }
        System.out.println(dpFechaNacimiento.getValue());
        System.out.println(String.valueOf(dpFechaNacimiento.getValue()).length());
        String fecha = sendFecha(String.valueOf(dpFechaNacimiento.getValue()));
        p.setFechaNacimiento(fecha);

        p.setCalle(txtCalle.getText());
        p.setColonia(txtColonia.getText());
        p.setNumero(txtNumeroDomicilio.getText());
        p.setCp(txtCodigoPostal.getText());
        p.setCiudad(txtCiudad.getText());
        p.setEstado(txtEstado.getText());

        p.setTelCasa(txtTelefonoCasa.getText());
        p.setTelMovil(txtTelefonoMovil.getText());
        p.setEmail(txtCorreoElectronico.getText());

        c.setPersona(p);

        if (validarDatos()) {
            try {
                String datos = "";
                gson = new Gson();
                datos = gson.toJson(c);
                System.out.println(datos);
                apiResponse = Unirest.post("http://dtcdev.com.mx/optik_dtc/api/cliente/save")
                        .field("datosCliente", datos).asJson();
                System.out.println(apiResponse.getBody().toString());
                alertas.guardar("cliente");

            } catch (Exception e) {
                e.printStackTrace();
                alertas.warning("Rellena todos los campos");
            }
            llenarTabla();
            limpiar();
        } else {
            alertas.warning("El correo es invalido");
        }
    }

    public void remove() {
        HttpResponse<JsonNode> apiResponse = null;
        String cliente = new Gson().toJson(tblvClientes.getSelectionModel().getSelectedItem());

        try {
            apiResponse = Unirest.post("http://dtcdev.com.mx/optik_dtc/api/cliente/delete")
                    .header("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8")
                    .field("datosCliente", cliente).asJson();
            llenarTabla();
            limpiar();
            alertas.eliminar("cliente");
        } catch (Exception e) {
            e.printStackTrace();
            alertas.warning("Selecciona un cliente");
        }
    }

    public void cerrar() {
        //ControllerPrincipal cp = new ControllerPrincipal();
        //cp.principal();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        dpFechaNacimiento.setConverter(new LocalDateStringConverter(formatter, null));

        listaGeneros = FXCollections.observableArrayList();
        listaGeneros.addAll("Masculino", "Femenino", "Otro");
        cbGenero.setItems(listaGeneros);

        final ObservableList<Cliente> tablaClienteSel = tblvClientes.getSelectionModel().getSelectedItems();
        tablaClienteSel.addListener(selectorClientes);

        try {
            this.llenarTabla();
        } catch (UnirestException e) {
            throw new RuntimeException(e);
        }


    }

    private String sendFecha(String fecha) {
        String dia = fecha.substring(8, 10);
        String mes = fecha.substring(5, 7);
        String annio = fecha.substring(0, 4);

        String fecha_s = dia + "/" + mes + "/" + annio;
        return fecha_s;
    }

    private String getFecha(String fecha) {
        String dia = fecha.substring(0, 2);
        String mes = fecha.substring(3, 5);
        String annio = fecha.substring(6, 10);

        String fecha_s = annio + "-" + mes + "-" + dia;
        return fecha_s;
    }
}
