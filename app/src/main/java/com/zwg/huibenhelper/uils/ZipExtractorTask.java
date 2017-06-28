package com.zwg.huibenhelper.uils;

import com.file.zip.ZipEntry;
import com.file.zip.ZipFile;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;

/**
 * 创建人： ganziqian
 * 时  间： 2017/6/28.
 * 作  用：
 */


public class ZipExtractorTask {
    /**
     * @param file 待解压文件
     * @param dir  解压后文件的存放目录
     * @throws IOException
     */
    public static void unzip(File file, String dir) throws IOException {
        ZipFile zipFile = new ZipFile(file, "UTF-8");//设置压缩文件的编码方式为GBK
        Enumeration<ZipEntry> entris = zipFile.getEntries();
        ZipEntry zipEntry = null;
        File tmpFile = null;
        BufferedOutputStream bos = null;
        InputStream is = null;
        byte[] buf = new byte[1024];
        int len = 0;
        while (entris.hasMoreElements()) {
            zipEntry = entris.nextElement();
            // 不进行文件夹的处理,些为特殊处理
            tmpFile = new File(dir + zipEntry.getName());
            if (zipEntry.isDirectory()) {//当前文件为目录
                if (!tmpFile.exists()) {
                    tmpFile.mkdir();
                }
            } else {
                if (!tmpFile.exists()) {
                    tmpFile.createNewFile();
                }
                is = zipFile.getInputStream(zipEntry);
                bos = new BufferedOutputStream(new FileOutputStream(tmpFile));
                while ((len = is.read(buf)) > 0) {
                    bos.write(buf, 0, len);
                }
                bos.flush();
                bos.close();
            }
        }
    }
}