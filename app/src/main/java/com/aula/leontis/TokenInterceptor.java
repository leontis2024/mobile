package com.aula.leontis;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import com.aula.leontis.activitys.TelaLogin;
import com.aula.leontis.interfaces.AuthInterface;
import com.aula.leontis.utilities.MetodosAux;
import com.google.firebase.auth.FirebaseAuth;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Call;

public class TokenInterceptor implements Interceptor {
    private TokenManager tokenManager;
    private AuthInterface authInterface;
    private MetodosAux aux = new MetodosAux();
    private Context context;

    public TokenInterceptor(TokenManager tokenManager, AuthInterface authInterface, Context context) {
        this.tokenManager = tokenManager;
        this.authInterface = authInterface;
        this.context = context;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request originalRequest = chain.request();

        // Adicionar o Token de Acesso no cabeçalho Authorization
        String accessToken = tokenManager.getAccessToken();
        if (accessToken == null) {
            String newAccessToken = refreshAccessToken(tokenManager.getRefreshToken());
            if (newAccessToken != null) {
                tokenManager.saveAccessToken(newAccessToken);
                accessToken = newAccessToken;  // Atualizar o accessToken com o novo token
            } else {
                throw new IOException("Não foi possível atualizar o token de acesso");
            }
        }

        // Não adicionar "Bearer " antes do token, pois a API já retorna o token com esse prefixo
        Request requestWithToken = originalRequest.newBuilder()
                .header("Authorization", accessToken)
                .build();

        Response response = chain.proceed(requestWithToken);

        // Se o Token de Acesso expirar (401 Unauthorized), tentar pegar um novo
        if (response.code() == 401) {
            synchronized (this) {
                String refreshToken = tokenManager.getRefreshToken();
                if (refreshToken != null && tokenManager.isTokenValid(refreshToken)) {
                    // Fechar a resposta antes de fazer uma nova requisição
                    response.close();

                    String newAccessToken = refreshAccessToken(refreshToken);
                    if (newAccessToken != null) {
                        tokenManager.saveAccessToken(newAccessToken);

                        // Refazer a requisição original com o novo Token de Acesso
                        Request newRequestWithToken = originalRequest.newBuilder()
                                .header("Authorization", newAccessToken)
                                .build();

                        // Prossiga com a nova requisição
                        return chain.proceed(newRequestWithToken);
                    }
                }
            }
        }

        // Se o refresh token também estiver expirado ou não disponível
        if (response.code() == 401 && (tokenManager.getRefreshToken() == null || !tokenManager.isTokenValid(tokenManager.getRefreshToken()))) {
            // Limpar tokens e redirecionar para login
            tokenManager.clearTokens();  // Método para limpar tokens
            FirebaseAuth.getInstance().signOut();

            // Exibir mensagem e redirecionar para login na thread principal
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(context, "Login expirado", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(context, TelaLogin.class);
                    context.startActivity(intent);
                    ((Activity) context).finish();
                }
            });

            throw new IOException("Refresh token está expirado");
        }

        return response;
    }


    // Método para pegar um novo Token de Acesso usando o Token de Refresh
    private String refreshAccessToken(String refreshToken) throws IOException {
        Map<String, String> tokens = new HashMap<>();
        tokens.put("refreshToken", refreshToken);
        Call<Map<String, String>> call = authInterface.refreshToken(tokens);
        retrofit2.Response<Map<String, String>> refreshResponse = call.execute();  // Executa de forma síncrona

        Log.d("TokenInterceptor", "Código de resposta: " + refreshResponse.code());

        if (refreshResponse.isSuccessful() && refreshResponse.body() != null) {
            String newToken = refreshResponse.body().get("accessToken");
            if (newToken != null) {
                return newToken;  // Retorna o novo Token de Acesso
            } else {
                Log.d("TokenInterceptor", "A resposta não contém um novo accessToken");
                throw new IOException("A resposta da API não contém um novo accessToken");
            }
        } else {
            // Tratamento de falhas ao renovar o token
            if (refreshResponse.code() == 401) {
                Log.d("TokenInterceptor", "Token de refresh expirado ou inválido");
                tokenManager.clearTokens();
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(context, TelaLogin.class);
                context.startActivity(intent);
                ((Activity) context).finish();
            }
            Log.d("TokenInterceptor", "Erro na resposta: " + refreshResponse.errorBody().string());
            throw new IOException("Falha ao obter novo Token de Acesso. Código: " + refreshResponse.code());
        }
    }
}
