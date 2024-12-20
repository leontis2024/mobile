// GeneroService.java
package com.aula.leontis.interfaces.genero;

import com.aula.leontis.models.genero.Genero;
import com.aula.leontis.models.genero.GeneroCompleto;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface GeneroInterface {

    @GET("/api/genero/selecionarTodosGenerosParcial")
    Call<List<Genero>> buscarTodosGenerosParciais();

    @GET("/api/genero/selecionarTodosGeneros")
    Call<List<GeneroCompleto>> buscarTodosGeneros();

    @GET("/api/genero/selecionarGeneroPorID/{id}")
    Call<GeneroCompleto> buscarGeneroPorId(@Path("id") String id);

    @GET("/api/genero/pesquisarGenero")
    Call<List<GeneroCompleto>> selecionarGenerosPorNome(@Query("pesquisa") String pesquisa);


}
