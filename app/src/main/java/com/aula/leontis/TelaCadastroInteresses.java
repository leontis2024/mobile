package com.aula.leontis;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import com.aula.leontis.adapter.AdapterGenero;
import com.aula.leontis.model.Genero;

import java.util.ArrayList;
import java.util.List;

public class TelaCadastroInteresses extends AppCompatActivity {
    RecyclerView rvGeneros;
    List<Genero> listaGeneros = new ArrayList<>();
    AdapterGenero adapterGenero = new AdapterGenero(listaGeneros);
    Button btnContinuar;
    TextView erroGenero;
    String nome, sobrenome, email, telefone, dtNasc, senha, apelido, biografia, sexo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tela_cadastro_interesses);
        rvGeneros = findViewById(R.id.rv_generos);
        rvGeneros.setAdapter(adapterGenero);
        btnContinuar = findViewById(R.id.btn_continuar_interesses);
        erroGenero = findViewById(R.id.erro_interesse);


        listaGeneros.add(new Genero(1,"Neorrealismo", false,"O neorrealismo foi movimento artístico que surgiu no início de século XX que teve influências de movimento políticos como o socialismo, o comunismo e o marxismo."));
        listaGeneros.add(new Genero(2,"Simbolismo", false,"A poesia simbolista apresenta teor metafísico, musicalidade, alienação social, rigor formal e caráter sinestésico.."));
        listaGeneros.add(new Genero(3,"Natureza-morta", false,"Esse tipo de pintura surgiu no século XVI e o objetivo era representar objetos inanimados como flores, frutas, jarros de metal, taças de cristal, vidros, porcelanas, instrumentos musicais, livros e muitas outras coisas."));
        listaGeneros.add(new Genero(4,"Paisagem", false,"A arte paisagem se caracteriza pela representação de cenários naturais, como montanhas e rios, com foco na composição espacial, uso da luz e cor, e detalhamento dos elementos. Pode evocar emoções específicas e mostrar a interação entre humanos e a natureza, variando de representações realistas a estilizadas.."));
        listaGeneros.add(new Genero(5,"Impressionista", false,"A proposta central do movimento impressionista consistia em representar, por meio das artes visuais, sobretudo na pintura, os efeitos luminosos no ambiente."));
        listaGeneros.add(new Genero(6,"Abstrata", false,"A arte abstrata, como seu próprio nome indica, trata-se de um estilo artístico que foca nas abstrações, e não na realidade. Em outras palavras: a arte abstrata não tenta reproduzir o mundo a partir de imagens conhecidas ou de formas definidas da realidade.."));

        rvGeneros.setLayoutManager(new androidx.recyclerview.widget.LinearLayoutManager(this));


        //pegando informações de cadastro das telas anteriores
        Intent info = getIntent();
        Bundle infoCadastro = info.getExtras();
        if(infoCadastro != null) {
            nome = infoCadastro.getString("nome");
            sobrenome = infoCadastro.getString("sobrenome");
            email = infoCadastro.getString("email");
            telefone = infoCadastro.getString("telefone");
            dtNasc = infoCadastro.getString("dtNasc");
            senha = infoCadastro.getString("senha");
            apelido = infoCadastro.getString("apelido");
            biografia = infoCadastro.getString("biografia");
            sexo = infoCadastro.getString("sexo");
        }




        btnContinuar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Primeiro, conte quantos itens terão no array
                int count = 0;
                for (Genero genero : listaGeneros) {
                    if (genero.getCheckInteresse()) {
                        count++;
                    }
                }

                // Inicialize o array com o tamanho correto
                long[] listaGenerosInteressados = new long[count];

                // Preencha o array com os IDs
                int index = 0;
                for (Genero genero : listaGeneros) {
                    if (genero.getCheckInteresse()) {
                        listaGenerosInteressados[index++] = genero.getId();
                    }
                }

                // Verifique se o array está vazio
                if (listaGenerosInteressados.length == 0) {
                    erroGenero.setText("Selecione pelo menos um gênero de interesse");
                    erroGenero.setVisibility(View.VISIBLE);
                }else{
                    erroGenero.setVisibility(View.INVISIBLE);
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
                    infos.putLongArray("generosInteressados",  listaGenerosInteressados);
                    Intent telaFotoPerfil = new Intent(TelaCadastroInteresses.this, TelaCadastroFotoPerfil.class);
                    telaFotoPerfil.putExtras(infos);
                    startActivity(telaFotoPerfil);
                }

            }
        });

    }
}