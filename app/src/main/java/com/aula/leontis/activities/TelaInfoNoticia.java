package com.aula.leontis.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.ProgressBar;

import com.aula.leontis.R;

public class TelaInfoNoticia extends AppCompatActivity {

    private WebView webview;
    private ProgressBar load;
    private ImageButton btVoltar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        setContentView(R.layout.tela_info_noticia);
        load = findViewById(R.id.progressBar3);
        load.bringToFront();
        webview = findViewById(R.id.webView2);
        webview.loadUrl(intent.getStringExtra("url"));
        webview.getSettings().setJavaScriptEnabled(true);
        btVoltar = findViewById(R.id.btn_voltar_noticia);
        btVoltar.bringToFront();

        btVoltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TelaInfoNoticia.this.finish();
            }
        });

        //permite continuar abrindo as paginas no webview
        webview.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                load.setVisibility(View.VISIBLE);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                load.setVisibility(View.INVISIBLE);
            }
        });
    }
    //controlar o clique do voltar para não fechar o aplicativo
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event){
        if (keyCode == KeyEvent.KEYCODE_BACK && webview.canGoBack()){
            webview.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}