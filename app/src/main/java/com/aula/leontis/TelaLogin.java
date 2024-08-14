package com.aula.leontis;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

public class TelaLogin extends AppCompatActivity {
    Button teste;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tela_login);

        teste = findViewById(R.id.btn_entrar);
        teste.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                teste.setBackground(ContextCompat.getDrawable(TelaLogin.this, R.drawable.botao_clique));
            }
        });
    }
}