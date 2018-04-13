package gestionar.soft3.inge.gestionar;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
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

public class CambiarDatos extends AppCompatActivity {

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
        setContentView(R.layout.activity_cambiar_datos);


        iniciarComponentes();
        Afiliado objeto = (Afiliado) getIntent().getExtras().getSerializable("afiliado");

        if (objeto==null)
        {
            Log.d("Prueba","null");
        }else
            {
                editText_nombre.setText(objeto.getNombres());
                editText_apellidos.setText(objeto.getApellidos());
                editText_direccion.setText(objeto.getDireccion());
                editText_telefono.setText(objeto.getTelefono());
                editText_cedula.setText(objeto.getCedula());
                editText_costo.setText(objeto.getCosto()+"");
            }


        eps = eps_array[0];
        arl = arl_array[0];
        pension = pension_array[0];
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
        int rango = numberPicker.getValue();
        double costo =Double.parseDouble(editText_costo.getText().toString());

        if (Utilidades.validarCampo(nombre)||Utilidades.validarCampo(apellidos)||Utilidades.validarCampo(cedula)||
                Utilidades.validarCampo(direccion)||Utilidades.validarCampo(rango+"")||Utilidades.validarCampo(costo+"")
                )

        {
            Toast.makeText(getApplicationContext(),"Faltan campos por llenar",Toast.LENGTH_SHORT).show();
        }else
            {
                Afiliado afiliado= new Afiliado();
                afiliado.setNombres(nombre);
                afiliado.setApellidos(editText_apellidos.getText().toString());
                afiliado.setCedula(editText_cedula.getText().toString());
                afiliado.setDireccion(editText_direccion.getText().toString());
                afiliado.setTelefono(editText_telefono.getText().toString());
                afiliado.setArl(arl);
                afiliado.setEps(eps);
                afiliado.setPension(pension);
                afiliado.setRango(rango);
                afiliado.setCosto(costo);




                Call<Afiliado> registro = apiRest.actualizarAfiliado(afiliado);
                registro.enqueue(new Callback<Afiliado>() {
                    @Override
                    public void onResponse(Call<Afiliado> call, Response<Afiliado> response)
                    {

                        if (response.code() == 200)
                        {
                            Toast.makeText(getApplicationContext(),"Se ha actualizado correctamente",Toast.LENGTH_LONG).show();
                            Intent intent =new Intent(getApplicationContext(),ListaAfiliados.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            finish();
                        }
                        else if (response.code() == 302)
                        {
                            Toast.makeText(getApplicationContext(),"Usuario no existe",Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Afiliado> call, Throwable t) {

                    }
                });
            }





    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent =new Intent(getApplicationContext(),ListaAfiliados.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
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
