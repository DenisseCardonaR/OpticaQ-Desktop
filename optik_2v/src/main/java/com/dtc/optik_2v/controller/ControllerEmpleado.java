package com.dtc.optik_2v.controller;

import com.dtc.optik_2v.ControllerPrincipal;
import com.dtc.optik_2v.model.Empleado;
import com.dtc.optik_2v.model.Persona;
import com.dtc.optik_2v.model.Usuario;
import com.google.gson.Gson;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXPasswordField;
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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.util.converter.LocalDateStringConverter;

import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

public class ControllerEmpleado implements Initializable {

    @FXML
    private TableView<Empleado> tblvEmpleado;

    @FXML
    private TableColumn<Empleado, String> tblcNombre;

    @FXML
    private TableColumn<Empleado, String> tblcEmail;

    @FXML
    private TableColumn<Empleado, String> tblcTelefonoCasa;

    @FXML
    private TableColumn<Empleado, String> tblcTelefonoMovil;

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
    private JFXTextField txtUsuario;

    @FXML
    private JFXComboBox<String> cbRol;

    @FXML
    private JFXPasswordField txtContraseña;

    @FXML
    private JFXPasswordField txtConfirmarContraseña;

    @FXML
    private JFXTextField txtIdEmpleado;

    @FXML
    private JFXTextField txtIdPersona;

    @FXML
    private JFXTextField txtIdUsuario;

    @FXML
    private JFXButton btnGuardar;

    @FXML
    private JFXButton btnEliminar;

    @FXML
    private JFXButton btnNuevo;

    private int posicionEmpleado;
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    ObservableList<String> listaGeneros;
    ObservableList<String> listaRol;
    List<Empleado> listaEmpleado;
    ObservableList<Empleado> listaEmpleados;
    HttpResponse<JsonNode> apiResponse = null;
    Gson gson = null;

