package gestionar.soft3.inge.gestionar;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.CorrectionInfo;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;
import java.util.Map;
import gestionar.soft3.inge.gestionar.ApiRest.ApiRest;
import gestionar.soft3.inge.gestionar.Utilidades.CircleTransform;
import gestionar.soft3.inge.gestionar.Utilidades.URLS;
import gestionar.soft3.inge.gestionar.Utilidades.Utilidades;
import gestionar.soft3.inge.gestionar.pojo.Afiliado;
import gestionar.soft3.inge.gestionar.pojo.Ingreso;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ListMora extends AppCompatActivity {

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
        setContentView(R.layout.activity_list_mora);
        listView = findViewById(R.id.listview_afiliados);
        searchView = findViewById(R.id.sv);

        cargarAfiliados();



    }

    private void cargarAfiliados()
    {
        retrofit_clase();
        Call c =  apiRest.obtenerAfiliadosmora();
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
                Toast.makeText(getApplicationContext(),"No hay personas en mora",Toast.LENGTH_LONG).show();
                misAfiliados = new ArrayList<>();
                afiliadosAdapter= new AdapterAsistentes(getApplicationContext(),misAfiliados);
                listView.setAdapter(afiliadosAdapter);
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
                            temp.get(position).getApellidos() , temp.get(position).getCedula()
                            ,temp.get(position), temp.get(position).getUltima_afiliacion() , temp.get(position).getCorreo());

                }
                else{

                    showRadioButtonDialog(misAfiliados.get(position).getNombres() + " " +
                            misAfiliados.get(position).getApellidos() , misAfiliados.get(position).getCedula(),
                            misAfiliados.get(position) , misAfiliados.get(position).getUltima_afiliacion(),misAfiliados.get(position).getCorreo());
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
                    ArrayList<Afiliado> filtro = new ArrayList<>();

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
                        else
                        {
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

    private void showRadioButtonDialog(final String nombre, final String cedula, final Afiliado id, String ultima, final String correo) {


        customDialog = new Dialog(this);
        customDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        customDialog.setCancelable(false);
        customDialog.setContentView(R.layout.radiogroupmora);

        TextView tv_nom= customDialog.findViewById(R.id.tv_nombre);
        TextView tv_cedula= customDialog.findViewById(R.id.tv_Cedula_g);
        TextView tv_ultima= customDialog.findViewById(R.id.tv_fecha);

        tv_nom.setText("Nombre: "+ nombre);
        tv_cedula.setText("Cedula: "+cedula);
        tv_ultima.setText("Ultimo registro: " + ultima.split("T")[0]);

        Button btn_aceptar = customDialog.findViewById(R.id.btn_aceptar);
        Button btn_cancelar = customDialog.findViewById(R.id.btn_cancelar);

        btn_aceptar.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

                RadioGroup radioGroup = customDialog.findViewById(R.id.radio_grupo);


                if (radioGroup.getCheckedRadioButtonId() == R.id.radioButtonCorreoElectronico)
                {
                    customDialog.dismiss();
                    enviarCorreoElectronico();


                }
                else  if (radioGroup.getCheckedRadioButtonId() == R.id.radioButtonAgregarPago)
                {

                    customDialog.dismiss();
                    registrarPago();
                }


            }

            private void enviarCorreoElectronico()
            {
                final AlertDialog.Builder builder;

                builder = new AlertDialog.Builder(ListMora.this);

                builder.setTitle("¿Estas seguro?")
                        .setMessage("Deseas enviar correo electronico a " + nombre)
                        .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                            public void onClick(final DialogInterface dialog, int which)
                            {


                                Call<gestionar.soft3.inge.gestionar.pojo.Response>call = apiRest.enviarCorreo(correo);
                                call.enqueue(new Callback<gestionar.soft3.inge.gestionar.pojo.Response>() {
                                    @Override
                                    public void onResponse(Call<gestionar.soft3.inge.gestionar.pojo.Response> call, Response<gestionar.soft3.inge.gestionar.pojo.Response> response) {

                                        if (response.code() ==200)
                                        {
                                            Toast.makeText(getApplicationContext(),"Correo enviado satisfatoriamente",Toast.LENGTH_LONG).show();
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

            private void registrarPago()
            {

                final AlertDialog.Builder builder;

                builder = new AlertDialog.Builder(ListMora.this);

                builder.setTitle("¿Estas seguro?")
                        .setMessage("Deseas realizar activacion de nuevo pago a " + nombre)
                        .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                            public void onClick(final DialogInterface dialog, int which)
                            {

                                Call<gestionar.soft3.inge.gestionar.pojo.Response>responseCall = apiRest.renovarAfiliacion(cedula,correo);
                                responseCall.enqueue(new Callback<gestionar.soft3.inge.gestionar.pojo.Response>() {
                                    @Override
                                    public void onResponse(Call<gestionar.soft3.inge.gestionar.pojo.Response> call, Response<gestionar.soft3.inge.gestionar.pojo.Response> response) {

                                        if (response.code()==200)
                                        {
                                            cargarAfiliados();
                                            Toast.makeText(getApplicationContext(),"Perfil actualizado correctamente",Toast.LENGTH_SHORT).show();

                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<gestionar.soft3.inge.gestionar.pojo.Response> call, Throwable t)
                                    {
                                        Toast.makeText(getApplicationContext(),"No se puede hacer la solicitud, intente nuevamente",Toast.LENGTH_SHORT).show();
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent intent =new Intent(getApplicationContext(),MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();

    }
}
