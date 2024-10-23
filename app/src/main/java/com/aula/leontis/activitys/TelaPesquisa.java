package com.aula.leontis.activitys;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageButton;

import com.aula.leontis.R;

public class TelaPesquisa extends AppCompatActivity {
    ImageButton btnVoltar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tela_pesquisa);

        btnVoltar = findViewById(R.id.btnFiltrar);
        btnVoltar.setOnClickListener(v -> finish());
    }
}