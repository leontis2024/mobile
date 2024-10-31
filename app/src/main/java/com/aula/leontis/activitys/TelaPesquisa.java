package com.aula.leontis.activitys;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.ProgressBar;

import com.aula.leontis.Geral;
import com.aula.leontis.LocalStorageHandler;
import com.aula.leontis.R;
import com.aula.leontis.interfaces.InterfaceLocalStorage;
import com.aula.leontis.interfaces.usuario.UsuarioInterface;
import com.aula.leontis.services.ApiService;
import com.aula.leontis.services.MongoService;
import com.aula.leontis.services.UsuarioService;

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
        webView.addJavascriptInterface(new LocalStorageHandler(), "AndroidFunction");
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                // Suponha que a chave do `localStorage` seja "minhaVariavel"
                webView.evaluateJavascript(
                        "(function() { \n" +
                                "    const terminou = localStorage.getItem('terminou');\n" +
                                "    const predicao = localStorage.getItem('predicao');\n" +
                                "    const data = JSON.stringify({ terminou: terminou, predicao: predicao });\n" +
                                "    AndroidFunction.sendData(data);\n" +
                                "})()\n",
                        null
                );
            }
        });

        webView.loadUrl("https://arearestrita.onrender.com/");
        btVoltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean terminou = Geral.getInstance().isTerminou();
                boolean predicao = Geral.getInstance().isPredicao();

                if(terminou==true){
                    if(predicao==true){

                    }

                }
                TelaPesquisa.this.finish();
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
    public void selecionarIdUsuarioPorEmail(String email, String id) {

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
                        //mongoService;
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