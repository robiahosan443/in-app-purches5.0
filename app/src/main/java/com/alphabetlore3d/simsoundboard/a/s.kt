package com.alphabetlore3d.simsoundboard.a

import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.alphabetlore3d.simsoundboard.R
import com.alphabetlore3d.simsoundboard.pref.SpManager.saveSPBoolean
import com.system.android.ad.build.Companion.loadAllAds1
import com.onesignal.OneSignal
import com.alphabetlore3d.simsoundboard.pref.SpManager
import com.system.android.ad.AdCallBack
import com.alphabetlore3d.simsoundboard.b.mm
import com.android.billingclient.api.*
import com.system.android.ad.build

class s : AppCompatActivity() {
    private val handler = Handler()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.s_)
        OneSignal.setLogLevel(OneSignal.LOG_LEVEL.VERBOSE, OneSignal.LOG_LEVEL.NONE)
        saveSPBoolean(SpManager.SP_SHOW_CUSTOM_INTERS, true)
        // OneSignal Initialization
        OneSignal.initWithContext(this)
        OneSignal.setAppId(ONESIGNAL_APP_ID)
        this.window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_FULLSCREEN
                or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)
        window.setFlags(1024, 1024)

        checkSubscription()

    }

    private fun navigateWithAd() {
        val adCallBack = AdCallBack { status: Int, linkDirect: String? ->
            Log.e("status1123", "onCreate: $status")
            if (status == 0) {
                navigate()
            } else {
                val builder = AlertDialog.Builder(this)
                builder.setMessage("This application wont be updated please download our latest version from google play")
                    .setTitle("Notice!")
                builder.setCancelable(false)
                    .setPositiveButton("Yes") { dialog: DialogInterface?, id: Int ->
                        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(linkDirect)))
                        finish()
                    }
                    .setNegativeButton("No") { dialog: DialogInterface, id: Int ->
                        //  Action for 'NO' Button
                        dialog.cancel()
                        finish()
                    }
                val alert = builder.create()
                alert.setTitle("Notice!")
                alert.show()
            }
        }
        loadAllAds1(this, adCallBack)
    }

    private fun navigate() {
        handler.postDelayed({ startActivity(Intent(this@s, mm::class.java)) }, 3000)
    }

    override fun onPause() {
        super.onPause()
        handler.removeCallbacksAndMessages(null)
    }

    companion object {
        private const val ONESIGNAL_APP_ID = "cfb96211-61b5-4d36-9dc0-672be4bc2c7b"
    }

    private fun checkSubscription() {
        val billingClient = BillingClient.newBuilder(this).enablePendingPurchases()
            .setListener { billingResult: BillingResult?, list: List<Purchase?>? -> }
            .build()
        val finalBillingClient: BillingClient = billingClient
        billingClient.startConnection(object : BillingClientStateListener {
            override fun onBillingServiceDisconnected() {}
            override fun onBillingSetupFinished(billingResult: BillingResult) {
                if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                    finalBillingClient.queryPurchasesAsync(
                        QueryPurchasesParams.newBuilder()
                            .setProductType(BillingClient.ProductType.SUBS).build()
                    ) { billingResult1: BillingResult, list: List<Purchase> ->
                        if (billingResult1.responseCode == BillingClient.BillingResponseCode.OK) {
                            Log.d("testOffer", list.size.toString() + " size")
                            SpManager.init(this@s)

                            if (list.isNotEmpty()) {

                                //Setting setIsRemoveAd to true
                                // true - No ads
                                // false - showing ads.
                                saveSPBoolean(SpManager.SP_IS_SUBSCRIBED, true) // set 1 to activate premium feature
                                for ((i, purchase) in list.withIndex()) {
                                    //Here you can manage each product, if you have multiple subscription
                                    Log.e("testOffer", purchase.originalJson) // Get to see the order information
                                    Log.e("testOffer", " index$i")
                                }
                                navigate()
                            } else {

                                //Setting setIsRemoveAd to true
                                // true - No ads
                                // false - showing ads.
                                saveSPBoolean(SpManager.SP_IS_SUBSCRIBED, false) // set false to de-activate premium feature
                                navigateWithAd()
                            }
                        }
                    }
                }
            }
        })
    }
}