package com.aula.leontis.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.aula.leontis.services.GeneroService;
import com.aula.leontis.utilities.MetodosAux;
import com.aula.leontis.R;
import com.aula.leontis.adapters.AdapterGeneroCompleto;
import com.aula.leontis.models.genero.GeneroCompleto;

import java.util.ArrayList;
import java.util.List;

public class GenerosFragment extends Fragment {
    GeneroService generoService = new GeneroService();
    MetodosAux aux = new MetodosAux();
    RecyclerView rvGeneros;
    TextView erroGenero;
    List<GeneroCompleto> listaGeneros = new ArrayList<>();
    AdapterGeneroCompleto adapterGeneroCompleto = new AdapterGeneroCompleto(listaGeneros);

    public GenerosFragment() {
        // Required empty public constructor
    }

    public static GenerosFragment newInstance(String param1, String param2) {
        GenerosFragment fragment = new GenerosFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_generos, container, false);
        erroGenero = view.findViewById(R.id.erroUsuario);
        rvGeneros = view.findViewById(R.id.rvGeneros);
        rvGeneros.setAdapter(adapterGeneroCompleto);
        rvGeneros.setLayoutManager(new LinearLayoutManager(getContext()));

        generoService.buscarGenerosCompleto(erroGenero,getContext(), rvGeneros,listaGeneros, adapterGeneroCompleto);

        return view;
    }
}