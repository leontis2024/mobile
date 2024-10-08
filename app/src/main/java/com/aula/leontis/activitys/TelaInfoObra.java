package com.aula.leontis.activitys;

import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.aula.leontis.R;
import com.aula.leontis.services.ObraService;
import com.bumptech.glide.Glide;

public class TelaInfoObra extends AppCompatActivity {
    ImageView fotoObra,fotoMuseu;
    ImageButton btnVoltar;
    TextView nomeObra,descObra,descMuseu,erroObraInfo,urlText;
    ObraService obraService = new ObraService();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tela_info_obra);

        fotoObra = findViewById(R.id.fotoObra);
        fotoMuseu = findViewById(R.id.imgMuseu);
        nomeObra = findViewById(R.id.nomeObra);
        descObra = findViewById(R.id.descObra);
        descMuseu = findViewById(R.id.descMuseu);
        erroObraInfo = findViewById(R.id.erroObraInfo);
        urlText = findViewById(R.id.urlText);

        btnVoltar = findViewById(R.id.btnVoltar);
        btnVoltar.setOnClickListener(v -> {
            finish();
        });



        String id = "0";
        Intent info = getIntent();
        Bundle infoObra = info.getExtras();
        if(infoObra != null) {
            id = infoObra.getString("id");
        }

        obraService.buscarObraPorId(id,TelaInfoObra.this,erroObraInfo,nomeObra,descObra,fotoObra,descMuseu,fotoMuseu,urlText);

        fotoObra.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog dialog = new Dialog(TelaInfoObra.this);
                dialog.setContentView(R.layout.dialog_foto);
                dialog.getWindow().setLayout(WRAP_CONTENT,WRAP_CONTENT);
                dialog.getWindow().setBackgroundDrawableResource(R.drawable.caixa_mensagem_fundo);
                dialog.setCancelable(false);
                dialog.setCanceledOnTouchOutside(true);

                ImageView foto = dialog.findViewById(R.id.imageView);
                Glide.with(TelaInfoObra.this).load(urlText.getText()).into(foto);

                dialog.show();
            }
        });
    }
}