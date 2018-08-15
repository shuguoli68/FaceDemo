package com.example.facedemo;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.example.facedemo.util.AssetsToSd;
import com.example.facedemo.util.FileUtil;
import com.example.facedemo.util.MyConfig;

import java.io.File;

/**
 * Created by shugu on 2018/7/14.
 */

public class WelcomeActivity extends AppCompatActivity{
    String TAG = "permission";
    private ProgressDialog pd;
    private static final int MSG_COPE = 0;
    private static final int MSG_COPE_FAIL = 2;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        initPermission();
    }

    private void initPermission() {
        if (Build.VERSION.SDK_INT < 23) {
            // 如果系统版本低于23，直接跑应用的逻辑
            runApp();
        }else {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE}, 2);
            } else {
                runApp();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 2) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                runApp();
            } else {
                Toast.makeText(this, "获取权限被拒绝后的操作", Toast.LENGTH_SHORT).show();
                Log.i("TAG","获取权限被拒绝后的操作");
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }


    /**
     * 跑应用的逻辑
     */
    private void runApp() {
        FileUtil.createDir(MyConfig.FACE_LIBRARY);
        FileUtil.createDir(MyConfig.ROOT_CACHE+ File.separator+MyConfig.FACE_DIR);
        FileUtil.createDir(MyConfig.FACE_CACHE);

        /* 显示ProgressDialog */
        if (new File(MyConfig.model1).exists()&&new File(MyConfig.model2).exists()&&new File(MyConfig.model3).exists()){
            startToMain();
            return;
        }
        pd = pd.show(this,"复制必要文件", "复制中，请稍后……");
         /* 开启一个新线程，在新线程里执行耗时的方法 */
        new Thread(new Runnable() {
            @Override
            public void run() {
                AssetsToSd.getInstance(WelcomeActivity.this).copyAssetsToSD("Model",MyConfig.modelPath).setFileOperateCallback(new AssetsToSd.FileOperateCallback() {
                    @Override
                    public void onSuccess() {
                        // TODO: 文件复制成功时，主线程回调
                        handler.sendEmptyMessage(MSG_COPE);
                    }

                    @Override
                    public void onFailed(String error) {
                        // TODO: 文件复制失败时，主线程回调
                        handler.sendEmptyMessage(MSG_COPE_FAIL);
                    }
                });
            }
        }).start();
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            int mCode = msg.what;
            switch (mCode){
                case MSG_COPE:
                    Log.d(TAG,"复制完成！");
                    Toast.makeText(WelcomeActivity.this,"复制完成！",Toast.LENGTH_SHORT).show();
                    if (pd.isShowing()) {
                        pd.dismiss();
                    }
                    startToMain();
                    break;
                case MSG_COPE_FAIL:
                    Log.d(TAG,"复制失败！");
                    Toast.makeText(WelcomeActivity.this,"复制失败！",Toast.LENGTH_SHORT).show();
                    if (pd.isShowing()) {
                        pd.dismiss();
                    }
                    finish();
                    break;
            }
        }
    };

    private void startToMain(){
        startActivity(new Intent(WelcomeActivity.this,MainActivity.class));
        finish();
    }
}
