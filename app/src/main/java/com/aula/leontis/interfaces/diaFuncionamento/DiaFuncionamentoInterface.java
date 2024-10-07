package com.aula.leontis.interfaces.diaFuncionamento;

import com.aula.leontis.models.diaFuncionamento.DiaFuncionamento;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface DiaFuncionamentoInterface {
    @GET("/api/diaFuncionamento/selecionarPorIdMuseu/{id}")
    Call<List<DiaFuncionamento>> selecionarPorIdMuseu(@Path("id") String id);
}
