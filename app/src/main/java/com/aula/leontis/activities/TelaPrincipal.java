package com.aula.leontis.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import android.os.Bundle;

import com.aula.leontis.R;
import com.aula.leontis.fragments.FeedFragment;
import com.aula.leontis.utilities.MetodosAux;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class TelaPrincipal extends AppCompatActivity {
    private NavHostFragment navHostFragment;
    private NavController navController;
    MetodosAux aux = new MetodosAux();
    private Bundle idBundle = new Bundle();
    FeedFragment feedFragment = new FeedFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tela_principal);

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