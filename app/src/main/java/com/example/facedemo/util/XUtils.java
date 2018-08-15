/**
 * 采用中科院山世光开源的SeetaFaceEngine实现android上的人脸检测与对齐、识别
 * 遵照BSD license
 * 广州炒米信息科技有限公司
 * www.cume.cc
 * 吴祖玉
 * wuzuyu365@163.com
 * 2016.11.9
 *
 */

package com.example.facedemo.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.util.Log;
import java.io.File;
import java.io.IOException;


public class XUtils {

  // 照片文件是否存在，注意，与数据库是否存在是不同的
  public static Boolean fileExists(String vPath) {
    if (null == vPath || "".equals(vPath)) {
      return false;
    }

    try {
      File file = new File(vPath);
      if (!file.exists()) {
        return false;
      }
    } catch (Exception e) {
      return false;
    }

    return true;
  }

  /**
   * 旋转图片
   * 
   * @param angle
   * @param bitmap
   * @return Bitmap
   */
  public static Bitmap rotateImage(int angle, Bitmap bitmap) {
    if (null == bitmap) {
      return bitmap;
    }
    // 图片旋转矩阵
    Matrix matrix = new Matrix();
    matrix.postRotate(angle);
    if (null == bitmap) {
      Log.v("rotateImageError", "bitmap is null");
      return bitmap;
    }

    // 得到旋转后的图片
    try {
      Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);

      return resizedBitmap;
    } catch (OutOfMemoryError e) {
      return bitmap;
    }
  }

  /**
   * 读取图片属性：旋转的角度
   * 
   * @param path
   *          图片绝对路径
   * @return degree旋转的角度
   */
  public static int readPictureDegree(String path) {
    int degree = 0;
    try {
      ExifInterface exifInterface = new ExifInterface(path);

      int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
      switch (orientation) {
        case ExifInterface.ORIENTATION_ROTATE_90:
          degree = 90;
          break;
        case ExifInterface.ORIENTATION_ROTATE_180:
          degree = 180;
          break;
        case ExifInterface.ORIENTATION_ROTATE_270:
          degree = 270;
          break;
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
    return degree;
  }

  
//获取图片的缩略图,宽度和高度中较小的缩放到vMinWidth. 确保宽度和高度最小的都能覆盖到，
 	//比如图片是3000*2000，要缩放到 150*100, 那么vMinWidth=100;	
	public static Bitmap getScaledBitmap(String vPath, int vMinWidth) {	
		String tag = "getScaledBitmap";
		Log.v(tag, "1==");
		if(null == vPath){
			Log.v(tag, "路径为null");
			return null; 
		}
		
		if(vPath.trim().equals("")){
			Log.v(tag, "路径为空");
			return null; 
		}
		
		Log.v(tag, "path="+vPath+", 期望：vMinWidth="+vMinWidth);  
		
		File file = new File(vPath);
		//如果不存在了，直接返回
		if(!file.exists()){
			Log.v(tag, "文件不存在：path="+vPath);
			return null; 
		}
		 		
		// 先获取图片的宽和高
		Options options = new Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(vPath, options);
		if (options.outWidth <= 0 || options.outHeight <= 0) {
			Log.v(tag, "解析图片失败");
			return null;
		}
		Log.v(tag, "原图大小：width:" + options.outWidth + ",height:"
				+ options.outHeight + ",path=" + vPath);
		int height0 = options.outHeight;

		int tMinWidth = Math.min(options.outWidth, options.outHeight);
				
		// 压缩图片，注意inSampleSize只能是2的整数次幂，如果不是的，话，向下取最近的2的整数次幂，例如3实际上会是2，7实际上会是4
		options.inSampleSize = Math.max(1, tMinWidth/vMinWidth);   
		//Log.v(tag, "options.inSampleSize="+options.inSampleSize);
			 
		//不能用Config.RGB_565
		//options.inPreferredConfig = Config.RGB_565;  
		options.inDither = false; 
		options.inPurgeable = true; 
		options.inInputShareable = true; 
		options.inJustDecodeBounds = false;
		Bitmap thumbImgNow = null; 
		try{
			 thumbImgNow = BitmapFactory.decodeFile(vPath, options);
		}catch(OutOfMemoryError e){
			Log.v(tag, "OutOfMemoryError, decodeFile失败   ");
			return null; 
		}
		 
		//Log.v(tag, "thumbImgNow.size="+thumbImgNow.getWidth()+","+thumbImgNow.getHeight()); 
		
		if(null == thumbImgNow){
			Log.v(tag, "decodeFile失败   ");
			return null;
		}
		
		int wid = thumbImgNow.getWidth();
		int hgt = thumbImgNow.getHeight();
		
		int degree = readPictureDegree(vPath);
		if (degree != 0) {
			//Log.v(tag, "degree="+degree);			
			// 把图片旋转为正的方向 
			thumbImgNow = rotateImage(degree, thumbImgNow);
		}
				 
		 wid = thumbImgNow.getWidth();
		 hgt = thumbImgNow.getHeight();
				
		tMinWidth = Math.min(wid, hgt);
		if (tMinWidth > vMinWidth) {
			//如果原图片最小宽度比预期最小高度大才进行缩小
			float ratio = ((float) vMinWidth) / tMinWidth; 
			Matrix matrix = new Matrix();    
			matrix.postScale(ratio, ratio);    
			thumbImgNow  = Bitmap.createBitmap(thumbImgNow, 0, 0, wid, hgt, matrix, true);   
		}
		 
	    Log.v(tag, "处理后, size, width="+thumbImgNow.getWidth()+",height="+thumbImgNow.getHeight());
	
		return thumbImgNow;
	}

}




