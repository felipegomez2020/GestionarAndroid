package gestionar.soft3.inge.gestionar.fragments;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.BaseExpandableListAdapter;
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
import java.util.Map;

import gestionar.soft3.inge.gestionar.ApiRest.ApiRest;
import gestionar.soft3.inge.gestionar.ListCitas;
import gestionar.soft3.inge.gestionar.ListDerechoPeticion;
import gestionar.soft3.inge.gestionar.ListIngresos;
import gestionar.soft3.inge.gestionar.ListMora;
import gestionar.soft3.inge.gestionar.ListaAfiliados;
import gestionar.soft3.inge.gestionar.R;
import gestionar.soft3.inge.gestionar.Utilidades.CircleTransform;
import gestionar.soft3.inge.gestionar.Utilidades.URLS;
import gestionar.soft3.inge.gestionar.pojo.Afiliado;
import gestionar.soft3.inge.gestionar.pojo.Ingreso;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ControlFragment extends Fragment implements View.OnClickListener {

    ArrayList<Ingreso> misAfiliados;
    ListView listView;
    ApiRest apiRest;
    Retrofit retrofit;
    CardView cardView_ingresos,cardView_citas,cardView_mora,cardView_derecho;


    public ControlFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_control, container, false);

        inicializarComponentes(view);




        return view;
    }

    private void inicializarComponentes(View view)
    {
        cardView_ingresos =view.findViewById(R.id.card_ingresos);
        cardView_citas =view.findViewById(R.id.card_citas);
        cardView_mora =view.findViewById(R.id.card_mora);
        cardView_derecho =view.findViewById(R.id.card_derecho);

        cardView_ingresos.setOnClickListener(this);
        cardView_citas.setOnClickListener(this);
        cardView_mora.setOnClickListener(this);
        cardView_derecho.setOnClickListener(this);



    }
    @Override
    public void onClick(View v) {

        Intent intent;
        switch (v.getId())
        {
            case R.id.card_ingresos:
                intent = new Intent(getActivity(), ListIngresos.class);
                startActivity(intent);
                break;

            case R.id.card_citas:
                intent = new Intent(getActivity(), ListCitas.class);
                startActivity(intent);
                break;


            case R.id.card_mora:
                intent = new Intent(getActivity(), ListMora.class);
                startActivity(intent);
                break;


            case R.id.card_derecho:
                intent = new Intent(getActivity(), ListDerechoPeticion.class);
                startActivity(intent);
                break;

        }
    }




















    private void obtenerIngresos(){
        Call <List<Ingreso>> ingresos = apiRest.obtenerIngresos();
        ingresos.enqueue(new Callback<List<Ingreso>>() {
            @Override
            public void onResponse(Call<List<Ingreso>> call, Response<List<Ingreso>> response) {
                if (response.code() ==200 )
                {
                    ArrayList<Ingreso>ingresos = (ArrayList)response.body();
                    AdapterAsistentes adapterAsistentes = new AdapterAsistentes(getActivity(),ingresos);
                    listView.setAdapter(adapterAsistentes);
                }
                else if (response.code() == 404)
                {
                    Toast.makeText(getActivity(),"No existen ingresos",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Ingreso>> call, Throwable t) {
                Toast.makeText(getActivity(),"Error al conectar al servirdor",Toast.LENGTH_SHORT).show();
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
    class AdapterAsistentes extends BaseAdapter {

        private LayoutInflater layoutInflater;
        Context context;
        ArrayList<Ingreso>afiliados;


        public AdapterAsistentes(Context context,ArrayList<Ingreso>afiliados) {
            this.context = context;
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
            ViewGroup viewGroup = (ViewGroup) layoutInflater.inflate(R.layout.adapter_list_ingresos, null);

            TextView cedula = viewGroup.findViewById(R.id.tv_cedula);
            TextView motivo =  viewGroup.findViewById(R.id.tv_motivo);
            TextView valor =  viewGroup.findViewById(R.id.tv_valor);
            TextView fecha =  viewGroup.findViewById(R.id.tv_fecha);
            ImageView imageView = viewGroup.findViewById(R.id.image_perfil);


            cedula.setText(cedula.getText().toString() + afiliados.get(position).getCedula_afiliado());
            motivo.setText(motivo.getText().toString() + afiliados.get(position).getMotivo());
            valor.setText(valor.getText().toString() + afiliados.get(position).getValor());
            fecha.setText(fecha.getText().toString() + afiliados.get(position).getFecha());


            Picasso.with(getActivity()).load(R.drawable.profile).transform(new CircleTransform()).into(imageView);

            return viewGroup;
        }


    }



}
