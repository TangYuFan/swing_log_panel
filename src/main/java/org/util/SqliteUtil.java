package org.util;


import org.pmw.tinylog.Logger;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

/**
*   @desc : 本地数据库
*   @auth : tyf
*   @date : 2023-09-21  18:20:19
*/
public class SqliteUtil {

    // 当前日志文件,按月生成
    public static String db_file = null;

    // 数据库链接
    public static Connection connection = null;
    public static Statement statement = null;

    // 系统初始化数据表
    public static Map<String,String> tables = new HashMap();

    static {
        try {
            Class.forName("org.sqlite.JDBC");

            // 需要进行初始化的表(表名称,建表语句)

            // 系统表
            tables.put("sys_params","CREATE TABLE sys_params(id INTEGER PRIMARY KEY, key TEXT, value TEXT);");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     *   @desc : 创建数据库目录
     *   @auth : tyf
     *   @date : 2023-09-21  11:43:49
     */
    public static void createDbDirectory(String directory){
        System.out.println("数据库文件目录:"+directory);
        File d = new File(directory);
        if(!d.exists()){
            d.mkdir();
        }
    }


    /**
    *   @desc : 创建数据库文件,并进行表结构初始化
    *   @auth : tyf
    *   @date : 2023-09-22  15:48:52
    */
    public static void dbInit(){

        String url = "jdbc:sqlite:"+db_file;
        System.out.println("数据库文件路径:"+url);
        try {
            connection = DriverManager.getConnection(url);
            statement = connection.createStatement();
            Logger.info("数据库连接成功,数据表校验和初始化!");
            // 判断是否有数据表
            checkAndInitTable();
        }
        catch (Exception e){
            e.printStackTrace();
            System.exit(0);
        }

    }


    /**
    *   @desc : 校验数据表是否存在,不存在则进行初始化
    *   @auth : tyf
    *   @date : 2023-09-25  11:20:55
    */
    public static void checkAndInitTable(){

        tables.entrySet().forEach(n->{

            String table = n.getKey();
            String createTable = n.getValue();

            // 先判断表是否存在
            try {
                ResultSet resultSet = statement.executeQuery("SELECT name FROM sqlite_master WHERE type='table' AND name='"+table+"';");
                if (!resultSet.next()) {
                    statement.executeUpdate(createTable);
                    System.out.println("Table '"+table+"' created successfully.");
                } else {
                    System.out.println("Table '"+table+"' already exists.");
                }
            }
            catch (Exception e){
                e.printStackTrace();
                System.exit(0);
            }

        });

    }

}
