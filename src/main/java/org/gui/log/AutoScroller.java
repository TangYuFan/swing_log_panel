package org.gui.log;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.plaf.basic.BasicScrollBarUI;

/**
*   @desc : 封装的自动垂直更新面板,修改了滚动条样式
*   @auth : tyf
*   @date : 2023-09-22  14:21:28
*/
public class AutoScroller implements AdjustmentListener {

    private JScrollBar scrollBar;
    private boolean adjustScrollBar = true;

    private int previousValue = -1;
    private int previousMaximum = -1;

    public AutoScroller(JScrollPane scrollPane) {

        scrollBar = scrollPane.getVerticalScrollBar();

        // 修改垂直滚动条样式
        scrollPane.getVerticalScrollBar().setUI(new BasicScrollBarUI(){
            @Override
            protected void configureScrollBarColors() {
                // 这里配置各个颜色
                super.configureScrollBarColors();
            }
            @Override
            public Dimension getPreferredSize(JComponent c) {
                // 设置
                c.setPreferredSize(new Dimension(10, 20));
//                c.setBackground(Color.LIGHT_GRAY);
                return super.getPreferredSize(c);
            }
        });

        // 修改水平滚动条样式
        scrollPane.getHorizontalScrollBar().setUI(new BasicScrollBarUI(){
            @Override
            protected void configureScrollBarColors() {
                // 这里配置各个颜色
                super.configureScrollBarColors();
            }
            @Override
            public Dimension getPreferredSize(JComponent c) {
                // 设置
                c.setPreferredSize(new Dimension(20, 10));
//                c.setBackground(Color.LIGHT_GRAY);
                return super.getPreferredSize(c);
            }
        });


        scrollBar.addAdjustmentListener(this);
    }

    @Override
    public void adjustmentValueChanged(final AdjustmentEvent e) {
        SwingUtilities.invokeLater(() -> checkScrollBar(e));
    }

    /**
     * Scroll down to the bottom of the list
     */
    public void scrollDown() {
        scrollBar.setValue(scrollBar.getModel().getMaximum());
    }

    /*
     *  Analyze every adjustment event to determine when the viewport
     *  needs to be repositioned.
     */
    private void checkScrollBar(AdjustmentEvent e) {

        //  The scroll bar listModel contains information needed to determine
        //  whether the viewport should be repositioned or not.
        JScrollBar scrollBar = (JScrollBar) e.getSource();
        BoundedRangeModel listModel = scrollBar.getModel();
        int value = listModel.getValue();
        int extent = listModel.getExtent();
        int maximum = listModel.getMaximum();

        boolean valueChanged = previousValue != value;
        boolean maximumChanged = previousMaximum != maximum;

        //  Check if the user has manually repositioned the scrollbar
        if (valueChanged && !maximumChanged) {
            adjustScrollBar = value + extent >= maximum;
        }

        /*
          Reset the "value" so we can reposition the viewport
          and distinguish between a user scroll and a program scroll.
         */
        if (adjustScrollBar) {
            //  Scroll the viewport to the end.
            scrollBar.removeAdjustmentListener(this);
            value = maximum - extent;
            scrollBar.setValue(value);
            scrollBar.addAdjustmentListener(this);
        }

        previousValue = value;
        previousMaximum = maximum;
    }
}