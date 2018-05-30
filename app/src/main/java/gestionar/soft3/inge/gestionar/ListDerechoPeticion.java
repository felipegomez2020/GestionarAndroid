package gestionar.soft3.inge.gestionar;
import android.content.Context;
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
import gestionar.soft3.inge.gestionar.ApiRest.ApiRest;
import gestionar.soft3.inge.gestionar.Utilidades.CircleTransform;
import gestionar.soft3.inge.gestionar.Utilidades.URLS;
import gestionar.soft3.inge.gestionar.pojo.DerechoPeticion;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ListDerechoPeticion extends AppCompatActivity {

    ArrayList<DerechoPeticion> misAfiliados;
    ArrayList<DerechoPeticion> temp;

    ListView listView;
    SearchView searchView;
    ApiRest apiRest;
    Retrofit retrofit;
    AdapterAsistentes afiliadosAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_derecho_peticion);
        listView = findViewById(R.id.listview_afiliados);
        searchView = findViewById(R.id.sv);

        cargarDerechoPeticion();



    }

    private void cargarDerechoPeticion()
    {
        retrofit_clase();
        Call c =  apiRest.obtenerDerechos();
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

                    misAfiliados = (ArrayList<DerechoPeticion>) response.body();
                    afiliadosAdapter= new AdapterAsistentes(getApplicationContext(),misAfiliados);
                    listView.setAdapter(afiliadosAdapter);
                } if (response.code()==404)
            {
                Toast.makeText(getApplicationContext(),"No hay Afiliados para mostrar",Toast.LENGTH_LONG).show();
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
        ArrayList<DerechoPeticion>afiliados;
        ArrayList<DerechoPeticion> filtroList;


        public AdapterAsistentes(Context context,ArrayList<DerechoPeticion>afiliados) {
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
            ViewGroup viewGroup = (ViewGroup) layoutInflater.inflate(R.layout.adapter_list_derechopeticion, null);

            TextView motivo = viewGroup.findViewById(R.id.tv_motivo);
            TextView cedula =  viewGroup.findViewById(R.id.tv_cedula);
            TextView valor =  viewGroup.findViewById(R.id.tv_valor);
            ImageView imageView = viewGroup.findViewById(R.id.image_perfil);


            cedula.setText(afiliados.get(position).getAfiliado());
            motivo.setText(afiliados.get(position).getDescripcion());
            valor.setText(afiliados.get(position).getCosto());




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
                    ArrayList<DerechoPeticion> filtro = new ArrayList<>();

                    for(Integer i=0;i<filtroList.size();i++)
                    {
                        String nombre=filtroList.get(i).getAfiliado();
                        if(nombre.toLowerCase().contains((""+constraint).toLowerCase()))
                        {
                            DerechoPeticion d= new DerechoPeticion();
                            d.setDescripcion(filtroList.get(i).getDescripcion());
                            d.setAfiliado(filtroList.get(i).getAfiliado());
                            d.setCosto(filtroList.get(i).getCosto());

                            filtro.add(d);
                        }
                    }
                    resulst.count= filtro.size();
                    resulst.values = filtro;
                }else{
                    resulst.count= filtroList.size();
                    resulst.values = filtroList;
                }

                temp=(ArrayList<DerechoPeticion>) resulst.values;
                return resulst;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                afiliados = (ArrayList<DerechoPeticion>) results.values;
                notifyDataSetChanged();

            }
        }
    }





}
