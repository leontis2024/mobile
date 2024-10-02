//package com.aula.leontis.adapters;
//
//import android.annotation.SuppressLint;
//import android.content.Context;
//import android.content.Intent;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//
//import androidx.annotation.NonNull;
//import androidx.recyclerview.widget.RecyclerView;
//
//import com.aula.leontis.R;
//import com.aula.leontis.models.noticia.NewsHeadlines;
//
//import java.util.List;
//
//public class AdapterNoticia extends RecyclerView.Adapter<AdapterNoticia.viewHolderNoticia> {
//        private Context context;
//        private List<NewsHeadlines> headlines;
//
//        public AdapterNoticia(Context context, List<NewsHeadlines> headlines) {
//            this.context = context;
//            this.headlines = headlines;
//        }
//
//        @NonNull
//        @Override
//        public AdapterNoticia onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//            return new AdapterNoticia(LayoutInflater.from(context).inflate(R.layout.item_noticia,parent,false));
//        }
//
//        @Override
//        public void onBindViewHolder(@NonNull AdapterNoticia holder, @SuppressLint("RecyclerView") int position) {
//            if (headlines.get(position).getTitle()==null || headlines.get(position).getTitle().equals("[Removed]")){
//                headlines.remove(position);
//            }else {
//                holder.text_title.setText(headlines.get(position).getTitle());
//                holder.text_source.setText(headlines.get(position).getSource().getName());
//
//                if (headlines.get(position).getUrlToImage() != null) {
//                    Picasso.get().load(headlines.get(position).getUrlToImage()).into(holder.img_headline);
//                }
//            }
//
//            holder.itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Intent intent = new Intent(context, Noticias.class);
//                    intent.putExtra("url",headlines.get(position).getUrl());
//                    context.startActivity(intent);
//                }
//            });
//        }
//
//        @Override
//        public int getItemCount() {
//            return headlines.size();
//        }
//
//        public class viewHolderNoticia extends RecyclerView.ViewHolder {
//            TextView text_title, text_source;
//            ImageView img_headline;
//            public viewHolderNoticia(@NonNull View itemView) {
//                super(itemView);
//                text_title = itemView.findViewById(R.id.txt_title);
//                text_source = itemView.findViewById(R.id.txt_source);
//                img_headline = itemView.findViewById(R.id.img_headline);
//            }
//        }
//
//}
