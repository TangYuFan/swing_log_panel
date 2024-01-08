package org.gui.log;

import org.pmw.tinylog.LogEntry;

import javax.swing.*;
import java.awt.*;


/**
*   @desc : 定义的日志行 支持修改不同level级别日志的颜色和字体
*   @auth : tyf
*   @date : 2023-09-22  14:23:01
*/
public class LogEntryListCellRenderer extends JTextField implements ListCellRenderer<LogEntry> {

    final Color colorTrace = new Color(64, 128, 64);
    final Color colorDebug = new Color(00, 0x5B, 0xD9);
    final Color colorInfo = new Color(0,0,0);
    final Color colorWarning = new Color(255, 0, 0);
    final Color colorError = new Color(255, 0, 0);
    final Color colorErrorBg = new Color(255, 255, 220);
    
    @Override
    public Component getListCellRendererComponent(JList<? extends LogEntry> list, LogEntry logEntry, int index, boolean isSelected, boolean cellHasFocus) {

        if (logEntry == null) {
            return this;
        }

        this.setText(logEntry.getRenderedLogEntry());
//        this.setFont(new Font("Monospaced", Font.BOLD, 11));// 粗体
        this.setFont(new Font("Monospaced", Font.PLAIN, 11));// 普通
        this.setBorder(null);

        if (isSelected) {
            setBackground(list.getSelectionBackground());
            setForeground(list.getSelectionForeground());
        } else {
            switch(logEntry.getLevel()) {
                case ERROR:
                    setBackground(colorErrorBg);
                    break;
                default:
                    setBackground(list.getBackground());
            }
            switch(logEntry.getLevel()) {
                case TRACE:
                    setForeground(colorTrace);
                    break;
                case DEBUG:
                    setForeground(colorDebug);
                    break;
                case INFO:
                    setForeground(colorInfo);
                    break;
                case WARNING:
                    setForeground(colorWarning);
                    break;
                case ERROR:
                    setForeground(colorError);
                    break;
                default:
                    setForeground(list.getForeground());
                    break;
            }
        }

        return this;
    }
}