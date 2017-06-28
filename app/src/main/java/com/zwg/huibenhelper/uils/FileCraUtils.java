package com.zwg.huibenhelper.uils;

import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;

/**
 * 创建人： ganziqian
 * 时  间： 2017/6/23.
 * 作  用：
 */

public class FileCraUtils {
    public static final String basepath= Environment.getExternalStorageDirectory().getAbsolutePath()+"/imagesave/";

    public static void creat(String tag){
       // private String basePth="mnt/sdcard/copyres/image/";

        File file=new File(basepath);
        if(!file.exists()){
            file.mkdir();
        }

        File file2=new File(basepath+tag+"/");
        if(!file2.exists()){
            file2.mkdir();
        }



    }

    /**
     * 获取指定文件大小
     * @param
     * @return
     * @throws Exception 　　
     */
    public static long getFileSize(File file) throws Exception {
        long size = 0;
        if (file.exists()) {
            FileInputStream fis = null;
            fis = new FileInputStream(file);
            size = fis.available();
        } else {
           // file.createNewFile();
            Log.e("获取文件大小", "文件不存在!");
        }
        return size;
    }

}
