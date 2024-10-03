package com.aula.leontis.activitys;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.aula.leontis.services.GeneroService;
import com.aula.leontis.utilities.MetodosAux;
import com.aula.leontis.R;
import com.aula.leontis.interfaces.genero.GeneroInterface;
import com.aula.leontis.models.genero.GeneroCompleto;
import com.bumptech.glide.Glide;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class TelaInfoGenero extends AppCompatActivity {
    TextView erroGenero, nomeGenero, descGenero;
    ImageView fotoGenero;
    ImageButton btnVoltar;
    GeneroService generoService = new GeneroService();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tela_info_genero);
        erroGenero = findViewById(R.id.erroGeneroInfo);
        nomeGenero = findViewById(R.id.nomeGenero);
        descGenero = findViewById(R.id.descGenero);
        fotoGenero = findViewById(R.id.fotoGenero);
        btnVoltar = findViewById(R.id.btnVoltar);
        btnVoltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        String id = "0";
        Intent info = getIntent();
        Bundle infoGenero = info.getExtras();
        if(infoGenero != null) {
            id = infoGenero.getString("id");
        }
        generoService.buscarGeneroPorId(id,TelaInfoGenero.this,erroGenero,nomeGenero,descGenero,fotoGenero);

    }

}