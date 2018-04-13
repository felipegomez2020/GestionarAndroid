package gestionar.soft3.inge.gestionar.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Beneficiario {

    @SerializedName("nombres")
    @Expose
    private String nombres;
    @SerializedName("apellidos")
    @Expose
    private String apellidos;
    @SerializedName("cedula")
    @Expose
    private String cedula;

    @SerializedName("parentesco")
    @Expose
    private String parentesco;


    @SerializedName("fecha_nacimiento")
    @Expose
    private String fecha_nacimiento;
    @SerializedName("cedula_afiliado")
    @Expose
    private String cedula_afiliado;


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

    public String getCedula() {
        return cedula;
    }

    public void setCedula(String cedula) {
        this.cedula = cedula;
    }

    public String getFecha_nacimiento() {
        return fecha_nacimiento;
    }

    public void setFecha_nacimiento(String fecha_nacimiento) {
        this.fecha_nacimiento = fecha_nacimiento;
    }

    public String getCedula_afiliado() {
        return cedula_afiliado;
    }

    public void setCedula_afiliado(String cedula_afiliado) {
        this.cedula_afiliado = cedula_afiliado;
    }


    public String getParentesco() {
        return parentesco;
    }

    public void setParentesco(String parentesco) {
        this.parentesco = parentesco;
    }
}
