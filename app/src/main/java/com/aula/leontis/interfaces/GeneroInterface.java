// GeneroService.java
package com.aula.leontis.interfaces;

import com.aula.leontis.model.Genero;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface GeneroInterface {
    @GET("/api/genero/selecionarTodosGenerosParcial")
    Call<List<Genero>> buscarTodosGenerosParciais();

}
