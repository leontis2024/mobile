package com.aula.leontis.adapters;

import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.aula.leontis.R;
import com.aula.leontis.activitys.TelaScanner;
import com.aula.leontis.interfaces.usuario.UsuarioInterface;
import com.aula.leontis.models.guia.ObraGuia;
import com.aula.leontis.services.ApiService;
import com.aula.leontis.services.MongoService;
import com.aula.leontis.services.ObraService;
import com.aula.leontis.utilities.MetodosAux;
import com.google.firebase.auth.FirebaseAuth;

import org.json.JSONObject;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdapterObraGuia extends RecyclerView.Adapter<AdapterObraGuia.viewHolderObraGuia>{
        MetodosAux aux = new MetodosAux();
    boolean esquerda = true;
        ObraService obraService = new ObraService();
        MongoService mongoService = new MongoService();
        private List<ObraGuia> listaObraGuias;
        String idUsuario;
        public AdapterObraGuia(List<ObraGuia> listaObraGuias){
            this.listaObraGuias = listaObraGuias;
        }
        @NonNull
        @Override
        public com.aula.leontis.adapters.AdapterObraGuia.viewHolderObraGuia onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View viewItem = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_guia_mapa,parent,false);
            return new com.aula.leontis.adapters.AdapterObraGuia.viewHolderObraGuia(viewItem);
        }

        @Override
        public void onBindViewHolder(@NonNull com.aula.leontis.adapters.AdapterObraGuia.viewHolderObraGuia holder, int position) {
            FirebaseAuth auth = FirebaseAuth.getInstance();
            String email = auth.getCurrentUser().getEmail();
            selecionarIdUsuarioPorEmail(email,holder);
            ViewGroup.MarginLayoutParams paramsImageView = (ViewGroup.MarginLayoutParams) holder.itemView.getLayoutParams();
            int left = 260;
            int right= 260;
            if(esquerda){
               paramsImageView.setMargins(left, 100, 0, 0);
            }else{
               paramsImageView.setMargins(0,100 , right,0 );
            }
            esquerda = !esquerda;

            holder.itemView.setLayoutParams(paramsImageView);


            holder.numeroOrdem.setText(listaObraGuias.get(holder.getAdapterPosition()).getNrOrdem()+"");

        }

        @Override
        public int getItemCount() {
            return listaObraGuias.size();
        }

        public class viewHolderObraGuia extends RecyclerView.ViewHolder{
            TextView numeroOrdem;

            public ImageView item;

            public viewHolderObraGuia(@NonNull View itemView) {
                super(itemView);
                numeroOrdem = itemView.findViewById(R.id.numOrdem);
                item = itemView.findViewById(R.id.item);


            }
        }
    public void selecionarIdUsuarioPorEmail(String email,AdapterObraGuia.viewHolderObraGuia holder) {

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
                            mongoService.selecionarStatusGuiaAdapter(obraService,idUsuario, holder.itemView.getContext(),listaObraGuias.get(holder.getAdapterPosition()).getIdGuia(),holder,listaObraGuias);
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


