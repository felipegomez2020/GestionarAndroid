package gestionar.soft3.inge.gestionar.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Afiliado implements Serializable {

    @SerializedName("cedula")
    @Expose
    private String cedula;
    @SerializedName("ultima_afiliacion")
    @Expose
    private String ultima_afiliacion;

    @SerializedName("nombres")
    @Expose
    private String nombres;
    @SerializedName("apellidos")
    @Expose
    private String apellidos;
    @SerializedName("correo")
    @Expose
    private String correo;
    @SerializedName("direccion")
    @Expose
    private String direccion;
    @SerializedName("telefono")
    @Expose
    private String telefono;
    @SerializedName("eps")
    @Expose
    private String eps;
    @SerializedName("arl")
    @Expose
    private String arl;
    @SerializedName("pension")
    @Expose
    private String pension;
    @SerializedName("rango")
    @Expose
    private Integer rango;
    @SerializedName("costo")
    @Expose
    private Double costo;

    public String getCedula() {
        return cedula;
    }

    public void setCedula(String cedula) {
        this.cedula = cedula;
    }

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getEps() {
        return eps;
    }

    public void setEps(String eps) {
        this.eps = eps;
    }

    public String getArl() {
        return arl;
    }

    public void setArl(String arl) {
        this.arl = arl;
    }

    public Integer getRango() {
        return rango;
    }

    public void setRango(Integer rango) {
        this.rango = rango;
    }

    public Double getCosto() {
        return costo;
    }

    public void setCosto(Double costo) {
        this.costo = costo;
    }

    public String getPension() {
        return pension;
    }

    public void setPension(String pension) {
        this.pension = pension;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getUltima_afiliacion() {
        return ultima_afiliacion;
    }

    public void setUltima_afiliacion(String ultima_afiliacion) {
        this.ultima_afiliacion = ultima_afiliacion;
    }

    public Afiliado(String cedula, String nombres, String apellidos, String correo, String direccion, String telefono, String eps, String arl, String pension, Integer rango, Double costo) {
        this.cedula = cedula;
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.correo = correo;
        this.direccion = direccion;
        this.telefono = telefono;
        this.eps = eps;
        this.arl = arl;
        this.pension = pension;
        this.rango = rango;
        this.costo = costo;
    }

}