package com.uguke.code.banner.transformer;

import android.view.View;

public class ZoomInTransformer extends BaseTransformer {

    @Override
    protected void onTransform(View view, float position) {

        float scale = Math.abs(position) - 1;

        view.setScaleX(Math.abs(scale));
        view.setScaleY(Math.abs(scale));
        view.setPivotX(view.getWidth() * 0.5f);
        view.setPivotY(view.getHeight() * 0.5f);

        if (position < -1 || position > 1) {
            view.setAlpha(1f - scale);
        } else {
            view.setAlpha(2f - scale);
        }
    }
}