package com.uguke.code.banner.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.uguke.code.banner.bean.BannerValue;
import com.uguke.code.banner.loader.SimpleImageLoader;

/**
 * 功能描述：轮播图图片适配器
 * @author LeiJue
 * @time 2017/08/25
 */
public class ImageBannerAdapter extends BannerAdapter {

    //缩放样式
    private ImageView.ScaleType scaleType;
    //图片加载器
    private SimpleImageLoader imageLoader;
    //点击事件
    private OnItemClickListener listener;

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent) {

        ImageView image = null;

        if (imageLoader != null) {
            image = imageLoader.onCreateView(parent.getContext());
            ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT);
            image.setLayoutParams(params);
        }

        return new ViewHolder(image);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        if (holder.itemView == null)
            return;
        if (imageLoader == null)
            return;
        ImageView image = (ImageView) holder.itemView;
        image.setScaleType(scaleType);
        final BannerValue value = (BannerValue) bannerValues.get(position);
        imageLoader.loadImage(holder.itemView.getContext(), value.getUri(), image);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    if (getRealCount() != getCount()) {
                        listener.onItemClick(position - 1, position, value);
                    } else {
                        listener.onItemClick(position, position, value);
                    }
                }
            }
        });
    }

    public void setScaleType(ImageView.ScaleType scaleType) {
        this.scaleType = scaleType;
    }

    public void setImageLoader(SimpleImageLoader imageLoader) {
        this.imageLoader = imageLoader;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public interface OnItemClickListener {

        /**
         * 功能描述：点击响应事件
         * @param realPosition 真实位置
         * @param itemPosition Item位置
         * @param value 数据
         */
        void onItemClick(int realPosition, int itemPosition, BannerValue value);
    }
}
