package com.aula.leontis.activitys;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Button;

import com.aula.leontis.R;
import com.aula.leontis.interfaces.usuario.UsuarioInterface;
import com.aula.leontis.services.UsuarioService;

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
    UsuarioService usuarioService = new UsuarioService();
    Button btnFinalizar;
    String  url,id;
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
                    feed.putExtra("id", id);
                    startActivity(feed);
                    finish();

                });
            }
        }, 2000);



    }




}