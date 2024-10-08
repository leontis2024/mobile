package com.aula.leontis;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
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

        // Adicionar "Bearer " antes do token
        Request requestWithToken = originalRequest.newBuilder()
                .header("Authorization",   accessToken)
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
                                .header("Authorization",  newAccessToken)
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
            aux.abrirDialogErro(context, "Login expirado", "Você será deslogado automaticamente");
            tokenManager.clearTokens();  // Método para limpar tokens
           // FirebaseAuth.getInstance().signOut();

            Handler handler = new Handler();
            handler.postDelayed(() -> {
                Intent intent = new Intent(context, TelaLogin.class);
               // context.startActivity(intent);
              //  ((Activity) context).finish();
            }, 5000);

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

        if (refreshResponse.isSuccessful() && refreshResponse.body() != null) {
            return refreshResponse.body().get("accessToken");  // Retorna o novo Token de Acesso
        } else {
            // Tratamento de falhas ao renovar o token
            if (refreshResponse.code() == 401) {
             //   Toast.makeText(context, "Token expirado", Toast.LENGTH_SHORT).show();
                tokenManager.clearTokens();
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(context, TelaLogin.class);
                context.startActivity(intent);
                ((Activity) context).finish();
            }
            throw new IOException("Falha ao obter novo Token de Acesso. Código: " + refreshResponse.code());
        }
    }
}
