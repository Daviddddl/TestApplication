package com.example.didonglin.testapplication;

import java.io.Serializable;


public class Msg implements Serializable{
    private String text;
    private int type;
    public static final int TYPE_SEND = 1;
    public static final int TYPE_RECEIVED = 0;
    private String Time;


    public Msg(String text, int type, String time){
        this.text = text;
        this.type = type;
        this.Time = time;

    }

    public void setText(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }

    public String getTime() {
        return Time;
    }

    public void setTime(String time) {
        Time = time;
    }


}
