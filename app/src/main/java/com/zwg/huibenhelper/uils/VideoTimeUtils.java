package com.zwg.huibenhelper.uils;

import java.util.Formatter;
import java.util.Locale;

/**
 * 创建人： ganziqian
 * 时  间： 2017/6/26.
 * 作  用：
 */

public class VideoTimeUtils {




    public static String stringForTime(int timeMs) {

        //将长度转换为时间
        StringBuilder mFormatBuilder;
        Formatter mFormatter;
        mFormatBuilder = new StringBuilder();
        mFormatter = new Formatter(mFormatBuilder, Locale.getDefault());


        int totalSeconds = timeMs / 1000;

        int seconds = totalSeconds % 60;
        int minutes = (totalSeconds / 60) % 60;
        int hours   = totalSeconds / 3600;

        mFormatBuilder.setLength(0);
        if (hours > 0) {
            return mFormatter.format("%d:%02d:%02d", hours, minutes, seconds).toString();
        } else {
            return mFormatter.format("%02d:%02d", minutes, seconds).toString();
        }
    }

}
