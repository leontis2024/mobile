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

import java.util.List;


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

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_noticias, container, false);
        erroNoticia = view.findViewById(R.id.erroNoticia);
        noticiaService.buscarNoticias(getContext(),listener,erroNoticia);


        return view;
    }
    private final OnFetchDataListener<NewsApiResponse> listener = new OnFetchDataListener<NewsApiResponse>() {

        @Override
        public void onfetchData(List<NewsHeadlines> list, String message) {
            showNews(list);
        }

        @Override
        public void onError(String message) {

        }
    };
    private void showNews(List<NewsHeadlines> list) {

        recyclerView = getView().findViewById(R.id.rvNoticias);
        recyclerView.setHasFixedSize(true);
        adapterNoticia = new AdapterNoticia(getContext(),list);
        recyclerView.setAdapter(adapterNoticia);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(),1));

    }
}