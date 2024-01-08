package org.util;


import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.LinkedHashMap;

/**
*   @desc : 系统运行配置工具类
*   @auth : tyf
*   @date : 2023-09-21  11:50:37
*/
public class ConfigUtil {

    // 当前配置文件路径
    public static String conf_file = null;


    /**
     *   @desc : 创建配置目录
     *   @auth : tyf
     *   @date : 2023-09-21  11:43:49
     */
    public static void createConfigDirectory(String directory){
        System.out.println("配置文件目录:"+directory);
        File d = new File(directory);
        if(!d.exists()){
            d.mkdir();
        }
    }

    /**
     *   @desc : 初始化配置文件,不存在则初始化,写入模板配置
     *   @auth : tyf
     *   @date : 2023-09-21  11:34:16
     */
    public static void initConfig(){

        System.out.println("配置文件路径:"+conf_file);
        File conf = new File(conf_file);
        // 不存在则创建并写入末班配置
        if(!conf.exists()){
            // 创建文件
            try {
                conf.createNewFile();
            }
            catch (Exception e){
                e.printStackTrace();
                System.out.println("配置文件初始化失败");
                System.exit(0);
            }
            // 写入模板配置
            LinkedHashMap<String,String> template = templateConf();
            updateConf(template);
        }
        // 存在则判断是否有新的模板配置字段需要加入
        else{
            LinkedHashMap<String,String> configs = readConf();
            LinkedHashMap<String,String> template = templateConf();
            // 遍历模板配置
            template.entrySet().forEach(n->{
                String k = n.getKey();
                String v = n.getValue();
                // 用户配置无这个字段,说明是配置模板新增的字段则加入
                if(!configs.containsKey(k)){
                    configs.put(k,v);
                }
            });
            // 配置更新
            updateConf(configs);
        }
    }



    /**
    *   @desc : 更新配置文件
    *   @auth : tyf
    *   @date : 2023-09-21  11:57:29
    */
    public static void updateConf(LinkedHashMap<String,String> configs){

        // 更新的配置
        configs.entrySet().forEach(n->{
            String k = n.getKey();
            String v = n.getValue();
            System.out.println("update配置: "+k+"="+v);
        });

        // 清空的方式写入
        try {
            FileWriter fw = new FileWriter(conf_file,false);// 覆盖方式
            BufferedWriter bw = new BufferedWriter(fw);
            StringBuffer content = new StringBuffer();
            configs.entrySet().forEach(n->{
                String k = n.getKey();
                String v = n.getValue();
                content.append(k).append("=").append(v).append("\r\n");
            });
            bw.write(content.toString());
            bw.close();
        }
        catch (Exception e){
            e.printStackTrace();
            System.out.println("配置更新失败");
            System.exit(0);
        }
    }


    /**
    *   @desc : 读取配置文件
    *   @auth : tyf
    *   @date : 2023-09-21  11:57:58
    */
    public static LinkedHashMap<String,String> readConf(){

        LinkedHashMap<String,String> configs = new LinkedHashMap<>();

        try {
            // 读取每一行
            ArrayList<String> data = TxtUtils.readTxt(conf_file);
            data.stream().forEach(n->{
                if(n.contains("=")){
                    String dd[] = n.split("=");
                    String k = dd[0].replace("\r\n","");
                    String v = dd[1].replace("\r\n","");
                    configs.put(k,v);
                }
            });
        }
        catch (Exception e){
            e.printStackTrace();
            System.out.println("配置文件读取失败");
            System.exit(0);
        }

        // 读取到的配置
        configs.entrySet().forEach(n->{
            String k = n.getKey();
            String v = n.getValue();
            System.out.println("read配置: "+k+"="+v);
        });

        return configs;
    }


    /**
    *   @desc : 模板配置,初始化时如果没有配置文件则通过模板配置创建一个
    *   @auth : tyf
    *   @date : 2023-09-21  11:59:06
    */
    public static LinkedHashMap<String,String> templateConf(){
        LinkedHashMap<String,String> configs = new LinkedHashMap<>();
        configs.put("serial_number_url","https://www.baidu.com");// 序列号api接口
        configs.put("serial_number_url_username","admin");// 序列号api接口用户名
        configs.put("serial_number_url_password","123456");// 序列号api接口密码
        configs.put("serial_number_rule_id","asdasdasdaswwsz");// 序列号规则编号
        configs.put("asd","asdasdasdaswwsz");// 序列号规则编号
        return configs;
    }


}
