package com.alphabetlore3d.simsoundboard.p.Interfaces;

public interface OnInterstitialAdListener {
    void onInterstitialAdLoaded();
    void onInterstitialAdClosed();
    void onInterstitialAdClicked();
    void onInterstitialAdFailedToLoad(String error);
}
