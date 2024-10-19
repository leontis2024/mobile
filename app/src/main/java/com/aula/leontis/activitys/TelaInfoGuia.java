package com.aula.leontis.activitys;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.aula.leontis.R;

public class TelaInfoGuia extends AppCompatActivity {
    ImageButton btnVoltar;
    TextView tituloGuia;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tela_info_guia);
        tituloGuia = findViewById(R.id.nomeGuia);

        Bundle bundle = getIntent().getExtras();
        if(bundle!=null){
            String id = bundle.getString("id");
            tituloGuia.setText(id);
        }


        btnVoltar = findViewById(R.id.btnVoltar);
        btnVoltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}