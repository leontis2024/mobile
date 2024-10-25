package com.aula.leontis.activitys;

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
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.aula.leontis.models.usuario.UsuarioMongo;
import com.aula.leontis.services.UsuarioGeneroService;
import com.aula.leontis.services.UsuarioService;
import com.aula.leontis.utilities.DataBaseFotos;
import com.aula.leontis.utilities.MetodosAux;
import com.aula.leontis.R;
import com.aula.leontis.models.usuario.Usuario;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

import retrofit2.Retrofit;

public class TelaCadastroFotoPerfil extends AppCompatActivity {
    UsuarioService usuarioService = new UsuarioService();
    UsuarioGeneroService usuarioGeneroService = new UsuarioGeneroService();
    private final String[] id = {""};
    ProgressBar carregando;
    MetodosAux aux = new MetodosAux();
    ImageButton btnAbrirGaleria;
    ImageView fotoPerfil;
    Retrofit retrofit;
    Button btnContinuar;
    TextView titulo, descricao;
    DataBaseFotos dataBase = new DataBaseFotos();
    Uri imagemUri;
    String nome, sobrenome, email, telefone, dtNasc, senha, urlFoto, apelido, biografia, sexo;
    long[] listaGenerosInteresse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tela_cadastro_foto_perfil);

        carregando = findViewById(R.id.carregar);
        btnAbrirGaleria = findViewById(R.id.btn_abrir_galeria);
        fotoPerfil = findViewById(R.id.imagem_perfil);
        btnContinuar = findViewById(R.id.btn_continuar_foto_perfil);
        titulo = findViewById(R.id.textView8);
        descricao = findViewById(R.id.textView9);

        Intent info = getIntent();
        Bundle informacoes = info.getExtras();
        if(informacoes != null){
            nome = informacoes.getString("nome");
            sobrenome = informacoes.getString("sobrenome");
            email = informacoes.getString("email");
            telefone = informacoes.getString("telefone");
            dtNasc = informacoes.getString("dtNasc");
            senha = informacoes.getString("senha");
            apelido = informacoes.getString("apelido");
            biografia = informacoes.getString("biografia");
            sexo = informacoes.getString("sexo");
            listaGenerosInteresse = informacoes.getLongArray("generosInteressados");
        }
        btnContinuar.setBackgroundResource(R.drawable.botao);
        btnContinuar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Usuario usuario =new Usuario(nome, sobrenome, email, telefone, dtNasc,biografia, sexo, apelido, senha,"https://firebasestorage.googleapis.com/v0/b/leontisfotos.appspot.com/o/usuarios%2FusuarioDefault.png?alt=media&token=04ef6067-fef3-4a33-9065-8788b3b44f96");
                usuarioService.inserirUsuario(usuario,TelaCadastroFotoPerfil.this,id);

                Handler esperarGenero = new Handler();
                esperarGenero.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        usuarioGeneroService.inserirUsuarioGenero(TelaCadastroFotoPerfil.this,id,listaGenerosInteresse);
                        UsuarioMongo usuarioMongo = new UsuarioMongo(id[0],nome,email);
                        usuarioService.inserirUsuarioMongo(usuarioMongo,TelaCadastroFotoPerfil.this);

                    }
                },4000);
                Bundle info = new Bundle();
                info.putString("id", id[0]);
                info.putString("email", email);
                info.putString("senha", senha);
                Intent telBemVindo = new Intent(TelaCadastroFotoPerfil.this, TelaBemVindo.class);
                telBemVindo.putExtras(info);
                startActivity(telBemVindo);
                cadastrarUsuarioFirebase(email, senha, nome);
                finish();
            }
        });


        //abrindo galeria
        btnAbrirGaleria.setOnClickListener(v -> {
            btnContinuar.setBackgroundResource(R.drawable.botao_inativo);
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

                                    //inserindo usuario via api
                                   Usuario usuario =new Usuario(nome, sobrenome, email, telefone, dtNasc,biografia, sexo, apelido, senha,null);
                                   usuarioService.inserirUsuario(usuario,TelaCadastroFotoPerfil.this,id);
                                   //falta cadastrar usuario genero
                                    Handler esperarGenero = new Handler();
                                    esperarGenero.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            usuarioGeneroService.inserirUsuarioGenero(TelaCadastroFotoPerfil.this,id,listaGenerosInteresse);

                                        }
                                    },4000);

                                   Toast.makeText(TelaCadastroFotoPerfil.this, "Aguarde enquanto cadastramos seu usuário...", Toast.LENGTH_SHORT).show();
                                   carregando.setVisibility(View.VISIBLE);

                                    Handler esperar = new Handler();
                                    esperar.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            UsuarioMongo usuarioMongo = new UsuarioMongo(id[0],nome,email);
                                            usuarioService.inserirUsuarioMongo(usuarioMongo,TelaCadastroFotoPerfil.this);
                                            if(!(id[0].equals(""))) {
                                                // upload do Bitmap para o Firebase Storage retornando a url dela
                                                dataBase.subirFoto(TelaCadastroFotoPerfil.this, bitmap, id[0],"usuarios","usuario").addOnSuccessListener(new OnSuccessListener<String>() {
                                                    @Override
                                                    public void onSuccess(String url) {
                                                        // Aqui você pode usar a URL da imagem
                                                        Log.d("URL", "URL da imagem: " + url);
                                                        urlFoto = url;
                                                    }
                                                }).addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        Log.e("URL", "Erro ao obter a URL da imagem", e);

                                                    }
                                                });
                                            }
                                        }
                                    }, 5000);
                                }
                            });

                    //Glide para carregar a imagem na tela
                    Glide.with(this)
                            .load(imagemUri)
                            .circleCrop()
                            .into(fotoPerfil);
                    titulo.setText("Tudo pronto!");
                    descricao.setText("Uh lá lá, bela foto");


                    Handler handler = new Handler();

                    //Esperando 10 segundos para abrir a tela de bem-vindo
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            carregando.setVisibility(View.GONE);
                            btnContinuar.setBackgroundResource(R.drawable.botao);

                            btnContinuar.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Bundle info = new Bundle();
                                    info.putString("id", id[0]);
                                    info.putString("urlFoto", urlFoto);
                                    info.putString("email", email);
                                    info.putString("senha", senha);
                                    Intent telBemVindo = new Intent(TelaCadastroFotoPerfil.this, TelaBemVindo.class);
                                    telBemVindo.putExtras(info);
                                    startActivity(telBemVindo);
                                    cadastrarUsuarioFirebase(email, senha, nome);
                                    finish();
                                }
                            });
                        }
                    }, 10000);

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
    public void cadastrarUsuarioFirebase(String email, String senha, String nome){
        FirebaseAuth auth = FirebaseAuth.getInstance();
        auth.createUserWithEmailAndPassword(email, senha).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()) {
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
                                Log.d("Cadastro","Sucesso");
                            }
                        }
                    });
                }
            }
        });
    }



}