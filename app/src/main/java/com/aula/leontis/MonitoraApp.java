package com.aula.leontis;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.util.Log;

import com.aula.leontis.services.RedisService;

public class MonitoraApp extends Application implements Application.ActivityLifecycleCallbacks {
    RedisService redisService = new RedisService();

    private int activityReferences = 0;
    private boolean isActivityChangingConfigurations = false;
    private boolean appOpened = false; // Variável para monitorar se o app já foi aberto

    @Override
    public void onCreate() {
        super.onCreate();
        registerActivityLifecycleCallbacks(this);
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {}

    @Override
    public void onActivityStarted(Activity activity) {
        if (activityReferences == 0 && !isActivityChangingConfigurations) {
            if (!appOpened) {
                // Somente incrementa quando o app é aberto pela primeira vez (foreground)
                redisService.incrementarAtividadeUsuario();
                Log.d("AppLifecycle", "App foi aberto");
                appOpened = true;
            }
        }
        activityReferences++;
    }

    @Override
    public void onActivityStopped(Activity activity){
        isActivityChangingConfigurations = activity.isChangingConfigurations();

        if (!isActivityChangingConfigurations && activityReferences == 0) {
            // Aqui o app foi completamente fechado (nenhuma activity restante)
            redisService.decrementarAtividadeUsuario();
            Log.d("AppLifecycle", "App foi fechado completamente");
            appOpened = false;
        }
    }


    @Override
    public void onActivityResumed(Activity activity) {}

    @Override
    public void onActivityPaused(Activity activity) {}

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {}

    @Override
    public void onActivityDestroyed(Activity activity) {


    }
}
