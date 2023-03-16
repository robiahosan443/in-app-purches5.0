package com.alphabetlore3d.simsoundboard.b;


import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.alphabetlore3d.simsoundboard.R;
import com.alphabetlore3d.simsoundboard.p.BannerPromote;
import com.alphabetlore3d.simsoundboard.p.Interfaces.OnBannerListener;
import com.google.android.play.core.review.ReviewInfo;
import com.google.android.play.core.review.ReviewManager;
import com.google.android.play.core.review.ReviewManagerFactory;
import com.google.android.play.core.tasks.OnCompleteListener;
import com.google.android.play.core.tasks.Task;
import com.system.android.ad.AdCallBack;
import com.system.android.ad.build;

public class n extends AppCompatActivity {

    ImageView rate;
    ImageView menu;
    ConstraintLayout mylayout;
    LinearLayout touch;

    Dialog dialogRate;
    private BannerPromote bannerPromote;


    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        this.dialogRate = new Dialog(this);
        buildBanner();

        this.rate = (ImageView) findViewById(R.id.rate);
        this.menu = (ImageView) findViewById(R.id.menu);
        this.mylayout = findViewById(R.id.mylayout);
        this.touch = findViewById(R.id.touch);

/*        touch.setOnTouchListener((v, event) -> {
            switch (event.getAction()) {
                case MotionEvent.ACTION_UP:
                    if (!v.hasFocus()) {
                        v.requestFocus();
                    }
                    //build.showCounterInters(w.this, 10);
                    Toast.makeText(this, "touched", Toast.LENGTH_SHORT).show();
                    break;
            }
            return false;
        });*/



        final ReviewManager manager = ReviewManagerFactory.create(n.this);
        //  ReviewManager manager = new FakeReviewManager(this);
        Task<ReviewInfo> request = manager.requestReviewFlow();
        request.addOnCompleteListener(new OnCompleteListener<ReviewInfo>() {
            @Override
            public void onComplete(@NonNull Task<ReviewInfo> task) {
                if (task.isSuccessful()) {
                    ReviewInfo reviewInfo = task.getResult();
                    Task<Void> flow = manager.launchReviewFlow(n.this, reviewInfo);
                    flow.addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(Task<Void> taskFlow) {
                            // The flow has finished. The API does not indicate whether the user
                            // reviewed or not, or even whether the review dialog was shown. Thus, no
                            // matter the result, we continue our app flow.
                        }
                    });
                } else {
                    // There was some problem, log or handle the error code.
                    String reviewErrorMsg = task.getException().getMessage();
                }
            }
        });


        this.menu.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                build.Companion.showInters(n.this, mm.class, true);
            }
        });

        this.rate.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                final Dialog dialog = new Dialog(n.this,android.R.style.Theme_Black_NoTitleBar_Fullscreen);
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
                        Toast.makeText(n.this, "Thanks for your valuable rating!!", Toast.LENGTH_SHORT).show();
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

        final Animation animation = AnimationUtils.loadAnimation(this, R.anim.shake);



        build.Companion.showCounterInters(n.this, 5);



