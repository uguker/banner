package com.uguke.code.banner;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import com.uguke.code.banner.bean.IBannerItem;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

/**
 * 功能描述：轮播图数据适配器
 * @author LeiJue
 * @date 2018/9/25
 */
public abstract class BannerAdapter<T extends BannerAdapter.ViewHolder> extends PagerAdapter {

    /** 用于刷新数据 **/
    private int childCount = 0;
    /** 记录真实数量 **/
    private int realCount = 0;



    private List<View> tempViews;

    private List<? extends IBannerItem> realItems;
    private List<IBannerItem> tempItems;


    Observable observable = new Observable();

    public BannerAdapter() {
        tempItems = new ArrayList<>();
        tempViews = new ArrayList<>();
    }

    @Override
    public int getCount() {
        return tempItems.size();
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
        tempViews.set(position, holder.itemView);
        return holder.itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        View view = (View) object;
        container.removeView(view);
        tempViews.set(position, null);
    }

    @Override
    public int getItemPosition(Object object)   {
        if (childCount > 0) {
            childCount --;
            return POSITION_NONE;
        }
        return super.getItemPosition(object);
    }

    public View getViewAt(int position) {
        return tempViews.get(position);

    }

    @Override
    public void notifyDataSetChanged() {
        childCount = getCount();
        super.notifyDataSetChanged();
    }

    private void initItems() {
        tempItems.clear();
        tempViews.clear();
        // 为空
        if (realItems != null && realItems.size() > 0) {
            // 其他
            tempItems.addAll(realItems);
            tempItems.add(0, realItems.get(realItems.size() - 1));
            tempItems.add(realItems.get(0));
        }
        for (int i = 0; i < tempItems.size(); i++) {
            tempViews.add(null);
        }
    }

    public void setItems(List<? extends IBannerItem> items) {
        this.realItems = items;
        initItems();
        try {
            Field field = Observable.class.getDeclaredField("changed");
            field.setAccessible(true);
            field.setBoolean(observable, true);
            observable.notifyObservers(tempItems.size());
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        notifyDataSetChanged();
    }

    /**
     * 功能描述：获取数据源
     * @return
     */
    public IBannerItem getItem(int position) {
        if (getCount() == 0)
            return null;
        if (position < 0 || position >= getCount())
            return null;
        return tempItems.get(position);
    }

    int getRealCount() {
        return realCount;
    }

    /**
     * 功能描述：绑定ViewHolder
     * @param parent 父容器
     * @return 控制器
     */
    public abstract T onCreateViewHolder(ViewGroup parent);

    //public abstract void convert(T holder, T item);

    /**
     * 功能描述：绑定ViewHolder中的View
     * @param holder   控制器
     * @param position 位置
     */
    public abstract void onBindViewHolder(T holder, int position);

    public static class ViewHolder {
        public View itemView;
        ViewHolder(View itemView) {
            this.itemView = itemView;
        }
    }

}
