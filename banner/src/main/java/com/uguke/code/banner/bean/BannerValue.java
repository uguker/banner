package com.uguke.code.banner.bean;

/**
 * 轮播图值
 */
public class BannerValue implements IBannerValue<Object> {
    /** 资源 **/
    private Object uri;
    /** 提示 **/
    private String hint;
    /** 标题 **/
    private String title;

    public Object getUri() {
        return uri;
    }

    public void setUri(Object uri) {
        this.uri = uri;
    }

    public String getHint() {
        return hint;
    }

    public void setHint(String hint) {
        this.hint = hint;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
