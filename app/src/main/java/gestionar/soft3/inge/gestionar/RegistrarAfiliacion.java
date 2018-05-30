package gestionar.soft3.inge.gestionar;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Toast;

import com.jaredrummler.materialspinner.MaterialSpinner;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
    EditText editText_nombre,editText_apellidos,editText_direccion,editText_telefono,editText_cedula,editText_costo,editText_correo;
    com.shawnlin.numberpicker.NumberPicker numberPicker;

    Context context;
    ApiRest apiRest;
    Retrofit retrofit;


    String eps_array[]={"Medimas", "EmssanarEPS", "SOS EPS", "Sura EPS", "Coomeva EPS"};
    String pension_array[]={"Proteccion", "Colpensiones", "Colfondos"};
    String arl_array[]={"Sura", "Colpatria", "Colmena", "Positiva"};

    String arl="",eps="",pension="",montoFinal;


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


        editText_costo.addTextChangedListener(new TextWatcher()
        {
            public void afterTextChanged(Editable s)
            {
                editText_costo.setSelection(editText_costo.getText().length(), editText_costo.getText().length());
                montoFinal=Utilidades.stringMonetarioToDouble(editText_costo.getText().toString(),1);
                if (editText_costo.getText().toString().equals(montoFinal)) {
                } else {
                    editText_costo.setText(montoFinal);
                }
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {


            }
        });




    }


    private void registrar()
    {
        String nombre = editText_nombre.getText().toString();
        String apellidos = editText_apellidos.getText().toString();
        String cedula = editText_cedula.getText().toString();
        String correo = editText_correo.getText().toString();
        String direccion = editText_direccion.getText().toString();
        String telefono = editText_telefono.getText().toString();
        int rango = numberPicker.getValue();
        String costo = editText_costo.getText().toString();

        if (Utilidades.validarCampo(nombre)||Utilidades.validarCampo(apellidos)||Utilidades.validarCampo(cedula)||
                Utilidades.validarCampo(direccion)||Utilidades.validarCampo(correo) ||Utilidades.validarCampo(rango+"")||Utilidades.validarCampo(costo+"")
                )
        {
            Toast.makeText(getApplicationContext(),"Faltan campos por llenar",Toast.LENGTH_SHORT).show();
        }else
        {


            if(!validarEmailFuerte(correo))
            {
                Toast.makeText(getApplicationContext(),"Correo no valido",Toast.LENGTH_SHORT).show();
            }else
                {
                    costo = costo.split(",")[0];
                    Afiliado afiliado= new Afiliado(cedula,nombre,apellidos,correo,direccion,
                            telefono,eps,arl,pension,rango,costo);


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
                        public void onFailure(Call<Afiliado> call, Throwable t)
                        {
                            Toast.makeText(getApplicationContext(),"No se pudo registrar el usuario",Toast.LENGTH_SHORT).show();
                        }
                    });
                }
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
        editText_correo = findViewById(R.id.editText_correo);
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


    public static boolean validarEmailFuerte(String email){

        String regex = "^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$";

        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(email);

        return matcher.matches();
    }

}
