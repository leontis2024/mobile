package com.aula.leontis.services;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.aula.leontis.R;
import com.aula.leontis.interfaces.artista.ArtistaInterface;
import com.aula.leontis.models.artista.Artista;
import com.aula.leontis.utilities.MetodosAux;
import com.bumptech.glide.Glide;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ArtistaService {
    MetodosAux aux =new MetodosAux();
    public void buscarArtistaPorId(String id, Context c, TextView erroArtista, TextView nomeArtista, TextView descArtista, ImageView fotoArtista) {
        ApiService apiService = new ApiService(c);
        ArtistaInterface artistaInterface = apiService.getArtistaInterface();

        Call<Artista> call = artistaInterface.buscarArtistaPorId(Long.parseLong(id));

        call.enqueue(new Callback<Artista>() {
            @Override
            public void onResponse(Call<Artista>call, Response<Artista> response) {
                if (response.isSuccessful() && response.body() != null) {
                    erroArtista.setVisibility(View.INVISIBLE);
                    erroArtista.setTextColor(ContextCompat.getColor(c, R.color.vermelho_erro));
                    Artista artista = response.body();

                    nomeArtista.setText(artista.getNomeArtista());
                    descArtista.setText(artista.getDescArtista());
                    String url = artista.getUrlImagem();
                    if (url == null){
                        url= "https://gamestation.com.br/wp-content/themes/game-station/images/image-not-found.png";
                    }
                    Glide.with(c).asBitmap().load(url).into(fotoArtista);


                } else {
                    erroArtista.setText("Falha ao obter dados do artista");
                    erroArtista.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<Artista> call, Throwable t) {
                Log.e("API_ERROR_GET_ARTISTA_ID", "Erro ao fazer a requisição: " + t.getMessage());
                aux.abrirDialogErro(c,"Erro inesperado","Erro ao obter dados do artista\nMensagem: "+t.getMessage());
            }
        });
    }
    public void buscarArtistaPorIdParcial(String id, Context c, TextView erroArtista,  TextView descArtista) {
        ApiService apiService = new ApiService(c);
        ArtistaInterface artistaInterface = apiService.getArtistaInterface();

        Call<Artista> call = artistaInterface.buscarArtistaPorId(Long.parseLong(id));

        call.enqueue(new Callback<Artista>() {
            @Override
            public void onResponse(Call<Artista>call, Response<Artista> response) {
                if (response.isSuccessful() && response.body() != null) {
                    erroArtista.setVisibility(View.INVISIBLE);
                    erroArtista.setTextColor(ContextCompat.getColor(c, R.color.vermelho_erro));
                    Artista artista = response.body();

                    descArtista.setText(artista.getNomeArtista()+" ( "+artista.getDtNascArtista().substring(0,4)+" - "+artista.getDtFalecimento().substring(0,4)+" )");


                } else {
                    erroArtista.setText("Falha ao obter dados do artista");
                    erroArtista.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<Artista> call, Throwable t) {
                Log.e("API_ERROR_GET_ARTISTA_ID_PARCIAL", "Erro ao fazer a requisição: " + t.getMessage());
                aux.abrirDialogErro(c,"Erro inesperado","Erro ao obter dados do artista\nMensagem: "+t.getMessage());
            }
        });
    }
}
