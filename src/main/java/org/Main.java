package org;

import org.apache.commons.io.FileUtils;
import org.gui.MainPanel;
import org.pmw.tinylog.Logger;
import org.util.*;

import java.io.File;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;


/**
*   @desc : 上位机演示软件
*   @auth : tyf
*   @date : 2023-07-21  15:35:42
*/
public class Main {

    // 系统ui初始化标价
    private static AtomicBoolean ui_success = new AtomicBoolean(false);

    // 工作目录dll路径
    public static String dll_file = "ControlCAN.dll";
    public static String user_home_dll = "C:\\ucchip\\dll\\";

    // 工作目录日志路径
    public static String user_home_log = "C:\\ucchip\\log\\";

    // 数据库
    public static String user_home_data = "C:\\ucchip\\data\\";

    // 工作目录下配置路径
    public static String user_home_conf = "C:\\ucchip\\conf\\";


    public static void main(String[] args){
        // 初始化系统工具和工作目录
        initUtil();
        // UI初始化
        MainPanel.showWindwos();
    }


    /**
    *   @desc : 初始化系统工具和工作目录
    *   @auth : tyf
    *   @date : 2023-09-22  15:54:12
    */
    public static void initUtil(){

        // 校验dll,不存在则进行初始化
        DllUtil.dllInit(user_home_dll,dll_file);

        // 初始化日志文件,清除3天以外的日志文件
        LogUtil.log_file = user_home_log + TimeTools.timeStempToTimeStr(System.currentTimeMillis(),11) +".log";
        LogUtil.createLogDirectory(user_home_log);
        LogUtil.clearLog(user_home_log);
        LogUtil.createLog();

        // 初始化本地数据库,按月生成
        SqliteUtil.db_file = user_home_data + TimeTools.timeStempToTimeStr(System.currentTimeMillis(),6) +".db";
        SqliteUtil.createDbDirectory(user_home_data);
        SqliteUtil.dbInit();

        // 初始化配置文件,如果没有则进行配置文件初始化
        ConfigUtil.conf_file = user_home_conf + "conf.txt";
        ConfigUtil.createConfigDirectory(user_home_conf);
        ConfigUtil.initConfig();


        ui_success.set(true);
        Logger.info("系统初始化成功!");

        new Thread(()->{
            try {
                while (true){
                    Logger.info("refreshLogIfOnTop 日志: "+ UUID.randomUUID());
                    Logger.error("refreshLogIfOnTop 日志: "+ UUID.randomUUID());
                    Logger.debug("refreshLogIfOnTop 日志: "+ UUID.randomUUID());
                    Thread.sleep(1000);
                }
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }).start();

    }

}