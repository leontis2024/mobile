package com.aula.leontis.services;

import android.content.Context;
import android.media.Image;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.aula.leontis.R;
import com.aula.leontis.adapters.AdapterObra;
import com.aula.leontis.interfaces.museu.MuseuInterface;
import com.aula.leontis.interfaces.obra.ObraInterface;
import com.aula.leontis.interfaces.obra.ObraInterface;
import com.aula.leontis.models.museu.Museu;
import com.aula.leontis.models.obra.Obra;
import com.aula.leontis.models.obra.Obra;
import com.aula.leontis.utilities.MetodosAux;
import com.bumptech.glide.Glide;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ObraService {
    MetodosAux aux =new MetodosAux();
    ArtistaService artistaService = new ArtistaService();
    GeneroService generoService = new GeneroService();
    MuseuService museuService = new MuseuService();
    public void buscarObrasPorMuseu(String idMuseu,TextView erroObra, Context context, RecyclerView rvObras, List<Obra> listaObras, AdapterObra adapterObra) {
        erroObra.setTextColor(ContextCompat.getColor(context, R.color.azul_carregando));
        erroObra.setText("Carregando...");
        erroObra.setVisibility(View.VISIBLE);

        ApiService apiService = new ApiService(context);
        ObraInterface obraInterface = apiService.getObraInterface();
        Call<List<Obra>> call = obraInterface.selecionarObrasPorMuseu(Long.parseLong(idMuseu));

        // Buscar todos os gêneros
        call.enqueue(new Callback<List<Obra>>() {
            @Override
            public void onResponse(Call<List<Obra>> call, Response<List<Obra>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    erroObra.setVisibility(View.INVISIBLE);
                    List<Obra> obras = response.body();
                    if(obras.size()!=0){

                        listaObras.addAll(response.body());
                        adapterObra.notifyDataSetChanged();
                        rvObras.setAdapter(adapterObra);
                    }

                } else {
                    if(response.code()!=404) {
                        erroObra.setTextColor(ContextCompat.getColor(context, R.color.vermelho_erro));
                        Log.e("API_ERROR_GET_OBRA", "Não foi possivel fazer a requisição: " + response.code() + " " + response.errorBody());
                        erroObra.setText("Falha ao obter dados das obras");
                        erroObra.setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Obra>> call, Throwable t) {
                erroObra.setTextColor(ContextCompat.getColor(context, R.color.vermelho_erro));
                Log.e("API_ERROR_GET", "Erro ao fazer a requisição: " + t.getMessage());
                erroObra.setText("Falha ao obter dados das obras");
                aux.abrirDialogErro(context,"Erro inesperado","Erro ao obter dados das obras\nMensagem: "+t.getMessage());
            }
        });
    }

    public void buscarObrasPorGenero(String idGenero,TextView erroObra, Context context, RecyclerView rvObras, List<Obra> listaObras, AdapterObra adapterObra) {
        erroObra.setTextColor(ContextCompat.getColor(context, R.color.azul_carregando));
        erroObra.setText("Carregando...");
        erroObra.setVisibility(View.VISIBLE);

        ApiService apiService = new ApiService(context);
        ObraInterface obraInterface = apiService.getObraInterface();
        Call<List<Obra>> call = obraInterface.selecionarObrasPorGenero(Long.parseLong(idGenero));

        call.enqueue(new Callback<List<Obra>>() {
            @Override
            public void onResponse(Call<List<Obra>> call, Response<List<Obra>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    erroObra.setVisibility(View.INVISIBLE);
                    List<Obra> obras = response.body();
                    if(obras.size()!=0){

                        listaObras.addAll(response.body());
                        adapterObra.notifyDataSetChanged();
                        rvObras.setAdapter(adapterObra);
                    }

                } else {
                    if(response.code()!=404) {
                        erroObra.setTextColor(ContextCompat.getColor(context, R.color.vermelho_erro));
                        Log.e("API_ERROR_GET_OBRA_GENERO", "Não foi possivel fazer a requisição: " + response.code() + " " + response.errorBody());
                        erroObra.setText("Falha ao obter dados das obras");
                        erroObra.setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Obra>> call, Throwable t) {
                erroObra.setTextColor(ContextCompat.getColor(context, R.color.vermelho_erro));
                Log.e("API_ERROR_GET_OBRA_GENERO", "Erro ao fazer a requisição: " + t.getMessage());
                erroObra.setText("Falha ao obter dados das obras");
                aux.abrirDialogErro(context,"Erro inesperado","Erro ao obter dados das obras\nMensagem: "+t.getMessage());
            }
        });
    }


    public void buscarObrasPorVariosGeneros(List<Long> generos,TextView erroObra, Context context, RecyclerView rvObras, List<Obra> listaObras, AdapterObra adapterObra) {
        erroObra.setTextColor(ContextCompat.getColor(context, R.color.azul_carregando));
        erroObra.setText("Carregando...");
        erroObra.setVisibility(View.VISIBLE);

        ApiService apiService = new ApiService(context);
        ObraInterface obraInterface = apiService.getObraInterface();
        Call<List<Obra>> call = obraInterface.selecionarObrasPorVariosGeneros(generos);

        call.enqueue(new Callback<List<Obra>>() {
            @Override
            public void onResponse(Call<List<Obra>> call, Response<List<Obra>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    erroObra.setVisibility(View.INVISIBLE);
                    List<Obra> obras = response.body();
                    if(obras.size()!=0){

                        listaObras.addAll(response.body());
                        adapterObra.notifyDataSetChanged();
                        rvObras.setAdapter(adapterObra);
                    }

                } else {
                    if(response.code()!=404) {
                        erroObra.setTextColor(ContextCompat.getColor(context, R.color.vermelho_erro));
                        Log.e("API_ERROR_GET_OBRA_GENERO", "Não foi possivel fazer a requisição: " + response.code() + " " + response.errorBody());
                        erroObra.setText("Falha ao obter dados das obras");
                        erroObra.setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Obra>> call, Throwable t) {
                erroObra.setTextColor(ContextCompat.getColor(context, R.color.vermelho_erro));
                Log.e("API_ERROR_GET_OBRA_GENERO", "Erro ao fazer a requisição: " + t.getMessage());
                erroObra.setText("Falha ao obter dados das obras");
                aux.abrirDialogErro(context,"Erro inesperado","Erro ao obter dados das obras\nMensagem: "+t.getMessage());
            }
        });
    }






    public void buscarObrasPorArtista(String idArtista,TextView erroObra, Context context, RecyclerView rvObras, List<Obra> listaObras, AdapterObra adapterObra) {
        erroObra.setTextColor(ContextCompat.getColor(context, R.color.azul_carregando));
        erroObra.setText("Carregando...");
        erroObra.setVisibility(View.VISIBLE);

        ApiService apiService = new ApiService(context);
        ObraInterface obraInterface = apiService.getObraInterface();
        Call<List<Obra>> call = obraInterface.selecionarObrasPorArtista(Long.parseLong(idArtista));

        call.enqueue(new Callback<List<Obra>>() {
            @Override
            public void onResponse(Call<List<Obra>> call, Response<List<Obra>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    erroObra.setVisibility(View.INVISIBLE);
                    List<Obra> obras = response.body();
                    if(obras.size()!=0){

                        listaObras.addAll(response.body());
                        adapterObra.notifyDataSetChanged();
                        rvObras.setAdapter(adapterObra);
                    }

                } else {
                    if(response.code()!=404) {
                        erroObra.setTextColor(ContextCompat.getColor(context, R.color.vermelho_erro));
                        Log.e("API_ERROR_GET", "Não foi possivel fazer a requisição: " + response.code() + " " + response.errorBody());
                        erroObra.setText("Falha ao obter dados das obras");
                        erroObra.setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Obra>> call, Throwable t) {
                erroObra.setTextColor(ContextCompat.getColor(context, R.color.vermelho_erro));
                Log.e("API_ERROR_GET", "Erro ao fazer a requisição: " + t.getMessage());
                erroObra.setText("Falha ao obter dados das obras");
                aux.abrirDialogErro(context,"Erro inesperado","Erro ao obter dados das obras\nMensagem: "+t.getMessage());
            }
        });
    }

    public void buscarObraPorId(String id, Context c, TextView erroObra, TextView nomeObra, TextView descObra, ImageView fotoObra, TextView descMuseu, ImageView imgMuseu,TextView urlText) {
        ApiService apiService = new ApiService(c);
        ObraInterface obraInterface= apiService.getObraInterface();

        Call<Obra> call = obraInterface.selecionarObraPorId(Long.parseLong(id));

        // Buscar todos os gêneros
        call.enqueue(new Callback<Obra>() {
            @Override
            public void onResponse(Call<Obra> call, Response<Obra> response) {
                if (response.isSuccessful() && response.body() != null) {
                    erroObra.setVisibility(View.INVISIBLE);
                    erroObra.setTextColor(ContextCompat.getColor(c, R.color.vermelho_erro));
                    Obra obra = response.body();

                    nomeObra.setText(obra.getNomeObra());
                    artistaService.buscarArtistaPorIdParcial(obra.getIdArtista(), c, erroObra, descObra);
                    Handler esperar = new Handler();
                    esperar.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            generoService.buscarGeneroPorIdParcial(obra.getIdGenero(), c, erroObra, descObra);
                        }
                    }, 1000);

                    Handler espera = new Handler();
                    espera.postDelayed(new Runnable() {
                        @Override
                        public void run() {

                            descObra.setText(descObra.getText()+"\n\nFeita em: "+obra.getAnoInicio()+"\n\n"+obra.getDescObra());
                        }
                    }, 2200);

                    museuService.buscarMuseuPorIdParcial(obra.getIdMuseu(), c, erroObra, descMuseu, imgMuseu);


                    String url = obra.getUrlImagem();
                    if (url == null) {
                        url = "https://gamestation.com.br/wp-content/themes/game-station/images/image-not-found.png";
                    }
                    urlText.setText(url);
                    Glide.with(c).asBitmap().load(url).into(fotoObra);

                } else {
                    erroObra.setText("Falha ao obter dados da obra");
                    erroObra.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<Obra> call, Throwable t) {
                Log.e("API_ERROR_GET_ID", "Erro ao fazer a requisição: " + t.getMessage());
                aux.abrirDialogErro(c, "Erro inesperado", "Erro ao obter dados da obra\nMensagem: " + t.getMessage());
            }
        });
    }








}
