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
import com.aula.leontis.interfaces.GeneroInterface;
import com.aula.leontis.model.Genero;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TelaCadastroInteresses extends AppCompatActivity {
    RecyclerView rvGeneros;
    MetodosAux aux = new MetodosAux();
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

        // Configurar Retrofit
        GeneroInterface generoInterface = RetrofitClient.getClient("https://dev2-tfqz.onrender.com").create(GeneroInterface.class);

        // Buscar todos os gêneros
        generoInterface.buscarTodosGenerosParciais().enqueue(new Callback<List<Genero>>() {
            @Override
            public void onResponse(Call<List<Genero>> call, Response<List<Genero>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    erroGenero.setVisibility(View.INVISIBLE);
                    listaGeneros = response.body();

                    // Configurar o Adapter da RecyclerView
                    adapterGenero = new AdapterGenero(listaGeneros);
                    rvGeneros.setAdapter(adapterGenero);
                } else {
                    erroGenero.setText("Falha ao obter dados dos gêneros");
                    erroGenero.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<List<Genero>> call, Throwable t) {
                aux.abrirDialogErro(TelaCadastroInteresses.this,"Erro inesperado","Erro ao obter dados dos gêneros\nMensagem: "+t.getMessage());
            }
        });




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