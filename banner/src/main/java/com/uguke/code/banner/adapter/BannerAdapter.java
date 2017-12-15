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
    protected List<IBannerValue> items;

    @Override
    public int getCount() {
        return items == null ? 0 : items.size();
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
     * @param items
     */
    public void setItems(List<IBannerValue> items) {
        setItems(items, isLoop);
    }

    /**
     * 功能描述：设置数据源
     * @param items
     */
    public void setItems(List<IBannerValue> items, boolean isLoop) {
        this.items = items;
        if (items != null && items.size() > 0) {
            this.isLoop = isLoop;
            realCount = items.size();
            if (isLoop) {
                this.items.add(0, items.get(realCount - 1));
                this.items.add(items.get(1));
            }
        }
        notifyDataSetChanged();
    }

    /**
     * 功能描述：设置是否可循环
     * @param isLoop
     */
    public void setLoop(boolean isLoop) {
        if (items == null)
            return;
        if (this.isLoop != isLoop) {
            this.isLoop = isLoop;
            if (!this.isLoop) {
                if (items.size() > 2) {
                    items.remove(items.size() - 1);
                    items.remove(0);
                }
            } else {
                if (items.size() > 0) {
                    items.add(0, items.get(
                            items.size() - 1));
                    items.add(items.get(1));
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
    public List<IBannerValue> getItems() {
        return items;
    }

    /**
     * 功能描述：获取数据源
     * @return
     */
    public IBannerValue getItem(int position) {
        if (getCount() == 0)
            return null;
        if (position < 0 || position >= getCount())
            return null;
        return items.get(position);
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
