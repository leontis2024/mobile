package com.aula.leontis.fragments.feed;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.aula.leontis.R;

public class ForYou extends Fragment {

    public ForYou() {
        // Required empty public constructor
    }


    public static ForYou newInstance(String param1, String param2) {
        ForYou fragment = new ForYou();
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
        View view= inflater.inflate(R.layout.fragment_for_you, container, false);

        return view;
    }
}