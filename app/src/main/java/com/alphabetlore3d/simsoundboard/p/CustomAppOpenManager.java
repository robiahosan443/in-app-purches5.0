package com.alphabetlore3d.simsoundboard.p;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;
import androidx.lifecycle.ProcessLifecycleOwner;

import com.alphabetlore3d.simsoundboard.p.Interfaces.OnInterstitialAdListener;

public class CustomAppOpenManager
        implements LifecycleObserver, Application.ActivityLifecycleCallbacks
{
    private InterstitialPromote interstitialPromote;
    private final Context context;
    private Activity currentActivity;

    public CustomAppOpenManager(final Context context)
    {
        ProcessLifecycleOwner.get().getLifecycle().addObserver( this );
        this.context = context;

    }

    private void buildInterstitial(){
        interstitialPromote = new InterstitialPromote(currentActivity);
        interstitialPromote.setStyle(InterstitialStyle.Advance);
        interstitialPromote.setInstallColor("#FF5252"); //color of button from resource.
        //interstitialAd.setInstallColor("#E91E63"); //color of button from string.
        interstitialPromote.setTimer(5);//5 second to closed the Ad.
        interstitialPromote.setInstallTitle("Open");
        interstitialPromote.setRadiusButton(10); //corner of button radius.
        interstitialPromote.setOnInterstitialAdListener(new OnInterstitialAdListener() {
            @Override
            public void onInterstitialAdLoaded() {
                com.alphabetlore3d.simsoundboard.h.setLog("interstitialAd loaded.");
            }

            @Override
            public void onInterstitialAdClosed() {
                com.alphabetlore3d.simsoundboard.h.setLog("interstitialAd closed.");
            }

            @Override
            public void onInterstitialAdClicked() {
                com.alphabetlore3d.simsoundboard.h.setLog("interstitialAd clicked.");
            }

            @Override
            public void onInterstitialAdFailedToLoad(String error) {
                com.alphabetlore3d.simsoundboard.h.setLog("Interstitial failed to load : " + error);
            }
        });

    }

    @Override
    public void onActivityCreated(@NonNull Activity activity, @Nullable Bundle savedInstanceState) {
        currentActivity = activity;
        buildInterstitial();
    }

    @Override
    public void onActivityStarted(Activity activity) {
        // Updating the currentActivity only when an ad is not showing.
        if (!interstitialPromote.isShowing()) {
            currentActivity = activity;
        }
    }

    @Override
    public void onActivityResumed(@NonNull Activity activity) {
        currentActivity = activity;
    }

    @Override
    public void onActivityPaused(@NonNull Activity activity) {

    }

    @Override
    public void onActivityStopped(@NonNull Activity activity) {

    }

    @Override
    public void onActivitySaveInstanceState(@NonNull Activity activity, @NonNull Bundle outState) {

    }

    @Override
    public void onActivityDestroyed(@NonNull Activity activity) {
        currentActivity = null;
    }

    private void showAdIfReady()
    {
        Log.e("OnLifecycleEvent", "onStart: ");
        Toast.makeText(context, "OnLifecycleEvent", Toast.LENGTH_SHORT).show();
        if (interstitialPromote.isAdLoaded()) {
            interstitialPromote.show();
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    public void onStart()
    {
        showAdIfReady();
    }

}