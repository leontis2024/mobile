package com.aula.leontis;

import android.content.Context;

import com.aula.leontis.interfaces.AuthInterface;
import com.aula.leontis.models.auth.AuthResponse;
import com.aula.leontis.models.auth.LoginRequest;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Response;

public class TokenInterceptor implements Interceptor {
    private TokenManager tokenManager;
    private AuthInterface authInterface;
    private Context context;

    public TokenInterceptor(TokenManager tokenManager, AuthInterface authInterface, Context context) {
        this.tokenManager = tokenManager;
        this.authInterface = authInterface;
        this.context = context;
    }

    @Override
    public okhttp3.Response intercept(Chain chain) throws IOException {
        Request originalRequest = chain.request();

        // Adiciona o token ao cabeçalho Authorization
        String token = tokenManager.getToken();
        Request requestWithToken = originalRequest.newBuilder()
                .header("Authorization", "Bearer " + token)
                .build();

        // Faz a primeira requisição
        okhttp3.Response response = chain.proceed(requestWithToken);

        // Se o token expirar (401 Unauthorized), tentar pegar um novo
        if (response.code() == 401) {
            synchronized (this) {
                // Tentar pegar um novo token
                String newToken = refreshToken();
                if (newToken != null) {
                    // Atualiza o token no TokenManager
                    tokenManager.saveToken(newToken);

                    // Refaz a requisição original com o novo token
                    Request newRequestWithToken = originalRequest.newBuilder()
                            .header("Authorization", "Bearer " + newToken)
                            .build();

                    // Retorna a nova resposta
                    return chain.proceed(newRequestWithToken);
                }
            }
        }

        return response;
    }

    // Método para pegar o novo token
    private String refreshToken() throws IOException {
        String username = tokenManager.getUsername();
        String password = tokenManager.getPassword();

        // Faz login novamente para pegar o novo token
        Call<AuthResponse> call = authInterface.login(new LoginRequest(username, password));
        Response<AuthResponse> loginResponse = call.execute();  // Executa de forma síncrona

        if (loginResponse.isSuccessful()) {
            return loginResponse.body().getToken();  // Retorna o novo token
        } else {
            throw new IOException("Não foi possível obter um novo token");
        }
    }
}
