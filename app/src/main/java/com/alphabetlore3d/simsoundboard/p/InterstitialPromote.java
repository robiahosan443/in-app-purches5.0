package com.alphabetlore3d.simsoundboard.p;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewbinding.BuildConfig;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.alphabetlore3d.simsoundboard.R;
import com.alphabetlore3d.simsoundboard.p.Config.AppsConfig;
import com.alphabetlore3d.simsoundboard.p.Helper.DatabaseHelper;
import com.alphabetlore3d.simsoundboard.p.Interfaces.OnAdClosed;
import com.alphabetlore3d.simsoundboard.p.Interfaces.OnInterstitialAdListener;
import com.alphabetlore3d.simsoundboard.p.Models.AppModels;
import com.alphabetlore3d.simsoundboard.pref.SpManager;

import java.util.ArrayList;
import java.util.Random;


public class InterstitialPromote extends Dialog {


    private int attrRadiusButton = 20; //default color
    private int attrInstallColor = Color.parseColor("#2196F3"); //default color
    private String attrInstallTitle = "Install"; //default title


    private final Activity activity;
    private RelativeLayout close;
    private TextView closeCount, closeText;


    private RelativeLayout install;
    private TextView installTitle, ad;
    private RelativeLayout previewProgress;
    private CountDownTimer countDownTimer;
    private boolean isTimer;
    private int timer = 0;

    private ImageView preview;
    private OnInterstitialAdListener onInterstitialAdListener;
    private OnAdClosed onAdClosed;

    private ArrayList<AppModels> adListInters = new ArrayList<>();


    //default style :
    public InterstitialPromote(@NonNull Activity activity) {
        super(activity, R.style.InterstitialStyle);
        this.activity = activity;
        setContentView(R.layout.pr_it);
        getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        setCancelable(false);
        initializeData();
        initializeUI();
    }

    private void initializeData() {
        DatabaseHelper databaseHelper = new DatabaseHelper(activity.getApplicationContext());
        adListInters = databaseHelper.getAllIntersApps();
        //initialize the screen shot
        onLoadAdListener();
    }

