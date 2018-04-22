package com.example.didonglin.testapplication;

/**
 * Created by zchai on 2018/4/11.
 */

public class SharedArea {

    //是否接收消息标志位
    volatile boolean flag;

    public SharedArea(boolean flag){
        this.flag = flag;
    }
}
