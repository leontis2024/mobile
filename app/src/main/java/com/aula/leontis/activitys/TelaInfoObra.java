package com.aula.leontis.activitys;

import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;

import com.aula.leontis.R;
import com.aula.leontis.adapters.AdapterComentario;
import com.aula.leontis.interfaces.usuario.UsuarioInterface;
import com.aula.leontis.models.avaliacao.Avaliacao;
import com.aula.leontis.models.comentario.Comentario;
import com.aula.leontis.models.comentario.ComentarioResponse;
import com.aula.leontis.services.ApiService;
import com.aula.leontis.services.MongoService;
import com.aula.leontis.services.ObraService;
import com.aula.leontis.services.RedisService;
import com.aula.leontis.utilities.MetodosAux;
import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TelaInfoObra extends AppCompatActivity {
    ImageView fotoObra,fotoMuseu,fotoArtista;
    MetodosAux aux = new MetodosAux();
    List<ComentarioResponse> listaComentarios = new ArrayList<>();
    RedisService redisService = new RedisService();
    AdapterComentario adapterComentario = new AdapterComentario(listaComentarios);
    RecyclerView rvComentarios;
    ImageButton btnVoltar;
    FloatingActionButton btnComentar;
    String id = "0";
    TextView nomeObra,descObra,descMuseu,descArtista,erroObraInfo,urlText,avaliacaoObra;
    ObraService obraService = new ObraService();
    MongoService mongoService = new MongoService();
    String idUsuario;
    TextView idGenero, idArtista,idMuseu;
    Button btnAcessarArtista,btnAcessarGenero,btnAcessarMuseu;
    ProgressBar um,dois,tres,quatro,cinco;

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
        btnComentar = findViewById(R.id.btnComentar);
        rvComentarios = findViewById(R.id.comentarios);
        rvComentarios.setLayoutManager(new androidx.recyclerview.widget.LinearLayoutManager(this));
        avaliacaoObra = findViewById(R.id.avaliacaoObra);

        fotoArtista = findViewById(R.id.imgArtista);

        descArtista = findViewById(R.id.descArtista);

        um = findViewById(R.id.um);
        dois = findViewById(R.id.dois);
        tres = findViewById(R.id.tres);
        quatro = findViewById(R.id.quatro);
        cinco = findViewById(R.id.cinco);

        idGenero = findViewById(R.id.idGenero);
        idArtista = findViewById(R.id.idArtista);
        idMuseu = findViewById(R.id.idMuseu);

        btnAcessarArtista = findViewById(R.id.btnAcessarArtista);
        btnAcessarGenero = findViewById(R.id.btnAcessarGenero);
        btnAcessarMuseu = findViewById(R.id.btnAcessarMuseu);

        btnVoltar = findViewById(R.id.btnFiltrar);
        btnVoltar.setOnClickListener(v -> {
            finish();
        });
        FirebaseAuth auth = FirebaseAuth.getInstance();
        String email = auth.getCurrentUser().getEmail();
        selecionarIdUsuarioPorEmail(email);


        Intent info = getIntent();
        Bundle infoObra = info.getExtras();
        if(infoObra != null) {
            id = infoObra.getString("id");
        }
        btnAcessarArtista.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TelaInfoObra.this, TelaInfoArtista.class);
                intent.putExtra("id",idArtista.getText());
                startActivity(intent);
            }
        });
        btnAcessarGenero.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TelaInfoObra.this, TelaInfoGenero.class);
                intent.putExtra("id",idGenero.getText());
                startActivity(intent);
            }
        });
        btnAcessarMuseu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TelaInfoObra.this, TelaInfoMuseu.class);
                intent.putExtra("id",idMuseu.getText());
                startActivity(intent);
            }
        });
        obraService.buscarObraPorId(id,TelaInfoObra.this,erroObraInfo,nomeObra,descObra,fotoObra,descMuseu,fotoMuseu,descArtista,fotoArtista,urlText,idGenero,idArtista,idMuseu);
        redisService.incrementarVizualizacao(id);
        mongoService.selecionarMediaNotaPorIdObra(id, TelaInfoObra.this,avaliacaoObra,erroObraInfo);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mongoService.selecionarPorcentagemPorAvaliacao(id,TelaInfoObra.this,"1","2",um,erroObraInfo);
                mongoService.selecionarPorcentagemPorAvaliacao(id,TelaInfoObra.this,"2","3",dois,erroObraInfo);
                mongoService.selecionarPorcentagemPorAvaliacao(id,TelaInfoObra.this,"3","4",tres,erroObraInfo);
                mongoService.selecionarPorcentagemPorAvaliacao(id,TelaInfoObra.this,"4","5",quatro,erroObraInfo);
                mongoService.selecionarPorcentagemPorAvaliacao(id,TelaInfoObra.this,"5","6",cinco,erroObraInfo);
            }
        }, 1000);
        mongoService.buscarComentariosPorIdObra(erroObraInfo,id,TelaInfoObra.this,rvComentarios,listaComentarios,adapterComentario,um,dois,tres,quatro,cinco,avaliacaoObra);
        btnComentar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog dialog = new Dialog(TelaInfoObra.this);
                dialog.setContentView(R.layout.dialog_comentario);
                dialog.getWindow().setLayout(WRAP_CONTENT,WRAP_CONTENT);
                dialog.getWindow().setBackgroundDrawableResource(R.drawable.caixa_mensagem_fundo);
                dialog.setCancelable(false);
                dialog.setCanceledOnTouchOutside(true);

                EditText comentario = dialog.findViewById(R.id.comentario);
                TextView qntCaracter = dialog.findViewById(R.id.qntCaracter);

                comentario.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                        // Não precisa implementar
                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        int remainingChars = 100 - s.length();
                        qntCaracter.setText(String.valueOf(remainingChars));
                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        // Não precisa implementar
                    }
                });


                RatingBar avaliacao = dialog.findViewById(R.id.avaliacao);
                Button comentar = dialog.findViewById(R.id.btn_comentar);
                TextView erroComentario = dialog.findViewById(R.id.erroComentario);
                avaliacao.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
                    @Override
                    public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                        avaliacao.setRating(rating);
                    }
                });
                comentar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(avaliacao.getRating()==0&&!(comentario.getText().toString().equals("")||comentario.getText()==null)){
                            erroComentario.setText("Avalie a obra");
                            erroComentario.setVisibility(View.VISIBLE);

                        }else{
                            erroComentario.setVisibility(View.INVISIBLE);
                            if(!(comentario.getText().toString().equals(""))&&comentario.getText()!=null){
                                mongoService.inserirComentario(idUsuario,new Comentario(Long.parseLong(id),comentario.getText().toString(),aux.dataAtualFormatada()),TelaInfoObra.this);
                                redisService.incrementarComentarioObra(id);
                            }
                            Handler handler1 = new Handler();
                            handler1.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    if(avaliacao.getRating()>0){
                                        mongoService.inserirAvaliacao(idUsuario,new Avaliacao(Long.parseLong(id),avaliacao.getRating(),aux.dataAtualFormatada()),TelaInfoObra.this);
                                        redisService.incrementarAvaliacaoObra(id);
                                    }
                                }
                            }, 1000);


                            dialog.dismiss();
                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    mongoService.buscarComentariosPorIdObra(erroObraInfo,id,TelaInfoObra.this,rvComentarios,listaComentarios,adapterComentario,um,dois,tres,quatro,cinco,avaliacaoObra);
                                    mongoService.selecionarMediaNotaPorIdObra(id, TelaInfoObra.this,avaliacaoObra,erroComentario);
                                    Handler handler = new Handler();
                                    handler.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            mongoService.selecionarPorcentagemPorAvaliacao(id,TelaInfoObra.this,"1","2",um,erroComentario);
                                            mongoService.selecionarPorcentagemPorAvaliacao(id,TelaInfoObra.this,"2","3",dois,erroComentario);
                                            mongoService.selecionarPorcentagemPorAvaliacao(id,TelaInfoObra.this,"3","4",tres,erroComentario);
                                            mongoService.selecionarPorcentagemPorAvaliacao(id,TelaInfoObra.this,"4","5",quatro,erroComentario);
                                            mongoService.selecionarPorcentagemPorAvaliacao(id,TelaInfoObra.this,"5","6",cinco,erroComentario);
                                        }
                                    }, 1000);

                                }
                            }, 1000);

                        }
                    }
                });

                dialog.show();
            }
        });


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
    public void selecionarIdUsuarioPorEmail(String email) {

        ApiService apiService = new ApiService(TelaInfoObra.this);
        UsuarioInterface usuarioInterface = apiService.getUsuarioInterface();
        Call<ResponseBody> call = usuarioInterface.selecionarUsuarioPorEmail(email);

        //executar chamada
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful() && response.body() != null) {
                    try {
                        // Converte o corpo da resposta para string
                        String jsonResponse = response.body().string();

                        // Cria um JSONObject a partir da string
                        JSONObject jsonObject = new JSONObject(jsonResponse);

                        String idApi = jsonObject.getString("id");
                        idUsuario = idApi;
                        // Faça algo com os valores obtidos
                        Log.d("API_RESPONSE_GET_EMAIL", "Campo obtido: id: " + idApi);

                    } catch (Exception e) {
                        Log.e("API_RESPONSE_GET_EMAIL", "Erro ao processar resposta: " + e.getMessage());

                    }
                } else {
                    Log.e("API_ERROR_GET_EMAIL", "Erro na resposta da API: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable throwable) {
                Log.e("API_ERROR_GET_EMAIL", "Erro ao fazer a requisição: " + throwable.getMessage());
                aux.abrirDialogErro(TelaInfoObra.this, "Erro inesperado", "Erro ao obter id\nMensagem: " + throwable.getMessage());
            }
        });
    }
}