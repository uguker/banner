package com.uguke.code.banner;

import android.graphics.Color;

import java.util.Observable;

public class BannerConfig extends Observable {

    /** 轮播图样式 **/
    private @Banner.BannerType int bannerType;
    /** 缩放样式 **/
    private @Banner.ScaleType int scaleType;
    //标题属性
    private float titleHeight;
    private float titlePaddingLeft;
    private float titlePaddingRight;
    private float titleHintSize;
    private float titleTextSize;
    private int titleHintColor;
    private int titleTextColor;
    private int titleBackgroundColor;
    //指示器属性
    private float indicatorWidth;
    private float indicatorHeight;
    private float indicatorPaddingLeft;
    private float indicatorPaddingRight;
    private float indicatorPaddingTop;
    private float indicatorPaddingBottom;
    private float indicatorInMargin;
    private float indicatorMarginLeft;
    private float indicatorMarginRight;
    private float indicatorMarginBottom;
    private int indicatorBackgroundColor;
    private int indicatorSelectedColor;
    private int indicatorUnselectedColor;
    //指示器资源ID
    private int indicatorSelectedResId;
    private int indicatorUnselectedResId;
    //指示器形状
    private @Banner.Indicator int indicatorSelectedShape;
    private @Banner.Indicator int indicatorUnselectedShape;
    //数字指示器属性
    private float numIndicatorSize;
    private float numIndicatorMargin;
    private float numIndicatorTextSize;
    private int numIndicatorTextColor;
    private int numIndicatorBackgroundColor;

    public BannerConfig() {

        bannerType = Banner.TYPE_TITLE_INDICATOR_CENTER;
        scaleType = Banner.SCALE_FIT_CENTER;

        //标题属性
        titleHeight = 30;
        titleHintSize = 12;
        titleTextSize = 14;
        titlePaddingLeft = 10;
        titlePaddingRight = 10;
        titleHintColor = Color.argb(255, 255, 255, 255);
        titleTextColor = Color.argb(255, 255, 255, 255);
        titleBackgroundColor = Color.argb(70, 0, 0, 0);

        //指示器属性
        indicatorWidth = 5;
        indicatorHeight = 5;
        indicatorInMargin = 0;
        indicatorMarginBottom = 10;
        indicatorMarginLeft = 16;
        indicatorMarginRight = 16;
        indicatorPaddingTop = 3;
        indicatorPaddingLeft = 3;
        indicatorPaddingRight = 3;
        indicatorPaddingBottom = 3;
        indicatorBackgroundColor = Color.argb(120, 0, 0, 0);
        indicatorSelectedColor = Color.argb(255, 255, 255, 255);
        indicatorUnselectedColor = Color.argb(180, 255, 255, 255);
        //背景属性
        indicatorSelectedResId = -1;
        indicatorUnselectedResId = -1;
        indicatorSelectedShape = Banner.SHAPE_CIRCLE;;
        indicatorUnselectedShape = Banner.SHAPE_RING;;

        //圆形指示器属性
        numIndicatorSize = 40;
        numIndicatorMargin = 6;
        numIndicatorTextSize = 14;
        numIndicatorTextColor = Color.argb(255, 255, 255, 255);
        numIndicatorBackgroundColor = Color.argb(120, 0, 0, 0);
    }

    public @Banner.BannerType int getBannerType() {
        return bannerType;
    }

    public @Banner.ScaleType int getScaleType() {
        return scaleType;
    }

    public float getTitleHeight() {
        return titleHeight;
    }

    public float getTitlePaddingLeft() {
        return titlePaddingLeft;
    }

    public float getTitlePaddingRight() {
        return titlePaddingRight;
    }

    public float getTitleHintSize() {
        return titleHintSize;
    }

    public float getTitleTextSize() {
        return titleTextSize;
    }

    public int getTitleHintColor() {
        return titleHintColor;
    }

    public int getTitleTextColor() {
        return titleTextColor;
    }

    public int getTitleBackgroundColor() {
        return titleBackgroundColor;
    }

