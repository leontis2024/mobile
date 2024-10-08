package com.aula.leontis.services;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.aula.leontis.R;
import com.aula.leontis.adapters.AdapterGenero;
import com.aula.leontis.adapters.AdapterGeneroCompleto;
import com.aula.leontis.interfaces.genero.GeneroInterface;
import com.aula.leontis.models.genero.Genero;
import com.aula.leontis.models.genero.GeneroCompleto;
import com.aula.leontis.utilities.MetodosAux;
import com.bumptech.glide.Glide;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class GeneroService {
    MetodosAux aux =new MetodosAux();
    public void buscarGeneroPorId(String id, Context c, TextView erroGenero,TextView nomeGenero, TextView descGenero, ImageView fotoGenero) {
        ApiService apiService = new ApiService(c);
        GeneroInterface generoInterface = apiService.getGeneroInterface();

        Call<GeneroCompleto> call = generoInterface.buscarGeneroPorId(id);

        // Buscar todos os gêneros
        call.enqueue(new Callback<GeneroCompleto>() {
            @Override
            public void onResponse(Call<GeneroCompleto>call, Response<GeneroCompleto> response) {
                if (response.isSuccessful() && response.body() != null) {
                    erroGenero.setVisibility(View.INVISIBLE);
                    erroGenero.setTextColor(ContextCompat.getColor(c, R.color.vermelho_erro));
                    GeneroCompleto genero = response.body();

                    nomeGenero.setText(genero.getNomeGenero());
                    descGenero.setText(genero.getDescGenero());
                    String url = genero.getUrlImagem();
                    if (url == null){
                        url= "https://gamestation.com.br/wp-content/themes/game-station/images/image-not-found.png";
                    }
                    Glide.with(c).asBitmap().load(url).into(fotoGenero);


                } else {
                    erroGenero.setText("Falha ao obter dados do gênero");
                    erroGenero.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<GeneroCompleto> call, Throwable t) {
                Log.e("API_ERROR_GET_ID", "Erro ao fazer a requisição: " + t.getMessage());
                aux.abrirDialogErro(c,"Erro inesperado","Erro ao obter dados do gênero\nMensagem: "+t.getMessage());
            }
        });
    }
    public void buscarGeneros(TextView erroGenero,  Context context,RecyclerView rvGeneros, List<Genero> listaGeneros, AdapterGenero adapterGenero) {
        erroGenero.setTextColor(ContextCompat.getColor(context, R.color.azul_carregando));
        erroGenero.setText("Carregando...");
        erroGenero.setVisibility(View.VISIBLE);
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
                    erroGenero.setTextColor(ContextCompat.getColor(context, R.color.vermelho_erro));

                    listaGeneros.addAll(response.body());
                    adapterGenero.notifyDataSetChanged();
                    rvGeneros.setAdapter(adapterGenero);

                } else {
                    erroGenero.setTextColor(ContextCompat.getColor(context, R.color.vermelho_erro));
                    erroGenero.setText("Falha ao obter dados dos gêneros");
                    erroGenero.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<List<Genero>> call, Throwable t) {
                erroGenero.setTextColor(ContextCompat.getColor(context, R.color.vermelho_erro));
                Log.e("API_ERROR_GET", "Erro ao fazer a requisição: " + t.getMessage());
                erroGenero.setText("Falha ao obter dados dos gêneros");
                aux.abrirDialogErro(context,"Erro inesperado","Erro ao obter dados dos gêneros\nMensagem: "+t.getMessage());
            }
        });
    }
    public void buscarGenerosCompleto(TextView erroGenero, Context context, RecyclerView rvGeneros, List<GeneroCompleto> listaGeneros, AdapterGeneroCompleto adapterGeneroCompleto) {
        erroGenero.setTextColor(ContextCompat.getColor(context, R.color.azul_carregando));
        erroGenero.setText("Carregando...");
        erroGenero.setVisibility(View.VISIBLE);
        // Configurar Retrofit
//        String urlAPI = "https://dev2-tfqz.onrender.com/";
//
//        // Configurar acesso à API
//        Retrofit retrofit = new Retrofit.Builder()
//                .baseUrl(urlAPI)
//                .addConverterFactory(GsonConverterFactory.create())
//                .build();
//
//        GeneroInterface generoInterface = retrofit.create(GeneroInterface.class);

        ApiService apiService = new ApiService(context);
        GeneroInterface generoInterface = apiService.getGeneroInterface();
        Call<List<GeneroCompleto>> call = generoInterface.buscarTodosGeneros();

        // Buscar todos os gêneros
        call.enqueue(new Callback<List<GeneroCompleto>>() {
            @Override
            public void onResponse(Call<List<GeneroCompleto>> call, Response<List<GeneroCompleto>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    erroGenero.setVisibility(View.INVISIBLE);
                    erroGenero.setTextColor(ContextCompat.getColor(context, R.color.vermelho_erro));

                    listaGeneros.addAll(response.body());
                    adapterGeneroCompleto.notifyDataSetChanged();
                    rvGeneros.setAdapter(adapterGeneroCompleto);

                } else {
                    erroGenero.setTextColor(ContextCompat.getColor(context, R.color.vermelho_erro));
                    Log.e("API_ERROR_GET", "Não foi possivel fazer a requisição: " + response.code());
                    erroGenero.setText("Falha ao obter dados dos gêneros");
                    erroGenero.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<List<GeneroCompleto>> call, Throwable t) {
                erroGenero.setTextColor(ContextCompat.getColor(context, R.color.vermelho_erro));
                Log.e("API_ERROR_GET", "Erro ao fazer a requisição: " + t.getMessage());
                erroGenero.setText("Falha ao obter dados dos gêneros");
                aux.abrirDialogErro(context,"Erro inesperado","Erro ao obter dados dos gêneros\nMensagem: "+t.getMessage());
            }
        });
    }
    public void buscarGeneroPorIdParcial(String id, Context c, TextView erroGenero, TextView descGenero) {
        ApiService apiService = new ApiService(c);
        GeneroInterface generoInterface = apiService.getGeneroInterface();

        Call<GeneroCompleto> call = generoInterface.buscarGeneroPorId(id);

        // Buscar todos os gêneros
        call.enqueue(new Callback<GeneroCompleto>() {
            @Override
            public void onResponse(Call<GeneroCompleto>call, Response<GeneroCompleto> response) {
                if (response.isSuccessful() && response.body() != null) {
                    erroGenero.setVisibility(View.INVISIBLE);
                    erroGenero.setTextColor(ContextCompat.getColor(c, R.color.vermelho_erro));
                    GeneroCompleto genero = response.body();

                    descGenero.setText(descGenero.getText()+"\n\nGênero artistico: "+genero.getNomeGenero());


                } else {
                    erroGenero.setText("Falha ao obter dados do gênero");
                    erroGenero.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<GeneroCompleto> call, Throwable t) {
                Log.e("API_ERROR_GET_ID", "Erro ao fazer a requisição: " + t.getMessage());
                aux.abrirDialogErro(c,"Erro inesperado","Erro ao obter dados do gênero\nMensagem: "+t.getMessage());
            }
        });
    }
}
