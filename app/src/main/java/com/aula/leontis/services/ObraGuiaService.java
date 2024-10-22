package com.aula.leontis.services;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.aula.leontis.R;
import com.aula.leontis.adapters.AdapterGuia;

import com.aula.leontis.adapters.AdapterObraGuia;
import com.aula.leontis.interfaces.obra.ObraGuiaInterface;
import com.aula.leontis.models.guia.Guia;
import com.aula.leontis.models.guia.ObraGuia;
import com.aula.leontis.models.guia.StatusGuiaRequest;
import com.aula.leontis.utilities.MetodosAux;
import com.bumptech.glide.Glide;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ObraGuiaService {
    MetodosAux aux =new MetodosAux();
    public void selecionarObraGuiaPorIdGuia( String idGuia, TextView erroGuia, Context context, RecyclerView rvGuias, List<ObraGuia> listaGuias, AdapterObraGuia adapterGuia, ProgressBar progressBar) {
        ApiService apiService = new ApiService(context);
        ObraGuiaInterface guiaInterface = apiService.getObraGuiaInterface();
        Call<List<ObraGuia>> call = guiaInterface.selecionarObrasGuiasPorIdGuia(Long.parseLong(idGuia));

        call.enqueue(new Callback<List<ObraGuia>>() {
            @Override
            public void onResponse(Call<List<ObraGuia>> call, Response<List<ObraGuia>> response) {
                progressBar.setVisibility(View.INVISIBLE);
                if (response.isSuccessful() && response.body() != null) {
                    List<ObraGuia> guias = response.body();
                    listaGuias.clear();
                    listaGuias.addAll(guias);
                    rvGuias.setAdapter(adapterGuia);
                    adapterGuia.notifyDataSetChanged();


                } else {
                    erroGuia.setText("Nenhuma guia encontrada");
                }
            }


            @Override
            public void onFailure(Call<List<ObraGuia>> call, Throwable t) {
                Log.e("API_ERROR_GET_GUIA", "Erro ao fazer a requisição: " + t.getMessage());
                erroGuia.setText("Falha ao obter obras do guia");
                aux.abrirDialogErro(context,"Erro inesperado","Erro ao obter dados dos guias\nMensagem: "+t.getMessage());
            }
        });
    }

    public void selecionarTamanhoGuia( String idUsuario,long idGuia, Context context,int nrOrdem) {
        ApiService apiService = new ApiService(context);
        ObraGuiaInterface guiaInterface = apiService.getObraGuiaInterface();
        Call<List<ObraGuia>> call = guiaInterface.selecionarObrasGuiasPorIdGuia(idGuia);
        MongoService mongoService = new MongoService();

        call.enqueue(new Callback<List<ObraGuia>>() {
            @Override
            public void onResponse(Call<List<ObraGuia>> call, Response<List<ObraGuia>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<ObraGuia> guias = response.body();
                    if(nrOrdem<guias.size()) {
                        mongoService.inserirStatusGuia(idUsuario, new StatusGuiaRequest(Long.parseLong(idUsuario), idGuia, false, nrOrdem), context);
                    }else{
                        mongoService.inserirStatusGuia(idUsuario, new StatusGuiaRequest(Long.parseLong(idUsuario), idGuia, true, guias.size()), context);
                    }


                }
            }


            @Override
            public void onFailure(Call<List<ObraGuia>> call, Throwable t) {
                Log.e("API_ERROR_GET_GUIA", "Erro ao fazer a requisição: " + t.getMessage());
                aux.abrirDialogErro(context,"Erro inesperado","Erro ao obter dados dos guias\nMensagem: "+t.getMessage());
            }
        });
    }

}
