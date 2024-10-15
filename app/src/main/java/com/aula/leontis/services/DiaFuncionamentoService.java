package com.aula.leontis.services;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.aula.leontis.R;
import com.aula.leontis.interfaces.diaFuncionamento.DiaFuncionamentoInterface;
import com.aula.leontis.interfaces.museu.MuseuInterface;
import com.aula.leontis.models.diaFuncionamento.DiaFuncionamento;
import com.aula.leontis.models.museu.Museu;
import com.aula.leontis.utilities.MetodosAux;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DiaFuncionamentoService {
    MetodosAux aux =new MetodosAux();
    public void buscarDiaFuncionamentoPorIdDoMuseu(String id, Context c, TextView erroDiaFuncionamento, TextView diaFuncionamentoMuseu) {
        ApiService apiService = new ApiService(c);
        DiaFuncionamentoInterface generoInterface = apiService.getDiaFuncionamentoInterface();

        Call<List<DiaFuncionamento>> call = generoInterface.selecionarPorIdMuseu(id);

        call.enqueue(new Callback<List<DiaFuncionamento>>() {
            @Override
            public void onResponse(Call<List<DiaFuncionamento>> call, Response<List<DiaFuncionamento>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    erroDiaFuncionamento.setVisibility(View.INVISIBLE);
                    erroDiaFuncionamento.setTextColor(ContextCompat.getColor(c, R.color.vermelho_erro));
                    List<DiaFuncionamento> diaFuncionamento = response.body();
                    diaFuncionamentoMuseu.setText(diaFuncionamentoMuseu.getText()+"\n\nDia de funcionamento: "+formatarHorarios(diaFuncionamento));

                } else {
                    erroDiaFuncionamento.setText("Falha ao obter dados do dia funcionamento");
                    erroDiaFuncionamento.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<List<DiaFuncionamento>> call, Throwable t) {
                Log.e("API_ERROR_GET_ID_DIA_FUNC", "Erro ao fazer a requisição: " + t.getMessage());
                aux.abrirDialogErro(c, "Erro inesperado", "Erro ao obter dados do dia funcionamento\nMensagem: " + t.getMessage());
            }
        });
    }
    public void buscarDiaFuncionamentoPorIdDoMuseuParcial(String id, Context c,  TextView diaFuncionamentoMuseu) {
        ApiService apiService = new ApiService(c);
        DiaFuncionamentoInterface generoInterface = apiService.getDiaFuncionamentoInterface();

        Call<List<DiaFuncionamento>> call = generoInterface.selecionarPorIdMuseu(id);


        call.enqueue(new Callback<List<DiaFuncionamento>>() {
            @Override
            public void onResponse(Call<List<DiaFuncionamento>> call, Response<List<DiaFuncionamento>> response) {
                if (response.isSuccessful() && response.body() != null) {

                    List<DiaFuncionamento> diaFuncionamento = response.body();
                    diaFuncionamentoMuseu.setText(formatarHorarios(diaFuncionamento));

                }
            }

            @Override
            public void onFailure(Call<List<DiaFuncionamento>> call, Throwable t) {
                Log.e("API_ERROR_GET_ID_DIA_FUNC_PARCIAL", "Erro ao fazer a requisição: " + t.getMessage());
                aux.abrirDialogErro(c, "Erro inesperado", "Erro ao obter dados do dia funcionamento\nMensagem: " + t.getMessage());
            }
        });
    }


    public void buscarDiaFuncionamentoPrecoPorIdDoMuseu(String id, Context c,  TextView preco) {
        ApiService apiService = new ApiService(c);
        DiaFuncionamentoInterface generoInterface = apiService.getDiaFuncionamentoInterface();

        Call<List<DiaFuncionamento>> call = generoInterface.selecionarPorIdMuseu(id);


        call.enqueue(new Callback<List<DiaFuncionamento>>() {
            @Override
            public void onResponse(Call<List<DiaFuncionamento>> call, Response<List<DiaFuncionamento>> response) {
                if (response.isSuccessful() && response.body() != null) {

                    List<DiaFuncionamento> diaFuncionamento = response.body();
                    Double precoDia=0.0;
                    for(DiaFuncionamento d : diaFuncionamento){
                        Double preco =0.0;
                        if(d.getPrecoDiaFuncionamento()!=null){
                            preco=d.getPrecoDiaFuncionamento();
                        }
                        precoDia+=preco;
                    }
                    precoDia=precoDia/diaFuncionamento.size();
                    if(precoDia==0.0){
                        String precoDiaString = "Gratuito";
                        preco.setText(precoDiaString);
                    }else {
                        preco.setText("R$ " + precoDia);
                    }

                }
            }

            @Override
            public void onFailure(Call<List<DiaFuncionamento>> call, Throwable t) {
                Log.e("API_ERROR_GET_ID_PRECO_DIA_FUNC", "Erro ao fazer a requisição: " + t.getMessage());
                aux.abrirDialogErro(c, "Erro inesperado", "Erro ao obter dados do preço do dia funcionamento\nMensagem: " + t.getMessage());
            }
        });
    }
    public String formatarHorarios(List<DiaFuncionamento> horarios) {
        // Definir a ordem dos dias da semana
        List<String> ordemDias = Arrays.asList("seg", "ter", "qua", "qui", "sex", "sab", "dom");

        // Ordenar os horários com base na ordem dos dias
        horarios.sort(Comparator.comparingInt(h -> ordemDias.indexOf(h.getDiaSemana())));

        Map<String, List<String>> grupos = new LinkedHashMap<>();
        String grupoAtual = null;

        // Agrupar dias com o mesmo horário
        for (DiaFuncionamento horario : horarios) {
            String chaveHorario = horario.getHrInicio() + " - " + horario.getHrTermino();

            if (grupoAtual == null || !grupoAtual.equals(chaveHorario)) {
                grupoAtual = chaveHorario;
                grupos.put(grupoAtual, new ArrayList<>());
            }
            grupos.get(grupoAtual).add(horario.getDiaSemana());
        }

        // Mapear os dias da semana para a versão completa
        Map<String, String> mapDias = new HashMap<>();
        mapDias.put("seg", "Segunda");
        mapDias.put("ter", "Terça");
        mapDias.put("qua", "Quarta");
        mapDias.put("qui", "Quinta");
        mapDias.put("sex", "Sexta");
        mapDias.put("sab", "Sábado");
        mapDias.put("dom", "Domingo");

        // Gerar a string final
        StringBuilder resultado = new StringBuilder();

        for (Map.Entry<String, List<String>> entry : grupos.entrySet()) {
            List<String> dias = entry.getValue();
            String horario = entry.getKey();

            if (dias.size() == 1) {
                resultado.append(mapDias.get(dias.get(0))).append(" ").append(horario);
            } else {
                String primeiroDia = mapDias.get(dias.get(0));
                String ultimoDia = mapDias.get(dias.get(dias.size() - 1));
                resultado.append(primeiroDia).append(" a ").append(ultimoDia).append(" ").append(horario);
            }

            resultado.append(", ");
        }

        // Remover a última vírgula
        if (resultado.length() > 0) {
            resultado.setLength(resultado.length() - 2);
        }

        return resultado.toString();
    }
}
