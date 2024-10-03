package com.aula.leontis.interfaces.usuario;


import com.aula.leontis.models.usuario.UsuarioGenero;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface UsuarioGeneroInterface {
    @POST("/api/usuarioGenero/inserir")
    Call<ResponseBody> inserirUsuarioGenero(@Query ("id_user") long idUsuario, @Query("id_genero") long idGenero);

}
