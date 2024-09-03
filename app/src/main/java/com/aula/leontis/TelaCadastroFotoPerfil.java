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
import android.os.Parcelable;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.aula.leontis.interfaces.UsuarioInterface;
import com.aula.leontis.model.Genero;
import com.aula.leontis.model.Usuario;
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

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class TelaCadastroFotoPerfil extends AppCompatActivity {
    private final String[] id = {""};
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
            listaGenerosInteresse = informacoes.getLongArray("listaGenerosInteresse");
        }



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
                                   Usuario usuario =new Usuario(nome, sobrenome, email, telefone, dtNasc, senha, apelido, biografia, sexo,null);
                                   cadastrarUsuarioApi(usuario);

                                    // upload do Bitmap para o Firebase Storage retornando a url dela
                                    dataBase.subirFotoUsuario(TelaCadastroFotoPerfil.this, bitmap, id[0]).addOnSuccessListener(new OnSuccessListener<String>() {
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
                                    Bundle info = new Bundle();
//                                    info.putString("nome", nome);
//                                    info.putString("sobrenome", sobrenome);
//                                    info.putString("email", email);
//                                    info.putString("telefone", telefone);
//                                    info.putString("dtNasc", dtNasc);
//                                    info.putString("senha", senha);
//                                    info.putString("apelido", apelido);
//                                    info.putString("biografia", biografia);
//                                    info.putString("sexo", sexo);
                                    info.putString("id", id[0]);
                                    info.putLongArray("listaGenerosInteresse", listaGenerosInteresse);
                                    info.putString("urlFoto", urlFoto);
                                    Intent telBemVindo = new Intent(TelaCadastroFotoPerfil.this, TelaBemVindo.class);
                                    telBemVindo.putExtras(info);
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
    public String cadastrarUsuarioApi(Usuario usuario){
        String urlAPI = "https://dev2-tfqz.onrender.com/";

        // Configurar acesso à API
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(urlAPI)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        UsuarioInterface usuarioInterface = retrofit.create(UsuarioInterface.class);

        Call<ResponseBody> call = usuarioInterface.salvarUsuario(usuario);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                String[] idUsuario = {""};
                if (response.isSuccessful()) {
                    try {
                        idUsuario[0] = response.body().string();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    Log.d("API_RESPONSE_POST", "ID do usuário inserido via API: " + idUsuario[0]);
                } else {
                    try {
                        // Obter e exibir o corpo da resposta de erro
                        String errorBody = response.errorBody().string();
                        Log.e("API_ERROR_POST", "Erro ao inserir o usuário: " + response.code() + " - " + errorBody + " - " + response.message());
                        aux.abrirDialogErro(TelaCadastroFotoPerfil.this, "Erro ao cadastrar usuário","Não foi possível realizar seu cadastro. Erro: " + errorBody);
                    } catch (IOException e) {
                        e.printStackTrace();
                        Log.e("API_ERROR_POST", "Erro ao processar o corpo da resposta de erro.");
                    }
                }
                id[0] = idUsuario[0];
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable throwable) {
                Log.e("API_ERROR_POST", "Erro ao inserir o usuário: " + throwable.getMessage());
                aux.abrirDialogErro(TelaCadastroFotoPerfil.this, "Erro ao cadastrar usuário","Não foi possível realizar seu cadastro. Erro: " + throwable.getMessage());
            }

        });
        return id[0];
    }



}