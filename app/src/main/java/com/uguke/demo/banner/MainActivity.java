package com.uguke.demo.banner;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;


import com.bumptech.glide.Glide;
import com.uguke.code.banner.Banner;
import com.uguke.code.banner.adapter.ImageBannerAdapter;
import com.uguke.code.banner.bean.BannerValue;
import com.uguke.code.banner.bean.IBannerValue;
import com.uguke.code.banner.loader.SimpleImageLoader;
import com.uguke.code.banner.transformer.BTFTransformer;


import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    ImageBannerAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
            public void onItemClick(int realPosition, int itemPosition, IBannerValue value) {
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

    }
}
