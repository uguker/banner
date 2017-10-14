# Banner
轮播图工具，可以实现大部分轮播图效果。

## 导入
1. 在build.gradle添加如下代码：<br>
```
allprojects {
	repositories {
		...
		maven { url 'https://jitpack.io' }
	}
}
```
2. 添加依赖关系
```
dependencies {
	compile 'com.github.uguker:banner:1.1.2'
}
```
## 效果
<img src="https://github.com/uguker/banner/blob/master/screenshot/banner.gif" height="160"><br>

## 使用
```
	Banner banner = (Banner) findViewById(R.id.banner);
        adapter = new ImageBannerAdapter();
        adapter.setImageLoader(new SimpleImageLoader() {
            @Override
            public void loadImage(Context context, Object path, ImageView imageView) {
                Glide.with(context)
                        .load(path)
                        .placeholder(R.drawable.b2)
                        .into(imageView);
            }
        });

        List<BannerValue> values = new ArrayList<>();
        //设置数据
        BannerValue value = new BannerValue();
        value.setTitle("美女");
        value.setHint("001");
        value.setUri(R.drawable.b2);
        values.add(value);

        value = new BannerValue();
        value.setTitle("呵呵");
        value.setHint("002");
        value.setUri(R.drawable.bb);
        values.add(value);

        value = new BannerValue();
        value.setTitle("美景");
        value.setHint("003");
        value.setUri(R.drawable.b3);
        values.add(value);
        //绑定数据
        banner.setAdapter(adapter);
        adapter.setData(values);
        adapter.setScaleType(ImageView.ScaleType.FIT_XY);
        adapter.setOnItemClickListener(new ImageBannerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int realPosition, int itemPosition, BannerValue value) {
                Toast.makeText(MainActivity.this,
                        value.getTitle() + realPosition + itemPosition,
                        Toast.LENGTH_SHORT).show();
            }
        });

        //配置Config
        banner.getBannerConfig()
                .setIndicatorInMargin(2)    //指示器内部指示器间隔，单位dp
                .setIndicatorMargin(20)     //指示器外部间隔，单位dp
                .setIndicatorMarginRight(10)//指示器外部右边间隔，单位dp
                .setIndicatorMarginBottom(0)//指示器外部底部间隔，单位dp
                .setIndicatorHeight(20)     //单个指示器高度，单位dp
                .setIndicatorWidth(20)      //单个指示器宽度，单位dp
                .setIndicatorPadding(10)    //单个指示器内部间隔，单位dp
                .setIndicatorBackgroundColor(Color.TRANSPARENT) //指示器背景颜色
                .setIndicatorSelectedColor(Color.BLUE)          //指示器选中时颜色，设置drawable失效
                .setIndicatorUnselectedColor(Color.RED)         //指示器未选中时颜色，设置drawable失效
                .setIndicatorSelectedShape(Banner.SHAPE_STAR)   //指示器选中时图形，设置drawable失效
                .setIndicatorUnselectedShape(Banner.SHAPE_LINE) //指示器未选中时图形，设置drawable失效
                .setIndicatorSelectedResId(R.drawable.b2)       //指示器选中时图片，设置drawable失效
                .setIndicatorUnselectedResId(R.drawable.b3)     //指示器未选中时图片，设置drawable失效
                .setNumIndicatorMargin(10)                      //数字指示器外部间隔，单位dp
                .setNumIndicatorSize(60)                        //数字指示器大小，单位dp
                .setNumIndicatorTextColor(Color.WHITE)          //数字指示器文字颜色，单位dp
                .setNumIndicatorTextSize(20)                    //数字指示器文字大小，单位dp
                .setTitleTextColor(Color.RED)                   //标题栏文字颜色
                .setTitleHintColor(Color.BLUE)                  //标题栏提示文字颜色
                .setTitleHintSize(20)                           //标题栏提示文字大小，单位dp
                .setTitleTextSize(20)                           //标题栏文字大小，单位dp
                .setTitleHeight(40)                             //标题栏高度，单位dp
                .setTitlePadding(10)                            //标题栏内部间隔，单位dp
                .setTitlePaddingLeft(16)
                .setTitlePaddingRight(16)
                .setIndicatorPaddingBottom(0)
                .setIndicatorPaddingBottom(0)
                .setTitleBackgroundColor(Color.parseColor("#88000000")) //标题栏背景色
                .setBannerType(Banner.TYPE_TITLE_INDICATOR_CENTER)      //轮播图样式
                .notifyViewChange();                                    //通知改变布局
        //banner.notifyViewChange();
        banner.addTransformer(new BTFTransformer());    //添加动画
        banner.openRandomModel(true);                   //开启随机动画
        banner.setScrollable(true);                     //轮播图是否可以手动拖动，默认true
        banner.setLoop(true);                           //是否可以循环播放，默认true
        banner.setScrollTime(800);                      //动画滚动时间，默认600毫秒
        banner.setAutomatic(true);                      //是否自动播放，默认true
        banner.setOnPageChangeListener(new Banner.OnPageChangeListener() {
            @Override
            public void onPageSelected(int realPosition, int itemPosition) {}
        });
        banner.start();

	//banner.stop();	//停止轮播图滚动
```

