package com.aula.leontis;

import android.content.Context;
import android.content.SharedPreferences;

public class TokenManager {
    private static final String PREFS_NAME = "my_prefs";
    private static final String KEY_TOKEN = "token_key";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_PASSWORD = "password";
    private SharedPreferences sharedPreferences;

    public TokenManager(Context context) {
        sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }

    // Salvar token
    public void saveToken(String token) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_TOKEN, token);
        editor.apply();
    }

    // Recuperar token
    public String getToken() {
        return sharedPreferences.getString(KEY_TOKEN, null);
    }

    // Salvar credenciais
    public void saveCredentials(String username, String password) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_USERNAME, username);
        editor.putString(KEY_PASSWORD, password);
        editor.apply();
    }

    // Recuperar nome de usuário
    public String getUsername() {
        return sharedPreferences.getString(KEY_USERNAME, null);
    }

    // Recuperar senha
    public String getPassword() {
        return sharedPreferences.getString(KEY_PASSWORD, null);
    }

    // Limpar token (para logout))
    public void clearToken() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(KEY_TOKEN);
        editor.apply();
    }
}
