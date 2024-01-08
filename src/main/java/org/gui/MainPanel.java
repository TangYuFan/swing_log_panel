package org.gui;


import org.util.GuiUtil;
import javax.swing.*;
import java.awt.*;

/**
*   @desc : 上位机界面
*   @auth : tyf
*   @date : 2023-09-21  11:18:41
*/
public class MainPanel {


    public static JButton init = new JButton();
    public static JButton start = new JButton();
    public static JButton stop = new JButton();
    public static JButton exit = new JButton();

    /**
    *   @desc : 初始化系统弹窗
    *   @auth : tyf
    *   @date : 2023-09-21  11:18:33
    */
    public static void showWindwos(){

        // 屏幕尺寸
        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        int www = screen.width;
        int hhh = screen.height;
        int w = Double.valueOf(www*0.5).intValue();
        int h = Double.valueOf(hhh*0.5).intValue();

        JFrame frame = new JFrame("生产测试");

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(getPanelContent());
        frame.setResizable(true);
        // 设置窗口大小
        frame.setSize(new Dimension(w,h));
        // 屏幕中心
        frame.setLocation(new Point((screen.width-w)/2,(screen.height-h)/2));
        // 图标
        frame.setIconImage(new ImageIcon(MainPanel.class.getResource("/icon.png")).getImage());
        // 标题栏颜色
        frame.setVisible(true);

    }


    /**
    *   @desc : 主面板
    *   @auth : tyf
    *   @date : 2023-09-21  11:22:04
    */
    public static JSplitPane getPanelContent(){

        JSplitPane panelContent = new JSplitPane();
        panelContent.setDividerSize(0);
        panelContent.setContinuousLayout(true);

        // 左边预留
        panelContent.setLeftComponent(null);

        // 下面是日志
        panelContent.setRightComponent(getRight());

        return panelContent;
    }

    /**
    *   @desc : 左边区域,预留
    *   @auth : tyf
    *   @date : 2023-09-21  15:47:50
    */
    public static JPanel getLeft(){
        JPanel jpanel = new JPanel();

        return jpanel;
    }

    /**
     *   @desc : 底部日志
     *   @auth : tyf
     *   @date : 2023-09-21  15:47:50
     */
    public static JPanel getRight(){

        // 日志面板
        JPanel jpanel = new LogPanel();
        jpanel.setBorder(GuiUtil.createBorder("日志",5,5,5,5));
        jpanel.setBackground(Color.WHITE);

        return jpanel;
    }


}
