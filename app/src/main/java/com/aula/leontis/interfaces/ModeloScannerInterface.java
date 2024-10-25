package com.aula.leontis.interfaces;

import com.aula.leontis.models.modelo.ModeloRequest;
import com.aula.leontis.models.modelo.ModeloResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ModeloScannerInterface {
    @POST("/prever")
    Call<ModeloResponse> preditarObra(@Body ModeloRequest request);
}
