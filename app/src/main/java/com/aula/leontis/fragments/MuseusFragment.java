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

        museuService.buscarMuseus(erroMuseu,getContext(), rvMuseus,listaMuseus, adapterMuseu);

        return view;
    }
}