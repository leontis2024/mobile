package com.aula.leontis.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.aula.leontis.TokenManager;
import com.aula.leontis.activitys.TelaAreaRestrita;
import com.aula.leontis.activitys.TelaEditarPerfil;
import com.aula.leontis.services.ApiService;
import com.aula.leontis.services.UsuarioService;
import com.aula.leontis.utilities.MetodosAux;
import com.aula.leontis.R;
import com.aula.leontis.activitys.TelaLogin;
import com.aula.leontis.interfaces.usuario.UsuarioInterface;
import com.google.firebase.auth.FirebaseAuth;

import org.json.JSONObject;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class PerfilFragment extends Fragment {
    UsuarioService usuarioService = new UsuarioService();
    MetodosAux aux = new MetodosAux();
    ImageButton btnAreaRestrita, btnLogout, btnDeletarConta,btnEditarPerfil;
    TextView nome,biografia,erro;
    ImageView foto;
    String id;


    public PerfilFragment() {
        // Required empty public constructor
    }

    public static PerfilFragment newInstance() {
        PerfilFragment fragment = new PerfilFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_perfil, container, false);
        btnAreaRestrita = view.findViewById(R.id.btnAreaRestrita);
        btnEditarPerfil = view.findViewById(R.id.btnEditarPerfil);
        btnLogout = view.findViewById(R.id.btnLogout);
        btnDeletarConta = view.findViewById(R.id.btnDeletarConta);

        nome = view.findViewById(R.id.apelido);
        biografia = view.findViewById(R.id.biografia);
        foto = view.findViewById(R.id.fotoPerfil);
        erro = view.findViewById(R.id.erroUsuario);

        btnAreaRestrita.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent areaRestrita = new Intent(getContext(), TelaAreaRestrita.class);
                startActivity(areaRestrita);
            }
        });

        btnDeletarConta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                aux.abrirDialogConfirmacao(getContext(),"Deletar Conta?","Deseja realmente deletar sua conta? Você não poderá recupera-la depois",true,id);
            }
        });
        btnEditarPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle info = new Bundle();
                info.putString("id", id);
                Intent editarPerfil = new Intent(getContext(), TelaEditarPerfil.class);
                editarPerfil.putExtras(info);
                startActivity(editarPerfil);
            }
        });

        String email = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        selecionarIdUsuarioPorEmail(email);
        usuarioService.selecionarUsuarioPorEmail(email,getContext(),nome,biografia,foto,erro);

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();
            }
        });

        return view;
    }
    public void logout(){
        TokenManager tokenManager = new TokenManager(getContext());
        tokenManager.clearTokens(); // Método que deve remover o token armazenado

        FirebaseAuth.getInstance().signOut();
        verificarUsuarioLogado();
    }
    public void verificarUsuarioLogado(){
        FirebaseAuth auth = FirebaseAuth.getInstance();
        if(auth.getCurrentUser() == null){
            Intent feed = new Intent(getContext(), TelaLogin.class);
            startActivity(feed);
            getActivity().finish();
        }
    }
    public void selecionarIdUsuarioPorEmail(String email) {

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
                        id=idApi;

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
                aux.abrirDialogErro(getContext(),"Erro inesperado","Erro ao obter idl\nMensagem: "+throwable.getMessage());
            }
        });
    }
}