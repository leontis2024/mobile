package com.aula.leontis.activitys;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


import com.aula.leontis.services.GeneroService;
import com.aula.leontis.utilities.MetodosAux;
import com.aula.leontis.R;
import com.aula.leontis.adapters.AdapterGenero;
import com.aula.leontis.interfaces.genero.GeneroInterface;
import com.aula.leontis.models.genero.Genero;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class TelaCadastroInteresses extends AppCompatActivity {
    GeneroService generoService = new GeneroService();
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

        generoService.buscarGeneros(erroGenero, this, rvGeneros, listaGeneros, adapterGenero);

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
    public void buscarGeneros(){
        // Configurar Retrofit
        String urlAPI = "https://dev2-tfqz.onrender.com/";

        // Configurar acesso à API
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(urlAPI)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        GeneroInterface generoInterface = retrofit.create(GeneroInterface.class);

        Call<List<Genero>> call = generoInterface.buscarTodosGenerosParciais();

        // Buscar todos os gêneros
        call.enqueue(new Callback<List<Genero>>() {
            @Override
            public void onResponse(Call<List<Genero>> call, Response<List<Genero>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    erroGenero.setVisibility(View.INVISIBLE);
                    erroGenero.setTextColor(getResources().getColor(R.color.vermelho_erro));
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
                Log.e("API_ERROR_GET", "Erro ao fazer a requisição: " + t.getMessage());
                aux.abrirDialogErro(TelaCadastroInteresses.this,"Erro inesperado","Erro ao obter dados dos gêneros\nMensagem: "+t.getMessage());
            }
        });
    }
}