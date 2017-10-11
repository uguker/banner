package com.uguke.code.banner.bean;

public interface IBannerValue<T> {

    void setUri(T uri);
    void setHint(String hint);
    void setTitle(String title);

    T getUri();
    String getHint();
    String getTitle();
}
