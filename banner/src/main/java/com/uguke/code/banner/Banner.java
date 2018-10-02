package com.uguke.code.banner;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.view.animation.Interpolator;
import android.widget.RelativeLayout;
import android.widget.Scroller;

import com.uguke.code.banner.transformer.BaseTransformer;


import com.uguke.code.banner.view.BannerScroller;
import com.uguke.code.banner.view.BannerViewPager;

import java.lang.reflect.Field;
import java.util.Observable;
import java.util.Observer;

/**
 * 功能描述：轮播图控件
 * @author LeiJue
 * @date 2018/9/25
 */
public class Banner extends RelativeLayout implements ViewPager.OnPageChangeListener {

    /**  **/
    public static final int DURATION_TIME = 3000;
    /**  **/
    public static final int SCROLLING_TIME = 600;

    private static final int HANDLER_WHAT = 100;
    //private int scrollTime = 400;

    private BannerScroller scroller;

    private BannerViewPager pagerContainer;
    BannerController controller;

    /** 是否随机动画 **/
    private boolean random;
    /** 是否自动滚动 **/
    private boolean automatic;
    /** 是否可滚动 **/
    private boolean scrollable;

    /** 延时时间 **/
    private int delayTime;
    /** 滚动时间 **/
    private int scrollTime;

    /** 数据适配器 **/
    private BannerAdapter adapter;
    /** 轮播图属性配置 **/
    BannerAttr attr;
    /** 轮播图切换动画管理 **/
    private TransformerManager manager;


    private int itemCount;

