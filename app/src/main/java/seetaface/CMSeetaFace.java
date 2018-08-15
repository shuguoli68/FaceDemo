package seetaface;

import java.util.Arrays;

public class CMSeetaFace {
 
	//人脸范围
    public int left, right, top, bottom;
    
    public float roll_angle;  //旋转角
    public float pitch_angle;	//俯仰角
    public float yaw_angle;	//偏头角
        
    //5个点的坐标，眼睛，鼻子，嘴巴
    public int landmarks[] = new int[10];
    
    //人脸特征
	public float features[] = new float[2048];

    @Override
    public String toString() {
        return "CMSeetaFace{" +
                "left=" + left +
                ", right=" + right +
                ", top=" + top +
                ", bottom=" + bottom +
                ", landmarks=" + Arrays.toString(landmarks) +
                '}';
    }
}
