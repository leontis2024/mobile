package com.aula.leontis.activitys;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import android.os.Bundle;

import com.aula.leontis.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class TelaPrincipal extends AppCompatActivity {
    private NavHostFragment navHostFragment;
    private NavController navController;
    private Bundle idBundle = new Bundle();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tela_principal);
        String id = getIntent().getStringExtra("id");
        if(id==null) {
            id = "28516";
        }

        idBundle.putString("id", id);



        initNavigation();
    }
    private void initNavigation(){
        navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        navController = navHostFragment.getNavController();
        navController.navigate(R.id.perfil, idBundle);
        navController.navigate(R.id.generos, idBundle);
        navController.navigate(R.id.museus, idBundle);
        navController.navigate(R.id.feed, idBundle);
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        NavigationUI.setupWithNavController(bottomNavigationView, navController);
    }
}