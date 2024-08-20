package com.aula.leontis;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

public class TelaCadastroFotoPerfil extends AppCompatActivity {
    ImageButton btnAbrirGaleria;
    ImageView fotoPerfil;
    Button btnContinuar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tela_cadastro_foto_perfil);

        btnAbrirGaleria = findViewById(R.id.btn_abrir_galeria);
        fotoPerfil = findViewById(R.id.imagem_perfil);
        btnContinuar = findViewById(R.id.btn_continuar_foto_perfil);

        btnAbrirGaleria.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            resultLauncherGaleria.launch(intent);
        });
    }
    private ActivityResultLauncher<Intent> resultLauncherGaleria = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                Uri imagemUri = result.getData().getData();
                if(imagemUri != null){
                    btnAbrirGaleria.setVisibility(View.INVISIBLE);
                    fotoPerfil.setVisibility(View.VISIBLE);
                    Glide.with(this)
                            .load(imagemUri)
                            .circleCrop()
                            .into(fotoPerfil);
                    btnContinuar.setBackgroundResource(R.drawable.botao);
                    btnContinuar.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Toast.makeText(TelaCadastroFotoPerfil.this, "Deu bom", Toast.LENGTH_SHORT).show();
                        }
                    });


                }
            });
}