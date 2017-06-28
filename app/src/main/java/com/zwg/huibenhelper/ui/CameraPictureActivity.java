package com.zwg.huibenhelper.ui;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.hardware.Camera.Size;
import android.media.AudioFormat;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.OrientationEventListener;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.zwg.huibenhelper.MainActivity;
import com.zwg.huibenhelper.R;
import com.zwg.huibenhelper.uils.FileCraUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class CameraPictureActivity extends  Activity implements OnClickListener,
        SurfaceHolder.Callback {
    private static final String TAG = "CameraPictureActivity";

    private Button mTakePhoto;
    private SurfaceView mSurfaceView;
    private Camera mCamera;

    private OrientationEventListener mOrEventListener; // 设备方向监听器
    private Boolean mCurrentOrientation=true; // 当前设备方向 横屏false,竖屏true


    private int type;


    /* 图像数据处理还未完成时的回调函数 */
    private Camera.ShutterCallback mShutter = new Camera.ShutterCallback() {
        @Override
        public void onShutter() {
            // 一般显示进度条
        }
    };

    /* 图像数据处理完成后的回调函数 */
    private Camera.PictureCallback mJpeg = new Camera.PictureCallback() {

        @Override
        public void onPictureTaken(byte[] data, Camera camera) {
            // 保存图片
           // mFileName = UUID.randomUUID().toString() + ".jpg";

            String tag=getIntent().getStringExtra("tag");
            String name=getIntent().getStringExtra("name");









            type=getIntent().getIntExtra("type",0);
            File file=new File(FileCraUtils.basepath+tag+"/"+name+"_1.jpg");
            if(type==1){
                if(file.exists()){
                    file=getNewFile(2,FileCraUtils.basepath+tag+"/"+name+"_");
                    try {
                        file.createNewFile();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }else {
                    try {
                        file.createNewFile();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }


            }else if(type==2){
                if(file.exists()){
                    file.delete();
                    try {
                        file.createNewFile();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }else {
                    try {
                        file.createNewFile();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }





            FileOutputStream out = null;
            try {
              //  out = openFileOutput(mFileName, Context.MODE_PRIVATE);
                out = new FileOutputStream(file);
                byte[] newData = null;
                if (mCurrentOrientation) {
                    // 竖屏时，旋转图片再保存
                    Bitmap oldBitmap = BitmapFactory.decodeByteArray(data, 0,
                            data.length);
                    Matrix matrix = new Matrix();
                    matrix.setRotate(0);
                    Bitmap newBitmap = Bitmap.createBitmap(oldBitmap, 0, 0,
                            oldBitmap.getWidth(), oldBitmap.getHeight(),
                            matrix, true);
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    newBitmap.compress(Bitmap.CompressFormat.JPEG, 85, baos);
                    newData = baos.toByteArray();
                    out.write(newData);
                } else {
                    out.write(data);
                }

            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (out != null)
                        out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }


            Intent intent=new Intent();
            intent.putExtra("result",1);
            setResult(MainActivity.START_CODE,intent);
            finish();
          /*  Intent i = new Intent(CameraPictureActivity.this, CopySpeekActivity.class);
            i.putExtra("path", mFileName);
            i.putExtra("name",  ed.getText().toString());
            startActivity(i);
            finish();*/
        }
    };

    private int frequence = 8000; //录制频率，单位hz.这里的值注意了，写的不好，可能实例化AudioRecord对象的时候，会出错。我开始写成11025就不行。这取决于硬件设备
    private int channelConfig = AudioFormat.CHANNEL_CONFIGURATION_MONO;
    private int audioEncoding = AudioFormat.ENCODING_PCM_16BIT;
    @SuppressWarnings("deprecation")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera_picture);
        mTakePhoto = (Button) findViewById(R.id.take_photo);
        mTakePhoto.setOnClickListener(this); // 拍照按钮监听器


/*
        BeepManager beepManager=new BeepManager(this,FileCraUtils.vivopath+"0.pcm");
        beepManager.playBeepSoundAndVibrate();*/

        String tag=getIntent().getStringExtra("tag");
        FileCraUtils.creat(tag);

        startOrientationChangeListener(); // 启动设备方向监听器
        mSurfaceView = (SurfaceView) findViewById(R.id.my_surfaceView);
        SurfaceHolder holder = mSurfaceView.getHolder();
     //   holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        holder.setType(SurfaceHolder.SURFACE_TYPE_GPU);
        holder.addCallback(this); // 回调接口

    }

    private final void startOrientationChangeListener() {
        mOrEventListener = new OrientationEventListener(this) {
            @Override
            public void onOrientationChanged(int rotation) {
                if (((rotation >= 0) && (rotation <= 45)) || (rotation >= 315)
                        || ((rotation >= 135) && (rotation <= 225))) {// portrait
                    mCurrentOrientation = true;
                    Log.i(TAG, "竖屏");
                } else if (((rotation > 45) && (rotation < 135))
                        || ((rotation > 225) && (rotation < 315))) {// landscape
                    mCurrentOrientation = true;
                    Log.i(TAG, "横屏");
                }
            }
        };
        mOrEventListener.enable();
    }

    @Override
    public void onClick(View v) {
        // 点击拍照
        switch (v.getId()) {
            case R.id.take_photo:
/*
                String str=ed.getText().toString();

                if(str.equals("")){
                    Toast.makeText(CameraPictureActivity.this,"请设置当前页数",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(!isNumeric(str)){
                    Toast.makeText(CameraPictureActivity.this,"请设置纯数字",Toast.LENGTH_SHORT).show();
                    return;

                }*/
                mCamera.takePicture(mShutter, null, mJpeg);
                break;
            default:
                break;
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
    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width,
                               int height) {
        // SurfaceView尺寸发生改变时（首次在屏幕上显示同样会调用此方法），初始化mCamera参数，启动Camera预览

        Parameters parameters = mCamera.getParameters();// 获取mCamera的参数对象
        Size largestSize = getBestSupportedSize(parameters
                .getSupportedPreviewSizes());
        parameters.setPreviewSize(largestSize.width, largestSize.height);// 设置预览图片尺寸
        largestSize = getBestSupportedSize(parameters
                .getSupportedPictureSizes());// 设置捕捉图片尺寸
        parameters.setPictureSize(largestSize.width, largestSize.height);

        mCamera.setDisplayOrientation(90);

//        parameters.setPreviewFrameRate(4);

        mCamera.setParameters(parameters);

        try {
            mCamera.startPreview();
        } catch (Exception e) {
            if (mCamera != null) {
                mCamera.release();
                mCamera = null;
            }
        }

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        // SurfaceView创建时，建立Camera和SurfaceView的联系
        if (mCamera != null) {
            try {
                mCamera.setPreviewDisplay(holder);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        // SurfaceView销毁时，取消Camera预览
        if (mCamera != null) {
            mCamera.stopPreview();
        }
    }

    @SuppressLint("NewApi")
    @Override
    public void onResume() {
        super.onResume();
        // 开启相机
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
            mCamera = Camera.open(0);
            // i=0 表示后置相机
        } else
            mCamera = Camera.open();
    }

    @Override
    public void onPause() {
        super.onPause();
        // 释放相机
        if (mCamera != null) {
            mCamera.release();
            mCamera = null;
        }

    }

    private Size getBestSupportedSize(List<Size> sizes) {
        // 取能适用的最大的SIZE
        Size largestSize = sizes.get(0);
        int largestArea = sizes.get(0).height * sizes.get(0).width;
        for (Size s : sizes) {
            int area = s.width * s.height;
            if (area > largestArea) {
                largestArea = area;
                largestSize = s;
            }
        }
        return largestSize;
    }

    private File getNewFile(int i,String path2){
        File file=new File(path2+i+".jpg");
        if(file.exists()){
            return getNewFile(i+1,path2);
        }else {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return file;

    }


}