package gestionar.soft3.inge.gestionar;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
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
import gestionar.soft3.inge.gestionar.pojo.Afiliado;
import gestionar.soft3.inge.gestionar.pojo.Beneficiario;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class VerBeneficiarios extends AppCompatActivity {

    ArrayList<Beneficiario> misAfiliados;
    ArrayList<Beneficiario> temp;
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


        try {
            retrofit_clase();
            Afiliado objeto = (Afiliado) getIntent().getExtras().getSerializable("afiliado");

            try {
                cargarAfiliados(objeto.getCedula());
            }catch (NullPointerException e)
            {
                e.printStackTrace();
            }
        }catch (NullPointerException e)
        {
            e.printStackTrace();
        }


    }

    private void cargarAfiliados(String cedula)
    {
        retrofit_clase();

        Call<List<Beneficiario>>c = apiRest.obtenerBeneficiarios(cedula);
        c.enqueue(new Callback<List<Beneficiario>>() {
            @Override
            public void onResponse(Call<List<Beneficiario>> call, Response<List<Beneficiario>> response) {
                if (response.code()==200)
                {
                    if (misAfiliados== null)
                    {
                        misAfiliados = new ArrayList<>();
                    }

                    misAfiliados = (ArrayList<Beneficiario>) response.body();
                    afiliadosAdapter= new AdapterAsistentes(getApplicationContext(),misAfiliados);
                    listView.setAdapter(afiliadosAdapter);
                }  if (response.code()==404)
                {
                    Toast.makeText(getApplicationContext(),"No hay beneficiarios para mostrar",Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<List<Beneficiario>> call, Throwable t) {

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
        ArrayList<Beneficiario>afiliados;
        ArrayList<Beneficiario> filtroList;


        public AdapterAsistentes(Context context,ArrayList<Beneficiario>afiliados) {
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
                    ArrayList<Beneficiario> filtro = new ArrayList<>();

                    for(Integer i=0;i<filtroList.size();i++)
                    {
                        String nombre=filtroList.get(i).getCedula();
                        if(nombre.toLowerCase().contains((""+constraint).toLowerCase()))
                        {
                            Beneficiario d= new Beneficiario();
                            d.setCedula(filtroList.get(i).getCedula());
                            d.setNombres(filtroList.get(i).getNombres());
                            d.setApellidos(filtroList.get(i).getApellidos());
                                    filtroList.get(i).getNombres();
                                    filtroList.get(i).getApellidos();



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

                temp=(ArrayList<Beneficiario>) resulst.values;
                return resulst;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                afiliados = (ArrayList<Beneficiario>) results.values;
                notifyDataSetChanged();

            }
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
}
