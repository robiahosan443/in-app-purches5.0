package com.system.android.ad

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.NonNull
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.applovin.mediation.*
import com.applovin.mediation.ads.MaxAdView
import com.applovin.mediation.ads.MaxInterstitialAd
import com.applovin.mediation.ads.MaxRewardedAd
import com.applovin.mediation.nativeAds.MaxNativeAdListener
import com.applovin.mediation.nativeAds.MaxNativeAdLoader
import com.applovin.mediation.nativeAds.MaxNativeAdView
import com.applovin.mediation.nativeAds.MaxNativeAdViewBinder
import com.applovin.sdk.AppLovinSdk
import com.applovin.sdk.AppLovinSdk.SdkInitializationListener
import com.applovin.sdk.AppLovinSdkConfiguration
import com.facebook.ads.*
import com.facebook.ads.AdSize
import com.google.android.gms.ads.*
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.google.android.gms.ads.nativead.MediaView
import com.google.android.gms.ads.nativead.NativeAd
import com.google.android.gms.ads.nativead.NativeAdOptions
import com.google.android.gms.ads.nativead.NativeAdView
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback
import com.ironsource.mediationsdk.ISBannerSize
import com.ironsource.mediationsdk.IronSource
import com.ironsource.mediationsdk.logger.IronSourceError
import com.ironsource.mediationsdk.sdk.BannerListener
import com.ironsource.mediationsdk.sdk.InterstitialListener
import com.unity3d.ads.IUnityAdsShowListener
import com.unity3d.ads.UnityAds
import com.unity3d.services.banners.BannerErrorInfo
import com.unity3d.services.banners.BannerView
import com.unity3d.services.banners.UnityBannerSize
import me.araib.module.ad.R
import org.json.JSONObject
import java.util.*


class build {

