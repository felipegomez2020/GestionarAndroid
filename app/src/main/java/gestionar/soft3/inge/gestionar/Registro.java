package gestionar.soft3.inge.gestionar;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import gestionar.soft3.inge.gestionar.Utilidades.Utilidades;

public class Registro extends AppCompatActivity
{

    EditText editText_nombre,editText_apellido,editText_usuario,editText_pass,editText_repass;
    Spinner spinner_cargo;
    Context context;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);
        context = getApplicationContext();
        iniciarComponentes();
        findViewById(R.id.btn_registro).setOnClickListener(new View.OnClickListener()
        {
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
        String apellido = editText_apellido.getText().toString();
        String usuario = editText_usuario.getText().toString();
        String pass = editText_pass.getText().toString();
        String repass = editText_repass.getText().toString();
        if (Utilidades.validarCampo(nombre) || Utilidades.validarCampo(apellido) ||
                Utilidades.validarCampo(usuario) || Utilidades.validarCampo(pass) || Utilidades.validarCampo(repass))
        {
            Toast.makeText(getApplicationContext(), "Faltan campos por llenar", Toast.LENGTH_SHORT).show();
        } else
            {
            if (pass.equals(repass))
            {
                if (pass.length() >= 8)
                {
                    if (Utilidades.isNumeric(pass))
                    {

                        Toast.makeText(context, "La contraseña debe ser alfa-numerica", Toast.LENGTH_SHORT).show();
                    } else
                        {



                    }
                }
                else
                    {
                    Toast.makeText(context,
                            "Debe tener almenos 8 caracteres", Toast.LENGTH_SHORT).show();
                }

            } else
                {
                Toast.makeText(context,
                        "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show();
            }
        }


    }

    private void iniciarComponentes() {
        editText_nombre = findViewById(R.id.editText_nombre);
        editText_apellido = findViewById(R.id.editText_apellidos);
        editText_usuario = findViewById(R.id.editText_username);
        editText_pass = findViewById(R.id.editText_pass);
        editText_repass = findViewById(R.id.editText_repass);
        spinner_cargo = findViewById(R.id.spinner_cargo);
    }


    private void cambiarActivity(Class miClase)
    {
        Intent intent = new Intent(context, miClase);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        cambiarActivity(Login.class);
        super.onBackPressed();
    }
}