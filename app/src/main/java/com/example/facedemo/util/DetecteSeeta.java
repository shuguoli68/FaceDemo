package com.example.facedemo.util;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;


import seetaface.CMSeetaFace;
import seetaface.SeetaFace;

import java.io.File;
import java.nio.ByteBuffer;


public class DetecteSeeta {
    private final static String TAG="DetecteSeeta";
    Bitmap mOriginBmp1,mOriginBmp2; //原位图
    Bitmap mDrawBmp; //显示用的位图，
    int mWidth1, mHeight1; //照片1的宽度高度、通道
    int mWidth2, mHeight2; //照片2的宽度高度、通道
    int face_num1, face_num2;
    SeetaFace jni;
    Bitmap mFaceBmp1,mFaceBmp2;//小脸

    String mFaceModelDir = MyConfig.ROOT_CACHE+File.separator+"Model"+File.separator;//人脸正面检测模型

    /***
     * 得到两张人脸的相似度
     * @param face1 第一张
     * @param face2 第二张
     * @return
     */
    public float getSimilarityNum(Bitmap face1,Bitmap face2){
        if (new File(MyConfig.model1).exists()&&new File(MyConfig.model2).exists()&&new File(MyConfig.model3).exists()){

        }else {
            Log.i("mode","人脸正面检测模型不存在");
            return 0.0f;
        }
        jni = new SeetaFace();//实例化检测对象
        jni.init(mFaceModelDir);
        mFaceBmp1 = Bitmap.createBitmap(256,256, Bitmap.Config.ARGB_8888);
        mFaceBmp2 = Bitmap.createBitmap(256,256, Bitmap.Config.ARGB_8888);
        mOriginBmp1 = XUtils.getScaledBitmap(FileUtil.saveTmpBitmap(face1,"mOriginBmp1"), 600);
        mOriginBmp2 = XUtils.getScaledBitmap(FileUtil.saveTmpBitmap(face2,"mOriginBmp2"), 600);
        if(null == mOriginBmp1 ||null==mOriginBmp2){
            Log.d(TAG, "图片无法加载");
            return 0.0f;
        }
        int width1 = mOriginBmp1.getWidth();
        int height1 = mOriginBmp1.getHeight();
        int width2 = mOriginBmp2.getWidth();
        int height2 = mOriginBmp2.getHeight();
        CMSeetaFace[] tRetFaces1,tRetFaces2;
        mWidth1 = width1;
        mHeight1 = height1;
        mWidth2 = width2;
        mHeight2 = height2;
        tRetFaces1 = jni.DetectFaces(mOriginBmp1, mFaceBmp1);
        tRetFaces2 = jni.DetectFaces(mOriginBmp2, mFaceBmp2);
        if (tRetFaces1==null || tRetFaces2==null){
            return 0.0f;
        }
        face_num1 =  tRetFaces1.length;
        face_num2 =  tRetFaces2.length;
        if(face_num1 > 0 && face_num2 > 0){
            float aa = jni.CalcSimilarity(tRetFaces1[0].features, tRetFaces2[0].features);
            Log.d(TAG,"检测结果"+aa);
            //msg += "相似度:"+tSim;
            return aa;
        }else {
            return 0.0f;
        }
    }



