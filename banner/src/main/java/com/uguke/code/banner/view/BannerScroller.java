package com.uguke.code.banner.view;

import android.support.v4.view.ViewPager;
import android.view.animation.Interpolator;
import android.widget.Scroller;

import java.lang.reflect.Field;

/**
 * 功能描述：轮播图滚动控制
 * @author LeiJue
 * @date 2018/9/25
 */
public class BannerScroller {

    private int scrollTime = 400;

    public BannerScroller() {

    }

    public void bindViewPager(ViewPager pager) {
        try {
            Field mScroller = ViewPager.class.getDeclaredField("mScroller");
            mScroller.setAccessible(true);

            Field interpolator = ViewPager.class.getDeclaredField("sInterpolator");
            interpolator.setAccessible(true);

            Scroller scroller = new Scroller(pager.getContext(),
                    (Interpolator) interpolator.get(null)) {
                @Override
                public void startScroll(int startX, int startY, int dx, int dy, int duration) {
                    super.startScroll(startX, startY, dx, dy, scrollTime);
                }
            };
            mScroller.set(pager, scroller);
        } catch (Exception e) {

        }
    }

    public void setScrollTime(int scrollTime) {
        this.scrollTime = scrollTime;
    }

}
