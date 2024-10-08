package com.aula.leontis.services;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.aula.leontis.R;
import com.aula.leontis.interfaces.endereco.EnderecoMuseuInterface;

import com.aula.leontis.utilities.MetodosAux;
import com.aula.leontis.models.endereco.EnderecoMuseu;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EnderecoMuseuService {
    MetodosAux aux =new MetodosAux();
    public void buscarEnderecoMuseuPorId(String id, Context c, TextView erroEnderecoMuseu, TextView endereco) {
        ApiService apiService = new ApiService(c);
        EnderecoMuseuInterface enderecoMuseuInterface = apiService.getEnderecoMuseuInterface();

        Call<EnderecoMuseu> call = enderecoMuseuInterface.selecionarEnderecoPorID(id);


        call.enqueue(new Callback<EnderecoMuseu>() {
            @Override
            public void onResponse(Call<EnderecoMuseu> call, Response<EnderecoMuseu> response) {
                if (response.isSuccessful() && response.body() != null) {
                    erroEnderecoMuseu.setVisibility(View.INVISIBLE);
                    erroEnderecoMuseu.setTextColor(ContextCompat.getColor(c, R.color.vermelho_erro));
                    EnderecoMuseu enderecoMuseu = response.body();
                    endereco.setText("Endereço: "+enderecoMuseu.formatarEndereco());


                } else {
                    erroEnderecoMuseu.setText("Falha ao obter dados do endereço");
                    erroEnderecoMuseu.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<EnderecoMuseu> call, Throwable t) {
                Log.e("API_ERROR_GET_ID", "Erro ao fazer a requisição: " + t.getMessage());
                aux.abrirDialogErro(c, "Erro inesperado", "Erro ao obter dados do endereço\nMensagem: " + t.getMessage());
            }
        });
    }
    public void buscarEnderecoMuseuPorIdDoMuseuParcial(String id, Context c, TextView endereco) {
        ApiService apiService = new ApiService(c);
        EnderecoMuseuInterface enderecoMuseuInterface = apiService.getEnderecoMuseuInterface();

        Call<EnderecoMuseu> call = enderecoMuseuInterface.selecionarEnderecoPorID(id);


        call.enqueue(new Callback<EnderecoMuseu>() {
            @Override
            public void onResponse(Call<EnderecoMuseu> call, Response<EnderecoMuseu> response) {
                if (response.isSuccessful() && response.body() != null) {
                    EnderecoMuseu enderecoMuseu = response.body();
                    endereco.setText(enderecoMuseu.formatarEndereco());

                }
            }

            @Override
            public void onFailure(Call<EnderecoMuseu> call, Throwable t) {
                Log.e("API_ERROR_GET_ID", "Erro ao fazer a requisição: " + t.getMessage());
                aux.abrirDialogErro(c, "Erro inesperado", "Erro ao obter dados do endereço\nMensagem: " + t.getMessage());
            }
        });
    }


}
