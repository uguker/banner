package com.uguke.demo.banner;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;


import com.bumptech.glide.Glide;
import com.uguke.code.banner.Banner;
import com.uguke.code.banner.ImageBannerAdapter;
import com.uguke.code.banner.bean.BannerItem;
import com.uguke.code.banner.bean.IBannerItem;
import com.uguke.code.banner.loader.SimpleImageLoader;


import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    ImageBannerAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Banner banner = findViewById(R.id.banner);
        adapter = new ImageBannerAdapter();
        adapter.setImageLoader(new SimpleImageLoader() {
            @Override
            public void loadImage(Context context, Object path, ImageView imageView) {
                Glide.with(context)
                        .load(path)
                        .placeholder(R.drawable.b2)
                        //.centerCrop()
                        .into(imageView);
            }
        });

        List<IBannerItem> values = new ArrayList<>();
        //设置数据
        BannerItem value = new BannerItem();
        value.setTitle("美女");
        value.setHint("001");
        value.setUri(R.drawable.b2);
        values.add(value);

        value = new BannerItem();
        value.setTitle("呵呵");
        value.setHint("002");
        value.setUri(R.drawable.bb);
        values.add(value);

        //绑定数据
        banner.setAdapter(adapter);

        adapter.setItems(values);
        banner.start();

    }
}
