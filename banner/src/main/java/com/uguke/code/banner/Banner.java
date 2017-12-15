package com.uguke.code.banner;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;

import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.IntDef;
import android.support.annotation.RequiresApi;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.uguke.code.banner.adapter.BannerAdapter;
import com.uguke.code.banner.adapter.ImageBannerAdapter;
import com.uguke.code.banner.bean.IBannerValue;
import com.uguke.code.banner.transformer.BaseTransformer;
import com.uguke.code.banner.util.ShapeUtil;
import com.uguke.code.banner.view.BannerScroller;
import com.uguke.code.banner.view.BannerViewPager;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

/**
 * 功能描述：轮播图控件
 * @author LeiJue
 * @time 2017/08/25
 */
public class Banner extends FrameLayout implements Observer, ViewPager.OnPageChangeListener {

    private static final int HANDLER_WHAT = 100;

    /** 轮播图滚动设置 **/
    private BannerScroller scroller;
    /** 轮播图标题 **/
    private TextView bannerTitle;
    /** 轮播图标题栏提示 **/
    private TextView bannerHint;
    /** 数字指示器 **/
    private TextView numIndicator;
    /** Viewpager容器 **/
    private BannerViewPager pagerContainer;
    /** 标题部分容器 **/
    private LinearLayout titleContainer;
    /** 圆形指示器容器 **/
    private LinearLayout indicatorContainer;
    /** 轮播图信息显示容器 **/
    private RelativeLayout messageContainer;

