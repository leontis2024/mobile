package com.aula.leontis.interfaces.usuario;

import com.aula.leontis.models.usuario.UsuarioMuseu;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface UsuarioMuseuInterface {
    @POST("/api/usuarioMuseu/inserir")
    Call<ResponseBody> inserirUsuarioMiseu(@Query ("id_user") long idUsuario, @Query("id_museu") long idMuseu);

    @GET("/api/usuarioMuseu/buscarSeExiste")
    Call<UsuarioMuseu> buscarSeExiste(@Query("usuario") long idUsuario, @Query("museu") long idMuseu);

    @DELETE("/api/usuarioMuseu/deletar")
    Call<ResponseBody> deletarUsuarioMuseu(@Query("id_user") long idUsuario, @Query("id_museu") long idMuseu);
    @GET("/api/usuarioMuseu/buscarPorUsuario")
    Call<List<UsuarioMuseu>> buscarMuseusPorUsuario(@Query("usuario") long idUsuario);
}
