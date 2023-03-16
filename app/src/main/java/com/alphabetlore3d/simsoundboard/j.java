package com.alphabetlore3d.simsoundboard;

import android.app.Application;
import android.content.Context;

import com.chesire.lifecyklelog.LifecykleLog;
import com.alphabetlore3d.simsoundboard.p.AppPromote;
import com.alphabetlore3d.simsoundboard.p.CustomAppOpenManager;
import com.alphabetlore3d.simsoundboard.p.Interfaces.OnPromoteListener;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;


public class j extends Application {

    private CustomAppOpenManager customAppOpenManager;
    public static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
        // Create global configuration and initialize ImageLoader with this config
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this)
                .build();
        ImageLoader.getInstance().init(config);

        AppPromote.initializeBannerPromote(getApplicationContext(), h.AdPromoteBannerLink);
        AppPromote.initializeIntersPromote(getApplicationContext(), h.AdPromoteIntersLink);
        AppPromote.setOnPromoteListener(new OnPromoteListener() {
            @Override
            public void onInitializeSuccessful() {
                h.setLog("AppPromote onInitializeSuccessful");
            }

            @Override
            public void onInitializeFailed(String error) {
                h.setLog("AppPromote onInitializeFailed : "+error);
            }
        });

        if (BuildConfig.DEBUG) {
            LifecykleLog.INSTANCE.initialize(this);
            LifecykleLog.INSTANCE.setRequireAnnotation(false);
        }

//        customAppOpenManager = new CustomAppOpenManager(getApplicationContext());


    }




}