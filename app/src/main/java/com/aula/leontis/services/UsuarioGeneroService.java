package com.aula.leontis.services;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.aula.leontis.Geral;
import com.aula.leontis.R;
import com.aula.leontis.adapters.AdapterGenero;
import com.aula.leontis.adapters.AdapterObra;
import com.aula.leontis.adapters.AdapterObraFeed;
import com.aula.leontis.interfaces.genero.GeneroInterface;
import com.aula.leontis.interfaces.usuario.UsuarioGeneroInterface;
import com.aula.leontis.interfaces.usuario.UsuarioInterface;
import com.aula.leontis.interfaces.usuario.UsuarioGeneroInterface;
import com.aula.leontis.models.genero.Genero;
import com.aula.leontis.models.obra.Obra;
import com.aula.leontis.models.usuario.UsuarioGenero;
import com.aula.leontis.utilities.MetodosAux;
import com.bumptech.glide.Glide;

import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class UsuarioGeneroService {
    MetodosAux aux = new MetodosAux();
    ObraService obraService = new ObraService();
    String urlAPI = Geral.getInstance().getUrlApiSql();

    public void inserirUsuarioGenero(Context context, String[] id, long[] listaGenerosInteresse) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(urlAPI)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        UsuarioGeneroInterface usuarioGeneroInterface = retrofit.create(UsuarioGeneroInterface.class);

        for (long idGenero : listaGenerosInteresse) {
            Call<ResponseBody> call = usuarioGeneroInterface.inserirUsuarioGenero(Long.parseLong(id[0]), idGenero);
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        try {
                            Log.d("API_RESPONSE_POST_USUARIO_GENERO", "Conexão usuário e genero criada: " + response.body().string());
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    } else {
                        try {
                            // Obter e exibir o corpo da resposta de erro
                            String errorBody = response.errorBody().string();
                            Log.e("API_ERROR_POST_USUARIO_GENERO", "Erro ao fazer conexão usuario e genero: " + response.code() + " - " + errorBody + " - " + response.message());
                        } catch (IOException e) {
                            e.printStackTrace();
                            Log.e("API_ERROR_POST_USUARIO_GENERO", "Erro ao processar o corpo da resposta de erro.");
                        }
                    }

                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable throwable) {
                    Log.e("API_ERROR_POST_USUARIO_GENERO", "Erro ao fazer conexão usuario e genero: " + throwable.getMessage());
                    aux.abrirDialogErro(context, "Erro inesperado", "Não foi possível realizar seu cadastro. Erro: " + throwable.getMessage());
                }
            });
        }


    }


    public void buscarSeExiste(String usuario,String genero, Context context, ImageButton interesse) {
        ApiService apiService = new ApiService(context);
        UsuarioGeneroInterface usuarioGeneroInterface = apiService.getUsuarioGeneroInterface();
        Call<UsuarioGenero> call = usuarioGeneroInterface.buscarSeExiste(Long.parseLong(usuario),Long.parseLong(genero));

        call.enqueue(new Callback<UsuarioGenero>() {
            @Override
            public void onResponse(Call<UsuarioGenero> call, Response<UsuarioGenero> response) {
                if (response.isSuccessful() && response.body() != null) {
                    try {
                       UsuarioGenero usuarioGenero = response.body();
                       if(usuarioGenero != null){
                           interesse.setImageResource(R.drawable.btn_interesse_selecionado);
                           interesse.setContentDescription("selecionado");
                       }else{
                           interesse.setImageResource(R.drawable.btn_interesse);
                           interesse.setContentDescription("vazio");
                       }

                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.e("API_ERROR_GET_ID_EXISTE_USUARIO_GENERO", "Erro ao processar resposta: " + e.getMessage());
                    }
                } else {
                    Log.e("API_ERROR_GET_ID_EXISTE_USUARIO_GENERO", "Erro na resposta da API: " + response.code()+" "+response.message());
                }
            }

            @Override
            public void onFailure(Call<UsuarioGenero> call, Throwable throwable) {
                Log.e("API_ERROR_GET_ID_EXISTE_USUARIO_GENERO", "Erro ao fazer a requisição: " + throwable.getMessage());
                aux.abrirDialogErro(context, "Erro inesperado", "Erro ao obter dados\nMensagem: " + throwable.getMessage());
            }
        });
    }
    public void deletarUsuarioGenero(Context context, String[] id, String idGenero) {
        ApiService apiService = new ApiService(context);
        UsuarioGeneroInterface usuarioGeneroInterface = apiService.getUsuarioGeneroInterface();

        Call<ResponseBody> call = usuarioGeneroInterface.deletarUsuarioGenero(Long.parseLong(idGenero),Long.parseLong(id[0]));
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful() && response.body() != null) {
                    try {
                        Log.d("API_RESPONSE_DELETE_USUARIO_GENERO", "O usuário de id: "+id[0]+" parou de ter interesse no genero de id: "+idGenero + response.body().string());
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                } else {
                    try {
                        // Obter e exibir o corpo da resposta de erro
                        String errorBody = response.errorBody().string();
                        Log.e("API_ERROR_DELETE_USUARIO_GENERO", "Erro ao desfazer conexão usuario e genero: " + response.code() + " - " + errorBody + " - " + response.message());
                    } catch (IOException e) {
                        e.printStackTrace();
                        Log.e("API_ERROR_DELETE_USUARIO_GENERO", "Erro ao processar o corpo da resposta de erro.");
                    }
                }
            }



            @Override
            public void onFailure(Call<ResponseBody> call, Throwable throwable) {
                Log.e("API_ERROR_DELETE_USUARIO_GENERO", "Erro ao desfazer conexão usuario e genero: " + throwable.getMessage());
                aux.abrirDialogErro(context, "Erro inesperado", "Não foi possível realizar seu cadastro. Erro: " + throwable.getMessage());
            }
        });

    }

    public void buscarGenerosDeUmUsuario(String usuario, Context context, List<Long> generosInteresse, RecyclerView rvObras, AdapterObraFeed adapterObra, List<Obra> listaObras, TextView erro, ProgressBar progressBar) {
        ApiService apiService = new ApiService(context);
        UsuarioGeneroInterface usuarioGeneroInterface = apiService.getUsuarioGeneroInterface();
        Call<List<UsuarioGenero>> call = usuarioGeneroInterface.buscarGenerosPorUsuario(Long.parseLong(usuario));
        progressBar.setVisibility(View.VISIBLE);

        call.enqueue(new Callback<List<UsuarioGenero>>() {
            @Override
            public void onResponse(Call<List<UsuarioGenero>> call, Response<List<UsuarioGenero>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    try {
                        List<UsuarioGenero> usuariosGeneros = response.body();
                        generosInteresse.clear();
                        for(int i=0;i<usuariosGeneros.size();i++){
                            generosInteresse.add(usuariosGeneros.get(i).getIdGenero());
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.e("API_ERROR_GET_GENEROS_USUARIO", "Erro ao processar resposta: " + e.getMessage());
                    }
                } else {
                    Log.e("API_ERROR_GET_GENEROS_USUARIO", "Erro na resposta da API: " + response.code()+" "+response.message());
                }
                if(generosInteresse.size()>0) {
                    obraService.buscarObrasPorVariosGeneros(generosInteresse, erro, context, rvObras, listaObras, adapterObra, progressBar);
                }else{
                    obraService.buscarTodasobras(erro, context, rvObras, listaObras, adapterObra, progressBar);
                }
            }

            @Override
            public void onFailure(Call<List<UsuarioGenero>> call, Throwable throwable) {
                Log.e("API_ERROR_GET_GENEROS_USUARIO", "Erro ao fazer a requisição: " + throwable.getMessage());
                aux.abrirDialogErro(context, "Erro inesperado", "Erro ao obter dados\nMensagem: " + throwable.getMessage());
            }
        });
    }


}
