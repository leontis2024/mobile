package com.aula.leontis.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.aula.leontis.utilities.MetodosAux;
import com.aula.leontis.R;
import com.aula.leontis.adapters.AdapterGeneroCompleto;
import com.aula.leontis.interfaces.genero.GeneroInterface;
import com.aula.leontis.models.genero.GeneroCompleto;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class GenerosFragment extends Fragment {
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
        erroGenero = view.findViewById(R.id.erroGenero);
        rvGeneros = view.findViewById(R.id.rvGeneros);
        rvGeneros.setAdapter(adapterGeneroCompleto);
        rvGeneros.setLayoutManager(new LinearLayoutManager(getContext()));

       // listaGeneros.add(new GeneroCompleto("1","Simbolismo","","O neorrealismo foi uma corrente artística de meados do século XX, com um carácter ideológico marcadamente de esquerda marxista, que teve ramificações em várias formas de arte (literatura, pintura, música), mas atingiu o seu expoente máximo no Cinema neorrealista, sobretudo no realismo poético francês e no neorrealismo italiano. Com o mesmo nome, mas com distinção, pode ser observada uma Teoria das relações internacionais","https://s5.static.brasilescola.uol.com.br/be/2023/04/o-lavrador-de-cafe-1934-de-candido-portinari-uma-importante-obra-do-neorrealismo-brasileiro.jpg"));
        buscarGeneros();
        return view;
    }
    public void buscarGeneros(){
        // Configurar Retrofit
        String urlAPI = "https://dev2-tfqz.onrender.com/";

        // Configurar acesso à API
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(urlAPI)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        GeneroInterface generoInterface = retrofit.create(GeneroInterface.class);

        Call<List<GeneroCompleto>> call = generoInterface.buscarTodosGeneros();

        // Buscar todos os gêneros
        call.enqueue(new Callback<List<GeneroCompleto>>() {
            @Override
            public void onResponse(Call<List<GeneroCompleto>> call, Response<List<GeneroCompleto>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    erroGenero.setVisibility(View.INVISIBLE);
                    erroGenero.setTextColor(getResources().getColor(R.color.vermelho_erro));
                    listaGeneros = response.body();

                    // Configurar o Adapter da RecyclerView
                    adapterGeneroCompleto = new AdapterGeneroCompleto(listaGeneros);
                    rvGeneros.setAdapter(adapterGeneroCompleto);
                } else {
                    erroGenero.setText("Falha ao obter dados dos gêneros");
                    erroGenero.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<List<GeneroCompleto>> call, Throwable t) {
                Log.e("API_ERROR_GET", "Erro ao fazer a requisição: " + t.getMessage());
                aux.abrirDialogErro(getContext(),"Erro inesperado","Erro ao obter dados dos gêneros\nMensagem: "+t.getMessage());
            }
        });
    }
}