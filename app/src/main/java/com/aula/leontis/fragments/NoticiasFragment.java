package com.aula.leontis.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
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
    String nome;
    ImageButton btnFecharPesquisa, btnBuscar;
    EditText campoPesquisa;
    AdapterNoticia adapterNoticia;
    TextView erroNoticia;
    ProgressBar progressBar;

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
        btnFecharPesquisa = view.findViewById(R.id.btnFecharPesquisa);
        btnBuscar = view.findViewById(R.id.btnBuscar);
        campoPesquisa = view.findViewById(R.id.campoPesquisa);
        progressBar = view.findViewById(R.id.progressBar8);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 1));
        erroNoticia = view.findViewById(R.id.erroNoticia);
        noticiaService.buscarNoticias(getContext(), listener, erroNoticia);


        campoPesquisa.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (s.length() > 0) {
                    nome = s.toString();
                    filtrar();
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        btnBuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                campoPesquisa.setVisibility(View.VISIBLE);
                btnBuscar.setVisibility(View.INVISIBLE);
                btnFecharPesquisa.setVisibility(View.VISIBLE);
                campoPesquisa.setText("");
            }
        });
        btnFecharPesquisa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                campoPesquisa.setVisibility(View.INVISIBLE);
                btnBuscar.setVisibility(View.VISIBLE);
                btnFecharPesquisa.setVisibility(View.INVISIBLE);
                recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 1));
                noticiaService.buscarNoticias(getContext(), listener, erroNoticia);

            }
        });

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
    private final OnFetchDataListener<NewsApiResponse> listener2 = new OnFetchDataListener<NewsApiResponse>() {
        @Override
        public void onfetchData(List<NewsHeadlines> list, String message) {
            showNews2(list,nome);
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
        List<String> keywords = Arrays.asList("museu", "arte", "exposição", "obra de arte", "artista", "cultura", "escultura", "galeria","pintor","grafite");

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

    private void showNews2(List<NewsHeadlines> list,String nome) {

        // Filtra os itens inválidos antes de passá-los ao adaptador
        List<NewsHeadlines> filteredList = new ArrayList<>();
        for (NewsHeadlines headline : list) {
            if (headline.getTitle() != null && !headline.getTitle().equals("[Removed]")) {
                filteredList.add(headline);
            }
        }

        List<String> keywords = Arrays.asList("museu", "arte", "exposição", "obra de arte", "artista", "cultura", "escultura", "galeria","pintor","grafite");

        String searchText = nome.toString().toLowerCase();

        // Filtra as notícias que contêm as palavras-chave e o título contém o texto de pesquisa
        List<NewsHeadlines> filteredArticles = filteredList.stream()
                .filter(article -> keywords.stream().anyMatch(keyword ->
                        article.getTitle().toLowerCase().contains(keyword)
                ) && article.getTitle().toLowerCase().contains(searchText))
                .collect(Collectors.toList());

        // Atualiza o RecyclerView com as notícias filtradas
        if (!filteredArticles.isEmpty()) {
            adapterNoticia = new AdapterNoticia(getContext(), filteredArticles);
            recyclerView.setAdapter(adapterNoticia);
        } else {
            erroNoticia.setText("Nenhuma notícia encontrada com os termos selecionados.");
        }
        progressBar.setVisibility(View.INVISIBLE);
    }


    public void filtrar(){
        progressBar.setVisibility(View.VISIBLE);
        noticiaService.buscarNoticias(getContext(), listener2, erroNoticia);
    }
}
