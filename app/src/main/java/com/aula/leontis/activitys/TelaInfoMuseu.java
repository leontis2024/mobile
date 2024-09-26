package com.aula.leontis.activitys;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.aula.leontis.R;
import com.aula.leontis.adapters.AdapterObra;
import com.aula.leontis.interfaces.obra.ObraInterface;
import com.aula.leontis.models.obra.Obra;
import com.aula.leontis.services.MuseuService;
import com.aula.leontis.services.ObraService;

import java.util.ArrayList;
import java.util.List;

public class TelaInfoMuseu extends AppCompatActivity {

    TextView erroMuseu, nomeMuseu, descMuseu;
    ImageView fotoMuseu;
    ImageButton btnVoltar;
    MuseuService museuService = new MuseuService();
    List<Obra> listaObras = new ArrayList<>();
    AdapterObra adapterObra = new AdapterObra(listaObras);
    RecyclerView rvObras;
    ObraService obraService = new ObraService();
    String id;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tela_info_museu);
            erroMuseu = findViewById(R.id.erroMuseuInfo);
            nomeMuseu = findViewById(R.id.nomeMuseu);
            descMuseu = findViewById(R.id.descMuseu);
            fotoMuseu = findViewById(R.id.fotoMuseu);
            btnVoltar = findViewById(R.id.btnVoltar);
            rvObras = findViewById(R.id.obrasRelacionadasMuseu);

            rvObras.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

            btnVoltar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
            Intent info = getIntent();
            Bundle infoMuseu = info.getExtras();
            if(infoMuseu != null) {
                id = infoMuseu.getString("id");
            }
            museuService.buscarMuseuPorId(id,TelaInfoMuseu.this,erroMuseu,nomeMuseu,descMuseu,fotoMuseu);
          //  obraService.buscarObrasPorMuseu(id,erroMuseu,TelaInfoMuseu.this,rvObras,listaObras,adapterObra);

        }


}