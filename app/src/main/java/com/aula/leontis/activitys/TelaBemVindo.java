package com.aula.leontis.activitys;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.aula.leontis.R;
import com.aula.leontis.TokenManager;
import com.aula.leontis.interfaces.AuthInterface;
import com.aula.leontis.models.auth.AuthResponse;
import com.aula.leontis.models.auth.LoginRequest;
import com.aula.leontis.services.ApiService;
import com.aula.leontis.services.UsuarioService;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class TelaBemVindo extends AppCompatActivity {
    UsuarioService usuarioService = new UsuarioService();
    Button btnFinalizar;
    String  url,id,senha,email;
    long[] listaGenerosInteresse;
 //   TokenManager tokenManager = new TokenManager(TelaBemVindo.this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tela_bem_vindo);

        Intent info = getIntent();
        Bundle infoCadastro = info.getExtras();
        if(infoCadastro != null) {
            id=infoCadastro.getString("id");
            url=infoCadastro.getString("urlFoto");
            email=infoCadastro.getString("email");
            senha=infoCadastro.getString("senha");
        //    listaGenerosInteresse = infoCadastro.getLongArray("listaGenerosInteresse");
            if(url!=null) {
                Map<String, Object> updates = new HashMap<>();
                updates.put("urlImagem", url);
                usuarioService.atualizarUsuario(id, updates, null, this);
            }

        }
        btnFinalizar = findViewById(R.id.btn_finalizar_bem_vindo);
        btnFinalizar.setBackground(getResources().getDrawable(R.drawable.botao_inativo));

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                btnFinalizar.setBackground(getResources().getDrawable(R.drawable.botao));
                btnFinalizar.setOnClickListener(v -> {
                    pegarToken();
                    Intent feed = new Intent(TelaBemVindo.this, TelaPrincipal.class);
                    feed.putExtra("id", id);
                    startActivity(feed);
                    finish();

                });
            }
        }, 3000);



    }
    public void pegarToken() {
        // Instancia ApiService para lidar com a autenticação
        Retrofit authRetrofit = new Retrofit.Builder()
                .baseUrl("https://dev2-tfqz.onrender.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        AuthInterface authInterface = authRetrofit.create(AuthInterface.class);// Obtém a interface de autenticação

        // Cria o objeto LoginRequest com as credenciais do usuário
        LoginRequest loginRequest = new LoginRequest(email, senha);

        // Faz a requisição para a API de login
        authInterface.login(loginRequest).enqueue(new Callback<Map<String, String>>() {
            @Override
            public void onResponse(Call<Map<String, String>> call, Response<Map<String, String>> response) {
                // Verifica se a resposta foi bem-sucedida e contém o token
                if (response.isSuccessful() && response.body() != null) {
                    // Pega o token de acesso e o token de refresh da resposta
                    Map<String, String> tokens = response.body();

                    // Obtendo os tokens do Map
                    String accessToken = tokens.get("accessToken");
                    String refreshToken = tokens.get("refreshToken");

                    // Salva os tokens no TokenManager
                    TokenManager tokenManager = new TokenManager(TelaBemVindo.this);
                    tokenManager.saveAccessToken(accessToken);
                    tokenManager.saveRefreshToken(refreshToken);

                    Intent main = new Intent(TelaBemVindo.this, TelaPrincipal.class);
                    startActivity(main);
                    finish();
                } else {
                    // Tratar erro de login (credenciais incorretas, por exemplo)
                }
            }

            @Override
            public void onFailure(Call<Map<String, String>> call, Throwable t) {
                // Lida com erros de conexão ou outros tipos de falha
                Log.e("ERRO_LOGIN", "Erro: " + t.getMessage());
            }
        });
    }




}