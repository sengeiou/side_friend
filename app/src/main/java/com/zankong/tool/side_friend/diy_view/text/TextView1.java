package com.zankong.tool.side_friend.diy_view.text;


import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.TextView;

import com.zankong.tool.side_friend.R;

public class TextView1 extends android.support.v7.widget.AppCompatTextView {


    private boolean adjusTopForAscent = true;

    private Paint.FontMetricsInt fontMetricsInt;

    public TextView1(Context context) {
        super(context);
        setDelBorder();

    }

    public TextView1(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TextView1(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private void setDelBorder() {
        setIncludeFontPadding(false);

    }
}
