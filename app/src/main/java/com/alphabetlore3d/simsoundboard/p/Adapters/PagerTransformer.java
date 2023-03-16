package com.alphabetlore3d.simsoundboard.p.Adapters;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.ViewPager;


public class PagerTransformer implements ViewPager.PageTransformer {

    private final float scaleAmountPercent = 10f;

    @Override
    public void transformPage(@NonNull View page, float position) {
        try {
            float percentage = 1 - Math.abs(position);
            float amount = ((100 - scaleAmountPercent) + (scaleAmountPercent * percentage)) / 100;
            setSize(page, position, amount);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void setSize(View page, float position, float percentage) {
        page.setScaleX((position != 0 && position != 1) ? percentage : 1);
        page.setScaleY((position != 0 && position != 1) ? percentage : 1);
    }


}