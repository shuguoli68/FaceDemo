package com.example.facedemo;

/**
 * Created by shugu on 2018/7/14.
 */

public class FaceBean {

    private int res;
    private int id;
    private String txt;
    private String sim;

    public FaceBean() {

    }

    public FaceBean(int res, int id, String txt, String sim) {
        this.res = res;
        this.id = id;
        this.txt = txt;
        this.sim = sim;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getRes() {
        return res;
    }

    public void setRes(int res) {
        this.res = res;
    }

    public String getSim() {
        return sim;
    }

    public void setSim(String sim) {
        this.sim = sim;
    }

    public String getTxt() {
        return txt;
    }

    public void setTxt(String txt) {
        this.txt = txt;
    }
}
