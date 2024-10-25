package com.aula.leontis.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
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
    ImageButton btnFecharPesquisa,btnBuscar;
    EditText campoPesquisa;
    ProgressBar progressBar;
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
        btnFecharPesquisa = view.findViewById(R.id.btnFecharPesquisa);
        btnBuscar = view.findViewById(R.id.btnBuscar);
        campoPesquisa = view.findViewById(R.id.campoPesquisa);
        progressBar = view.findViewById(R.id.progressBar8);

        campoPesquisa.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (s.length() > 0) {
                    filtrar(s.toString());
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        btnBuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                campoPesquisa.setVisibility(View.VISIBLE);
                btnBuscar.setVisibility(View.INVISIBLE);
                btnFecharPesquisa.setVisibility(View.VISIBLE);
                campoPesquisa.setText("");
            }
        });
        btnFecharPesquisa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                campoPesquisa.setVisibility(View.INVISIBLE);
                btnBuscar.setVisibility(View.VISIBLE);
                btnFecharPesquisa.setVisibility(View.INVISIBLE);
                rvGeneros.setLayoutManager(new LinearLayoutManager(getContext()));
                generoService.buscarGenerosCompleto(erroGenero,getContext(), rvGeneros,listaGeneros, adapterGeneroCompleto);

            }
        });


        rvGeneros = view.findViewById(R.id.rvGeneros);
        rvGeneros.setAdapter(adapterGeneroCompleto);
        rvGeneros.setLayoutManager(new LinearLayoutManager(getContext()));

        generoService.buscarGenerosCompleto(erroGenero,getContext(), rvGeneros,listaGeneros, adapterGeneroCompleto);

        return view;
    }
    public void filtrar(String nome){
        progressBar.setVisibility(View.VISIBLE);
        generoService.buscarGeneroPorNomePesquisa(nome,getContext(),erroGenero,rvGeneros,adapterGeneroCompleto,listaGeneros,progressBar);
    }
}