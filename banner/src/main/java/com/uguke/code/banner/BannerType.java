package com.uguke.code.banner;

/**
 * 功能描述：
 * @author LeiJue
 * @date 2018/9/25
 */
public enum BannerType {

    NONE(0, "不显示任何附加信息"),
    LEFT(1, "只显示图形指示器居左"),
    RIGHT(2, "只显示图形指示器居右"),
    CENTER(3, "只显示图形指示器居中"),
    NUMBER(4, "只显示数字指示器"),
    TITLE_ONLY(5, "只显示标题栏"),
    TITLE_NUMBER(6, "显示标题栏及内部数字指示器"),
    TITLE_INNER(7, "显示标题栏及内部图形指示器"),
    TITLE_LEFT(8, "显示标题栏及外部图形指示器居左"),
    TITLE_RIGHT(9, "显示标题栏及外部图形指示器居右"),
    TITLE_CENTER(10, "显示标题栏及外部图形指示器居中");
    int number;
    String name;
    BannerType(int number, String name) {
        this.number = number;
        this.name = name;
    }

    public int getNumber() {
        return number;
    }

    public String getName() {
        return name;
    }

    public static BannerType get(int number) {
        for (BannerType item : BannerType.values()) {
            if (number == item.number) {
                return item;
            }
        }
        return NONE;
    }
}
