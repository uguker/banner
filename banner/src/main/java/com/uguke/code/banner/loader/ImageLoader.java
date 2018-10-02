package com.uguke.code.banner.loader;

import android.content.Context;
import android.view.View;

import java.io.Serializable;

/**
 * 功能描述：图片加载器
 * @author LeiJue
 * @date 2018/9/25
 */
public interface ImageLoader <T extends View> extends Serializable {
    void loadImage(Context context, Object path, T imageView);
    T onCreateView(Context context);
}
