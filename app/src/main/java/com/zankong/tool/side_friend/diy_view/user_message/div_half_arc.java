package com.zankong.tool.side_friend.diy_view.user_message;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

public class div_half_arc extends View {
    private Paint mPaint;
    private int mCircleWidth;

    public div_half_arc(Context context) {
        super(context);
        init();
    }

    public div_half_arc(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public div_half_arc(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

    }

    private void init(){
        mPaint = new Paint();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        mPaint.setAntiAlias(true);//取消锯齿
        mPaint.setStyle(Paint.Style.FILL);//设置画圆弧的画笔的属性为描边(空心)，个人喜欢叫它描边，叫空心有点会引起歧义
        mPaint.setStrokeWidth(mCircleWidth);
        mPaint.setColor(Color.WHITE);
        int r = getHeight()/2+getWidth()*getWidth()/getHeight()/8;
        canvas.drawCircle(getWidth()/2,r,r,mPaint);
    }
}
