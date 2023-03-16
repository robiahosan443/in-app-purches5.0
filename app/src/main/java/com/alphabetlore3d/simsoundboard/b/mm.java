package com.alphabetlore3d.simsoundboard.b;


import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.IntentSender;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatImageView;

import com.alphabetlore3d.simsoundboard.R;
import com.alphabetlore3d.simsoundboard.billing.RemoveAdsActivity;
import com.alphabetlore3d.simsoundboard.p.BannerPromote;
import com.alphabetlore3d.simsoundboard.p.Interfaces.OnBannerListener;
import com.alphabetlore3d.simsoundboard.p.Interfaces.OnInterstitialAdListener;
import com.alphabetlore3d.simsoundboard.p.InterstitialPromote;
import com.alphabetlore3d.simsoundboard.p.InterstitialStyle;
import com.alphabetlore3d.simsoundboard.pref.SpManager;
import com.google.android.play.core.appupdate.AppUpdateInfo;
import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.android.play.core.appupdate.AppUpdateManagerFactory;
import com.google.android.play.core.install.model.AppUpdateType;
import com.google.android.play.core.install.model.UpdateAvailability;
import com.google.android.play.core.tasks.Task;
import com.system.android.ad.build;

public class mm extends AppCompatActivity {

    ImageView a;
    ImageView b;
    ImageView h;
    ImageView d;
    ImageView e;
    Dialog dialogRate;
    private BannerPromote bannerPromote;
    private InterstitialPromote interstitialPromote;

    String f1 = "com.alpha";
    String f2 = "betl";
    String f3 = "ore3";
    String f4 = "d.simso";
    String f5 = "undbo";
    String f6 = "ard";


    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mm_);
        if (getPackageName().compareTo(f1 + f2 + f3 + f4 + f5 + f6) != 0) {
            String error = null;
            error.getBytes();
        }
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        this.dialogRate = new Dialog(this);
        buildBanner();
        inAppUpdate();
        buildInterstitial();


        if (interstitialPromote.isAdLoaded()) {
            interstitialPromote.show();
        }

        this.a = findViewById(R.id.playbtn);
        this.b = findViewById(R.id.moreapps);
        this.h = findViewById(R.id.rate);
        this.d = findViewById(R.id.share);



        boolean isSubscribed = SpManager.INSTANCE.getSpBoolean(SpManager.SP_IS_SUBSCRIBED, false);
        if (!isSubscribed) {
            startActivity(new Intent(this, RemoveAdsActivity.class));
        }




        this.b.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext() , mrfragm.class));
            }
        });


        this.a.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                build.Companion.showInters(mm.this, n.class, true);
            }
        });

        this.h.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                final Dialog dialog = new Dialog(mm.this,android.R.style.Theme_Black_NoTitleBar_Fullscreen);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setCancelable(false);
                dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                dialog.setContentView(R.layout.rd_);

                AppCompatButton submit = dialog.findViewById(R.id.submit);
                RatingBar ratingBar = dialog.findViewById(R.id.ratingbar);
                TextView tvLater = dialog.findViewById(R.id.tvLater);
                AppCompatImageView ivClose = dialog.findViewById(R.id.ivClose);

                tvLater.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                ivClose.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });



                submit.setOnClickListener(v1 -> {
                    if (ratingBar.getRating() <4) {
                        Toast.makeText(mm.this, "Thanks for your valuable rating!!", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        try{
                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id="+getPackageName())));
                        }
                        catch (ActivityNotFoundException e){
                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id="+getPackageName())));
                        }
                    }
                    dialog.dismiss();
                });

                dialog.show();

            }


        });

        this.d.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                String shareBody = "Alphabet Lore Soundboard Game";
                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Hey Check out this awesome Alphabet lore Game");
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
                startActivity(Intent.createChooser(sharingIntent, "Share via"));            }
        });


    }



    private void inAppUpdate() {
        AppUpdateManager appUpdateManager = AppUpdateManagerFactory.create(this);
        Task<AppUpdateInfo> appUpdateInfoTask = appUpdateManager.getAppUpdateInfo();
        appUpdateInfoTask.addOnSuccessListener(appUpdateInfo -> {
            if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
                    && appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)) {
                try {
                    appUpdateManager.startUpdateFlowForResult(
                            appUpdateInfo,
                            AppUpdateType.IMMEDIATE,
                            this,
                            190);
                } catch (IntentSender.SendIntentException e) {
                    e.printStackTrace();
                }
            }
        });
    }



    private void buildInterstitial(){
        interstitialPromote = new InterstitialPromote(mm.this);
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


    private void buildBanner(){
        bannerPromote = findViewById(R.id.banner_view);
        bannerPromote.setOnBannerListener(new OnBannerListener() {
            @Override
            public void onBannerAdLoaded() {
                com.alphabetlore3d.simsoundboard.h.setLog("banner loaded.");
            }

            @Override
            public void onBannerAdClicked() {
                com.alphabetlore3d.simsoundboard.h.setLog("banner clicked.");

            }

            @Override
            public void onBannerAdFailedToLoad(String error) {
                com.alphabetlore3d.simsoundboard.h.setLog("banner failed to load : " + error);

            }
        });

    }



    public void onResume() {
        super.onResume();
    }


}



