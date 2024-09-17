package com.aula.leontis.interfaces.usuario;


import com.aula.leontis.models.usuario.UsuarioGenero;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.POST;

public interface UsuarioGeneroInterface {
    @POST("api/usuarioGenero/inserir")
    Call<ResponseBody> inserir(UsuarioGenero usuarioGenero);
}
