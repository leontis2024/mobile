package com.aula.leontis.activitys;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.aula.leontis.utilities.MetodosAux;
import com.aula.leontis.R;
import com.aula.leontis.interfaces.genero.GeneroInterface;
import com.aula.leontis.models.genero.GeneroCompleto;
import com.bumptech.glide.Glide;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class TelaInfoGenero extends AppCompatActivity {
    TextView erroGenero, nomeGenero, descGenero;
    ImageView fotoGenero;
    ImageButton btnVoltar;
    MetodosAux aux = new MetodosAux();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tela_info_genero);
        erroGenero = findViewById(R.id.erroGeneroInfo);
        nomeGenero = findViewById(R.id.nomeGenero);
        descGenero = findViewById(R.id.descGenero);
        fotoGenero = findViewById(R.id.fotoGenero);
        btnVoltar = findViewById(R.id.btnVoltar);
        btnVoltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        String id = "0";
        Intent info = getIntent();
        Bundle infoGenero = info.getExtras();
        if(infoGenero != null) {
            id = infoGenero.getString("id");
        }
        buscarGeneroPorId(id);

    }
    public void buscarGeneroPorId(String id){
        // Configurar Retrofit
        String urlAPI = "https://dev2-tfqz.onrender.com/";

        // Configurar acesso à API
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(urlAPI)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        GeneroInterface generoInterface = retrofit.create(GeneroInterface.class);

        Call<GeneroCompleto> call = generoInterface.buscarGenroPorId(id);

        // Buscar todos os gêneros
        call.enqueue(new Callback<GeneroCompleto>() {
            @Override
            public void onResponse(Call<GeneroCompleto>call, Response<GeneroCompleto> response) {
                if (response.isSuccessful() && response.body() != null) {
                    erroGenero.setVisibility(View.INVISIBLE);
                    erroGenero.setTextColor(getResources().getColor(R.color.vermelho_erro));
                    GeneroCompleto genero = response.body();

                    nomeGenero.setText(genero.getNomeGenero());
                    descGenero.setText(genero.getDescGenero());
                    String url = genero.getUrlImagem();
                    if (url == null){
                        url= "https://gamestation.com.br/wp-content/themes/game-station/images/image-not-found.png";
                    }
                    Glide.with(TelaInfoGenero.this).asBitmap().load(url).into(fotoGenero);

                } else {
                    erroGenero.setText("Falha ao obter dados do gênero");
                    erroGenero.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<GeneroCompleto> call, Throwable t) {
                Log.e("API_ERROR_GET_ID", "Erro ao fazer a requisição: " + t.getMessage());
                aux.abrirDialogErro(TelaInfoGenero.this,"Erro inesperado","Erro ao obter dados do gênero\nMensagem: "+t.getMessage());
            }
        });
    }
}