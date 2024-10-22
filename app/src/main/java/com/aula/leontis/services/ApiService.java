package com.aula.leontis.services;

import android.content.Context;

import com.aula.leontis.Geral;
import com.aula.leontis.TokenInterceptor;
import com.aula.leontis.TokenManager;
import com.aula.leontis.interfaces.AuthInterface;
import com.aula.leontis.interfaces.artista.ArtistaInterface;
import com.aula.leontis.interfaces.diaFuncionamento.DiaFuncionamentoInterface;
import com.aula.leontis.interfaces.endereco.EnderecoMuseuInterface;
import com.aula.leontis.interfaces.genero.GeneroInterface;
import com.aula.leontis.interfaces.guia.GuiaInterface;
import com.aula.leontis.interfaces.museu.MuseuInterface;
import com.aula.leontis.interfaces.obra.ObraGuiaInterface;
import com.aula.leontis.interfaces.obra.ObraInterface;
import com.aula.leontis.interfaces.usuario.UsuarioGeneroInterface;
import com.aula.leontis.interfaces.usuario.UsuarioInterface;
import com.aula.leontis.interfaces.usuario.UsuarioMuseuInterface;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiService {
    String urlApi = Geral.getInstance().getUrlApiSql();
    ;
        private EnderecoMuseuInterface enderecoMuseuInterface;
        private DiaFuncionamentoInterface diaFuncionamentoInterface;
        private GuiaInterface guiaInterface;
        private ArtistaInterface artistaInterface;
        private UsuarioInterface usuarioInterface;
        private GeneroInterface generoInterface;
        private MuseuInterface museuInterface;
        private AuthInterface authInterface;
        private UsuarioGeneroInterface usuarioGeneroInterface;
        private ObraInterface obraInterface;
        private UsuarioMuseuInterface usuarioMuseuInterface;
        private ObraGuiaInterface obraGuiaInterface;


        public ApiService(Context context) {
            TokenManager tokenManager = new TokenManager(context);

            // Retrofit usado para login/renovação do token
            Retrofit authRetrofit = new Retrofit.Builder()
                    .baseUrl(urlApi)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            authInterface = authRetrofit.create(AuthInterface.class);

            // Configuração do OkHttpClient com o TokenInterceptor
            OkHttpClient client = new OkHttpClient.Builder()
                    .addInterceptor(new TokenInterceptor(tokenManager, authInterface, context))
                    .build();

            // Retrofit principal
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(urlApi)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            // Inicializando os serviços
            diaFuncionamentoInterface = retrofit.create(DiaFuncionamentoInterface.class);
            artistaInterface = retrofit.create(ArtistaInterface.class);
            usuarioInterface = retrofit.create(UsuarioInterface.class);
            generoInterface = retrofit.create(GeneroInterface.class);
            museuInterface = retrofit.create(MuseuInterface.class);
            usuarioGeneroInterface = retrofit.create(UsuarioGeneroInterface.class);
            obraInterface = retrofit.create(ObraInterface.class);
            usuarioMuseuInterface = retrofit.create(UsuarioMuseuInterface.class);
            enderecoMuseuInterface = retrofit.create(EnderecoMuseuInterface.class);
            obraGuiaInterface = retrofit.create(ObraGuiaInterface.class);
            guiaInterface = retrofit.create(GuiaInterface.class);
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
        public UsuarioGeneroInterface getUsuarioGeneroInterface() {
            return usuarioGeneroInterface;
        }

        public ObraInterface getObraInterface() {
            return obraInterface;
        }

        public UsuarioMuseuInterface getUsuarioMuseuInterface() {
            return usuarioMuseuInterface;
        }

        public ArtistaInterface getArtistaInterface() {
            return artistaInterface;
        }

        public DiaFuncionamentoInterface getDiaFuncionamentoInterface() {
            return diaFuncionamentoInterface;
        }

        public EnderecoMuseuInterface getEnderecoMuseuInterface() {
            return this.enderecoMuseuInterface;
        }

        public GuiaInterface getGuiaInterface() {
            return guiaInterface;
        }

        public ObraGuiaInterface getObraGuiaInterface() {
            return obraGuiaInterface;
        }


}
