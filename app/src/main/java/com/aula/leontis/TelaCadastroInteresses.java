package com.aula.leontis;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

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
//        infos.putParcelableArrayList("listaGenerosInteressados", Genero.class);


        btnContinuar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Pegando os generos de interesse
                List<Genero> listaGenerosInteressados = new ArrayList<>();
                for(Genero genero : listaGeneros){
                    if(genero.getCheckInteresse()){
                        listaGenerosInteressados.add(genero);
                    }
                }
                if(listaGenerosInteressados.size() == 0){
                    erroGenero.setText("Selecione pelo menos um gênero de interesse");
                    erroGenero.setVisibility(View.VISIBLE);
                }else{
                    erroGenero.setVisibility(View.INVISIBLE);
                    Intent telaFotoPerfil = new Intent(TelaCadastroInteresses.this, TelaCadastroFotoPerfil.class);
                    telaFotoPerfil.putExtras(infos);
                    startActivity(telaFotoPerfil);
                }

            }
        });

    }
}