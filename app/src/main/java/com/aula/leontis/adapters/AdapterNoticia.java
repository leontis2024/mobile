package com.aula.leontis.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.aula.leontis.R;
import com.aula.leontis.activitys.TelaInfoNoticia;
import com.aula.leontis.models.noticia.NewsHeadlines;
import com.squareup.picasso.Picasso;

import java.util.List;

public class AdapterNoticia extends RecyclerView.Adapter<AdapterNoticia.viewHolderNoticia> {
        private Context context;
        private List<NewsHeadlines> headlines;

        public AdapterNoticia(Context context, List<NewsHeadlines> headlines) {
            this.context = context;
            this.headlines = headlines;
        }

        @NonNull
        @Override
        public viewHolderNoticia onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View viewItem = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_noticia,parent,false);
            return new viewHolderNoticia(viewItem);
        }

    @Override
    public void onBindViewHolder(@NonNull viewHolderNoticia holder, int position) {
        // Evitar remoção dentro do onBindViewHolder
        holder.titulo.setText(headlines.get(holder.getAdapterPosition()).getTitle());
        holder.desc.setText(headlines.get(holder.getAdapterPosition()).getSource().getName());

        if (headlines.get(holder.getAdapterPosition()).getUrlToImage() != null) {
            Picasso.get().load(headlines.get(holder.getAdapterPosition()).getUrlToImage()).into(holder.img);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, TelaInfoNoticia.class);
                intent.putExtra("url", headlines.get(holder.getAdapterPosition()).getUrl());
                context.startActivity(intent);
            }
        });
    }


    @Override
        public int getItemCount() {
            return headlines.size();
        }

        public class viewHolderNoticia extends RecyclerView.ViewHolder {
            TextView titulo,desc;
            ImageView img;
            public viewHolderNoticia(@NonNull View itemView) {
                super(itemView);
                titulo = itemView.findViewById(R.id.tituloNoticia);
                desc = itemView.findViewById(R.id.descNoticia);
                img = itemView.findViewById(R.id.imgNoticia);
            }
        }

}
