package com.jetsynthesys.rightlife.ui.affirmation;

import android.view.View;

import androidx.viewpager.widget.ViewPager;

public class AffirmationPageTransformer implements ViewPager.PageTransformer {

    private int mScaleOffset = 40;

    public void setScaleOffset(int mScaleOffset) {
        this.mScaleOffset = mScaleOffset;
    }

    public void transformPage(View page, float position) {
        if (position <= 0.0f) {
            page.setTranslationX(0f);
            page.setRotation((45 * position));
            page.setTranslationX(((float) page.getWidth() / 3 * position));
        } else {
            float scale = (page.getWidth() - mScaleOffset * position) / (float) (page.getWidth());

            page.setScaleX(scale);
            page.setScaleY(scale);

            page.setTranslationX((-page.getWidth() * position));
            page.setTranslationY((mScaleOffset * 0.8f) * position);
        }
    }
}