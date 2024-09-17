package com.aula.leontis.activitys;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.aula.leontis.R;

public class TelaSplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tela_splash_screen);

        Handler handler = new Handler();
        //Esperando 3 segundos para abrir a tela de login
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent telaLogin = new Intent(TelaSplashScreen.this, TelaLogin.class);
                startActivity(telaLogin);
                finish();
            }
        }, 3000);
    }
}