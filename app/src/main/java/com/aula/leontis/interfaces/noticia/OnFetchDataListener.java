package com.aula.leontis.interfaces.noticia;

import com.aula.leontis.models.noticia.NewsHeadlines;

import java.util.List;

public interface OnFetchDataListener <NewsApiResponse>{
    void onfetchData(List<NewsHeadlines> list, String message);
    void onError(String message);
}

