/**
 * 采用中科院山世光开源的SeetaFaceEngine实现android上的人脸检测与对齐、识别
 * BSD license
 * 广州炒米信息科技有限公司
 * www.cume.cc
 * 吴祖玉
 * wuzuyu365@163.com
 * 2016.11.9
 *
 */

package seetaface;

import android.graphics.Bitmap;

public class SeetaFace {
	static {
		System.loadLibrary("SeetafaceSo");
	}

	//初始化so库，告诉底层人脸识别模型文件的目录
	//该目录下应当包括这3个文件：seeta_fd_frontal_v1.0.bin,seeta_fa_v1.1.bin,seeta_fr_v1.0.bin
	public native boolean init(String vModelDir);
 
	/**
	 * 检测人脸
	 * @param vImgData：图像数据
	 * @param vColNum：图像宽度
	 * @param vRowNum：图像高度
	 * @param vCh：通道数
	 * @param vFaceBmp：人脸抠图（有多个人脸也只抠1一个图返回）
	 * @return
	 */
	//public native CMSeetaFace[] GetFaces(byte[] vImgData, int vColNum, int vRowNum, int vCh, Bitmap vFaceBmp);
	
	/**
	 * 检测人脸
	 * @param vBmp：待检测人脸的大图
	 * @param vFaceBmp：其中一个人脸抠图
	 * @return
	 */
	public native CMSeetaFace[] DetectFaces(Bitmap vBmp, Bitmap vFaceBmp);
	
	/**
	 * 测试
	 * @param vVal
	 * @return
	 */
	public native int Test(int vVal); 
	/**
	 * 图像的gamma校正
	 * @param vColorBmp:原图
	 * @param vGammaBmp:处理后的图
	 * @param vGamma:gamma值
	 */
	public native void imGamma(Bitmap vColorBmp, Bitmap vGammaBmp, float vGamma);
	
	/**
	 * 彩色转灰度图
	 * @param vColorBmp
	 * @param vGrayBmp
	 */
	public native void im2gray(Bitmap vColorBmp, Bitmap vGrayBmp);
	
	/**
	 * 检测人脸，返回各人脸位置，每个人的以;分隔，坐标以分号分隔
	 * @param vImgData:图像的char*数据
	 * @param vColNum:图像列数
	 * @param vRowNum:图像行数
	 * @param vCh:图像通道数，3或4
	 * @param vDetectModelPath:正面人脸检测模型的绝对路径
	 * @param vFaceNo:人脸编号，用于保存特征数据生成文件名用
	 * @param vFaceBmp:人脸抠图
	 * @return
	 */	
	public native String DetectFace(byte[] vImgData, int vColNum, int vRowNum, int vCh, String vDetectModelPath, int vFaceNo, Bitmap vFaceBmp);
 
	/**
	 * 比对2个人脸特征值的相似度 
	 * @param vFeat1
	 * @param vFeat2
	 * @param vNum
	 * @return 
	 */
	public native float CalcSimilarity(float[] vFeat1, float[] vFeat2);
	
} 
