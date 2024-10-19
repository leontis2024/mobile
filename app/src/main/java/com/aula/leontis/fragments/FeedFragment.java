package com.aula.leontis.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;

import com.aula.leontis.R;
import com.aula.leontis.fragments.feed.ForYou;
import com.aula.leontis.fragments.feed.MuseusSeguidos;

public class FeedFragment extends Fragment {
    ImageButton btnBuscar, btnFiltrar, btnForYou, btnScanner,btnMuseusSeguidos, btnFecharPesquisa;
    ForYou foryou = new ForYou();
    MuseusSeguidos museusSeguidos = new MuseusSeguidos();
    EditText campoPesquisa;


    public FeedFragment() {

    }

    public static FeedFragment newInstance(String param1, String param2) {
        FeedFragment fragment = new FeedFragment();
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
        View view = inflater.inflate(R.layout.fragment_feed, container, false);
        btnBuscar = view.findViewById(R.id.btnBuscar);
        btnFiltrar = view.findViewById(R.id.btnVoltar);
        btnForYou = view.findViewById(R.id.btnForYou);
        btnScanner = view.findViewById(R.id.btnScanner);
        btnMuseusSeguidos = view.findViewById(R.id.btnMuseusSeguidos);
        btnFecharPesquisa = view.findViewById(R.id.btnFecharPesquisa);

        campoPesquisa = view.findViewById(R.id.campoPesquisa);

        FragmentTransaction fragmentTransaction = getChildFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.container_feed, foryou).commit();
        setButtonState("foryou", true);
        setButtonState("museus", false);

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
            }
        });

        btnMuseusSeguidos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FragmentTransaction fragmentTransaction = getChildFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.container_feed, museusSeguidos).commit();
                setButtonState("museus", true);
                 setButtonState("foryou", false);
            }
        });

        btnForYou.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FragmentTransaction fragmentTransaction = getChildFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.container_feed, foryou).commit();
                setButtonState("foryou", true);
                setButtonState("museus", false);
            }
        });

        return view;
    }
    public void setButtonState(String button, boolean isActive) {
        if (isActive) {
            switch (button) {
                case "foryou":
                    btnForYou.setImageResource(R.drawable.for_you_selecionada); // Define a imagem ativa para btn1
                    break;
                case "museus":
                    btnMuseusSeguidos.setImageResource(R.drawable.museus_seguidos_selecionado); // Define a imagem ativa para btn2
                    break;
            }
        } else {
            switch (button) {
                case "museus":
                    btnMuseusSeguidos.setImageResource(R.drawable.museus_seguidos); // Define a imagem inativa para btn1
                    break;
                case "foryou":
                    btnForYou.setImageResource(R.drawable.for_you); // Define a imagem inativa para btn2
                    break;
            }
        }
    }
}