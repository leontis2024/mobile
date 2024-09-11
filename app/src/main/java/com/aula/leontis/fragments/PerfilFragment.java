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

import com.aula.leontis.utilities.MetodosAux;
import com.aula.leontis.R;
import com.aula.leontis.activitys.TelaLogin;
import com.aula.leontis.interfaces.usuario.UsuarioInterface;
import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;

import org.json.JSONObject;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class PerfilFragment extends Fragment {
    MetodosAux aux = new MetodosAux();
    ImageButton btnAreaRestrita, btnLogout, btnDeletarConta;
    TextView nome,biografia;
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
        btnLogout = view.findViewById(R.id.btnLogout);
        btnDeletarConta = view.findViewById(R.id.btnDeletarConta);

        nome = view.findViewById(R.id.nomeUsuario);
        biografia = view.findViewById(R.id.biografia);
        foto = view.findViewById(R.id.fotoPerfil);

        btnDeletarConta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                aux.abrirDialogConfirmacao(getContext(),"Deletar Conta?","Deseja realmente deletar sua conta? Você não poderá recupera-la depois");
            }
        });

        if (getArguments() != null) {
            String id = getArguments().getString("id");
            Log.d("PerfilFragment", "ID recebido: " + id);
            buscarUsuarioPorId(id);
        }

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();
            }
        });

        return view;
    }
    public void logout(){
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
    public void buscarUsuarioPorId(String id) {
        String urlAPI = "https://dev2-tfqz.onrender.com/";

        // Configurar acesso à API
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(urlAPI)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        UsuarioInterface usuarioInterface = retrofit.create(UsuarioInterface.class);

        Call<ResponseBody> call = usuarioInterface.buscarUsuarioPorID(id);

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

                        // Obtém os valores de "apelido" e "biografia"
                        String apelidoApi = jsonObject.getString("apelido");
                        String nomeApi = jsonObject.getString("nome");
                        String biografiaApi = jsonObject.getString("biografia");
                        String urlFotoApi = jsonObject.getString("urlImagem");
                        if(urlFotoApi.equals("")||urlFotoApi == null){
                            urlFotoApi =  "https://static.vecteezy.com/system/resources/previews/019/879/186/non_2x/user-icon-on-transparent-background-free-png.png";
                        }

                        if(apelidoApi.equals("")){
                            nome.setText(nomeApi);
                        }else{
                            nome.setText(apelidoApi);
                        }

                        if(biografia.equals("")){
                            biografia.setHint("Nada por aqui...");
                        }else{
                            biografia.setText(biografiaApi);
                        }

                        Glide.with(getContext()).load(urlFotoApi).circleCrop().into(foto);

                        // Faça algo com os valores obtidos
                        Log.d("API_RESPONSE_GETID", "Campos obtidos: apelido: " + apelidoApi+" nome: "+nomeApi+" biografia: "+biografiaApi);

                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.e("API_ERROR_GETID", "Erro ao processar resposta: " + e.getMessage());
                    }
                } else {
                    Log.e("API_ERROR_GETID", "Erro na resposta da API: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable throwable) {
                Log.e("API_ERROR", "Erro ao fazer a requisição: " + throwable.getMessage());
                aux.abrirDialogErro(getContext(),"Erro inesperado","Erro ao obter dados do perfil\nMensagem: "+throwable.getMessage());
            }
        });
    }
}