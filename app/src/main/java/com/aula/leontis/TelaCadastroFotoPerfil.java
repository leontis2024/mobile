package com.aula.leontis;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;

public class TelaCadastroFotoPerfil extends AppCompatActivity {
    ImageButton btnAbrirGaleria;
    ImageView fotoPerfil;
    Button btnContinuar;
    TextView titulo, descricao;
    DataBaseFotos dataBaseFotos = new DataBaseFotos();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tela_cadastro_foto_perfil);

        btnAbrirGaleria = findViewById(R.id.btn_abrir_galeria);
        fotoPerfil = findViewById(R.id.imagem_perfil);
        btnContinuar = findViewById(R.id.btn_continuar_foto_perfil);
        titulo = findViewById(R.id.textView8);
        descricao = findViewById(R.id.textView9);

        //abrindo galeria
        btnAbrirGaleria.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            resultLauncherGaleria.launch(intent);
        });
    }
    //Activity result para abrir a galeria
    private ActivityResultLauncher<Intent> resultLauncherGaleria = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                Uri imagemUri = result.getData().getData();
                if(imagemUri != null){
                    btnAbrirGaleria.setVisibility(View.INVISIBLE);
                    fotoPerfil.setVisibility(View.VISIBLE);
                    //Glide para carregar a imagem no firebase
                    Glide.with(this)
                            .load(imagemUri)
                            .circleCrop()
                            .into(new SimpleTarget<Drawable>() {
                                @Override
                                public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                                    // acessar o Drawable e convertê-lo em Bitmap
                                    Bitmap bitmap = drawableToBitmap(resource);
                                    // upload do Bitmap para o Firebase Storage
                                    dataBaseFotos.subirFotoUsuario(TelaCadastroFotoPerfil.this, bitmap, "12345");
                                }
                            });

                    //Glide para carregar a imagem na tela
                    Glide.with(this)
                            .load(imagemUri)
                            .circleCrop()
                            .into(fotoPerfil);
                    titulo.setText("Tudo pronto!");
                    descricao.setText("Uh lá lá, bela foto");
                    Toast.makeText(this, "Aguarde enquanto fazemos upload da sua foto", Toast.LENGTH_SHORT).show();


                    Handler handler = new Handler();

                    //Esperando 6 segundos para abrir a tela de bem-vindo
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            btnContinuar.setBackgroundResource(R.drawable.botao);
                            btnContinuar.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Toast.makeText(TelaCadastroFotoPerfil.this, "Deu bom", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }, 6000);

                }
            });

    //método que converte uma imagem em Bitmap
    private Bitmap drawableToBitmap(Drawable drawable) {
        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable) drawable).getBitmap();
        }

        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return bitmap;
    }
}