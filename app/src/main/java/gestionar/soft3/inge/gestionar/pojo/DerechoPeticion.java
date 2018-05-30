package gestionar.soft3.inge.gestionar.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DerechoPeticion {



    @SerializedName("costo")
    @Expose
    private String costo;
    @SerializedName("descripcion")
    @Expose
    private String descripcion;
    @SerializedName("cedula")
    @Expose
    private String cedula;

    @SerializedName("afiliado")
    @Expose
    private String afiliado;

    public String getCosto() {
        return costo;
    }

    public void setCosto(String costo) {
        this.costo = costo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getCedula() {
        return cedula;
    }

    public void setCedula(String cedula) {
        this.cedula = cedula;
    }


    public String getAfiliado() {
        return afiliado;
    }

    public void setAfiliado(String afiliado) {
        this.afiliado = afiliado;
    }
}
