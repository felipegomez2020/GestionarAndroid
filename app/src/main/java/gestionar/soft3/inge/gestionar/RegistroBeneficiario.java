package gestionar.soft3.inge.gestionar;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.jaredrummler.materialspinner.MaterialSpinner;

import gestionar.soft3.inge.gestionar.ApiRest.ApiRest;
import gestionar.soft3.inge.gestionar.Utilidades.URLS;
import gestionar.soft3.inge.gestionar.Utilidades.Utilidades;
import gestionar.soft3.inge.gestionar.fragments.DatePickerFragment;
import gestionar.soft3.inge.gestionar.pojo.Afiliado;
import gestionar.soft3.inge.gestionar.pojo.Beneficiario;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RegistroBeneficiario extends AppCompatActivity {


    EditText editText_nombre,editText_apellidos,editText_parentesco,editText_cedula,editText_fecha,editText_cotizante;

    Context context;
    ApiRest apiRest;
    Retrofit retrofit;
    Afiliado objeto;
    String cedula;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro_beneficiario);
        iniciarComponentes();
        retrofit_clase();
        objeto = (Afiliado) getIntent().getExtras().getSerializable("afiliado");

        if (objeto==null)
        {
            Log.d("Prueba","null");
        }else
        {
            editText_cotizante.setText(objeto.getNombres() + " " + objeto.getApellidos());
             cedula = objeto.getCedula();
        }


        findViewById(R.id.btn_registro).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                registrarBeneficiario();
            }
        });

    }

    private void registrarBeneficiario()
    {

        String nombre = editText_nombre.getText().toString();
        String apellidos = editText_apellidos.getText().toString();
        String cedula = editText_cedula.getText().toString();
        String parentesco = editText_parentesco.getText().toString();
        String fecha = editText_fecha.getText().toString();


        if (Utilidades.validarCampo(nombre)||Utilidades.validarCampo(apellidos)||Utilidades.validarCampo(cedula)||
                Utilidades.validarCampo(parentesco)||Utilidades.validarCampo(fecha))
        {
            Toast.makeText(getApplicationContext(),"Faltan campos por llenar",Toast.LENGTH_SHORT).show();
        }
        else
            {
                Beneficiario beneficiario= new Beneficiario();
                beneficiario.setNombres(editText_nombre.getText().toString());
                beneficiario.setCedula_afiliado(this.cedula);
                beneficiario.setApellidos(editText_apellidos.getText().toString());
                beneficiario.setCedula(editText_cedula.getText().toString());
                beneficiario.setParentesco(editText_parentesco.getText().toString());
                beneficiario.setFecha_nacimiento(editText_fecha.getText().toString());


                Call<Beneficiario>call = apiRest.registro_beneficiario(beneficiario);
                call.enqueue(new Callback<Beneficiario>() {
                    @Override
                    public void onResponse(Call<Beneficiario> call, Response<Beneficiario> response)
                    {
                        if (response.code()==201)
                        {
                            Toast.makeText(getApplicationContext(),"Beneficiario registrado", Toast.LENGTH_SHORT).show();
                            Intent intent =new Intent(getApplicationContext(),ListaAfiliados.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            finish();
                        }else if (response.code()==302)
                        {
                            Toast.makeText(getApplicationContext(),"El usuario ya se encuentra registrado", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Beneficiario> call, Throwable t) {

                    }
                });
            }







    }

    private void iniciarComponentes()
    {
        editText_nombre = findViewById(R.id.editText_nombre);
        editText_cotizante = findViewById(R.id.editText_cotizandte);
        editText_apellidos = findViewById(R.id.editText_apellidos);
        editText_cedula = findViewById(R.id.editText_cedula);
        editText_parentesco = findViewById(R.id.editText_parentesco);
        editText_fecha = findViewById(R.id.editText_date);


        editText_fecha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DatePickerFragment newFragment = DatePickerFragment.newInstance(new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        // +1 because january is zero
                        final String selectedDate = day + " / " + (month+1) + " / " + year;
                        editText_fecha.setText(selectedDate);
                    }
                });
                newFragment.show(getFragmentManager(), "datePicker");
            }
        });
    }


    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        Intent intent =new Intent(getApplicationContext(),ListaAfiliados.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
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
