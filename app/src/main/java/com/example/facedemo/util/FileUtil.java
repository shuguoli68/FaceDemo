package com.example.facedemo.util;

import android.graphics.Bitmap;
import android.os.Environment;
import android.util.Log;


import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by lishuguo on 2018
 */

public class FileUtil {

    private static final String TAG = "FileUtil";

    public static String createDir(String path){
        File file = new File(path);
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs(); // 创建父文件夹路径
        }
        if (!file.exists()){
            file.mkdirs();
        }
        return path;
    }

    /**
     * 保存一个暂时的bitmap图片
     *
     * 保存目录在
     * @param b
     */
    public static String saveTmpBitmap(Bitmap b,String name){
        String result= "";
        String jpegName = MyConfig.ROOT_CACHE+File.separator+MyConfig.FACE_DIR +File.separator+name +".jpg";
        Log.d("FileUtil",jpegName);
        result = jpegName;
        try {
            FileOutputStream fout = new FileOutputStream(jpegName);
            BufferedOutputStream bos = new BufferedOutputStream(fout);
            b.compress(Bitmap.CompressFormat.JPEG, 100 , bos);
            bos.flush();
            bos.close();
            Log.i(TAG, "暂存的 saveBitmap成功");
        } catch (IOException e) {
            // TODO Auto-generated catch block
            Log.i(TAG, "暂存的saveBitmap失败");
            e.printStackTrace();
        }
        return result;
    }
}
