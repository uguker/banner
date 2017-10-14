package com.uguke.code.banner.adapter;

import android.support.v4.view.PagerAdapter;

import android.view.View;
import android.view.ViewGroup;

import com.uguke.code.banner.bean.IBannerValue;

import java.util.List;


/**
 * 功能描述：轮播图适配器
 * @author LeiJue
 * @time 2017/08/25
 */
public abstract class BannerAdapter<T extends BannerAdapter.ViewHolder> extends PagerAdapter {

    /** 用于刷新数据 **/
    private int childCount = 0;
    /** 记录真实数量 **/
    private int realCount = 0;
    /** 是否是可循环模式 **/
    private boolean isLoop = true;
    /** 刷新数据监听 **/
    private OnRefreshListener listener;
    protected List<IBannerValue> bannerValues;

    @Override
    public int getCount() {
        //返回轮播图数量
        if (bannerValues == null) {
            return 0;
        }
        return bannerValues.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        ViewHolder holder = onCreateViewHolder(container);
        onBindViewHolder((T) holder, position);
        container.addView(holder.itemView);
        return holder.itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        View view = (View) object;
        container.removeView(view);
    }

    @Override
    public int getItemPosition(Object object)   {
        if (childCount > 0) {
            childCount --;
            return POSITION_NONE;
        }
        return super.getItemPosition(object);
    }

    @Override
    public void notifyDataSetChanged() {
        childCount = getCount();
        if (listener != null) {
            listener.onRefresh();
        }
        super.notifyDataSetChanged();
    }

    /**
     * 功能描述：设置数据源
     * @param data
     */
    public void setData(List<IBannerValue> data) {
        setData(data, isLoop);
    }

    /**
     * 功能描述：设置数据源
     * @param data
     */
    public void setData(List<IBannerValue> data, boolean isLoop) {
        bannerValues = data;
        if (data != null && data.size() > 0) {
            this.isLoop = isLoop;
            realCount = data.size();
            if (isLoop) {
                bannerValues.add(0, data.get(realCount - 1));
                bannerValues.add(data.get(1));
            }
        }
        notifyDataSetChanged();
    }

    /**
     * 功能描述：设置是否可循环
     * @param isLoop
     */
    public void setLoop(boolean isLoop) {
        if (bannerValues == null)
            return;
        if (this.isLoop != isLoop) {
            this.isLoop = isLoop;
            if (!this.isLoop) {
                if (bannerValues.size() > 2) {
                    bannerValues.remove(bannerValues.size() - 1);
                    bannerValues.remove(0);
                }
            } else {
                if (bannerValues.size() > 0) {
                    bannerValues.add(0, bannerValues.get(
                            bannerValues.size() - 1));
                    bannerValues.add(bannerValues.get(1));
                }
            }
            notifyDataSetChanged();
        }
    }

    /**
     * 功能描述：是否可循环
     * @return
     */
    public boolean isLoop() {
        return isLoop;
    }

    /**
     * 功能描述：获取数据源
     * @return
     */
    public List<IBannerValue> getData() {
        return bannerValues;
    }
    /**
     * 功能描述：获取真实数量
     * @return
     */
    public int getRealCount() {
        return realCount;
    }

    /**
     * 设置刷新
     * @param listener
     */
    public void setOnRefreshListener(OnRefreshListener listener) {
        this.listener = listener;
    }

    /**
     * 功能描述：绑定ViewHolder
     * @param parent 父容器
     */
    public abstract T onCreateViewHolder(ViewGroup parent);

    /**
     * 功能描述：绑定ViewHolder中的View
     * @param holder
     * @param position 位置
     */
    public abstract void onBindViewHolder(T holder, int position);

    public static class ViewHolder {

        public View itemView;

        public ViewHolder (View itemView){
            this.itemView = itemView;
        }
    }

    public interface OnRefreshListener {
        void onRefresh();
    }

}
