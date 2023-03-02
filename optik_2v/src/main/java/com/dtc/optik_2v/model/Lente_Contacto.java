package com.dtc.optik_2v.model;

public class Lente_Contacto {
    int idLenteContacto;
    int keratometria;
    String fotografia;
    Producto producto;

    public int getIdLenteContacto() {
        return idLenteContacto;
    }

    public void setIdLenteContacto(int idLenteContacto) {
        this.idLenteContacto = idLenteContacto;
    }

    public int getKeratometria() {
        return keratometria;
    }

    public void setKeratometria(int keratometria) {
        this.keratometria = keratometria;
    }

    public String getFotografia() {
        return fotografia;
    }

    public void setFotografia(String fotografia) {
        this.fotografia = fotografia;
    }

    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }
}
