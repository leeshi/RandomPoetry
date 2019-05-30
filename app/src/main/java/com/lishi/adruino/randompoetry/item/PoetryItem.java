package com.lishi.adruino.randompoetry.item;

public class PoetryItem {
    public final String mContent;
    public final String mTitle;
    public final String mPoet;
    public final String serial;

    public PoetryItem(String content,String title,String poet,String serial){
        this.mContent = content;
        this.mPoet = poet;
        this.mTitle = title;
        this.serial = serial;
    }

    public PoetryItem(String content,String title,String poet){
        this.mContent = content;
        this.mPoet = poet;
        this.mTitle = title;
        serial = "";
    }

    /*getter
     */
    public String getContent(){
        return this.mContent;
    }
    public String getTitle(){
        return this.mTitle;
    }
    public String getPoet(){
        return this.mPoet;
    }

    @Override
    public String toString(){
        return mTitle + mPoet + mContent;
    }
}

