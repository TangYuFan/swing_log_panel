package org.util;




public class StringUtil {


    // 在字符串前面添加空格保持一定的长度
    public static String format(String value,int length){
        StringBuffer res = new StringBuffer();
        for (int i = 1; i <= length - value.length(); i++) {
            res.append(" ");
        }
        res.append(value);
        return res.toString();
    }


}
