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

import com.aula.leontis.utilities.MetodosAux;
import com.aula.leontis.R;
import com.aula.leontis.activitys.TelaInfoGenero;
import com.aula.leontis.models.genero.GeneroCompleto;
import com.bumptech.glide.Glide;

import java.util.List;

public class AdapterGeneroCompleto extends RecyclerView.Adapter<AdapterGeneroCompleto.viewHolderGeneroCompleto>{
    MetodosAux aux = new MetodosAux();
    private List<GeneroCompleto> listaGeneros;
    public AdapterGeneroCompleto(List<GeneroCompleto> listaGeneros){
        this.listaGeneros = listaGeneros;
    }
    @NonNull
    @Override
    public viewHolderGeneroCompleto onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View viewItem = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_genero_completo,parent,false);
        return new viewHolderGeneroCompleto(viewItem);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolderGeneroCompleto holder, int position) {
        holder.nomeGenero.setText(listaGeneros.get(position).getNomeGenero());
        String url = listaGeneros.get(holder.getAdapterPosition()).getUrlImagem();
        if(url == null){
            url= "https://gamestation.com.br/wp-content/themes/game-station/images/image-not-found.png";
        }
        Glide.with(holder.imagemGenero.getContext()).asBitmap().load(url).into(holder.imagemGenero);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                Intent infoGenero = new Intent(holder.itemView.getContext(), TelaInfoGenero.class);
                bundle.putString("id", listaGeneros.get(holder.getAdapterPosition()).getId());
                infoGenero.putExtras(bundle);
                holder.itemView.getContext().startActivity(infoGenero);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listaGeneros.size();
    }

    public class viewHolderGeneroCompleto extends RecyclerView.ViewHolder{

        TextView nomeGenero;
        ImageView imagemGenero;


        public viewHolderGeneroCompleto(@NonNull View itemView) {
            super(itemView);
            nomeGenero = itemView.findViewById(R.id.titulo_genero);
            imagemGenero = itemView.findViewById(R.id.imagemGenero);

        }
    }
}
