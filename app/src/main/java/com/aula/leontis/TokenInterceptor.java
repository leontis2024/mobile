package com.aula.leontis;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;

import com.aula.leontis.activitys.TelaLogin;
import com.aula.leontis.interfaces.AuthInterface;
import com.aula.leontis.models.auth.AuthResponse;
import com.aula.leontis.utilities.MetodosAux;
import com.google.firebase.auth.FirebaseAuth;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Interceptor;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Response;

public class TokenInterceptor implements Interceptor {
    private TokenManager tokenManager;
    private AuthInterface authInterface;
    private MetodosAux aux =new MetodosAux();
    private Context context;

    public TokenInterceptor(TokenManager tokenManager, AuthInterface authInterface, Context context) {
        this.tokenManager = tokenManager;
        this.authInterface = authInterface;
        this.context = context;
    }

    @Override
    public okhttp3.Response intercept(Chain chain) throws IOException {
        Request originalRequest = chain.request();

        // Adicionar o Token de Acesso no cabeçalho Authorization
        String accessToken = tokenManager.getAccessToken();
        Request requestWithToken = originalRequest.newBuilder()
                .header("Authorization", accessToken)
                .build();

        // Fazer a primeira requisição
        okhttp3.Response response = chain.proceed(requestWithToken);

        // Se o Token de Acesso expirar (401 Unauthorized), tentar pegar um novo
        String refreshToken="";
        if (response.code() == 401) {
            synchronized (this) {
                // Tentar pegar um novo Token de Acesso usando o Token de Refresh
                refreshToken = tokenManager.getRefreshToken();
                if (refreshToken != null && tokenManager.isTokenValid(refreshToken)) {
                    String newAccessToken = refreshAccessToken(refreshToken);
                    if (newAccessToken != null) {
                        // Atualizar o Token de Acesso no TokenManager
                        tokenManager.saveAccessToken(newAccessToken);

                        // Refazer a requisição original com o novo Token de Acesso
                        Request newRequestWithToken = originalRequest.newBuilder()
                                .header("Authorization", newAccessToken)
                                .build();

                        // Refazer a requisição original com o novo Token de Acesso
                        return chain.proceed(newRequestWithToken);
                    }
                }
            }
        }
        if (response.code() == 401 && refreshToken.equals("")) {
            // Limpar tokens e redirecionar para login
            // Aqui você deve lançar uma exceção ou notificar a Activity/Fragment para redirecionar para o login
            aux.abrirDialogErro(context,"Login expirado","Você será deslogado automaticamente");
            tokenManager.clearTokens(); // Método para limpar tokens
            FirebaseAuth.getInstance().signOut();
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(context, TelaLogin.class);
                    context.startActivity(intent);
                    ((Activity) context).finish();
                }
            },5000);
            throw new IOException("Refresh token esta expirado");
        }

        return response;
    }

    // Método para pegar um novo Token de Acesso usando o Token de Refresh
    private String refreshAccessToken(String refreshToken) throws IOException {
        Map<String,String> tokens = new HashMap<>();
        tokens.put("refreshToken", refreshToken);
        Call<Map<String, String>> call = authInterface.refreshToken(tokens);
        Response<Map<String, String>> refreshResponse = call.execute();  // Executa de forma síncrona

        if (refreshResponse.isSuccessful()) {
            return refreshResponse.body().get("accessToken");  // Retorna o novo Token de Acesso
        } else {
            throw new IOException("Não foi possível obter um novo Token de Acesso");
        }
    }
}