    companion object {


        @SuppressLint("StaticFieldLeak")
        private var appOpenManager: AppOpenManager ?= null
        private var applovinAppOpenManager: ApplovinAppOpenManager ?= null
        private const val TAG = "AD_MODULE"
        var admobInterstitialAd: InterstitialAd? = null
        var mRewardedAd: RewardedAd ?= null
        //        var mRewardedAd: RewardedAd? = null
        private var fanInterstitialAd: com.facebook.ads.InterstitialAd? = null
        private var selectedAd: String = ""
        private var admobBannerId: String ?= null
        private var admob_native_ad_id: String ?= null
        private var admob_rewarded_ad_id: String ?= null
        var mFrameLayout: FrameLayout? = null
        var nativeAdLoader: MaxNativeAdLoader? = null
        var loadedNativeAd: MaxAd? = null
        private var fanBannerId: String ?= null
        private var unityGameId: String ?= null
        private var unityTestMode: Boolean = false
        private var unityIntersId: String ?= null
        private var unityBannerId: String ?= null
        var applovin_banner_ad_id = ""
        var applovin_native_ad_id = ""
        var applovin_rewarded_ad_id = ""
        var applovin_inters_id = ""
        var applovin_app_open_ad_id = ""
        private var interval: String = "1"

        fun getAdmobRewardedInstance(): RewardedAd? {
            return mRewardedAd
        }

        fun loadAllAds1(
            context: Context,
            adCallBack: AdCallBack
        ): Boolean {

            val mRequestQueue = Volley.newRequestQueue(context)
            val url = "https://ia902506.us.archive.org/18/items/myadstest_202205/myadstest.json"
            mRequestQueue.cache.invalidate(url, true)

            var status = true

            val mStringRequest = StringRequest(Request.Method.GET, url,
                { response ->
                    try {
                        Log.e(TAG, "response: $response")
                        val jsonObj = JSONObject(response)
                        val ads = jsonObj.getJSONArray("Ads")
                        val adsData = ads.getJSONObject(0)
                        selectedAd = adsData.optString("select_ads").lowercase()
                        val admobIntersId = adsData.optString("admob_interid")
                        val admobAppOpenAdId = adsData.optString("admob_app_open_ad_id")
                        admobBannerId = adsData.optString("admob_bannerid")
                        admob_native_ad_id = adsData.optString("admob_native_ad_id")
                        admob_rewarded_ad_id = adsData.optString("admob_rewarded_ad_id")
                        val fanIntersId = adsData.optString("fan_inter")
                        fanBannerId = adsData.optString("fan_banner")
                        unityGameId = adsData.optString("unityGameID")
                        unityIntersId = adsData.optString("unity_inter")
                        unityBannerId = adsData.optString("unity_banner")
                        applovin_banner_ad_id = adsData.optString("applovin_banner_ad_id")
                        applovin_native_ad_id = adsData.optString("applovin_native_ad_id")
                        applovin_rewarded_ad_id = adsData.optString("applovin_rewarded_ad_id")
                        applovin_inters_id = adsData.optString("applovin_inters_id")
                        applovin_app_open_ad_id = adsData.optString("applovin_app_open_ad_id")
                        interval = adsData.optString("interval")
                        val ironSourceAppKey = adsData.optString("ironsourceAppKey")
                        val statusApp = adsData.optString("STATUS_APP")
                        Log.e(TAG, "statusApp: $statusApp")
                        val linkDirect = adsData.optString("LINK_REDIRECT")


                        if (statusApp.toInt() == 0) {
                            status = true
                            when (selectedAd) {
                                java.AD_TYPE_ADMOB -> {
                                    Log.e(TAG, "Admob init")
                                    initAdmob(context, admobIntersId, admobAppOpenAdId)
                                }
                                java.AD_TYPE_FACEBOOK -> {
                                    initFacebook(context, fanIntersId)
                                }
                                java.AD_TYPE_UNITY -> {
                                    initUnity(context)
                                }
                                java.AD_TYPE_IRON -> {
                                    initIron(context, ironSourceAppKey)
                                }
                                java.AD_TYPE_APPLOVIN -> {
                                    initApplovin(context)
                                }

                                java.AD_TYPE_ADMOB + java.AD_TYPE_FACEBOOK -> {
                                    Log.e(TAG, "selectedAd2TEst: ${java.AD_TYPE_ADMOB + java.AD_TYPE_FACEBOOK}")
                                    val randomNumber = generateRandom(1,2)
                                    Log.e(TAG, "randomNumber: $randomNumber")
                                    initAdmob(context, admobIntersId, admobAppOpenAdId)
                                    initFacebook(context, fanIntersId)

                                }
                                java.AD_TYPE_ADMOB + java.AD_TYPE_UNITY -> {
                                    val randomNumber = generateRandom(1,2)
                                    Log.e(TAG, "randomNumber: $randomNumber")
                                    initAdmob(context, admobIntersId, admobAppOpenAdId)
                                    initUnity(context)

                                }
                                java.AD_TYPE_ADMOB + java.AD_TYPE_IRON -> {
                                    val randomNumber = generateRandom(1,2)
                                    Log.e(TAG, "randomNumber: $randomNumber")
                                    initAdmob(context, admobIntersId, admobAppOpenAdId)
                                    initIron(context, ironSourceAppKey)

                                }
                                java.AD_TYPE_ADMOB + java.AD_TYPE_APPLOVIN -> {
                                    initAdmob(context, admobIntersId, admobAppOpenAdId)
                                    initApplovin(context)
                                }

                                java.AD_TYPE_FACEBOOK + java.AD_TYPE_ADMOB -> {
                                    val randomNumber = generateRandom(1,2)
                                    Log.e(TAG, "randomNumber: $randomNumber")
                                    initFacebook(context, fanIntersId)
                                    initAdmob(context, admobIntersId, admobAppOpenAdId)

                                }
                                java.AD_TYPE_FACEBOOK + java.AD_TYPE_UNITY -> {
                                    val randomNumber = generateRandom(1,2)
                                    Log.e(TAG, "randomNumber: $randomNumber")
                                    initFacebook(context, fanIntersId)
                                    initUnity(context)

                                }
                                java.AD_TYPE_FACEBOOK + java.AD_TYPE_IRON -> {
                                    val randomNumber = generateRandom(1,2)
                                    Log.e(TAG, "randomNumber: $randomNumber")
                                    initFacebook(context, fanIntersId)
                                    initIron(context, ironSourceAppKey)
                                }
                                java.AD_TYPE_FACEBOOK + java.AD_TYPE_APPLOVIN -> {
                                    val randomNumber = generateRandom(1,2)
                                    Log.e(TAG, "randomNumber: $randomNumber")
                                    initFacebook(context, fanIntersId)
                                    initApplovin(context)
                                }

                                java.AD_TYPE_UNITY + java.AD_TYPE_ADMOB -> {
                                    val randomNumber = generateRandom(1,2)
                                    Log.e(TAG, "randomNumber: $randomNumber")
                                    initUnity(context)
                                    initAdmob(context, admobIntersId, admobAppOpenAdId)
                                }
                                java.AD_TYPE_UNITY + java.AD_TYPE_FACEBOOK -> {
                                    val randomNumber = generateRandom(1,2)
                                    Log.e(TAG, "randomNumber: $randomNumber")
                                    initUnity(context)
                                    initFacebook(context, fanIntersId)
                                }
                                java.AD_TYPE_UNITY + java.AD_TYPE_IRON -> {
                                    val randomNumber = generateRandom(1,2)
                                    Log.e(TAG, "randomNumber: $randomNumber")
                                    initUnity(context)
                                    initIron(context, ironSourceAppKey)
                                }
                                java.AD_TYPE_UNITY + java.AD_TYPE_APPLOVIN -> {
                                    val randomNumber = generateRandom(1,2)
                                    Log.e(TAG, "randomNumber: $randomNumber")
                                    initUnity(context)
                                    initApplovin(context)
                                }

                                java.AD_TYPE_IRON + java.AD_TYPE_ADMOB -> {
                                    val randomNumber = generateRandom(1,2)
                                    Log.e(TAG, "randomNumber: $randomNumber")
                                    initIron(context, ironSourceAppKey)
                                    initAdmob(context, admobIntersId, admobAppOpenAdId)
                                }
                                java.AD_TYPE_IRON + java.AD_TYPE_FACEBOOK -> {
                                    val randomNumber = generateRandom(1,2)
                                    Log.e(TAG, "randomNumber: $randomNumber")
                                    initIron(context, ironSourceAppKey)
                                    initFacebook(context, fanIntersId)
                                }
                                java.AD_TYPE_IRON + java.AD_TYPE_UNITY -> {
                                    val randomNumber = generateRandom(1,2)
                                    Log.e(TAG, "randomNumber: $randomNumber")
                                    initIron(context, ironSourceAppKey)
                                    initUnity(context)
                                }
                                java.AD_TYPE_IRON + java.AD_TYPE_APPLOVIN -> {
                                    val randomNumber = generateRandom(1,2)
                                    Log.e(TAG, "randomNumber: $randomNumber")
                                    initIron(context, ironSourceAppKey)
                                    initApplovin(context)
                                }

                                java.AD_TYPE_APPLOVIN + java.AD_TYPE_ADMOB -> {
                                    initApplovin(context)
                                    initAdmob(context, admobIntersId, admobAppOpenAdId)
                                }
                                java.AD_TYPE_APPLOVIN + java.AD_TYPE_FACEBOOK -> {
                                    initApplovin(context)
                                    initFacebook(context, fanIntersId)
                                }
                                java.AD_TYPE_APPLOVIN + java.AD_TYPE_UNITY -> {
                                    initApplovin(context)
                                    initUnity(context)
                                }
                                java.AD_TYPE_APPLOVIN + java.AD_TYPE_IRON -> {
                                    initApplovin(context)
                                    initIron(context, ironSourceAppKey)
                                }



                                java.AD_TYPE_ADMOB + java.AD_TYPE_UNITY + java.AD_TYPE_FACEBOOK-> {
                                    initAdmob(context, admobIntersId, admobAppOpenAdId)
                                    initUnity(context)
                                    initFacebook(context, fanIntersId)
                                }
                                java.AD_TYPE_ADMOB + java.AD_TYPE_UNITY + java.AD_TYPE_IRON-> {
                                    initAdmob(context, admobIntersId, admobAppOpenAdId)
                                    initUnity(context)
                                    initIron(context, ironSourceAppKey)
                                }
                                java.AD_TYPE_ADMOB + java.AD_TYPE_UNITY + java.AD_TYPE_APPLOVIN-> {
                                    initAdmob(context, admobIntersId, admobAppOpenAdId)
                                    initUnity(context)
                                    initApplovin(context)
                                }

                            }

                        }
                        adCallBack.callBack(statusApp.toInt(), linkDirect)

                    }
                    catch (e: Exception) {
                        adCallBack.callBack(0, "")
                    }

                }) { error ->
                Log.i(TAG, "Error :$error")
                adCallBack.callBack(0, "")
            }

            mRequestQueue.add(mStringRequest)

            return status

        }

        fun loadNativeAd(context: Context, frameLayout: FrameLayout) {
            if (selectedAd == java.AD_TYPE_ADMOB) {
                loadAdmobNativeAd(context, frameLayout)
            }
            else if (selectedAd == java.AD_TYPE_APPLOVIN) {
                loadApplovinNative(context as Activity, frameLayout)
            }
        }
        @SuppressLint("MissingPermission")
        fun loadAdmobNativeAd(context: Context, frameLayout: FrameLayout) {
            try {
                val builder = AdLoader.Builder(
                    context,
                    admob_native_ad_id
                )
                builder.forNativeAd { nativeAd: NativeAd ->
                    // Assumes you have a placeholder FrameLayout in your View layout
                    // (with id fl_adplaceholder) where the ad is to be placed.

                    // Assumes that your ad layout is in a file call native_ad_layout.xml
                    // in the res/layout folder
                    @SuppressLint("InflateParams") val adView =
                        (context as Activity).layoutInflater
                            .inflate(
                                R.layout.layout_native_ads,
                                null
                            ) as NativeAdView
                    // This method sets the text, images and the native ad, etc into the ad
                    // view.
                    populateNativeAdView(nativeAd, adView)
                    frameLayout.removeAllViews()
                    frameLayout.addView(adView)
                }
                val videoOptions = VideoOptions.Builder().setStartMuted(true).build()
                val adOptions = NativeAdOptions.Builder().setVideoOptions(videoOptions).build()
                builder.withNativeAdOptions(adOptions)
                val adLoader = builder.withAdListener(object : AdListener() {
                    override fun onAdFailedToLoad(loadAdError: LoadAdError) {
                        super.onAdFailedToLoad(loadAdError)
                        Log.e("adload", "adload faild \$error")
                    }
                }).build()
                adLoader.loadAd(AdRequest.Builder().build())
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        private fun populateNativeAdView(nativeAd: NativeAd, adView: NativeAdView) {
            try {


                // Set the media view.
                adView.setMediaView(adView.findViewById(R.id.ad_media))

                // Set other ad assets.
                adView.setHeadlineView(adView.findViewById(R.id.ad_headline))
                adView.setBodyView(adView.findViewById(R.id.ad_body))
                adView.setCallToActionView(adView.findViewById(R.id.ad_call_to_action))
                adView.setIconView(adView.findViewById(R.id.ad_app_icon))
                adView.setPriceView(adView.findViewById(R.id.ad_price))
                adView.setStarRatingView(adView.findViewById(R.id.ad_stars))
                adView.setStoreView(adView.findViewById(R.id.ad_store))
                adView.setAdvertiserView(adView.findViewById(R.id.ad_advertiser))

                // The headline and media content are guaranteed to be in every UnifiedNativeAd.
                (Objects.requireNonNull<View>(adView.headlineView) as TextView).text =
                    nativeAd.headline
                Objects.requireNonNull<MediaView>(adView.mediaView)
                    .setMediaContent(Objects.requireNonNull<MediaContent>(nativeAd.mediaContent))

                // These assets aren't guaranteed to be in every UnifiedNativeAd, so it's important to
                // check before trying to display them.
                if (nativeAd.body == null) {
                    Objects.requireNonNull<View>(adView.bodyView)
                        .setVisibility(View.INVISIBLE)
                } else {
                    Objects.requireNonNull<View>(adView.bodyView).setVisibility(View.VISIBLE)
                    (adView.bodyView as TextView).text = nativeAd.body
                }
                if (nativeAd.callToAction == null) {
                    Objects.requireNonNull<View>(adView.callToActionView)
                        .setVisibility(View.INVISIBLE)
                } else {
                    Objects.requireNonNull<View>(adView.callToActionView)
                        .setVisibility(View.VISIBLE)
                    (adView.callToActionView as TextView).text = nativeAd.callToAction
                }
                if (nativeAd.icon == null) {
                    Objects.requireNonNull<View>(adView.iconView).setVisibility(View.GONE)
                } else {
                    (Objects.requireNonNull<View>(adView.iconView) as ImageView).setImageDrawable(
                        nativeAd.icon.drawable
                    )
                    adView.iconView.visibility = View.VISIBLE
                }
                if (nativeAd.price == null) {
                    Objects.requireNonNull<View>(adView.priceView)
                        .setVisibility(View.INVISIBLE)
                } else {
                    Objects.requireNonNull<View>(adView.priceView).setVisibility(View.VISIBLE)
                    (adView.priceView as TextView).text = nativeAd.price
                }
                if (nativeAd.store == null) {
                    Objects.requireNonNull<View>(adView.storeView)
                        .setVisibility(View.INVISIBLE)
                } else {
                    Objects.requireNonNull<View>(adView.storeView).setVisibility(View.VISIBLE)
                    (adView.storeView as TextView).text = nativeAd.store
                }
                if (nativeAd.starRating == null) {
                    Objects.requireNonNull<View>(adView.starRatingView)
                        .setVisibility(View.INVISIBLE)
                } else {
                    (Objects.requireNonNull<View>(adView.starRatingView) as RatingBar).rating =
                        nativeAd.starRating.toFloat()
                    adView.starRatingView.visibility = View.VISIBLE
                }
                if (nativeAd.advertiser == null) {
                    Objects.requireNonNull<View>(adView.advertiserView)
                        .setVisibility(View.INVISIBLE)
                } else {
                    (Objects.requireNonNull<View>(adView.advertiserView) as TextView).text =
                        nativeAd.advertiser
                    adView.advertiserView.visibility = View.VISIBLE
                }

                // This method tells the Google Mobile Ads SDK that you have finished populating your
                // native ad view with this native ad.
                adView.setNativeAd(nativeAd)

                // Get the video controller for the ad. One will always be provided, even if the ad doesn't
                // have a video asset.
                val vc = nativeAd.mediaContent.videoController

                // Updates the UI to say whether or not this ad has a video asset.
                if (vc.hasVideoContent()) {
                    // Create a new VideoLifecycleCallbacks object and pass it to the VideoController. The
                    // VideoController will call methods on this object when events occur in the video
                    // lifecycle.
                    vc.setVideoLifecycleCallbacks(object : VideoController.VideoLifecycleCallbacks() {
                        override fun onVideoEnd() {
                            super.onVideoEnd()
                        }
                    })
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        fun loadAdAdmobRewarded(
            activity: Activity
        ) {
            try {
                //mRewardedAd = null
                println("ads working")
                val adRequest = AdRequest.Builder().build()
                if (!activity.isFinishing) {
                    RewardedAd.load(activity,
                        admob_rewarded_ad_id,
                        adRequest,
                        object : RewardedAdLoadCallback() {
                            override fun onAdFailedToLoad(loadAdError: LoadAdError) {
                                // Handle the error.
                                Log.d("TAG", loadAdError.message)
                                mRewardedAd = null
                            }

                            override fun onAdLoaded(rewardedAd: RewardedAd) {
                                mRewardedAd = rewardedAd
                                Log.d("TAG", "Ad was loaded.")
                            }
                        })
                }
            } catch (ignored: Exception) {
            }
        }

        fun loadAdAdmobRewarded(
            activity: Activity,
            fullScreenContentCallback: FullScreenContentCallback?
        ) {
            try {

                println("ads working")
                val adRequest = AdRequest.Builder().build()
                if (!activity.isFinishing) {
                    RewardedAd.load(activity,
                        admob_rewarded_ad_id,
                        adRequest,
                        object : RewardedAdLoadCallback() {
                            override fun onAdFailedToLoad(loadAdError: LoadAdError) {
                                // Handle the error.
                                Log.d("TAG", loadAdError.message)
                                mRewardedAd = null
                            }

                            override fun onAdLoaded(rewardedAd: RewardedAd) {
                                mRewardedAd = rewardedAd
                                Log.d("TAG", "Ad was loaded.")
                                mRewardedAd!!.fullScreenContentCallback = fullScreenContentCallback
                            }
                        })
                }
            } catch (ignored: Exception) {
            }
        }

        fun showAdmobRewarded(
            activity: Activity,
            onUserEarnedRewardListener: OnUserEarnedRewardListener?,
            fullScreenContentCallback: FullScreenContentCallback
        ) {
            try {
                if (mRewardedAd != null && !activity.isFinishing) {
                    mRewardedAd!!.fullScreenContentCallback = fullScreenContentCallback
                    if (onUserEarnedRewardListener != null) {
                        mRewardedAd!!.show(
                            activity,
                            onUserEarnedRewardListener
                        )
                    }
                } else {
                    Log.d("TAG", "The rewarded ad wasn't ready yet.")
                }
            } catch (ignored: Exception) {
            }
        }

        private fun showAdmobRewarded(
            activity: Activity,
            adCallBack: AdCallBack
        ) {
            try {
                if (mRewardedAd != null && !activity.isFinishing) {
                    var count = 1
                    mRewardedAd?.fullScreenContentCallback = object : FullScreenContentCallback() {

                        override fun onAdDismissedFullScreenContent() {
                            super.onAdDismissedFullScreenContent()
                            loadAdAdmobRewarded(activity)
                            if (count == 1) {
                                adCallBack.callBack(0, "")
                            }
                            else {
                                adCallBack.callBack(1, "")
                            }
                        }

                        override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                            super.onAdFailedToShowFullScreenContent(adError)
                        }

                        override fun onAdShowedFullScreenContent() {
                            super.onAdShowedFullScreenContent()
                            Toast.makeText(
                                activity,
                                "You must watch the complete ad to go next activity",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                    mRewardedAd?.show(activity) {
                        count = 2
                    }
                } else {
                    Log.d("TAG", "The rewarded ad wasn't ready yet.")
                    adCallBack.callBack(1, "")
                }
            } catch (ignored: Exception) {
                adCallBack.callBack(1, "")
            }
        }

        private fun initApplovin(context: Context) {
            AppLovinSdk.getInstance(context).mediationProvider = "max"
//            AppLovinSdk.initializeSdk(context) { configuration: AppLovinSdkConfiguration? -> }
            AppLovinSdk.getInstance(context)
                .settings.testDeviceAdvertisingIds =
                listOf(
                    "01472d57-f72e-41a6-9850-6fc0c7d0032b",
                    "d1cf44a5-7561-4e21-967e-e6dbaf6f4a4c",
                    "209e3099-9f99-4d42-8c18-9ead00d69d77",
                    "dca36648-42cc-48c2-a80b-26ff9106ef99"
                )
            AppLovinSdk.initializeSdk(context, object : SdkInitializationListener {
                override fun onSdkInitialized(configuration: AppLovinSdkConfiguration) {
                    applovinAppOpenManager = ApplovinAppOpenManager(context, applovin_app_open_ad_id)
                    loadApplovinInters(context as Activity)
//                    loadApplovinRewarded(context)
                }
            })


        }


        private fun generateRandom(min: Int, max: Int): Int {
            return Random().nextInt(max - min + 1) + min
        }

        private fun initIron(
            context: Context,
            ironSourceAppKey: String
        ) {
            IronSource.setConsent(true)
            IronSource.setMetaData("do_not_sell", "false")
            IronSource.setMetaData("is_child_directed", "false")
            IronSource.init(context as Activity?, ironSourceAppKey)
            IronSource.init(context as Activity?, ironSourceAppKey, IronSource.AD_UNIT.INTERSTITIAL)
            IronSource.init(context as Activity?, ironSourceAppKey, IronSource.AD_UNIT.BANNER)
            IronSource.loadInterstitial()
        }

        private fun initUnity(context: Context) {
            UnityAds.initialize(context, unityGameId, unityTestMode)
        }

        private fun initFacebook(
            context: Context,
            fanIntersId: String
        ) {
            AudienceNetworkAds.initialize(context)
            loadFanInterstitial(context, fanIntersId)
//            loadNativeAd()
        }

        private fun initAdmob(
            context: Context,
            admobIntersId: String,
            admobAppOpenAdId: String
        ) {
            MobileAds.initialize(context) { }
            loadAdmobInterstitial(context, admobIntersId)
            loadAdAdmobRewarded(context as Activity)
            appOpenManager = AppOpenManager(context.applicationContext as Application?, admobAppOpenAdId)
        }



        private fun loadFanInterstitial(context: Context, adId: String) {
            fanInterstitialAd = com.facebook.ads.InterstitialAd(context, adId)
            val interstitialAdListener: com.facebook.ads.InterstitialAdListener = object : com.facebook.ads.InterstitialAdListener {
                override fun onInterstitialDisplayed(ad: Ad) {
                    // Interstitial ad displayed callback
                    Log.e(TAG, "Interstitial ad displayed.")
                }

                override fun onInterstitialDismissed(ad: Ad) {
                    // Interstitial dismissed callback
                    Log.e(TAG, "Interstitial ad dismissed.")
                    loadFanInterstitial(context, adId)
                    moveToNextActivity(context as Activity)
                }


                override fun onError(p0: Ad?, adError: com.facebook.ads.AdError?) {
                    Log.e(TAG, "Interstitial ad failed to load: " + adError?.errorMessage)
                    moveToNextActivity(context as Activity)
                }

                override fun onAdLoaded(ad: Ad) {
                    // Interstitial ad is loaded and ready to be displayed
                    Log.d(TAG, "Interstitial ad is loaded and ready to be displayed!")
                }

                override fun onAdClicked(ad: Ad) {
                    // Ad clicked callback
                    Log.d(TAG, "Interstitial ad clicked!")
                }

                override fun onLoggingImpression(ad: Ad) {
                    // Ad impression logged callback
                    Log.d(TAG, "Interstitial ad impression logged!")
                }
            }

            fanInterstitialAd?.loadAd(
                fanInterstitialAd?.buildLoadAdConfig()
                    ?.withAdListener(interstitialAdListener)
                    ?.build()
            )
        }

        private fun loadAdmobInterstitial(context: Context, adID: String) {
            Log.e(TAG, "Admob inters loading")
            val adRequest: AdRequest = AdRequest.Builder().build()

            InterstitialAd.load(context, adID, adRequest,
                object : InterstitialAdLoadCallback() {
                    override fun onAdLoaded(@NonNull interstitialAd: InterstitialAd) {
                        // The mInterstitialAd reference will be null until
                        // an ad is loaded.
                        admobInterstitialAd = interstitialAd
                        Log.e(TAG, "admob inters: onAdLoaded")

                        admobInterstitialAd?.fullScreenContentCallback = object: FullScreenContentCallback() {
                            override fun onAdDismissedFullScreenContent() {
                                Log.d(TAG, "admob inters: Ad was dismissed.")
                                admobInterstitialAd = null
                                loadAdmobInterstitial(context, adID)
                                moveToNextActivity(context as Activity)
                            }

                            override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                                Log.e(TAG, "admob inters: onAdFailedToShowFullScreenContent: "+adError.message)
                            }

                            override fun onAdShowedFullScreenContent() {
                                Log.e(TAG, "admob inters: Ad showed fullscreen content.")
                                admobInterstitialAd = null
                            }
                        }
                    }

                    override fun onAdFailedToLoad(@NonNull loadAdError: LoadAdError) {
                        // Handle the error
                        Log.i(TAG, "admob inters: onAdFailedToLoad: "+loadAdError.message)
                        admobInterstitialAd = null
                        moveToNextActivity(context as Activity)
                    }
                })
        }



        fun onPauseCall() {
            admobInterstitialAd = null
            fanInterstitialAd = null
            //handler.removeCallbacksAndMessages(null)
        }

        private var counter = 1
        private var cG: Class<*> ?= null
        private var intentG: Intent ?= null
        @SuppressLint("StaticFieldLeak")
        private var activityG: Activity ?= null
        private var adCallBack: AdCallBack ?= null
        private var isClearStack: Boolean = false
        fun showInters(activity: Activity) {
            activityG = activity
            if (counter == interval.toInt()) {
                counter = 1

                when (selectedAd) {
                    "" -> {
                        moveToNextActivity(activity)
                    }
                    java.AD_TYPE_ADMOB -> {
                        showAdmobInters(activity)
                    }
                    java.AD_TYPE_FACEBOOK -> {
                        showFbInters()
                    }
                    java.AD_TYPE_UNITY -> {
                        showUnityInters(activity)
                    }
                    java.AD_TYPE_IRON -> {
                        showIronSourceInterstitial(activity)
                    }
                    java.AD_TYPE_APPLOVIN -> {
                        showApplovinInterstitial()
                    }

                    java.AD_TYPE_ADMOB + java.AD_TYPE_FACEBOOK -> {
                        Log.e(TAG, "selectedAd2TEst: ${java.AD_TYPE_ADMOB + java.AD_TYPE_FACEBOOK}")
                        val randomNumber = generateRandom(1,2)
                        Log.e(TAG, "randomNumber: $randomNumber")
                        if (randomNumber == 1) showAdmobInters(activity)
                        else showFbInters()

                    }
                    java.AD_TYPE_ADMOB + java.AD_TYPE_UNITY -> {
                        val randomNumber = generateRandom(1,2)
                        Log.e(TAG, "randomNumber: $randomNumber")
                        if (randomNumber == 1) showAdmobInters(activity)
                        else showUnityInters(activity)

                    }
                    java.AD_TYPE_ADMOB + java.AD_TYPE_IRON -> {
                        val randomNumber = generateRandom(1,2)
                        Log.e(TAG, "randomNumber: $randomNumber")
                        if (randomNumber == 1) showAdmobInters(activity)
                        else showIronSourceInterstitial(activity)

                    }
                    java.AD_TYPE_ADMOB + java.AD_TYPE_APPLOVIN -> {
                        val randomNumber = generateRandom(1,2)
                        Log.e(TAG, "randomNumber: $randomNumber")
                        if (randomNumber == 1) showAdmobInters(activity)
                        else showAdmobInters(activity)

                    }


                    java.AD_TYPE_FACEBOOK + java.AD_TYPE_ADMOB -> {
                        val randomNumber = generateRandom(1,2)
                        Log.e(TAG, "randomNumber: $randomNumber")
                        if (randomNumber == 1) showFbInters()
                        else showAdmobInters(activity)

                    }
                    java.AD_TYPE_FACEBOOK + java.AD_TYPE_UNITY -> {
                        val randomNumber = generateRandom(1,2)
                        Log.e(TAG, "randomNumber: $randomNumber")
                        if (randomNumber == 1) showFbInters()
                        else showUnityInters(activity)

                    }
                    java.AD_TYPE_FACEBOOK + java.AD_TYPE_IRON -> {
                        val randomNumber = generateRandom(1,2)
                        Log.e(TAG, "randomNumber: $randomNumber")
                        if (randomNumber == 1) showFbInters()
                        else showIronSourceInterstitial(activity)
                    }
                    java.AD_TYPE_FACEBOOK + java.AD_TYPE_APPLOVIN -> {
                        val randomNumber = generateRandom(1,2)
                        Log.e(TAG, "randomNumber: $randomNumber")
                        if (randomNumber == 1) showFbInters()
                        else showApplovinInterstitial()
                    }


                    java.AD_TYPE_UNITY + java.AD_TYPE_ADMOB -> {
                        val randomNumber = generateRandom(1,2)
                        Log.e(TAG, "randomNumber: $randomNumber")
                        if (randomNumber == 1) showUnityInters(activity)
                        else showAdmobInters(activity)
                    }
                    java.AD_TYPE_UNITY + java.AD_TYPE_FACEBOOK -> {
                        val randomNumber = generateRandom(1,2)
                        Log.e(TAG, "randomNumber: $randomNumber")
                        if (randomNumber == 1) showUnityInters(activity)
                        else showFbInters()
                    }
                    java.AD_TYPE_UNITY + java.AD_TYPE_IRON -> {
                        val randomNumber = generateRandom(1,2)
                        Log.e(TAG, "randomNumber: $randomNumber")
                        if (randomNumber == 1) showUnityInters(activity)
                        else showIronSourceInterstitial(activity)
                    }
                    java.AD_TYPE_UNITY + java.AD_TYPE_APPLOVIN -> {
                        val randomNumber = generateRandom(1,2)
                        Log.e(TAG, "randomNumber: $randomNumber")
                        if (randomNumber == 1) showUnityInters(activity)
                        else showApplovinInterstitial()
                    }



                    java.AD_TYPE_IRON + java.AD_TYPE_ADMOB -> {
                        val randomNumber = generateRandom(1,2)
                        Log.e(TAG, "randomNumber: $randomNumber")
                        if (randomNumber == 1) showIronSourceInterstitial(activity)
                        else showAdmobInters(activity)
                    }
                    java.AD_TYPE_IRON + java.AD_TYPE_FACEBOOK -> {
                        val randomNumber = generateRandom(1,2)
                        Log.e(TAG, "randomNumber: $randomNumber")
                        if (randomNumber == 1) showIronSourceInterstitial(activity)
                        else showFbInters()
                    }
                    java.AD_TYPE_IRON + java.AD_TYPE_UNITY -> {
                        val randomNumber = generateRandom(1,2)
                        Log.e(TAG, "randomNumber: $randomNumber")
                        if (randomNumber == 1) showIronSourceInterstitial(activity)
                        else showUnityInters(activity)
                    }
                    java.AD_TYPE_IRON + java.AD_TYPE_APPLOVIN -> {
                        val randomNumber = generateRandom(1,2)
                        Log.e(TAG, "randomNumber: $randomNumber")
                        if (randomNumber == 1) showIronSourceInterstitial(activity)
                        else showApplovinInterstitial()
                    }



                    java.AD_TYPE_APPLOVIN + java.AD_TYPE_ADMOB -> {
                        val randomNumber = generateRandom(1,2)
                        Log.e(TAG, "randomNumber: $randomNumber")
                        if (randomNumber == 1) showAdmobInters(activity)
                        else showAdmobInters(activity)

                    }
                    java.AD_TYPE_APPLOVIN + java.AD_TYPE_FACEBOOK -> {
                        val randomNumber = generateRandom(1,2)
                        Log.e(TAG, "randomNumber: $randomNumber")
                        if (randomNumber == 1) showFbInters()
                        else showApplovinInterstitial()
                    }
                    java.AD_TYPE_APPLOVIN + java.AD_TYPE_UNITY -> {
                        val randomNumber = generateRandom(1,2)
                        Log.e(TAG, "randomNumber: $randomNumber")
                        if (randomNumber == 1) showUnityInters(activity)
                        else showApplovinInterstitial()
                    }
                    java.AD_TYPE_APPLOVIN + java.AD_TYPE_IRON -> {
                        val randomNumber = generateRandom(1,2)
                        Log.e(TAG, "randomNumber: $randomNumber")
                        if (randomNumber == 1) showIronSourceInterstitial(activity)
                        else showApplovinInterstitial()
                    }


                    java.AD_TYPE_ADMOB + java.AD_TYPE_UNITY + java.AD_TYPE_FACEBOOK-> {
                        val randomNumber = generateRandom(1,3)
                        Log.e(TAG, "randomNumber: $randomNumber")
                        when (randomNumber) {
                            1 -> {
                                showAdmobInters(activity)
                            }
                            2 -> {
                                showUnityInters(activity)
                            }
                            else -> {
                                showFbInters()
                            }
                        }
                    }
                    java.AD_TYPE_ADMOB + java.AD_TYPE_UNITY + java.AD_TYPE_IRON-> {
                        val randomNumber = generateRandom(1,2)
                        Log.e(TAG, "randomNumber: $randomNumber")
                        when (randomNumber) {
                            1 -> {
                                showAdmobInters(activity)
                            }
                            2 -> {
                                showUnityInters(activity)
                            }
                            else -> {
                                showIronSourceInterstitial(activity)
                            }
                        }
                    }
                    java.AD_TYPE_ADMOB + java.AD_TYPE_UNITY + java.AD_TYPE_APPLOVIN-> {
                        val randomNumber = generateRandom(1,2)
                        Log.e(TAG, "randomNumber: $randomNumber")
                        when (randomNumber) {
                            1 -> {
                                showAdmobInters(activity)
                            }
                            2 -> {
                                showUnityInters(activity)
                            }
                            else -> {
                                showApplovinInterstitial()
                            }
                        }
                    }
                }
            }
            else {
                counter++
            }
        }

        fun showInters(activity: Activity, c: Class<*>? = null) {
            cG = c
            activityG = activity

            if (counter == interval.toInt()) {
                counter = 1

                when (selectedAd) {
                    "" -> {
                        moveToNextActivity(activity)
                    }
                    java.AD_TYPE_ADMOB -> {
                        showAdmobInters(activity)
                    }
                    java.AD_TYPE_FACEBOOK -> {
                        showFbInters()
                    }
                    java.AD_TYPE_UNITY -> {
                        showUnityInters(activity)
                    }
                    java.AD_TYPE_IRON -> {
                        showIronSourceInterstitial(activity)
                    }
                    java.AD_TYPE_APPLOVIN -> {
                        showApplovinInterstitial()
                    }

                    java.AD_TYPE_ADMOB + java.AD_TYPE_FACEBOOK -> {
                        Log.e(TAG, "selectedAd2TEst: ${java.AD_TYPE_ADMOB + java.AD_TYPE_FACEBOOK}")
                        val randomNumber = generateRandom(1,2)
                        Log.e(TAG, "randomNumber: $randomNumber")
                        if (randomNumber == 1) showAdmobInters(activity)
                        else showFbInters()

                    }
                    java.AD_TYPE_ADMOB + java.AD_TYPE_UNITY -> {
                        val randomNumber = generateRandom(1,2)
                        Log.e(TAG, "randomNumber: $randomNumber")
                        if (randomNumber == 1) showAdmobInters(activity)
                        else showUnityInters(activity)

                    }
                    java.AD_TYPE_ADMOB + java.AD_TYPE_IRON -> {
                        val randomNumber = generateRandom(1,2)
                        Log.e(TAG, "randomNumber: $randomNumber")
                        if (randomNumber == 1) showAdmobInters(activity)
                        else showIronSourceInterstitial(activity)

                    }
                    java.AD_TYPE_ADMOB + java.AD_TYPE_APPLOVIN -> {
                        val randomNumber = generateRandom(1,2)
                        Log.e(TAG, "randomNumber: $randomNumber")
                        if (randomNumber == 1) showAdmobInters(activity)
                        else showAdmobInters(activity)

                    }


                    java.AD_TYPE_FACEBOOK + java.AD_TYPE_ADMOB -> {
                        val randomNumber = generateRandom(1,2)
                        Log.e(TAG, "randomNumber: $randomNumber")
                        if (randomNumber == 1) showFbInters()
                        else showAdmobInters(activity)

                    }
                    java.AD_TYPE_FACEBOOK + java.AD_TYPE_UNITY -> {
                        val randomNumber = generateRandom(1,2)
                        Log.e(TAG, "randomNumber: $randomNumber")
                        if (randomNumber == 1) showFbInters()
                        else showUnityInters(activity)

                    }
                    java.AD_TYPE_FACEBOOK + java.AD_TYPE_IRON -> {
                        val randomNumber = generateRandom(1,2)
                        Log.e(TAG, "randomNumber: $randomNumber")
                        if (randomNumber == 1) showFbInters()
                        else showIronSourceInterstitial(activity)
                    }
                    java.AD_TYPE_FACEBOOK + java.AD_TYPE_APPLOVIN -> {
                        val randomNumber = generateRandom(1,2)
                        Log.e(TAG, "randomNumber: $randomNumber")
                        if (randomNumber == 1) showFbInters()
                        else showApplovinInterstitial()
                    }


                    java.AD_TYPE_UNITY + java.AD_TYPE_ADMOB -> {
                        val randomNumber = generateRandom(1,2)
                        Log.e(TAG, "randomNumber: $randomNumber")
                        if (randomNumber == 1) showUnityInters(activity)
                        else showAdmobInters(activity)
                    }
                    java.AD_TYPE_UNITY + java.AD_TYPE_FACEBOOK -> {
                        val randomNumber = generateRandom(1,2)
                        Log.e(TAG, "randomNumber: $randomNumber")
                        if (randomNumber == 1) showUnityInters(activity)
                        else showFbInters()
                    }
                    java.AD_TYPE_UNITY + java.AD_TYPE_IRON -> {
                        val randomNumber = generateRandom(1,2)
                        Log.e(TAG, "randomNumber: $randomNumber")
                        if (randomNumber == 1) showUnityInters(activity)
                        else showIronSourceInterstitial(activity)
                    }
                    java.AD_TYPE_UNITY + java.AD_TYPE_APPLOVIN -> {
                        val randomNumber = generateRandom(1,2)
                        Log.e(TAG, "randomNumber: $randomNumber")
                        if (randomNumber == 1) showUnityInters(activity)
                        else showApplovinInterstitial()
                    }



                    java.AD_TYPE_IRON + java.AD_TYPE_ADMOB -> {
                        val randomNumber = generateRandom(1,2)
                        Log.e(TAG, "randomNumber: $randomNumber")
                        if (randomNumber == 1) showIronSourceInterstitial(activity)
                        else showAdmobInters(activity)
                    }
                    java.AD_TYPE_IRON + java.AD_TYPE_FACEBOOK -> {
                        val randomNumber = generateRandom(1,2)
                        Log.e(TAG, "randomNumber: $randomNumber")
                        if (randomNumber == 1) showIronSourceInterstitial(activity)
                        else showFbInters()
                    }
                    java.AD_TYPE_IRON + java.AD_TYPE_UNITY -> {
                        val randomNumber = generateRandom(1,2)
                        Log.e(TAG, "randomNumber: $randomNumber")
                        if (randomNumber == 1) showIronSourceInterstitial(activity)
                        else showUnityInters(activity)
                    }
                    java.AD_TYPE_IRON + java.AD_TYPE_APPLOVIN -> {
                        val randomNumber = generateRandom(1,2)
                        Log.e(TAG, "randomNumber: $randomNumber")
                        if (randomNumber == 1) showIronSourceInterstitial(activity)
                        else showApplovinInterstitial()
                    }



                    java.AD_TYPE_APPLOVIN + java.AD_TYPE_ADMOB -> {
                        val randomNumber = generateRandom(1,2)
                        Log.e(TAG, "randomNumber: $randomNumber")
                        if (randomNumber == 1) showAdmobInters(activity)
                        else showAdmobInters(activity)

                    }
                    java.AD_TYPE_APPLOVIN + java.AD_TYPE_FACEBOOK -> {
                        val randomNumber = generateRandom(1,2)
                        Log.e(TAG, "randomNumber: $randomNumber")
                        if (randomNumber == 1) showFbInters()
                        else showApplovinInterstitial()
                    }
                    java.AD_TYPE_APPLOVIN + java.AD_TYPE_UNITY -> {
                        val randomNumber = generateRandom(1,2)
                        Log.e(TAG, "randomNumber: $randomNumber")
                        if (randomNumber == 1) showUnityInters(activity)
                        else showApplovinInterstitial()
                    }
                    java.AD_TYPE_APPLOVIN + java.AD_TYPE_IRON -> {
                        val randomNumber = generateRandom(1,2)
                        Log.e(TAG, "randomNumber: $randomNumber")
                        if (randomNumber == 1) showIronSourceInterstitial(activity)
                        else showApplovinInterstitial()
                    }


                    java.AD_TYPE_ADMOB + java.AD_TYPE_UNITY + java.AD_TYPE_FACEBOOK-> {
                        val randomNumber = generateRandom(1,3)
                        Log.e(TAG, "randomNumber: $randomNumber")
                        when (randomNumber) {
                            1 -> {
                                showAdmobInters(activity)
                            }
                            2 -> {
                                showUnityInters(activity)
                            }
                            else -> {
                                showFbInters()
                            }
                        }
                    }
                    java.AD_TYPE_ADMOB + java.AD_TYPE_UNITY + java.AD_TYPE_IRON-> {
                        val randomNumber = generateRandom(1,2)
                        Log.e(TAG, "randomNumber: $randomNumber")
                        when (randomNumber) {
                            1 -> {
                                showAdmobInters(activity)
                            }
                            2 -> {
                                showUnityInters(activity)
                            }
                            else -> {
                                showIronSourceInterstitial(activity)
                            }
                        }
                    }
                    java.AD_TYPE_ADMOB + java.AD_TYPE_UNITY + java.AD_TYPE_APPLOVIN-> {
                        val randomNumber = generateRandom(1,2)
                        Log.e(TAG, "randomNumber: $randomNumber")
                        when (randomNumber) {
                            1 -> {
                                showAdmobInters(activity)
                            }
                            2 -> {
                                showUnityInters(activity)
                            }
                            else -> {
                                showApplovinInterstitial()
                            }
                        }
                    }
                }
            }
            else {
                counter++
            }
        }

        fun showInters(activity: Activity, c: Class<*>? = null, clearStack: Boolean = false) {
            Log.e(TAG, "interval: $interval")
            Log.e(TAG, "selectedAd: $selectedAd")
            cG = c
            activityG = activity
            isClearStack = clearStack
            if (counter == interval.toInt()) {
                counter = 1

                when (selectedAd) {
                    "" -> {
                        moveToNextActivity(activity)
                    }
                    java.AD_TYPE_ADMOB -> {
                        showAdmobInters(activity)
                    }
                    java.AD_TYPE_FACEBOOK -> {
                        showFbInters()
                    }
                    java.AD_TYPE_UNITY -> {
                        showUnityInters(activity)
                    }
                    java.AD_TYPE_IRON -> {
                        showIronSourceInterstitial(activity)
                    }
                    java.AD_TYPE_APPLOVIN -> {
                        showApplovinInterstitial()
                    }

                    java.AD_TYPE_ADMOB + java.AD_TYPE_FACEBOOK -> {
                        Log.e(TAG, "selectedAd2TEst: ${java.AD_TYPE_ADMOB + java.AD_TYPE_FACEBOOK}")
                        val randomNumber = generateRandom(1,2)
                        Log.e(TAG, "randomNumber: $randomNumber")
                        if (randomNumber == 1) showAdmobInters(activity)
                        else showFbInters()

                    }
                    java.AD_TYPE_ADMOB + java.AD_TYPE_UNITY -> {
                        val randomNumber = generateRandom(1,2)
                        Log.e(TAG, "randomNumber: $randomNumber")
                        if (randomNumber == 1) showAdmobInters(activity)
                        else showUnityInters(activity)

                    }
                    java.AD_TYPE_ADMOB + java.AD_TYPE_IRON -> {
                        val randomNumber = generateRandom(1,2)
                        Log.e(TAG, "randomNumber: $randomNumber")
                        if (randomNumber == 1) showAdmobInters(activity)
                        else showIronSourceInterstitial(activity)

                    }
                    java.AD_TYPE_ADMOB + java.AD_TYPE_APPLOVIN -> {
                        val randomNumber = generateRandom(1,2)
                        Log.e(TAG, "randomNumber: $randomNumber")
                        if (randomNumber == 1) showAdmobInters(activity)
                        else showAdmobInters(activity)

                    }


                    java.AD_TYPE_FACEBOOK + java.AD_TYPE_ADMOB -> {
                        val randomNumber = generateRandom(1,2)
                        Log.e(TAG, "randomNumber: $randomNumber")
                        if (randomNumber == 1) showFbInters()
                        else showAdmobInters(activity)

                    }
                    java.AD_TYPE_FACEBOOK + java.AD_TYPE_UNITY -> {
                        val randomNumber = generateRandom(1,2)
                        Log.e(TAG, "randomNumber: $randomNumber")
                        if (randomNumber == 1) showFbInters()
                        else showUnityInters(activity)

                    }
                    java.AD_TYPE_FACEBOOK + java.AD_TYPE_IRON -> {
                        val randomNumber = generateRandom(1,2)
                        Log.e(TAG, "randomNumber: $randomNumber")
                        if (randomNumber == 1) showFbInters()
                        else showIronSourceInterstitial(activity)
                    }
                    java.AD_TYPE_FACEBOOK + java.AD_TYPE_APPLOVIN -> {
                        val randomNumber = generateRandom(1,2)
                        Log.e(TAG, "randomNumber: $randomNumber")
                        if (randomNumber == 1) showFbInters()
                        else showApplovinInterstitial()
                    }


                    java.AD_TYPE_UNITY + java.AD_TYPE_ADMOB -> {
                        val randomNumber = generateRandom(1,2)
                        Log.e(TAG, "randomNumber: $randomNumber")
                        if (randomNumber == 1) showUnityInters(activity)
                        else showAdmobInters(activity)
                    }
                    java.AD_TYPE_UNITY + java.AD_TYPE_FACEBOOK -> {
                        val randomNumber = generateRandom(1,2)
                        Log.e(TAG, "randomNumber: $randomNumber")
                        if (randomNumber == 1) showUnityInters(activity)
                        else showFbInters()
                    }
                    java.AD_TYPE_UNITY + java.AD_TYPE_IRON -> {
                        val randomNumber = generateRandom(1,2)
                        Log.e(TAG, "randomNumber: $randomNumber")
                        if (randomNumber == 1) showUnityInters(activity)
                        else showIronSourceInterstitial(activity)
                    }
                    java.AD_TYPE_UNITY + java.AD_TYPE_APPLOVIN -> {
                        val randomNumber = generateRandom(1,2)
                        Log.e(TAG, "randomNumber: $randomNumber")
                        if (randomNumber == 1) showUnityInters(activity)
                        else showApplovinInterstitial()
                    }



                    java.AD_TYPE_IRON + java.AD_TYPE_ADMOB -> {
                        val randomNumber = generateRandom(1,2)
                        Log.e(TAG, "randomNumber: $randomNumber")
                        if (randomNumber == 1) showIronSourceInterstitial(activity)
                        else showAdmobInters(activity)
                    }
                    java.AD_TYPE_IRON + java.AD_TYPE_FACEBOOK -> {
                        val randomNumber = generateRandom(1,2)
                        Log.e(TAG, "randomNumber: $randomNumber")
                        if (randomNumber == 1) showIronSourceInterstitial(activity)
                        else showFbInters()
                    }
                    java.AD_TYPE_IRON + java.AD_TYPE_UNITY -> {
                        val randomNumber = generateRandom(1,2)
                        Log.e(TAG, "randomNumber: $randomNumber")
                        if (randomNumber == 1) showIronSourceInterstitial(activity)
                        else showUnityInters(activity)
                    }
                    java.AD_TYPE_IRON + java.AD_TYPE_APPLOVIN -> {
                        val randomNumber = generateRandom(1,2)
                        Log.e(TAG, "randomNumber: $randomNumber")
                        if (randomNumber == 1) showIronSourceInterstitial(activity)
                        else showApplovinInterstitial()
                    }



                    java.AD_TYPE_APPLOVIN + java.AD_TYPE_ADMOB -> {
                        val randomNumber = generateRandom(1,2)
                        Log.e(TAG, "randomNumber: $randomNumber")
                        if (randomNumber == 1) showApplovinInterstitial()
                        else showAdmobInters(activity)

                    }
                    java.AD_TYPE_APPLOVIN + java.AD_TYPE_FACEBOOK -> {
                        val randomNumber = generateRandom(1,2)
                        Log.e(TAG, "randomNumber: $randomNumber")
                        if (randomNumber == 1) showFbInters()
                        else showApplovinInterstitial()
                    }
                    java.AD_TYPE_APPLOVIN + java.AD_TYPE_UNITY -> {
                        val randomNumber = generateRandom(1,2)
                        Log.e(TAG, "randomNumber: $randomNumber")
                        if (randomNumber == 1) showUnityInters(activity)
                        else showApplovinInterstitial()
                    }
                    java.AD_TYPE_APPLOVIN + java.AD_TYPE_IRON -> {
                        val randomNumber = generateRandom(1,2)
                        Log.e(TAG, "randomNumber: $randomNumber")
                        if (randomNumber == 1) showIronSourceInterstitial(activity)
                        else showApplovinInterstitial()
                    }


                    java.AD_TYPE_ADMOB + java.AD_TYPE_UNITY + java.AD_TYPE_FACEBOOK-> {
                        val randomNumber = generateRandom(1,3)
                        Log.e(TAG, "randomNumber: $randomNumber")
                        when (randomNumber) {
                            1 -> {
                                showAdmobInters(activity)
                            }
                            2 -> {
                                showUnityInters(activity)
                            }
                            else -> {
                                showFbInters()
                            }
                        }
                    }
                    java.AD_TYPE_ADMOB + java.AD_TYPE_UNITY + java.AD_TYPE_IRON-> {
                        val randomNumber = generateRandom(1,2)
                        Log.e(TAG, "randomNumber: $randomNumber")
                        when (randomNumber) {
                            1 -> {
                                showAdmobInters(activity)
                            }
                            2 -> {
                                showUnityInters(activity)
                            }
                            else -> {
                                showIronSourceInterstitial(activity)
                            }
                        }
                    }
                    java.AD_TYPE_ADMOB + java.AD_TYPE_UNITY + java.AD_TYPE_APPLOVIN-> {
                        val randomNumber = generateRandom(1,2)
                        Log.e(TAG, "randomNumber: $randomNumber")
                        when (randomNumber) {
                            1 -> {
                                showAdmobInters(activity)
                            }
                            2 -> {
                                showUnityInters(activity)
                            }
                            else -> {
                                showApplovinInterstitial()
                            }
                        }
                    }
                }
            }
            else {
                counter++
            }
        }

        var counter1 = 1
        fun showCounterInters(activity: Activity, interval: Int, c: Class<*>? = null, clearStack: Boolean = false) {
            Log.e(TAG, "interval: $interval")
            Log.e(TAG, "selectedAd: $selectedAd")
            cG = c
            activityG = activity
            isClearStack = clearStack
            if (counter1 == interval.toInt()) {
                counter1 = 1

                when (selectedAd) {
                    "" -> {
                        moveToNextActivity(activity)
                    }
                    java.AD_TYPE_ADMOB -> {
                        showAdmobInters(activity)
                    }
                    java.AD_TYPE_FACEBOOK -> {
                        showFbInters()
                    }
                    java.AD_TYPE_UNITY -> {
                        showUnityInters(activity)
                    }
                    java.AD_TYPE_IRON -> {
                        showIronSourceInterstitial(activity)
                    }
                    java.AD_TYPE_APPLOVIN -> {
                        showApplovinInterstitial()
                    }

                    java.AD_TYPE_ADMOB + java.AD_TYPE_FACEBOOK -> {
                        Log.e(TAG, "selectedAd2TEst: ${java.AD_TYPE_ADMOB + java.AD_TYPE_FACEBOOK}")
                        val randomNumber = generateRandom(1,2)
                        Log.e(TAG, "randomNumber: $randomNumber")
                        if (randomNumber == 1) showAdmobInters(activity)
                        else showFbInters()

                    }
                    java.AD_TYPE_ADMOB + java.AD_TYPE_UNITY -> {
                        val randomNumber = generateRandom(1,2)
                        Log.e(TAG, "randomNumber: $randomNumber")
                        if (randomNumber == 1) showAdmobInters(activity)
                        else showUnityInters(activity)

                    }
                    java.AD_TYPE_ADMOB + java.AD_TYPE_IRON -> {
                        val randomNumber = generateRandom(1,2)
                        Log.e(TAG, "randomNumber: $randomNumber")
                        if (randomNumber == 1) showAdmobInters(activity)
                        else showIronSourceInterstitial(activity)

                    }
                    java.AD_TYPE_ADMOB + java.AD_TYPE_APPLOVIN -> {
                        val randomNumber = generateRandom(1,2)
                        Log.e(TAG, "randomNumber: $randomNumber")
                        if (randomNumber == 1) showAdmobInters(activity)
                        else showAdmobInters(activity)

                    }


                    java.AD_TYPE_FACEBOOK + java.AD_TYPE_ADMOB -> {
                        val randomNumber = generateRandom(1,2)
                        Log.e(TAG, "randomNumber: $randomNumber")
                        if (randomNumber == 1) showFbInters()
                        else showAdmobInters(activity)

                    }
                    java.AD_TYPE_FACEBOOK + java.AD_TYPE_UNITY -> {
                        val randomNumber = generateRandom(1,2)
                        Log.e(TAG, "randomNumber: $randomNumber")
                        if (randomNumber == 1) showFbInters()
                        else showUnityInters(activity)

                    }
                    java.AD_TYPE_FACEBOOK + java.AD_TYPE_IRON -> {
                        val randomNumber = generateRandom(1,2)
                        Log.e(TAG, "randomNumber: $randomNumber")
                        if (randomNumber == 1) showFbInters()
                        else showIronSourceInterstitial(activity)
                    }
                    java.AD_TYPE_FACEBOOK + java.AD_TYPE_APPLOVIN -> {
                        val randomNumber = generateRandom(1,2)
                        Log.e(TAG, "randomNumber: $randomNumber")
                        if (randomNumber == 1) showFbInters()
                        else showApplovinInterstitial()
                    }


                    java.AD_TYPE_UNITY + java.AD_TYPE_ADMOB -> {
                        val randomNumber = generateRandom(1,2)
                        Log.e(TAG, "randomNumber: $randomNumber")
                        if (randomNumber == 1) showUnityInters(activity)
                        else showAdmobInters(activity)
                    }
                    java.AD_TYPE_UNITY + java.AD_TYPE_FACEBOOK -> {
                        val randomNumber = generateRandom(1,2)
                        Log.e(TAG, "randomNumber: $randomNumber")
                        if (randomNumber == 1) showUnityInters(activity)
                        else showFbInters()
                    }
                    java.AD_TYPE_UNITY + java.AD_TYPE_IRON -> {
                        val randomNumber = generateRandom(1,2)
                        Log.e(TAG, "randomNumber: $randomNumber")
                        if (randomNumber == 1) showUnityInters(activity)
                        else showIronSourceInterstitial(activity)
                    }
                    java.AD_TYPE_UNITY + java.AD_TYPE_APPLOVIN -> {
                        val randomNumber = generateRandom(1,2)
                        Log.e(TAG, "randomNumber: $randomNumber")
                        if (randomNumber == 1) showUnityInters(activity)
                        else showApplovinInterstitial()
                    }



                    java.AD_TYPE_IRON + java.AD_TYPE_ADMOB -> {
                        val randomNumber = generateRandom(1,2)
                        Log.e(TAG, "randomNumber: $randomNumber")
                        if (randomNumber == 1) showIronSourceInterstitial(activity)
                        else showAdmobInters(activity)
                    }
                    java.AD_TYPE_IRON + java.AD_TYPE_FACEBOOK -> {
                        val randomNumber = generateRandom(1,2)
                        Log.e(TAG, "randomNumber: $randomNumber")
                        if (randomNumber == 1) showIronSourceInterstitial(activity)
                        else showFbInters()
                    }
                    java.AD_TYPE_IRON + java.AD_TYPE_UNITY -> {
                        val randomNumber = generateRandom(1,2)
                        Log.e(TAG, "randomNumber: $randomNumber")
                        if (randomNumber == 1) showIronSourceInterstitial(activity)
                        else showUnityInters(activity)
                    }
                    java.AD_TYPE_IRON + java.AD_TYPE_APPLOVIN -> {
                        val randomNumber = generateRandom(1,2)
                        Log.e(TAG, "randomNumber: $randomNumber")
                        if (randomNumber == 1) showIronSourceInterstitial(activity)
                        else showApplovinInterstitial()
                    }



                    java.AD_TYPE_APPLOVIN + java.AD_TYPE_ADMOB -> {
                        val randomNumber = generateRandom(1,2)
                        Log.e(TAG, "randomNumber: $randomNumber")
                        if (randomNumber == 1) showApplovinInterstitial()
                        else showAdmobInters(activity)

                    }
                    java.AD_TYPE_APPLOVIN + java.AD_TYPE_FACEBOOK -> {
                        val randomNumber = generateRandom(1,2)
                        Log.e(TAG, "randomNumber: $randomNumber")
                        if (randomNumber == 1) showFbInters()
                        else showApplovinInterstitial()
                    }
                    java.AD_TYPE_APPLOVIN + java.AD_TYPE_UNITY -> {
                        val randomNumber = generateRandom(1,2)
                        Log.e(TAG, "randomNumber: $randomNumber")
                        if (randomNumber == 1) showUnityInters(activity)
                        else showApplovinInterstitial()
                    }
                    java.AD_TYPE_APPLOVIN + java.AD_TYPE_IRON -> {
                        val randomNumber = generateRandom(1,2)
                        Log.e(TAG, "randomNumber: $randomNumber")
                        if (randomNumber == 1) showIronSourceInterstitial(activity)
                        else showApplovinInterstitial()
                    }


                    java.AD_TYPE_ADMOB + java.AD_TYPE_UNITY + java.AD_TYPE_FACEBOOK-> {
                        val randomNumber = generateRandom(1,3)
                        Log.e(TAG, "randomNumber: $randomNumber")
                        when (randomNumber) {
                            1 -> {
                                showAdmobInters(activity)
                            }
                            2 -> {
                                showUnityInters(activity)
                            }
                            else -> {
                                showFbInters()
                            }
                        }
                    }
                    java.AD_TYPE_ADMOB + java.AD_TYPE_UNITY + java.AD_TYPE_IRON-> {
                        val randomNumber = generateRandom(1,2)
                        Log.e(TAG, "randomNumber: $randomNumber")
                        when (randomNumber) {
                            1 -> {
                                showAdmobInters(activity)
                            }
                            2 -> {
                                showUnityInters(activity)
                            }
                            else -> {
                                showIronSourceInterstitial(activity)
                            }
                        }
                    }
                    java.AD_TYPE_ADMOB + java.AD_TYPE_UNITY + java.AD_TYPE_APPLOVIN-> {
                        val randomNumber = generateRandom(1,2)
                        Log.e(TAG, "randomNumber: $randomNumber")
                        when (randomNumber) {
                            1 -> {
                                showAdmobInters(activity)
                            }
                            2 -> {
                                showUnityInters(activity)
                            }
                            else -> {
                                showApplovinInterstitial()
                            }
                        }
                    }
                }
            }
            else {
                counter1++
            }
        }

        var counter2 = 1
        fun showCounterInters(activity: Activity, interval: Int) {
            Log.e(TAG, "interval: $interval")
            Log.e(TAG, "selectedAd: $selectedAd")
            activityG = activity
            if (counter2 == interval.toInt()) {
                counter2 = 1

                when (selectedAd) {
                    "" -> {
                        moveToNextActivity(activity)
                    }
                    java.AD_TYPE_ADMOB -> {
                        showAdmobInters(activity)
                    }
                    java.AD_TYPE_FACEBOOK -> {
                        showFbInters()
                    }
                    java.AD_TYPE_UNITY -> {
                        showUnityInters(activity)
                    }
                    java.AD_TYPE_IRON -> {
                        showIronSourceInterstitial(activity)
                    }
                    java.AD_TYPE_APPLOVIN -> {
                        showApplovinInterstitial()
                    }

                    java.AD_TYPE_ADMOB + java.AD_TYPE_FACEBOOK -> {
                        Log.e(TAG, "selectedAd2TEst: ${java.AD_TYPE_ADMOB + java.AD_TYPE_FACEBOOK}")
                        val randomNumber = generateRandom(1,2)
                        Log.e(TAG, "randomNumber: $randomNumber")
                        if (randomNumber == 1) showAdmobInters(activity)
                        else showFbInters()

                    }
                    java.AD_TYPE_ADMOB + java.AD_TYPE_UNITY -> {
                        val randomNumber = generateRandom(1,2)
                        Log.e(TAG, "randomNumber: $randomNumber")
                        if (randomNumber == 1) showAdmobInters(activity)
                        else showUnityInters(activity)

                    }
                    java.AD_TYPE_ADMOB + java.AD_TYPE_IRON -> {
                        val randomNumber = generateRandom(1,2)
                        Log.e(TAG, "randomNumber: $randomNumber")
                        if (randomNumber == 1) showAdmobInters(activity)
                        else showIronSourceInterstitial(activity)

                    }
                    java.AD_TYPE_ADMOB + java.AD_TYPE_APPLOVIN -> {
                        val randomNumber = generateRandom(1,2)
                        Log.e(TAG, "randomNumber: $randomNumber")
                        if (randomNumber == 1) showAdmobInters(activity)
                        else showAdmobInters(activity)

                    }


                    java.AD_TYPE_FACEBOOK + java.AD_TYPE_ADMOB -> {
                        val randomNumber = generateRandom(1,2)
                        Log.e(TAG, "randomNumber: $randomNumber")
                        if (randomNumber == 1) showFbInters()
                        else showAdmobInters(activity)

                    }
                    java.AD_TYPE_FACEBOOK + java.AD_TYPE_UNITY -> {
                        val randomNumber = generateRandom(1,2)
                        Log.e(TAG, "randomNumber: $randomNumber")
                        if (randomNumber == 1) showFbInters()
                        else showUnityInters(activity)

                    }
                    java.AD_TYPE_FACEBOOK + java.AD_TYPE_IRON -> {
                        val randomNumber = generateRandom(1,2)
                        Log.e(TAG, "randomNumber: $randomNumber")
                        if (randomNumber == 1) showFbInters()
                        else showIronSourceInterstitial(activity)
                    }
                    java.AD_TYPE_FACEBOOK + java.AD_TYPE_APPLOVIN -> {
                        val randomNumber = generateRandom(1,2)
                        Log.e(TAG, "randomNumber: $randomNumber")
                        if (randomNumber == 1) showFbInters()
                        else showApplovinInterstitial()
                    }


                    java.AD_TYPE_UNITY + java.AD_TYPE_ADMOB -> {
                        val randomNumber = generateRandom(1,2)
                        Log.e(TAG, "randomNumber: $randomNumber")
                        if (randomNumber == 1) showUnityInters(activity)
                        else showAdmobInters(activity)
                    }
                    java.AD_TYPE_UNITY + java.AD_TYPE_FACEBOOK -> {
                        val randomNumber = generateRandom(1,2)
                        Log.e(TAG, "randomNumber: $randomNumber")
                        if (randomNumber == 1) showUnityInters(activity)
                        else showFbInters()
                    }
                    java.AD_TYPE_UNITY + java.AD_TYPE_IRON -> {
                        val randomNumber = generateRandom(1,2)
                        Log.e(TAG, "randomNumber: $randomNumber")
                        if (randomNumber == 1) showUnityInters(activity)
                        else showIronSourceInterstitial(activity)
                    }
                    java.AD_TYPE_UNITY + java.AD_TYPE_APPLOVIN -> {
                        val randomNumber = generateRandom(1,2)
                        Log.e(TAG, "randomNumber: $randomNumber")
                        if (randomNumber == 1) showUnityInters(activity)
                        else showApplovinInterstitial()
                    }



                    java.AD_TYPE_IRON + java.AD_TYPE_ADMOB -> {
                        val randomNumber = generateRandom(1,2)
                        Log.e(TAG, "randomNumber: $randomNumber")
                        if (randomNumber == 1) showIronSourceInterstitial(activity)
                        else showAdmobInters(activity)
                    }
                    java.AD_TYPE_IRON + java.AD_TYPE_FACEBOOK -> {
                        val randomNumber = generateRandom(1,2)
                        Log.e(TAG, "randomNumber: $randomNumber")
                        if (randomNumber == 1) showIronSourceInterstitial(activity)
                        else showFbInters()
                    }
                    java.AD_TYPE_IRON + java.AD_TYPE_UNITY -> {
                        val randomNumber = generateRandom(1,2)
                        Log.e(TAG, "randomNumber: $randomNumber")
                        if (randomNumber == 1) showIronSourceInterstitial(activity)
                        else showUnityInters(activity)
                    }
                    java.AD_TYPE_IRON + java.AD_TYPE_APPLOVIN -> {
                        val randomNumber = generateRandom(1,2)
                        Log.e(TAG, "randomNumber: $randomNumber")
                        if (randomNumber == 1) showIronSourceInterstitial(activity)
                        else showApplovinInterstitial()
                    }



                    java.AD_TYPE_APPLOVIN + java.AD_TYPE_ADMOB -> {
                        val randomNumber = generateRandom(1,2)
                        Log.e(TAG, "randomNumber: $randomNumber")
                        if (randomNumber == 1) showApplovinInterstitial()
                        else showAdmobInters(activity)

                    }
                    java.AD_TYPE_APPLOVIN + java.AD_TYPE_FACEBOOK -> {
                        val randomNumber = generateRandom(1,2)
                        Log.e(TAG, "randomNumber: $randomNumber")
                        if (randomNumber == 1) showFbInters()
                        else showApplovinInterstitial()
                    }
                    java.AD_TYPE_APPLOVIN + java.AD_TYPE_UNITY -> {
                        val randomNumber = generateRandom(1,2)
                        Log.e(TAG, "randomNumber: $randomNumber")
                        if (randomNumber == 1) showUnityInters(activity)
                        else showApplovinInterstitial()
                    }
                    java.AD_TYPE_APPLOVIN + java.AD_TYPE_IRON -> {
                        val randomNumber = generateRandom(1,2)
                        Log.e(TAG, "randomNumber: $randomNumber")
                        if (randomNumber == 1) showIronSourceInterstitial(activity)
                        else showApplovinInterstitial()
                    }


                    java.AD_TYPE_ADMOB + java.AD_TYPE_UNITY + java.AD_TYPE_FACEBOOK-> {
                        val randomNumber = generateRandom(1,3)
                        Log.e(TAG, "randomNumber: $randomNumber")
                        when (randomNumber) {
                            1 -> {
                                showAdmobInters(activity)
                            }
                            2 -> {
                                showUnityInters(activity)
                            }
                            else -> {
                                showFbInters()
                            }
                        }
                    }
                    java.AD_TYPE_ADMOB + java.AD_TYPE_UNITY + java.AD_TYPE_IRON-> {
                        val randomNumber = generateRandom(1,2)
                        Log.e(TAG, "randomNumber: $randomNumber")
                        when (randomNumber) {
                            1 -> {
                                showAdmobInters(activity)
                            }
                            2 -> {
                                showUnityInters(activity)
                            }
                            else -> {
                                showIronSourceInterstitial(activity)
                            }
                        }
                    }
                    java.AD_TYPE_ADMOB + java.AD_TYPE_UNITY + java.AD_TYPE_APPLOVIN-> {
                        val randomNumber = generateRandom(1,2)
                        Log.e(TAG, "randomNumber: $randomNumber")
                        when (randomNumber) {
                            1 -> {
                                showAdmobInters(activity)
                            }
                            2 -> {
                                showUnityInters(activity)
                            }
                            else -> {
                                showApplovinInterstitial()
                            }
                        }
                    }
                }
            }
            else {
                counter2++
            }
        }

        fun showCounterInters(activity: Activity, intent: Intent, clearStack: Boolean = false) {
            Log.e(TAG, "interval: $interval")
            Log.e(TAG, "selectedAd: $selectedAd")
            intentG = intent
            activityG = activity
            isClearStack = clearStack
            if (counter == interval.toInt()) {
                counter = 3

                when (selectedAd) {
                    "" -> {
                        moveToNextActivity(activity)
                    }
                    java.AD_TYPE_ADMOB -> {
                        showAdmobInters(activity)
                    }
                    java.AD_TYPE_FACEBOOK -> {
                        showFbInters()
                    }
                    java.AD_TYPE_UNITY -> {
                        showUnityInters(activity)
                    }
                    java.AD_TYPE_IRON -> {
                        showIronSourceInterstitial(activity)
                    }
                    java.AD_TYPE_APPLOVIN -> {
                        showApplovinInterstitial()
                    }

                    java.AD_TYPE_ADMOB + java.AD_TYPE_FACEBOOK -> {
                        Log.e(TAG, "selectedAd2TEst: ${java.AD_TYPE_ADMOB + java.AD_TYPE_FACEBOOK}")
                        val randomNumber = generateRandom(1,2)
                        Log.e(TAG, "randomNumber: $randomNumber")
                        if (randomNumber == 1) showAdmobInters(activity)
                        else showFbInters()

                    }
                    java.AD_TYPE_ADMOB + java.AD_TYPE_UNITY -> {
                        val randomNumber = generateRandom(1,2)
                        Log.e(TAG, "randomNumber: $randomNumber")
                        if (randomNumber == 1) showAdmobInters(activity)
                        else showUnityInters(activity)

                    }
                    java.AD_TYPE_ADMOB + java.AD_TYPE_IRON -> {
                        val randomNumber = generateRandom(1,2)
                        Log.e(TAG, "randomNumber: $randomNumber")
                        if (randomNumber == 1) showAdmobInters(activity)
                        else showIronSourceInterstitial(activity)

                    }
                    java.AD_TYPE_ADMOB + java.AD_TYPE_APPLOVIN -> {
                        val randomNumber = generateRandom(1,2)
                        Log.e(TAG, "randomNumber: $randomNumber")
                        if (randomNumber == 1) showAdmobInters(activity)
                        else showAdmobInters(activity)

                    }


                    java.AD_TYPE_FACEBOOK + java.AD_TYPE_ADMOB -> {
                        val randomNumber = generateRandom(1,2)
                        Log.e(TAG, "randomNumber: $randomNumber")
                        if (randomNumber == 1) showFbInters()
                        else showAdmobInters(activity)

                    }
                    java.AD_TYPE_FACEBOOK + java.AD_TYPE_UNITY -> {
                        val randomNumber = generateRandom(1,2)
                        Log.e(TAG, "randomNumber: $randomNumber")
                        if (randomNumber == 1) showFbInters()
                        else showUnityInters(activity)

                    }
                    java.AD_TYPE_FACEBOOK + java.AD_TYPE_IRON -> {
                        val randomNumber = generateRandom(1,2)
                        Log.e(TAG, "randomNumber: $randomNumber")
                        if (randomNumber == 1) showFbInters()
                        else showIronSourceInterstitial(activity)
                    }
                    java.AD_TYPE_FACEBOOK + java.AD_TYPE_APPLOVIN -> {
                        val randomNumber = generateRandom(1,2)
                        Log.e(TAG, "randomNumber: $randomNumber")
                        if (randomNumber == 1) showFbInters()
                        else showApplovinInterstitial()
                    }


                    java.AD_TYPE_UNITY + java.AD_TYPE_ADMOB -> {
                        val randomNumber = generateRandom(1,2)
                        Log.e(TAG, "randomNumber: $randomNumber")
                        if (randomNumber == 1) showUnityInters(activity)
                        else showAdmobInters(activity)
                    }
                    java.AD_TYPE_UNITY + java.AD_TYPE_FACEBOOK -> {
                        val randomNumber = generateRandom(1,2)
                        Log.e(TAG, "randomNumber: $randomNumber")
                        if (randomNumber == 1) showUnityInters(activity)
                        else showFbInters()
                    }
                    java.AD_TYPE_UNITY + java.AD_TYPE_IRON -> {
                        val randomNumber = generateRandom(1,2)
                        Log.e(TAG, "randomNumber: $randomNumber")
                        if (randomNumber == 1) showUnityInters(activity)
                        else showIronSourceInterstitial(activity)
                    }
                    java.AD_TYPE_UNITY + java.AD_TYPE_APPLOVIN -> {
                        val randomNumber = generateRandom(1,2)
                        Log.e(TAG, "randomNumber: $randomNumber")
                        if (randomNumber == 1) showUnityInters(activity)
                        else showApplovinInterstitial()
                    }



                    java.AD_TYPE_IRON + java.AD_TYPE_ADMOB -> {
                        val randomNumber = generateRandom(1,2)
                        Log.e(TAG, "randomNumber: $randomNumber")
                        if (randomNumber == 1) showIronSourceInterstitial(activity)
                        else showAdmobInters(activity)
                    }
                    java.AD_TYPE_IRON + java.AD_TYPE_FACEBOOK -> {
                        val randomNumber = generateRandom(1,2)
                        Log.e(TAG, "randomNumber: $randomNumber")
                        if (randomNumber == 1) showIronSourceInterstitial(activity)
                        else showFbInters()
                    }
                    java.AD_TYPE_IRON + java.AD_TYPE_UNITY -> {
                        val randomNumber = generateRandom(1,2)
                        Log.e(TAG, "randomNumber: $randomNumber")
                        if (randomNumber == 1) showIronSourceInterstitial(activity)
                        else showUnityInters(activity)
                    }
                    java.AD_TYPE_IRON + java.AD_TYPE_APPLOVIN -> {
                        val randomNumber = generateRandom(1,2)
                        Log.e(TAG, "randomNumber: $randomNumber")
                        if (randomNumber == 1) showIronSourceInterstitial(activity)
                        else showApplovinInterstitial()
                    }



                    java.AD_TYPE_APPLOVIN + java.AD_TYPE_ADMOB -> {
                        val randomNumber = generateRandom(1,2)
                        Log.e(TAG, "randomNumber: $randomNumber")
                        if (randomNumber == 1) showApplovinInterstitial()
                        else showAdmobInters(activity)

                    }
                    java.AD_TYPE_APPLOVIN + java.AD_TYPE_FACEBOOK -> {
                        val randomNumber = generateRandom(1,2)
                        Log.e(TAG, "randomNumber: $randomNumber")
                        if (randomNumber == 1) showFbInters()
                        else showApplovinInterstitial()
                    }
                    java.AD_TYPE_APPLOVIN + java.AD_TYPE_UNITY -> {
                        val randomNumber = generateRandom(1,2)
                        Log.e(TAG, "randomNumber: $randomNumber")
                        if (randomNumber == 1) showUnityInters(activity)
                        else showApplovinInterstitial()
                    }
                    java.AD_TYPE_APPLOVIN + java.AD_TYPE_IRON -> {
                        val randomNumber = generateRandom(1,2)
                        Log.e(TAG, "randomNumber: $randomNumber")
                        if (randomNumber == 1) showIronSourceInterstitial(activity)
                        else showApplovinInterstitial()
                    }


                    java.AD_TYPE_ADMOB + java.AD_TYPE_UNITY + java.AD_TYPE_FACEBOOK-> {
                        val randomNumber = generateRandom(1,3)
                        Log.e(TAG, "randomNumber: $randomNumber")
                        when (randomNumber) {
                            1 -> {
                                showAdmobInters(activity)
                            }
                            2 -> {
                                showUnityInters(activity)
                            }
                            else -> {
                                showFbInters()
                            }
                        }
                    }
                    java.AD_TYPE_ADMOB + java.AD_TYPE_UNITY + java.AD_TYPE_IRON-> {
                        val randomNumber = generateRandom(1,2)
                        Log.e(TAG, "randomNumber: $randomNumber")
                        when (randomNumber) {
                            1 -> {
                                showAdmobInters(activity)
                            }
                            2 -> {
                                showUnityInters(activity)
                            }
                            else -> {
                                showIronSourceInterstitial(activity)
                            }
                        }
                    }
                    java.AD_TYPE_ADMOB + java.AD_TYPE_UNITY + java.AD_TYPE_APPLOVIN-> {
                        val randomNumber = generateRandom(1,2)
                        Log.e(TAG, "randomNumber: $randomNumber")
                        when (randomNumber) {
                            1 -> {
                                showAdmobInters(activity)
                            }
                            2 -> {
                                showUnityInters(activity)
                            }
                            else -> {
                                showApplovinInterstitial()
                            }
                        }
                    }
                }
            }
            else {
                counter++
            }
        }

        fun showInters(activity: Activity, adCallBackMethod: AdCallBack) {
            adCallBack = adCallBackMethod
            activityG = activity
            if (counter == interval.toInt()) {
                counter = 1

                when (selectedAd) {
                    "" -> {
                        moveToNextActivity(activity)
                    }
                    java.AD_TYPE_ADMOB -> {
                        showAdmobInters(activity)
                    }
                    java.AD_TYPE_FACEBOOK -> {
                        showFbInters()
                    }
                    java.AD_TYPE_UNITY -> {
                        showUnityInters(activity)
                    }
                    java.AD_TYPE_IRON -> {
                        showIronSourceInterstitial(activity)
                    }
                    java.AD_TYPE_APPLOVIN -> {
                        showApplovinInterstitial()
                    }

                    java.AD_TYPE_ADMOB + java.AD_TYPE_FACEBOOK -> {
                        Log.e(TAG, "selectedAd2TEst: ${java.AD_TYPE_ADMOB + java.AD_TYPE_FACEBOOK}")
                        val randomNumber = generateRandom(1,2)
                        Log.e(TAG, "randomNumber: $randomNumber")
                        if (randomNumber == 1) showAdmobInters(activity)
                        else showFbInters()

                    }
                    java.AD_TYPE_ADMOB + java.AD_TYPE_UNITY -> {
                        val randomNumber = generateRandom(1,2)
                        Log.e(TAG, "randomNumber: $randomNumber")
                        if (randomNumber == 1) showAdmobInters(activity)
                        else showUnityInters(activity)

                    }
                    java.AD_TYPE_ADMOB + java.AD_TYPE_IRON -> {
                        val randomNumber = generateRandom(1,2)
                        Log.e(TAG, "randomNumber: $randomNumber")
                        if (randomNumber == 1) showAdmobInters(activity)
                        else showIronSourceInterstitial(activity)

                    }
                    java.AD_TYPE_ADMOB + java.AD_TYPE_APPLOVIN -> {
                        val randomNumber = generateRandom(1,2)
                        Log.e(TAG, "randomNumber: $randomNumber")
                        if (randomNumber == 1) showAdmobInters(activity)
                        else showAdmobInters(activity)

                    }


                    java.AD_TYPE_FACEBOOK + java.AD_TYPE_ADMOB -> {
                        val randomNumber = generateRandom(1,2)
                        Log.e(TAG, "randomNumber: $randomNumber")
                        if (randomNumber == 1) showFbInters()
                        else showAdmobInters(activity)

                    }
                    java.AD_TYPE_FACEBOOK + java.AD_TYPE_UNITY -> {
                        val randomNumber = generateRandom(1,2)
                        Log.e(TAG, "randomNumber: $randomNumber")
                        if (randomNumber == 1) showFbInters()
                        else showUnityInters(activity)

                    }
                    java.AD_TYPE_FACEBOOK + java.AD_TYPE_IRON -> {
                        val randomNumber = generateRandom(1,2)
                        Log.e(TAG, "randomNumber: $randomNumber")
                        if (randomNumber == 1) showFbInters()
                        else showIronSourceInterstitial(activity)
                    }
                    java.AD_TYPE_FACEBOOK + java.AD_TYPE_APPLOVIN -> {
                        val randomNumber = generateRandom(1,2)
                        Log.e(TAG, "randomNumber: $randomNumber")
                        if (randomNumber == 1) showFbInters()
                        else showApplovinInterstitial()
                    }


                    java.AD_TYPE_UNITY + java.AD_TYPE_ADMOB -> {
                        val randomNumber = generateRandom(1,2)
                        Log.e(TAG, "randomNumber: $randomNumber")
                        if (randomNumber == 1) showUnityInters(activity)
                        else showAdmobInters(activity)
                    }
                    java.AD_TYPE_UNITY + java.AD_TYPE_FACEBOOK -> {
                        val randomNumber = generateRandom(1,2)
                        Log.e(TAG, "randomNumber: $randomNumber")
                        if (randomNumber == 1) showUnityInters(activity)
                        else showFbInters()
                    }
                    java.AD_TYPE_UNITY + java.AD_TYPE_IRON -> {
                        val randomNumber = generateRandom(1,2)
                        Log.e(TAG, "randomNumber: $randomNumber")
                        if (randomNumber == 1) showUnityInters(activity)
                        else showIronSourceInterstitial(activity)
                    }
                    java.AD_TYPE_UNITY + java.AD_TYPE_APPLOVIN -> {
                        val randomNumber = generateRandom(1,2)
                        Log.e(TAG, "randomNumber: $randomNumber")
                        if (randomNumber == 1) showUnityInters(activity)
                        else showApplovinInterstitial()
                    }



                    java.AD_TYPE_IRON + java.AD_TYPE_ADMOB -> {
                        val randomNumber = generateRandom(1,2)
                        Log.e(TAG, "randomNumber: $randomNumber")
                        if (randomNumber == 1) showIronSourceInterstitial(activity)
                        else showAdmobInters(activity)
                    }
                    java.AD_TYPE_IRON + java.AD_TYPE_FACEBOOK -> {
                        val randomNumber = generateRandom(1,2)
                        Log.e(TAG, "randomNumber: $randomNumber")
                        if (randomNumber == 1) showIronSourceInterstitial(activity)
                        else showFbInters()
                    }
                    java.AD_TYPE_IRON + java.AD_TYPE_UNITY -> {
                        val randomNumber = generateRandom(1,2)
                        Log.e(TAG, "randomNumber: $randomNumber")
                        if (randomNumber == 1) showIronSourceInterstitial(activity)
                        else showUnityInters(activity)
                    }
                    java.AD_TYPE_IRON + java.AD_TYPE_APPLOVIN -> {
                        val randomNumber = generateRandom(1,2)
                        Log.e(TAG, "randomNumber: $randomNumber")
                        if (randomNumber == 1) showIronSourceInterstitial(activity)
                        else showApplovinInterstitial()
                    }



                    java.AD_TYPE_APPLOVIN + java.AD_TYPE_ADMOB -> {
                        val randomNumber = generateRandom(1,2)
                        Log.e(TAG, "randomNumber: $randomNumber")
                        if (randomNumber == 1) showAdmobInters(activity)
                        else showAdmobInters(activity)

                    }
                    java.AD_TYPE_APPLOVIN + java.AD_TYPE_FACEBOOK -> {
                        val randomNumber = generateRandom(1,2)
                        Log.e(TAG, "randomNumber: $randomNumber")
                        if (randomNumber == 1) showFbInters()
                        else showApplovinInterstitial()
                    }
                    java.AD_TYPE_APPLOVIN + java.AD_TYPE_UNITY -> {
                        val randomNumber = generateRandom(1,2)
                        Log.e(TAG, "randomNumber: $randomNumber")
                        if (randomNumber == 1) showUnityInters(activity)
                        else showApplovinInterstitial()
                    }
                    java.AD_TYPE_APPLOVIN + java.AD_TYPE_IRON -> {
                        val randomNumber = generateRandom(1,2)
                        Log.e(TAG, "randomNumber: $randomNumber")
                        if (randomNumber == 1) showIronSourceInterstitial(activity)
                        else showApplovinInterstitial()
                    }


                    java.AD_TYPE_ADMOB + java.AD_TYPE_UNITY + java.AD_TYPE_FACEBOOK-> {
                        val randomNumber = generateRandom(1,3)
                        Log.e(TAG, "randomNumber: $randomNumber")
                        when (randomNumber) {
                            1 -> {
                                showAdmobInters(activity)
                            }
                            2 -> {
                                showUnityInters(activity)
                            }
                            else -> {
                                showFbInters()
                            }
                        }
                    }
                    java.AD_TYPE_ADMOB + java.AD_TYPE_UNITY + java.AD_TYPE_IRON-> {
                        val randomNumber = generateRandom(1,2)
                        Log.e(TAG, "randomNumber: $randomNumber")
                        when (randomNumber) {
                            1 -> {
                                showAdmobInters(activity)
                            }
                            2 -> {
                                showUnityInters(activity)
                            }
                            else -> {
                                showIronSourceInterstitial(activity)
                            }
                        }
                    }
                    java.AD_TYPE_ADMOB + java.AD_TYPE_UNITY + java.AD_TYPE_APPLOVIN-> {
                        val randomNumber = generateRandom(1,2)
                        Log.e(TAG, "randomNumber: $randomNumber")
                        when (randomNumber) {
                            1 -> {
                                showAdmobInters(activity)
                            }
                            2 -> {
                                showUnityInters(activity)
                            }
                            else -> {
                                showApplovinInterstitial()
                            }
                        }
                    }
                }
            }
            else {
                moveToNextActivity(activity)
                counter++
            }
        }

        private var counterSpinAdButton = 1
        fun showIntersForSpinAddBtn(activity: Activity, adCallBackMethod: AdCallBack) {
            adCallBack = adCallBackMethod
            activityG = activity
            if (counterSpinAdButton == 3) {
                counterSpinAdButton = 1

                when (selectedAd) {
                    "" -> {
                        moveToNextActivity(activity)
                    }
                    java.AD_TYPE_ADMOB -> {
                        showAdmobInters(activity)
                    }
                    java.AD_TYPE_FACEBOOK -> {
                        showFbInters()
                    }
                    java.AD_TYPE_UNITY -> {
                        showUnityInters(activity)
                    }
                    java.AD_TYPE_IRON -> {
                        showIronSourceInterstitial(activity)
                    }
                    java.AD_TYPE_APPLOVIN -> {
                        showApplovinInterstitial()
                    }

                    java.AD_TYPE_ADMOB + java.AD_TYPE_FACEBOOK -> {
                        Log.e(TAG, "selectedAd2TEst: ${java.AD_TYPE_ADMOB + java.AD_TYPE_FACEBOOK}")
                        val randomNumber = generateRandom(1,2)
                        Log.e(TAG, "randomNumber: $randomNumber")
                        if (randomNumber == 1) showAdmobInters(activity)
                        else showFbInters()

                    }
                    java.AD_TYPE_ADMOB + java.AD_TYPE_UNITY -> {
                        val randomNumber = generateRandom(1,2)
                        Log.e(TAG, "randomNumber: $randomNumber")
                        if (randomNumber == 1) showAdmobInters(activity)
                        else showUnityInters(activity)

                    }
                    java.AD_TYPE_ADMOB + java.AD_TYPE_IRON -> {
                        val randomNumber = generateRandom(1,2)
                        Log.e(TAG, "randomNumber: $randomNumber")
                        if (randomNumber == 1) showAdmobInters(activity)
                        else showIronSourceInterstitial(activity)

                    }
                    java.AD_TYPE_ADMOB + java.AD_TYPE_APPLOVIN -> {
                        val randomNumber = generateRandom(1,2)
                        Log.e(TAG, "randomNumber: $randomNumber")
                        if (randomNumber == 1) showAdmobInters(activity)
                        else showAdmobInters(activity)

                    }


                    java.AD_TYPE_FACEBOOK + java.AD_TYPE_ADMOB -> {
                        val randomNumber = generateRandom(1,2)
                        Log.e(TAG, "randomNumber: $randomNumber")
                        if (randomNumber == 1) showFbInters()
                        else showAdmobInters(activity)

                    }
                    java.AD_TYPE_FACEBOOK + java.AD_TYPE_UNITY -> {
                        val randomNumber = generateRandom(1,2)
                        Log.e(TAG, "randomNumber: $randomNumber")
                        if (randomNumber == 1) showFbInters()
                        else showUnityInters(activity)

                    }
                    java.AD_TYPE_FACEBOOK + java.AD_TYPE_IRON -> {
                        val randomNumber = generateRandom(1,2)
                        Log.e(TAG, "randomNumber: $randomNumber")
                        if (randomNumber == 1) showFbInters()
                        else showIronSourceInterstitial(activity)
                    }
                    java.AD_TYPE_FACEBOOK + java.AD_TYPE_APPLOVIN -> {
                        val randomNumber = generateRandom(1,2)
                        Log.e(TAG, "randomNumber: $randomNumber")
                        if (randomNumber == 1) showFbInters()
                        else showApplovinInterstitial()
                    }


                    java.AD_TYPE_UNITY + java.AD_TYPE_ADMOB -> {
                        val randomNumber = generateRandom(1,2)
                        Log.e(TAG, "randomNumber: $randomNumber")
                        if (randomNumber == 1) showUnityInters(activity)
                        else showAdmobInters(activity)
                    }
                    java.AD_TYPE_UNITY + java.AD_TYPE_FACEBOOK -> {
                        val randomNumber = generateRandom(1,2)
                        Log.e(TAG, "randomNumber: $randomNumber")
                        if (randomNumber == 1) showUnityInters(activity)
                        else showFbInters()
                    }
                    java.AD_TYPE_UNITY + java.AD_TYPE_IRON -> {
                        val randomNumber = generateRandom(1,2)
                        Log.e(TAG, "randomNumber: $randomNumber")
                        if (randomNumber == 1) showUnityInters(activity)
                        else showIronSourceInterstitial(activity)
                    }
                    java.AD_TYPE_UNITY + java.AD_TYPE_APPLOVIN -> {
                        val randomNumber = generateRandom(1,2)
                        Log.e(TAG, "randomNumber: $randomNumber")
                        if (randomNumber == 1) showUnityInters(activity)
                        else showApplovinInterstitial()
                    }



                    java.AD_TYPE_IRON + java.AD_TYPE_ADMOB -> {
                        val randomNumber = generateRandom(1,2)
                        Log.e(TAG, "randomNumber: $randomNumber")
                        if (randomNumber == 1) showIronSourceInterstitial(activity)
                        else showAdmobInters(activity)
                    }
                    java.AD_TYPE_IRON + java.AD_TYPE_FACEBOOK -> {
                        val randomNumber = generateRandom(1,2)
                        Log.e(TAG, "randomNumber: $randomNumber")
                        if (randomNumber == 1) showIronSourceInterstitial(activity)
                        else showFbInters()
                    }
                    java.AD_TYPE_IRON + java.AD_TYPE_UNITY -> {
                        val randomNumber = generateRandom(1,2)
                        Log.e(TAG, "randomNumber: $randomNumber")
                        if (randomNumber == 1) showIronSourceInterstitial(activity)
                        else showUnityInters(activity)
                    }
                    java.AD_TYPE_IRON + java.AD_TYPE_APPLOVIN -> {
                        val randomNumber = generateRandom(1,2)
                        Log.e(TAG, "randomNumber: $randomNumber")
                        if (randomNumber == 1) showIronSourceInterstitial(activity)
                        else showApplovinInterstitial()
                    }



                    java.AD_TYPE_APPLOVIN + java.AD_TYPE_ADMOB -> {
                        val randomNumber = generateRandom(1,2)
                        Log.e(TAG, "randomNumber: $randomNumber")
                        if (randomNumber == 1) showAdmobInters(activity)
                        else showAdmobInters(activity)

                    }
                    java.AD_TYPE_APPLOVIN + java.AD_TYPE_FACEBOOK -> {
                        val randomNumber = generateRandom(1,2)
                        Log.e(TAG, "randomNumber: $randomNumber")
                        if (randomNumber == 1) showFbInters()
                        else showApplovinInterstitial()
                    }
                    java.AD_TYPE_APPLOVIN + java.AD_TYPE_UNITY -> {
                        val randomNumber = generateRandom(1,2)
                        Log.e(TAG, "randomNumber: $randomNumber")
                        if (randomNumber == 1) showUnityInters(activity)
                        else showApplovinInterstitial()
                    }
                    java.AD_TYPE_APPLOVIN + java.AD_TYPE_IRON -> {
                        val randomNumber = generateRandom(1,2)
                        Log.e(TAG, "randomNumber: $randomNumber")
                        if (randomNumber == 1) showIronSourceInterstitial(activity)
                        else showApplovinInterstitial()
                    }


                    java.AD_TYPE_ADMOB + java.AD_TYPE_UNITY + java.AD_TYPE_FACEBOOK-> {
                        val randomNumber = generateRandom(1,3)
                        Log.e(TAG, "randomNumber: $randomNumber")
                        when (randomNumber) {
                            1 -> {
                                showAdmobInters(activity)
                            }
                            2 -> {
                                showUnityInters(activity)
                            }
                            else -> {
                                showFbInters()
                            }
                        }
                    }
                    java.AD_TYPE_ADMOB + java.AD_TYPE_UNITY + java.AD_TYPE_IRON-> {
                        val randomNumber = generateRandom(1,2)
                        Log.e(TAG, "randomNumber: $randomNumber")
                        when (randomNumber) {
                            1 -> {
                                showAdmobInters(activity)
                            }
                            2 -> {
                                showUnityInters(activity)
                            }
                            else -> {
                                showIronSourceInterstitial(activity)
                            }
                        }
                    }
                    java.AD_TYPE_ADMOB + java.AD_TYPE_UNITY + java.AD_TYPE_APPLOVIN-> {
                        val randomNumber = generateRandom(1,2)
                        Log.e(TAG, "randomNumber: $randomNumber")
                        when (randomNumber) {
                            1 -> {
                                showAdmobInters(activity)
                            }
                            2 -> {
                                showUnityInters(activity)
                            }
                            else -> {
                                showApplovinInterstitial()
                            }
                        }
                    }
                }
            }
            else {
                moveToNextActivity(activity)
                counterSpinAdButton++
            }
        }

        private var counterWithCounterAndCallBack = 1
        fun showIntersWithCounterAndCallBack(activity: Activity, interval: Int, adCallBackMethod: AdCallBack) {
            adCallBack = adCallBackMethod
            activityG = activity
            cG = null
            intentG = null
            if (counterWithCounterAndCallBack == interval) {
                counterWithCounterAndCallBack = 1

                when (selectedAd) {
                    "" -> {
                        moveToNextActivity(activity)
                    }
                    java.AD_TYPE_ADMOB -> {
                        showAdmobInters(activity)
                    }
                    java.AD_TYPE_FACEBOOK -> {
                        showFbInters()
                    }
                    java.AD_TYPE_UNITY -> {
                        showUnityInters(activity)
                    }
                    java.AD_TYPE_IRON -> {
                        showIronSourceInterstitial(activity)
                    }
                    java.AD_TYPE_APPLOVIN -> {
                        showApplovinInterstitial()
                    }

                    java.AD_TYPE_ADMOB + java.AD_TYPE_FACEBOOK -> {
                        Log.e(TAG, "selectedAd2TEst: ${java.AD_TYPE_ADMOB + java.AD_TYPE_FACEBOOK}")
                        val randomNumber = generateRandom(1,2)
                        Log.e(TAG, "randomNumber: $randomNumber")
                        if (randomNumber == 1) showAdmobInters(activity)
                        else showFbInters()

                    }
                    java.AD_TYPE_ADMOB + java.AD_TYPE_UNITY -> {
                        val randomNumber = generateRandom(1,2)
                        Log.e(TAG, "randomNumber: $randomNumber")
                        if (randomNumber == 1) showAdmobInters(activity)
                        else showUnityInters(activity)

                    }
                    java.AD_TYPE_ADMOB + java.AD_TYPE_IRON -> {
                        val randomNumber = generateRandom(1,2)
                        Log.e(TAG, "randomNumber: $randomNumber")
                        if (randomNumber == 1) showAdmobInters(activity)
                        else showIronSourceInterstitial(activity)

                    }
                    java.AD_TYPE_ADMOB + java.AD_TYPE_APPLOVIN -> {
                        val randomNumber = generateRandom(1,2)
                        Log.e(TAG, "randomNumber: $randomNumber")
                        if (randomNumber == 1) showAdmobInters(activity)
                        else showAdmobInters(activity)

                    }


                    java.AD_TYPE_FACEBOOK + java.AD_TYPE_ADMOB -> {
                        val randomNumber = generateRandom(1,2)
                        Log.e(TAG, "randomNumber: $randomNumber")
                        if (randomNumber == 1) showFbInters()
                        else showAdmobInters(activity)

                    }
                    java.AD_TYPE_FACEBOOK + java.AD_TYPE_UNITY -> {
                        val randomNumber = generateRandom(1,2)
                        Log.e(TAG, "randomNumber: $randomNumber")
                        if (randomNumber == 1) showFbInters()
                        else showUnityInters(activity)

                    }
                    java.AD_TYPE_FACEBOOK + java.AD_TYPE_IRON -> {
                        val randomNumber = generateRandom(1,2)
                        Log.e(TAG, "randomNumber: $randomNumber")
                        if (randomNumber == 1) showFbInters()
                        else showIronSourceInterstitial(activity)
                    }
                    java.AD_TYPE_FACEBOOK + java.AD_TYPE_APPLOVIN -> {
                        val randomNumber = generateRandom(1,2)
                        Log.e(TAG, "randomNumber: $randomNumber")
                        if (randomNumber == 1) showFbInters()
                        else showApplovinInterstitial()
                    }


                    java.AD_TYPE_UNITY + java.AD_TYPE_ADMOB -> {
                        val randomNumber = generateRandom(1,2)
                        Log.e(TAG, "randomNumber: $randomNumber")
                        if (randomNumber == 1) showUnityInters(activity)
                        else showAdmobInters(activity)
                    }
                    java.AD_TYPE_UNITY + java.AD_TYPE_FACEBOOK -> {
                        val randomNumber = generateRandom(1,2)
                        Log.e(TAG, "randomNumber: $randomNumber")
                        if (randomNumber == 1) showUnityInters(activity)
                        else showFbInters()
                    }
                    java.AD_TYPE_UNITY + java.AD_TYPE_IRON -> {
                        val randomNumber = generateRandom(1,2)
                        Log.e(TAG, "randomNumber: $randomNumber")
                        if (randomNumber == 1) showUnityInters(activity)
                        else showIronSourceInterstitial(activity)
                    }
                    java.AD_TYPE_UNITY + java.AD_TYPE_APPLOVIN -> {
                        val randomNumber = generateRandom(1,2)
                        Log.e(TAG, "randomNumber: $randomNumber")
                        if (randomNumber == 1) showUnityInters(activity)
                        else showApplovinInterstitial()
                    }



                    java.AD_TYPE_IRON + java.AD_TYPE_ADMOB -> {
                        val randomNumber = generateRandom(1,2)
                        Log.e(TAG, "randomNumber: $randomNumber")
                        if (randomNumber == 1) showIronSourceInterstitial(activity)
                        else showAdmobInters(activity)
                    }
                    java.AD_TYPE_IRON + java.AD_TYPE_FACEBOOK -> {
                        val randomNumber = generateRandom(1,2)
                        Log.e(TAG, "randomNumber: $randomNumber")
                        if (randomNumber == 1) showIronSourceInterstitial(activity)
                        else showFbInters()
                    }
                    java.AD_TYPE_IRON + java.AD_TYPE_UNITY -> {
                        val randomNumber = generateRandom(1,2)
                        Log.e(TAG, "randomNumber: $randomNumber")
                        if (randomNumber == 1) showIronSourceInterstitial(activity)
                        else showUnityInters(activity)
                    }
                    java.AD_TYPE_IRON + java.AD_TYPE_APPLOVIN -> {
                        val randomNumber = generateRandom(1,2)
                        Log.e(TAG, "randomNumber: $randomNumber")
                        if (randomNumber == 1) showIronSourceInterstitial(activity)
                        else showApplovinInterstitial()
                    }



                    java.AD_TYPE_APPLOVIN + java.AD_TYPE_ADMOB -> {
                        val randomNumber = generateRandom(1,2)
                        Log.e(TAG, "randomNumber: $randomNumber")
                        if (randomNumber == 1) showAdmobInters(activity)
                        else showAdmobInters(activity)

                    }
                    java.AD_TYPE_APPLOVIN + java.AD_TYPE_FACEBOOK -> {
                        val randomNumber = generateRandom(1,2)
                        Log.e(TAG, "randomNumber: $randomNumber")
                        if (randomNumber == 1) showFbInters()
                        else showApplovinInterstitial()
                    }
                    java.AD_TYPE_APPLOVIN + java.AD_TYPE_UNITY -> {
                        val randomNumber = generateRandom(1,2)
                        Log.e(TAG, "randomNumber: $randomNumber")
                        if (randomNumber == 1) showUnityInters(activity)
                        else showApplovinInterstitial()
                    }
                    java.AD_TYPE_APPLOVIN + java.AD_TYPE_IRON -> {
                        val randomNumber = generateRandom(1,2)
                        Log.e(TAG, "randomNumber: $randomNumber")
                        if (randomNumber == 1) showIronSourceInterstitial(activity)
                        else showApplovinInterstitial()
                    }


                    java.AD_TYPE_ADMOB + java.AD_TYPE_UNITY + java.AD_TYPE_FACEBOOK-> {
                        val randomNumber = generateRandom(1,3)
                        Log.e(TAG, "randomNumber: $randomNumber")
                        when (randomNumber) {
                            1 -> {
                                showAdmobInters(activity)
                            }
                            2 -> {
                                showUnityInters(activity)
                            }
                            else -> {
                                showFbInters()
                            }
                        }
                    }
                    java.AD_TYPE_ADMOB + java.AD_TYPE_UNITY + java.AD_TYPE_IRON-> {
                        val randomNumber = generateRandom(1,2)
                        Log.e(TAG, "randomNumber: $randomNumber")
                        when (randomNumber) {
                            1 -> {
                                showAdmobInters(activity)
                            }
                            2 -> {
                                showUnityInters(activity)
                            }
                            else -> {
                                showIronSourceInterstitial(activity)
                            }
                        }
                    }
                    java.AD_TYPE_ADMOB + java.AD_TYPE_UNITY + java.AD_TYPE_APPLOVIN-> {
                        val randomNumber = generateRandom(1,2)
                        Log.e(TAG, "randomNumber: $randomNumber")
                        when (randomNumber) {
                            1 -> {
                                showAdmobInters(activity)
                            }
                            2 -> {
                                showUnityInters(activity)
                            }
                            else -> {
                                showApplovinInterstitial()
                            }
                        }
                    }
                }
            }
            else {
                moveToNextActivity(activity)
                counterWithCounterAndCallBack++
            }
        }


        private var counterScratchAdButton = 1
        fun showIntersForScratchddBtn(activity: Activity, adCallBackMethod: AdCallBack) {
            adCallBack = adCallBackMethod
            activityG = activity
            if (counterScratchAdButton == 2) {
                counterScratchAdButton = 1

                when (selectedAd) {
                    "" -> {
                        moveToNextActivity(activity)
                    }
                    java.AD_TYPE_ADMOB -> {
                        showAdmobInters(activity)
                    }
                    java.AD_TYPE_FACEBOOK -> {
                        showFbInters()
                    }
                    java.AD_TYPE_UNITY -> {
                        showUnityInters(activity)
                    }
                    java.AD_TYPE_IRON -> {
                        showIronSourceInterstitial(activity)
                    }
                    java.AD_TYPE_APPLOVIN -> {
                        showApplovinInterstitial()
                    }

                    java.AD_TYPE_ADMOB + java.AD_TYPE_FACEBOOK -> {
                        Log.e(TAG, "selectedAd2TEst: ${java.AD_TYPE_ADMOB + java.AD_TYPE_FACEBOOK}")
                        val randomNumber = generateRandom(1,2)
                        Log.e(TAG, "randomNumber: $randomNumber")
                        if (randomNumber == 1) showAdmobInters(activity)
                        else showFbInters()

                    }
                    java.AD_TYPE_ADMOB + java.AD_TYPE_UNITY -> {
                        val randomNumber = generateRandom(1,2)
                        Log.e(TAG, "randomNumber: $randomNumber")
                        if (randomNumber == 1) showAdmobInters(activity)
                        else showUnityInters(activity)

                    }
                    java.AD_TYPE_ADMOB + java.AD_TYPE_IRON -> {
                        val randomNumber = generateRandom(1,2)
                        Log.e(TAG, "randomNumber: $randomNumber")
                        if (randomNumber == 1) showAdmobInters(activity)
                        else showIronSourceInterstitial(activity)

                    }
                    java.AD_TYPE_ADMOB + java.AD_TYPE_APPLOVIN -> {
                        val randomNumber = generateRandom(1,2)
                        Log.e(TAG, "randomNumber: $randomNumber")
                        if (randomNumber == 1) showAdmobInters(activity)
                        else showAdmobInters(activity)

                    }


                    java.AD_TYPE_FACEBOOK + java.AD_TYPE_ADMOB -> {
                        val randomNumber = generateRandom(1,2)
                        Log.e(TAG, "randomNumber: $randomNumber")
                        if (randomNumber == 1) showFbInters()
                        else showAdmobInters(activity)

                    }
                    java.AD_TYPE_FACEBOOK + java.AD_TYPE_UNITY -> {
                        val randomNumber = generateRandom(1,2)
                        Log.e(TAG, "randomNumber: $randomNumber")
                        if (randomNumber == 1) showFbInters()
                        else showUnityInters(activity)

                    }
                    java.AD_TYPE_FACEBOOK + java.AD_TYPE_IRON -> {
                        val randomNumber = generateRandom(1,2)
                        Log.e(TAG, "randomNumber: $randomNumber")
                        if (randomNumber == 1) showFbInters()
                        else showIronSourceInterstitial(activity)
                    }
                    java.AD_TYPE_FACEBOOK + java.AD_TYPE_APPLOVIN -> {
                        val randomNumber = generateRandom(1,2)
                        Log.e(TAG, "randomNumber: $randomNumber")
                        if (randomNumber == 1) showFbInters()
                        else showApplovinInterstitial()
                    }


                    java.AD_TYPE_UNITY + java.AD_TYPE_ADMOB -> {
                        val randomNumber = generateRandom(1,2)
                        Log.e(TAG, "randomNumber: $randomNumber")
                        if (randomNumber == 1) showUnityInters(activity)
                        else showAdmobInters(activity)
                    }
                    java.AD_TYPE_UNITY + java.AD_TYPE_FACEBOOK -> {
                        val randomNumber = generateRandom(1,2)
                        Log.e(TAG, "randomNumber: $randomNumber")
                        if (randomNumber == 1) showUnityInters(activity)
                        else showFbInters()
                    }
                    java.AD_TYPE_UNITY + java.AD_TYPE_IRON -> {
                        val randomNumber = generateRandom(1,2)
                        Log.e(TAG, "randomNumber: $randomNumber")
                        if (randomNumber == 1) showUnityInters(activity)
                        else showIronSourceInterstitial(activity)
                    }
                    java.AD_TYPE_UNITY + java.AD_TYPE_APPLOVIN -> {
                        val randomNumber = generateRandom(1,2)
                        Log.e(TAG, "randomNumber: $randomNumber")
                        if (randomNumber == 1) showUnityInters(activity)
                        else showApplovinInterstitial()
                    }



                    java.AD_TYPE_IRON + java.AD_TYPE_ADMOB -> {
                        val randomNumber = generateRandom(1,2)
                        Log.e(TAG, "randomNumber: $randomNumber")
                        if (randomNumber == 1) showIronSourceInterstitial(activity)
                        else showAdmobInters(activity)
                    }
                    java.AD_TYPE_IRON + java.AD_TYPE_FACEBOOK -> {
                        val randomNumber = generateRandom(1,2)
                        Log.e(TAG, "randomNumber: $randomNumber")
                        if (randomNumber == 1) showIronSourceInterstitial(activity)
                        else showFbInters()
                    }
                    java.AD_TYPE_IRON + java.AD_TYPE_UNITY -> {
                        val randomNumber = generateRandom(1,2)
                        Log.e(TAG, "randomNumber: $randomNumber")
                        if (randomNumber == 1) showIronSourceInterstitial(activity)
                        else showUnityInters(activity)
                    }
                    java.AD_TYPE_IRON + java.AD_TYPE_APPLOVIN -> {
                        val randomNumber = generateRandom(1,2)
                        Log.e(TAG, "randomNumber: $randomNumber")
                        if (randomNumber == 1) showIronSourceInterstitial(activity)
                        else showApplovinInterstitial()
                    }



                    java.AD_TYPE_APPLOVIN + java.AD_TYPE_ADMOB -> {
                        val randomNumber = generateRandom(1,2)
                        Log.e(TAG, "randomNumber: $randomNumber")
                        if (randomNumber == 1) showAdmobInters(activity)
                        else showAdmobInters(activity)

                    }
                    java.AD_TYPE_APPLOVIN + java.AD_TYPE_FACEBOOK -> {
                        val randomNumber = generateRandom(1,2)
                        Log.e(TAG, "randomNumber: $randomNumber")
                        if (randomNumber == 1) showFbInters()
                        else showApplovinInterstitial()
                    }
                    java.AD_TYPE_APPLOVIN + java.AD_TYPE_UNITY -> {
                        val randomNumber = generateRandom(1,2)
                        Log.e(TAG, "randomNumber: $randomNumber")
                        if (randomNumber == 1) showUnityInters(activity)
                        else showApplovinInterstitial()
                    }
                    java.AD_TYPE_APPLOVIN + java.AD_TYPE_IRON -> {
                        val randomNumber = generateRandom(1,2)
                        Log.e(TAG, "randomNumber: $randomNumber")
                        if (randomNumber == 1) showIronSourceInterstitial(activity)
                        else showApplovinInterstitial()
                    }


                    java.AD_TYPE_ADMOB + java.AD_TYPE_UNITY + java.AD_TYPE_FACEBOOK-> {
                        val randomNumber = generateRandom(1,3)
                        Log.e(TAG, "randomNumber: $randomNumber")
                        when (randomNumber) {
                            1 -> {
                                showAdmobInters(activity)
                            }
                            2 -> {
                                showUnityInters(activity)
                            }
                            else -> {
                                showFbInters()
                            }
                        }
                    }
                    java.AD_TYPE_ADMOB + java.AD_TYPE_UNITY + java.AD_TYPE_IRON-> {
                        val randomNumber = generateRandom(1,2)
                        Log.e(TAG, "randomNumber: $randomNumber")
                        when (randomNumber) {
                            1 -> {
                                showAdmobInters(activity)
                            }
                            2 -> {
                                showUnityInters(activity)
                            }
                            else -> {
                                showIronSourceInterstitial(activity)
                            }
                        }
                    }
                    java.AD_TYPE_ADMOB + java.AD_TYPE_UNITY + java.AD_TYPE_APPLOVIN-> {
                        val randomNumber = generateRandom(1,2)
                        Log.e(TAG, "randomNumber: $randomNumber")
                        when (randomNumber) {
                            1 -> {
                                showAdmobInters(activity)
                            }
                            2 -> {
                                showUnityInters(activity)
                            }
                            else -> {
                                showApplovinInterstitial()
                            }
                        }
                    }
                }
            }
            else {
                moveToNextActivity(activity)
                counterScratchAdButton++
            }
        }

        private fun showUnityInters(activity: Activity) {
            val myAdsListener = UnityAdsListener()
            UnityAds.show(activity, unityIntersId, myAdsListener)
        }

        private fun showFbInters() {
            if (fanInterstitialAd == null) {
                if (cG!= null){
                    if (isClearStack) {
                        val newIntent = Intent(activityG, cG)
                        newIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                        newIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        activityG?.startActivity(newIntent)
                    }
                    else {
                        val newIntent = Intent(activityG, cG)
                        activityG?.startActivity(newIntent)
                    }
                }
            }
            else if (!fanInterstitialAd!!.isAdLoaded) {
                if (cG!= null){
                    if (isClearStack) {
                        val newIntent = Intent(activityG, cG)
                        newIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                        newIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        activityG?.startActivity(newIntent)
                    }
                    else {
                        val newIntent = Intent(activityG, cG)
                        activityG?.startActivity(newIntent)
                    }
                }
            }
            else if (fanInterstitialAd!!.isAdInvalidated) {
                if (cG!= null){
                    if (isClearStack) {
                        val newIntent = Intent(activityG, cG)
                        newIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                        newIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        activityG?.startActivity(newIntent)
                    }
                    else {
                        val newIntent = Intent(activityG, cG)
                        activityG?.startActivity(newIntent)
                    }
                }
            }
            else {
                fanInterstitialAd!!.show()
            }

        }

        private fun showAdmobInters(activity: Activity) {

            if (admobInterstitialAd != null) {
                Log.e(TAG, "showAdmobInters: not null " )
                admobInterstitialAd?.show(activity)
            }
            else {
                Log.e(TAG, "showAdmobInters: null" )
                moveToNextActivity(activity)
            }
        }


        private var mAdView : com.google.android.gms.ads.AdView ?= null
        private fun loadAdmobBanner(context: Context, relativeLayout: FrameLayout){
            mAdView = com.google.android.gms.ads.AdView(context)
            mAdView?.adSize = com.google.android.gms.ads.AdSize.BANNER
            mAdView?.adUnitId = admobBannerId
            val adRequest = AdRequest.Builder().build()
            mAdView?.loadAd(adRequest)
            relativeLayout.addView(mAdView)

        }

        private var mAdViewFacebook: com.facebook.ads.AdView?= null
        private fun loadFacebookBanner(context: Context, relativeLayout: FrameLayout) {
            mAdViewFacebook = com.facebook.ads.AdView(context, fanBannerId, AdSize.BANNER_HEIGHT_50)
            relativeLayout.addView(mAdViewFacebook)
            mAdViewFacebook!!.loadAd()
        }


        private fun loadUnityBanner(context: Activity, adView: FrameLayout) {
            val bannerListener = UnityBannerListener()
//        UnityAds.initialize(context, unityGameId, null, unityTestMode, true)
            val bottomBanner = BannerView(context, unityBannerId, UnityBannerSize(320, 50))
            bottomBanner.listener = bannerListener
            adView.addView(bottomBanner)
            bottomBanner.load()
        }

        private class UnityBannerListener : BannerView.IListener {
            override fun onBannerLoaded(bannerAdView: BannerView) {
                // Called when the banner is loaded.

            }

            override fun onBannerFailedToLoad(bannerAdView: BannerView, errorInfo: BannerErrorInfo) {
                // Note that the BannerErrorInfo object can indicate a no fill (see API documentation).

            }

            override fun onBannerClick(bannerAdView: BannerView) {
                // Called when a banner is clicked.
            }

            override fun onBannerLeftApplication(bannerAdView: BannerView) {
                // Called when the banner links out of the application.
            }
        }


        private fun loadIronBanner(context: Activity, bannerContainer: FrameLayout) {
            //IronSource.init(this, "YOUR_APP_KEY", IronSource.AD_UNIT.BANNER)
            val banner = IronSource.createBanner(context, ISBannerSize.BANNER)
            val layoutParams = FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.WRAP_CONTENT
            )
            bannerContainer.addView(banner, 0, layoutParams)
            banner.bannerListener = object : BannerListener {
                override fun onBannerAdLoaded() {
                    // Called after a banner ad has been successfully loaded
                }

                override fun onBannerAdLoadFailed(error: IronSourceError?) {
                    // Called after a banner has attempted to load an ad but failed.
                    context.runOnUiThread { bannerContainer.removeAllViews() }
                    Log.e(TAG, "IronOnBannerAdLoadFailed: ${error?.errorMessage}")
                }

                override fun onBannerAdClicked() {
                    // Called after a banner has been clicked.
                }

                override fun onBannerAdScreenPresented() {
                    // Called when a banner is about to present a full screen content.
                }

                override fun onBannerAdScreenDismissed() {
                    // Called after a full screen content has been dismissed
                }

                override fun onBannerAdLeftApplication() {
                    // Called when a user would be taken out of the application context.
                }
            }
            IronSource.loadBanner(banner)

        }

        fun showBanner(context: Context, frameLayout: FrameLayout) {
            when (selectedAd) {
                java.AD_TYPE_ADMOB -> {
                    loadAdmobBanner(context, frameLayout)
                }
                java.AD_TYPE_FACEBOOK -> {
                    loadFacebookBanner(context, frameLayout)
                }
                java.AD_TYPE_UNITY -> {
                    loadUnityBanner(context as Activity, frameLayout)

                }
                java.AD_TYPE_IRON -> {
                    loadIronBanner(context as Activity, frameLayout)
                }
                java.AD_TYPE_APPLOVIN -> {
                    showApplovinBanner(context as Activity, frameLayout)
                }
                java.AD_TYPE_ADMOB + java.AD_TYPE_FACEBOOK -> {
                    Log.e(TAG, "selectedAd2TEst: ${java.AD_TYPE_ADMOB + java.AD_TYPE_FACEBOOK}")
                    val randomNumber = generateRandom(1,2)

                    if (randomNumber == 1) {
                        loadAdmobBanner(context, frameLayout)

                    }
                    else {
                        loadFacebookBanner(context, frameLayout)

                    }
                }
                java.AD_TYPE_ADMOB + java.AD_TYPE_UNITY -> {
                    val randomNumber = generateRandom(1,2)

                    if (randomNumber == 1) {
                        loadAdmobBanner(context, frameLayout)

                    }
                    else {
                        loadUnityBanner(context as Activity, frameLayout)

                    }
                }
                java.AD_TYPE_ADMOB + java.AD_TYPE_IRON -> {
                    val randomNumber = generateRandom(1,2)

                    if (randomNumber == 1) {
                        loadAdmobBanner(context, frameLayout)

                    }
                    else {
                        loadIronBanner(context as Activity, frameLayout)

                    }
                }
                java.AD_TYPE_ADMOB + java.AD_TYPE_APPLOVIN -> {
                    val randomNumber = generateRandom(1,2)

                    if (randomNumber == 1) {
                        loadAdmobBanner(context, frameLayout)

                    }
                    else {
                        showApplovinBanner(context as Activity, frameLayout)
                    }
                }


                java.AD_TYPE_FACEBOOK + java.AD_TYPE_ADMOB -> {
                    val randomNumber = generateRandom(1,2)

                    if (randomNumber == 1) {
                        loadFacebookBanner(context, frameLayout)

                    }
                    else {
                        loadAdmobBanner(context, frameLayout)

                    }
                }
                java.AD_TYPE_FACEBOOK + java.AD_TYPE_UNITY -> {
                    val randomNumber = generateRandom(1,2)

                    if (randomNumber == 1) {
                        loadFacebookBanner(context, frameLayout)

                    }
                    else {
                        loadUnityBanner(context as Activity, frameLayout)

                    }
                }
                java.AD_TYPE_FACEBOOK + java.AD_TYPE_IRON -> {
                    val randomNumber = generateRandom(1,2)

                    if (randomNumber == 1) {
                        loadFacebookBanner(context, frameLayout)

                    }
                    else {
                        loadIronBanner(context as Activity, frameLayout)

                    }
                }
                java.AD_TYPE_FACEBOOK + java.AD_TYPE_APPLOVIN -> {
                    val randomNumber = generateRandom(1,2)

                    if (randomNumber == 1) {
                        loadFacebookBanner(context, frameLayout)

                    }
                    else {
                        showApplovinBanner(context as Activity, frameLayout)

                    }
                }


                java.AD_TYPE_UNITY + java.AD_TYPE_ADMOB -> {
                    val randomNumber = generateRandom(1,2)

                    if (randomNumber == 1) {
                        loadUnityBanner(context as Activity, frameLayout)

                    }
                    else {
                        loadAdmobBanner(context, frameLayout)

                    }
                }
                java.AD_TYPE_UNITY + java.AD_TYPE_FACEBOOK -> {
                    val randomNumber = generateRandom(1,2)


                    if (randomNumber == 1) {
                        loadUnityBanner(context as Activity, frameLayout)

                    }
                    else {
                        loadFacebookBanner(context, frameLayout)

                    }
                }
                java.AD_TYPE_UNITY + java.AD_TYPE_IRON -> {
                    val randomNumber = generateRandom(1,2)

                    if (randomNumber == 1) {
                        loadUnityBanner(context as Activity, frameLayout)

                    }
                    else {
                        loadIronBanner(context as Activity, frameLayout)

                    }
                }
                java.AD_TYPE_UNITY + java.AD_TYPE_APPLOVIN -> {
                    val randomNumber = generateRandom(1,2)

                    if (randomNumber == 1) {
                        loadUnityBanner(context as Activity, frameLayout)

                    }
                    else {
                        showApplovinBanner(context as Activity, frameLayout)

                    }
                }



                java.AD_TYPE_IRON + java.AD_TYPE_ADMOB -> {
                    val randomNumber = generateRandom(1,2)

                    if (randomNumber == 1) {
                        loadIronBanner(context as Activity, frameLayout)

                    }
                    else {
                        loadAdmobBanner(context, frameLayout)

                    }
                }
                java.AD_TYPE_IRON + java.AD_TYPE_FACEBOOK -> {
                    val randomNumber = generateRandom(1,2)

                    if (randomNumber == 1) {
                        loadIronBanner(context as Activity, frameLayout)

                    }
                    else {
                        loadFacebookBanner(context, frameLayout)

                    }
                }
                java.AD_TYPE_IRON + java.AD_TYPE_UNITY -> {
                    val randomNumber = generateRandom(1,2)

                    if (randomNumber == 1) {
                        loadIronBanner(context as Activity, frameLayout)
                    }
                    else {
                        loadUnityBanner(context as Activity, frameLayout)

                    }
                }
                java.AD_TYPE_IRON + java.AD_TYPE_APPLOVIN -> {
                    val randomNumber = generateRandom(1,2)

                    if (randomNumber == 1) {
                        loadIronBanner(context as Activity, frameLayout)
                    }
                    else {
                        showApplovinBanner(context as Activity, frameLayout)

                    }
                }


                java.AD_TYPE_APPLOVIN + java.AD_TYPE_ADMOB -> {
                    val randomNumber = generateRandom(1,2)

                    if (randomNumber == 1) {
                        loadAdmobBanner(context, frameLayout)

                    }
                    else {
                        showApplovinBanner(context as Activity, frameLayout)
                    }
                }
                java.AD_TYPE_APPLOVIN + java.AD_TYPE_FACEBOOK -> {
                    val randomNumber = generateRandom(1,2)

                    if (randomNumber == 1) {
                        loadFacebookBanner(context, frameLayout)

                    }
                    else {
                        showApplovinBanner(context as Activity, frameLayout)

                    }
                }
                java.AD_TYPE_APPLOVIN + java.AD_TYPE_UNITY -> {
                    val randomNumber = generateRandom(1,2)

                    if (randomNumber == 1) {
                        loadUnityBanner(context as Activity, frameLayout)

                    }
                    else {
                        showApplovinBanner(context as Activity, frameLayout)

                    }
                }
                java.AD_TYPE_APPLOVIN + java.AD_TYPE_IRON -> {
                    val randomNumber = generateRandom(1,2)

                    if (randomNumber == 1) {
                        loadIronBanner(context as Activity, frameLayout)
                    }
                    else {
                        showApplovinBanner(context as Activity, frameLayout)

                    }
                }

                java.AD_TYPE_ADMOB + java.AD_TYPE_UNITY + java.AD_TYPE_FACEBOOK-> {
                    val randomNumber = generateRandom(1,3)
                    Log.e(TAG, "randomNumber: $randomNumber")
                    when (randomNumber) {
                        1 -> {
                            loadAdmobBanner(context, frameLayout)
                        }
                        2 -> {
                            loadUnityBanner(context as Activity, frameLayout)
                        }
                        else -> {
                            loadFacebookBanner(context, frameLayout)
                        }
                    }
                }
                java.AD_TYPE_ADMOB + java.AD_TYPE_UNITY + java.AD_TYPE_IRON-> {
                    val randomNumber = generateRandom(1,2)
                    Log.e(TAG, "randomNumber: $randomNumber")
                    when (randomNumber) {
                        1 -> {
                            loadAdmobBanner(context, frameLayout)
                        }
                        2 -> {
                            loadUnityBanner(context as Activity, frameLayout)
                        }
                        else -> {
                            loadIronBanner(context as Activity, frameLayout)
                        }
                    }
                }
                java.AD_TYPE_ADMOB + java.AD_TYPE_UNITY + java.AD_TYPE_APPLOVIN-> {
                    val randomNumber = generateRandom(1,2)
                    Log.e(TAG, "randomNumber: $randomNumber")
                    when (randomNumber) {
                        1 -> {
                            loadAdmobBanner(context, frameLayout)
                        }
                        2 -> {
                            loadUnityBanner(context as Activity, frameLayout)
                        }
                        else -> {
                            showApplovinBanner(context as Activity, frameLayout)
                        }
                    }
                }

            }
        }



        private fun showIronSourceInterstitial(activity: Activity) {
            if (IronSource.isInterstitialReady()) {
                IronSource.removeInterstitialListener()
                IronSource.setInterstitialListener(object : InterstitialListener {
                    /**
                     * Invoked when Interstitial Ad is ready to be shown after load function was called.
                     */
                    override fun onInterstitialAdReady() {}

                    /**
                     * invoked when there is no Interstitial Ad available after calling load function.
                     */
                    override fun onInterstitialAdLoadFailed(error: IronSourceError) {
                        Log.e(TAG, "Interstitial Failed1: ${error.errorMessage}")
                        moveToNextActivity(activity)
                    }

                    /**
                     * Invoked when the Interstitial Ad Unit is opened
                     */
                    override fun onInterstitialAdOpened() {}

                    /*
                     * Invoked when the ad is closed and the user is about to return to the application.
                     */
                    override fun onInterstitialAdClosed() {
                        IronSource.loadInterstitial()
                        moveToNextActivity(activity)
                    }

                    /**
                     * Invoked when Interstitial ad failed to show.
                     * @param error - An object which represents the reason of showInterstitial failure.
                     */
                    override fun onInterstitialAdShowFailed(error: IronSourceError) {
                        Log.e(TAG, "Interstitial Failed to Show2: ${error.errorMessage}")
                        moveToNextActivity(activity)
                    }

                    /*
                     * Invoked when the end user clicked on the interstitial ad, for supported networks only.
                     */
                    override fun onInterstitialAdClicked() {}

                    /** Invoked right before the Interstitial screen is about to open.
                     * NOTE - This event is available only for some of the networks.
                     * You should NOT treat this event as an interstitial impression, but rather use InterstitialAdOpenedEvent
                     */
                    override fun onInterstitialAdShowSucceeded() {}
                })
                IronSource.showInterstitial()
                Log.i("MEDIATION-SAMPLE", "Interstitial Showed")
            } else {
                IronSource.loadInterstitial()
                moveToNextActivity(activity)
                Log.i("MEDIATION-SAMPLE", "Interstitial not loaded")
            }
        }

        private fun moveToNextActivity(activity: Activity) {
            if (cG != null) {
                if (isClearStack) {
                    val newIntent = Intent(activity, cG)
                    newIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    newIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    activity.startActivity(newIntent)
                } else {
                    val newIntent = Intent(activity, cG)
                    activity.startActivity(newIntent)
                }
            }
            else if (intentG != null) {
                if (isClearStack) {
                    intentG?.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    intentG?.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    activity.startActivity(intentG)
                } else {
                    activity.startActivity(intentG)
                }
            }
            adCallBack?.callBack(1, "")
        }

        var interstitialAd: MaxInterstitialAd? = null
        private fun loadApplovinInters(activity: Activity?) {
            interstitialAd = MaxInterstitialAd(
                applovin_inters_id,
                activity
            )
            interstitialAd!!.setListener(object : MaxAdListener {
                override fun onAdLoaded(ad: MaxAd) {}
                override fun onAdDisplayed(ad: MaxAd) {}
                override fun onAdHidden(ad: MaxAd) {
                    interstitialAd!!.loadAd()
                    moveToNextActivity(activity!!)
                }

                override fun onAdClicked(ad: MaxAd) {}
                override fun onAdLoadFailed(adUnitId: String, error: MaxError) {
                    moveToNextActivity(activity!!)
                }
                override fun onAdDisplayFailed(ad: MaxAd, error: MaxError) {}
            })
            // Load the first ad
            interstitialAd!!.loadAd()
        }

        private fun showApplovinInterstitial() {
            if (interstitialAd != null && interstitialAd!!.isReady) interstitialAd!!.showAd()
        }

        fun showApplovinBanner(context: Activity, adLayout: FrameLayout) {
            val adView = MaxAdView(
                applovin_banner_ad_id,
                context
            )
            val width = ViewGroup.LayoutParams.MATCH_PARENT
            val heightPx = context.resources.getDimensionPixelSize(R.dimen.banner_height)
            adView.layoutParams = FrameLayout.LayoutParams(width, heightPx)
            adLayout.addView(adView)
            adView.loadAd()
        }


        // Implement the IUnityAdsListener interface methods:
        private class UnityAdsListener : IUnityAdsShowListener {
            override fun onUnityAdsShowFailure(
                placementId: String?,
                error: UnityAds.UnityAdsShowError?,
                message: String?
            ) {
                moveToNextActivity(activityG!!)
            }

            override fun onUnityAdsShowStart(placementId: String?) {

            }

            override fun onUnityAdsShowClick(placementId: String?) {

            }

            override fun onUnityAdsShowComplete(
                placementId: String?,
                state: UnityAds.UnityAdsShowCompletionState?
            ) {
                moveToNextActivity(activityG!!)
            }
        }

        fun loadApplovinNative(activity: Activity?, frameLayout: FrameLayout) {
            mFrameLayout = frameLayout
            nativeAdLoader =
                MaxNativeAdLoader(applovin_native_ad_id, activity)
            nativeAdLoader!!.setNativeAdListener(NativeAdListener())
            nativeAdLoader!!.loadAd(createNativeAdView(activity))
        }

        fun createNativeAdView(activity: Activity?): MaxNativeAdView? {
            val binder: MaxNativeAdViewBinder =
                MaxNativeAdViewBinder.Builder(R.layout.item_native_applovin)
                    .setTitleTextViewId(R.id.title_text_view)
                    .setBodyTextViewId(R.id.body_text_view)
                    .setAdvertiserTextViewId(R.id.advertiser_textView)
                    .setIconImageViewId(R.id.icon_image_view)
                    .setMediaContentViewGroupId(R.id.media_view_container)
                    .setOptionsContentViewGroupId(R.id.ad_options_view)
                    .setCallToActionButtonId(R.id.cta_button)
                    .build()
            return MaxNativeAdView(binder, activity)
        }


        class NativeAdListener : MaxNativeAdListener() {
            override fun onNativeAdLoaded(nativeAdView: MaxNativeAdView?, nativeAd: MaxAd) {
                // Clean up any pre-existing native ad to prevent memory leaks.
                if (loadedNativeAd != null) {
                    nativeAdLoader?.destroy(loadedNativeAd)
                }
                mFrameLayout?.setVisibility(View.VISIBLE)
                // Save ad for cleanup.
                loadedNativeAd = nativeAd
                mFrameLayout?.removeAllViews()
                mFrameLayout?.addView(nativeAdView)
                Log.e("ADSSS", "applovin native loaded")
            }

            override fun onNativeAdLoadFailed(adUnitId: String, error: MaxError) {
                // Native ad load failed.
                // AppLovin recommends retrying with exponentially higher delays up to a maximum delay.
                Log.e("ADSSS", "applovin native error: " + error.message)
            }

            override fun onNativeAdClicked(nativeAd: MaxAd) {}
        }

        fun showRewardedAd(activity: Activity, adCallBack: AdCallBack) {
            if (selectedAd == java.AD_TYPE_ADMOB) {
                showAdmobRewarded(activity, adCallBack)
            }
            else if (selectedAd == java.AD_TYPE_APPLOVIN) {
                showApplovinRewardedAd(activity, adCallBack)
            }
        }


        var rewardedAd: MaxRewardedAd? = null
        private const val retryAttempt = 0

        /*   fun loadApplovinRewarded(activity: Activity?) {
               rewardedAd = MaxRewardedAd.getInstance(
                   applovin_rewarded_ad_id, activity
               )
               rewardedAd?.loadAd()
           }
   */
        private fun showApplovinRewardedAd(activity: Activity, adCallBack: AdCallBack) {
            if (rewardedAd!!.isReady) {
                rewardedAd!!.showAd()
                var count = 1
                rewardedAd?.setListener(object : MaxRewardedAdListener {
                    override fun onRewardedVideoStarted(ad: MaxAd) {}
                    override fun onRewardedVideoCompleted(ad: MaxAd) {}
                    override fun onUserRewarded(ad: MaxAd, reward: MaxReward) {
                        count = 2
                    }
                    override fun onAdLoaded(ad: MaxAd) {}
                    override fun onAdDisplayed(ad: MaxAd) {}
                    override fun onAdHidden(ad: MaxAd) {
                        //loadApplovinRewarded(activity)
                        if (count == 2) {
                            adCallBack.callBack(1, "")
                        }
                        else {
                            adCallBack.callBack(0, "")
                        }
                    }

                    override fun onAdClicked(ad: MaxAd) {}
                    override fun onAdLoadFailed(adUnitId: String, error: MaxError) {}
                    override fun onAdDisplayFailed(ad: MaxAd, error: MaxError) {}
                })
            }
            else {
                adCallBack.callBack(1, "")
            }
        }


    }

    var nativeAd: com.facebook.ads.NativeAd? = null
    private fun loadFbBigNativeAd() {
        // Instantiate a NativeAd object.
        // NOTE: the placement ID will eventually identify this as your App, you can ignore it for
        // now, while you are testing and replace it later when you have signed up.
        // While you are using this temporary code you will only get test ads and if you release
        // your code like this to the Google Play your users will not receive ads (you will get a no fill error).
        nativeAd =
            NativeAd(activityG, "native_ad_id")
        val nativeAdListener: com.facebook.ads.NativeAdListener =
            object : com.facebook.ads.NativeAdListener {
                override fun onMediaDownloaded(ad: Ad) {
                    // Native ad finished downloading all assets
                    Log.e(
                        TAG,
                        "Native ad finished downloading all assets."
                    )
                }

                override fun onError(ad: Ad, adError: com.facebook.ads.AdError) {
                    // Native ad failed to load
                    Log.e(
                        TAG,
                        "Native ad failed to load: " + adError.errorMessage
                    )
                }

                override fun onAdLoaded(ad: Ad) {
                    // Native ad is loaded and ready to be displayed
                    Log.d(
                        TAG,
                        "Native ad is loaded and ready to be displayed!"
                    )
                }

                override fun onAdClicked(ad: Ad) {
                    // Native ad clicked
                    Log.d(TAG, "Native ad clicked!")
                }

                override fun onLoggingImpression(ad: Ad) {
                    // Native ad impression
                    Log.d(TAG, "Native ad impression logged!")
                }
            }

        // Request an ad
        nativeAd?.loadAd(
            nativeAd?.buildLoadAdConfig()
                ?.withAdListener(nativeAdListener)
                ?.build()
        )
    }

}