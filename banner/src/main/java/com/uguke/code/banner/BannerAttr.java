package com.uguke.code.banner;

import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;

import com.uguke.code.banner.util.ShapeUtil;


class BannerAttr {

    //标题属性

    int titleHeight;
    int titlePaddingLeft;
    int titlePaddingRight;
    int titleTextColor;
    float titleTextSize;
    Drawable titleBackground;


    //指示器属性

    int indicatorWidth;
    int indicatorHeight;
    int indicatorPadding;
    int indicatorMargin;
    int indicatorSelectColor;
    int indicatorUnselectColor;
    Drawable indicatorSelected;
    Drawable indicatorUnselected;

    //数字指示器属性

    int numberWidth;
    int numberHeight;
    int numberMargin;
    int numberTextColor;
    float numberTextSize;
    Drawable numberBackground;

    BannerAttr() {

        float density = Resources.getSystem().getDisplayMetrics().density;
        //标题属性
        titleHeight = (int) (30 * density);
        titleTextSize = 14;
        titlePaddingLeft = (int) (10 * density);
        titlePaddingRight = (int) (10 * density);
        titleTextColor = Color.argb(255, 255, 255, 255);
        titleBackground = new ColorDrawable(Color.argb(70, 0, 0, 0));

        //指示器属性
        indicatorWidth = (int) (10 * density);
        indicatorHeight = (int) (10 * density);
        indicatorMargin = (int) (10 * density);
        indicatorPadding = 0;
        indicatorSelectColor =  Color.WHITE;
        indicatorUnselectColor = Color.argb(180, 255, 255, 255);
        indicatorSelected = ShapeUtil.circleShape(indicatorWidth, indicatorHeight, indicatorSelectColor, true);
        indicatorUnselected = ShapeUtil.circleShape(indicatorWidth, indicatorHeight, Color.argb(180, 255, 255, 255), true);

        //圆形指示器属性
        numberWidth = (int) (40 * density);
        numberHeight = (int) (40 * density);
        numberMargin = (int) (16 * density);
        numberTextSize = 14;
        numberTextColor = Color.argb(255, 255, 255, 255);
        numberBackground = new ColorDrawable(Color.argb(120, 0, 0, 0));
    }
}
