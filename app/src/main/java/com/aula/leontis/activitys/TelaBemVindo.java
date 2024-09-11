package com.aula.leontis.activitys;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Button;

import com.aula.leontis.R;
import com.aula.leontis.interfaces.usuario.UsuarioInterface;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class TelaBemVindo extends AppCompatActivity {
    Button btnFinalizar;
    String  url,id, nome, sobrenome, email, telefone, dtNasc, senha, apelido, biografia, sexo;
    long[] listaGenerosInteresse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tela_bem_vindo);

        Intent info = getIntent();
        Bundle infoCadastro = info.getExtras();
        if(infoCadastro != null) {

            id=infoCadastro.getString("id");
            url=infoCadastro.getString("urlFoto");
            listaGenerosInteresse = infoCadastro.getLongArray("listaGenerosInteresse");
            Map<String, Object> updates = new HashMap<>();
            updates.put("urlImagem", url);
            alterarUsuarioApi(id,updates);

        }
        btnFinalizar = findViewById(R.id.btn_finalizar_bem_vindo);
        btnFinalizar.setBackground(getResources().getDrawable(R.drawable.botao_inativo));

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                btnFinalizar.setBackground(getResources().getDrawable(R.drawable.botao));
                btnFinalizar.setOnClickListener(v -> {
                    Intent feed = new Intent(TelaBemVindo.this, TelaPrincipal.class);
                    feed.putExtra("id", id);
                    startActivity(feed);
                    finish();

                });
            }
        }, 2000);



    }
    public void alterarUsuarioApi(String id,Map<String,Object> campo){
        String urlAPI = "https://dev2-tfqz.onrender.com/";

        // Configurar acesso à API
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(urlAPI)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        UsuarioInterface usuarioInterface = retrofit.create(UsuarioInterface.class);
        Call<ResponseBody> call = usuarioInterface.atualizarUsuario(id,campo);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                String[] resposta = {""};
                if (response.isSuccessful()) {
                    try {
                        resposta[0] = response.body().string();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    Log.d("API_RESPONSE_PATCH", "Usuário atualizado via API: " + resposta[0]);
                } else {
                    try {
                        // Obter e exibir o corpo da resposta de erro
                        String errorBody = response.errorBody().string();
                        Log.e("API_ERROR_PATCH", "Erro ao atualizar o usuário: " + response.code() + " - " + errorBody + " - " + response.message());
                    } catch (IOException e) {
                        e.printStackTrace();
                        Log.e("API_ERROR_PATCH", "Erro ao processar o corpo da resposta de erro.");
                    }
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable throwable) {
                Log.e("API_ERROR_PATCH", "Erro ao atualizar o usuário: " + throwable.getMessage());
            }

        });
    }



}