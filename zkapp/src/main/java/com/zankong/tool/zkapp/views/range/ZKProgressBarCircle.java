package com.zankong.tool.zkapp.views.range;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by YF on 2018/7/17.
 */

public class ZKProgressBarCircle extends View {
    private float mProgress = 45;
    private float mMax = 360;
    private Paint mProgressPaint;
    private Paint mEmptyPaint;
    float strokeWidth = 50;

    public ZKProgressBarCircle(Context context) {
        super(context);
        init();
    }

    public ZKProgressBarCircle(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ZKProgressBarCircle(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public float getProgress() {
        return mProgress;
    }

    public void setProgress(float progress) {
        mProgress = progress;
        if (mOnProgressChangeListener != null)mOnProgressChangeListener.change(progress);
        invalidate();
    }

    private void init(){
        mProgressPaint = new Paint();
        mProgressPaint.setColor(Color.parseColor("#ff0000"));
        mProgressPaint.setAntiAlias(true);
        mProgressPaint.setStrokeWidth(strokeWidth);
        mProgressPaint.setStyle(Paint.Style.STROKE);

        mEmptyPaint = new Paint();
        mEmptyPaint.setColor(Color.parseColor("#00ff00"));
        mEmptyPaint.setAntiAlias(true);
        mEmptyPaint.setStrokeWidth(strokeWidth);
        mEmptyPaint.setStyle(Paint.Style.STROKE);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawEmpty(canvas);
        drawProgress(canvas);
    }

    private void drawProgress(Canvas canvas){
        canvas.drawArc(realRect(),-90,mProgress/mMax*360,false,mProgressPaint);
    }

    private void drawEmpty(Canvas canvas){
        canvas.drawArc(realRect(),0,360,false,mEmptyPaint);
    }
    private RectF realRect(){
        return new RectF(strokeWidth/2,strokeWidth/2,getWidth()-strokeWidth/2,getHeight()-strokeWidth/2);
    }

    private OnProgressChangeListener mOnProgressChangeListener;
    public void setOnProgressChangeListener(OnProgressChangeListener onProgressChangeListener){
        mOnProgressChangeListener = onProgressChangeListener;
    }

}
