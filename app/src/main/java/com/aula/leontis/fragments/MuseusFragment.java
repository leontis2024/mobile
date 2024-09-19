package com.aula.leontis.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.aula.leontis.R;
import com.aula.leontis.adapters.AdapterGeneroCompleto;
import com.aula.leontis.adapters.AdapterMuseu;
import com.aula.leontis.models.genero.GeneroCompleto;
import com.aula.leontis.models.museu.Museu;
import com.aula.leontis.services.MuseuService;

import java.util.ArrayList;
import java.util.List;

public class MuseusFragment extends Fragment {
    MuseuService museuService = new MuseuService();
    TextView erroMuseu;
    RecyclerView rvMuseus;
    List<Museu> listaMuseus = new ArrayList<>();
    AdapterMuseu adapterMuseu = new AdapterMuseu(listaMuseus);

    public MuseusFragment() {
        // Required empty public constructor
    }


    public static MuseusFragment newInstance(String param1, String param2) {
        MuseusFragment fragment = new MuseusFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_museus, container, false);
        erroMuseu = view.findViewById(R.id.erroMuseu);
        rvMuseus = view.findViewById(R.id.rvMuseus);
        rvMuseus.setAdapter(adapterMuseu);
        rvMuseus.setLayoutManager(new LinearLayoutManager(getContext()));

        //listaMuseus.add(new Museu("1","Teste","https://s2-oglobo.glbimg.com/UiPoK-OLSflpcG8JxeysJWTT4Dc=/0x0:4000x2250/924x0/smart/filters:strip_icc()/i.s3.glbimg.com/v1/AUTH_da025474c0c44edd99332dddb09cabe8/internal_photos/bs/2022/p/p/AKvOfIQqygvStPWHRsww/100335462-sc-sao-paulo-sp-02-09-2022-museu-do-ipiranga-espaco-passou-por-obras-de-restauracao-que.jpg"));

        museuService.buscarMuseus(erroMuseu,getContext(), rvMuseus,listaMuseus, adapterMuseu);

        return view;
    }
}