    private Observer adapterObserver = new Observer() {
        @Override
        public void update(Observable o, Object arg) {
            itemCount = (int) arg;
            pagerContainer.setCurrentItem(1);
        }
    };

    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case HANDLER_WHAT:
                    int index = pagerContainer.getCurrentItem() + 1;
                    if (index == itemCount - 1) {
                        pagerContainer.setCurrentItem(0, false);
                        pagerContainer.setCurrentItem(1, true);
                        handler.sendEmptyMessageDelayed(HANDLER_WHAT, delayTime + scrollTime);
                    } else {
                        if (index != itemCount - 2) {
                            pagerContainer.setCurrentItem(index, true);
                            handler.sendEmptyMessageDelayed(HANDLER_WHAT, delayTime + scrollTime);
                        }
                    }
                    break;
                default:
                    break;
            }
            return false;
        }
    });

    public Banner(Context context) {
        super(context);
        initData(context, null);
        initView();
    }

    public Banner(Context context, AttributeSet attrs) {
        super(context, attrs);
        initData(context, attrs);
        initView();
    }

    public Banner(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initData(context, attrs);
        initView();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public Banner(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initData(context, attrs);
        initView();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (automatic && itemCount > 0) {
            switch (ev.getAction()) {
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_CANCEL:
                case MotionEvent.ACTION_OUTSIDE:
                    handler.sendEmptyMessageDelayed(HANDLER_WHAT, delayTime);
                    break;
                case MotionEvent.ACTION_DOWN:
                    stop();
                    break;
                default:
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    public void stop() {
        handler.removeMessages(HANDLER_WHAT);
    }

    public void start() {
        handler.sendEmptyMessageDelayed(HANDLER_WHAT, delayTime);
    }

    private void initData(Context context, AttributeSet attrs) {
        manager = new TransformerManager();
        delayTime = 3000;
        scrollTime = 600;
        random = false;
        automatic = true;
        scrollable = true;

        attr = new BannerAttr();

        float density = Resources.getSystem().getDisplayMetrics().density;

        if (attrs != null) {
            TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.Banner);

            automatic = ta.getBoolean(R.styleable.Banner_automatic, automatic);
            scrollable = ta.getBoolean(R.styleable.Banner_scrollable, scrollable);

            delayTime = ta.getInt(R.styleable.Banner_delayTime, delayTime);
            scrollTime = ta.getInt(R.styleable.Banner_scrollTime, scrollTime);

            // 标题部分
            Drawable titleBackground = ta.getDrawable(R.styleable.Banner_titleBackground);
            int titlePadding = ta.getDimensionPixelSize(R.styleable.Banner_titlePadding, (int) (16 * density));
            attr.titleHeight = ta.getDimensionPixelSize(R.styleable.Banner_titleHeight, attr.titleHeight);
            attr.titleTextSize = ta.getDimension(R.styleable.Banner_titleTextSize, attr.titleTextSize * density) / density;
            attr.titleTextColor = ta.getColor(R.styleable.Banner_titleTextColor, attr.titleTextColor);
            attr.titlePaddingLeft = titlePadding;
            attr.titlePaddingRight = titlePadding;
            attr.titlePaddingLeft = ta.getDimensionPixelSize(R.styleable.Banner_titlePaddingLeft, titlePadding);
            attr.titlePaddingRight = ta.getDimensionPixelSize(R.styleable.Banner_titlePaddingRight, titlePadding);
            attr.titleBackground = titleBackground == null ? attr.titleBackground : titleBackground;

            // 指示器
            attr.indicatorWidth = ta.getDimensionPixelSize(R.styleable.Banner_indicatorWidth, attr.indicatorWidth);
            attr.indicatorHeight = ta.getDimensionPixelSize(R.styleable.Banner_indicatorHeight, attr.indicatorHeight);
            attr.indicatorMargin = ta.getDimensionPixelSize(R.styleable.Banner_indicatorMargin, attr.indicatorMargin);
            attr.indicatorPadding = ta.getDimensionPixelSize(R.styleable.Banner_indicatorPadding, 0);
            attr.indicatorSelectColor = ta.getColor(R.styleable.Banner_indicatorSelectedColor, attr.indicatorSelectColor);
            attr.indicatorUnselectColor = ta.getColor(R.styleable.Banner_indicatorUnselectedColor, attr.indicatorUnselectColor);
            //数字指示器
            attr.numberWidth = ta.getDimensionPixelSize(R.styleable.Banner_numberWidth, attr.numberWidth);
            attr.numberHeight = ta.getDimensionPixelSize(R.styleable.Banner_numberHeight, attr.numberWidth);
            attr.numberMargin = ta.getDimensionPixelSize(R.styleable.Banner_numberMargin, attr.numberMargin);
            attr.numberTextSize = ta.getDimension(R.styleable.Banner_numberTextSize, attr.numberTextSize * density) / density;
            attr.numberTextColor = ta.getColor(R.styleable.Banner_numberTextColor, attr.numberTextColor);
            attr.numberBackground = ta.getDrawable(R.styleable.Banner_numberBackground);

            ta.recycle();
        }
    }



    /**
     * 功能描述：初始化控件
     */
    private void initView() {
        controller = new BannerController(this);
        //初始化
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        pagerContainer = new BannerViewPager(getContext());
        pagerContainer.setLayoutParams(params);
        pagerContainer.addOnPageChangeListener(this);
        pagerContainer.setFocusable(true);
        pagerContainer.setScrollable(scrollable);
        pagerContainer.setClipToPadding(false);
        pagerContainer.setOffscreenPageLimit(2);
        addView(pagerContainer);
        setScrollTime(scrollTime);

    }

    public Banner setScrollTime(int time) {
        this.scrollTime = time;
        try {
            Field field1 = ViewPager.class.getDeclaredField("mScroller");
            field1.setAccessible(true);

            Field field2 = ViewPager.class.getDeclaredField("sInterpolator");
            field2.setAccessible(true);

            Scroller scroller = new Scroller(pagerContainer.getContext(),
                    (Interpolator) field2.get(null)) {
                @Override
                public void startScroll(int startX, int startY, int dx, int dy, int duration) {
                    super.startScroll(startX, startY, dx, dy, scrollTime);
                }
            };
            field1.set(pagerContainer, scroller);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return this;
    }

    public Banner setTransformer(BaseTransformer transformer) {
        if (transformer != null) {
            manager.setTransformer(transformer);
        }
        return this;
    }

    public Banner setAdapter(BannerAdapter adapter) {
        this.adapter = adapter;
        this.adapter.observable.addObserver(adapterObserver);
        this.adapter.observable.addObserver(controller.observer);
        this.pagerContainer.setAdapter(adapter);
        return this;
    }
    
    /**
     * 功能描述：设置当前位置
     * @param position 当前位置
     * @param smoothScroll 是否显示动画
     */
    public Banner setCurrentItem(int position, boolean smoothScroll) {
//        curPosition = position + (loop ? 1 : 0);
        if (pagerContainer.getAdapter() != null) {
            pagerContainer.setCurrentItem(position + 1, smoothScroll);
        }
        return this;
    }


    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

    @Override
    public void onPageSelected(int position) {
        controller.update(adapter.getItem(position));
        controller.u(position - 1);
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        //获取真实数量和当前Item位置
        int currentItem = pagerContainer.getCurrentItem();
        switch (state) {
            case ViewPager.SCROLL_STATE_DRAGGING:
                if (currentItem == itemCount - 1) {
                    pagerContainer.setCurrentItem(1, false);
                } else if (currentItem == 0) {
                    pagerContainer.setCurrentItem(itemCount - 2, false);
                }
                break;
            default:
        }
    }
}