    /** 像素密度 **/
    private float density;
    /** 上次指示器位置 **/
    private int indicatorLastPosition;
    /** 是否是随机模式 **/
    private boolean isRandomModel;
    /** 是否自动播放 **/
    private boolean isAutomatic;
    /** 是否可以滑动 **/
    private boolean isScrollable;
    /** 是否开启循环 **/
    private boolean isLoop;
    /** 延时时间 **/
    private int delayTime;
    /** 滚动时间 **/
    private int scrollTime;
    /** 当前指示器位置 **/
    private int curPosition;
    //指示器Drawable
    private Drawable indicatorSelectedDrawable;
    private Drawable indicatorUnselectedDrawable;
    /** 指示器列表 **/
    private List<View> indicatorList;
    /** 上下文 **/
    private Context context;
    /** 数据适配器 **/
    private BannerAdapter adapter;
    /** 轮播图属性配置 **/
    private BannerConfig bannerConfig;
    /** 轮播图切换动画管理 **/
    private TransformerManager manager;
    /** 轮播图切换监听 **/
    private OnPageChangeListener listener;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case HANDLER_WHAT:
                    int currentItem = pagerContainer.getCurrentItem() + 1;
                    if (currentItem > adapter.getRealCount()) {
                        pagerContainer.setCurrentItem(0, false);
                        pagerContainer.setCurrentItem(1, true);
                        handler.sendEmptyMessageDelayed(
                                HANDLER_WHAT, delayTime + scrollTime);
                    } else {
                        if (isLoop || currentItem != adapter.getRealCount()) {
                            pagerContainer.setCurrentItem(currentItem, true);
                            handler.sendEmptyMessageDelayed(
                                    HANDLER_WHAT, delayTime + scrollTime);
                        }
                    }
                    break;
                default:
                    break;
            }
        }
    };
    
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
        stop();
        super.onDetachedFromWindow();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (isAutomatic) {
            switch (ev.getAction()) {
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_CANCEL:
                case MotionEvent.ACTION_OUTSIDE:
                    handler.sendEmptyMessageDelayed(HANDLER_WHAT, delayTime);
                    break;
                case MotionEvent.ACTION_DOWN:
                    stop();
                    break;
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    /**
     * 初始化数据
     * @param context
     */
    private void initData(Context context, AttributeSet attrs) {

        this.context = context;
        density = getResources().getDisplayMetrics().density;
        indicatorList = new ArrayList<>();
        manager = new TransformerManager();
        bannerConfig = new BannerConfig();
        bannerConfig.addObserver(this);

        curPosition = 0;
        delayTime = 3000;
        scrollTime = 600;
        isLoop = true;
        isAutomatic = true;
        isScrollable = true;

        if (attrs != null) {
            TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.Banner);

            isLoop = typedArray.getBoolean(R.styleable.Banner_loop, isLoop);
            isAutomatic = typedArray.getBoolean(R.styleable.Banner_automatic, isAutomatic);
            isScrollable = typedArray.getBoolean(R.styleable.Banner_scrollable, isScrollable);

            delayTime = typedArray.getInt(R.styleable.Banner_delayTime, delayTime);
            scrollTime = typedArray.getInt(R.styleable.Banner_scrollTime, scrollTime);

            int scaleType = typedArray.getInt(R.styleable.Banner_scaleType, bannerConfig.getScaleType());

            //轮播图样式
            @BannerType
            int bannerType = typedArray.getInt(R.styleable.Banner_bannerType,
                    bannerConfig.getBannerType());

            //指示器容器内部间隔
            float indicatorPadding = typedArray.getDimension(R.styleable.Banner_indicator_padding, 0) / density;
            float indicatorPaddingLeft = bannerConfig.getIndicatorPaddingLeft();
            float indicatorPaddingTop = bannerConfig.getIndicatorPaddingTop();
            float indicatorPaddingRight = bannerConfig.getIndicatorPaddingRight();
            float indicatorPaddingBottom = bannerConfig.getIndicatorPaddingBottom();
            if (indicatorPadding != 0) {
                indicatorPaddingLeft = typedArray.getDimension(R.styleable.Banner_indicator_paddingLeft,
                        indicatorPadding  * density) / density;
                indicatorPaddingRight = typedArray.getDimension(R.styleable.Banner_indicator_paddingRight,
                        indicatorPadding  * density) / density;
                indicatorPaddingTop = typedArray.getDimension(R.styleable.Banner_indicator_paddingTop,
                        indicatorPadding  * density) / density;
                indicatorPaddingBottom = typedArray.getDimension(R.styleable.Banner_indicator_paddingBottom,
                        indicatorPadding * density ) / density;
            }

            //指示器背景
            int indicatorBackgroundColor = typedArray.getColor(R.styleable.Banner_indicator_background,
                    bannerConfig.getIndicatorBackgroundColor());
            //指示器资源ID
            int indicatorSelectedResId = typedArray.getResourceId(R.styleable.Banner_indicator_selected, -1);
            int indicatorUnselectedResId = typedArray.getResourceId(R.styleable.Banner_indicator_unselected, -1);
            //指示器资源形状
            @Indicator
            int indicatorSelectedShape = typedArray.getInt(R.styleable.Banner_indicator_selected_shape, 0);
            @Indicator
            int indicatorUnselectedShape = typedArray.getInt(R.styleable.Banner_indicator_unselected_shape, 0);

            //标题栏属性
            int titleBackgroundColor = typedArray.getColor(R.styleable.Banner_title_background,
                    bannerConfig.getTitleBackgroundColor());
            int titleTextColor = typedArray.getColor(R.styleable.Banner_title_textColor,
                    bannerConfig.getTitleTextColor());
            int titleHintColor = typedArray.getColor(R.styleable.Banner_title_hintColor,
                    bannerConfig.getTitleHintColor());
            //标题栏内部间隔
            float titlePadding = typedArray.getDimension(
                    R.styleable.Banner_title_padding, 0) / density;
            //标题栏内部左部间隔
            float titlePaddingLeft = bannerConfig.getTitlePaddingLeft();
            //标题栏内部右部间隔
            float titlePaddingRight = bannerConfig.getTitlePaddingRight();
            if (titlePadding != 0) {
                titlePaddingLeft = typedArray.getDimension(R.styleable.Banner_title_paddingLeft,
                    titlePadding  * density) / density;
                titlePaddingRight = typedArray.getDimension(R.styleable.Banner_title_paddingRight,
                    titlePadding  * density) / density;
            }

            float titleHeight = typedArray.getDimension(R.styleable.Banner_title_height,
                    bannerConfig.getTitleHeight() * density) / density;
            float titleTextSize = typedArray.getDimension(R.styleable.Banner_title_textSize,
                    bannerConfig.getTitleTextSize() * density) / density;
            float titleHintSize = typedArray.getDimension(R.styleable.Banner_title_hintSize,
                    bannerConfig.getTitleHintSize() * density) / density;

            //指示器
            float indicatorWidth = typedArray.getDimension(R.styleable.Banner_indicator_width,
                    bannerConfig.getIndicatorWidth() * density) / density;
            float indicatorHeight = typedArray.getDimension(R.styleable.Banner_indicator_height,
                    bannerConfig.getIndicatorHeight() * density) / density;
            //指示器外部间隔
            float indicatorInMargin = typedArray.getDimension(
                    R.styleable.Banner_indicator_in_margin, 0) / density;
            float indicatorMargin = typedArray.getDimension(
                    R.styleable.Banner_indicator_margin, 0) / density;
            float indicatorMarginLeft = bannerConfig.getIndicatorMarginLeft();
            float indicatorMarginRight = bannerConfig.getIndicatorMarginRight();
            float indicatorMarginBottom = bannerConfig.getIndicatorMarginBottom();

            if (indicatorMargin != 0) {
                indicatorMarginLeft = typedArray.getDimension(R.styleable.Banner_indicator_marginLeft,
                        bannerConfig.getIndicatorMarginLeft() * density) / density;
                indicatorMarginRight = typedArray.getDimension(R.styleable.Banner_indicator_marginRight,
                        bannerConfig.getIndicatorMarginRight() * density) / density;
                indicatorMarginBottom = typedArray.getDimension(R.styleable.Banner_indicator_marginBottom,
                        bannerConfig.getIndicatorMarginBottom() * density) / density;
            }

            //指示器颜色
            int indicatorSelectedColor = typedArray.getColor(R.styleable.Banner_indicator_selected,
                    bannerConfig.getIndicatorSelectedColor());
            int indicatorUnselectedColor = typedArray.getColor(R.styleable.Banner_indicator_unselected,
                    bannerConfig.getIndicatorUnselectedColor());
            //数字指示器
            int numIndicatorBackgroundColor = typedArray.getColor(R.styleable.Banner_indicator_num_background,
                    bannerConfig.getNumIndicatorBackgroundColor());
            int numIndicatorTextColor = typedArray.getColor(R.styleable.Banner_indicator_num_textColor,
                    bannerConfig.getNumIndicatorTextColor());
            float numIndicatorSize = typedArray.getDimension(R.styleable.Banner_indicator_num_backgroundSize,
                    bannerConfig.getNumIndicatorSize() * density) / density;
            float numIndicatorTextSize = typedArray.getDimension(R.styleable.Banner_indicator_num_textSize,
                    bannerConfig.getNumIndicatorTextSize() * density) / density;
            float numIndicatorMargin = typedArray.getDimension(R.styleable.Banner_indicator_num_margin,
                    bannerConfig.getNumIndicatorMargin() * density) / density;
            bannerConfig
                    .setBannerType(bannerType)      //设置轮播图样式
                    .setScaleType(scaleType)        //设置ImageBannerAdapter缩放样式
                    //标题栏属性
                    .setTitleBackgroundColor(titleBackgroundColor)  //设置标题栏背景颜色
                    .setTitlePaddingLeft(titlePaddingLeft)          //设置标题栏内部左间隔
                    .setTitlePaddingRight(titlePaddingRight)        //设置标题栏内部右间隔
                    .setTitleHeight(titleHeight)                    //设置标题栏高度
                    .setTitleTextColor(titleTextColor)              //设置标题文字颜色
                    .setTitleTextSize(titleTextSize)                //设置标题文字大小
                    .setTitleHintColor(titleHintColor)              //设置标题提示颜色
                    .setTitleHintSize(titleHintSize)                //设置标题提示大小
                    //指示器属性
                    .setIndicatorPaddingLeft(indicatorPaddingLeft)          //设置指示器容器内部左间隔
                    .setIndicatorPaddingTop(indicatorPaddingTop)            //设置指示器容器内部右间隔
                    .setIndicatorPaddingRight(indicatorPaddingRight)        //设置指示器容器内部右间隔
                    .setIndicatorPaddingBottom(indicatorPaddingBottom)      //设置指示器容器内部下间隔
                    .setIndicatorWidth(indicatorWidth)                      //设置指示器宽度
                    .setIndicatorHeight(indicatorHeight)                    //设置指示器高度
                    .setIndicatorInMargin(indicatorInMargin)                //设置指示器容器内部指示器间隔
                    .setIndicatorMarginLeft(indicatorMarginLeft)            //设置指示器容器外部左间隔
                    .setIndicatorMarginRight(indicatorMarginRight)          //设置指示器容器外部右间隔
                    .setIndicatorMarginBottom(indicatorMarginBottom)        //设置指示器容器外部下间隔
                    .setIndicatorBackgroundColor(indicatorBackgroundColor)  //设置指示器背景颜色
                    .setIndicatorSelectedColor(indicatorSelectedColor)      //设置指示器选择时的颜色
                    .setIndicatorUnselectedColor(indicatorUnselectedColor)  //设置指示器未选择时的颜色
                    .setIndicatorSelectedResId(indicatorSelectedResId)      //设置指示器选择时的资源
                    .setIndicatorUnselectedResId(indicatorUnselectedResId)  //设置指示器未选择时的资源
                    .setIndicatorSelectedShape(indicatorSelectedShape)      //设置指示器选择时形状
                    .setIndicatorUnselectedShape(indicatorUnselectedShape)  //设置指示器未选择时的形状
                    //
                    .setNumIndicatorBackgroundColor(numIndicatorBackgroundColor)//设置数字指示器背景颜色
                    .setNumIndicatorTextColor(numIndicatorTextColor)    //设置数字指示器文字颜色
                    .setNumIndicatorSize(numIndicatorSize)              //设置数字指示器大小
                    .setNumIndicatorTextSize(numIndicatorTextSize)      //设置数字指示器文字大小
                    .setNumIndicatorMargin(numIndicatorMargin);         //设置数字指示器外部底部间隔
            typedArray.recycle();
        }
    }

    /**
     * 功能描述：初始化控件
     */
    private void initView() {
        //清空Banner内部控件
        removeAllViews();
        //初始化
        View container = LayoutInflater.from(context).inflate(R.layout.banner, this, true);
        //初始化文本控件
        bannerHint = (TextView) container.findViewById(R.id.banner_hint);
        bannerTitle = (TextView) container.findViewById(R.id.banner_title);
        numIndicator = (TextView) container.findViewById(R.id.indicator_num);

        //初始化容器
        indicatorContainer = (LinearLayout) container.findViewById(R.id.container_indicator);
        messageContainer = (RelativeLayout) container.findViewById(R.id.container_message);
        titleContainer = (LinearLayout) container.findViewById(R.id.container_title);

        //ViewPager容器
        pagerContainer = (BannerViewPager) container.findViewById(R.id.container_pager);
        pagerContainer.addOnPageChangeListener(this);
        pagerContainer.setFocusable(true);
        pagerContainer.setScrollable(isScrollable);
        pagerContainer.setOffscreenPageLimit(3);

        //设置指示器容器
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.CENTER_HORIZONTAL);
        params.bottomMargin = (int) (bannerConfig.getIndicatorMarginBottom() * density);
        indicatorContainer.setLayoutParams(params);
        //设置背景
        setIndicatorBackgroundColor(
                bannerConfig.getIndicatorBackgroundColor());
        //设置内部间隔
        indicatorContainer.setPadding(
                (int) (bannerConfig.getIndicatorPaddingLeft() * density),
                (int) (bannerConfig.getIndicatorPaddingTop() * density),
                (int) (bannerConfig.getIndicatorPaddingRight() * density),
                (int) (bannerConfig.getIndicatorPaddingBottom() * density));
        //设置标题栏属性
        params = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                (int) (bannerConfig.getTitleHeight() * density));
        params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);

        titleContainer.setLayoutParams(params);
        titleContainer.setBackgroundColor(bannerConfig.getTitleBackgroundColor());
        titleContainer.setPadding(
                (int) (bannerConfig.getTitlePaddingLeft() * density), 0,
                (int) (bannerConfig.getTitlePaddingRight() * density), 0);
        bannerTitle.setTextColor(bannerConfig.getTitleTextColor());
        bannerTitle.setTextSize(bannerConfig.getTitleTextSize());
        bannerHint.setTextColor(bannerConfig.getTitleHintColor());
        bannerHint.setTextSize(bannerConfig.getTitleHintSize());

        //设置数字指示器属性
        numIndicator.setTextColor(bannerConfig.getNumIndicatorTextColor());
        params = new RelativeLayout.LayoutParams(
                (int) (bannerConfig.getNumIndicatorSize() * density),
                (int) (bannerConfig.getNumIndicatorSize() * density));
        params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        params.addRule(RelativeLayout.ABOVE, R.id.container_title);
        params.rightMargin = (int) (bannerConfig.getNumIndicatorMargin() * density);
        params.bottomMargin = (int) (bannerConfig.getNumIndicatorMargin() * density);

        //设置数字指示器背景
        GradientDrawable drawable = new GradientDrawable();
        drawable.setColor(bannerConfig.getNumIndicatorBackgroundColor());
        drawable.setCornerRadius(bannerConfig.getNumIndicatorSize() * density);
        drawable.setShape(GradientDrawable.RECTANGLE);
        ViewCompat.setBackground(numIndicator, drawable);

        numIndicator.setLayoutParams(params);
        numIndicator.setTextColor(bannerConfig.getNumIndicatorTextColor());
        numIndicator.setTextSize(bannerConfig.getNumIndicatorTextSize());

        //初始化轮播图
        initBanner();
        //重新给Viewpager绑定Scroller
        scroller = new BannerScroller();
        scroller.bindViewPager(pagerContainer);
        scroller.setScrollTime(scrollTime);

    }

    /**
     * 设置指示器背景颜色
     * @param color
     */
    private void setIndicatorBackgroundColor(int color) {
        GradientDrawable drawable = new GradientDrawable();
        drawable.setColor(color);
        drawable.setCornerRadius(
                bannerConfig.getIndicatorHeight() +
                bannerConfig.getIndicatorPaddingTop() * density +
                bannerConfig.getIndicatorPaddingBottom() * density);
        drawable.setShape(GradientDrawable.RECTANGLE);
        ViewCompat.setBackground(indicatorContainer, drawable);
    }

    /**
     * 功能描述：设置标题栏
     */
    private void initBanner() {
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        bannerHint.setVisibility(VISIBLE);
        //设置指示器容器背景颜色
        setIndicatorBackgroundColor(
                 bannerConfig.getIndicatorBackgroundColor());
        switch (bannerConfig.getBannerType()) {
            case TYPE_INDICATOR_CENTER:
                messageContainer.setVisibility(VISIBLE);
                numIndicator.setVisibility(GONE);
                titleContainer.setVisibility(GONE);
                indicatorContainer.setVisibility(VISIBLE);
                //设置Params
                params.addRule(RelativeLayout.CENTER_HORIZONTAL);
                params.bottomMargin = (int) (bannerConfig.getIndicatorMarginBottom() * density);
                indicatorContainer.setLayoutParams(params);
                break;
            case TYPE_INDICATOR_LEFT:
                messageContainer.setVisibility(VISIBLE);
                numIndicator.setVisibility(GONE);
                titleContainer.setVisibility(GONE);
                indicatorContainer.setVisibility(VISIBLE);
                //设置Params
                params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
                params.leftMargin = (int) (bannerConfig.getIndicatorMarginLeft() * density);
                params.bottomMargin = (int) (bannerConfig.getIndicatorMarginBottom() * density);
                indicatorContainer.setLayoutParams(params);
                break;
            case TYPE_INDICATOR_RIGHT:
                messageContainer.setVisibility(VISIBLE);
                numIndicator.setVisibility(GONE);
                titleContainer.setVisibility(GONE);
                indicatorContainer.setVisibility(VISIBLE);
                //设置Params
                params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                params.rightMargin = (int) (bannerConfig.getIndicatorMarginRight() * density);
                params.bottomMargin = (int) (bannerConfig.getIndicatorMarginBottom() * density);
                indicatorContainer.setLayoutParams(params);
                break;
            case TYPE_NUM_INDICATOR:
                messageContainer.setVisibility(VISIBLE);
                numIndicator.setVisibility(VISIBLE);
                titleContainer.setVisibility(GONE);
                indicatorContainer.setVisibility(GONE);
                break;
            case TYPE_TITLE:
                messageContainer.setVisibility(VISIBLE);
                numIndicator.setVisibility(GONE);
                titleContainer.setVisibility(VISIBLE);
                indicatorContainer.setVisibility(GONE);
                break;
            case TYPE_TITLE_NUM_INDICATOR:
                messageContainer.setVisibility(VISIBLE);
                numIndicator.setVisibility(VISIBLE);
                titleContainer.setVisibility(VISIBLE);
                indicatorContainer.setVisibility(GONE);
                break;
            case TYPE_TITLE_INDICATOR_CENTER:
                messageContainer.setVisibility(VISIBLE);
                numIndicator.setVisibility(GONE);
                titleContainer.setVisibility(VISIBLE);
                indicatorContainer.setVisibility(VISIBLE);
                //设置Params
                params.addRule(RelativeLayout.CENTER_HORIZONTAL);
                params.bottomMargin = (int) (bannerConfig.getIndicatorMarginBottom() * density);
                params.addRule(RelativeLayout.ABOVE, R.id.container_title);
                indicatorContainer.setLayoutParams(params);
                break;
            case TYPE_TITLE_INDICATOR_LEFT:
                messageContainer.setVisibility(VISIBLE);
                numIndicator.setVisibility(GONE);
                titleContainer.setVisibility(VISIBLE);
                indicatorContainer.setVisibility(VISIBLE);
                //设置Params
                params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
                params.leftMargin = (int) (bannerConfig.getIndicatorMarginLeft() * density);
                params.bottomMargin = (int) (bannerConfig.getIndicatorMarginBottom() * density);
                params.addRule(RelativeLayout.ABOVE, R.id.container_title);
                indicatorContainer.setLayoutParams(params);
                break;
            case TYPE_TITLE_INDICATOR_RIGHT:
                messageContainer.setVisibility(VISIBLE);
                numIndicator.setVisibility(GONE);
                titleContainer.setVisibility(VISIBLE);
                indicatorContainer.setVisibility(VISIBLE);
                //设置Params
                params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                params.rightMargin = (int) (bannerConfig.getIndicatorMarginRight() * density);
                params.bottomMargin = (int) (bannerConfig.getIndicatorMarginBottom() * density);
                params.addRule(RelativeLayout.ABOVE, R.id.container_title);
                indicatorContainer.setLayoutParams(params);
                break;
            case TYPE_TITLE_IN_INDICATOR:
                messageContainer.setVisibility(VISIBLE);
                numIndicator.setVisibility(GONE);
                titleContainer.setVisibility(VISIBLE);
                indicatorContainer.setVisibility(VISIBLE);
                bannerHint.setVisibility(INVISIBLE);
                setIndicatorBackgroundColor(Color.TRANSPARENT);
                //设置Params
                params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                params.addRule(RelativeLayout.CENTER_VERTICAL);
                params.rightMargin = (int) (bannerConfig.getIndicatorMarginRight() * density);
                indicatorContainer.setLayoutParams(params);
                params = new RelativeLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        (int) (bannerConfig.getTitleHeight() * density));
                titleContainer.setLayoutParams(params);
                break;
            default:
                messageContainer.setVisibility(GONE);
                break;
        }
    }

    /**
     * 功能描述：初始化指示器
     */
    private void initIndicator() {
        indicatorList.clear();
        indicatorContainer.removeAllViews();
        initDrawable();
        if (adapter.getRealCount() == 0) {
            indicatorContainer.setVisibility(INVISIBLE);
        } else {
            indicatorContainer.setVisibility(VISIBLE);
        }
        for (int i = 0; i < adapter.getRealCount(); i++) {
            View view = new View(context);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    (int) (bannerConfig.getIndicatorWidth() * density),
                    (int) (bannerConfig.getIndicatorHeight() * density));
            params.leftMargin = (int) (bannerConfig.getIndicatorInMargin() * density);
            params.rightMargin = (int) (bannerConfig.getIndicatorInMargin() * density);
            view.setLayoutParams(params);
            if (i == 0) {
                ViewCompat.setBackground(view, indicatorSelectedDrawable);
            } else {
                ViewCompat.setBackground(view, indicatorUnselectedDrawable);
            }
            indicatorContainer.addView(view);
            indicatorList.add(view);
        }
    }

    /**
     * 功能描述：初始化Drawable
     */
    private void initDrawable() {
        //选择时的背景
        indicatorSelectedDrawable = getDrawable(
                bannerConfig.getIndicatorSelectedResId());
        if (indicatorSelectedDrawable == null) {
            indicatorSelectedDrawable = getDrawable(
                    bannerConfig.getIndicatorSelectedShape(),
                    bannerConfig.getIndicatorSelectedColor());
        }
        //未选择时的背景
        indicatorUnselectedDrawable = getDrawable(
                bannerConfig.getIndicatorUnselectedResId());
        if (indicatorUnselectedDrawable == null) {
            indicatorUnselectedDrawable = getDrawable(
                    bannerConfig.getIndicatorUnselectedShape(),
                    bannerConfig.getIndicatorUnselectedColor());
        }
    }

    /**
     * 功能描述：获取Drawable
     * @param resId
     * @return
     */
    private Drawable getDrawable(int resId) {
        Drawable drawable = null;
        if (resId != -1) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (getResources().getDrawable(resId, context.getTheme()) != null) {
                    drawable = getResources().getDrawable(resId, context.getTheme());
                }
            } else {
                if (getResources().getDrawable(resId) != null) {
                    drawable = getResources().getDrawable(resId);
                }
            }
        }
        return drawable;
    }

    /**
     * 功能描述：根据形状和颜色获取Drawable
     * @param color
     * @return
     */
    private Drawable getDrawable(@Indicator int indicator, int color) {

        int width = (int) (bannerConfig.getIndicatorWidth() * density);
        int height = (int) (bannerConfig.getIndicatorHeight() * density);

        switch (indicator) {
            case SHAPE_CIRCLE:
                return ShapeUtil.circleShape(width, height, color, true);
            case SHAPE_SQUARE:
                return ShapeUtil.squareShape(width, height, color, true);
            case SHAPE_LINE:
                return ShapeUtil.lineShape(width, height, color);
            case SHAPE_STAR:
                return ShapeUtil.starShape(width, height, color, true);
            case SHAPE_TRIANGLE:
                return ShapeUtil.triangleShape(width, height, color, true);
            case SHAPE_RING:
                return ShapeUtil.circleShape(width, height, color, false);
            case SHAPE_EMPTY_SQUARE:
                return ShapeUtil.squareShape(width, height, color, false);
            case SHAPE_EMPTY_START:
                return ShapeUtil.starShape(width, height, color, false);
            case SHAPE_EMPTY_TRIANGLE:
                return ShapeUtil.triangleShape(width, height, color, false);
            default:
                return ShapeUtil.circleShape(width, height, color, true);
        }
    }

    /**
     * 功能描述：获取缩放样式
     * @param scaleType
     * @return
     */
    private ImageView.ScaleType getScaleType(int scaleType) {
        switch (scaleType) {
            case 0:  return ImageView.ScaleType.CENTER;
            case 1:  return ImageView.ScaleType.CENTER_CROP;
            case 2:  return ImageView.ScaleType.CENTER_INSIDE;
            case 3:  return ImageView.ScaleType.FIT_CENTER;
            case 4:  return ImageView.ScaleType.FIT_END;
            case 5:  return ImageView.ScaleType.FIT_START;
            case 6:  return ImageView.ScaleType.FIT_XY;
            case 7:  return ImageView.ScaleType.MATRIX;
            default: return ImageView.ScaleType.CENTER_CROP;
        }
    }

    /**
     * 功能描述：获取轮播图配置属性
     * @return
     */
    public BannerConfig getBannerConfig() {
        return bannerConfig;
    }

    /**
     * 功能描述：是否自动切换
     */
    public boolean isAutomatic() {
        return isAutomatic;
    }

    /**
     * 功能描述：是否可手动滑动
     */
    public boolean isScrollable() {
        return isScrollable;
    }

    /**
     * 功能描述：是否可循环
     */
    public boolean isLoop() {
        return isLoop;
    }

    /**
     * 功能描述：获取轮播图播放循环时间
     */
    public int getDelayTime() {
        return delayTime;
    }

    /**
     * 功能描述：获取轮播图播放滚动时间
     */
    public int getScrollTime() {
        return scrollTime;
    }

    /**
     * 功能描述：设置轮播图动画
     * @param transformer
     * @return
     */
    public Banner setTransformer(BaseTransformer transformer) {
        if (transformer != null) {
            manager.setTransformer(transformer);
        }
        return this;
    }

    /**
     * 功能描述：添加轮播图动画
     * @param transformer
     * @return
     */
    public Banner addTransformer(BaseTransformer transformer) {
        if (transformer != null) {
            manager.addTransformer(transformer);
        }
        return this;
    }
    /**
     * 功能描述：开启轮播图动画
     * @param isRandomModel
     * @return
     */
    public Banner openRandomModel(boolean isRandomModel) {
        this.isRandomModel = isRandomModel;
        return this;
    }

    /**
     * 功能描述：设置数据适配器
     * @param adapter
     * @return
     */
    public Banner setAdapter(final BannerAdapter adapter) {
        this.adapter = adapter;
        if(adapter != null) {
            if (adapter instanceof ImageBannerAdapter) {
                ((ImageBannerAdapter) adapter).setScaleType(
                        getScaleType(bannerConfig.getScaleType()));
            }
            adapter.setOnRefreshListener(new BannerAdapter.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    initIndicator();
                }
            });
        }
        return this;
    }

    /**
     * 功能描述：设置是否自动切换
     * @param automatic
     */
    public Banner setAutomatic(boolean automatic) {
        this.isAutomatic = automatic;
        return this;
    }

    /**
     * 功能描述：设置是否可手动滑动
     * @param isScrollable
     */
    public Banner setScrollable(boolean isScrollable) {
        this.isScrollable = isScrollable;
        pagerContainer.setScrollable(isScrollable);
        return this;
    }

    /**
     * 功能描述：设置是否可循环
     * @param loop
     */
    public Banner setLoop(boolean loop) {
        isLoop = loop;
        if (adapter != null) {
            adapter.setLoop(isLoop);
        }
        return this;
    }

    /**
     * 功能描述：设置轮播图播放循环时间
     * @param delayTime
     */
    public Banner setDelayTime(int delayTime) {
        if (delayTime < 0) delayTime = 0;
        this.delayTime = delayTime;
        return this;
    }

    /**
     * 功能描述：设置轮播图播放滚动时间
     * @param scrollTime
     */
    public Banner setScrollTime(int scrollTime) {
        if (scrollTime < 0) scrollTime = 0;
        this.scrollTime = scrollTime;
        return this;
    }
    /**
     * 功能描述：设置当前位置
     * @param position 当前位置
     */
    public Banner setCurrentItem(int position) {
        setCurrentItem(position, false);
        return this;
    }

    /**
     * 功能描述：设置当前位置
     * @param position 当前位置
     * @param smoothScroll 是否显示动画
     */
    public Banner setCurrentItem(int position, boolean smoothScroll) {
        curPosition = position + (isLoop ? 1 : 0);
        if (pagerContainer.getAdapter() != null) {
            pagerContainer.setCurrentItem(curPosition, smoothScroll);
        }
        return this;
    }

    /**
     * 功能描述：开始轮播图
     */
    public void start() {
        if (adapter == null)
            return;
        stop();
        // 设置动画类型
        pagerContainer.setPageTransformer(true, isRandomModel ? manager.random() : manager.next());
        pagerContainer.setAdapter(adapter);
        adapter.setLoop(isLoop);
        if (adapter.getRealCount() > 0) {
            if (isLoop) {
                IBannerValue value = adapter.getItem(indicatorLastPosition + 1);
                bannerHint.setText(value.getHint());
                bannerTitle.setText(value.getTitle());
            } else {
                IBannerValue value = adapter.getItem(indicatorLastPosition);
                bannerHint.setText(value.getHint());
                bannerTitle.setText(value.getTitle());
            }
            pagerContainer.setCurrentItem(curPosition, false);
        }

        if (isAutomatic) {
            handler.sendEmptyMessageDelayed(HANDLER_WHAT, delayTime);
        }
    }

    /**
     * 功能描述：停止轮播图
     */
    public void stop() {
        handler.removeMessages(HANDLER_WHAT);
    }

    /**
     * 功能描述：设置轮播图切换监听
     * @param listener
     */
    public void setOnPageChangeListener(OnPageChangeListener listener) {
        this.listener = listener;
    }

    /**
     * 功能描述：获取当前轮播图位置
     * @return 当前轮播图位置
     */
    public int getCurrentItem() {
        return curPosition - (isLoop ? 1 : 0);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

    @Override
    public void onPageSelected(int position) {

        curPosition = position;

        IBannerValue value = adapter.getItem(position);
        //设置Banner属性
        bannerHint.setText(value.getHint());
        bannerTitle.setText(value.getTitle());

        //真实数量
        int realCount = adapter.getRealCount();
        if (isLoop) {
            if (position > realCount) {
                position = 0;
            } else if (position == 0) {
                position = realCount - 1;
            } else {
                position--;
            }
        }
        //设置背景
        ViewCompat.setBackground(indicatorList.get(indicatorLastPosition),
                indicatorUnselectedDrawable);
        ViewCompat.setBackground(indicatorList.get(position),
                indicatorSelectedDrawable);
        //设置圆形指示器
        numIndicator.setText((position + 1) + "/" + adapter.getRealCount());
        indicatorLastPosition = position;
        // 设置动画类型
        pagerContainer.setPageTransformer(true, isRandomModel ? manager.random() : manager.next());
        if (listener != null) {
            listener.onPageSelected(position, pagerContainer.getCurrentItem());
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        if (!isLoop)
            //如果不开启循环，则直接返回
            return;
        //获取真实数量和当前Item位置
        int realCount = adapter.getRealCount();
        int currentItem = pagerContainer.getCurrentItem();
        switch (state) {
            case ViewPager.SCROLL_STATE_DRAGGING:
                if (currentItem > realCount) {
                    pagerContainer.setCurrentItem(1, false);
                } else if (currentItem == 0) {
                    pagerContainer.setCurrentItem(realCount, false);
                }
                break;
        }
    }

    @Override
    public void update(Observable o, Object arg) {
        initView();
        initIndicator();
    }

    /** Banner样式（不显示任何附加信息） **/
    public static final int TYPE_NONE = 0;
    /** Banner样式（只显示图形指示器居中） **/
    public static final int TYPE_INDICATOR_CENTER = 1;
    /** Banner样式（只显示图形指示器居左） **/
    public static final int TYPE_INDICATOR_LEFT = 2;
    /** Banner样式（只显示图形指示器居右） **/
    public static final int TYPE_INDICATOR_RIGHT = 3;
    /** Banner样式（只显示数字指示器） **/
    public static final int TYPE_NUM_INDICATOR = 4;
    /** Banner样式（只显示标题栏） **/
    public static final int TYPE_TITLE = 5;
    /** Banner样式（标题栏及数字指示器） **/
    public static final int TYPE_TITLE_NUM_INDICATOR = 6;
    /** Banner样式（显示标题栏及外部图形指示器居中） **/
    public static final int TYPE_TITLE_INDICATOR_CENTER = 7;
    /** Banner样式（显示标题栏及外部图形指示器居左） **/
    public static final int TYPE_TITLE_INDICATOR_LEFT = 8;
    /** Banner样式（显示标题栏及外部图形指示器居右） **/
    public static final int TYPE_TITLE_INDICATOR_RIGHT = 9;
    /** Banner样式（显示标题栏及内部图形指示器） **/
    public static final int TYPE_TITLE_IN_INDICATOR = 10;

    @IntDef({
            TYPE_NONE,
            TYPE_INDICATOR_CENTER,
            TYPE_INDICATOR_LEFT,
            TYPE_INDICATOR_RIGHT,
            TYPE_NUM_INDICATOR,
            TYPE_TITLE,
            TYPE_TITLE_NUM_INDICATOR,
            TYPE_TITLE_INDICATOR_CENTER,
            TYPE_TITLE_INDICATOR_LEFT,
            TYPE_TITLE_INDICATOR_RIGHT,
            TYPE_TITLE_IN_INDICATOR})
    @Retention(RetentionPolicy.SOURCE)
    //指示器
    public @interface BannerType {}

    //===========指示器属性===========//
    /** 圆形指示器 **/
    public static final int SHAPE_CIRCLE = 0;
    /** 矩形指示器 **/
    public static final int SHAPE_SQUARE = 1;
    /** 线性指示器 **/
    public static final int SHAPE_LINE = 2;
    /** 星形指示器 **/
    public static final int SHAPE_STAR = 3;
    /** 三角形指示器 **/
    public static final int SHAPE_TRIANGLE = 4;
    /** 圆环指示器 **/
    public static final int SHAPE_RING = 5;
    /** 空心矩形指示器 **/
    public static final int SHAPE_EMPTY_SQUARE = 6;
    /** 空心星形指示器 **/
    public static final int SHAPE_EMPTY_START = 7;
    /** 空心三角形指示器 **/
    public static final int SHAPE_EMPTY_TRIANGLE = 8;

    @IntDef({
            SHAPE_CIRCLE,
            SHAPE_SQUARE,
            SHAPE_LINE,
            SHAPE_STAR,
            SHAPE_TRIANGLE,
            SHAPE_RING,
            SHAPE_EMPTY_SQUARE,
            SHAPE_EMPTY_START,
            SHAPE_EMPTY_TRIANGLE})
    @Retention(RetentionPolicy.SOURCE)
    //指示器
    public @interface Indicator {}

    public interface OnPageChangeListener {
        void onPageSelected(int realPosition, int itemPosition);
    }
}
