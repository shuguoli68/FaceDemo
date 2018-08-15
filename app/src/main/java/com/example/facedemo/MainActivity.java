package com.example.facedemo;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;

import com.example.facedemo.util.DetecteSeeta;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity{

    private ListView listView;
    private DetecteSeeta mDetecteSeeta;
    private ProgressDialog pd;
    private Myadapter myadapter;
    private List<FaceBean> list = new ArrayList<>();
    private Handler mHandler;
    private ImageView img;
    private Bitmap bit=null,change=null;

    private Integer[] imgs = {R.mipmap.zyl0,R.mipmap.zyl1,R.mipmap.zyl2,R.mipmap.zyl3,R.mipmap.zyl4
            ,R.mipmap.zyl5,R.mipmap.zyl6,R.mipmap.zly,R.mipmap.ly,R.mipmap.lyf,R.mipmap.lyf2
            ,R.mipmap.lzy,R.mipmap.cwt};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mDetecteSeeta = new DetecteSeeta();
        initData();
        initView();
        mHandler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (msg.what == 1){
                    myadapter.notifyDataSetChanged();
                }else if (msg.what == 2){
                    img.setImageBitmap(change);
                }
            }
        };

    }

    private List<FaceBean> initData() {
        FaceBean bean = null;
        for (int i=0;i<imgs.length;i++){
            bean = new FaceBean();
            bean.setId(i+1);
            bean.setRes(imgs[i]);
            if (i<7) {
                bean.setTxt("同一人");
            }else {
                bean.setTxt("不同人");
            }
            list.add(bean);
        }
        return list;
    }

    private void initView() {
        img = (ImageView) findViewById(R.id.img);
        listView = (ListView) findViewById(R.id.list);
        myadapter = new Myadapter(MainActivity.this,list);
        listView.setAdapter(myadapter);
        bit = BitmapFactory.decodeResource(getResources(),R.mipmap.zyl0);
        findViewById(R.id.btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(){
                    @Override
                    public void run() {
                        super.run();
                        if (bit!=null) {
                            change = mDetecteSeeta.DetectionBitmap(bit);
                            mHandler.sendEmptyMessage(2);
                        }
                        for (int i=0;i<list.size();i++){
                            list.get(i).setSim("相似度："+getSim(list.get(i).getRes()));
                            mHandler.sendEmptyMessage(1);
                        }
                    }
                }.start();
            }
        });
    }

    private float getSim(int res){
        float similarity = mDetecteSeeta.getSimilarityNum(bit,BitmapFactory.decodeResource(getResources(),res));
        Log.i("cmp", "相似度：" + similarity);
        return similarity;
    }
}
