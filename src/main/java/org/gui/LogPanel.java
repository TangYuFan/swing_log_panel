package org.gui;


import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.prefs.Preferences;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import org.Main;
import org.gui.log.AutoScroller;
import org.gui.log.LogEntryListCellRenderer;
import org.gui.log.LogEntryListModel;
import org.pmw.tinylog.Configurator;
import org.pmw.tinylog.Level;
import org.pmw.tinylog.LogEntry;
import org.util.GuiUtil;
import org.util.Icons;



/**
*   @desc : 日志面板
*   @auth : tyf
*   @date : 2023-09-22  14:19:02
*/
public class LogPanel extends JPanel {

    // 一个通用的文字字体
    public static Font textFont = new Font("Arial", Font.PLAIN, 11);

    private Preferences prefs = Preferences.userNodeForPackage(LogPanel.class);

    private static final String PREF_LOG_LEVEL = "LogPanel.logLevel";
    private static final String PREF_LOG_LEVEL_DEF = Level.INFO.toString();

    private LogEntryListModel logEntries = new LogEntryListModel();
    private JList<LogEntry> logEntryJList = new JList<>(logEntries);

    private Level filterLogLevel = Level.TRACE;
    private LogEntryListModel.LogEntryFilter logLevelFilter = new LogEntryListModel.LogEntryFilter();
    private LogEntryListModel.LogEntryFilter searchBarFilter = new LogEntryListModel.LogEntryFilter();
    private LogEntryListModel.LogEntryFilter systemOutFilter = new LogEntryListModel.LogEntryFilter();

    private ScheduledExecutorService scheduledExecutor;

