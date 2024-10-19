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
    private boolean appOpened = false;

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
             //   redisService.incrementarAtividadeUsuario();
                Log.d("Monitoramento", "Incrementado");
                appOpened = true;
            }
        }
        activityReferences++;
    }

    @Override
    public void onActivityStopped(Activity activity){
        isActivityChangingConfigurations = activity.isChangingConfigurations();

        if (!isActivityChangingConfigurations && activityReferences == 0) {
          //  redisService.decrementarAtividadeUsuario();
            Log.d("Monitoramento", "Decrementado");
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