    public List<Empleado> getall() throws UnirestException {
        try {
            HttpResponse<JsonNode> apiResponse = Unirest.get("http://dtcdev.com.mx/optik_dtc/api/empleado/getAll").asJson();
            ;
            String json = apiResponse.getBody().toString();
            Empleado[] dato = new Gson().fromJson(json, Empleado[].class);
            listaEmpleado = new ArrayList(Arrays.asList(dato));
            return listaEmpleado;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listaEmpleado;
    }

    public void llenarTabla() throws UnirestException {
        tblcNombre.setCellValueFactory(param -> new SimpleObjectProperty<>(param.getValue().getPersona().getNombre()));
        tblcEmail.setCellValueFactory(param -> new SimpleObjectProperty<>(param.getValue().getPersona().getEmail()));
        tblcTelefonoCasa.setCellValueFactory(param -> new SimpleObjectProperty<>(param.getValue().getPersona().getTelCasa()));
        tblcTelefonoMovil.setCellValueFactory(param -> new SimpleObjectProperty<>(param.getValue().getPersona().getTelMovil()));

        listaEmpleados = FXCollections.observableArrayList(getall());
        tblvEmpleado.setItems(listaEmpleados);
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
        txtUsuario.setText("");
        txtContraseña.setText("");
        txtConfirmarContraseña.setText("");
        cbRol.setItems(null);
        txtIdEmpleado.setText("");
        txtIdPersona.setText("");
        txtIdUsuario.setText("");
    }

    public Empleado getTabla() {
        if (tblvEmpleado != null) {
            List<Empleado> tabla = tblvEmpleado.getSelectionModel().getSelectedItems();
            if (tabla.size() == 1) {
                final Empleado competicionSeleccionada = tabla.get(0);
                return competicionSeleccionada;
            }
        }
        return null;
    }

    private void ponerEmpleado() {
        final Empleado empleado = getTabla();
        posicionEmpleado = listaEmpleados.indexOf(empleado);
        if (empleado != null) {
            txtNombre.setText(empleado.getPersona().getNombre());
            txtApellidoPaterno.setText(empleado.getPersona().getApellidoPaterno());
            txtApellidoMaterno.setText(empleado.getPersona().getApellidoMaterno());
            if (empleado.getPersona().getGenero().equals("M")) {
                cbGenero.setValue("Masculino");
            } else if (empleado.getPersona().getGenero().equals("F")) {
                cbGenero.setValue("Femenino");
            } else {
                cbGenero.setValue("Otro");
            }

            dpFechaNacimiento.setValue(LocalDate.parse(getFecha(empleado.getPersona().getFechaNacimiento())));
            txtCalle.setText(empleado.getPersona().getCalle());
            txtColonia.setText(empleado.getPersona().getColonia());
            txtNumeroDomicilio.setText(empleado.getPersona().getNumero());
            txtCodigoPostal.setText(empleado.getPersona().getCp());
            txtCiudad.setText(empleado.getPersona().getCiudad());
            txtEstado.setText(empleado.getPersona().getEstado());

            txtTelefonoCasa.setText(empleado.getPersona().getTelCasa());
            txtTelefonoMovil.setText(empleado.getPersona().getTelMovil());
            txtCorreoElectronico.setText(empleado.getPersona().getEmail());

            txtUsuario.setText(empleado.getUsuario().getNombre());
            txtContraseña.setText(empleado.getUsuario().getContrasenia());
            txtConfirmarContraseña.setText(empleado.getUsuario().getContrasenia());
            if (empleado.getUsuario().getRol().equals("Administrador")) {
                cbRol.setValue("Administrador");
            } else {
                cbRol.setValue("Empleado");
            }

            txtIdEmpleado.setText(String.valueOf(empleado.getIdEmpleado()));
            txtIdPersona.setText(String.valueOf(empleado.getPersona().getIdPersona()));
            txtIdUsuario.setText(String.valueOf(empleado.getUsuario().getIdUsuario()));
        }
    }

    //Para seleccionar una celda de la tabla
    private final ListChangeListener<Empleado> selectorEmpleados = new ListChangeListener<Empleado>() {
        @Override
        public void onChanged(ListChangeListener.Change<? extends Empleado> e) {
            ponerEmpleado();
        }
    };

    public void save() throws UnirestException {
        Empleado empleado = new Empleado();
        Persona p = new Persona();
        Usuario u = new Usuario();

        if (txtIdEmpleado.getText().length() < 1) {
            empleado.setIdEmpleado(0);
            p.setIdPersona(0);
            u.setIdUsuario(0);
        } else {
            empleado.setIdEmpleado(Integer.parseInt(txtIdEmpleado.getText()));
            p.setIdPersona(Integer.parseInt(txtIdPersona.getText()));
            u.setIdUsuario(Integer.parseInt(txtIdUsuario.getText()));
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

        u.setNombre(txtUsuario.getText());
        u.setContrasenia(txtContraseña.getText());
        if (cbRol.getSelectionModel().getSelectedItem().toString() == "Administrador") {
            u.setRol("Administrador");
        } else
            u.setRol("Empleado");

        empleado.setPersona(p);
        empleado.setUsuario(u);

        try {
            String datos = "";
            gson = new Gson();
            datos = gson.toJson(empleado);
            System.out.println(datos);
            apiResponse = Unirest.post("http://dtcdev.com.mx/optik_dtc/api/empleado/save")
                    .field("datosEmpleado", datos).asJson();
            System.out.println(apiResponse.getBody().toString());

        } catch (Exception e) {
            e.printStackTrace();
        }
        llenarTabla();
        limpiar();
    }

    public void remove() {
        HttpResponse<JsonNode> apiResponse = null;
        String empleado = new Gson().toJson(tblvEmpleado.getSelectionModel().getSelectedItem());

        try {
            apiResponse = Unirest.post("http://dtcdev.com.mx/optik_dtc/api/empleado/delete")
                    .header("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8")
                    .field("datosEmpleado", empleado).asJson();
            llenarTabla();
            limpiar();
        } catch (Exception e) {
            e.printStackTrace();
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

        listaRol = FXCollections.observableArrayList();
        listaRol.addAll("Administrador", "Empleado");
        cbRol.setItems(listaRol);

        final ObservableList<Empleado> tablaEmpleadoSel = tblvEmpleado.getSelectionModel().getSelectedItems();
        tablaEmpleadoSel.addListener(selectorEmpleados);

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
