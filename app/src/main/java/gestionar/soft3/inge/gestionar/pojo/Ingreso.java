package gestionar.soft3.inge.gestionar.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Ingreso
{
    @SerializedName("motivo")
    @Expose
    private String motivo;
    @SerializedName("valor")
    @Expose
    private String valor;
    @SerializedName("afiliado")
    @Expose
    private String cedula_afiliado;
    @SerializedName("date")
    @Expose
    private String fecha;


    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }

    public String getCedula_afiliado() {
        return cedula_afiliado;
    }

    public void setCedula_afiliado(String cedula_afiliado) {
        this.cedula_afiliado = cedula_afiliado;
    }


    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }
}
