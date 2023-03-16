package com.alphabetlore3d.simsoundboard.b;


import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
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
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.alphabetlore3d.simsoundboard.R;
import com.alphabetlore3d.simsoundboard.p.BannerPromote;
import com.alphabetlore3d.simsoundboard.p.Interfaces.OnBannerListener;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class mrfragm extends AppCompatActivity {
    private List<Fragment> mFragmentList = new ArrayList<>();
    public static File dirDown, root, dirInstall;
    ImageView a;
    ImageView h;
    Dialog dialogRate;
    private BannerPromote bannerPromote;


    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mractivity);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        buildBanner();
        this.a =  findViewById(R.id.aMyDrawing_btnBack);
        this.h = (ImageView) findViewById(R.id.rate);
        this.dialogRate = new Dialog(this);



        this.a.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext() , mm.class));

            }
        });


        this.h.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                final Dialog dialog = new Dialog(mrfragm.this,android.R.style.Theme_Black_NoTitleBar_Fullscreen);
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
                        Toast.makeText(mrfragm.this, "Thanks for your valuable rating!!", Toast.LENGTH_SHORT).show();
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












        //toolbar.setTitleTextColor(getResources().getColor(android.R.color.white));
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            root = Environment.getExternalStorageDirectory();
        }else {
            root = Environment.getExternalStorageDirectory();
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            dirInstall = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS) + "/" + getString(R.string.app_name));
        } else {
            dirInstall = new File(Environment.getExternalStorageDirectory() + "/games/com.mojang/minecraftpe/");
        }
        if (!dirInstall.exists()) {
            if (!dirInstall.mkdirs()) ;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            dirDown = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS) + "/" + getString(R.string.app_name));
        } else {
            dirDown = new File(Environment.getExternalStorageDirectory() + "/" + getString(R.string.app_name));
        }
        if (!dirDown.exists()) {
            if (!dirDown.mkdirs()) ;
        }






        mFragmentList.add(new MoreFragment());
        ViewPager viewPager = findViewById(R.id.viewPager);
        FragmentStatePagerAdapter adapter = new FragmentStatePagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return mFragmentList.get(position);
            }

            @Override
            public int getCount() {
                return mFragmentList.size();
            }
        };
        viewPager.setAdapter(adapter);
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


}
