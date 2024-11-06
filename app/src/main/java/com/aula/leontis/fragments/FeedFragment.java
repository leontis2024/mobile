package com.aula.leontis.fragments;

import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

import static androidx.core.content.ContextCompat.getSystemService;

import android.app.Dialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.os.Bundle;

import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.aula.leontis.Geral;
import com.aula.leontis.R;
import com.aula.leontis.activitys.TelaPrincipal;
import com.aula.leontis.activitys.TelaScanner;
import com.aula.leontis.adapters.AdapterGenero;
import com.aula.leontis.adapters.AdapterGeneroFiltro;
import com.aula.leontis.fragments.feed.ForYou;
import com.aula.leontis.fragments.feed.MuseusSeguidos;
import com.aula.leontis.interfaces.usuario.UsuarioInterface;
import com.aula.leontis.models.genero.Genero;
import com.aula.leontis.services.ApiService;
import com.aula.leontis.services.GeneroService;
import com.aula.leontis.utilities.MetodosAux;
import com.google.firebase.auth.FirebaseAuth;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FeedFragment extends Fragment {
    ImageButton btnBuscar, btnFiltrar, btnForYou, btnScanner,btnMuseusSeguidos, btnFecharPesquisa;
    ForYou foryou = new ForYou();
    MuseusSeguidos museusSeguidos = new MuseusSeguidos();
    MetodosAux aux = new MetodosAux();
    EditText campoPesquisa;
    GeneroService generoService = new GeneroService();
    String idUsuario;


    public FeedFragment() {

    }

    public static FeedFragment newInstance(String param1, String param2) {
        FeedFragment fragment = new FeedFragment();
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
    public void onResume() {

        super.onResume();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_feed, container, false);
        btnBuscar = view.findViewById(R.id.btnBuscar);
        btnFiltrar = view.findViewById(R.id.btnFiltrar);
        btnForYou = view.findViewById(R.id.btnForYou);
        btnScanner = view.findViewById(R.id.btnScanner);
        aux.agendarNotificacaoHoraEmHora(getContext());
        btnMuseusSeguidos = view.findViewById(R.id.btnMuseusSeguidos);
        btnFecharPesquisa = view.findViewById(R.id.btnFecharPesquisa);

          if(Geral.getInstance().isPrimeiroAcesso()) {
                aux.abrirDialogPrimeiroAcesso(getContext(),"Seja bem-vindo!","Para ajudar a equipe do Leontis, por favor responda a nossa pesquisa!");
            }


        campoPesquisa = view.findViewById(R.id.campoPesquisa);
        FirebaseAuth auth = FirebaseAuth.getInstance();
        String email = auth.getCurrentUser().getEmail();
        selecionarIdUsuarioPorEmail(email);

        FragmentTransaction fragmentTransaction = getChildFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.container_feed, foryou).commit();
        setButtonState("foryou", true);
        setButtonState("museus", false);

        campoPesquisa.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(btnForYou.getContentDescription().equals("selecionado")) {
                    if (s.length() > 0) {
                        foryou.buscar(s.toString());
                    }
                }else{
                    FragmentTransaction fragmentTransaction = getChildFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.container_feed, foryou).commit();
                    setButtonState("foryou", true);
                    setButtonState("museus", false);
                }


            }

            @Override
            public void afterTextChanged(Editable s) {

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
                foryou.onCreateView(inflater, container, savedInstanceState);
            }
        });

        btnMuseusSeguidos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FragmentTransaction fragmentTransaction = getChildFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.container_feed, museusSeguidos).commit();
                setButtonState("museus", true);
                setButtonState("foryou", false);
            }
        });

        btnForYou.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FragmentTransaction fragmentTransaction = getChildFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.container_feed, foryou).commit();
                setButtonState("foryou", true);
                setButtonState("museus", false);
            }
        });
        btnScanner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent escaner = new Intent(getContext(), TelaScanner.class);
                Bundle bundle = new Bundle();
                bundle.putString("id", idUsuario);
                escaner.putExtras(bundle);
                startActivity(escaner);
            }
        });
        btnFiltrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog dialog = new Dialog(getContext());
                dialog.setContentView(R.layout.dialog_filtro);
                dialog.getWindow().setLayout(WRAP_CONTENT,WRAP_CONTENT);
                dialog.getWindow().setBackgroundDrawableResource(R.drawable.caixa_mensagem_fundo);
                dialog.setCancelable(false);
                dialog.setCanceledOnTouchOutside(true);
                RecyclerView rvGeneros = dialog.findViewById(R.id.recyclerView);
                List<Genero> generos = new ArrayList<>();
                AdapterGeneroFiltro adapterGeneroFiltro = new AdapterGeneroFiltro(generos);
                rvGeneros.setLayoutManager(new LinearLayoutManager(getContext()));
                rvGeneros.setAdapter(adapterGeneroFiltro);
                generoService.buscarGenerosFiltro(null,getContext(),rvGeneros,generos,adapterGeneroFiltro);
                Button btnAplicar = dialog.findViewById(R.id.btnAplicarFiltro);
                btnAplicar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        List<Long> generosId = new ArrayList<>();
                        for(Genero genero : generos) {
                            if(genero.getCheckInteresse()) {
                                generosId.add(genero.getId());
                            }
                        }
                        foryou.buscarGenerosId(generosId);
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });

        return view;
    }
    public void setButtonState(String button, boolean isActive) {
        if (isActive) {
            switch (button) {
                case "foryou":
                    btnForYou.setImageResource(R.drawable.for_you_selecionada); // Define a imagem ativa para btn1
                    btnForYou.setContentDescription("selecionado");
                    break;
                case "museus":
                    btnMuseusSeguidos.setImageResource(R.drawable.museus_seguidos_selecionado); // Define a imagem ativa para btn2
                    btnMuseusSeguidos.setContentDescription("selecionado");
                    break;
            }
        } else {
            switch (button) {
                case "museus":
                    btnMuseusSeguidos.setImageResource(R.drawable.museus_seguidos); // Define a imagem inativa para btn1
                    btnMuseusSeguidos.setContentDescription("deselecionado");
                    break;
                case "foryou":
                    btnForYou.setImageResource(R.drawable.for_you); // Define a imagem inativa para btn2
                    btnForYou.setContentDescription("deselecionado");
                    break;
            }
        }
    }
    public void selecionarIdUsuarioPorEmail(String email) {

        ApiService apiService = new ApiService(getContext());
        UsuarioInterface usuarioInterface = apiService.getUsuarioInterface();
        if(email == null){
            return;
        }
        Call<ResponseBody> call = usuarioInterface.selecionarUsuarioPorEmail(email);
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
                aux.abrirDialogErro(getContext(), "Erro inesperado", "Erro ao obter id\nMensagem: " + throwable.getMessage());
            }
        });
    }


}