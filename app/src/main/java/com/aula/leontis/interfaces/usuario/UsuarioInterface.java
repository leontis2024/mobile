package com.aula.leontis.interfaces.usuario;

import com.aula.leontis.models.usuario.Usuario;

import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface UsuarioInterface {

    @DELETE("api/usuario/excluir/{id}")
    Call<ResponseBody> deletarUsuario(@Path("id") String id);
    @PATCH("api/usuario/atualizar/{id}")
    Call<ResponseBody> atualizarUsuario( @Path("id") String id, @Body Map<String, Object> updates);
    @POST("api/usuario/inserir")
    Call<ResponseBody> salvarUsuario(@Body Usuario usuario);
    @GET("/api/usuario/selecionarUsuarioPorID/{id}")
    Call<ResponseBody> buscarUsuarioPorID(@Path("id") String id);

    @GET("/api/usuario/selecionarPorEmail")
    Call<ResponseBody> buscarPorEmail(@Field("email") String email);
}
