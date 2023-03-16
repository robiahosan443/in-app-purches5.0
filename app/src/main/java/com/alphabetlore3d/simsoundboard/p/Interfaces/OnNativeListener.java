package com.alphabetlore3d.simsoundboard.p.Interfaces;

public interface OnNativeListener {
    void onNativeAdLoaded();
    void onNativeAdClicked();
    void onNativeAdFailedToLoad(String error);
}
