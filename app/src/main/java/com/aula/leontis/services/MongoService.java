package com.aula.leontis.services;

import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.aula.leontis.Geral;
import com.aula.leontis.R;
import com.aula.leontis.activitys.TelaInfoObra;
import com.aula.leontis.adapters.AdapterComentario;
import com.aula.leontis.adapters.AdapterGenero;
import com.aula.leontis.interfaces.mongo.MongoInterface;
import com.aula.leontis.interfaces.mongo.MongoInterface;
import com.aula.leontis.interfaces.usuario.UsuarioInterface;
import com.aula.leontis.models.avaliacao.Avaliacao;
import com.aula.leontis.models.comentario.Comentario;
import com.aula.leontis.models.comentario.ComentarioResponse;
import com.aula.leontis.models.genero.Genero;
import com.aula.leontis.utilities.MetodosAux;
import com.bumptech.glide.Glide;

import org.json.JSONObject;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;


import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MongoService {
    MetodosAux aux = new MetodosAux();
    String urlAPI = Geral.getInstance().getUrlApiMongo();
    public void inserirComentario(String idUsuario, Comentario comentario, Context context) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(urlAPI)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        MongoInterface mongoInterface = retrofit.create(MongoInterface.class);
        Call<ResponseBody> call = mongoInterface.inserirComentario(Long.parseLong(idUsuario),comentario);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                String body = "";
                if (response.isSuccessful()) {
                    try {
                        body = response.body().string();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    Log.d("MONGO_API_RESPONSE_POST_COMENTARIO", "Comentario do usuário inserido via API mongo: " + body);
                } else {
                    try {
                        // Obter e exibir o corpo da resposta de erro
                        String errorBody = response.errorBody().string();
                        Log.e("MONGO_API_ERROR_POST_COMENTARIO", "Erro ao adicionar comentario: " + response.code() + " - " + errorBody + " - " + response.message());
                        aux.abrirDialogErro(context, "Erro ao comentar", "Não foi possível comentar. Erro: " + errorBody);
                    } catch (IOException e) {
                        e.printStackTrace();
                        Log.e("MONGO_API_ERROR_POST_COMENTARIO", "Erro ao processar o corpo da resposta de erro.");
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable throwable) {
                Log.e("MONGO_API_ERROR_POST", "Erro ao adicionar comentario: " + throwable.getMessage());
                aux.abrirDialogErro(context, "Erro ao comentar", "Não foi possível comentar. Erro: " + throwable.getMessage());
            }

        });
    }


    public void inserirAvaliacao(String idUsuario, Avaliacao avaliacao, Context context) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(urlAPI)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        MongoInterface mongoInterface = retrofit.create(MongoInterface.class);
        Call<ResponseBody> call = mongoInterface.inserirAvaliacao(Long.parseLong(idUsuario),avaliacao);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                String body = "";
                if (response.isSuccessful()) {
                    try {
                        body = response.body().string();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    Log.d("MONGO_API_RESPONSE_POST_AVALIACAO", "Avaliacao do usuário inserido via API mongo: " + body);
                } else {
                    try {
                        // Obter e exibir o corpo da resposta de erro
                        String errorBody = response.errorBody().string();
                        Log.e("MONGO_API_ERROR_POST_AVALIACAO", "Erro ao inserir avaliacao usuário mongo: " + response.code() + " - " + errorBody + " - " + response.message());
                        aux.abrirDialogErro(context, "Erro ao avaliar", "Não foi possível realizar a avaliacao. Erro: " + errorBody);
                    } catch (IOException e) {
                        e.printStackTrace();
                        Log.e("MONGO_API_ERROR_POST_AVALIACAO", "Erro ao processar o corpo da resposta de erro.");
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable throwable) {
                Log.e("MONGO_API_ERROR_POST", "Erro ao inserir avaliacao usuário mong: " + throwable.getMessage());
                aux.abrirDialogErro(context, "Erro ao inserir avaliacao", "Não foi possível avaliar. Erro: " + throwable.getMessage());
            }

        });
    }

    public void buscarComentariosPorIdObra(TextView erroComentario, String obraId,Context context, RecyclerView rvComentarios, List<ComentarioResponse> listaComentarios, AdapterComentario adapterComentarios,ProgressBar um,ProgressBar dois,ProgressBar tres,ProgressBar quatro,ProgressBar cinco, TextView avaliacaoObra) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(urlAPI)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        MongoInterface mongoInterface = retrofit.create(MongoInterface.class);
        Call<List<ComentarioResponse>> call = mongoInterface.buscarComentariosPorIdObra(Long.parseLong(obraId));

        call.enqueue(new Callback<List<ComentarioResponse>>() {
            @Override
            public void onResponse(Call<List<ComentarioResponse>> call, Response<List<ComentarioResponse>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    erroComentario.setVisibility(View.INVISIBLE);
                    erroComentario.setTextColor(ContextCompat.getColor(context, R.color.vermelho_erro));

                    listaComentarios.clear();
                    listaComentarios.addAll(response.body());
                    adapterComentarios.notifyDataSetChanged();
                    rvComentarios.setAdapter(adapterComentarios);

                    selecionarMediaNotaPorIdObra(obraId, context,avaliacaoObra,erroComentario);
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            selecionarPorcentagemPorAvaliacao(obraId,context,"1","2",um,erroComentario);
                            selecionarPorcentagemPorAvaliacao(obraId,context,"2","3",dois,erroComentario);
                            selecionarPorcentagemPorAvaliacao(obraId,context,"3","4",tres,erroComentario);
                            selecionarPorcentagemPorAvaliacao(obraId,context,"4","5",quatro,erroComentario);
                            selecionarPorcentagemPorAvaliacao(obraId,context,"5","6",cinco,erroComentario);
                        }
                    }, 1000);

                }
            }

            @Override
            public void onFailure(Call<List<ComentarioResponse>> call, Throwable t) {
                Log.e("MONGO_API_ERROR_GET_COMENTARIOS", "Erro ao fazer a requisição: " + t.getMessage());
                aux.abrirDialogErro(context,"Erro inesperado","Erro ao obter comentarios\nMensagem: "+t.getMessage());
            }
        });
    }

    public void selecionarMediaNotaPorIdObra(String obraId, Context context, TextView media, TextView erro) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(urlAPI)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        MongoInterface mongoInterface = retrofit.create(MongoInterface.class);
        Call<ResponseBody> call = mongoInterface.buscarMediaNotaPorIdObra(Long.parseLong(obraId));

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful() && response.body() != null) {
                    try {
                        String mediaStr = response.body().string();

                        double mediaApi = Double.parseDouble(mediaStr);
                        media.setText(String.format("%.1f", mediaApi).replace(".",","));

                        // Faça algo com os valores obtidos
                        Log.d("MONGO_RESPONSE_MEDIA", "Média obtida: " + mediaApi);

                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.e("MONGO_ERROR_MEDIA", "Erro ao processar resposta: " + e.getMessage());
                    }
                } else {
                    Log.e("MONGO_ERROR_MEDIA", "Erro na resposta da API: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable throwable) {
                Log.e("MONGO_ERROR_API", "Erro ao fazer a requisição: " + throwable.getMessage());
                aux.abrirDialogErro(context, "Erro inesperado", "Erro ao obter media\nMensagem: " + throwable.getMessage());
            }
        });
    }

    public void selecionarPorcentagemPorAvaliacao(String obraId, Context context, String notaMinima, String notaMaxima, ProgressBar progressBar,TextView erro) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(urlAPI)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        MongoInterface mongoInterface = retrofit.create(MongoInterface.class);
        Call<ResponseBody> call = mongoInterface.buscarPorcentagem(Long.parseLong(obraId),Double.parseDouble(notaMinima),Double.parseDouble(notaMaxima));

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful() && response.body() != null) {
                    try {
                        String mediaStr = response.body().string();

                        double mediaApi = Double.parseDouble(mediaStr);
                        progressBar.setProgress((int) mediaApi);

                        // Faça algo com os valores obtidos
                        Log.d("MONGO_RESPONSE_PORCENTAGEM", "Porcentagem obtida: " + mediaApi);

                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.e("MONGO_ERROR_PORCENTAGEM", "Erro ao processar resposta: " + e.getMessage());
                    }
                } else {
                    Log.e("MONGO_ERROR_PORCENTAGEM", "Erro na resposta da API: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable throwable) {
                Log.e("MONGO_ERROR_PORCENTAGEM", "Erro ao fazer a requisição: " + throwable.getMessage());
                aux.abrirDialogErro(context, "Erro inesperado", "Erro ao obter porcentagem\nMensagem: " + throwable.getMessage());
            }
        });
    }

}
