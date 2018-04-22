package com.example.didonglin.testapplication;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class DrawBall extends View {
    public float currentX = 70;
    public float currentY = 70;
    //定义。创建画笔
    Paint p = new Paint();
    public DrawBall(Context context) {
        super(context);
    }

    public DrawBall(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public DrawBall(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
    @Override
    protected void onDraw(Canvas canvas) {
        // TODO Auto-generated method stub
        super.onDraw(canvas);
        //设置画笔的颜色
        p.setColor(Color.RED);
        //绘制一个小球
        //参数分别是：圆心坐标，半径 ，所使用的画笔
        canvas.drawCircle(currentX, currentY, 30, p);

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // TODO Auto-generated method stub
        //修改当前的坐标
        this.currentX = event.getX();
        this.currentY = event.getY();
        //重绘小球
        this.invalidate();

        return true;
    }
}
