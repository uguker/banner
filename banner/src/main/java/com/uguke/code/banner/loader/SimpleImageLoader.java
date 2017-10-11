package com.uguke.code.banner.loader;

import android.content.Context;
import android.widget.ImageView;

public abstract class SimpleImageLoader implements ImageLoader<ImageView> {

    @Override
    public ImageView onCreateView(Context context) {
        return new ImageView(context);
    }
}
