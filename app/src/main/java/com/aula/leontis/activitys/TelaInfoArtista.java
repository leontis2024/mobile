package com.aula.leontis.activitys;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.aula.leontis.R;
import com.aula.leontis.adapters.AdapterObra;
import com.aula.leontis.models.obra.Obra;
import com.aula.leontis.services.ArtistaService;
import com.aula.leontis.services.ObraService;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

public class TelaInfoArtista extends AppCompatActivity {
    List<Obra> listaObras = new ArrayList<>();
    AdapterObra adapterObra = new AdapterObra(listaObras);
    RecyclerView rvObras;
    TextView erroArtista, nomeArtista, descArtista;
    ImageView fotoArtista;
    ImageButton btnVoltar;
    ObraService obraService = new ObraService();
    ArtistaService artistaService = new ArtistaService();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tela_info_artista);

        erroArtista = findViewById(R.id.erroArtistaInfo);
        nomeArtista = findViewById(R.id.nomeArtista);
        descArtista = findViewById(R.id.descArtista);
        fotoArtista = findViewById(R.id.fotoArtista);
        btnVoltar = findViewById(R.id.btnVoltar);

        btnVoltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        String id = "0";
        Intent info = getIntent();
        Bundle infoArtista = info.getExtras();
        if(infoArtista != null) {
            id = infoArtista.getString("id");
        }
        erroArtista.setTextColor(ContextCompat.getColor(this, R.color.azul_carregando));
        erroArtista.setText("Carregando...");
        erroArtista.setVisibility(View.VISIBLE);
        artistaService.buscarArtistaPorId(id,TelaInfoArtista.this,erroArtista,nomeArtista,descArtista,fotoArtista);
        obraService.buscarObrasPorArtista(id,erroArtista,TelaInfoArtista.this,rvObras,listaObras,adapterObra);
    }
}