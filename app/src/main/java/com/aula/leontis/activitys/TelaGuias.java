package com.aula.leontis.activitys;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.aula.leontis.R;
import com.aula.leontis.adapters.AdapterGuia;
import com.aula.leontis.adapters.AdapterObra;
import com.aula.leontis.interfaces.usuario.UsuarioInterface;
import com.aula.leontis.models.guia.Guia;
import com.aula.leontis.models.obra.Obra;
import com.aula.leontis.services.ApiService;
import com.aula.leontis.services.GuiaService;
import com.aula.leontis.utilities.MetodosAux;
import com.google.firebase.auth.FirebaseAuth;

import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TelaGuias extends AppCompatActivity {
    String id,idUsuario;
    MetodosAux aux = new MetodosAux();
    ImageView fotoGuiaDestaque;
    ImageButton btnVoltar,btnBuscar,btnFecharPesquisa;
    ProgressBar progressBar;
    EditText campoPesquisa;
    List<Guia> listaGuias = new ArrayList<>();
    AdapterGuia adapterGuia = new AdapterGuia(listaGuias);
    RecyclerView rvGuias;
    GuiaService guiaService = new GuiaService();
    TextView erroGuias,tituloGuiaDestaque,idGuiadestaque;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tela_guias);
        fotoGuiaDestaque = findViewById(R.id.fotoGuiaDestaque);
        rvGuias = findViewById(R.id.outrosGuias);
        btnVoltar = findViewById(R.id.btnVoltar);
        btnBuscar = findViewById(R.id.btnBuscar);
        erroGuias = findViewById(R.id.erroGuias);
        tituloGuiaDestaque = findViewById(R.id.tituloGuiaDestaque);
        idGuiadestaque = findViewById(R.id.idGuiadestaque);
        btnFecharPesquisa = findViewById(R.id.btnFecharPesquisa);
        progressBar = findViewById(R.id.progressBar6);
        campoPesquisa = findViewById(R.id.campoPesquisa);
        btnVoltar.setOnClickListener(v -> {
            finish();
        });
        campoPesquisa.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    filtrar(s.toString());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        rvGuias.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        Intent info = getIntent();
        Bundle infoGuia = info.getExtras();
        if(infoGuia != null) {
            id = infoGuia.getString("id");
        }
        FirebaseAuth auth = FirebaseAuth.getInstance();
        String email = auth.getCurrentUser().getEmail();
   //     selecionarIdUsuarioPorEmail(email);
         progressBar.setVisibility(View.VISIBLE);
         guiaService.selecionarGuiaPorMuseu(id,erroGuias, TelaGuias.this,rvGuias,listaGuias,adapterGuia,fotoGuiaDestaque,tituloGuiaDestaque,idGuiadestaque,progressBar);
         fotoGuiaDestaque.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                Bundle bundle = new Bundle();
                Intent infoGuia = new Intent(TelaGuias.this, TelaInfoGuia.class);
                bundle.putString("id", idGuiadestaque.getText().toString());
                infoGuia.putExtras(bundle);
                startActivity(infoGuia);
             }
         });
        btnBuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                campoPesquisa.setVisibility(View.VISIBLE);
                btnBuscar.setVisibility(View.INVISIBLE);
                btnFecharPesquisa.setVisibility(View.VISIBLE);
                campoPesquisa.setText("");

            }
        });
        btnFecharPesquisa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                campoPesquisa.setVisibility(View.INVISIBLE);
                btnBuscar.setVisibility(View.VISIBLE);
                btnFecharPesquisa.setVisibility(View.INVISIBLE);
                guiaService.selecionarGuiaPorMuseu(id,erroGuias, TelaGuias.this,rvGuias,listaGuias,adapterGuia,fotoGuiaDestaque,tituloGuiaDestaque,idGuiadestaque,progressBar);
            }
        });
    }
    public void selecionarIdUsuarioPorEmail(String email) {

        ApiService apiService = new ApiService(TelaGuias.this);
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
                aux.abrirDialogErro(TelaGuias.this, "Erro inesperado", "Erro ao obter id\nMensagem: " + throwable.getMessage());
            }
        });
    }
    public void filtrar(String nome){
        progressBar.setVisibility(View.VISIBLE);
        guiaService.buscarGuiaPorNomePesquisa(nome,erroGuias, TelaGuias.this,rvGuias,listaGuias,adapterGuia,fotoGuiaDestaque,tituloGuiaDestaque,idGuiadestaque,progressBar);
    }
}