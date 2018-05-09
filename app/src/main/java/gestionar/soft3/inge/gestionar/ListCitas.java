package gestionar.soft3.inge.gestionar;

import android.app.DatePickerDialog;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.DatePicker;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import gestionar.soft3.inge.gestionar.ApiRest.ApiRest;
import gestionar.soft3.inge.gestionar.Utilidades.CircleTransform;
import gestionar.soft3.inge.gestionar.Utilidades.URLS;
import gestionar.soft3.inge.gestionar.fragments.DatePickerFragment;
import gestionar.soft3.inge.gestionar.pojo.CitaMedica;
import gestionar.soft3.inge.gestionar.pojo.Ingreso;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ListCitas extends AppCompatActivity {

    ArrayList<CitaMedica> ingresos;
    ArrayList<CitaMedica> temp;

    AdapterIngreso adapterAsistentes;
    ListView listView;
    ApiRest apiRest;
    Retrofit retrofit;
    SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_citas);
        listView =  findViewById(R.id.listview_ingresos);
        searchView = findViewById(R.id.sv);
        retrofit_clase();
        obtenerIngresos();


        searchView.setOnClickListener(new View.OnClickListener() {
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
                        searchView.setQuery(selectedDate,false);
                        searchView.clearFocus();
                    }
                });
                newFragment.show(getFragmentManager(), "datePicker");



            }
        });




    }



    private void obtenerIngresos(){

        Call<List<CitaMedica>> call = apiRest.obtenerCitas();


        call.enqueue(new Callback<List<CitaMedica>>() {
            @Override
            public void onResponse(Call<List<CitaMedica>> call, Response<List<CitaMedica>> response) {
                if (response.code() ==200 )
                {
                    if (ingresos== null)
                    {
                        ingresos = new ArrayList<>();
                    }


                    ingresos = (ArrayList)response.body();
                    adapterAsistentes = new AdapterIngreso(getApplicationContext(),ingresos);
                    listView.setAdapter(adapterAsistentes);
                }
                else if (response.code() == 404)
                {
                    Toast.makeText(getApplicationContext(),"No existen ingresos",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<CitaMedica>> call, Throwable t) {
                Toast.makeText(getApplicationContext(),"Error al conectar al servirdor + " + t.getCause() ,Toast.LENGTH_SHORT).show();
                Log.d("Prueba",t.getCause()+"");
            }
        });




        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapterAsistentes.getFilter().filter(newText);

                return false;
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


    class AdapterIngreso extends BaseAdapter implements Filterable{

        private LayoutInflater layoutInflater;
        CustomFilter filtro;
        Context context;
        ArrayList<CitaMedica> ingresos;
        ArrayList<CitaMedica> filtroList;


        public AdapterIngreso(Context context, ArrayList<CitaMedica>ingresos)
        {
            this.context = context;
            this.ingresos =ingresos;
            this.filtroList = ingresos;
            layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            return ingresos.size();
        }

        @Override
        public Object getItem(int position) {
            return ingresos.get(position);
        }

        @Override
        public long getItemId(int position) {
            return ingresos.indexOf(getItem(position));
        }


        @Override
        public View getView(int position, View convertView, ViewGroup parent)
        {
            ViewGroup viewGroup = (ViewGroup) layoutInflater.inflate(R.layout.adapter_list_citas, null);

            TextView cedula = viewGroup.findViewById(R.id.tv_cedula);
            TextView motivo =  viewGroup.findViewById(R.id.tv_motivo);
            TextView valor =  viewGroup.findViewById(R.id.tv_valor);
            TextView fecha =  viewGroup.findViewById(R.id.tv_fecha);
            ImageView imageView = viewGroup.findViewById(R.id.image_perfil);

            cedula.setText(cedula.getText().toString() + ingresos.get(position).getCedula());
            motivo.setText(motivo.getText().toString() + ingresos.get(position).getTipo_cita());
            valor.setText(valor.getText().toString() + ingresos.get(position).getValor());
            fecha.setText(fecha.getText().toString() + ingresos.get(position).getFecha_cita());

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
            protected FilterResults performFiltering(CharSequence constraint)
            {
                FilterResults resulst = new FilterResults();
                if(constraint != null && constraint.length()>0)
                {
                    constraint = constraint.toString();
                    ArrayList<CitaMedica> filtro = new ArrayList<>();

                    for(Integer i=0;i<filtroList.size();i++)
                    {
                        String nombre=filtroList.get(i).getFecha_cita();
                        Log.d("Prueba",nombre);
                        if(nombre.toLowerCase().contains((""+constraint).toLowerCase()))
                        {
                            CitaMedica d = new CitaMedica(filtroList.get(i).getCedula(),filtroList.get(i).getTipo_cita()
                                    ,filtroList.get(i).getValor(),filtroList.get(i).getFecha_cita(),filtroList.get(i).getNombre(),
                                    filtroList.get(i).getCedula_dos()
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

                temp=(ArrayList<CitaMedica>) resulst.values;
                return resulst;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                ingresos = (ArrayList<CitaMedica>) results.values;
                notifyDataSetChanged();

            }
        }
    }
}
