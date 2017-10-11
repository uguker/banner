package com.uguke.code.banner.transformer;

import android.view.View;

public class ZoomOutTransformer extends BaseTransformer {

    @Override
    protected void onTransform(View view, float pos) {

        final float scale = 1f + Math.abs(pos);

        view.setScaleX(scale);
        view.setScaleY(scale);
        view.setPivotX(view.getWidth() * 0.5f);
        view.setPivotY(view.getHeight() * 0.5f);

        if (pos < -1 || pos > 1) {
            view.setAlpha(1f - scale);
        } else {
            view.setAlpha(2f - scale);
            if (pos == -1)
                view.setTranslationX(-view.getWidth());
        }
    }
}
