package com.aula.leontis.services;

import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.aula.leontis.Geral;
import com.aula.leontis.R;
import com.aula.leontis.activitys.TelaInfoObra;
import com.aula.leontis.activitys.TelaScanner;
import com.aula.leontis.adapters.AdapterComentario;
import com.aula.leontis.adapters.AdapterGenero;
import com.aula.leontis.adapters.AdapterHistorico;
import com.aula.leontis.adapters.AdapterObra;
import com.aula.leontis.adapters.AdapterObraGuia;
import com.aula.leontis.interfaces.mongo.MongoInterface;
import com.aula.leontis.interfaces.mongo.MongoInterface;
import com.aula.leontis.interfaces.usuario.UsuarioInterface;
import com.aula.leontis.models.avaliacao.Avaliacao;
import com.aula.leontis.models.comentario.Comentario;
import com.aula.leontis.models.comentario.ComentarioResponse;
import com.aula.leontis.models.genero.Genero;
import com.aula.leontis.models.guia.ObraGuia;
import com.aula.leontis.models.guia.StatusGuia;
import com.aula.leontis.models.guia.StatusGuiaRequest;
import com.aula.leontis.models.historico.Historico;
import com.aula.leontis.models.obra.Obra;
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
    ObraGuiaService obraGuiaService = new ObraGuiaService();
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
    public void inserirHistorico(String idUsuario, Historico historico, Context context) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(urlAPI)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        MongoInterface mongoInterface = retrofit.create(MongoInterface.class);
        Call<ResponseBody> call = mongoInterface.inserirHistorico(Long.parseLong(idUsuario),historico);

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
                    Log.d("MONGO_API_RESPONSE_POST_HISTORICO", "Historico do usuário inserido via API mongo: " + body);
                } else {
                    try {
                        // Obter e exibir o corpo da resposta de erro
                        String errorBody = response.errorBody().string();
                        Log.e("MONGO_API_ERROR_POST_HISTORICO", "Erro ao adicionar historico: " + response.code() + " - " + errorBody + " - " + response.message());
                        aux.abrirDialogErro(context, "Erro ao adicionar historico", "Não foi possível adicionar historico. Erro: " + errorBody);
                    } catch (IOException e) {
                        e.printStackTrace();
                        Log.e("MONGO_API_ERROR_POST_HISTORICO", "Erro ao processar o corpo da resposta de erro.");
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable throwable) {
                Log.e("MONGO_API_ERROR_POST_HISTORICO   ", "Erro ao adicionar historico: " + throwable.getMessage());
                aux.abrirDialogErro(context, "Erro ao adicionar historico", "Não foi possível adicionar historico. Erro: " + throwable.getMessage());
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


    public void buscarHistoricoObraPorIdUsuario(TextView erroHistorico, String id, Context context, RecyclerView rvHistorico, List<Historico> listaHistorico, AdapterHistorico adapterHistorico) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(urlAPI)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        MongoInterface mongoInterface = retrofit.create(MongoInterface.class);
        Call<List<Historico>> call = mongoInterface.buscarHistoricoPorId(Long.parseLong(id));

        call.enqueue(new Callback<List<Historico>>() {
            @Override
            public void onResponse(Call<List<Historico>> call, Response<List<Historico>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    erroHistorico.setVisibility(View.INVISIBLE);
                    erroHistorico.setTextColor(ContextCompat.getColor(context, R.color.vermelho_erro));

                    listaHistorico.clear();
                    listaHistorico.addAll(response.body());
                    adapterHistorico.notifyDataSetChanged();
                    rvHistorico.setAdapter(adapterHistorico);

                }
            }

            @Override
            public void onFailure(Call<List<Historico>> call, Throwable t) {
                Log.e("MONGO_API_ERROR_GET_HISTORICO", "Erro ao fazer a requisição: " + t.getMessage());
                aux.abrirDialogErro(context,"Erro inesperado","Erro ao obter o historico\nMensagem: "+t.getMessage());
            }
        });
    }

    public void selecionarStatusGuia(String idUsuario, Context context, long idGuia,ImageView img,int nrOrdem) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(urlAPI)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        MongoInterface mongoInterface = retrofit.create(MongoInterface.class);
        Call<StatusGuia> call = mongoInterface.selecionarStatusGuia(idGuia,Long.parseLong(idUsuario));

        call.enqueue(new Callback<StatusGuia>() {
            @Override
            public void onResponse(Call<StatusGuia> call, Response<StatusGuia> response) {
                if (response.isSuccessful() && response.body() != null) {
                    try {
                       StatusGuia statusGuia = response.body();
                       if(statusGuia.isConcluido() && img!=null){
                           img.setImageResource(R.drawable.guia_concluido_grande);

                       }else{
                           if(nrOrdem>0){
                               if(statusGuia.getNumeroPassoAtual()<nrOrdem) {
                                   obraGuiaService.selecionarTamanhoGuia(idUsuario,idGuia, context,nrOrdem);

                               }
                           }


                       }



                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.e("MONGO_ERROR_STATUS_GUIA", "Erro ao processar resposta: " + e.getMessage());
                    }
                } else {
                    if(idGuia>0 && nrOrdem>0) {
                        inserirStatusGuia(idUsuario, new StatusGuiaRequest(Long.parseLong(idUsuario), idGuia, false, 1), context);
                    }
                    Log.e("MONGO_ERROR_STATUS_GUIA", "Erro na resposta da API: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<StatusGuia> call, Throwable throwable) {
                Log.e("MONGO_ERROR_STATUS_GUIA", "Erro ao fazer a requisição: " + throwable.getMessage());
                aux.abrirDialogErro(context, "Erro inesperado", "Erro ao obter status guia\nMensagem: " + throwable.getMessage());
            }
        });
    }


    public void selecionarStatusGuiaAdapter(ObraService obraService,String idUsuario, Context context, long idGuia, AdapterObraGuia.viewHolderObraGuia holder,List<ObraGuia> listaObraGuias) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(urlAPI)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        MongoInterface mongoInterface = retrofit.create(MongoInterface.class);
        Call<StatusGuia> call = mongoInterface.selecionarStatusGuia(idGuia,Long.parseLong(idUsuario));

        call.enqueue(new Callback<StatusGuia>() {
            @Override
            public void onResponse(Call<StatusGuia> call, Response<StatusGuia> response) {
                if (response.isSuccessful() && response.body() != null) {
                    try {
                        StatusGuia statusGuia = response.body();
                        if(statusGuia.isConcluido()==false && holder.item!=null){
                            if(listaObraGuias.get(holder.getAdapterPosition()).getNrOrdem()<=statusGuia.getNumeroPassoAtual()) {
                                holder.item.setImageResource(R.drawable.item_mapa_concluido);
                            }else if(listaObraGuias.get(holder.getAdapterPosition()).getNrOrdem()==statusGuia.getNumeroPassoAtual()+1){
                                holder.item.setImageResource(R.drawable.item_mapa);
                                holder.itemView.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        holder.item.setImageResource(R.drawable.item_mapa_apertado);
                                        Dialog dialog = new Dialog(holder.itemView.getContext());
                                        dialog.setContentView(R.layout.dialog_obra_guia);
                                        dialog.getWindow().setLayout(WRAP_CONTENT, WRAP_CONTENT);
                                        dialog.getWindow().setBackgroundDrawableResource(R.drawable.caixa_mensagem_fundo);
                                        dialog.setCancelable(false);
                                        dialog.setCanceledOnTouchOutside(true);
                                        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                                            @Override
                                            public void onDismiss(DialogInterface dialogInterface) {
                                                // Ação ao fechar o dialog
                                                holder.item.setImageResource(R.drawable.item_mapa); // Exemplo: redefinir a imagem
                                            }
                                        });

                                        TextView localizaco = dialog.findViewById(R.id.descLocalizacao);
                                        ImageView obra = dialog.findViewById(R.id.imagemObra);
                                        obraService.buscarObraPorIdParcial(listaObraGuias.get(holder.getAdapterPosition()).getIdObra().toString(), holder.itemView.getContext(), obra, localizaco, listaObraGuias.get(holder.getAdapterPosition()).getDescLocalizacao());

                                        Button acessarScanner = dialog.findViewById(R.id.btnAcessarScanner);
                                        acessarScanner.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                Bundle bundle = new Bundle();
                                                Intent intent = new Intent(holder.itemView.getContext(), TelaScanner.class);

                                                bundle.putString("id", idUsuario);
                                                bundle.putString("idGuia", listaObraGuias.get(holder.getAdapterPosition()).getIdGuia().toString());
                                                bundle.putInt("nrOrdem", listaObraGuias.get(holder.getAdapterPosition()).getNrOrdem());
                                                intent.putExtras(bundle);
                                                holder.itemView.getContext().startActivity(intent);


                                            }
                                        });
                                        dialog.show();

                                    }
                                });

                            }else{
                                holder.item.setImageResource(R.drawable.item_mapa_desabilitado);
                            }
                        }else{
                            holder.item.setImageResource(R.drawable.item_mapa_concluido);
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.e("MONGO_ERROR_STATUS_GUIA", "Erro ao processar resposta: " + e.getMessage());
                    }
                } else {
                    if(response.code()==404) {
                        if(listaObraGuias.get(holder.getAdapterPosition()).getNrOrdem()==1){
                            holder.item.setImageResource(R.drawable.item_mapa);
                            holder.itemView.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    holder.item.setImageResource(R.drawable.item_mapa_apertado);
                                    Dialog dialog = new Dialog(holder.itemView.getContext());
                                    dialog.setContentView(R.layout.dialog_obra_guia);
                                    dialog.getWindow().setLayout(WRAP_CONTENT, WRAP_CONTENT);
                                    dialog.getWindow().setBackgroundDrawableResource(R.drawable.caixa_mensagem_fundo);
                                    dialog.setCancelable(false);
                                    dialog.setCanceledOnTouchOutside(true);
                                    dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                                        @Override
                                        public void onDismiss(DialogInterface dialogInterface) {
                                            // Ação ao fechar o dialog
                                            holder.item.setImageResource(R.drawable.item_mapa); // Exemplo: redefinir a imagem
                                        }
                                    });

                                    TextView localizaco = dialog.findViewById(R.id.descLocalizacao);
                                    ImageView obra = dialog.findViewById(R.id.imagemObra);
                                    obraService.buscarObraPorIdParcial(listaObraGuias.get(holder.getAdapterPosition()).getIdObra().toString(), holder.itemView.getContext(), obra, localizaco, listaObraGuias.get(holder.getAdapterPosition()).getDescLocalizacao());

                                    Button acessarScanner = dialog.findViewById(R.id.btnAcessarScanner);
                                    acessarScanner.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            Bundle bundle = new Bundle();
                                            Intent intent = new Intent(holder.itemView.getContext(), TelaScanner.class);

                                            bundle.putString("id", idUsuario);
                                            bundle.putString("idGuia", listaObraGuias.get(holder.getAdapterPosition()).getIdGuia().toString());
                                            bundle.putInt("nrOrdem", listaObraGuias.get(holder.getAdapterPosition()).getNrOrdem());
                                            intent.putExtras(bundle);
                                            holder.itemView.getContext().startActivity(intent);


                                        }
                                    });
                                    dialog.show();

                                }
                            });
                        }else{
                            holder.item.setImageResource(R.drawable.item_mapa_desabilitado);
                        }
                    }
                    Log.e("MONGO_ERROR_STATUS_GUIA", "Erro na resposta da API: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<StatusGuia> call, Throwable throwable) {
                Log.e("MONGO_ERROR_STATUS_GUIA", "Erro ao fazer a requisição: " + throwable.getMessage());
                aux.abrirDialogErro(context, "Erro inesperado", "Erro ao obter status guia\nMensagem: " + throwable.getMessage());
            }
        });
    }


    public void inserirStatusGuia(String idUsuario, StatusGuiaRequest statusGuiaRequest, Context context) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(urlAPI)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        MongoInterface mongoInterface = retrofit.create(MongoInterface.class);
        Call<ResponseBody> call = mongoInterface.inserirStatusGuia(Long.parseLong(idUsuario),statusGuiaRequest);

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
                    Log.d("MONGO_API_RESPONSE_POST_STATUS_GUIA", "Status guia do usuário inserido via API mongo: " + body);
                } else {
                    try {
                        // Obter e exibir o corpo da resposta de erro
                        String errorBody = response.errorBody().string();
                        Log.e("MONGO_API_ERROR_POST_STATUS_GUIA", "Erro ao adicionar status guia: " + response.code() + " - " + errorBody + " - " + response.message());
                        aux.abrirDialogErro(context, "Erro ao adicionar status guia", "Não foi possível adicionar status guia. Erro: " + errorBody);
                    } catch (IOException e) {
                        e.printStackTrace();
                        Log.e("MONGO_API_ERROR_POST_STATUS_GUIA", "Erro ao processar o corpo da resposta de erro.");
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

    public void buscarComentariosPorIdUsuario( String usuarioId,Context context, List<Comentario> listaComentarios) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(urlAPI)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        MongoInterface mongoInterface = retrofit.create(MongoInterface.class);
        Call<List<Comentario>> call = mongoInterface.buscarComentariosPorId(Long.parseLong(usuarioId));

        call.enqueue(new Callback<List<Comentario>>() {
            @Override
            public void onResponse(Call<List<Comentario>> call, Response<List<Comentario>> response) {
                if (response.isSuccessful() && response.body() != null) {

                    listaComentarios.clear();
                    listaComentarios.addAll(response.body());


                }
            }

            @Override
            public void onFailure(Call<List<Comentario>> call, Throwable t) {
                Log.e("MONGO_API_ERROR_GET_COMENTARIOS", "Erro ao fazer a requisição: " + t.getMessage());
                aux.abrirDialogErro(context,"Erro inesperado","Erro ao obter comentarios\nMensagem: "+t.getMessage());
            }
        });
    }

    public void buscarAvaliacaoPorIdUsuario( String usuarioId,Context context, List<Avaliacao> listaAvaliacoes) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(urlAPI)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        MongoInterface mongoInterface = retrofit.create(MongoInterface.class);
        Call<List<Avaliacao>> call = mongoInterface.buscarAvaliacoesPorId(Long.parseLong(usuarioId));

        call.enqueue(new Callback<List<Avaliacao>>() {
            @Override
            public void onResponse(Call<List<Avaliacao>> call, Response<List<Avaliacao>> response) {
                if (response.isSuccessful() && response.body() != null) {

                    listaAvaliacoes.clear();
                    listaAvaliacoes.addAll(response.body());


                }
            }

            @Override
            public void onFailure(Call<List<Avaliacao>> call, Throwable t) {
                Log.e("MONGO_API_ERROR_GET_COMENTARIOS", "Erro ao fazer a requisição: " + t.getMessage());
                aux.abrirDialogErro(context,"Erro inesperado","Erro ao obter comentarios\nMensagem: "+t.getMessage());
            }
        });
    }



}
