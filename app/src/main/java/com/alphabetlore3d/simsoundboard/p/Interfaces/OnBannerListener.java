package com.alphabetlore3d.simsoundboard.p.Interfaces;

public interface OnBannerListener {
    void onBannerAdLoaded();
    void onBannerAdClicked();
    void onBannerAdFailedToLoad(String error);
}
