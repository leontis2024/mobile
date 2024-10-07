package com.aula.leontis.fragments.feed;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.aula.leontis.R;


public class MuseusSeguidos extends Fragment {



    public MuseusSeguidos() {
        // Required empty public constructor
    }

    public static MuseusSeguidos newInstance(String param1, String param2) {
        MuseusSeguidos fragment = new MuseusSeguidos();
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
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_museus_seguidos, container, false);

        return view;
    }
}