package com.zwg.huibenhelper.uils;

import android.content.Context;
import android.content.SharedPreferences;

import com.zwg.huibenhelper.MyApp;

/**
 * 创建人： ganziqian
 * 时  间： 2017/6/26.
 * 作  用：
 */

public class SaveUtils {

    public static void SaveData(String tag,String page,String start,String end,String isbo){
        SharedPreferences preferences= MyApp.getInstance().getSharedPreferences("nn", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=preferences.edit();
        editor.putString("tag",tag);
        editor.putString("page",page);
        editor.putString("start",start);
        editor.putString("end",end);
        editor.putString("isbo",isbo);
        editor.commit();

    }

    public static String getDate(String key){
        SharedPreferences preferences= MyApp.getInstance().getSharedPreferences("nn", Context.MODE_PRIVATE);
        return preferences.getString(key,"");
    }


}
