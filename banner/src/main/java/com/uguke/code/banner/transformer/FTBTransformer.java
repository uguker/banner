package com.uguke.code.banner.transformer;

import android.view.View;

public class FTBTransformer extends BaseTransformer {

    @Override
    protected void onTransform(View view, float position) {
        final float height = view.getHeight();
        final float width = view.getWidth();
        final float scale = min(position > 0 ? 1f : Math.abs(1f + position), 0.5f);

        view.setScaleX(scale);
        view.setScaleY(scale);
        view.setPivotX(width * 0.5f);
        view.setPivotY(height * 0.5f);

        if (position > 0) {
            view.setTranslationX(width * position);
        } else {
            view.setTranslationX(-width * position * 0.4f);
        }
    }

}
