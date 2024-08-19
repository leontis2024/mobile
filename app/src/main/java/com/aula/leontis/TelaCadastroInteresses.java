package com.aula.leontis;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

public class TelaCadastroInteresses extends AppCompatActivity {
    RecyclerView rvGeneros;
    List<Genero> listaGeneros = new ArrayList<>();
    AdapterGenero adapterGenero = new AdapterGenero(listaGeneros);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tela_cadastro_interesses);
        rvGeneros = findViewById(R.id.rv_generos);
        rvGeneros.setAdapter(adapterGenero);

        listaGeneros.add(new Genero(1,"Ação", true,"Ação e aventura."));
        listaGeneros.add(new Genero(2,"Aventura", false,"Aventuraaaaa."));
        listaGeneros.add(new Genero(3,"Comedia", false,"Comediaaaaa."));
        listaGeneros.add(new Genero(4,"Drama", false,"Dramaaaaa."));
        listaGeneros.add(new Genero(5,"Ficcao Cientifica", false,"Ficcao Cientificaaaaaa."));
        listaGeneros.add(new Genero(6,"Romance", false,"Romanceeeeee."));

        rvGeneros.setLayoutManager(new androidx.recyclerview.widget.LinearLayoutManager(this));


        //pegando informações de cadastro das telas anteriores
        Intent infoCadastro = getIntent();
        String nome = infoCadastro.getStringExtra("nome");
        String sobrenome = infoCadastro.getStringExtra("sobrenome");
        String email = infoCadastro.getStringExtra("email");
        String telefone = infoCadastro.getStringExtra("telefone");
        String dtNasc = infoCadastro.getStringExtra("dtNasc");
        String senha = infoCadastro.getStringExtra("senha");
        String apelido = infoCadastro.getStringExtra("apelido");
        String biografia = infoCadastro.getStringExtra("biografia");
        String sexo = infoCadastro.getStringExtra("sexo");




    }
}