    public float getIndicatorWidth() {
        return indicatorWidth;
    }

    public float getIndicatorHeight() {
        return indicatorHeight;
    }

    public float getIndicatorPaddingLeft() {
        return indicatorPaddingLeft;
    }

    public float getIndicatorPaddingRight() {
        return indicatorPaddingRight;
    }

    public float getIndicatorPaddingTop() {
        return indicatorPaddingTop;
    }

    public float getIndicatorPaddingBottom() {
        return indicatorPaddingBottom;
    }

    public float getIndicatorInMargin() {
        return indicatorInMargin;
    }

    public float getIndicatorMarginLeft() {
        return indicatorMarginLeft;
    }

    public float getIndicatorMarginRight() {
        return indicatorMarginRight;
    }

    public float getIndicatorMarginBottom() {
        return indicatorMarginBottom;
    }

    public int getIndicatorBackgroundColor() {
        return indicatorBackgroundColor;
    }

    public int getIndicatorSelectedColor() {
        return indicatorSelectedColor;
    }

    public int getIndicatorUnselectedColor() {
        return indicatorUnselectedColor;
    }

    public int getIndicatorSelectedResId() {
        return indicatorSelectedResId;
    }

    public int getIndicatorUnselectedResId() {
        return indicatorUnselectedResId;
    }

    public @Banner.Indicator int getIndicatorSelectedShape() {
        return indicatorSelectedShape;
    }

    public @Banner.Indicator int getIndicatorUnselectedShape() {
        return indicatorUnselectedShape;
    }

    public float getNumIndicatorSize() {
        return numIndicatorSize;
    }

    public float getNumIndicatorMargin() {
        return numIndicatorMargin;
    }

    public float getNumIndicatorTextSize() {
        return numIndicatorTextSize;
    }

    public int getNumIndicatorTextColor() {
        return numIndicatorTextColor;
    }

    public int getNumIndicatorBackgroundColor() {
        return numIndicatorBackgroundColor;
    }

    public BannerConfig setBannerType(@Banner.BannerType int bannerType) {
        this.bannerType = bannerType;
        return this;
    }

    public BannerConfig setScaleType(@Banner.ScaleType int scaleType) {
        this.scaleType = scaleType;
        return this;
    }

    public BannerConfig setTitleHeight(float titleHeight) {
        this.titleHeight = titleHeight;
        return this;
    }

    public BannerConfig setTitlePadding(float titlePadding) {
        this.titlePaddingLeft = titlePadding;
        this.titlePaddingRight = titlePadding;
        return this;
    }

    public BannerConfig setTitlePaddingLeft(float titlePaddingLeft) {
        this.titlePaddingLeft = titlePaddingLeft;
        return this;
    }

    public BannerConfig setTitlePaddingRight(float titlePaddingRight) {
        this.titlePaddingRight = titlePaddingRight;
        return this;
    }

    public BannerConfig setTitleHintSize(float titleHintSize) {
        this.titleHintSize = titleHintSize;
        return this;
    }

    public BannerConfig setTitleTextSize(float titleTextSize) {
        this.titleTextSize = titleTextSize;
        return this;
    }

    public BannerConfig setTitleHintColor(int titleHintColor) {
        this.titleHintColor = titleHintColor;
        return this;
    }

    public BannerConfig setTitleTextColor(int titleTextColor) {
        this.titleTextColor = titleTextColor;
        return this;
    }

    public BannerConfig setTitleBackgroundColor(int titleBackgroundColor) {
        this.titleBackgroundColor = titleBackgroundColor;
        return this;
    }

    public BannerConfig setIndicatorWidth(float indicatorWidth) {
        this.indicatorWidth = indicatorWidth;
        return this;
    }

    public BannerConfig setIndicatorHeight(float indicatorHeight) {
        this.indicatorHeight = indicatorHeight;
        return this;
    }

    public BannerConfig setIndicatorInMargin(float indicatorMargin) {
        this.indicatorInMargin = indicatorMargin;
        return this;
    }

