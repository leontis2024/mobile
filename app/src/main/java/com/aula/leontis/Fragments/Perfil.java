package com.aula.leontis.Fragments;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.aula.leontis.R;
import com.aula.leontis.TelaLogin;
import com.aula.leontis.TelaPrincipal;
import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Perfil#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Perfil extends Fragment {
    ImageButton btnVoltar, btnAreaRestrita, btnLogout;
    TextView nome,biografia;
    ImageView foto;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Perfil() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Perfil.
     */
    // TODO: Rename and change types and number of parameters
    public static Perfil newInstance(String param1, String param2) {
        Perfil fragment = new Perfil();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_perfil, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

//        btnVoltar = view.findViewById(R.id.btnVoltar);
        btnAreaRestrita = view.findViewById(R.id.btnAreaRestrita);
        btnLogout = view.findViewById(R.id.btnLogout);

        nome = view.findViewById(R.id.nomeUsuario);
        biografia = view.findViewById(R.id.biografia);
        foto = view.findViewById(R.id.fotoPerfil);
      //  String url = FirebaseAuth.getInstance().getCurrentUser().getPhotoUrl().toString();
        String url =  "https://static.vecteezy.com/system/resources/previews/019/879/186/non_2x/user-icon-on-transparent-background-free-png.png";
        Glide.with(this).load(url).centerCrop().into(foto);
        nome.setText(FirebaseAuth.getInstance().getCurrentUser().getDisplayName());

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();
            }
        });
    }
    public void logout(){
        FirebaseAuth.getInstance().signOut();
        verificarUsuarioLogado();
    }
    public void verificarUsuarioLogado(){
        FirebaseAuth auth = FirebaseAuth.getInstance();
        if(auth.getCurrentUser() == null){
            Intent feed = new Intent(getContext(), TelaLogin.class);
            startActivity(feed);
            getActivity().finish();
        }
    }
}