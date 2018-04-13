package gestionar.soft3.inge.gestionar;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Toast;

import com.jaredrummler.materialspinner.MaterialSpinner;

import gestionar.soft3.inge.gestionar.ApiRest.ApiRest;
import gestionar.soft3.inge.gestionar.Utilidades.URLS;
import gestionar.soft3.inge.gestionar.Utilidades.Utilidades;
import gestionar.soft3.inge.gestionar.pojo.Afiliado;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RegistrarAfiliacion extends AppCompatActivity {

    MaterialSpinner spinner_eps,spinner_pension,spinner_arl;
    EditText editText_nombre,editText_apellidos,editText_direccion,editText_telefono,editText_cedula,editText_costo;
    com.shawnlin.numberpicker.NumberPicker numberPicker;

    Context context;
    ApiRest apiRest;
    Retrofit retrofit;


    String eps_array[]={"Medimas", "EmssanarEPS", "SOS EPS", "Sura EPS", "Coomeva EPS"};
    String arl_array[]={"Proteccion", "Colpensiones", "Colfondos"};
    String pension_array[]={"Sura", "Colpatria", "Colmena", "Positiva"};

    String arl="",eps="",pension="";


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_afiliaciones);
        eps = eps_array[0];
        arl = arl_array[0];
        pension = pension_array[0];
        iniciarComponentes();


        findViewById(R.id.btn_registro).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                registrar();
            }
        });




    }

    private void registrar()
    {
        String nombre = editText_nombre.getText().toString();
        String apellidos = editText_apellidos.getText().toString();
        String cedula = editText_cedula.getText().toString();
        String direccion = editText_direccion.getText().toString();
        String telefono = editText_telefono.getText().toString();
        int rango = numberPicker.getValue();
        String costo =editText_costo.getText().toString();

        if (Utilidades.validarCampo(nombre)||Utilidades.validarCampo(apellidos)||Utilidades.validarCampo(cedula)||
                Utilidades.validarCampo(direccion)||Utilidades.validarCampo(rango+"")||Utilidades.validarCampo(costo+"")
                )
        {
            Toast.makeText(getApplicationContext(),"Faltan campos por llenar",Toast.LENGTH_SHORT).show();
        }else
        {
            Afiliado afiliado= new Afiliado();
            afiliado.setNombres(nombre);
            afiliado.setApellidos(apellidos);
            afiliado.setCedula(cedula);
            afiliado.setDireccion(direccion);
            afiliado.setTelefono(telefono);
            afiliado.setArl(arl);
            afiliado.setEps(eps);
            afiliado.setPension(pension);
            afiliado.setRango(rango);
            afiliado.setCosto(Double.parseDouble(costo));

            Call<Afiliado> registro = apiRest.registro_afiliado(afiliado);
            registro.enqueue(new Callback<Afiliado>() {
                @Override
                public void onResponse(Call<Afiliado> call, Response<Afiliado> response)
                {

                    if (response.code() == 201)
                    {
                        Toast.makeText(getApplicationContext(),"Registro correcto",Toast.LENGTH_LONG).show();
                        finish();
                    }
                    else if (response.code() == 302)
                    {
                        Toast.makeText(getApplicationContext(),"Usuario ya existe",Toast.LENGTH_SHORT).show();
                    }
                }
                @Override
                public void onFailure(Call<Afiliado> call, Throwable t) {

                }
            });
        }





    }

    private void iniciarComponentes()
    {

        spinner_eps = (MaterialSpinner) findViewById(R.id.spinner_eps);
        spinner_eps.setItems(eps_array);
        spinner_eps.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<String>() {

        @Override public void onItemSelected(MaterialSpinner view, int position, long id, String item)
        {
            eps = item;
        }
    });

        spinner_pension= (MaterialSpinner) findViewById(R.id.spinner_pension);
        spinner_pension.setItems(pension_array);
        spinner_pension.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<String>() {

            @Override public void onItemSelected(MaterialSpinner view, int position, long id, String item)
            {
                pension = item;
            }
        });

        spinner_arl = (MaterialSpinner) findViewById(R.id.spinner_arl);
        spinner_arl.setItems(arl_array);
        spinner_arl.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<String>() {

        @Override public void onItemSelected(MaterialSpinner view, int position, long id, String item)
        {
            arl = item;
        }
    });


        editText_nombre = findViewById(R.id.editText_nombre);
        editText_apellidos = findViewById(R.id.editText_apellidos);
        editText_direccion = findViewById(R.id.editText_direccion);
        editText_telefono = findViewById(R.id.editText_telefono);
        editText_cedula = findViewById(R.id.editText_cedula);
        editText_costo = findViewById(R.id.editText_costo);
        numberPicker = (com.shawnlin.numberpicker.NumberPicker) findViewById(R.id.number_picker_local);

        retrofit_clase();


    }

    private void retrofit_clase()
    {
        retrofit = new Retrofit.Builder()
                .baseUrl(URLS.SERVER)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        apiRest=retrofit.create(ApiRest.class);
    }

}
