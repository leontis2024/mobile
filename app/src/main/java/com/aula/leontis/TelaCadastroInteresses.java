package com.aula.leontis;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.widget.Toast;

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

        listaGeneros.add(new Genero(1,"Neorrealismo", false,"O neorrealismo foi movimento artístico que surgiu no início de século XX que teve influências de movimento políticos como o socialismo, o comunismo e o marxismo."));
        listaGeneros.add(new Genero(2,"Simbolismo", false,"A poesia simbolista apresenta teor metafísico, musicalidade, alienação social, rigor formal e caráter sinestésico.."));
        listaGeneros.add(new Genero(3,"Comedia", false,"Comediaaaaa."));
        listaGeneros.add(new Genero(4,"Drama", true,"Dramaaaaa."));
        listaGeneros.add(new Genero(5,"Ficcao Cientifica", false,"Ficcao Cientificaaaaaa."));
        listaGeneros.add(new Genero(6,"Romance", true,"Romanceeeeee."));

        rvGeneros.setLayoutManager(new androidx.recyclerview.widget.LinearLayoutManager(this));

        List<Genero> listaGenerosInteressados = new ArrayList<>();
        for(Genero genero : listaGeneros){
            if(genero.getCheckInteresse()){
                listaGenerosInteressados.add(genero);
            }
        }

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
//        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
//            List<Genero> generos = infoCadastro.getParcelableArrayListExtra("listaGenerosInteressados", Genero.class);
//        }
        //criando bundle para passar todas as informações para a proxima tela
        Bundle infos = new Bundle();
        infos.putString("nome", nome);
        infos.putString("sobrenome", sobrenome);
        infos.putString("email", email);
        infos.putString("telefone", telefone);
        infos.putString("dtNasc", dtNasc);
        infos.putString("senha", senha);
        infos.putString("apelido", apelido);
        infos.putString("biografia", biografia);
        infos.putString("sexo", sexo);
        infos.putParcelableArrayList("listaGenerosInteressados", (ArrayList<? extends Parcelable>) listaGenerosInteressados);

        Intent telaFotoPerfil = new Intent(TelaCadastroInteresses.this, TelaCadastroFotoPerfil.class);
        telaFotoPerfil.putExtras(infos);
        startActivity(telaFotoPerfil);

    }
}