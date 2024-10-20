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
import com.aula.leontis.activitys.TelaInfoObra;
import com.aula.leontis.models.obra.Obra;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.Target;

import java.util.List;

public class AdapterObraFeed extends RecyclerView.Adapter<AdapterObraFeed.viewHolderObraFeed>{
    private List<Obra> listaObras;

    public AdapterObraFeed(List<Obra> listaObras) {
        this.listaObras = listaObras;
    }

    @NonNull
    @Override
    public AdapterObraFeed.viewHolderObraFeed onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View viewItem = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_obra_feed,parent,false);
        return new AdapterObraFeed.viewHolderObraFeed(viewItem);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterObraFeed.viewHolderObraFeed holder, int position) {
        String url = listaObras.get(holder.getAdapterPosition()).getUrlImagem();
        if(url == null){
            url= "https://gamestation.com.br/wp-content/themes/game-station/images/image-not-found.png";
        }
        Glide.with(holder.imagemObra.getContext()).asBitmap().load(url).override(Target.SIZE_ORIGINAL).into(holder.imagemObra);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                Intent infoObra = new Intent(holder.itemView.getContext(), TelaInfoObra.class);
                bundle.putString("id", listaObras.get(holder.getAdapterPosition()).getId());
                infoObra.putExtras(bundle);
                holder.itemView.getContext().startActivity(infoObra);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listaObras.size();
    }

    public class viewHolderObraFeed extends RecyclerView.ViewHolder {
        ImageView imagemObra;

        public viewHolderObraFeed(@NonNull View itemView) {
            super(itemView);
            imagemObra = itemView.findViewById(R.id.imgObra);

        }
    }
}
