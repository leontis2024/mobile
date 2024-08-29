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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class TelaCadastroFotoPerfil extends AppCompatActivity {
    ImageButton btnAbrirGaleria;
    ImageView fotoPerfil;
    Button btnContinuar;
    TextView titulo, descricao;
    DataBaseFotos dataBaseFotos = new DataBaseFotos();
    Uri imagemUri;
    String nome, sobrenome, email, telefone, dtNasc, senha;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tela_cadastro_foto_perfil);

        btnAbrirGaleria = findViewById(R.id.btn_abrir_galeria);
        fotoPerfil = findViewById(R.id.imagem_perfil);
        btnContinuar = findViewById(R.id.btn_continuar_foto_perfil);
        titulo = findViewById(R.id.textView8);
        descricao = findViewById(R.id.textView9);

        Intent informacoes = getIntent();
        nome = informacoes.getStringExtra("nome");
        sobrenome = informacoes.getStringExtra("sobrenome");
        email = informacoes.getStringExtra("email");
        telefone = informacoes.getStringExtra("telefone");
        dtNasc = informacoes.getStringExtra("dtNasc");
        senha = informacoes.getStringExtra("senha");

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
                imagemUri = result.getData().getData();
                if(imagemUri != null){
                    btnAbrirGaleria.setVisibility(View.INVISIBLE);
                    fotoPerfil.setVisibility(View.VISIBLE);
                    //Glide para carregar a imagem no firebase
                    Glide.with(this)
                            .load(imagemUri)
                            .into(new SimpleTarget<Drawable>() {
                                @Override
                                public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                                    // acessar o Drawable e convertê-lo em Bitmap
                                    Bitmap bitmap = drawableToBitmap(resource);
                                    // upload do Bitmap para o Firebase Storage
                                    dataBaseFotos.subirFotoUsuario(TelaCadastroFotoPerfil.this, bitmap, "12347");
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
                                    Intent telBemVindo = new Intent(TelaCadastroFotoPerfil.this, TelaBemVindo.class);
                                    startActivity(telBemVindo);
                                    cadastrarUsuario(email, senha, nome);
                                    finish();
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
    public void cadastrarUsuario(String email, String senha, String nome){
        FirebaseAuth auth = FirebaseAuth.getInstance();
        auth.createUserWithEmailAndPassword(email, senha).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()) {
                    Toast.makeText(TelaCadastroFotoPerfil.this, "Cadastro efetuado com sucesso", Toast.LENGTH_SHORT).show();

                    //Atualizar o nome do usuario e foto
                    FirebaseUser user = auth.getCurrentUser();
                    UserProfileChangeRequest profileChangeRequest= new UserProfileChangeRequest.Builder()
                            .setDisplayName(nome)
                            .setPhotoUri(imagemUri)
                            .build();
                    user.updateProfile(profileChangeRequest).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(TelaCadastroFotoPerfil.this, "Não foi possível efetuar seu cadastro", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });
    }
}