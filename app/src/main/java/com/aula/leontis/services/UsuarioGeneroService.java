package com.aula.leontis.services;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.aula.leontis.R;
import com.aula.leontis.adapters.AdapterGenero;
import com.aula.leontis.interfaces.genero.GeneroInterface;
import com.aula.leontis.interfaces.usuario.UsuarioGeneroInterface;
import com.aula.leontis.interfaces.usuario.UsuarioInterface;
import com.aula.leontis.models.genero.Genero;
import com.aula.leontis.models.usuario.UsuarioGenero;
import com.aula.leontis.utilities.MetodosAux;

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
    public void inserirUsuarioGenero(Context context, String[] id, long[] listaGenerosInteresse) {

        String urlAPI = "https://dev2-tfqz.onrender.com/";

        // Configurar acesso à API
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
                            Log.d("API_RESPONSE_POST", "Conexão usuário e genero criada: " + response.body().string());
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    } else {
                        try {
                            // Obter e exibir o corpo da resposta de erro
                            String errorBody = response.errorBody().string();
                            Log.e("API_ERROR_POST", "Erro ao fazer conexão usuario e genero: " + response.code() + " - " + errorBody + " - " + response.message());
                        } catch (IOException e) {
                            e.printStackTrace();
                            Log.e("API_ERROR_POST", "Erro ao processar o corpo da resposta de erro.");
                        }
                    }

                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable throwable) {
                    Log.e("API_ERROR_POST", "Erro ao fazer conexão usuario e genero: " + throwable.getMessage());
                    aux.abrirDialogErro(context, "Erro inesperado", "Não foi possível realizar seu cadastro. Erro: " + throwable.getMessage());
                }
            });
        }


    }



}
