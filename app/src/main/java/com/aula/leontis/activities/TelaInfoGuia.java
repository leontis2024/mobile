package com.aula.leontis.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.aula.leontis.R;
import com.aula.leontis.adapters.AdapterObraGuia;
import com.aula.leontis.interfaces.usuario.UsuarioInterface;
import com.aula.leontis.models.guia.ObraGuia;
import com.aula.leontis.services.ApiService;
import com.aula.leontis.services.ObraGuiaService;
import com.aula.leontis.utilities.MetodosAux;
import com.google.firebase.auth.FirebaseAuth;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TelaInfoGuia extends AppCompatActivity {
    ImageButton btnVoltar;
    TextView tituloGuia;
    String id,idUsuario;
    MetodosAux aux = new MetodosAux();
    List<ObraGuia> listaObraGuias = new ArrayList<>();
    AdapterObraGuia adapterObraGuia = new AdapterObraGuia(listaObraGuias);
    RecyclerView rvObrasGuias;
    ObraGuiaService obraGuiaService = new ObraGuiaService();
    ProgressBar progressBar;
    @Override
    protected void onResume() {
        super.onResume();
        obraGuiaService.selecionarObraGuiaPorIdGuia(id,tituloGuia,TelaInfoGuia.this,rvObrasGuias,listaObraGuias,adapterObraGuia,progressBar);    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tela_info_guia);
        tituloGuia = findViewById(R.id.nomeGuia);
        rvObrasGuias = findViewById(R.id.mapaGuia);
        progressBar = findViewById(R.id.progressBar7);
        rvObrasGuias.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        Bundle bundle = getIntent().getExtras();
        if(bundle!=null){
            String titulo = bundle.getString("titulo");
            tituloGuia.setText(titulo);
            id = bundle.getString("id");
            progressBar.setVisibility(View.VISIBLE);
            obraGuiaService.selecionarObraGuiaPorIdGuia(id,tituloGuia,TelaInfoGuia.this,rvObrasGuias,listaObraGuias,adapterObraGuia,progressBar);
        }
        FirebaseAuth auth = FirebaseAuth.getInstance();
        String email = auth.getCurrentUser().getEmail();
        selecionarIdUsuarioPorEmail(email);

        btnVoltar = findViewById(R.id.btnFiltrar);
        btnVoltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    public void selecionarIdUsuarioPorEmail(String email) {

        ApiService apiService = new ApiService(TelaInfoGuia.this);
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
                aux.abrirDialogErro(TelaInfoGuia.this, "Erro inesperado", "Erro ao obter id\nMensagem: " + throwable.getMessage());
            }
        });
    }
}