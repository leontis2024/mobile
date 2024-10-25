package com.aula.leontis.services;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.aula.leontis.Geral;
import com.aula.leontis.R;
import com.aula.leontis.adapters.AdapterGenero;
import com.aula.leontis.adapters.AdapterGeneroCompleto;
import com.aula.leontis.adapters.AdapterGeneroFiltro;
import com.aula.leontis.interfaces.genero.GeneroInterface;
import com.aula.leontis.interfaces.museu.MuseuInterface;
import com.aula.leontis.models.genero.Genero;
import com.aula.leontis.models.genero.GeneroCompleto;
import com.aula.leontis.models.museu.Museu;
import com.aula.leontis.utilities.MetodosAux;
import com.bumptech.glide.Glide;

import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class GeneroService {
    MetodosAux aux =new MetodosAux();
    String urlAPI = Geral.getInstance().getUrlApiSql();

    public void buscarGeneroPorId(String id, Context c, TextView erroGenero,TextView nomeGenero, TextView descGenero, ImageView fotoGenero) {
        ApiService apiService = new ApiService(c);
        GeneroInterface generoInterface = apiService.getGeneroInterface();

        Call<GeneroCompleto> call = generoInterface.buscarGeneroPorId(id);

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
                Log.e("API_ERROR_GET_ID_GENERO", "Erro ao fazer a requisição: " + t.getMessage());
                aux.abrirDialogErro(c,"Erro inesperado","Erro ao obter dados do gênero\nMensagem: "+t.getMessage());
            }
        });
    }
    public void buscarGeneros(TextView erroGenero,  Context context,RecyclerView rvGeneros, List<Genero> listaGeneros, AdapterGenero adapterGenero) {
        if(erroGenero!=null) {
            erroGenero.setTextColor(ContextCompat.getColor(context, R.color.azul_carregando));
            erroGenero.setText("Carregando...");
            erroGenero.setVisibility(View.VISIBLE);
        }

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(urlAPI)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        GeneroInterface generoInterface = retrofit.create(GeneroInterface.class);
        Call<List<Genero>> call = generoInterface.buscarTodosGenerosParciais();

        call.enqueue(new Callback<List<Genero>>() {
            @Override
            public void onResponse(Call<List<Genero>> call, Response<List<Genero>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    if(erroGenero!=null) {
                        erroGenero.setVisibility(View.INVISIBLE);
                        erroGenero.setTextColor(ContextCompat.getColor(context, R.color.vermelho_erro));
                    }
                    listaGeneros.clear();
                    listaGeneros.addAll(response.body());
                    adapterGenero.notifyDataSetChanged();
                    rvGeneros.setAdapter(adapterGenero);

                } else {
                    if(erroGenero!=null) {
                        erroGenero.setTextColor(ContextCompat.getColor(context, R.color.vermelho_erro));
                        erroGenero.setText("Falha ao obter dados dos gêneros");
                        erroGenero.setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Genero>> call, Throwable t) {
                if(erroGenero!=null) {
                    erroGenero.setText("Falha ao obter dados dos gêneros");
                    erroGenero.setTextColor(ContextCompat.getColor(context, R.color.vermelho_erro));
                }
                Log.e("API_ERROR_GET_GENERO_PARCIAL", "Erro ao fazer a requisição: " + t.getMessage());

                aux.abrirDialogErro(context,"Erro inesperado","Erro ao obter dados dos gêneros\nMensagem: "+t.getMessage());
            }
        });
    }

    public void buscarGenerosFiltro(TextView erroGenero,  Context context,RecyclerView rvGeneros, List<Genero> listaGeneros, AdapterGeneroFiltro adapterGeneroFiltro) {
        if(erroGenero!=null) {
            erroGenero.setTextColor(ContextCompat.getColor(context, R.color.azul_carregando));
            erroGenero.setText("Carregando...");
            erroGenero.setVisibility(View.VISIBLE);
        }

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(urlAPI)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        GeneroInterface generoInterface = retrofit.create(GeneroInterface.class);
        Call<List<Genero>> call = generoInterface.buscarTodosGenerosParciais();

        call.enqueue(new Callback<List<Genero>>() {
            @Override
            public void onResponse(Call<List<Genero>> call, Response<List<Genero>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    if(erroGenero!=null) {
                        erroGenero.setVisibility(View.INVISIBLE);
                        erroGenero.setTextColor(ContextCompat.getColor(context, R.color.vermelho_erro));
                    }
                    listaGeneros.clear();
                    listaGeneros.addAll(response.body());
                    adapterGeneroFiltro.notifyDataSetChanged();
                    rvGeneros.setAdapter(adapterGeneroFiltro);

                } else {
                    if(erroGenero!=null) {
                        erroGenero.setTextColor(ContextCompat.getColor(context, R.color.vermelho_erro));
                        erroGenero.setText("Falha ao obter dados dos gêneros");
                        erroGenero.setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Genero>> call, Throwable t) {
                if(erroGenero!=null) {
                    erroGenero.setText("Falha ao obter dados dos gêneros");
                    erroGenero.setTextColor(ContextCompat.getColor(context, R.color.vermelho_erro));
                }
                Log.e("API_ERROR_GET_GENERO_PARCIAL", "Erro ao fazer a requisição: " + t.getMessage());

                aux.abrirDialogErro(context,"Erro inesperado","Erro ao obter dados dos gêneros\nMensagem: "+t.getMessage());
            }
        });
    }


    public void buscarGenerosCompleto(TextView erroGenero, Context context, RecyclerView rvGeneros, List<GeneroCompleto> listaGeneros, AdapterGeneroCompleto adapterGeneroCompleto) {
        erroGenero.setTextColor(ContextCompat.getColor(context, R.color.azul_carregando));
        erroGenero.setText("Carregando...");
        erroGenero.setVisibility(View.VISIBLE);

        ApiService apiService = new ApiService(context);
        GeneroInterface generoInterface = apiService.getGeneroInterface();
        Call<List<GeneroCompleto>> call = generoInterface.buscarTodosGeneros();

        call.enqueue(new Callback<List<GeneroCompleto>>() {
            @Override
            public void onResponse(Call<List<GeneroCompleto>> call, Response<List<GeneroCompleto>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    erroGenero.setVisibility(View.INVISIBLE);
                    erroGenero.setTextColor(ContextCompat.getColor(context, R.color.vermelho_erro));
                    listaGeneros.clear();
                    listaGeneros.addAll(response.body());
                    adapterGeneroCompleto.notifyDataSetChanged();
                    rvGeneros.setAdapter(adapterGeneroCompleto);

                } else {
                    erroGenero.setTextColor(ContextCompat.getColor(context, R.color.vermelho_erro));
                    Log.e("API_ERROR_GET_GENERO_COMPLETO", "Não foi possivel fazer a requisição: " + response.code());
                    erroGenero.setText("Falha ao obter dados dos gêneros");
                    erroGenero.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<List<GeneroCompleto>> call, Throwable t) {
                erroGenero.setTextColor(ContextCompat.getColor(context, R.color.vermelho_erro));
                Log.e("API_ERROR_GET_GENERO_COMPLETO", "Erro ao fazer a requisição: " + t.getMessage());
                erroGenero.setText("Falha ao obter dados dos gêneros");
                aux.abrirDialogErro(context,"Erro inesperado","Erro ao obter dados dos gêneros\nMensagem: "+t.getMessage());
            }
        });
    }
    public void buscarGeneroPorIdParcial(String id, Context c, TextView erroGenero, TextView descGenero) {
        ApiService apiService = new ApiService(c);
        GeneroInterface generoInterface = apiService.getGeneroInterface();

        Call<GeneroCompleto> call = generoInterface.buscarGeneroPorId(id);

        call.enqueue(new Callback<GeneroCompleto>() {
            @Override
            public void onResponse(Call<GeneroCompleto>call, Response<GeneroCompleto> response) {
                if (response.isSuccessful() && response.body() != null) {
                    erroGenero.setVisibility(View.INVISIBLE);
                    erroGenero.setTextColor(ContextCompat.getColor(c, R.color.vermelho_erro));
                    GeneroCompleto genero = response.body();

                    descGenero.setText("Gênero artistico: "+genero.getNomeGenero());


                } else {
                    erroGenero.setText("Falha ao obter dados do gênero");
                    erroGenero.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<GeneroCompleto> call, Throwable t) {
                Log.e("API_ERROR_GET_ID_GENERO_PARCIAL", "Erro ao fazer a requisição: " + t.getMessage());
                aux.abrirDialogErro(c,"Erro inesperado","Erro ao obter dados do gênero\nMensagem: "+t.getMessage());
            }
        });
    }

    public void buscarGeneroPorNomePesquisa(String nome, Context c, TextView erro, RecyclerView rvGeneros, AdapterGeneroCompleto adapterGeneroCompleto,List<GeneroCompleto> listaGenero, ProgressBar progressBar) {
        erro.setVisibility(View.INVISIBLE);
        ApiService apiService = new ApiService(c);
        GeneroInterface generoInterface = apiService.getGeneroInterface();

        Call<List<GeneroCompleto>> call = generoInterface.selecionarGenerosPorNome(nome);
        call.enqueue(new Callback<List<GeneroCompleto>>() {
            @Override
            public void onResponse(Call<List<GeneroCompleto>> call, Response<List<GeneroCompleto>> response) {
                progressBar.setVisibility(View.INVISIBLE);
                if (response.isSuccessful() && response.body() != null) {
                    List<GeneroCompleto> generos = response.body();
                    rvGeneros.setLayoutManager(new LinearLayoutManager(c));
                    if (generos.size() > 0) {
                        if(generos.size()==1){
                            listaGenero.clear();
                            listaGenero.addAll(Collections.singletonList(generos.get(0)));
                            adapterGeneroCompleto.notifyDataSetChanged();
                            rvGeneros.setAdapter(adapterGeneroCompleto);
                        }else {
                            listaGenero.clear();
                            listaGenero.addAll(generos);
                            adapterGeneroCompleto.notifyDataSetChanged();
                            rvGeneros.setAdapter(adapterGeneroCompleto);
                        }
                        erro.setVisibility(View.INVISIBLE); // Esconda a mensagem de erro
                    } else {
                        listaGenero.clear();
                        adapterGeneroCompleto.notifyDataSetChanged();
                        rvGeneros.setAdapter(adapterGeneroCompleto);
                        erro.setTextColor(ContextCompat.getColor(c, R.color.vermelho_erro));
                        erro.setText("Nenhum gênero encontrado");
                        erro.setVisibility(View.VISIBLE);
                    }
                } else {
                    // Tente ler o corpo de erro

                    Log.e("API_ERROR_GET_NOME_GENERO", "Erro ao fazer a requisição: " + response.errorBody());
                    erro.setTextColor(ContextCompat.getColor(c, R.color.vermelho_erro));
                    erro.setText("Nenhum gênero encontrado");
                    erro.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<List<GeneroCompleto>> call, Throwable t) {
                Log.e("API_ERROR_GET_NOME_GENERO", "Erro ao fazer a requisição: " + t.getMessage());
                aux.abrirDialogErro(c, "Erro inesperado", "Erro ao obter dados dos museus\nMensagem: " + t.getMessage());
                progressBar.setVisibility(View.INVISIBLE); // Esconda a barra de progresso em caso de falha
            }
        });
    }
}