    /**
     * 传入一个bitmap 返回检测完毕的bitmap
     * @param tmp1
     * @return
     */
    public Bitmap DetectionBitmap(Bitmap tmp1){
        jni = new SeetaFace();//实例化检测对象
        if (jni!=null){
            jni.init(mFaceModelDir);
        }
        else {
            Log.d(TAG, "图片无法加载");
        }
        mFaceBmp1 = Bitmap.createBitmap(256,256, Bitmap.Config.ARGB_8888);
        mOriginBmp1 = XUtils.getScaledBitmap(FileUtil.saveTmpBitmap(tmp1,"mOriginBmp1"), 600);
        if(null == mOriginBmp1){
            Log.d(TAG, "图片无法加载");
            return null;
        }
        int width = mOriginBmp1.getWidth();
        int height = mOriginBmp1.getHeight();
        CMSeetaFace[] tRetFaces;
        mWidth1 = width;
        mHeight1 = height;
        Long tLong = System.currentTimeMillis();
        tRetFaces = jni.DetectFaces(mOriginBmp1, mFaceBmp1);
        tLong = System.currentTimeMillis()-tLong;
        Log.d(TAG, "优化之后一个检测的运行时间"+tLong);
        int face_num = 0;
        if(null != tRetFaces){
            face_num =  tRetFaces.length;
        }
        Log.d(TAG, "face_num="+face_num);
        //大图，显示人脸矩形
        mDrawBmp = mOriginBmp1.copy(Bitmap.Config.ARGB_8888, true);
        Canvas canvas = new Canvas(mDrawBmp);
        Paint paint = new Paint();
        //线条宽度
        int tStokeWid = 1+(width+height)/300;
        paint.setColor(Color.RED);
        paint.setStyle(Paint.Style.STROKE);//不填充
        paint.setStrokeWidth(tStokeWid);  //线的宽度
        for(int i=0; i<face_num; i++){
            int left = tRetFaces[i].left;
            int top = tRetFaces[i].top;
            int right = tRetFaces[i].right;
            int bottom = tRetFaces[i].bottom;
            Log.i("loadimg", "drawRect,i="+i+", "+left+","+right+","+top+","+bottom);
            canvas.drawRect(left, top, right, bottom, paint);

            //画特征点
            for(int j=0; j<5; j++){
                int px = tRetFaces[i].landmarks[j*2];
                int py = tRetFaces[i].landmarks[j*2+1];
                canvas.drawCircle(px, py, tStokeWid, paint);
            }
        }
        return mDrawBmp;
    }

    /**
     * 检测一个脸里面的面部信息数组
     * @param tmp1 检测的bitmap
     * @return 一个含有面部信息的数组
     */
    public CMSeetaFace[] DetectionBitmapInfo(Bitmap tmp1){

        jni = new SeetaFace();//实例化检测对象
        jni.init(mFaceModelDir);
        mFaceBmp1 = Bitmap.createBitmap(256,256, Bitmap.Config.ARGB_8888);
        mOriginBmp1 = XUtils.getScaledBitmap(FileUtil.saveTmpBitmap(tmp1,"mOriginBmp1"), 600);
        if(null == mOriginBmp1){
            Log.d(TAG, "图片无法加载");
            return null;
        }
        int width = mOriginBmp1.getWidth();
        int height = mOriginBmp1.getHeight();
        CMSeetaFace[] tRetFaces;
        mWidth1 = width;
        mHeight1 = height;
        tRetFaces = jni.DetectFaces(mOriginBmp1, mFaceBmp1);
        return tRetFaces;
    }

    /***
     * 根据两个面部信息数组计算第一张脸的相似度
     * @param tRetFaces1 第一张脸的信息数组
     * @param tRetFaces2 第二张脸的信息数组
     * @return 相似度
     */

    public  float CalcSimilarityNum(CMSeetaFace[] tRetFaces1, CMSeetaFace[] tRetFaces2){
        jni = new SeetaFace();//实例化检测对象
        float i = jni.CalcSimilarity(tRetFaces1[0].features, tRetFaces2[0].features);
        return i;
    }

    /**
     * 获取图像的字节数据
     * @param image
     * @return
     */
    public byte[] getPixelsRGBA(Bitmap image) {
        // calculate how many bytes our image consists of
        int bytes = image.getByteCount();

        ByteBuffer buffer = ByteBuffer.allocate(bytes); // Create a new buffer
        image.copyPixelsToBuffer(buffer); // Move the byte data to the buffer

        byte[] temp = buffer.array(); // Get the underlying array containing the

        return temp;
    }
}
