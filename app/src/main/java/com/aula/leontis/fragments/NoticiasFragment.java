package com.aula.leontis.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.aula.leontis.R;
import com.aula.leontis.adapters.AdapterNoticia;
import com.aula.leontis.interfaces.noticia.OnFetchDataListener;
import com.aula.leontis.models.noticia.NewsApiResponse;
import com.aula.leontis.models.noticia.NewsHeadlines;
import com.aula.leontis.services.NoticiaService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class NoticiasFragment extends Fragment {
    NoticiaService noticiaService = new NoticiaService();
    RecyclerView recyclerView;
    AdapterNoticia adapterNoticia;
    TextView erroNoticia;

    public NoticiasFragment() {
        // Required empty public constructor
    }

    public static NoticiasFragment newInstance(String param1, String param2) {
        NoticiasFragment fragment = new NoticiasFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            // Additional initialization logic, if needed
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_noticias, container, false);
        recyclerView = view.findViewById(R.id.rvNoticias);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 1));
        erroNoticia = view.findViewById(R.id.erroNoticia);
        noticiaService.buscarNoticias(getContext(), listener, erroNoticia);

        return view;
    }

    private final OnFetchDataListener<NewsApiResponse> listener = new OnFetchDataListener<NewsApiResponse>() {
        @Override
        public void onfetchData(List<NewsHeadlines> list, String message) {
            showNews(list);
        }

        @Override
        public void onError(String message) {
            erroNoticia.setText("Erro ao carregar notícias: " + message);
        }
    };

    private void showNews(List<NewsHeadlines> list) {
        // Filtra os itens inválidos antes de passá-los ao adaptador
        List<NewsHeadlines> filteredList = new ArrayList<>();
        for (NewsHeadlines headline : list) {
            if (headline.getTitle() != null && !headline.getTitle().equals("[Removed]")) {
                filteredList.add(headline);
            }
        }

        // Lista de palavras-chave para filtrar notícias
        List<String> keywords = Arrays.asList("museu", "arte", "exposição", "obra de arte", "artista", "cultura", "escultura", "galeria");

        // Filtra as notícias que contêm as palavras-chave no título
        List<NewsHeadlines> filteredArticles = filteredList.stream()
                .filter(article -> keywords.stream().anyMatch(keyword ->
                        article.getTitle().toLowerCase().contains(keyword)
                ))
                .collect(Collectors.toList());

        // Atualiza o RecyclerView com as notícias filtradas
        if (!filteredArticles.isEmpty()) {
            adapterNoticia = new AdapterNoticia(getContext(), filteredArticles);
            recyclerView.setAdapter(adapterNoticia);
        } else {
            erroNoticia.setText("Nenhuma notícia encontrada com os termos selecionados.");
        }
    }
}
