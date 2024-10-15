package com.aula.leontis.adapters;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.aula.leontis.R;
import com.aula.leontis.models.guia.Guia;
import com.bumptech.glide.Glide;

import java.util.List;

public class AdapterGuia extends RecyclerView.Adapter<AdapterGuia.viewHolderGuia>{
    private List<Guia> listaGuias;

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
        String url = listaGuias.get(holder.getAdapterPosition()).getUrl_imagem();
        if(url == null){
            url= "https://gamestation.com.br/wp-content/themes/game-station/images/image-not-found.png";
        }
        Glide.with(holder.imgGuia.getContext()).asBitmap().load(url).into(holder.imgGuia);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                Intent infoGuia = new Intent(holder.itemView.getContext(), TelaInfoGuia.class);
                bundle.putString("id", String.valueOf(listaGuias.get(holder.getAdapterPosition()).getId()));
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
            tituloGuia = itemView.findViewById(R.id.tituloGuia);

        }
    }
}