    public BannerConfig setIndicatorMargin(float indicatorMargin) {
        this.indicatorMarginLeft = indicatorMargin;
        this.indicatorMarginRight = indicatorMargin;
        this.indicatorMarginBottom = indicatorMargin;
        return this;
    }

    public BannerConfig setIndicatorPadding(float indicatorPadding) {
        this.indicatorPaddingLeft = indicatorPadding;
        this.indicatorPaddingTop = indicatorPadding;
        this.indicatorPaddingRight = indicatorPadding;
        this.indicatorPaddingBottom = indicatorPadding;
        return this;
    }

    public BannerConfig setIndicatorPaddingLeft(float indicatorPaddingLeft) {
        this.indicatorPaddingLeft = indicatorPaddingLeft;
        return this;
    }

    public BannerConfig setIndicatorPaddingRight(float indicatorPaddingRight) {
        this.indicatorPaddingRight = indicatorPaddingRight;
        return this;
    }

    public BannerConfig setIndicatorPaddingTop(float indicatorPaddingTop) {
        this.indicatorPaddingTop = indicatorPaddingTop;
        return this;
    }

    public BannerConfig setIndicatorPaddingBottom(float indicatorPaddingBottom) {
        this.indicatorPaddingBottom = indicatorPaddingBottom;
        return this;
    }

    public BannerConfig setIndicatorMarginLeft(float indicatorMarginLeft) {
        this.indicatorMarginLeft = indicatorMarginLeft;
        return this;
    }

    public BannerConfig setIndicatorMarginRight(float indicatorMarginRight) {
        this.indicatorMarginRight = indicatorMarginRight;
        return this;
    }

    public BannerConfig setIndicatorMarginBottom(float indicatorMarginBottom) {
        this.indicatorMarginBottom = indicatorMarginBottom;
        return this;
    }

    public BannerConfig setIndicatorBackgroundColor(int indicatorBackgroundColor) {
        this.indicatorBackgroundColor = indicatorBackgroundColor;
        return this;
    }

    public BannerConfig setIndicatorSelectedColor(int indicatorSelectedColor) {
        this.indicatorSelectedColor = indicatorSelectedColor;
        return this;
    }

    public BannerConfig setIndicatorUnselectedColor(int indicatorUnselectedColor) {
        this.indicatorUnselectedColor = indicatorUnselectedColor;
        return this;
    }

    public BannerConfig setIndicatorSelectedResId(int indicatorSelectedResId) {
        this.indicatorSelectedResId = indicatorSelectedResId;
        return this;
    }

    public BannerConfig setIndicatorUnselectedResId(int indicatorUnselectedResId) {
        this.indicatorUnselectedResId = indicatorUnselectedResId;
        return this;
    }

    public BannerConfig setIndicatorSelectedShape(@Banner.Indicator int indicatorSelectedShape) {
        this.indicatorSelectedShape = indicatorSelectedShape;
        return this;
    }

    public BannerConfig setIndicatorUnselectedShape(@Banner.Indicator int indicatorUnselectedShape) {
        this.indicatorUnselectedShape = indicatorUnselectedShape;
        return this;
    }

    public BannerConfig setNumIndicatorSize(float numIndicatorSize) {
        this.numIndicatorSize = numIndicatorSize;
        return this;
    }

    public BannerConfig setNumIndicatorMargin(float numIndicatorMargin) {
        this.numIndicatorMargin = numIndicatorMargin;
        return this;
    }

    public BannerConfig setNumIndicatorTextSize(float numIndicatorTextSize) {
        this.numIndicatorTextSize = numIndicatorTextSize;
        return this;
    }

    public BannerConfig setNumIndicatorTextColor(int numIndicatorTextColor) {
        this.numIndicatorTextColor = numIndicatorTextColor;
        return this;
    }

    public BannerConfig setNumIndicatorBackgroundColor(int numIndicatorBackgroundColor) {
        this.numIndicatorBackgroundColor = numIndicatorBackgroundColor;
        return this;
    }

    public void notifyViewChange() {
        setChanged();
        notifyObservers();
    }
}
