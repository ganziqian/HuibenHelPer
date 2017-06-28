package com.zwg.huibenhelper;

import android.app.Application;
import android.widget.Toast;

/**
 * 创建人： ganziqian
 * 时  间： 2017/6/26.
 * 作  用：
 */

public class MyApp extends Application{


    private static MyApp app;
    @Override
    public void onCreate() {
        super.onCreate();
        app=this;
    }

    public static MyApp getInstance(){
        return app;
    }

    public  void showToast(String msg){
        Toast.makeText(app,msg,Toast.LENGTH_SHORT).show();
    }

}
