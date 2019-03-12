package com.zankong.tool.side_friend.diy_document;

import android.content.Context;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;

import com.zankong.tool.side_friend.R;
import com.zankong.tool.zkapp.document.ZKDocument;
import com.zankong.tool.zkapp.util.Util;
import com.zankong.tool.zkapp.util.ZKAppDocument;

import org.dom4j.Element;

/**
 * Created by YF on 2018/7/31.
 */

@ZKAppDocument("ffff")
public class asdf extends ZKDocument {

    private View mSurfaceView;
    private GestureDetector mGestureDetector;

    public asdf(Context context, Element root, Window window) {
        super(context, root, window);
    }

    @Override
    protected void initView() {
        setContextView(R.layout.doc_ffff);
        mSurfaceView = findViewById(R.id.surfaceView);
    }

    @Override
    protected void fillData(Element docElement) {
        mGestureDetector = new GestureDetector(getContext(),new GestureDetector.SimpleOnGestureListener(){
            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                Util.log("video","onSingleTapUp");

                return true;
            }

            @Override
            public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
                Util.log("video","onScroll");

                return super.onScroll(e1, e2, distanceX, distanceY);
            }

            @Override
            public boolean onDown(MotionEvent e) {
                Util.log("video","onDown");
                return false;
            }

            @Override
            public boolean onDoubleTap(MotionEvent e) {
                Util.log("video","onDoubleTap");

                return true;
            }
        });
        mSurfaceView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                mGestureDetector.onTouchEvent(motionEvent);
                return true;
            }
        });
    }
}
