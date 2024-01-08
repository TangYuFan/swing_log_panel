package org.util;


import org.pmw.tinylog.Configurator;
import org.pmw.tinylog.writers.ConsoleWriter;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.Arrays;

/**
*   @desc : 日志工具类
*   @auth : tyf
*   @date : 2023-09-21  11:33:50
*/
public class LogUtil {


    // 当前日志文件
    public static String log_file = null;


    /**
    *   @desc : 创建日志目录
    *   @auth : tyf
    *   @date : 2023-09-21  11:43:49
    */
    public static void createLogDirectory(String directory){
        System.out.println("日志文件目录:"+directory);
        File d = new File(directory);
        if(!d.exists()){
            d.mkdir();
        }
    }

    /**
    *   @desc : 创建日志文件
    *   @auth : tyf
    *   @date : 2023-09-21  11:34:16
    */
    public static void createLog(){
        System.out.println("日志文件路径:"+log_file);
        File log = new File(log_file);
        if(log.exists()){
            log.delete();
        }
        // 创建文件
        try {
            log.createNewFile();
        }
        catch (Exception e){
            e.printStackTrace();
            System.out.println("日志文件初始化失败");
            System.exit(0);
        }

        // 自动将 logging 输出到日志文件
        try {
            Configurator
                    .currentConfig()
                    .formatPattern("{date:yyyy-MM-dd HH:mm:ss} [{class}] {level}: {message}")
                    // writer 子类有文件、滚动文件、jdbc、等等
                    .writer(new org.pmw.tinylog.writers.FileWriter(log_file))
                    .addWriter(new ConsoleWriter(System.out))
                    .activate();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }


    /**
    *   @desc : 默认清除三天以外的日志文件
    *   @auth : tyf
    *   @date : 2023-09-22  14:03:00
    */
    public static void clearLog(String directory){

        // 仅保留3天
        int old = 5;
        long limit = System.currentTimeMillis() - old * 24 * 60 * 60 * 1000;

        File dir = new File(directory);
        if(dir.exists()&&dir.isDirectory()){
            File[] files = dir.listFiles();
            Arrays.stream(files).forEach(n->{
                // 转为时间戳
                String name = n.getName().replace(".log","");
                long t = TimeTools.timeStrToTimeStemp(name,11);
                // old 之前的日志文件进行清除
                if(t<=limit){
                    n.delete();
                }
            });
        }

    }

}
