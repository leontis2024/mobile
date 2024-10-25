package com.aula.leontis.interfaces.obra;

import com.aula.leontis.models.guia.ObraGuia;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface ObraGuiaInterface {
    @GET("/api/obraGuia/selecionarObraGuiaPorID/{id}")
    Call<List<ObraGuia>> selecionarObrasGuiasPorIdGuia(@Path("id") Long id);
}