    public LogPanel() {

        loadLoggingPreferences();

        logEntries.addFilter(logLevelFilter);
        logEntries.addFilter(searchBarFilter);
        logEntries.addFilter(systemOutFilter);

        systemOutFilter.setFilter(logEntry -> true);

        setLayout(new BorderLayout(0, 0));

        JScrollPane scrollPane = new JScrollPane(logEntryJList);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        add(scrollPane, BorderLayout.CENTER);

        // 设置滚动条颜色和宽度

        JScrollBar verticalBar = scrollPane.getVerticalScrollBar();
        AutoScroller autoScroller = new AutoScroller(scrollPane);
        verticalBar.addAdjustmentListener(autoScroller);

        JPanel settingsAndFilterPanel = new JPanel();
        settingsAndFilterPanel.setBackground(Color.WHITE);
        settingsAndFilterPanel.setLayout(new BorderLayout(0, 0));
        add(settingsAndFilterPanel, BorderLayout.NORTH);

        // The filter settings
        JPanel filterPanel = new JPanel(new BorderLayout(0, 0));
        filterPanel.setBackground(Color.WHITE);


        JPanel filterContentPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        filterContentPanel.setBackground(Color.WHITE);


        // 按钮
        JPanel buttonArea = new JPanel();
        buttonArea.setLayout(GuiUtil.flowLayout_left_3_3);
        buttonArea.setBackground(Color.WHITE);
        buttonArea.setPreferredSize(new Dimension(130,30));
        JToolBar toolBar = new JToolBar();
        toolBar.setBackground(Color.WHITE);
        toolBar.setFloatable(false);
        toolBar.setBorder(null);
        MainPanel.init.setIcon(Icons.home);
        MainPanel.start.setIcon(Icons.start);
        MainPanel.stop.setIcon(Icons.pause);
        MainPanel.exit.setIcon(Icons.powerOff);
        MainPanel.init.setBorder(null);
        MainPanel.init.setBackground(Color.WHITE);
        MainPanel.start.setBorder(null);
        MainPanel.start.setBackground(Color.WHITE);
        MainPanel.stop.setBorder(null);
        MainPanel.stop.setBackground(Color.WHITE);
        MainPanel.exit.setBorder(null);
        MainPanel.exit.setBackground(Color.WHITE);
        toolBar.add(MainPanel.init);// 初始化
        toolBar.add(MainPanel.start);// 开始
        toolBar.add(MainPanel.stop);// 暂停
        toolBar.add(MainPanel.exit);// 退出
        buttonArea.add(toolBar);
        filterContentPanel.add(buttonArea);



        filterContentPanel.add(createSearchFieldPanel());
        filterContentPanel.add(createFilterLogLevelPanel());


        JButton openDir = new JButton();
        openDir.setIcon(Icons.wenjianjia);
        openDir.setBackground(Color.white);
        openDir.setBorder(null);

        openDir.addActionListener(e->{
            File dir = new File(Main.user_home_log);
            // 打开资源管理器
            try {
                Desktop.getDesktop().open(dir);
            }
            catch (Exception oe){
                oe.printStackTrace();
            }
        });

        filterContentPanel.add(openDir);

        filterPanel.add(filterContentPanel, BorderLayout.WEST);

        JPanel filterControlPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        filterControlPanel.setBackground(Color.WHITE);

        JButton btnClear = new JButton(Icons.delete);
        btnClear.setToolTipText("clear");
        btnClear.setBorder(null);
        btnClear.setBackground(Color.WHITE);

        btnClear.addActionListener(e -> logEntries.clear());

        filterControlPanel.add(btnClear);


        JButton btnScroll = new JButton(Icons.scrollDown);
        btnScroll.setToolTipText("buttom");
        btnScroll.setBackground(Color.WHITE);
        btnScroll.setBorder(null);

        btnScroll.addActionListener(e -> {
            autoScroller.scrollDown();
        });

        filterControlPanel.add(btnScroll);

        filterPanel.add(filterControlPanel, BorderLayout.CENTER);

        settingsAndFilterPanel.add(filterPanel);

        // Log Panel
        logEntryJList.setCellRenderer(new LogEntryListCellRenderer());

        // 支持 ctrl-c 复制日支行
        logEntryJList.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_C, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()), "log-copy");
        logEntryJList.getActionMap().put("log-copy", new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                ArrayList<LogEntry> logList = (ArrayList<LogEntry>) logEntryJList.getSelectedValuesList();
                StringBuilder sb = new StringBuilder();
                logList.forEach(logEntry -> sb.append(logEntry.getRenderedLogEntry()));
                copyStringToClipboard(sb.toString());
            }
        });


        scheduledExecutor = Executors.newSingleThreadScheduledExecutor();
        scheduledExecutor.scheduleAtFixedRate(() -> {
            // 定时刷新日志,某些情况日志界面被隐藏时就不用刷新,那么在这里判断一下日志界面是否被选中
            // getSelectedComponent()==LogPanel.this
            refreshLogIfOnTop();
        }, 0, 500, TimeUnit.MILLISECONDS);


    }

    private void loadLoggingPreferences() {

        // 设置为最低级别,不然低于这个级别的不打印
        Level level = Level.TRACE;
//        try {
//            level = Level.valueOf(prefs.get(PREF_LOG_LEVEL, PREF_LOG_LEVEL_DEF));
//        } catch (Exception ignored) {
//        }
//        if (level == null) {
//            level = Level.INFO;
//        }

        Configurator
                .currentConfig()
                .formatPattern("{date:yyyy-MM-dd HH:mm:ss} [{class}] {level}: {message}")
                .level(level)
                .activate();

        Configurator
                .currentConfig()
                .formatPattern("{date:yyyy-MM-dd HH:mm:ss} [{class}] {level}: {message}")
                .addWriter(logEntries)
                .activate();
    }

    private JPanel createSearchFieldPanel() {

        JPanel searchField = new JPanel();
        searchField.setBackground(Color.WHITE);
        JLabel lblSearch = new JLabel("Search");
        lblSearch.setFont(textFont);
        lblSearch.setPreferredSize(new Dimension(36,30));
        searchField.add(lblSearch);

        // 日志搜索框
        JTextField searchTextField = new JTextField();
        searchTextField.getDocument().addDocumentListener(new DocumentListener() {

            private void updateSearchBarFilter() {
                LogEntry entry = getSelectedEntry();
                String searchText = searchTextField.getText();
                searchBarFilter.setFilter((logEntry -> logEntry.getRenderedLogEntry().toLowerCase().contains(searchText.toLowerCase())));
                logEntries.filter();
                setSelectedEntry(entry);
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                updateSearchBarFilter();
            }

            @Override
            public void insertUpdate(DocumentEvent e) {
                updateSearchBarFilter();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                updateSearchBarFilter();
            }
        });

        searchTextField.setColumns(10);
        searchField.add(searchTextField);
        return searchField;
    }

    private JPanel createFilterLogLevelPanel() {

        JPanel filterLogLevelPanel = new JPanel();
        filterLogLevelPanel.setBackground(Color.WHITE);

        JLabel levelLabel = new JLabel("Level");
        levelLabel.setFont(textFont);
        filterLogLevelPanel.add(levelLabel);

        // 下拉选日志级别
        JComboBox logLevelFilterComboBox = new JComboBox(Level.values());
        logLevelFilterComboBox.setPreferredSize(new Dimension(80,20));
        logLevelFilterComboBox.setFont(textFont);
        logLevelFilterComboBox.setBackground(Color.WHITE);
        logLevelFilterComboBox.setSelectedItem(filterLogLevel);
        logLevelFilterComboBox.addActionListener(e -> {
            LogEntry entry = getSelectedEntry();
            Level logLevel = (Level) logLevelFilterComboBox.getSelectedItem();
            // 设置日志过滤级别
            logLevelFilter.setFilter(logEntry -> logEntry.getLevel().compareTo(logLevel) >= 0);
            logEntries.filter();
            setSelectedEntry(entry);
        });
        filterLogLevelPanel.add(logLevelFilterComboBox);
        return filterLogLevelPanel;
    }

    private void copyStringToClipboard(String s) {
        StringSelection selection = new StringSelection(s);
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        clipboard.setContents(selection, selection);
    }

    protected void refreshLogIfOnTop() {
        if (logEntries.isRefreshNeeded()) {
            logEntries.refresh();
        }
    }

    protected LogEntry getSelectedEntry() {
        int index = logEntryJList.getSelectedIndex();
        if (index >= 0) {
            return logEntries.getElementAt(index);
        }
        return null;
    }

    protected void setSelectedEntry(LogEntry entry) {
        if (entry != null) {
            int index = logEntries.getFilteredLogEntries().indexOf(entry);
            if (index >= 0) {
                logEntryJList.setSelectedIndex(index);
            }
        }
    }

}
