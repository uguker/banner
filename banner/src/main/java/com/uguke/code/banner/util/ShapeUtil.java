package com.uguke.code.banner.util;

import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.PathShape;

/**
 * 图形工具类
 */
public class ShapeUtil {

    private ShapeUtil() {

    }

    /**
     * 五角星形状
     * @param width 宽度
     * @param height 高度
     * @param color 颜色
     * @param isFill 是否填充
     * @return
     */
    public static ShapeDrawable starShape(int width, int height, int color, boolean isFill) {
        //创建图形对象
        ShapeDrawable shape = new ShapeDrawable();
        //设置颜色
        shape.getPaint().setColor(color);
        //创建路径
        Path path = new Path();
        //五角星外五个点离圆心的半径
        int outRadius = (int) (Math.max(width, height) / 2 / Math.sqrt(1.6));
        //五角星内五个点离圆心的半径
        int inRadius = (int) (Math.max(width, height) / Math.sqrt(2) / 5);
        //线条粗细
        int stroke = Math.max(width, height) / 10;

        if (!isFill) {
            shape.getPaint().setStrokeWidth(stroke);
            shape.getPaint().setStyle(Paint.Style.STROKE);
            outRadius = outRadius - stroke;
        } else {
            inRadius = inRadius + stroke / 2;
        }
        //弧线
        double radians = Math.toRadians(-90);
        //画五角星的路径
        path.moveTo(
                (float)(width / 2 + outRadius * Math.cos(radians)),
                (float)(height / 2 + outRadius * Math.sin(radians)));
        radians = Math.toRadians(-54);
        path.lineTo(
                (float)(width / 2 + inRadius * Math.cos(radians)),
                (float)(height / 2 + inRadius * Math.sin(radians)));
        for(int i = 0; i < 4; i++) {
            radians = Math.toRadians(72 * i - 18);
            int outX = (int) (width / 2 + outRadius * Math.cos(radians));
            int outY = (int) (height / 2 + outRadius * Math.sin(radians));
            path.lineTo(outX, outY);
            radians = Math.toRadians(72 * i + 18);
            int inX = (int) (width / 2  + inRadius * Math.cos(radians));
            int inY = (int) (height / 2 + inRadius * Math.sin(radians));
            path.lineTo(inX, inY);
        }
        path.close();
        //将路径设置到图形中
        shape.setShape(new PathShape(path, width, height));
        return shape;
    }

    /**
     * 直线形状
     * @param width 宽度
     * @param height 高度
     * @param color 颜色
     * @return
     */
    public static ShapeDrawable lineShape(int width, int height, int color) {
        //创建图形对象
        ShapeDrawable shape = new ShapeDrawable();
        //设置颜色
        shape.getPaint().setColor(color);
        shape.getPaint().setStyle(Paint.Style.STROKE);
        //创建路径
        Path path = new Path();
        //圆内切正方形边长
        int length = (int) (Math.max(width, height) / Math.sqrt(2));
        //线条粗细
        int stroke = Math.max(width, height) / 10;
        shape.getPaint().setStrokeWidth(stroke);
        //画五角星的路径
        path.moveTo(width / 2 - length / 2, height / 2);
        path.lineTo(width / 2 + length / 2, height / 2);
        //将路径设置到图形中
        shape.setShape(new PathShape(path, width, height));
        return shape;
    }

    /**
     * 三角形形状
     * @param width 宽度
     * @param height 高度
     * @param color 颜色
     * @param isFill 是否填充
     * @return
     */
    public static ShapeDrawable triangleShape(int width, int height, int color, boolean isFill) {
        //创建图形对象
        ShapeDrawable shape = new ShapeDrawable();
        //设置颜色
        shape.getPaint().setColor(color);
        //创建路径
        Path path = new Path();
        //圆内切正方形边长
        int length = (int) (Math.max(width, height) / Math.sqrt(2));
        //线条粗细
        int stroke = Math.max(width, height) / 10;

        if (!isFill) {
            shape.getPaint().setStrokeWidth(stroke);
            shape.getPaint().setStyle(Paint.Style.STROKE);
        }
        //画五角星的路径
        path.moveTo(
                (float)(width / 2),
                (float)(height / 2 - length / 2 + stroke));
        path.lineTo(
                (float)(width / 2 + length / 2 - stroke / 2),
                (float)(height / 2 + length / 2 - stroke / 2));
        path.lineTo(
                (float)(width / 2 - length / 2 + stroke / 2),
                (float)(height / 2 + length / 2 - stroke / 2));
        path.close();
        //将路径设置到图形中
        shape.setShape(new PathShape(path, width, height));
        return shape;
    }

    /**
     * 正方形形状
     * @param width 宽度
     * @param height 高度
     * @param color 颜色
     * @param isFill 是否填充
     * @return
     */
    public static ShapeDrawable squareShape(int width, int height, int color, boolean isFill) {
        //创建图形对象
        ShapeDrawable shape = new ShapeDrawable();
        //设置颜色
        shape.getPaint().setColor(color);
        //创建路径
        Path path = new Path();
        //四个点离圆心的半径
        int outRadius = Math.max(width, height) / 2;
        //线条粗细
        int stroke = Math.max(width, height) / 10;

        if (!isFill) {
            shape.getPaint().setStrokeWidth(stroke);
            shape.getPaint().setStyle(Paint.Style.STROKE);
            outRadius = outRadius - stroke;
        }
        //弧线
        double radians = Math.toRadians(-135);
        //画五角星的路径
        path.moveTo(
                (float)(width / 2 + outRadius * Math.cos(radians)),
                (float)(height / 2 + outRadius * Math.sin(radians)));
        for(int i = 0; i < 3; i++) {
            radians = Math.toRadians(90 * i - 45);
            int outX = (int) (width / 2 + outRadius * Math.cos(radians));
            int outY = (int) (height / 2 + outRadius * Math.sin(radians));
            path.lineTo(outX, outY);

        }
        path.close();
        //将路径设置到图形中
        shape.setShape(new PathShape(path, width, height));
        return shape;
    }

    /**
     * 圆形形状
     * @param width 宽度
     * @param height 高度
     * @param color 颜色
     * @param isFill 是否填充
     * @return
     */
    public static ShapeDrawable circleShape(int width, int height, int color, boolean isFill) {
        //创建图形对象
        ShapeDrawable shape = new ShapeDrawable();
        //设置颜色
        shape.getPaint().setColor(color);
        shape.getPaint().setAntiAlias(true);

        int radius = (int) (Math.min(width, height) / 2 / Math.sqrt(2));
        //线条粗细
        int stroke = Math.max(width, height) / 10;
        Path path = new Path();
        if (!isFill) {
            shape.getPaint().setStrokeWidth(stroke);
            shape.getPaint().setStyle(Paint.Style.STROKE);
            path.addCircle(width / 2, height / 2 , radius - stroke / 2, Path.Direction.CW);
        } else {
            path.addCircle(width / 2, height / 2 , radius, Path.Direction.CW);
        }
        shape.setShape(new PathShape(path, width, height));
        //将路径设置到图形中
        return shape;
    }

}
