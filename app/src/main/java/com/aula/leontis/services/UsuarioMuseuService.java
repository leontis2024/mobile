package com.aula.leontis.services;

import android.content.Context;
import android.util.Log;
import android.widget.ImageButton;

import com.aula.leontis.R;
import com.aula.leontis.interfaces.usuario.UsuarioGeneroInterface;
import com.aula.leontis.interfaces.usuario.UsuarioMuseuInterface;
import com.aula.leontis.models.usuario.UsuarioGenero;
import com.aula.leontis.models.usuario.UsuarioMuseu;
import com.aula.leontis.utilities.MetodosAux;

import java.io.IOException;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class UsuarioMuseuService {
    MetodosAux aux = new MetodosAux();
    public void buscarSeExiste(String usuario, String museu, Context context, ImageButton seguir) {

        ApiService apiService = new ApiService(context);
        UsuarioMuseuInterface usuarioMuseuInterface = apiService.getUsuarioMuseuInterface();

        Call<UsuarioMuseu> call = usuarioMuseuInterface.buscarSeExiste(Long.parseLong(usuario),Long.parseLong(museu));

        //executar chamada
        call.enqueue(new Callback<UsuarioMuseu>() {
            @Override
            public void onResponse(Call<UsuarioMuseu> call, Response<UsuarioMuseu> response) {
                if (response.isSuccessful() && response.body() != null) {
                    try {
                        UsuarioMuseu usuarioMuseu = response.body();
                        if(usuarioMuseu != null){
                            seguir.setImageResource(R.drawable.btn_seguir_selecionado);
                            seguir.setContentDescription("selecionado");
                        }else{
                            seguir.setImageResource(R.drawable.btn_seguir);
                            seguir.setContentDescription("vazio");
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.e("API_ERROR_GETID", "Erro ao processar resposta: " + e.getMessage());
                    }
                } else {
                    Log.e("API_ERROR_GETID", "Erro na resposta da API: " + response.code()+" "+response.message());
                }
            }

            @Override
            public void onFailure(Call<UsuarioMuseu> call, Throwable throwable) {
                Log.e("API_ERROR", "Erro ao fazer a requisição: " + throwable.getMessage());
                aux.abrirDialogErro(context, "Erro inesperado", "Erro ao obter dados\nMensagem: " + throwable.getMessage());
            }
        });
    }

    public void inserirUsuarioMuseu(Context context, String[] id, String idMuseu) {

        ApiService apiService = new ApiService(context);
        UsuarioMuseuInterface usuarioMuseuInterface = apiService.getUsuarioMuseuInterface();

            Call<ResponseBody> call = usuarioMuseuInterface.inserirUsuarioMiseu(Long.parseLong(id[0]), Long.parseLong(idMuseu));
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        try {
                            Log.d("API_RESPONSE_POST", "Conexão usuário e museu criada: " + response.body().string());
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    } else {
                        try {
                            // Obter e exibir o corpo da resposta de erro
                            String errorBody = response.errorBody().string();
                            Log.e("API_ERROR_POST", "Erro ao fazer conexão usuario e museu: " + response.code() + " - " + errorBody + " - " + response.message());
                        } catch (IOException e) {
                            e.printStackTrace();
                            Log.e("API_ERROR_POST", "Erro ao processar o corpo da resposta de erro.");
                        }
                    }
                }



                @Override
                public void onFailure(Call<ResponseBody> call, Throwable throwable) {
                    Log.e("API_ERROR_POST", "Erro ao fazer conexão museu e genero: " + throwable.getMessage());
                    aux.abrirDialogErro(context, "Erro inesperado", "Não foi possível realizar seu cadastro. Erro: " + throwable.getMessage());
                }
            });



    }

    public void deletarUsuarioMuseu(Context context, String[] id, String idMuseu) {

        ApiService apiService = new ApiService(context);
        UsuarioMuseuInterface usuarioMuseuInterface = apiService.getUsuarioMuseuInterface();

        Call<ResponseBody> call = usuarioMuseuInterface.deletarUsuarioMuseu(Long.parseLong(id[0]), Long.parseLong(idMuseu));

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful() && response.body() != null) {
                    try {
                        Log.d("API_RESPONSE_DELETE", "O usuário de id: "+id[0]+" parou de seguir o museu de id: "+idMuseu + response.body().string());
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                } else {
                    try {
                        // Obter e exibir o corpo da resposta de erro
                        String errorBody = response.errorBody().string();
                        Log.e("API_ERROR_DELETE", "Erro ao desfazer conexão usuario e museu: " + response.code() + " - " + errorBody + " - " + response.message());
                    } catch (IOException e) {
                        e.printStackTrace();
                        Log.e("API_ERROR_DELETE", "Erro ao processar o corpo da resposta de erro.");
                    }
                }
            }



            @Override
            public void onFailure(Call<ResponseBody> call, Throwable throwable) {
                Log.e("API_ERROR_POST", "Erro ao desfazer conexão usuario e genero: " + throwable.getMessage());
                aux.abrirDialogErro(context, "Erro inesperado", "Não foi possível realizar seu cadastro. Erro: " + throwable.getMessage());
            }
        });



    }
    public void buscarMuseusDeUmUsuario(String usuario, Context context, List<Long> museusSeguidos) {

        ApiService apiService = new ApiService(context);
        UsuarioMuseuInterface usuarioMuseuInterface = apiService.getUsuarioMuseuInterface();
        Call<List<UsuarioMuseu>> call = usuarioMuseuInterface.buscarMuseusPorUsuario(Long.parseLong(usuario));

        //executar chamada
        call.enqueue(new Callback<List<UsuarioMuseu>>() {
            @Override
            public void onResponse(Call<List<UsuarioMuseu>> call, Response<List<UsuarioMuseu>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    try {
                        List<UsuarioMuseu> usuariosMuseus = response.body();
                        for(int i=0;i<usuariosMuseus.size();i++){
                            museusSeguidos.add(usuariosMuseus.get(i).getIdMuseu());
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.e("API_ERROR_GETID", "Erro ao processar resposta: " + e.getMessage());
                    }
                } else {
                    Log.e("API_ERROR_GETID", "Erro na resposta da API: " + response.code()+" "+response.message());
                }
            }

            @Override
            public void onFailure(Call<List<UsuarioMuseu>> call, Throwable throwable) {
                Log.e("API_ERROR", "Erro ao fazer a requisição: " + throwable.getMessage());
                aux.abrirDialogErro(context, "Erro inesperado", "Erro ao obter dados\nMensagem: " + throwable.getMessage());
            }
        });
    }


}
