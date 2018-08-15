package com.example.facedemo.util;

import android.os.Environment;

import java.io.File;
import java.text.SimpleDateFormat;

/**
 * Created by lishuguo on 2018
 */

public class MyConfig {
    public static final double FACE_CRITICAL_VALUE = 0.55;//相似度临界值
    public static final String ROOT_CACHE = Environment.getExternalStorageDirectory()
            + File.separator+"AAA";
    public static final String FACE_DIR = "face";
    public static final String FACE_LIBRARY = ROOT_CACHE+File.separator+"library";
    public static final String FACE_CACHE = ROOT_CACHE+File.separator+"cache";

    public static final String modelPath = "AAA" + File.separator + "Model";
    public static final String model1 = ROOT_CACHE+ File.separator + "Model"+File.separator+ "seeta_fa_v1.1.bin";
    public static final String model2 = ROOT_CACHE+ File.separator + "Model"+File.separator + "seeta_fd_frontal_v1.0.bin";
    public static final String model3 = ROOT_CACHE+ File.separator + "Model"+File.separator + "seeta_fr_v1.0.bin";

}
