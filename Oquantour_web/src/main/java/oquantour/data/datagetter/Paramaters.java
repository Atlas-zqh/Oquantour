package oquantour.data.datagetter;

import java.io.*;
import java.nio.charset.Charset;

/**
 * Created by island on 5/25/17.
 */
public class Paramaters {
    /**
     * 相对路径
     */
    public static final String ABSOLUTE_PATH = "cache/";

    /**
     * 获得cache文件夹的路径
     *
     * @return
     */
//    public static String getPath() {
//        System.out.println(Paramaters.class.getResource("/"));
//        System.out.println(Paramaters.class.getResource("/").getPath());
//        System.out.println(Paramaters.class.getResource("/").getPath().substring(1));
//
//        return Paramaters.class.getResource("/").getPath() + ABSOLUTE_PATH;
//        System.out.println(Paramaters.class.getResource("/").getPath() + ABSOLUTE_PATH);
//        return "Oquantour_web/WEB-INF/classes/cache/";
//        return "Oquantour_web/target/classes/cache/"; //本地可以
//    }

    public String getPath() {
        return "/" + Paramaters.class.getResource("/").getPath().substring(1) + ABSOLUTE_PATH;//本地可以
//        return "Oquantour_web/target/classes/cache/";
    }

    public BufferedReader getBufferedReader(String fileName) {
        File file = new File(getPath() + fileName);
        if(file.exists()) {
            BufferedReader reader;
            try {
                reader = new BufferedReader(new FileReader(file));
                return reader;
            } catch (IOException e) {
                return null;
            }
        }else{
            return null;
        }
    }

    public static void main(String[] args) {
/*
        System.out.println(getPath());
        System.out.println("/Users/island/IdeaProjects/Oquantour/Oquantour_web/target/classes/cache/000001.csv");
        BufferedReader reader = Paramaters.getBufferedReader("000001.csv");
        try {
            System.out.println(reader.readLine());
        }catch (IOException e){
            e.printStackTrace();
        }
    }
    */
    }
}
