package com.zwg.huibenhelper;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.universalvideoview.UniversalMediaController;
import com.universalvideoview.UniversalVideoView;
import com.zwg.huibenhelper.ui.CameraPictureActivity;
import com.zwg.huibenhelper.uils.SaveUtils;
import com.zwg.huibenhelper.uils.UpZipFileUtils;
import com.zwg.huibenhelper.uils.ZipExtractorTask;

import java.io.File;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.vov.vitamio.Vitamio;

/**
 * Vitamio视频播放框架Demo
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener,RadioGroup.OnCheckedChangeListener{
    //视频地址
    private String path = "http://file.xjkb.com/group1/M00/00/0F/eEzdEFgQTbGASInvAd4nNjAV_u8577.mp4";


    public static final int START_CODE=98;

    private long myposy=0;


    private EditText biaoshiEd,pageEd,startEd,endEd;
    private RadioGroup myradio;
    private int suanNa=100;



    private static final String TAG = "MainActivity";
    private static final String SEEK_POSITION_KEY = "SEEK_POSITION_KEY";
    private static final String VIDEO_URL = "mnt/sdcard/hei/video/16";

    UniversalVideoView mVideoView;
    UniversalMediaController mMediaController;

    View mBottomLayout;
    View mVideoLayout;

    private int mSeekPosition;
    private int cachedHeight;
    private boolean isFullscreen;


    private CheckBox checkBox,checkBox2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        //定义全屏参数
        int flag = WindowManager.LayoutParams.FLAG_FULLSCREEN;
        //获得当前窗体对象
        Window window = MainActivity.this.getWindow();
        //设置当前窗体为全屏显示
        window.setFlags(flag, flag);
        //必须写这个，初始化加载库文件
        Vitamio.isInitialized(this);
        //设置视频解码监听
//        if (!LibsChecker.checkVitamioLibs(this)) {
//            return;
//        }
        setContentView(R.layout.activity_main);
        initView();
       initData();



        initCofView();

      //  String time=stringForTime(VideoView.getCurrentPosition());
    }








    private void initCofView() {
        biaoshiEd= (EditText) findViewById(R.id.ed_bioshi);
        pageEd= (EditText) findViewById(R.id.ed_page);
        startEd= (EditText) findViewById(R.id.ed_start);
        endEd= (EditText) findViewById(R.id.ed_end);
        myradio= (RadioGroup) findViewById(R.id.my_radio);

        checkBox= (CheckBox) findViewById(R.id.my_check);
        checkBox2= (CheckBox) findViewById(R.id.my_time_check);
        Button saveBtn= (Button) findViewById(R.id.save_btn);
        Button changeBtn= (Button) findViewById(R.id.change_btn);
        Button huoBtn= (Button) findViewById(R.id.huo_btn);
        saveBtn.setOnClickListener(this);
        changeBtn.setOnClickListener(this);
        huoBtn.setOnClickListener(this);
        myradio.setOnCheckedChangeListener(this);

        //ZipExtractorTask task = new ZipExtractorTask("/storage/usb3/system.zip", "/storage/emulated/legacy/", this, true);


        new Thread(){
            @Override
            public void run() {
                super.run();
                File file=new File("mnt/sdcard/hei/444.zip");
                try {
                    ZipExtractorTask.unzip(file,"mnt/sdcard/hei/222/");
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.e("=====",e.toString());
                }

            }
        }.start();



    }

    //初始化控件
    private void initView() {
        mVideoLayout = findViewById(R.id.video_layout);
        mBottomLayout = findViewById(R.id.bottom_layout);
        mVideoView = (UniversalVideoView) findViewById(R.id.videoView);
        mMediaController = (UniversalMediaController) findViewById(R.id.media_controller);
        mVideoView.setMediaController(mMediaController);
        setVideoAreaSize();
/*        if (mSeekPosition > 0) {
            mVideoView.seekTo(mSeekPosition);
        }
        mVideoView.start();
        mMediaController.setTitle("Big Buck Bunny");*/

        mVideoView.setOnCompletionListener(new android.media.MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(android.media.MediaPlayer mp) {
                Log.d(TAG, "onCompletion ");
            }
        });

    }









    /**
     * 置视频区域大小
     */
    private void setVideoAreaSize() {
        mVideoLayout.post(new Runnable() {
            @Override
            public void run() {
                int width = mVideoLayout.getWidth();
                cachedHeight = (int) (width * 405f / 720f);
//                cachedHeight = (int) (width * 3f / 4f);
//                cachedHeight = (int) (width * 9f / 16f);
                ViewGroup.LayoutParams videoLayoutParams = mVideoLayout.getLayoutParams();
                videoLayoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
                videoLayoutParams.height = cachedHeight;
                mVideoLayout.setLayoutParams(videoLayoutParams);
                mVideoView.setVideoPath(VIDEO_URL);
                mVideoView.requestFocus();
            }
        });
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.d(TAG, "onSaveInstanceState Position=" + mVideoView.getCurrentPosition());
        outState.putInt(SEEK_POSITION_KEY, mSeekPosition);
    }

    @Override
    protected void onRestoreInstanceState(Bundle outState) {
        super.onRestoreInstanceState(outState);
        mSeekPosition = outState.getInt(SEEK_POSITION_KEY);
        Log.d(TAG, "onRestoreInstanceState Position=" + mSeekPosition);
    }






    @Override
    public void onBackPressed() {
        if (this.isFullscreen) {
            mVideoView.setFullscreen(false);
        } else {
            super.onBackPressed();
        }
    }





    //初始化数据
    private void initData() {

    }

    @Override
    protected void onResume() {
        super.onResume();
    //    mCustomMediaController.playOrPause();
     /*   Log.e("==========",myposy+"");
       // mVideoView.seekTo(myposy);

      */
        if (mSeekPosition > 0) {
            mVideoView.seekTo(mSeekPosition);
        }
        mVideoView.start();
        mMediaController.setTitle("Big Buck Bunny");
        mVideoView.pause();
    }



    @Override
    protected void onPause() {
        super.onPause();
        myposy=  mVideoView.getCurrentPosition();
        mSeekPosition=  mVideoView.getCurrentPosition();
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.e("=====","=======aaa");
        mVideoView.pause();

    }



    @Override
    public void onClick(View v) {

        int id=v.getId();
        switch (id){
            case R.id.save_btn:

                pai(1);
                break;
            case R.id.change_btn:
                pai(2);
                break;
            case R.id.huo_btn:
                endEd.setText(mVideoView.getCurrentPosition()+"");
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e("========","==========yu");
    }

    private void pai(int i) {

        mVideoView.pause();
        endEd.setText(mVideoView.getCurrentPosition()+"");

        String biaoshi=biaoshiEd.getText().toString();
        String page=pageEd.getText().toString();
        String start=startEd.getText().toString();
        String end=endEd.getText().toString();

        if(check(biaoshi,page,start,end)){


            String name=biaoshi+"_"+suanNa+"_"+start+"_"+end+"_"+page;
            SaveUtils.SaveData(biaoshi,page,start,end,suanNa+"");

            Intent intent=new Intent(MainActivity.this, CameraPictureActivity.class);
            intent.putExtra("name",name);
            intent.putExtra("tag",biaoshi);
            intent.putExtra("type",i);
            myposy= (int) mVideoView.getCurrentPosition();
            startActivityForResult(intent,START_CODE);
        }

    }

    private boolean check(String biaoshi,String page,String start,String end){
        boolean flag=true;
        if(biaoshi.equals("")){
            MyApp.getInstance().showToast("请输入标识");
            return false;
        }
        if(page.equals("")){
            MyApp.getInstance().showToast("请输入页数");
            return false;
        }
        if(start.equals("")){
            MyApp.getInstance().showToast("请输入开始时间");
            return false;
        }
        if(end.equals("")){
            MyApp.getInstance().showToast("请输入结束时间");
            return false;
        }
        if(suanNa==100){
            MyApp.getInstance().showToast("请选择播放视频还是播放图片");
            return false;
        }
        if(!isNumeric(start)){
            MyApp.getInstance().showToast("时间必须为纯数字");
            return false;
        }
        if(!isNumeric(end)){
            MyApp.getInstance().showToast("时间必须为纯数字");
            return false;
        }
        if(!isNumeric(page)){
            MyApp.getInstance().showToast("页数必须为纯数字");
            return false;
        }
        if(Integer.parseInt(start)>Integer.parseInt(end)){
            MyApp.getInstance().showToast("开始时间不能大于结束时间");
            return false;
        }
        return flag;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(data!=null){
            if(resultCode==START_CODE){
                int result=data.getIntExtra("result",0);

                if(result==1){
                    if(checkBox.isChecked()){
                        int page2=Integer.parseInt(pageEd.getText().toString());
                        pageEd.setText((page2+1)+"");
                    }
                    if(checkBox2.isChecked()){
                        String start1=startEd.getText().toString();
                        String end1=endEd.getText().toString();
                        startEd.setText(end1);
                        endEd.setText("");
                    }
                }

            }
        }

    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {

        for (int i=0;i<group.getChildCount();i++){
            RadioButton radioButton= (RadioButton) group.getChildAt(i);
            if(radioButton.isChecked()){
                suanNa=i;
            }
        }

    }
    public boolean isNumeric(String str){
        Pattern pattern = Pattern.compile("[0-9]*");
        Matcher isNum = pattern.matcher(str);
        if( !isNum.matches() ){
            return false;
        }
        return true;
    }
}
