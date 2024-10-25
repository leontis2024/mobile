package com.aula.leontis.activitys;

import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
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
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class TelaInfoArtista extends AppCompatActivity {
    TextView urlText;
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
        urlText = findViewById(R.id.urlText);
        btnVoltar = findViewById(R.id.btnFiltrar);
        rvObras = findViewById(R.id.obrasRelacionadasArtista);
        rvObras.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        fotoArtista.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog dialog = new Dialog(TelaInfoArtista.this);
                dialog.setContentView(R.layout.dialog_foto);
                dialog.getWindow().setLayout(WRAP_CONTENT,WRAP_CONTENT);
                dialog.getWindow().setBackgroundDrawableResource(R.drawable.caixa_mensagem_fundo);
                dialog.setCancelable(false);
                dialog.setCanceledOnTouchOutside(true);

                ImageView foto = dialog.findViewById(R.id.imageView);
                Glide.with(TelaInfoArtista.this).load(urlText.getText()).into(foto);

                dialog.show();
            }
        });

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
        artistaService.buscarArtistaPorId(id,TelaInfoArtista.this,erroArtista,nomeArtista,descArtista,fotoArtista,urlText);
        obraService.buscarObrasPorArtista(id,erroArtista,TelaInfoArtista.this,rvObras,listaObras,adapterObra);
    }
}