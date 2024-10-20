package com.aula.leontis.fragments.feed;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.aula.leontis.R;
import com.aula.leontis.adapters.AdapterObraFeed;
import com.aula.leontis.interfaces.usuario.UsuarioInterface;
import com.aula.leontis.models.obra.Obra;
import com.aula.leontis.services.ApiService;
import com.aula.leontis.services.ObraService;
import com.aula.leontis.services.UsuarioGeneroService;
import com.aula.leontis.services.UsuarioMuseuService;
import com.aula.leontis.utilities.MetodosAux;
import com.google.firebase.auth.FirebaseAuth;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MuseusSeguidos extends Fragment {
    RecyclerView rvMuseusSeguidos;
    TextView erroMuseusSeguidos;
    ProgressBar progressBar5;
    List<Obra> listaObras = new ArrayList<>();
    AdapterObraFeed adapterObraFeed = new AdapterObraFeed(listaObras);
    ObraService obraService = new ObraService();
    UsuarioMuseuService usuarioMuseuService = new UsuarioMuseuService();
    List<Long>museusSeguidos = new ArrayList<>();
    String idUsuario;

    MetodosAux aux = new MetodosAux();

    public MuseusSeguidos() {
    }

    public static MuseusSeguidos newInstance(String param1, String param2) {
        MuseusSeguidos fragment = new MuseusSeguidos();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_museus_seguidos, container, false);
        FirebaseAuth auth = FirebaseAuth.getInstance();
        String email = auth.getCurrentUser().getEmail();
        selecionarIdUsuarioPorEmail(email,idUsuario);
        rvMuseusSeguidos = view.findViewById(R.id.rvMuseusSeguidos);
        erroMuseusSeguidos = view.findViewById(R.id.erroMuseusSeguidos);
        rvMuseusSeguidos.setAdapter(adapterObraFeed);
        progressBar5 = view.findViewById(R.id.progressBar5);
        rvMuseusSeguidos.setLayoutManager(new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL));

        return view;
    }
    public void selecionarIdUsuarioPorEmail(String email, String id) {

        ApiService apiService = new ApiService(getContext());
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
                        usuarioMuseuService.buscarMuseusDeUmUsuario(idUsuario,getContext(),museusSeguidos,rvMuseusSeguidos,adapterObraFeed,listaObras,erroMuseusSeguidos,progressBar5);
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
                aux.abrirDialogErro(getContext(),"Erro inesperado","Erro ao obter id\nMensagem: "+throwable.getMessage());
            }
        });
    }
}