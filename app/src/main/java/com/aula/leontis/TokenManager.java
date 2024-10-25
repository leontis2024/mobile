package com.aula.leontis;

import android.content.Context;
import android.content.SharedPreferences;

public class TokenManager {
    private static final String PREFS_NAME = "my_prefs";
    private static final String KEY_ACCESS_TOKEN = "access_token_key";
    private static final String KEY_REFRESH_TOKEN = "refresh_token_key";
    private SharedPreferences sharedPreferences;

    public TokenManager(Context context) {
        sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }

    // Salvar o Token de Acesso
    public void saveAccessToken(String accessToken) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_ACCESS_TOKEN, accessToken);
        editor.apply();
    }

    // Recuperar o Token de Acesso
    public String getAccessToken() {
        return sharedPreferences.getString(KEY_ACCESS_TOKEN, null);
    }

    // Salvar o Token de Refresh
    public void saveRefreshToken(String refreshToken) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_REFRESH_TOKEN, refreshToken);
        editor.apply();
    }

    // Recuperar o Token de Refresh
    public String getRefreshToken() {
        return sharedPreferences.getString(KEY_REFRESH_TOKEN, null);
    }

    // Verificar validade do Token de Refresh
    public boolean isTokenValid(String token) {
        return token != null && !token.isEmpty();
    }

    // Limpar todos os tokens (para logout)
    public void clearTokens() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(KEY_ACCESS_TOKEN);
        editor.remove(KEY_REFRESH_TOKEN);
        editor.apply();
    }
}
