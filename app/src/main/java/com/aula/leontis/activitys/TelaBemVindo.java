package com.aula.leontis.activitys;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.aula.leontis.Geral;
import com.aula.leontis.R;
import com.aula.leontis.TokenManager;
import com.aula.leontis.interfaces.AuthInterface;
import com.aula.leontis.models.auth.AuthResponse;
import com.aula.leontis.models.auth.LoginRequest;
import com.aula.leontis.services.ApiService;
import com.aula.leontis.services.UsuarioService;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;

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
                    Bundle info = new Bundle();
                    Intent feed = new Intent(TelaBemVindo.this, TelaLogin.class);
                    Geral.getInstance().setPrimeiroAcesso(true);
                    info.putBoolean("cadastro",true);
                    info.putString("email",email);
                    feed.putExtras(info);
                    startActivity(feed);
                    finish();

                });
            }
        }, 3000);



    }

}