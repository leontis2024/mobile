package com.aula.leontis.services;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.aula.leontis.R;
import com.aula.leontis.adapters.AdapterObra;
import com.aula.leontis.interfaces.obra.ObraInterface;
import com.aula.leontis.interfaces.obra.ObraInterface;
import com.aula.leontis.models.obra.Obra;
import com.aula.leontis.models.obra.Obra;
import com.aula.leontis.utilities.MetodosAux;
import com.bumptech.glide.Glide;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ObraService {
    MetodosAux aux =new MetodosAux();
    public void buscarObrasPorMuseu(String idMuseu,TextView erroObra, Context context, RecyclerView rvObras, List<Obra> listaObras, AdapterObra adapterObra) {
        erroObra.setTextColor(ContextCompat.getColor(context, R.color.azul_carregando));
        erroObra.setText("Carregando...");
        erroObra.setVisibility(View.VISIBLE);

        ApiService apiService = new ApiService(context);
        ObraInterface obraInterface = apiService.getObraInterface();
        Call<List<Obra>> call = obraInterface.selecionarObrasPorMuseu(Long.parseLong(idMuseu));

        // Buscar todos os gêneros
        call.enqueue(new Callback<List<Obra>>() {
            @Override
            public void onResponse(Call<List<Obra>> call, Response<List<Obra>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    erroObra.setVisibility(View.INVISIBLE);

                    listaObras.addAll(response.body());
                    adapterObra.notifyDataSetChanged();
                    rvObras.setAdapter(adapterObra);

                } else {
                    erroObra.setTextColor(ContextCompat.getColor(context, R.color.vermelho_erro));
                    Log.e("API_ERROR_GET", "Não foi possivel fazer a requisição: " + response.code()+" "+response.errorBody());
                    erroObra.setText("Falha ao obter dados das obras");
                    erroObra.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<List<Obra>> call, Throwable t) {
                erroObra.setTextColor(ContextCompat.getColor(context, R.color.vermelho_erro));
                Log.e("API_ERROR_GET", "Erro ao fazer a requisição: " + t.getMessage());
                erroObra.setText("Falha ao obter dados das obras");
                aux.abrirDialogErro(context,"Erro inesperado","Erro ao obter dados das obras\nMensagem: "+t.getMessage());
            }
        });
    }
}
