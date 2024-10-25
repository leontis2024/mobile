package com.aula.leontis.interfaces.museu;

import com.aula.leontis.models.genero.GeneroCompleto;
import com.aula.leontis.models.museu.Museu;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface MuseuInterface {
    @GET("/api/museu/selecionarTudo")
    Call<List<Museu>> selecionarTodosMuseus();

    @GET("/api/museu/selecionarMuseuPorId/{id}")
    Call<Museu> buscarMuseuPorId(@Path("id") String id);

    @GET("/api/museu/pesquisarMuseu")
    Call<List<Museu>> selecionarMuseusPorNome(@Query("pesquisa") String pesquisa);
}
