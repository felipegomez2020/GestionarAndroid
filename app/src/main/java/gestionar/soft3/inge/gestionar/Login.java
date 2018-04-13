package gestionar.soft3.inge.gestionar;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import gestionar.soft3.inge.gestionar.ApiRest.ApiRest;
import gestionar.soft3.inge.gestionar.Utilidades.URLS;
import gestionar.soft3.inge.gestionar.pojo.UsuarioAdministrativo;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Login extends AppCompatActivity implements View.OnClickListener
{

    Button button_log;
    EditText user,pas;
    TextView no_cuenta;
    Context context;
    ApiRest apiRest;
    Retrofit retrofit;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        context = getApplicationContext();
        iniciarComponentes();


    }

    private void iniciarComponentes() {
        button_log = findViewById(R.id.btn_login);
        user = findViewById(R.id.editText_loging_tel);
        pas = findViewById(R.id.editText_loging_pass);
        no_cuenta = findViewById(R.id.tv_no_cuenta);
        button_log.setOnClickListener(this);
        no_cuenta.setOnClickListener(this);
    }

    private void cambiarActivity(Class miClase)
    {
        Intent intent = new Intent(context, miClase);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    private void iniciarSesion(String user,String pass)
    {

        retrofit_clase();
        Call<UsuarioAdministrativo>c = apiRest.login(user,pass);
        c.enqueue(new Callback<UsuarioAdministrativo>() {
            @Override
            public void onResponse(Call<UsuarioAdministrativo> call, Response<UsuarioAdministrativo> response)
            {

                Log.d("Prueba",response.code()+"");
                if (response.code() ==200)
                {
                    cambiarActivity(MainActivity.class);
                }
                else if (response.code() ==401)
                {
                    Toast.makeText(getApplicationContext(),"Contraseña incorrecta",Toast.LENGTH_SHORT).show();
                }
                else if (response.code() ==404)
                {
                    Toast.makeText(getApplicationContext(),"Usuario incorrecto",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<UsuarioAdministrativo> call, Throwable t) {
                Toast.makeText(getApplicationContext(),"Error de conexion",Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.btn_login:
                iniciarSesion(user.getText().toString(),pas.getText().toString());
                break;

            case R.id.tv_no_cuenta:
                cambiarActivity(Registro.class);
                break;

        }

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
