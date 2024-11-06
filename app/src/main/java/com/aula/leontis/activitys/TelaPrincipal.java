package com.aula.leontis.activitys;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.window.OnBackInvokedDispatcher;

import com.aula.leontis.R;
import com.aula.leontis.TokenManager;
import com.aula.leontis.fragments.FeedFragment;
import com.aula.leontis.interfaces.AuthInterface;
import com.aula.leontis.models.auth.AuthResponse;
import com.aula.leontis.models.auth.LoginRequest;
import com.aula.leontis.services.ApiService;
import com.aula.leontis.utilities.MetodosAux;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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