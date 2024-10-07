package com.aula.leontis.interfaces.endereco;

import com.aula.leontis.models.endereco.EnderecoMuseu;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface EnderecoMuseuInterface {
    @GET("/api/enderecoMuseu/selecionarPorID/{id}")
    Call<EnderecoMuseu> selecionarEnderecoPorID(@Path("id") String id);
}
