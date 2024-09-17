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
import retrofit2.http.Query;

public interface UsuarioInterface {
    @GET("/api/usuario/selecionarUsuarioPorID/{id}")
    Call<ResponseBody> selecionarUsuarioPorID(@Path("id") String id);
    @GET("/api/usuario/selecionarPorEmail")
    Call<ResponseBody> selecionarUsuarioPorEmail(@Query("email") String email);
    @POST("api/usuario/inserir")
    Call<ResponseBody> inserirUsuario(@Body Usuario usuario);
    @PATCH("api/usuario/atualizar/{id}")
    Call<ResponseBody> atualizarUsuario( @Path("id") String id, @Body Map<String, Object> updates);
    @DELETE("api/usuario/excluir/{id}")
    Call<ResponseBody> deletarUsuario(@Path("id") String id);

}