    private void onLoadAdListener() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (adListInters != null && !adListInters.isEmpty()) {

                    if (onInterstitialAdListener != null) {
                        onInterstitialAdListener.onInterstitialAdLoaded();
                    }
                } else {
                    if (onInterstitialAdListener != null) {
                        onInterstitialAdListener.onInterstitialAdFailedToLoad("Ad Loaded, but data base of ad wrong ! please check your file.");
                    }
                }
            }
        }, 500);
    }


    private void initializeUI() {
        close = findViewById(R.id.interstitial_close);
        closeCount = findViewById(R.id.closeCount);
        closeText = findViewById(R.id.closeText);
        install = findViewById(R.id.interstitial_install);
        installTitle = findViewById(R.id.interstitial_install_txt);
        ad = findViewById(R.id.ad);


        previewProgress = (RelativeLayout) findViewById(R.id.interstitial_preview_progress);

        //change data :
        preview = findViewById(R.id.interstitial_preview);

        generateInstallButton();
        updateInstallTitle();
    }

    private void startCountDown() {
        if (timer != 0) {
            close.setClickable(false);
            closeText.setAlpha(0.5f);
            closeCount.setVisibility(View.VISIBLE);
            countDownTimer = new CountDownTimer(1000L * timer, 1000) {
                public void onTick(long j) {
                    String count = String.valueOf(j / 1000);
                    closeCount.setText(count);
                    isTimer = true;
                }

                public void onFinish() {
                    isTimer = false;
                    //closeCount.setText(" ");
                    closeCount.setVisibility(View.INVISIBLE);
                    closeText.setAlpha(1f);
                    close.setClickable(true);
                    timerCancel();
                }
            }.start();
        }
    }

    private void applyStyle(int style) {
        switch (style) {
            case 1: //style 1 : Advances
                setContentView(R.layout.pr_it);
                break;
            case 2: //style 2 : Standard
                setContentView(R.layout.pr_it);
                break;

            default:
                setContentView(R.layout.pr_it);
                break;

        }
    }


    private void timerCancel() {
        try {
            if (isTimer) {
                if (countDownTimer != null) {
                    countDownTimer.cancel();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    //check the Ad List is not empty
    public boolean isAdLoaded() {
        if (adListInters != null) {
            return !adListInters.isEmpty();
        }
        //the ad list is empty.
        if (onInterstitialAdListener != null) {
            onInterstitialAdListener.onInterstitialAdFailedToLoad("Failed to show : No Ad");
        }
        return false;

    }

    @Override
    public void show() {
        boolean isShow = SpManager.INSTANCE.getSpBoolean(SpManager.SP_SHOW_CUSTOM_INTERS, true);
        if (isShow) {
            super.show();
            SpManager.INSTANCE.saveSPBoolean(SpManager.SP_SHOW_CUSTOM_INTERS, false);
            buildInterstitial();
         }

           }

    private void dismissAd() {
        try {
            if (isShowing()) {
                if (onAdClosed != null) {
                    onAdClosed.onAdClosed();
                }
                dismiss();
                if (BuildConfig.DEBUG) {
                    AppsConfig.setLog("The Interstitial was dismiss because something wrong !");
                }
            }
        } catch (Exception e) {
            cancel();
            if (BuildConfig.DEBUG) {
                AppsConfig.setLog("Interstitial Dismiss methode caused a crush : " + e.getMessage());
            }
        }
    }

    private void updateInstallTitle() {
        installTitle.setText(attrInstallTitle);
    }






    private void buildInterstitial() {

        try {

            if (adListInters != null && !adListInters.isEmpty()) {
                int size = adListInters.size();
                Random random = new Random();
                int interstitialIndex = random.nextInt(size);
                updateInterstitial(interstitialIndex);
                startCountDown();
                if (BuildConfig.DEBUG) {
                    AppsConfig.setLog("Build Interstitial...");
                }
            } else {
                //the ad list is empty.
                if (onInterstitialAdListener != null) {
                    onInterstitialAdListener.onInterstitialAdFailedToLoad("The Ad list is empty ! please check your json file.");
                }
                if (BuildConfig.DEBUG) {
                    AppsConfig.setLog("Failed to build Interstitial cause : the List of ad is empty, please check your connection first, than check your file json.");
                }

                dismissAd();
            }

        } catch (Exception e) {
            if (BuildConfig.DEBUG) {
                AppsConfig.setLog("Failed to build Interstitial cause : " + e.getMessage());
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private void updateInterstitial(int index) {

        String data_name = adListInters.get(index).getName();
        String data_icons = adListInters.get(index).getIcons();
        String data_preview = adListInters.get(index).getAppPreview();
        String data_packageName = adListInters.get(index).getPackageName();
        String data_description = adListInters.get(index).getShortDescription();

        loadPreview(data_preview);





        install.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onInterstitialAdListener != null) {
                    onInterstitialAdListener.onInterstitialAdClicked();
                }
                dismiss();
                AppsConfig.openAdLink(activity.getApplicationContext(), data_packageName, data_name);
                if (BuildConfig.DEBUG) {
                    AppsConfig.setLog("Interstitial Clicked.");
                }
            }
        });

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onInterstitialAdListener != null) {
                    onInterstitialAdListener.onInterstitialAdClosed();
                }
                if (BuildConfig.DEBUG) {
                    AppsConfig.setLog("Interstitial Closed.");
                }
                dismissAd();


            }
        });

    }


    private void loadPreview(String previewIcon) {
        previewProgress.setVisibility(View.VISIBLE);
        Glide.with(activity.getApplicationContext()).load(previewIcon)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        if (BuildConfig.DEBUG) {
                            AppsConfig.setLog("Interstitial preview failed to load : " + e);
                        }
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        previewProgress.setVisibility(View.GONE);
                        if (BuildConfig.DEBUG) {
                            AppsConfig.setLog("Interstitial preview loaded.");
                        }
                        return false;
                    }
                })
                .into(preview);
    }


    private void generateInstallButton() {

        GradientDrawable gradientDrawable = new GradientDrawable();
        gradientDrawable.setShape(GradientDrawable.RECTANGLE);
        gradientDrawable.setColor(attrInstallColor);
        ad.setBackgroundColor(attrInstallColor);



        //corner of button shape
        gradientDrawable.setCornerRadii(new float[]{
                attrRadiusButton, attrRadiusButton, // Top Left
                attrRadiusButton, attrRadiusButton, // Top Right
                attrRadiusButton, attrRadiusButton, // Bottom Right
                attrRadiusButton, attrRadiusButton});//Bottom left
        install.setBackground(gradientDrawable);
        generateInstallClose();
    }

    private void generateInstallClose() {

        GradientDrawable gradientDrawable = new GradientDrawable();
        gradientDrawable.setShape(GradientDrawable.RECTANGLE);
        gradientDrawable.setColor(Color.parseColor("#E4E4E4"));
        //corner of button shape
        gradientDrawable.setCornerRadii(new float[]{
                attrRadiusButton, attrRadiusButton, // Top Left
                attrRadiusButton, attrRadiusButton, // Top Right
                attrRadiusButton, attrRadiusButton, // Bottom Right
                attrRadiusButton, attrRadiusButton});//Bottom left
        close.setBackground(gradientDrawable);
    }

    public void setTimer(int timer) {
        this.timer = timer;
    }

    public void setStyle(int style) {
        applyStyle(style);
        initializeUI();
    }

    public void setRadiusButton(int radius) {
        this.attrRadiusButton = radius;
        generateInstallButton();
    }

    public void setInstallTitle(String title) {
        this.attrInstallTitle = title;
        updateInstallTitle();
    }


    public void setInstallColor(int color) {
        try {
            this.attrInstallColor = activity.getResources().getColor(color);
            generateInstallButton();
        } catch (Exception e) {
            if (BuildConfig.DEBUG) {
                AppsConfig.setLog("setInstallColor : Color value wrong, failed to get color from resource");
            }
        }
    }

    public void setInstallColor(String color) {
        try {
            if (color.startsWith("#")) {
                this.attrInstallColor = Color.parseColor(color);
                generateInstallButton();
            } else {
                if (BuildConfig.DEBUG) {
                    AppsConfig.setLog("The value of color wrong : forget the symbol #");
                }
            }
        } catch (Exception e) {
            if (BuildConfig.DEBUG) {
                AppsConfig.setLog("setInstallColor : Color value wrong, failed to get color from string");
            }
        }

    }


    public void setOnInterstitialAdListener(OnInterstitialAdListener onInterstitialAdListener) {
        this.onInterstitialAdListener = onInterstitialAdListener;
    }

    public void setOnAdClosed(OnAdClosed onAdClosed) {
        this.onAdClosed = onAdClosed;
    }
}
