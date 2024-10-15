package com.aula.leontis.activitys;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.aula.leontis.R;
import com.aula.leontis.adapters.AdapterObra;
import com.aula.leontis.interfaces.obra.ObraInterface;
import com.aula.leontis.interfaces.usuario.UsuarioInterface;
import com.aula.leontis.interfaces.usuario.UsuarioMuseuInterface;
import com.aula.leontis.models.obra.Obra;
import com.aula.leontis.services.ApiService;
import com.aula.leontis.services.MuseuService;
import com.aula.leontis.services.ObraService;
import com.aula.leontis.services.UsuarioMuseuService;
import com.aula.leontis.utilities.MetodosAux;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TelaInfoMuseu extends AppCompatActivity {

    TextView erroMuseu, nomeMuseu, descMuseu;
    ImageView fotoMuseu;
    FloatingActionButton btnGuia;
    ImageButton btnVoltar,btnSeguir;
    MuseuService museuService = new MuseuService();
    List<Obra> listaObras = new ArrayList<>();
    AdapterObra adapterObra = new AdapterObra(listaObras);
    RecyclerView rvObras;
    ObraService obraService = new ObraService();
    String id,idUsuario;
    MetodosAux aux = new MetodosAux();
    UsuarioMuseuService usuarioMuseuService = new UsuarioMuseuService();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tela_info_museu);
            erroMuseu = findViewById(R.id.erroMuseuInfo);
            nomeMuseu = findViewById(R.id.nomeMuseu);
            descMuseu = findViewById(R.id.descMuseu);
            fotoMuseu = findViewById(R.id.fotoMuseu);
            btnVoltar = findViewById(R.id.btnVoltar);
            rvObras = findViewById(R.id.obrasRelacionadasMuseu);
            btnSeguir = findViewById(R.id.btnSeguir);
            btnGuia = findViewById(R.id.btnGuia);

            rvObras.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

            btnVoltar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
            Intent info = getIntent();
            Bundle infoMuseu = info.getExtras();
            if(infoMuseu != null) {
                id = infoMuseu.getString("id");
            }
            btnGuia.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(TelaInfoMuseu.this, TelaGuias.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("id", id);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            });
        erroMuseu.setTextColor(ContextCompat.getColor(this, R.color.azul_carregando));
        erroMuseu.setText("Carregando...");
        erroMuseu.setVisibility(View.VISIBLE);
        FirebaseAuth auth = FirebaseAuth.getInstance();
        String email = auth.getCurrentUser().getEmail();
        selecionarIdUsuarioPorEmail(email,id);
        museuService.buscarMuseuPorId(id,TelaInfoMuseu.this,erroMuseu,nomeMuseu,descMuseu,fotoMuseu);
        obraService.buscarObrasPorMuseu(id,erroMuseu,TelaInfoMuseu.this,rvObras,listaObras,adapterObra);

        String finalId = id;
        btnSeguir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] idUser = new String[1];
                idUser[0] = idUsuario;
                if (btnSeguir.getContentDescription() != null && btnSeguir.getContentDescription().equals("selecionado")) {
                    usuarioMuseuService.deletarUsuarioMuseu(TelaInfoMuseu.this,idUser, finalId);
                    btnSeguir.setImageResource(R.drawable.btn_seguir);

                } else {
                    String idMuseu =id;
                    usuarioMuseuService.inserirUsuarioMuseu(TelaInfoMuseu.this, idUser, idMuseu);
                    btnSeguir.setImageResource(R.drawable.btn_seguir_selecionado);
                }
            }
        });

    }
    public void selecionarIdUsuarioPorEmail(String email, String id) {

        ApiService apiService = new ApiService(TelaInfoMuseu.this);
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
                        usuarioMuseuService.buscarSeExiste(idUsuario, id, TelaInfoMuseu.this, btnSeguir);
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
                aux.abrirDialogErro(TelaInfoMuseu.this, "Erro inesperado", "Erro ao obter id\nMensagem: " + throwable.getMessage());
            }
        });
    }


}