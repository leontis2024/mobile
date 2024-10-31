package com.aula.leontis.interfaces;

import android.util.Log;
import android.webkit.JavascriptInterface;

import com.aula.leontis.Geral;

import org.json.JSONException;
import org.json.JSONObject;

public interface InterfaceLocalStorage {
        @JavascriptInterface
        default void sendData(String data) {
            try {
                JSONObject jsonObject = new JSONObject(data);
                String terminou = jsonObject.getString("terminou");
                String predicao = jsonObject.getString("predicao");
                Geral.getInstance().setTerminou(Boolean.parseBoolean(terminou));
                Geral.getInstance().setPredicao(Boolean.parseBoolean(predicao));

                Log.d("predicao", "terminou: " + terminou);
                Log.d("predicao", "predicao: " + predicao);
            } catch (JSONException e) {
                Log.e("predicao", "Erro ao processar JSON", e);
            }
        }

}
