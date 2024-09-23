package com.aula.leontis.activitys;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
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

public class TelaBemVindo extends AppCompatActivity {
    UsuarioService usuarioService = new UsuarioService();
    Button btnFinalizar;
    String  url,id;
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
            listaGenerosInteresse = infoCadastro.getLongArray("listaGenerosInteresse");
            Map<String, Object> updates = new HashMap<>();
            updates.put("urlImagem", url);
            usuarioService.atualizarUsuario(id,updates,null,this);

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
                 //   pegarToken();
                    feed.putExtra("id", id);
                    startActivity(feed);
                    finish();

                });
            }
        }, 3000);



    }
//    public void pegarToken(){
//        ApiService apiService = new ApiService(TelaBemVindo.this);
//        AuthInterface authInterface = apiService.getAuthInterface(); // Você pode adicionar um método para obter o AuthInterface
//
//        LoginRequest loginRequest = new LoginRequest(tokenManager.getUsername(), tokenManager.getPassword()); // Substitua pelos dados do usuário
//        authInterface.login(loginRequest).enqueue(new Callback<AuthResponse>() {
//            @Override
//            public void onResponse(Call<AuthResponse> call, Response<AuthResponse> response) {
//                if (response.isSuccessful()) {
//                    String token = response.body().getToken();
//                    tokenManager.saveToken(token);
//                    // Token salvo com sucesso
//                } else {
//                    // Tratar erro
//                }
//            }
//
//            @Override
//            public void onFailure(Call<AuthResponse> call, Throwable t) {
//                // Lidar com erro de rede
//            }
//        });
//
//    }




}