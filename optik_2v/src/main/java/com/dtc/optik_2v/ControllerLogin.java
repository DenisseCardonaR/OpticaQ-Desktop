package com.dtc.optik_2v;

import com.dtc.optik_2v.model.Empleado;
import com.dtc.optik_2v.model.Respuesta;
import com.google.gson.Gson;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class ControllerLogin implements Initializable {
    @FXML
    private JFXTextField txtUser;

    @FXML
    private JFXPasswordField txtPassword;
    @FXML
    private JFXButton btnLogin;


    private Empleado respuesta;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void login() throws UnirestException {
        try {
            String usuario = txtUser.getText();
            String password = txtPassword.getText();
            HttpResponse<String> apiResponse = Unirest.get("http://localhost:8080/optik_dtc/api/login/login")
                    .queryString("usuario", usuario)
                    .queryString("contrasenia", password).asString();

            System.out.println(apiResponse.getBody() + "\n" + apiResponse.getStatus());

            boolean in = apiResponse.getBody().contains("\"rol\":\"Administrador\"");


            if (in) {
                Stage stage = new Stage();
                Parent root = FXMLLoader.load(this.getClass().getResource("principal.fxml"));
                Scene scene = new Scene(root);
                stage.setScene(scene);
                stage.setTitle("Optica Qualite");
                Image img = new Image(this.getClass().getResource("image/logo.png").toURI().toString());
                stage.getIcons().add(img);
                stage.setMaximized(true);
                stage.show();
                stage = (Stage) btnLogin.getScene().getWindow();
                stage.close();
            }else {
                Alert a = new Alert(Alert.AlertType.ERROR, apiResponse.getBody().substring(10,27));
                a.showAndWait();
            }


        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
