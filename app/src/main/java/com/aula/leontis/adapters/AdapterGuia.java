package com.aula.leontis.adapters;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.aula.leontis.Geral;
import com.aula.leontis.R;
import com.aula.leontis.activitys.TelaGuias;
import com.aula.leontis.activitys.TelaInfoGuia;
import com.aula.leontis.interfaces.usuario.UsuarioInterface;
import com.aula.leontis.models.guia.Guia;
import com.aula.leontis.services.ApiService;
import com.aula.leontis.services.MongoService;
import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;

import org.json.JSONObject;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdapterGuia extends RecyclerView.Adapter<AdapterGuia.viewHolderGuia>{
    MongoService mongoService = new MongoService();
    private List<Guia> listaGuias;
    String idUsuario;

    public AdapterGuia(List<Guia> listaGuias) {
        this.listaGuias = listaGuias;
    }

    @NonNull
    @Override
    public AdapterGuia.viewHolderGuia onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View viewItem = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_guia,parent,false);
        return new AdapterGuia.viewHolderGuia(viewItem);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterGuia.viewHolderGuia holder, int position) {
        String url = listaGuias.get(holder.getAdapterPosition()).getUrlImagem();
        if(url == null){
            url= Geral.getInstance().getUrlImagePadrao();
        }
        holder.tituloGuia.setText(listaGuias.get(holder.getAdapterPosition()).getTituloGuia());
        Glide.with(holder.imgGuia.getContext()).asBitmap().load(url).into(holder.imgGuia);
        FirebaseAuth auth = FirebaseAuth.getInstance();
        String email = auth.getCurrentUser().getEmail();
        selecionarIdUsuarioPorEmail(email,holder,position);


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                Intent infoGuia = new Intent(holder.itemView.getContext(), TelaInfoGuia.class);
                bundle.putString("titulo", listaGuias.get(holder.getAdapterPosition()).getTituloGuia());
                bundle.putString("id", String.valueOf(listaGuias.get(holder.getAdapterPosition()).getId()));
                bundle.putString("idUsuario", idUsuario);
                infoGuia.putExtras(bundle);
                holder.itemView.getContext().startActivity(infoGuia);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listaGuias.size();
    }

    public class viewHolderGuia extends RecyclerView.ViewHolder {
        ImageView imgGuia;
        TextView tituloGuia;

        public viewHolderGuia(@NonNull View itemView) {
            super(itemView);
            imgGuia = itemView.findViewById(R.id.imgGuia);
            tituloGuia = itemView.findViewById(R.id.numOrdem);

        }
    }
    public void selecionarIdUsuarioPorEmail(String email, AdapterGuia.viewHolderGuia holder, int position) {

        ApiService apiService = new ApiService(holder.itemView.getContext());
        UsuarioInterface usuarioInterface = apiService.getUsuarioInterface();
        Call<ResponseBody> call = usuarioInterface.selecionarUsuarioPorEmail(email);

        //executar chamada
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful() && response.body() != null) {
                    try {
                        // Converte o corpo da resposta para string
                        String jsonResponse = response.body().string();

                        // Cria um JSONObject a partir da string
                        JSONObject jsonObject = new JSONObject(jsonResponse);

                        String idApi = jsonObject.getString("id");
                        idUsuario = idApi;
                        mongoService.selecionarStatusGuia(idUsuario,holder.itemView.getContext(), listaGuias.get(position).getId(),holder.imgGuia,-1);
                        // Faça algo com os valores obtidos
                        Log.d("API_RESPONSE_GET_EMAIL", "Campo obtido: id: " + idApi);

                    } catch (Exception e) {
                        Log.e("API_RESPONSE_GET_EMAIL", "Erro ao processar resposta: " + e.getMessage());

                    }
                } else {
                    Log.e("API_ERROR_GET_EMAIL", "Erro na resposta da API: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable throwable) {
                Log.e("API_ERROR_GET_EMAIL", "Erro ao fazer a requisição: " + throwable.getMessage());

            }
        });
    }
}
