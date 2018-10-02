package com.uguke.code.banner;

/**
 * 功能描述：指示器样式
 * @author LeiJue
 * @date 2018/9/25
 */
public enum IndicatorType {

    CIRCLE(0, ""),
    SQUARE(0, ""),
    LINE(0, ""),
    STAR(0, ""),
    TRIANGLE(0, ""),
    RING(1, ""),
    EMPTY_SQUARE(1, ""),
    EMPTY_START(2, ""),
    EMPTY_TRIANGLE(3, "");
    
    int number;
    String name;
    IndicatorType(int number, String name) {
        this.number = number;
        this.name = name;
    }

    public int getNumber() {
        return number;
    }

    public String getName() {
        return name;
    }

    public static IndicatorType get(int number) {
        for (IndicatorType item : IndicatorType.values()) {
            if (number == item.number) {
                return item;
            }
        }
        return CIRCLE;
    }
}
