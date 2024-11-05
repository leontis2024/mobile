package com.aula.leontis.activitys;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.ProgressBar;

import com.aula.leontis.Geral;
import com.aula.leontis.R;
import com.aula.leontis.interfaces.usuario.UsuarioInterface;
import com.aula.leontis.services.ApiService;
import com.aula.leontis.services.MongoService;
import com.google.firebase.auth.FirebaseAuth;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TelaPesquisa extends AppCompatActivity {
    WebView webView;
    ProgressBar carregar;
    ImageButton btVoltar;
    MongoService mongoService = new MongoService();


    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tela_pesquisa);
        carregar = findViewById(R.id.progressBar2);
        carregar.bringToFront();
        btVoltar = findViewById(R.id.btnVoltar);
        btVoltar.bringToFront();
        webView = findViewById(R.id.webView);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setDomStorageEnabled(true);
        webView.getSettings().setSupportZoom(false);
        webView.getSettings().setUseWideViewPort(true);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.loadUrl("http://ec2-34-200-118-26.compute-1.amazonaws.com:5000");
        btVoltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                webView.evaluateJavascript(
                        "(function() { " +
                                "    const terminou = localStorage.getItem('terminou');" +
                                "    const predicao = localStorage.getItem('predicao');" +
                                "    return JSON.stringify({ terminou: terminou, predicao: predicao });" +
                                "})()",
                        value -> {
                            try {
                                // Remove as aspas externas da string retornada
                                String jsonString = value.replaceAll("^\"|\"$", "").replace("\\", "");
                                JSONObject jsonObject = new JSONObject(jsonString);

                                String terminou = jsonObject.getString("terminou");
                                String predicao = jsonObject.getString("predicao");
                                if(terminou != null && predicao != null) {
                                    if (terminou.toLowerCase().equals("true")) {
                                        if (predicao.toLowerCase().equals("true")) {
                                            FirebaseAuth auth = FirebaseAuth.getInstance();
                                            String email = auth.getCurrentUser().getEmail();
                                            selecionarIdUsuarioPorEmail(email, predicao);
                                        }

                                    }
                                }


                                Log.d("predicao", "terminou: " + terminou);
                                Log.d("predicao", "predicao: " + predicao);
                            } catch (JSONException e) {
                                Log.e("JSON_ERROR", "Erro ao converter para JSONObject: " + e.getMessage());
                            }
                        }
                );


                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        TelaPesquisa.this.finish();
                    }
                },4500);

            }
        });
        webView.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                carregar.setVisibility(View.VISIBLE);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                carregar.setVisibility(View.INVISIBLE);
            }

        });
    }
    public boolean onKeyDown(int keyCode, KeyEvent event){
        if(keyCode== KeyEvent.KEYCODE_BACK && webView.canGoBack()){
            webView.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
    public void selecionarIdUsuarioPorEmail(String email, String premium) {

        ApiService apiService = new ApiService(TelaPesquisa.this);
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
                        mongoService.atualizarPremium(idApi,premium,TelaPesquisa.this);
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

            }
        });
    }
}