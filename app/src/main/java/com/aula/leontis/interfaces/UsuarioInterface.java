package com.aula.leontis.interfaces;

import com.aula.leontis.model.Usuario;

import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface UsuarioInterface {
    @PATCH("api/usuario/atualizar/{id}")
    Call<ResponseBody> atualizarUsuario( @Path("id") String id, @Body Map<String, Object> updates);
    @POST("api/usuario/inserir")
    Call<ResponseBody> salvarUsuario(@Body Usuario usuario);

    @GET("/api/usuario/selecionarPorEmail")
    Call<String> buscarUsuarioPorEmail(@Body String email);
}