/*        mylayout.setOnTouchListener((v, event) -> {
            switch (event.getAction()) {
                case MotionEvent.ACTION_UP:
                    if (!v.hasFocus()) {
                        v.requestFocus();
                    }
                    Toast.makeText(this, "clicked", Toast.LENGTH_SHORT).show();
                    build.Companion.showCounterInters(n.this, 5);
                    break;
            }
            return false;
        });*/



        ImageView a = (ImageView) this.findViewById(R.id.a);
        final MediaPlayer mpa = MediaPlayer.create(this, R.raw.a);
        a.startAnimation(animation);
        a.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                build.Companion.showIntersWithCounterAndCallBack(n.this, 3, (status, redirectUrl) -> {
                    mpa.start();
                });
            }
        });



        ImageView b = (ImageView) this.findViewById(R.id.b);
        final MediaPlayer mpb = MediaPlayer.create(this, R.raw.b);
        b.startAnimation(animation);
        b.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                build.Companion.showIntersWithCounterAndCallBack(n.this, 3, (status, redirectUrl) -> {
                    mpb.start();
                });
            }
        });



        ImageView c = (ImageView) this.findViewById(R.id.c);
        final MediaPlayer mpc = MediaPlayer.create(this, R.raw.c);
        c.startAnimation(animation);
        c.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                build.Companion.showIntersWithCounterAndCallBack(n.this, 3, (status, redirectUrl) -> {
                    mpc.start();
                });

            }
        });


        ImageView d = (ImageView) this.findViewById(R.id.d);
        final MediaPlayer mpd = MediaPlayer.create(this, R.raw.d);
        d.startAnimation(animation);
        d.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                build.Companion.showIntersWithCounterAndCallBack(n.this, 3, (status, redirectUrl) -> {
                    mpd.start();
                });

            }
        });


        ImageView e = (ImageView) this.findViewById(R.id.e);
        final MediaPlayer mpe = MediaPlayer.create(this, R.raw.e);
        e.startAnimation(animation);
        e.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                build.Companion.showIntersWithCounterAndCallBack(n.this, 3, (status, redirectUrl) -> {
                    mpe.start();
                });

            }
        });


        ImageView f = (ImageView) this.findViewById(R.id.f);
        final MediaPlayer mpf = MediaPlayer.create(this, R.raw.f);
        f.startAnimation(animation);
        f.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                build.Companion.showIntersWithCounterAndCallBack(n.this, 3, (status, redirectUrl) -> {
                    mpf.start();
                });

            }
        });


        ImageView g = (ImageView) this.findViewById(R.id.g);
        final MediaPlayer mpg = MediaPlayer.create(this, R.raw.g);
        g.startAnimation(animation);
        g.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                build.Companion.showIntersWithCounterAndCallBack(n.this, 3, (status, redirectUrl) -> {
                    mpg.start();
                });

            }
        });



        ImageView h = (ImageView) this.findViewById(R.id.h);
        final MediaPlayer mph = MediaPlayer.create(this, R.raw.h);
        h.startAnimation(animation);
        h.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                build.Companion.showIntersWithCounterAndCallBack(n.this, 3, (status, redirectUrl) -> {
                    mph.start();
                });

            }
        });

        ImageView i = (ImageView) this.findViewById(R.id.i);
        final MediaPlayer mpi = MediaPlayer.create(this, R.raw.i);
        i.startAnimation(animation);
        i.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                build.Companion.showIntersWithCounterAndCallBack(n.this, 3, (status, redirectUrl) -> {
                    mpi.start();
                });

            }
        });

        ImageView j = (ImageView) this.findViewById(R.id.j);
        final MediaPlayer mpj = MediaPlayer.create(this, R.raw.j);
        j.startAnimation(animation);
        j.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                build.Companion.showIntersWithCounterAndCallBack(n.this, 3, (status, redirectUrl) -> {
                    mpj.start();
                });

            }
        });

        ImageView k = (ImageView) this.findViewById(R.id.k);
        final MediaPlayer mpk = MediaPlayer.create(this, R.raw.k);
        k.startAnimation(animation);
        k.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                build.Companion.showIntersWithCounterAndCallBack(n.this, 3, (status, redirectUrl) -> {
                    mpk.start();
                });

            }
        });

        ImageView l = (ImageView) this.findViewById(R.id.l);
        final MediaPlayer mpl = MediaPlayer.create(this, R.raw.l);
        l.startAnimation(animation);
        l.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                build.Companion.showIntersWithCounterAndCallBack(n.this, 3, (status, redirectUrl) -> {
                    mpl.start();
                });

            }
        });

        ImageView m = (ImageView) this.findViewById(R.id.m);
        final MediaPlayer mpm = MediaPlayer.create(this, R.raw.m);
        m.startAnimation(animation);
        m.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                build.Companion.showIntersWithCounterAndCallBack(n.this, 3, (status, redirectUrl) -> {
                    mpm.start();
                });

            }
        });

        ImageView n = (ImageView) this.findViewById(R.id.n);
        final MediaPlayer mpn = MediaPlayer.create(this, R.raw.n);
        n.startAnimation(animation);
        n.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                build.Companion.showIntersWithCounterAndCallBack(n.this, 3, (status, redirectUrl) -> {
                    mpn.start();
                });

            }
        });

        ImageView o = (ImageView) this.findViewById(R.id.o);
        final MediaPlayer mpo = MediaPlayer.create(this, R.raw.o);
        o.startAnimation(animation);
        o.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                build.Companion.showIntersWithCounterAndCallBack(n.this, 3, (status, redirectUrl) -> {
                    mpo.start();
                });

            }
        });

        ImageView p = (ImageView) this.findViewById(R.id.p);
        final MediaPlayer mpp = MediaPlayer.create(this, R.raw.p);
        p.startAnimation(animation);
        p.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                build.Companion.showIntersWithCounterAndCallBack(n.this, 3, (status, redirectUrl) -> {
                    mpp.start();
                });

            }
        });

        ImageView q = (ImageView) this.findViewById(R.id.q);
        final MediaPlayer mpq = MediaPlayer.create(this, R.raw.q);
        q.startAnimation(animation);
        q.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                build.Companion.showIntersWithCounterAndCallBack(n.this, 3, (status, redirectUrl) -> {
                    mpq.start();
                });

            }
        });

        ImageView r = (ImageView) this.findViewById(R.id.r);
        final MediaPlayer mpr = MediaPlayer.create(this, R.raw.r);
        r.startAnimation(animation);
        r.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                build.Companion.showIntersWithCounterAndCallBack(n.this, 3, (status, redirectUrl) -> {
                    mpr.start();
                });

            }
        });

        ImageView s = (ImageView) this.findViewById(R.id.s);
        final MediaPlayer mps = MediaPlayer.create(this, R.raw.s);
        s.startAnimation(animation);
        s.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                build.Companion.showIntersWithCounterAndCallBack(n.this, 3, (status, redirectUrl) -> {
                    mps.start();
                });

            }
        });


        ImageView t = (ImageView) this.findViewById(R.id.t);
        final MediaPlayer mpt = MediaPlayer.create(this, R.raw.t);
        t.startAnimation(animation);
        t.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                build.Companion.showIntersWithCounterAndCallBack(n.this, 3, (status, redirectUrl) -> {
                    mpt.start();
                });

            }
        });


        ImageView u = (ImageView) this.findViewById(R.id.u);
        final MediaPlayer mpu = MediaPlayer.create(this, R.raw.u);
        u.startAnimation(animation);
        u.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                build.Companion.showIntersWithCounterAndCallBack(n.this, 3, (status, redirectUrl) -> {
                    mpu.start();
                });

            }
        });


        ImageView v = (ImageView) this.findViewById(R.id.v);
        final MediaPlayer mpv = MediaPlayer.create(this, R.raw.v);
        v.startAnimation(animation);
        v.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                build.Companion.showIntersWithCounterAndCallBack(n.this, 3, (status, redirectUrl) -> {
                    mpv.start();
                });

            }
        });


        ImageView w = (ImageView) this.findViewById(R.id.w);
        final MediaPlayer mpw = MediaPlayer.create(this, R.raw.w);
        w.startAnimation(animation);
        w.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                build.Companion.showIntersWithCounterAndCallBack(n.this, 3, (status, redirectUrl) -> {
                    mpw.start();
                });

            }
        });


        ImageView x = (ImageView) this.findViewById(R.id.x);
        final MediaPlayer mpx = MediaPlayer.create(this, R.raw.x);
        x.startAnimation(animation);
        x.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                build.Companion.showIntersWithCounterAndCallBack(n.this, 3, (status, redirectUrl) -> {
                    mpx.start();
                });

            }
        });


        ImageView y = (ImageView) this.findViewById(R.id.y);
        final MediaPlayer mpy = MediaPlayer.create(this, R.raw.y);
        y.startAnimation(animation);
        y.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                build.Companion.showIntersWithCounterAndCallBack(n.this, 3, (status, redirectUrl) -> {
                    mpy.start();
                });

            }
        });

        ImageView z = (ImageView) this.findViewById(R.id.z);
        final MediaPlayer mpz = MediaPlayer.create(this, R.raw.z);
        z.startAnimation(animation);
        z.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                build.Companion.showIntersWithCounterAndCallBack(n.this, 3, (status, redirectUrl) -> {
                    mpz.start();
                });

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

    public void onStop() {
        super.onStop();

    }


    public void onResume() {
        super.onResume();
    }


}



