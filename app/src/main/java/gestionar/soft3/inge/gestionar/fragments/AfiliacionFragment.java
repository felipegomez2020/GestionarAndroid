package gestionar.soft3.inge.gestionar.fragments;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import gestionar.soft3.inge.gestionar.ListaAfiliados;
import gestionar.soft3.inge.gestionar.RegistrarAfiliacion;
import gestionar.soft3.inge.gestionar.R;

public class AfiliacionFragment extends Fragment implements View.OnClickListener {


    View view;

    CardView cardView_registro;
    CardView cardView_lista;

    public AfiliacionFragment()
    {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        view= inflater.inflate(R.layout.fragment_afiliacion, container, false);

        cardView_registro = view.findViewById(R.id.card_registro);
        cardView_lista = view.findViewById(R.id.card_lista);

        cardView_registro.setOnClickListener(this);
        cardView_lista.setOnClickListener(this);






        return view;
    }


    @Override
    public void onClick(View v)
    {
        Intent intent;
        switch (v.getId())
        {
            case R.id.card_registro:
                intent = new Intent(getActivity(), RegistrarAfiliacion.class);
                startActivity(intent);
                break;
            case R.id.card_lista:
                intent = new Intent(getActivity(), ListaAfiliados.class);
                startActivity(intent);
                break;

        }

    }
}
