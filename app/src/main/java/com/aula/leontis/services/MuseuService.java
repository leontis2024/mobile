package com.aula.leontis.services;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.aula.leontis.R;
import com.aula.leontis.adapters.AdapterMuseu;
import com.aula.leontis.interfaces.museu.MuseuInterface;
import com.aula.leontis.models.museu.Museu;
import com.aula.leontis.utilities.MetodosAux;
import com.bumptech.glide.Glide;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MuseuService {
    MetodosAux aux =new MetodosAux();
    public void buscarMuseus(TextView erroMuseu, Context context, RecyclerView rvMuseus, List<Museu> listaMuseus, AdapterMuseu adapterMuseu) {
        erroMuseu.setTextColor(ContextCompat.getColor(context, R.color.azul_carregando));
        erroMuseu.setText("Carregando...");
        erroMuseu.setVisibility(View.VISIBLE);
        // Configurar Retrofit
//        String urlAPI = "https://dev2-tfqz.onrender.com/";
//
//        // Configurar acesso à API
//        Retrofit retrofit = new Retrofit.Builder()
//                .baseUrl(urlAPI)
//                .addConverterFactory(GsonConverterFactory.create())
//                .build();
//
//        MuseuInterface museuInterface = retrofit.create(MuseuInterface.class);
        ApiService apiService = new ApiService(context);
        MuseuInterface museuInterface = apiService.getMuseuInterface();
        Call<List<Museu>> call = museuInterface.selecionarTodosMuseus();

        // Buscar todos os gêneros
        call.enqueue(new Callback<List<Museu>>() {
            @Override
            public void onResponse(Call<List<Museu>> call, Response<List<Museu>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    erroMuseu.setVisibility(View.INVISIBLE);

                    listaMuseus.addAll(response.body());
                    adapterMuseu.notifyDataSetChanged();
                    rvMuseus.setAdapter(adapterMuseu);

                } else {
                    erroMuseu.setTextColor(ContextCompat.getColor(context, R.color.vermelho_erro));
                    Log.e("API_ERROR_GET", "Não foi possivel fazer a requisição: " + response.code());
                    erroMuseu.setText("Falha ao obter dados dos museus");
                    erroMuseu.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<List<Museu>> call, Throwable t) {
                erroMuseu.setTextColor(ContextCompat.getColor(context, R.color.vermelho_erro));
                Log.e("API_ERROR_GET", "Erro ao fazer a requisição: " + t.getMessage());
                erroMuseu.setText("Falha ao obter dados dos museus");
                aux.abrirDialogErro(context,"Erro inesperado","Erro ao obter dados dos museus\nMensagem: "+t.getMessage());
            }
        });
    }

    public void buscarMuseuPorId(String id, Context c, TextView erroMuseu,TextView nomeMuseu, TextView descMuseu, ImageView fotoMuseu) {
        // Configurar Retrofit
//        String urlAPI = "https://dev2-tfqz.onrender.com/";
//
//        // Configurar acesso à API
//        Retrofit retrofit = new Retrofit.Builder()
//                .baseUrl(urlAPI)
//                .addConverterFactory(GsonConverterFactory.create())
//                .build();
//
//        MuseuInterface generoInterface = retrofit.create(MuseuInterface.class);
        ApiService apiService = new ApiService(c);
        MuseuInterface generoInterface = apiService.getMuseuInterface();

        Call<Museu> call = generoInterface.buscarMuseuPorId(id);

        // Buscar todos os gêneros
        call.enqueue(new Callback<Museu>() {
            @Override
            public void onResponse(Call<Museu> call, Response<Museu> response) {
                if (response.isSuccessful() && response.body() != null) {
                    erroMuseu.setVisibility(View.INVISIBLE);
                    erroMuseu.setTextColor(ContextCompat.getColor(c, R.color.vermelho_erro));
                    Museu museu = response.body();

                    nomeMuseu.setText(museu.getNomeMuseu());
                    descMuseu.setText(museu.getDescMuseu());
                    String url = museu.getUrlImagem();
                    if (url == null) {
                        url = "https://gamestation.com.br/wp-content/themes/game-station/images/image-not-found.png";
                    }
                    Glide.with(c).asBitmap().load(url).into(fotoMuseu);

                } else {
                    erroMuseu.setText("Falha ao obter dados do museu");
                    erroMuseu.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<Museu> call, Throwable t) {
                Log.e("API_ERROR_GET_ID", "Erro ao fazer a requisição: " + t.getMessage());
                aux.abrirDialogErro(c, "Erro inesperado", "Erro ao obter dados do museu\nMensagem: " + t.getMessage());
            }
        });
    }
}
