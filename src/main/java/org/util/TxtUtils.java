package org.util;

import java.io.*;
import java.util.ArrayList;

public class TxtUtils {

    //读取文本文档
    public static ArrayList<String> readTxt(String source) throws Exception{
        ArrayList<String> lines=new ArrayList<>();
        BufferedReader br=new BufferedReader(new InputStreamReader(new FileInputStream(source),"UTF-8"));
        String line = null;
        while ((line = br.readLine()) != null) {
            lines.add(line);
        }
        br.close();
        // 返回数组
        return lines;
    }

    //读取文本文档
    public static ArrayList<String> readTxt(String source,String charset) throws Exception{
        ArrayList<String> lines=new ArrayList<>();
        BufferedReader br=new BufferedReader(new InputStreamReader(new FileInputStream(source),charset));
        String line = null;
        while ((line = br.readLine()) != null) {
            lines.add(line);
        }
        br.close();
        // 返回数组
        return lines;
    }


    //读取文本第n行  可以将目录和页码分开识别  然后按照行拼接到一起
    public static String readLine(String source,Integer index) throws Exception{

        BufferedReader br=new BufferedReader(new InputStreamReader(new FileInputStream(source),"UTF-8"));
        String line = null;
        int i = 1;
        while ((line = br.readLine()) != null) {
            if(i==index){
                br.close();
                return line;
            }
            else{
                i++;
            }
        }
        br.close();
        return null;
    }



    public static void main(String[] args) throws Exception{


        String mapping = "{\n" +
                "  \"properties\": {\n" +
                "    \"brandName\": {\n" +
                "      \"type\": \"keyword\"\n" +
                "    },\n" +
                "    \"categoryName\": {\n" +
                "      \"type\": \"keyword\"\n" +
                "    },\n" +
                "    \"createTime\": {\n" +
                "      \"type\": \"date\",\n" +
                "      \"format\": \"yyyy-MM-dd HH:mm:ss\"\n" +
                "    },\n" +
                "    \"id\": {\n" +
                "      \"type\": \"long\"\n" +
                "    },\n" +
                "    \"price\": {\n" +
                "      \"type\": \"double\"\n" +
                "    },\n" +
                "    \"saleNum\": {\n" +
                "      \"type\": \"integer\"\n" +
                "    },\n" +
                "    \"status\": {\n" +
                "      \"type\": \"integer\"\n" +
                "    },\n" +
                "    \"stock\": {\n" +
                "      \"type\": \"integer\"\n" +
                "    },\n" +
                "    \"spec\": {\n" +
                "      \"type\": \"text\",\n" +
                "      \"analyzer\": \"ik_max_word\",\n" +
                "      \"search_analyzer\": \"ik_smart\"\n" +
                "    },\n" +
                "    \"title\": {\n" +
                "      \"type\": \"text\",\n" +
                "      \"analyzer\": \"ik_max_word\",\n" +
                "      \"search_analyzer\": \"ik_smart\"\n" +
                "    }\n" +
                "  }\n" +
                "}";
        System.out.println(mapping);

    }

}
