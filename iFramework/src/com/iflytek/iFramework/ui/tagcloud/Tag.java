package com.iflytek.iFramework.ui.tagcloud;
/**
 * Komodo Lab: Tagin! Project: 3D Tag Cloud
 * Google Summer of Code 2011
 * @authors Reza Shiftehfar, Sara Khosravinasr and Jorge Silva
 */

/*
 * 标签类:
 * 现在标签仅仅是多维数据集。之后,他们将取代真正的文本!
 */
public class Tag implements Comparable<Tag>{
    public Tag() {
        this("", 0f, 0f, 0f, 1.0f, 0, "");
    }
    public Tag(String text, int popularity) {
        this(text, 0f, 0f, 0f, 1.0f, popularity, "");
    }
    public Tag(String text, int popularity, String url) {
        this(text, 0f, 0f, 0f, 1.0f, popularity, url);
    }
    public Tag(String text,float locX, float locY, float locZ) {
        this(text, locX, locY, locZ, 1.0f, DEFAULT_POPULARITY, "");
    }
    public Tag(String text,float locX, float locY, float locZ, float scale) {
        this(text, locX, locY, locZ, scale, DEFAULT_POPULARITY, "");
    }
    public Tag(String text,float locX, float locY, float locZ, float scale, int popularity,
               String url) {
        this.text = text;
        this.locX = locX;
        this.locY = locY;
        this.locZ = locZ;

        this.loc2DX = 0;
        this.loc2DY=0;

        this.colorR= 0.5f;
        this.colorG= 0.5f;
        this.colorB= 0.5f;
        this.alpha = 1.0f;

        this.scale = scale;
        this.popularity= popularity;
        this.url = url;
    }

    @Override
    public int compareTo(Tag another) {
        return (int)(another.locZ - locZ);
    }

    public float getLocX() {
        return locX;
    }
    public void setLocX(float locX) {
        this.locX = locX;
    }
    public float getLocY() {
        return locY;
    }
    public void setLocY(float locY) {
        this.locY = locY;
    }
    public float getLocZ() {
        return locZ;
    }
    public void setLocZ(float locZ) {
        this.locZ = locZ;
    }
    public float getScale() {
        return scale;
    }
    public void setScale(float scale) {
        this.scale = scale;
    }
    public String getText() {
        return text;
    }
    public void setText(String text) {
        this.text = text;
    }
    public float getColorR() {
        return colorR;
    }
    public void setColorR(float colorR) {
        this.colorR = colorR;
    }
    public float getColorG() {
        return colorG;
    }
    public void setColorG(float colorG) {
        this.colorG = colorG;
    }
    public float getColorB() {
        return colorB;
    }
    public void setColorB(float colorB) {
        this.colorB = colorB;
    }
    public float getAlpha() {
        return alpha;
    }
    public void setAlpha(float alpha) {
        this.alpha = alpha;
    }
    public int getPopularity() {
        return popularity;
    }
    public void setPopularity(int popularity) {
        this.popularity = popularity;
    }

    public int getTextSize() {
        return textSize;
    }
    public void setTextSize(int textSize) {
        this.textSize = textSize;
    }
    public float getLoc2DX() {
        return loc2DX;
    }
    public void setLoc2DX(float loc2dx) {
        loc2DX = loc2dx;
    }
    public float getLoc2DY() {
        return loc2DY;
    }
    public void setLoc2DY(float loc2dy) {
        loc2DY = loc2dy;
    }
    public int getParamNo() {
        return paramNo;
    }
    public void setParamNo(int paramNo) {
        this.paramNo = paramNo;
    }
    public String getUrl() {
        return url;
    }
    public void setUrl(String url) {
        this.url = url;
    }

    private String text, url;
    private int popularity;  //这是标签的重要性/流行
    private int textSize;
    private float locX, locY, locZ; //3 d的中心标记
    private float loc2DX, loc2DY;
    private float scale;
    private float colorR, colorG, colorB, alpha;
    private static final int DEFAULT_POPULARITY = 1;
    private int paramNo; //把这个标签的设置参数
}
