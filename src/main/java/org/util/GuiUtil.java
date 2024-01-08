package org.util;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.MatteBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;


public class GuiUtil {


    public static MatteBorder matteBorder_0_0_1_0 = BorderFactory.createMatteBorder(0,0,1,0,Color.BLACK);
    public static MatteBorder matteBorder_0_0_0_0 = BorderFactory.createMatteBorder(0,0,0,0,Color.BLACK);

    public static FlowLayout flowLayout_left_20_20 = new FlowLayout(FlowLayout.LEFT, 20, 20);

    public static FlowLayout flowLayout_left_5_5 = new FlowLayout(FlowLayout.LEFT, 5, 5);

    public static FlowLayout flowLayout_left_3_3 = new FlowLayout(FlowLayout.LEFT, 3, 3);

    public static FlowLayout flowLayout_right_3_3 = new FlowLayout(FlowLayout.RIGHT, 3, 3);

    public static Border border_20_20_20_20 = BorderFactory.createEmptyBorder(20, 20, 20, 20);
    public static Border border_5_5_5_5 = BorderFactory.createEmptyBorder(5, 5, 5, 5);

    // 创建边框
    public static CompoundBorder createBorder(String name, int top, int left, int bottom, int right){
        TitledBorder titledBorder = new TitledBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY), name, TitledBorder.LEADING, TitledBorder.TOP, null, null);
        Border emptyBorder = BorderFactory.createEmptyBorder(top, left, bottom, right); // 设置上、左、下、右边距为10像素
        return new CompoundBorder(titledBorder, emptyBorder);
    }

    // 创建边框
    public static CompoundBorder createBorder2(String name, int top, int left, int bottom, int right){
        TitledBorder titledBorder = new TitledBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY), name, TitledBorder.TOP, TitledBorder.TOP, null, null);
        Border emptyBorder = BorderFactory.createEmptyBorder(top, left, bottom, right); // 设置上、左、下、右边距为10像素
        return new CompoundBorder(titledBorder, emptyBorder);
    }

    // 创建边框
    public static CompoundBorder createBorder3(String name, int top, int left, int bottom, int right){
        TitledBorder titledBorder = new TitledBorder(BorderFactory.createLineBorder(Color.BLACK), name, TitledBorder.LEADING, TitledBorder.TOP, null, null);
        Border emptyBorder = BorderFactory.createEmptyBorder(top, left, bottom, right); // 设置上、左、下、右边距为10像素
        return new CompoundBorder(titledBorder, emptyBorder);
    }

    // 创建边框
    public static CompoundBorder createBorder4(String name, int top, int left, int bottom, int right){
        TitledBorder titledBorder = new TitledBorder(BorderFactory.createLineBorder(Color.WHITE), name, TitledBorder.TOP, TitledBorder.TOP, null, null);
        Border emptyBorder = BorderFactory.createEmptyBorder(top, left, bottom, right); // 设置上、左、下、右边距为10像素
        return new CompoundBorder(titledBorder, emptyBorder);
    }

}
