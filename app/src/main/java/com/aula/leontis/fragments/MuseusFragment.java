package com.aula.leontis.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
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

import com.aula.leontis.R;
import com.aula.leontis.adapters.AdapterMuseu;
import com.aula.leontis.models.museu.Museu;
import com.aula.leontis.services.MuseuService;

import java.util.ArrayList;
import java.util.List;

public class MuseusFragment extends Fragment {
    MuseuService museuService = new MuseuService();
    TextView erroMuseu;
    ImageButton btnFecharPesquisa,btnBuscar;
    EditText campoPesquisa;
    RecyclerView rvMuseus;
    ProgressBar progressBar;
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
                rvMuseus.setLayoutManager(new LinearLayoutManager(getContext()));
                museuService.buscarMuseus(erroMuseu,getContext(), rvMuseus,listaMuseus, adapterMuseu);

            }
        });

        rvMuseus = view.findViewById(R.id.rvMuseus);
        rvMuseus.setAdapter(adapterMuseu);
        rvMuseus.setLayoutManager(new LinearLayoutManager(getContext()));

        museuService.buscarMuseus(erroMuseu,getContext(), rvMuseus,listaMuseus, adapterMuseu);

        return view;
    }
    public void filtrar(String nome){
        progressBar.setVisibility(View.VISIBLE);
        museuService.buscarMuseuPorNomePesquisa(nome,getContext(),erroMuseu,rvMuseus,adapterMuseu,listaMuseus,progressBar);
    }
}