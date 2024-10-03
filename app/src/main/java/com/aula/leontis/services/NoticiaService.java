package com.aula.leontis.services;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;

import com.aula.leontis.R;
import com.aula.leontis.interfaces.noticia.NoticiaInterface;
import com.aula.leontis.interfaces.noticia.OnFetchDataListener;
import com.aula.leontis.models.noticia.NewsApiResponse;

import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NoticiaService {
    String[] palavrasChave = {"Arte","Romero Britto","Rodolfo Amoêdo","Tarsila do Amaral","Anita Malfatti","Candido Portinari","Di Cavalcanti","Vicente do Rego Monteiro","Iberê Camargo","Georgina de Albuquerque","Almeida Júnior"};

    public void buscarNoticias(Context context, OnFetchDataListener listener, TextView erroNoticia){
        erroNoticia.setTextColor(ContextCompat.getColor(context, R.color.azul_carregando));
        erroNoticia.setText("Carregando notícias, isso pode demorar um pouco...");
        erroNoticia.setVisibility(View.VISIBLE);
        Random random = new Random();
        Retrofit retrofit = new Retrofit.Builder().baseUrl("https://newsapi.org/v2/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        NoticiaInterface noticiaInterface = retrofit.create(NoticiaInterface.class);
        Call<NewsApiResponse> call = noticiaInterface.callHeadlines("pt",palavrasChave[random.nextInt(palavrasChave.length)],context.getString(R.string.api_key),"publishedAt");

        try {
            call.enqueue(new Callback<NewsApiResponse>() {
                @Override
                public void onResponse(Call<NewsApiResponse> call, Response<NewsApiResponse> response) {
                    if (!response.isSuccessful()){
                        Toast.makeText(context, "Erro", Toast.LENGTH_SHORT).show();
                    }
                    listener.onfetchData(response.body().getArticles(),response.message());
                }

                @Override
                public void onFailure(Call<NewsApiResponse> call, Throwable throwable) {
                    erroNoticia.setTextColor(ContextCompat.getColor(context, R.color.vermelho_erro));
                    Log.e("API_ERROR_GET", "Erro ao fazer a requisição: " + throwable.getMessage());
                    erroNoticia.setText("Falha ao obter dados das notícias");
                    listener.onError("Request Failed");
                }
            });
        }catch (Exception e){
            erroNoticia.setTextColor(ContextCompat.getColor(context, R.color.vermelho_erro));
            Log.e("API_ERROR_GET", "Erro ao fazer a requisição: " + e.getMessage());
            erroNoticia.setText("Falha ao obter dados das notícias");
            e.printStackTrace();
        }
    }
}
