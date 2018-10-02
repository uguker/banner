package com.uguke.code.banner.bean;

/**
 * 功能描述：轮播图数据项
 * @author LeiJue
 * @date 2018/9/25
 */
public interface IBannerItem<T> {

    void setUri(T uri);
    void setHint(String hint);
    void setTitle(String title);

    T getUri();
    String getHint();
    String getTitle();
}
