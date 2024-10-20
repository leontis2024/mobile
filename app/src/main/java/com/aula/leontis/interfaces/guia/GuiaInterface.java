package com.aula.leontis.interfaces.guia;

import com.aula.leontis.models.guia.Guia;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface GuiaInterface {
    @GET("/api/guia/selecionarGuiaPorMuseu/{id}")
    Call<List<Guia>> selecionarGuiaPorMuseu(@Path("id") long id);
    @GET("/api/guia/pesquisarGuia")
    Call<List<Guia>> selecionarGuiaPorNome(@Query("pesquisa") String pesquisa);
}