## XML属性
```
	<!-- 延时时间 -->
        <attr name="delayTime" format="integer"></attr>
        <!-- 滚动时间 -->
        <attr name="scrollTime" format="integer"></attr>
        <!-- 是否自动播放 -->
        <attr name="automatic" format="boolean"></attr>
        <!-- 是否可以滑动 -->
        <attr name="scrollable" format="boolean"></attr>
        <!-- 是否可以循环 -->
        <attr name="loop" format="boolean"></attr>
        <!-- 标题背景 -->
        <attr name="title_background" format="reference|color"></attr>
        <!-- 标题文字颜色 -->
        <attr name="title_textColor" format="color"></attr>
        <!-- 标题文字大小 -->
        <attr name="title_textSize" format="dimension"></attr>
        <!-- 标题提示文字颜色 -->
        <attr name="title_hintColor" format="color"></attr>
        <!-- 标题提示文字大小 -->
        <attr name="title_hintSize" format="dimension"></attr>
        <!-- 标题高度 -->
        <attr name="title_height" format="dimension"></attr>
        <!-- 标题内部间隔 -->
        <attr name="title_padding" format="dimension"></attr>
        <!-- 标题内部左间隔 -->
        <attr name="title_paddingLeft" format="dimension"></attr>
        <!-- 标题内部右间隔 -->
        <attr name="title_paddingRight" format="dimension"></attr>
        <!-- 数字指示器背景 -->
        <attr name="indicator_num_background" format="reference|color"></attr>
        <!-- 数字指示器背景大小 -->
        <attr name="indicator_num_backgroundSize" format="dimension"></attr>
        <!-- 数字指示器文字颜色 -->
        <attr name="indicator_num_textColor" format="color"></attr>
        <!-- 数字指示器文字大小 -->
        <attr name="indicator_num_textSize" format="dimension"></attr>
        <!-- 数字指示器间隔 -->
        <attr name="indicator_num_margin" format="dimension"></attr>
        <!-- 指示器背景色 -->
        <attr name="indicator_background" format="reference|color"></attr>
        <!-- 单个指示器宽度 -->
        <attr name="indicator_width" format="dimension"></attr>
        <!-- 单个指示器高度 -->
        <attr name="indicator_height" format="dimension"></attr>
        <!-- 指示器间间隔 -->
        <attr name="indicator_in_margin" format="dimension"></attr>
        <!-- 指示器外部间隔 -->
        <attr name="indicator_margin" format="dimension"></attr>
        <attr name="indicator_marginLeft" format="dimension"></attr>
        <attr name="indicator_marginRight" format="dimension"></attr>
        <attr name="indicator_marginBottom" format="dimension"></attr>
        <!-- 指示器内部间隔 -->
        <attr name="indicator_padding" format="dimension"></attr>
        <attr name="indicator_paddingLeft" format="dimension"></attr>
        <attr name="indicator_paddingRight" format="dimension"></attr>
        <attr name="indicator_paddingTop" format="dimension"></attr>
        <attr name="indicator_paddingBottom" format="dimension"></attr>
        <!-- 选择时的指示器的颜色和图形（如果设置了图形，设置形状无效） -->
        <attr name="indicator_selected" format="reference|color" />
        <!-- 未选择时的指示器的颜色和图形（如果设置了图形，设置形状无效） -->
        <attr name="indicator_unselected" format="reference|color" />

        <!-- 选择时的指示器的形状 -->
        <attr name="indicator_selected_shape" format="enum">
            <!-- 实心圆 -->
            <enum name="circle" value="0"></enum>
            <!-- 实心方形 -->
            <enum name="square" value="1"></enum>
            <!-- 线条 -->
            <enum name="line" value="2"></enum>
            <!-- 五角星 -->
            <enum name="star" value="3"></enum>
            <!-- 三角形 -->
            <enum name="triangle" value="4"></enum>
            <!-- 圆环 -->
            <enum name="ring" value="5"></enum>
            <!-- 空心方形 -->
            <enum name="emptySquare" value="6"></enum>
            <!-- 空心五角星 -->
            <enum name="emptyStar" value="7"></enum>
            <!-- 空心三角形 -->
            <enum name="emptyTriangle" value="8"></enum>
        </attr>
        <!-- 未选择时的指示器的形状 -->
        <attr name="indicator_unselected_shape" format="enum">
            <!-- 实心圆 -->
            <enum name="circle" value="0"></enum>
            <!-- 实心方形 -->
            <enum name="square" value="1"></enum>
            <!-- 线条 -->
            <enum name="line" value="2"></enum>
            <!-- 五角星 -->
            <enum name="star" value="3"></enum>
            <!-- 三角形 -->
            <enum name="triangle" value="4"></enum>
            <!-- 圆环 -->
            <enum name="ring" value="5"></enum>
            <!-- 空心方形 -->
            <enum name="emptySquare" value="6"></enum>
            <!-- 空心五角星 -->
            <enum name="emptyStar" value="7"></enum>
            <!-- 空心三角形 -->
            <enum name="emptyTriangle" value="8"></enum>
        </attr>

        <attr name="bannerType" format="enum">
            <!-- 没有任何提示 -->
            <enum name="none" value="0"></enum>
            <!-- 只有外部图形指示器居中 -->
            <enum name="indicatorCenter" value="1"></enum>
            <!-- 只有外部图形指示器居左 -->
            <enum name="indicatorLeft" value="2"></enum>
            <!-- 只有外部图形指示器居右 -->
            <enum name="indicatorRight" value="3"></enum>
            <!-- 只有外部数字指示器 -->
            <enum name="numIndicator" value="4"></enum>
            <!-- 只有标题栏 -->
            <enum name="title" value="5"></enum>
            <!-- 标题栏及外部数字指示器 -->
            <enum name="titleNumIndicator" value="6"></enum>
            <!-- 标题栏及图形数字指示器居中 -->
            <enum name="titleIndicatorCenter" value="7"></enum>
            <!-- 标题栏及图形数字指示器居左 -->
            <enum name="titleIndicatorLeft" value="8"></enum>
            <!-- 标题栏及图形数字指示器居右 -->
            <enum name="titleIndicatorRight" value="9"></enum>
            <!-- 标题栏及内部图形数字指示器 -->
            <enum name="titleInIndicator" value="10"></enum>
        </attr>

        <attr name="scaleType" format="enum">
            <enum name="center" value="0" />
            <enum name="center_crop" value="1" />
            <enum name="center_inside" value="2" />
            <enum name="fit_center" value="3" />
            <enum name="fit_end" value="4" />
            <enum name="fit_start" value="5" />
            <enum name="fit_xy" value="6" />
            <enum name="matrix" value="7" />
        </attr>

```
