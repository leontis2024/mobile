package com.aula.leontis.services;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.aula.leontis.R;
import com.aula.leontis.activities.TelaInfoObra;
import com.aula.leontis.adapters.AdapterObra;
import com.aula.leontis.adapters.AdapterObraFeed;
import com.aula.leontis.interfaces.obra.ObraInterface;
import com.aula.leontis.models.historico.Historico;
import com.aula.leontis.models.obra.Obra;
import com.aula.leontis.utilities.MetodosAux;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ObraService {
    MetodosAux aux =new MetodosAux();
    ArtistaService artistaService = new ArtistaService();
    GeneroService generoService = new GeneroService();
    MuseuService museuService = new MuseuService();
    MongoService mongoService = new MongoService();
    public void buscarObrasPorMuseu(String idMuseu,TextView erroObra, Context context, RecyclerView rvObras, List<Obra> listaObras, AdapterObra adapterObra) {
        erroObra.setTextColor(ContextCompat.getColor(context, R.color.azul_carregando));
        erroObra.setText("Carregando...");
        erroObra.setVisibility(View.VISIBLE);

        ApiService apiService = new ApiService(context);
        ObraInterface obraInterface = apiService.getObraInterface();
        Call<List<Obra>> call = obraInterface.selecionarObrasPorMuseu(Long.parseLong(idMuseu));

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
                Log.e("API_ERROR_GET_OBRA", "Erro ao fazer a requisição: " + t.getMessage());
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


    public void buscarObrasPorVariosGeneros(List<Long> generos,TextView erroObra, Context context, RecyclerView rvObras, List<Obra> listaObras, AdapterObraFeed adapterObra,ProgressBar progressBar) {
        erroObra.setTextColor(ContextCompat.getColor(context, R.color.azul_carregando));
        ApiService apiService = new ApiService(context);
        ObraInterface obraInterface = apiService.getObraInterface();
        if(generos.size()==0){
            generos.add(-1L);
        }
        Call<List<Obra>> call = obraInterface.selecionarObrasPorVariosGeneros(generos);


        call.enqueue(new Callback<List<Obra>>() {
            @Override
            public void onResponse(Call<List<Obra>> call, Response<List<Obra>> response) {
                progressBar.setVisibility(View.INVISIBLE);
                if (response.isSuccessful() ) {
                    erroObra.setVisibility(View.INVISIBLE);
                    List<Obra> obras = response.body();
                    if(obras.size()!=0){
                        rvObras.setLayoutManager(new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL));
                        listaObras.clear();
                        listaObras.addAll(response.body());
                        adapterObra.notifyDataSetChanged();
                        rvObras.setAdapter(adapterObra);
                    }

                } else {
                    if(response.code()!=404) {
                        erroObra.setTextColor(ContextCompat.getColor(context, R.color.vermelho_erro));
                        Log.e("API_ERROR_GET_OBRA_GENEROS", "Não foi possivel fazer a requisição: " + response.code() + " " + response.errorBody());
                        erroObra.setText("Falha ao obter dados das obras");
                        erroObra.setVisibility(View.VISIBLE);
                    }
                    rvObras.setLayoutManager(new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL));
                    listaObras.clear();
                    adapterObra.notifyDataSetChanged();
                    rvObras.setAdapter(adapterObra);
                }
            }

            @Override
            public void onFailure(Call<List<Obra>> call, Throwable t) {
                erroObra.setTextColor(ContextCompat.getColor(context, R.color.vermelho_erro));
                Log.e("API_ERROR_GET_OBRA_GENEROS", "Erro ao fazer a requisição: " + t.getMessage());
                erroObra.setText("Falha ao obter dados das obras");
                aux.abrirDialogErro(context,"Erro inesperado","Erro ao obter dados das obras\nMensagem: "+t.getMessage());
            }
        });
    }

    public void buscarObrasPorVariosMuseus(List<Long> museus, TextView erroObra, Context context, RecyclerView rvObras, List<Obra> listaObras, AdapterObraFeed adapterObra, ProgressBar progressBar) {
        erroObra.setTextColor(ContextCompat.getColor(context, R.color.azul_carregando));
        ApiService apiService = new ApiService(context);
        ObraInterface obraInterface = apiService.getObraInterface();
        if(museus.size()==0){
            museus.add(-1L);
        }
        Call<List<Obra>> call = obraInterface.selecionarObrasPorVariosMuseus(museus);

        call.enqueue(new Callback<List<Obra>>() {
            @Override
            public void onResponse(Call<List<Obra>> call, Response<List<Obra>> response) {
                progressBar.setVisibility(View.INVISIBLE);
                if (response.isSuccessful()) {
                    erroObra.setVisibility(View.INVISIBLE);

                    List<Obra> obras = response.body();
                    if(obras.size()!=0){
                        rvObras.setLayoutManager(new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL));
                        listaObras.clear();
                        listaObras.addAll(response.body());
                        adapterObra.notifyDataSetChanged();
                        rvObras.setAdapter(adapterObra);
                    }

                } else {
                    if(response.code()!=404) {
                        erroObra.setTextColor(ContextCompat.getColor(context, R.color.vermelho_erro));
                        Log.e("API_ERROR_GET_OBRA_MUSEUS", "Não foi possivel fazer a requisição: " + response.code() + " " + response.errorBody());
                        erroObra.setText("Falha ao obter dados das obras");
                        erroObra.setVisibility(View.VISIBLE);
                    }
                    rvObras.setLayoutManager(new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL));
                    listaObras.clear();
                    adapterObra.notifyDataSetChanged();
                    rvObras.setAdapter(adapterObra);
                }
            }

            @Override
            public void onFailure(Call<List<Obra>> call, Throwable t) {
                erroObra.setTextColor(ContextCompat.getColor(context, R.color.vermelho_erro));
                Log.e("API_ERROR_GET_OBRA_MUSEUS", "Erro ao fazer a requisição: " + t.getMessage());
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
                        Log.e("API_ERROR_GET_OBRA_ARTISTA", "Não foi possivel fazer a requisição: " + response.code() + " " + response.errorBody());
                        erroObra.setText("Falha ao obter dados das obras");
                        erroObra.setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Obra>> call, Throwable t) {
                erroObra.setTextColor(ContextCompat.getColor(context, R.color.vermelho_erro));
                Log.e("API_ERROR_GET_OBRA_ARTISTA", "Erro ao fazer a requisição: " + t.getMessage());
                erroObra.setText("Falha ao obter dados das obras");
                aux.abrirDialogErro(context,"Erro inesperado","Erro ao obter dados das obras\nMensagem: "+t.getMessage());
            }
        });
    }

    public void buscarObraPorId(String id, Context c, TextView erroObra, TextView nomeObra, TextView descObra, ImageView fotoObra, TextView descMuseu, ImageView imgMuseu,TextView descArtista, ImageView imgArtista,TextView urlText,TextView idGenero,TextView idArtista,TextView idMuseu) {
        ApiService apiService = new ApiService(c);
        ObraInterface obraInterface= apiService.getObraInterface();

        Call<Obra> call = obraInterface.selecionarObraPorId(Long.parseLong(id));

        call.enqueue(new Callback<Obra>() {
            @Override
            public void onResponse(Call<Obra> call, Response<Obra> response) {
                if (response.isSuccessful() && response.body() != null) {
                    erroObra.setVisibility(View.INVISIBLE);
                    erroObra.setTextColor(ContextCompat.getColor(c, R.color.vermelho_erro));
                    Obra obra = response.body();

                    nomeObra.setText(obra.getNomeObra());
                    generoService.buscarGeneroPorIdParcial(obra.getIdGenero(), c, erroObra, descObra);


                    Handler esperar = new Handler();
                    esperar.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            artistaService.buscarArtistaPorIdParcial(obra.getIdArtista(), c, erroObra, descObra);
                            artistaService.buscarArtistaPorIdParcialAParte(obra.getIdArtista(), c, erroObra, descArtista, imgArtista);
                        }
                    }, 1100);

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

                    idGenero.setText(obra.getIdGenero());
                    idArtista.setText(obra.getIdArtista());
                    idMuseu.setText(obra.getIdMuseu());
                } else {
                    erroObra.setText("Falha ao obter dados da obra");
                    erroObra.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<Obra> call, Throwable t) {
                Log.e("API_ERROR_GET_ID_OBRA", "Erro ao fazer a requisição: " + t.getMessage());
                aux.abrirDialogErro(c, "Erro inesperado", "Erro ao obter dados da obra\nMensagem: " + t.getMessage());
            }
        });
    }



    public void buscarObraPorIdParcial(String id, Context c,  ImageView fotoObra,TextView nome,String localizacao) {
        ApiService apiService = new ApiService(c);
        ObraInterface obraInterface= apiService.getObraInterface();

        Call<Obra> call = obraInterface.selecionarObraPorId(Long.parseLong(id));

        call.enqueue(new Callback<Obra>() {
            @Override
            public void onResponse(Call<Obra> call, Response<Obra> response) {
                if (response.isSuccessful() && response.body() != null) {

                    Obra obra = response.body();
                    if(nome!=null) {
                        nome.setText(obra.getNomeObra()+"\n\nLocalização: "+localizacao);
                    }

                    String url = obra.getUrlImagem();
                    if (url == null) {
                        url = "https://gamestation.com.br/wp-content/themes/game-station/images/image-not-found.png";
                    }
                    Glide.with(c).asBitmap().apply(RequestOptions.bitmapTransform(new RoundedCorners(20))).load(url).into(fotoObra);

                }
            }

            @Override
            public void onFailure(Call<Obra> call, Throwable t) {
                Log.e("API_ERROR_GET_ID_OBRA", "Erro ao fazer a requisição: " + t.getMessage());
                aux.abrirDialogErro(c, "Erro inesperado", "Erro ao obter dados da obra\nMensagem: " + t.getMessage());
            }
        });
    }



    public void buscarObraPorNome(String idUsuario,String nome, TextView idObra,String idGuia,int nrOrdem,Context c,TextView erro,ImageView borda) {
        erro.setVisibility(View.INVISIBLE);
        ApiService apiService = new ApiService(c);
        ObraInterface obraInterface= apiService.getObraInterface();

        Call<Obra> call = obraInterface.selecionarObraPorNome(nome);

        call.enqueue(new Callback<Obra>() {
            @Override
            public void onResponse(Call<Obra> call, Response<Obra> response) {
                if (response.isSuccessful() && response.body() != null) {
                    borda.setImageResource(R.drawable.borda_escaner);
                    erro.setVisibility(View.INVISIBLE);
                    Obra obra = response.body();
                    idObra.setText(obra.getId().toString());
                 //   mongoService.inserirHistorico(idUsuario,new Historico(Long.parseLong(obra.getId()),aux.dataAtualFormatada()),c);
                    mongoService.selecionarStatusGuia(idUsuario,c,Long.parseLong(idGuia),null,nrOrdem);

                    Bundle bundle = new Bundle();
                    bundle.putString("id",obra.getId().toString());
                    Intent i = new Intent(c, TelaInfoObra.class);
                    i.putExtras(bundle);
                    c.startActivity(i);

                }else{
                    borda.setImageResource(R.drawable.borda_scan_erro);
                    erro.setTextColor(ContextCompat.getColor(c, R.color.vermelho_erro));
                    erro.setText("Não foi possível escanear esta obra");
                    erro.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<Obra> call, Throwable t) {
                Log.e("API_ERROR_GET_ID_OBRA", "Erro ao fazer a requisição: " + t.getMessage());
                aux.abrirDialogErro(c, "Erro inesperado", "Erro ao obter dados da obra\nMensagem: " + t.getMessage());
            }
        });
    }


    public void buscarObraPorIdScanner(String idUsuario,String id, TextView idObra,String idGuia,int nrOrdem,Context c,TextView erro,ImageView borda) {
        erro.setVisibility(View.INVISIBLE);
        ApiService apiService = new ApiService(c);
        ObraInterface obraInterface= apiService.getObraInterface();

        Call<Obra> call = obraInterface.selecionarObraPorId(Long.parseLong(id));

        call.enqueue(new Callback<Obra>() {
            @Override
            public void onResponse(Call<Obra> call, Response<Obra> response) {
                if (response.isSuccessful() && response.body() != null) {
                    borda.setImageResource(R.drawable.borda_escaner);
                    erro.setVisibility(View.INVISIBLE);
                    Obra obra = response.body();
                    idObra.setText(obra.getId().toString());
                    mongoService.inserirHistorico(idUsuario, new Historico(Long.parseLong(obra.getId()), aux.dataAtualFormatada()), c);
                    if(idGuia!=null) {
                        mongoService.selecionarStatusGuia(idUsuario, c, Long.parseLong(idGuia), null, nrOrdem);
                    }

                    Bundle bundle = new Bundle();
                    bundle.putString("id",obra.getId().toString());
                    Intent i = new Intent(c, TelaInfoObra.class);
                    i.putExtras(bundle);
                    c.startActivity(i);

                }else{
                    borda.setImageResource(R.drawable.borda_scan_erro);
                    erro.setTextColor(ContextCompat.getColor(c, R.color.vermelho_erro));
                    erro.setText("Não foi possível escanear esta obra");
                    erro.setVisibility(View.VISIBLE);
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            erro.setVisibility(View.INVISIBLE);
                            borda.setImageResource(R.drawable.borda_escaner);
                        }
                    }, 3000);
                }
            }

            @Override
            public void onFailure(Call<Obra> call, Throwable t) {
                Log.e("API_ERROR_GET_ID_OBRA", "Erro ao fazer a requisição: " + t.getMessage());
                aux.abrirDialogErro(c, "Erro inesperado", "Erro ao obter dados da obra\nMensagem: " + t.getMessage());
            }
        });
    }


    public void buscarObraPorNomePesquisa(String nome, Context c, TextView erro, RecyclerView rvObras, AdapterObraFeed adapterObra, List<Obra> listaObras, ProgressBar progressBar) {
        erro.setVisibility(View.INVISIBLE);
        ApiService apiService = new ApiService(c);
        ObraInterface obraInterface = apiService.getObraInterface();

        Call<List<Obra>> call = obraInterface.selecionarObrasPorNome(nome);
        call.enqueue(new Callback<List<Obra>>() {
            @Override
            public void onResponse(Call<List<Obra>> call, Response<List<Obra>> response) {
                progressBar.setVisibility(View.INVISIBLE);
                if (response.isSuccessful() && response.body() != null) {
                    List<Obra> obras = response.body();
                    rvObras.setLayoutManager(new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL));
                    if (obras.size() > 0) {
                        if(obras.size()==1){
                            listaObras.clear();
                            listaObras.addAll(Collections.singletonList(obras.get(0)));
                            adapterObra.notifyDataSetChanged();
                            rvObras.setAdapter(adapterObra);
                        }else {
                            listaObras.clear();
                            listaObras.addAll(obras);
                            adapterObra.notifyDataSetChanged();
                            rvObras.setAdapter(adapterObra);
                        }
                        erro.setVisibility(View.INVISIBLE); // Esconda a mensagem de erro
                    } else {
                        listaObras.clear();
                        listaObras.addAll(obras);
                        adapterObra.notifyDataSetChanged();
                        rvObras.setAdapter(adapterObra);
                        erro.setTextColor(ContextCompat.getColor(c, R.color.vermelho_erro));
                        erro.setText("Nenhuma obra encontrada");
                        erro.setVisibility(View.VISIBLE);
                    }
                } else {
                    // Tente ler o corpo de erro

                    Log.e("API_ERROR_GET_ID_OBRA", "Erro ao fazer a requisição: " + response.errorBody());
                    erro.setTextColor(ContextCompat.getColor(c, R.color.vermelho_erro));
                    erro.setText("Nenhuma obra encontrada");
                    erro.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<List<Obra>> call, Throwable t) {
                Log.e("API_ERROR_GET_ID_OBRA", "Erro ao fazer a requisição: " + t.getMessage());
                aux.abrirDialogErro(c, "Erro inesperado", "Erro ao obter dados da obra\nMensagem: " + t.getMessage());
                progressBar.setVisibility(View.INVISIBLE); // Esconda a barra de progresso em caso de falha
            }
        });
    }



    public interface ObraCallback {
        void onObrasReceived(List<Obra> obras);
    }

    public void buscarTodasobras(Context context, final ObraCallback callback) {
        ApiService apiService = new ApiService(context);
        ObraInterface obraInterface = apiService.getObraInterface();
        Call<List<Obra>> call = obraInterface.selecionarTudasObras();

        call.enqueue(new Callback<List<Obra>>() {
            @Override
            public void onResponse(Call<List<Obra>> call, Response<List<Obra>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onObrasReceived(response.body()); // Chama o callback quando a resposta é recebida
                } else {
                    callback.onObrasReceived(new ArrayList<>()); // Chama o callback com lista vazia em caso de erro
                    Log.e("API_ERROR_GET_OBRA", "Não foi possível fazer a requisição: " + response.code() + " " + response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<List<Obra>> call, Throwable t) {
                callback.onObrasReceived(new ArrayList<>()); // Chama o callback com lista vazia em caso de falha
                Log.e("API_ERROR_GET_OBRA", "Erro ao fazer a requisição: " + t.getMessage());
            }
        });
    }
}
