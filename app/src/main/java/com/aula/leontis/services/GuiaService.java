package com.aula.leontis.services;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.aula.leontis.R;
import com.aula.leontis.adapters.AdapterGuia;
import com.aula.leontis.interfaces.guia.GuiaInterface;
import com.aula.leontis.models.guia.Guia;
import com.aula.leontis.utilities.MetodosAux;
import com.bumptech.glide.Glide;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GuiaService {
    MetodosAux aux =new MetodosAux();
    public void selecionarGuiaPorMuseu(String idMuseu, TextView erroGuia, Context context, RecyclerView rvGuias, List<Guia> listaGuias, AdapterGuia adapterGuia, ImageView imgGuiaDestaque, TextView nomeGuiaDestaque,TextView idGuiadestaque) {
        erroGuia.setTextColor(ContextCompat.getColor(context, R.color.azul_carregando));
        erroGuia.setText("Carregando...");
        erroGuia.setVisibility(View.VISIBLE);

        ApiService apiService = new ApiService(context);
        GuiaInterface guiaInterface = apiService.getGuiaInterface();
        Call<List<Guia>> call = guiaInterface.selecionarGuiaPorMuseu(Long.parseLong(idMuseu));

        call.enqueue(new Callback<List<Guia>>() {
            @Override
            public void onResponse(Call<List<Guia>> call, Response<List<Guia>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    erroGuia.setVisibility(View.INVISIBLE);
                    List<Guia> guias = response.body();
                    if(guias.size()!=0){
                        String url = guias.get(0).getUrlImagem();
                        Glide.with(context).load(url).into(imgGuiaDestaque);
                        nomeGuiaDestaque.setText(guias.get(0).getTituloGuia());
                        idGuiadestaque.setText(String.valueOf(guias.get(0).getId()));
                        guias.remove(0);
                        listaGuias.addAll(guias);
                        adapterGuia.notifyDataSetChanged();
                        rvGuias.setAdapter(adapterGuia);
                    }

                } else {
                    if(response.code()!=404) {
                        erroGuia.setTextColor(ContextCompat.getColor(context, R.color.vermelho_erro));
                        Log.e("API_ERROR_GET_GUIA", "Não foi possivel fazer a requisição: " + response.code() + " " + response.errorBody());
                        erroGuia.setText("Falha ao obter dados dos guias");
                        erroGuia.setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Guia>> call, Throwable t) {
                erroGuia.setTextColor(ContextCompat.getColor(context, R.color.vermelho_erro));
                Log.e("API_ERROR_GET_GUIA", "Erro ao fazer a requisição: " + t.getMessage());
                erroGuia.setText("Falha ao obter dados dos guias");
                aux.abrirDialogErro(context,"Erro inesperado","Erro ao obter dados dos guias\nMensagem: "+t.getMessage());
            }
        });
    }

}
