package com.alphabetlore3d.simsoundboard.p.Config;

import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.alphabetlore3d.simsoundboard.w.w;

import java.util.List;


public class AppsConfig {

    public static String appPromote = "AppsPromote";
    public static String appName = "name";
    public static String appIcons = "icon";
    public static String appShortDescription = "shortDescription";
    public static String appPackage = "packageName";
    public static String appPreview = "preview";
    public static String screenShot = "screenShot";


    public static String[] downloadCount = new String[]{
            "100 K",
            "500 K",
            "10 K",
            "50 K"};

    public static float[] ratingCount = new float[]{
            4.4f,
            4.5f,
            4.6f,
            4.7f,
            4.8f,
            4.9f,
            5.0f,
            };

    public static void openAdLink(Context context, String link, String title) {

        if (!TextUtils.isEmpty(link) && link != null) {

            if (link.contains("http://") || link.contains("https://")) {
                //new FinestWebView.Builder(context).show(link);
                Intent i = new Intent(context, w.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                i.putExtra("url", link);
                i.putExtra("title", title);
                context.startActivity(i);
            }
            else openOnGooglePlayStore(context, link);

//            if (link.startsWith("http")) {
//                //open in internet app :
//                openOnInternet(context, link);
//            } else {
//                // open app with his package name in google play store.
//                openOnGooglePlayStore(context, link);
//            }

        } else {
            Toast.makeText(context, "Failed to get Ad link.", Toast.LENGTH_SHORT).show();
        }
    }

    public static void openOnGooglePlayStore(Context context, String packageName) {
        try {
            String GGMarket = "market://details?id=";
            Intent intentMarket = new Intent(Intent.ACTION_VIEW, Uri.parse(GGMarket + packageName));
            intentMarket.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intentMarket.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            try {
                intentMarket.setPackage("com.android.vending");
            } catch (Exception v) {
                v.printStackTrace();
            }
            context.startActivity(intentMarket);
        } catch (ActivityNotFoundException localActivityNotFoundException) {
            String GG_APPS = "https://play.google.com/store/apps/details?id=";
            Intent intentStore = new Intent(Intent.ACTION_VIEW, Uri.parse(GG_APPS + packageName));
            intentStore.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intentStore.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            try {
                intentStore.setPackage("com.android.vending");
            } catch (Exception v) {
                v.printStackTrace();
            }
            if (isIntentAvailable(context, intentStore)) {
                context.startActivity(intentStore);
            } else {
                Toast.makeText(context, "Failed to open Ad.", Toast.LENGTH_SHORT).show();
            }

        }
    }

    public static void openOnInternet(Context context, String link) {
        try {
            Intent intentMarket = new Intent(Intent.ACTION_VIEW, Uri.parse(link));
            intentMarket.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intentMarket.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            context.startActivity(intentMarket);
        } catch (ActivityNotFoundException localActivityNotFoundException) {
            Toast.makeText(context, "Failed to open Ad.", Toast.LENGTH_SHORT).show();

        }
    }


    public static boolean isIntentAvailable(Context context, Intent intent) {
        final PackageManager mgr = context.getPackageManager();
        @SuppressLint("QueryPermissionsNeeded")
        List<ResolveInfo> list = mgr.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
        return list.size() > 0;
    }

    public static void setLog(String log) {
        Log.d("adPromote", log);
    }



}
