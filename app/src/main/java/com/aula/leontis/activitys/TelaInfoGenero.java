package com.aula.leontis.activitys;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.aula.leontis.adapters.AdapterObra;
import com.aula.leontis.interfaces.usuario.UsuarioInterface;
import com.aula.leontis.models.obra.Obra;
import com.aula.leontis.services.ApiService;
import com.aula.leontis.services.GeneroService;
import com.aula.leontis.services.ObraService;
import com.aula.leontis.services.UsuarioGeneroService;
import com.aula.leontis.utilities.MetodosAux;
import com.aula.leontis.R;
import com.aula.leontis.interfaces.genero.GeneroInterface;
import com.aula.leontis.models.genero.GeneroCompleto;
import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class TelaInfoGenero extends AppCompatActivity {
    TextView erroGenero, nomeGenero, descGenero,interesse;
    ImageView fotoGenero;
    List<Obra> listaObras = new ArrayList<>();
    AdapterObra adapterObra = new AdapterObra(listaObras);
    RecyclerView rvObras;
    ObraService obraService = new ObraService();
    ImageButton btnVoltar,btnInteresse;
    GeneroService generoService = new GeneroService();
    MetodosAux aux = new MetodosAux();
    String idUsuario;
    UsuarioGeneroService usuarioGeneroService = new UsuarioGeneroService();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tela_info_genero);
        erroGenero = findViewById(R.id.erroGeneroInfo);
        nomeGenero = findViewById(R.id.nomeGenero);
        descGenero = findViewById(R.id.descGenero);
        fotoGenero = findViewById(R.id.fotoGenero);
        btnVoltar = findViewById(R.id.btnVoltar);
        interesse = findViewById(R.id.interesse);
        btnInteresse = findViewById(R.id.btnInteresse);
        rvObras = findViewById(R.id.obrasRelacionadas); // Inicialização do RecyclerView

        // Configurando o RecyclerView
        rvObras.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        btnVoltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        String id = "0";
        Intent info = getIntent();
        Bundle infoGenero = info.getExtras();
        if(infoGenero != null) {
            id = infoGenero.getString("id");
        }
        erroGenero.setTextColor(ContextCompat.getColor(this, R.color.azul_carregando));
        erroGenero.setText("Carregando...");
        erroGenero.setVisibility(View.VISIBLE);
        FirebaseAuth auth = FirebaseAuth.getInstance();
        String email = auth.getCurrentUser().getEmail();
        selecionarIdUsuarioPorEmail(email,id);
        generoService.buscarGeneroPorId(id,TelaInfoGenero.this,erroGenero,nomeGenero,descGenero,fotoGenero);
        obraService.buscarObrasPorGenero(id,erroGenero,TelaInfoGenero.this,rvObras,listaObras,adapterObra);


        String[] finalId = new String[]{id};
        btnInteresse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (btnInteresse.getContentDescription() != null &&
                        btnInteresse.getContentDescription().equals("selecionado")) {
                    usuarioGeneroService.deletarUsuarioGenero(TelaInfoGenero.this, finalId, idUsuario);
                    btnInteresse.setImageResource(R.drawable.btn_interesse);

                } else {
                    long[] idGenero = new long[1];
                    idGenero[0] = Long.parseLong(finalId[0]);
                    String[] idUser = new String[1];
                    idUser[0] = idUsuario;
                    usuarioGeneroService.inserirUsuarioGenero(TelaInfoGenero.this, idUser, idGenero);
                    btnInteresse.setImageResource(R.drawable.btn_interesse_selecionado);
                }
            }
        });

    }
    public void selecionarIdUsuarioPorEmail(String email, String id) {

        ApiService apiService = new ApiService(TelaInfoGenero.this);
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
                        idUsuario=idApi;
                        usuarioGeneroService.buscarSeExiste(idUsuario, id,TelaInfoGenero.this,btnInteresse);
                        // Faça algo com os valores obtidos
                        Log.d("API_RESPONSE_GET_EMAIL", "Campo obtido: id: "+idApi);

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
                aux.abrirDialogErro(TelaInfoGenero.this,"Erro inesperado","Erro ao obter id\nMensagem: "+throwable.getMessage());
            }
        });
    }


}