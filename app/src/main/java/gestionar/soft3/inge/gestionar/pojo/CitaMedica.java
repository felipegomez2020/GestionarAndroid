package gestionar.soft3.inge.gestionar.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CitaMedica {


    @SerializedName("cedula")
    @Expose
    private String cedula;
    @SerializedName("tipo_cita")
    @Expose
    private String tipo_cita;
    @SerializedName("valor")
    @Expose
    private String valor;
    @SerializedName("fecha_cita")
    @Expose
    private String fecha_cita;
    @SerializedName("nombre")
    @Expose
    private String nombre;
    @SerializedName("cedula_dos")
    @Expose
    private String cedula_dos;

    public CitaMedica()
    {

    }

    public String getCedula() {
        return cedula;
    }

    public void setCedula(String cedula) {
        this.cedula = cedula;
    }

    public String getTipo_cita() {
        return tipo_cita;
    }

    public void setTipo_cita(String tipo_cita) {
        this.tipo_cita = tipo_cita;
    }

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }

    public String getFecha_cita() {
        return fecha_cita;
    }

    public void setFecha_cita(String fecha_cita) {
        this.fecha_cita = fecha_cita;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCedula_dos() {
        return cedula_dos;
    }

    public void setCedula_dos(String cedula_dos) {
        this.cedula_dos = cedula_dos;
    }


    public CitaMedica(String cedula, String tipo_cita, String valor, String fecha_cita, String nombre, String cedula_dos) {
        this.cedula = cedula;
        this.tipo_cita = tipo_cita;
        this.valor = valor;
        this.fecha_cita = fecha_cita;
        this.nombre = nombre;
        this.cedula_dos = cedula_dos;
    }
}
