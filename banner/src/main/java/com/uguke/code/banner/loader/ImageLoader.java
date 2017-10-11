package com.uguke.code.banner.loader;

import android.content.Context;
import android.view.View;

import java.io.Serializable;

/**
 * 图片加载器
 * @param <T>
 */
public interface ImageLoader <T extends View> extends Serializable {
    void loadImage(Context context, Object path, T imageView);
    T onCreateView(Context context);
}
