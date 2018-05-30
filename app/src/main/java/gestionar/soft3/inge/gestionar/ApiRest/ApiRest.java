package gestionar.soft3.inge.gestionar.ApiRest;

import java.util.List;

import gestionar.soft3.inge.gestionar.pojo.Afiliado;
import gestionar.soft3.inge.gestionar.pojo.Beneficiario;
import gestionar.soft3.inge.gestionar.pojo.CitaMedica;
import gestionar.soft3.inge.gestionar.pojo.DerechoPeticion;
import gestionar.soft3.inge.gestionar.pojo.Ingreso;
import gestionar.soft3.inge.gestionar.pojo.Response;
import gestionar.soft3.inge.gestionar.pojo.UsuarioAdministrativo;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface ApiRest {

    @FormUrlEncoded
    @POST("login/")
    Call<UsuarioAdministrativo> login(@Field("usuario") String usuario, @Field("pass") String pass);


    @FormUrlEncoded
    @POST("eliminar_afiliado/")
    Call <Response>eliminar(@Field("cedula") String cedula);

    @FormUrlEncoded
    @POST("obtener_beneficiarios/")
    Call <List<Beneficiario>>obtenerBeneficiarios(@Field("cedula_afiliado") String cedula);

    @FormUrlEncoded
    @POST("renovar_afiliacion/")
    Call <Response>renovarAfiliacion(@Field("cedula") String cedula,@Field("correo") String correo);

    @FormUrlEncoded
    @POST("enviar_correo/")
    Call <Response>enviarCorreo(@Field("correo") String correo);

    @POST("registrar_afiliado/")
    Call<Afiliado> registro_afiliado(@Body Afiliado afiliado);

    @POST("registrar_ingresos/")
    Call<Ingreso> registro_ingreso(@Body Ingreso afiliado);


    @POST("registro_cita/")
    Call<CitaMedica> registro_cita(@Body CitaMedica citaMedica);


    @POST("registrarDerecho/")
    Call<DerechoPeticion> registro_derecho(@Body DerechoPeticion derechoPeticion);


    @POST("registrar_beneficiario/")
    Call<Beneficiario> registro_beneficiario(@Body Beneficiario beneficiario);


    @POST("actualizar_afiliado/")
    Call<Afiliado> actualizarAfiliado(@Body Afiliado afiliado);

    @GET("obtener_afiliado/")
    Call <List<Afiliado>> obtenerAfiliados();

    @GET("obtener_afiliado_mora/")
    Call <List<Afiliado>> obtenerAfiliadosmora();


    @GET("obtener_ingresos/")
    Call <List<Ingreso>> obtenerIngresos();

    @GET("obtener_citas/")
    Call <List<CitaMedica>> obtenerCitas();

    @GET("obtenerDerechos/")
    Call <List<DerechoPeticion>> obtenerDerechos();

}
