package org.util;


import org.Main;
import org.apache.commons.io.FileUtils;

import java.io.File;

/**
*   @desc : 软件需要的 dll 文件初始化
*   @auth : tyf
*   @date : 2023-09-22  15:50:01
*/
public class DllUtil {


    /**
    *   @desc : 校验dll,不存在则进行初始化
    *   @auth : tyf
    *   @date : 2023-09-22  15:52:51
    */
    public static void dllInit(String user_home_dll,String dll_file){

        // 校验dll,不存在则进行初始化
        File dll = new File(user_home_dll+dll_file);
        if(dll.exists()){
            dll.delete();
        }
        try {
            FileUtils.copyInputStreamToFile(Main.class.getClassLoader().getResourceAsStream(dll_file), new File(user_home_dll+dll_file));
            System.out.println("初始化dll");
        }
        catch (Exception e){
            e.printStackTrace();
            System.out.println("dll文件初始化失败");
            System.exit(0);
        }

    }



}
