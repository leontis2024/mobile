package com.aula.leontis.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Button;

import com.aula.leontis.Geral;
import com.aula.leontis.R;
import com.aula.leontis.services.UsuarioService;

import java.util.HashMap;
import java.util.Map;

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