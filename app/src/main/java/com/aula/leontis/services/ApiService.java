package com.aula.leontis.services;

import android.content.Context;

import com.aula.leontis.TokenInterceptor;
import com.aula.leontis.TokenManager;
import com.aula.leontis.interfaces.AuthInterface;
import com.aula.leontis.interfaces.genero.GeneroInterface;
import com.aula.leontis.interfaces.museu.MuseuInterface;
import com.aula.leontis.interfaces.obra.ObraInterface;
import com.aula.leontis.interfaces.usuario.UsuarioGeneroInterface;
import com.aula.leontis.interfaces.usuario.UsuarioInterface;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiService {

        private UsuarioInterface usuarioInterface;
        private GeneroInterface generoInterface;
        private MuseuInterface museuInterface;
        private AuthInterface authInterface;
        private UsuarioGeneroInterface usuarioGeneroInterface;
        private ObraInterface obraInterface;


        public ApiService(Context context) {
            TokenManager tokenManager = new TokenManager(context);

            // Retrofit usado para login/renovação do token
            Retrofit authRetrofit = new Retrofit.Builder()
                    .baseUrl("https://dev2-tfqz.onrender.com/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            authInterface = authRetrofit.create(AuthInterface.class);

            // Configuração do OkHttpClient com o TokenInterceptor
            OkHttpClient client = new OkHttpClient.Builder()
                    .addInterceptor(new TokenInterceptor(tokenManager, authInterface, context))
                    .build();

            // Retrofit principal
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("https://dev2-tfqz.onrender.com/")
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            // Inicializando os serviços
            usuarioInterface = retrofit.create(UsuarioInterface.class);
            generoInterface = retrofit.create(GeneroInterface.class);
            museuInterface = retrofit.create(MuseuInterface.class);
            usuarioGeneroInterface = retrofit.create(UsuarioGeneroInterface.class);
            authInterface = retrofit.create(AuthInterface.class);
            obraInterface = retrofit.create(ObraInterface.class);
        }

        // Métodos para obter os serviços
        public UsuarioInterface getUsuarioInterface() {
            return usuarioInterface;
        }

        public GeneroInterface getGeneroInterface() {
            return generoInterface;
        }

        public MuseuInterface getMuseuInterface() {
            return museuInterface;
        }
        public AuthInterface getAuthInterface() {
            return authInterface;
        }

        public UsuarioGeneroInterface getUsuarioGeneroInterface() {
            return usuarioGeneroInterface;
        }

        public ObraInterface getObraInterface() {
            return obraInterface;
        }

}
