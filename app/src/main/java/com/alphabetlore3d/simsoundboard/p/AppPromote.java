package com.alphabetlore3d.simsoundboard.p;

import android.content.Context;
import android.text.TextUtils;

import com.alphabetlore3d.simsoundboard.p.ConnectionAd.ConnectionBannerPromote;
import com.alphabetlore3d.simsoundboard.p.ConnectionAd.ConnectionIntersPromote;
import com.alphabetlore3d.simsoundboard.p.Interfaces.OnConnectedListener;
import com.alphabetlore3d.simsoundboard.p.Interfaces.OnPromoteListener;


public class AppPromote {

    public static OnPromoteListener onPromoteListener ;


    public static void initializeBannerPromote(Context context,String AdLink){
        if (!TextUtils.isEmpty(AdLink)){

            ConnectionBannerPromote connectionBannerPromote = new ConnectionBannerPromote(context,AdLink);
            connectionBannerPromote.setOnPromoteConnected(new OnConnectedListener() {
                @Override
                public void onAppConnected() {
                    if (onPromoteListener != null){
                        onPromoteListener.onInitializeSuccessful();
                    }
                }

                @Override
                public void onAppFailed(String error) {
                    if (onPromoteListener != null){
                        onPromoteListener.onInitializeFailed(error);
                    }
                }
            });
        }else {
            if (onPromoteListener != null){
                onPromoteListener.onInitializeFailed("initializePromote failed cause : link is empty.");
            }

        }

   }

    public static void initializeIntersPromote(Context context,String AdLink){
        if (!TextUtils.isEmpty(AdLink)){

            ConnectionIntersPromote connectionIntersPromote = new ConnectionIntersPromote(context,AdLink);
            connectionIntersPromote.setOnPromoteConnected(new OnConnectedListener() {
                @Override
                public void onAppConnected() {
                    if (onPromoteListener != null){
                        onPromoteListener.onInitializeSuccessful();
                    }
                }

                @Override
                public void onAppFailed(String error) {
                    if (onPromoteListener != null){
                        onPromoteListener.onInitializeFailed(error);
                    }
                }
            });
        }else {
            if (onPromoteListener != null){
                onPromoteListener.onInitializeFailed("initializePromote failed cause : link is empty.");
            }

        }

    }

//    public static void initializePromote(Activity activity, String AdLink){
//        if (!TextUtils.isEmpty(AdLink)){
//
//            ConnectionBannerPromote connectionBannerPromote = new ConnectionBannerPromote(activity,AdLink);
//            connectionBannerPromote.setOnPromoteConnected(new OnConnectedListener() {
//                @Override
//                public void onAppConnected() {
//                    if (onPromoteListener != null){
//                        onPromoteListener.onInitializeSuccessful();
//                    }
//                }
//
//                @Override
//                public void onAppFailed(String error) {
//                    if (onPromoteListener != null){
//                        onPromoteListener.onInitializeFailed(error);
//                    }
//                }
//            });
//        }else {
//            if (onPromoteListener != null){
//                onPromoteListener.onInitializeFailed("initializePromote failed cause : link is empty.");
//            }
//
//        }
//
//    }


    public static void setOnPromoteListener(OnPromoteListener onPromoteListener) {
        AppPromote.onPromoteListener = onPromoteListener;
    }
}
