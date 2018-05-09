package gestionar.soft3.inge.gestionar;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.net.Inet4Address;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import gestionar.soft3.inge.gestionar.ApiRest.ApiRest;
import gestionar.soft3.inge.gestionar.Utilidades.CircleTransform;
import gestionar.soft3.inge.gestionar.Utilidades.URLS;
import gestionar.soft3.inge.gestionar.Utilidades.Utilidades;
import gestionar.soft3.inge.gestionar.fragments.DatePickerFragment;
import gestionar.soft3.inge.gestionar.pojo.Afiliado;
import gestionar.soft3.inge.gestionar.pojo.CitaMedica;
import gestionar.soft3.inge.gestionar.pojo.Ingreso;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ListaAfiliados extends AppCompatActivity {

    ArrayList<Afiliado> misAfiliados;
    ArrayList<Afiliado> temp;

    ListView listView;
    SearchView searchView;
    ApiRest apiRest;
    Retrofit retrofit;
    AdapterAsistentes afiliadosAdapter;
    Dialog customDialog = null;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_afiliados);
        listView = findViewById(R.id.listview_afiliados);
        searchView = findViewById(R.id.sv);

        cargarAfiliados();



    }

    private void cargarAfiliados()
     {
        retrofit_clase();
        Call c =  apiRest.obtenerAfiliados();
        c.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response)
            {
                if (response.code()==200)
                {
                    if (misAfiliados== null)
                    {
                        misAfiliados = new ArrayList<>();
                    }

                    misAfiliados = (ArrayList<Afiliado>) response.body();
                    afiliadosAdapter= new AdapterAsistentes(getApplicationContext(),misAfiliados);
                    listView.setAdapter(afiliadosAdapter);
                } if (response.code()==404)
            {
                Toast.makeText(getApplicationContext(),"No hay beneficiarios para mostrar",Toast.LENGTH_LONG).show();
            }



            }

            @Override
            public void onFailure(Call call, Throwable t) {

            }
        });


        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                afiliadosAdapter.getFilter().filter(newText);

                return false;
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                //Intent intent = new Intent(getActivity(), Chat_activity.class);
                if (temp!=null)
                {


                    showRadioButtonDialog(temp.get(position).getNombres() + " " +
                            temp.get(position).getApellidos() , temp.get(position).getCedula() ,temp.get(position));

                }
                else{

                    showRadioButtonDialog(misAfiliados.get(position).getNombres() + " " +
                            misAfiliados.get(position).getApellidos() , misAfiliados.get(position).getCedula(), misAfiliados.get(position));
                }
            }
        });

    }

    private void retrofit_clase()
    {
        retrofit = new Retrofit.Builder()
                .baseUrl(URLS.SERVER)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        apiRest=retrofit.create(ApiRest.class);
    }



    class AdapterAsistentes extends BaseAdapter implements Filterable {

        private LayoutInflater layoutInflater;
        Context context;
        CustomFilter filtro;
        ArrayList<Afiliado>afiliados;
        ArrayList<Afiliado> filtroList;


        public AdapterAsistentes(Context context,ArrayList<Afiliado>afiliados) {
            this.context = context;
            this.filtroList=afiliados;
            this.afiliados=afiliados;
            layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            return afiliados.size();
        }

        @Override
        public Object getItem(int position) {
            return afiliados.get(position);
        }

        @Override
        public long getItemId(int position) {
            return afiliados.indexOf(getItem(position));
        }


        @Override
        public View getView(int position, View convertView, ViewGroup parent)
        {
            ViewGroup viewGroup = (ViewGroup) layoutInflater.inflate(R.layout.adapter_list_afiliados, null);

            TextView nombre = viewGroup.findViewById(R.id.tv_nombre);
            TextView cedula =  viewGroup.findViewById(R.id.tv_cedula);
            ImageView imageView = viewGroup.findViewById(R.id.image_perfil);


            nombre.setText(afiliados.get(position).getNombres() + " " + afiliados.get(position).getApellidos());
            cedula.setText(afiliados.get(position).getCedula() + "");


            Picasso.with(getApplicationContext()).load(R.drawable.profile).transform(new CircleTransform()).into(imageView);

            return viewGroup;
        }

        @Override
        public Filter getFilter() {
            if(filtro == null){
                filtro = new CustomFilter();
            }

            return filtro;
        }

        class CustomFilter extends Filter{


            @Override
            protected FilterResults performFiltering(CharSequence constraint) {

                FilterResults resulst = new FilterResults();
                if(constraint != null && constraint.length()>0)
                {
                    constraint = constraint.toString();
                    ArrayList<Afiliado> filtro = new ArrayList<Afiliado>();

                    for(Integer i=0;i<filtroList.size();i++)
                    {
                        String nombre=filtroList.get(i).getCedula();
                        if(nombre.toLowerCase().contains((""+constraint).toLowerCase()))
                        {
                            Afiliado d= new Afiliado(filtroList.get(i).getCedula(),
                                    filtroList.get(i).getNombres(),
                                    filtroList.get(i).getApellidos(),
                                    filtroList.get(i).getCorreo(),
                                    filtroList.get(i).getDireccion(),
                                    filtroList.get(i).getTelefono(),
                                    filtroList.get(i).getEps(),
                                    filtroList.get(i).getArl(),
                                    filtroList.get(i).getPension(),
                                    filtroList.get(i).getRango(),
                                    filtroList.get(i).getCosto()
                            );

                            filtro.add(d);
                        }
                    }
                    resulst.count= filtro.size();
                    resulst.values = filtro;
                }else{
                    resulst.count= filtroList.size();
                    resulst.values = filtroList;
                }

                temp=(ArrayList<Afiliado>) resulst.values;
                return resulst;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                afiliados = (ArrayList<Afiliado>) results.values;
                notifyDataSetChanged();

            }
        }
    }


    private void showRadioButtonDialog(final String nombre, final String cedula, final Afiliado id) {


        customDialog = new Dialog(this);
        customDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        customDialog.setCancelable(false);
        customDialog.setContentView(R.layout.radiogroup);

        TextView tv_nom= customDialog.findViewById(R.id.tv_nombre);
        TextView tv_cedula= customDialog.findViewById(R.id.tv_Cedula_g);

        tv_nom.setText(nombre);
        tv_cedula.setText(cedula);

        Button btn_aceptar = customDialog.findViewById(R.id.btn_aceptar);
        Button btn_cancelar = customDialog.findViewById(R.id.btn_cancelar);

        btn_aceptar.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

                RadioGroup radioGroup = customDialog.findViewById(R.id.radio_grupo);


                if (radioGroup.getCheckedRadioButtonId() == R.id.radioButtonAgregar)
                {

                    customDialog.dismiss();
                    Intent intent =new Intent(getApplicationContext(),RegistroBeneficiario.class);
                    intent.putExtra("afiliado", id);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();

                }
                else  if (radioGroup.getCheckedRadioButtonId() == R.id.radioButtonVer)
                {
                    customDialog.dismiss();
                    mostrarInfo(id);
                }
                else  if (radioGroup.getCheckedRadioButtonId() == R.id.radioButtonActualizar)
                {

                    customDialog.dismiss();
                    Intent intent =new Intent(getApplicationContext(),CambiarDatos.class);
                    intent.putExtra("afiliado", id);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();

                }
                else  if (radioGroup.getCheckedRadioButtonId() == R.id.radioButtonEliminar)
                {
                    customDialog.dismiss();
                   eliminar(nombre,cedula);

                }
                else  if (radioGroup.getCheckedRadioButtonId() == R.id.radioButtonVerBeneficiarios)
                {
                    customDialog.dismiss();
                    Intent intent =new Intent(getApplicationContext(),VerBeneficiarios.class);
                    intent.putExtra("afiliado", id);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();

                }
                else  if (radioGroup.getCheckedRadioButtonId() == R.id.radioButtonAgregarIngreso)
                {
                    customDialog.dismiss();
                    agregarIngreso(nombre,cedula);
                } else  if (radioGroup.getCheckedRadioButtonId() == R.id.radioButtonAgregarCitaMedica)
                {
                    customDialog.dismiss();
                    agregarCitaMedica(nombre,cedula);
                }







            }
        });

        btn_cancelar.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {


                customDialog.dismiss();

            }
        });

        customDialog.show();
    }

    private void agregarCitaMedica(String nombre, final String cedula) {


        customDialog = new Dialog(ListaAfiliados.this);
        customDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        customDialog.setCancelable(false);
        customDialog.setContentView(R.layout.cita_medica);

        TextView tv_nom= customDialog.findViewById(R.id.tv_nombre);
        TextView tv_cedula= customDialog.findViewById(R.id.tv_Cedula_g);

        tv_nom.setText(nombre);
        tv_cedula.setText(cedula);

        final EditText editText_motivo = customDialog.findViewById(R.id.editText_motivo);
        final EditText editText_valor = customDialog.findViewById(R.id.editText_valor);
        final EditText editText_fecha = customDialog.findViewById(R.id.editText_fecha);
        final EditText editText_cedula= customDialog.findViewById(R.id.editText_cedula);
        final EditText editText_nombre= customDialog.findViewById(R.id.editText_nombre);


        Button btn_aceptar = customDialog.findViewById(R.id.btn_aceptar);
        Button btn_cancelar = customDialog.findViewById(R.id.btn_cancelar);

        editText_fecha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DatePickerFragment newFragment = DatePickerFragment.newInstance(new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        // +1 because january is zero
                        String dia="";
                        String mes="";
                        if (day<10)
                        {
                            dia = 0 + "" + day;
                        }
                        else
                        {
                            dia = day+"";
                        }
                        if (month<10)
                        {
                            mes = 0 + "" + (month+1);
                        }
                        else
                        {
                            mes = (month +1) +"";
                        }




                        final String selectedDate = year + "-" + mes + "-" + dia;
                        editText_fecha.setText(selectedDate);
                    }
                });
                newFragment.show(getFragmentManager(), "datePicker");




            }
        });


        btn_cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                customDialog.dismiss();
            }
        });
        btn_aceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {

                String motivo = editText_motivo.getText().toString();
                String valor = editText_valor.getText().toString();
                String fecha = editText_fecha.getText().toString();
                String cedula_dos = editText_cedula.getText().toString();
                String nombre = editText_nombre.getText().toString();



                if (Utilidades.validarCampo(motivo)||
                        Utilidades.validarCampo(valor) || Utilidades.validarCampo(fecha) ||
                        Utilidades.validarCampo(cedula_dos) || Utilidades.validarCampo(nombre))
                {
                    customDialog.dismiss();
                    Toast.makeText(getApplicationContext(),"Debes llenar todos los valores",Toast.LENGTH_SHORT).show();
                }else
                {


                    CitaMedica citaMedica = new CitaMedica();
                    citaMedica.setCedula(cedula);

                    citaMedica.setNombre(nombre);
                    citaMedica.setTipo_cita(motivo);
                    citaMedica.setCedula_dos(cedula_dos);
                    citaMedica.setValor(valor);
                    citaMedica.setFecha_cita(fecha);
                    citaMedica.setValor(valor);


                    Call<CitaMedica>c = apiRest.registro_cita(citaMedica);
                    c.enqueue(new Callback<CitaMedica>() {
                        @Override
                        public void onResponse(Call<CitaMedica> call, Response<CitaMedica> response)
                        {

                            if (response.code()==200)
                            {
                                Toast.makeText(getApplicationContext(),"Cita registrada " +
                                        "correctamente" , Toast.LENGTH_SHORT).show();
                                customDialog.dismiss();
                            }
                        }

                        @Override
                        public void onFailure(Call<CitaMedica> call, Throwable t) {
                            Toast.makeText(getApplicationContext(),"Problemas de " +
                                    "conexion, intente nuevamente" , Toast.LENGTH_SHORT).show();
                        }
                    });



                }


            }
        });
        customDialog.show();
    }

    private void agregarIngreso(String nombre, final String cedula) {


        customDialog = new Dialog(ListaAfiliados.this);
        customDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        customDialog.setCancelable(false);
        customDialog.setContentView(R.layout.ingreso);

        TextView tv_nom= customDialog.findViewById(R.id.tv_nombre);
        TextView tv_cedula= customDialog.findViewById(R.id.tv_Cedula_g);

        tv_nom.setText(nombre);
        tv_cedula.setText(cedula);

        final EditText editText_motivo = customDialog.findViewById(R.id.editText_motivo);
        final EditText editText_valor = customDialog.findViewById(R.id.editText_valor);


        Button btn_aceptar = customDialog.findViewById(R.id.btn_aceptar);
        Button btn_cancelar = customDialog.findViewById(R.id.btn_cancelar);


        btn_cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                customDialog.dismiss();
            }
        });
        btn_aceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {

                String motivo = editText_motivo.getText().toString();
                String valor = editText_valor.getText().toString();

                if (Utilidades.validarCampo(motivo)|| Utilidades.validarCampo(valor))
                {
                    customDialog.dismiss();
                    Toast.makeText(getApplicationContext(),"Debes llenar todos los valores",Toast.LENGTH_SHORT).show();
                }else
                {

                    Ingreso ingreso = new Ingreso();
                    ingreso.setCedula(cedula);
                    ingreso.setMotivo(motivo);
                    ingreso.setValor(valor);
                    Call<Ingreso>c = apiRest.registro_ingreso(ingreso);
                    c.enqueue(new Callback<Ingreso>() {
                        @Override
                        public void onResponse(Call<Ingreso> call, Response<Ingreso> response) {

                            if (response.code()==200)
                            {

                                Toast.makeText(getApplicationContext(),"Ingreso registrado correctamente",
                                        Toast.LENGTH_SHORT).show();
                                customDialog.dismiss();

                            }

                        }

                        @Override
                        public void onFailure(Call<Ingreso> call, Throwable t)
                        {
                            Toast.makeText(getApplicationContext(),"Problemas de " +
                                    "conexion, intente nuevamente" , Toast.LENGTH_SHORT).show();
                        }
                    });



                }


            }
        });
        customDialog.show();
    }


    public void eliminar(String nombre, final String cedula)
    {
        final AlertDialog.Builder builder;

        builder = new AlertDialog.Builder(ListaAfiliados.this);

        builder.setTitle("¿Estas seguro?")
                .setMessage("Eliminar afiliado " + nombre)
                .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, int which)
                    {

                        Call<gestionar.soft3.inge.gestionar.pojo.Response> ca = apiRest.eliminar(cedula);
                        ca.enqueue(new Callback<gestionar.soft3.inge.gestionar.pojo.Response>() {
                            @Override
                            public void onResponse(Call<gestionar.soft3.inge.gestionar.pojo.Response> call, Response<gestionar.soft3.inge.gestionar.pojo.Response> response) {
                                if (response.code()==200)
                                {
                                    Toast.makeText(getApplicationContext(),
                                            response.body().getMensaje(),Toast.LENGTH_SHORT).show();
                                    cargarAfiliados();
                                    dialog.cancel();
                                }
                            }

                            @Override
                            public void onFailure(Call<gestionar.soft3.inge.gestionar.pojo.Response> call, Throwable t) {

                            }
                        });



                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which)
                    {

                        dialog.cancel();
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    public void mostrarInfo(Afiliado afiliado)
    {
        customDialog = new Dialog(this);
        customDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        customDialog.setCancelable(false);
        customDialog.setContentView(R.layout.info);

        TextView tv_nom= customDialog.findViewById(R.id.tv_nombre);
        TextView tv_cedula= customDialog.findViewById(R.id.tv_Cedula_g);
        TextView tv_direccion= customDialog.findViewById(R.id.tv_direccion);
        TextView tv_telefono= customDialog.findViewById(R.id.tv_telefono);
        TextView tv_eps= customDialog.findViewById(R.id.tv_eps);
        TextView tv_arl= customDialog.findViewById(R.id.tv_arl);
        TextView tv_rango= customDialog.findViewById(R.id.tv_rango);
        TextView tv_pension= customDialog.findViewById(R.id.tv_pension);
        TextView tv_costo= customDialog.findViewById(R.id.tv_costo);

        tv_nom.setText(tv_nom.getText().toString()+afiliado.getNombres() + " " + afiliado.getApellidos());
        tv_cedula.setText(tv_cedula.getText().toString()+afiliado.getCedula());
        tv_direccion.setText(tv_direccion.getText().toString()+afiliado.getDireccion());
        tv_telefono.setText(tv_telefono.getText().toString()+afiliado.getTelefono());
        tv_eps.setText(tv_eps.getText().toString()+afiliado.getEps());
        tv_arl.setText(tv_arl.getText().toString()+afiliado.getArl());
        tv_pension.setText(tv_pension.getText().toString()+afiliado.getPension());
        tv_rango.setText(tv_rango.getText().toString()+afiliado.getRango()+"");
        tv_costo.setText(tv_costo.getText().toString()+afiliado.getCosto()+"");

        Button btn_aceptar = customDialog.findViewById(R.id.btn_aceptar);

        btn_aceptar.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                customDialog.dismiss();
            }
        });



        customDialog.show();

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent intent =new Intent(getApplicationContext(),MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();

    }
}
