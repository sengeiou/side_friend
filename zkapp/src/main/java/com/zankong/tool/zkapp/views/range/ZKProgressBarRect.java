package com.zankong.tool.zkapp.views.range;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by YF on 2018/7/17.
 */

public class ZKProgressBarRect extends View{
    private float progress = 12f;
    private int length = 100;

    public ZKProgressBarRect(Context context) {
        super(context);
        initPaint();
    }

    public ZKProgressBarRect(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initPaint();
    }

    public ZKProgressBarRect(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initPaint();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawBorder(canvas);
        drawProgress(canvas);
        drawEmpty(canvas);
    }

    public float getProgress() {
        return progress;
    }

    public void setProgress(float progress) {
        this.progress = progress;
        if (mOnProgressChangeListener != null) {
            mOnProgressChangeListener.change(progress);
        }
        invalidate();
    }

    private Paint progressPaint;//走过区域的画笔
    private Paint emptyPaint;//没走过区域的画笔
    private void initPaint(){
        progressPaint = new Paint();
        progressPaint.setColor(Color.parseColor("#ff00ff"));
        emptyPaint = new Paint();
        emptyPaint.setColor(Color.parseColor("#464646"));
    };

    /**
     * 进度走过的区域
     */
    private void drawProgress(Canvas canvas){
        Rect rect = new Rect(0,0, (int) (getWidth()*progress/length),getHeight());
        canvas.drawRect(rect,progressPaint);
    }

    /**
     * 进度没走过的区域
     */
    private void drawEmpty(Canvas canvas){
        Rect rect = new Rect((int) (getWidth()*progress/length),0, getWidth(),getHeight());
        canvas.drawRect(rect,emptyPaint);
    }

    /**
     * 边框线
     */
    private void drawBorder(Canvas canvas){
        Path path = new Path();
        RectF rectF = new RectF(0,0,getWidth(),getHeight());
        path.addRoundRect(rectF,getHeight()/2,getHeight()/2,Path.Direction.CCW);
        canvas.clipPath(path);
    }

    private OnProgressChangeListener mOnProgressChangeListener;
    public void setOnProgressChangeListener(OnProgressChangeListener onProgressChangeListener){
        mOnProgressChangeListener = onProgressChangeListener;
    }
}
