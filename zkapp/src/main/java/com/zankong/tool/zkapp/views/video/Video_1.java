package com.zankong.tool.zkapp.views.video;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.media.AudioManager;
import android.view.Display;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.VideoView;

import com.eclipsesource.v8.JavaCallback;
import com.eclipsesource.v8.V8Array;
import com.eclipsesource.v8.V8Function;
import com.eclipsesource.v8.V8Object;
import com.zankong.tool.zkapp.R;
import com.zankong.tool.zkapp.document.ZKDocument;
import com.zankong.tool.zkapp.util.Util;
import com.zankong.tool.zkapp.views.ZKViewAgent;

import org.dom4j.Attribute;
import org.dom4j.Element;

import java.io.IOException;
import java.util.HashMap;
import java.util.Timer;
import java.util.logging.Handler;

import tv.danmaku.ijk.media.player.IMediaPlayer;
import tv.danmaku.ijk.media.player.IjkMediaPlayer;
import tv.danmaku.ijk.media.player.pragma.DebugLog;

/**
 * Created by YF on 2018/7/23.
 */

public class Video_1 extends ZKViewAgent {
    private IjkMediaPlayer mIjkMediaPlayer;           //播放器
    private SurfaceView mSurfaceView;                 //播放布局
    private String url = null;                        //播放路径
    private AudioManager mAudioManager;              //声音控制器
    private GestureDetector mGestureDetector;        //手势控制器
    private int mStreamMaxVolume,                    //最大音量
            volume = 0,                                //当前音量百分比
            brightness = -1,                           //当前亮度百分比
            currentPosition = 0;                      //当前播放进度
    private final float MAX_BRIGHTNESS = 225f;      //最大亮度值
    private TextView mTVolume,mTBrightness,mTProcess;
    private RelativeLayout mControlView;
    private boolean isKey = false,                   //是否锁屏
                     isPrepared,                      //是否准备完成
                     isFullScreen;                    //是否是全屏
    private Timer viewTimer,                          //定时隐藏控制视图
                   processTimer;                      //定时更新进度条
    private ImageView mFullScreen;
    private FrameLayout mVideoView;
    private V8Function mJSCompletion;

    public Video_1(ZKDocument zkDocument, Element element) {
        super(zkDocument, element);
    }


    @Override
    public void initView(ViewGroup viewGroup) {
        setContentView(R.layout.view_video_1);
        mVideoView = ((FrameLayout) findViewById(R.id.videoView));
        mSurfaceView = (SurfaceView) findViewById(R.id.surfaceView);
        mControlView = (RelativeLayout) findViewById(R.id.controlView);
        mTVolume = (TextView) findViewById(R.id.volume);
        mTBrightness = (TextView) findViewById(R.id.brightness);
        mTProcess = (TextView) findViewById(R.id.process);
        mFullScreen = ((ImageView) findViewById(R.id.fullScreen));
    }

    @Override
    public void fillData(Element selfElement) {
        isFullScreen = getContext().getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE;
        mGestureDetector = new GestureDetector(getContext(),new PlayerGestureListener());
        mSurfaceView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (!isKey) {
                    mGestureDetector.onTouchEvent(motionEvent);
                }
                return true;
            }
        });
        mAudioManager = (AudioManager) getContext().getSystemService(Context.AUDIO_SERVICE);
        mStreamMaxVolume = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        for (Attribute attribute : selfElement.attributes()) {
            String value = attribute.getValue();
            switch (attribute.getName()) {
                case "url":
                    url = value;
                    break;
            }
        }
        mIjkMediaPlayer = new IjkMediaPlayer();

        IjkMediaPlayer.loadLibrariesOnce(null);
        IjkMediaPlayer.native_profileBegin("libijkplay,so");
        mIjkMediaPlayer.setOnCompletionListener(new IMediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(IMediaPlayer iMediaPlayer) {
                //播放完成回调
            }
        });
        mIjkMediaPlayer.setOnErrorListener(new IMediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(IMediaPlayer iMediaPlayer, int i, int i1) {
                //播放错误回调
                return false;
            }
        });
        mSurfaceView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder surfaceHolder) {
                mIjkMediaPlayer.setDisplay(surfaceHolder);
            }

            @Override
            public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {

            }

            @Override
            public void surfaceDestroyed(SurfaceHolder surfaceHolder) {

            }
        });
        try {
            mIjkMediaPlayer.setDataSource(url);
        } catch (IOException e) {
            e.printStackTrace();
        }
        mIjkMediaPlayer.prepareAsync();
        mIjkMediaPlayer.start();
        mFullScreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setFullScreen();
                Util.log("video","isFullScreen---"+isFullScreen);
            }
        });
    }

    //得到跟布局
    private ViewGroup getBaseView(){
        return ((Activity) getContext()).getWindow().getDecorView().findViewById(android.R.id.content);
    }

    @Override
    public boolean onBack() {
        if (isFullScreen){
            setFullScreen();
            return false;
        }else {
            return true;
        }
    }

    //设置全屏
    private void setFullScreen(){
        Util.log("video","setFullScreen0---"+isFullScreen);
        if (isFullScreen){
            mFullScreen.setBackgroundResource(R.drawable.video_full_screen);
            Util.showT("小屏幕");
            getBaseView().removeView(mVideoView);
            ((ViewGroup) getView()).addView(mVideoView);
            ((Activity) getContext()).getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
            ((Activity) getContext()).setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            ViewGroup.LayoutParams layoutParams = mVideoView.getLayoutParams();
            layoutParams.height = WindowManager.LayoutParams.MATCH_PARENT;
            layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
            mVideoView.setLayoutParams(layoutParams);
            isFullScreen = false;
        }else {
            Util.showT("全屏");
            mFullScreen.setBackgroundResource(R.drawable.video_full_screen_off);
            ((ViewGroup) getView()).removeAllViews();
            getBaseView().addView(mVideoView);
            ((Activity) getContext()).getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
            ((Activity) getContext()).setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            ViewGroup.LayoutParams layoutParams = mVideoView.getLayoutParams();
            layoutParams.height = getContext().getResources().getDisplayMetrics().widthPixels;
            layoutParams.width = getContext().getResources().getDisplayMetrics().heightPixels;
            mVideoView.setLayoutParams(layoutParams);
            isFullScreen = true;
        }
        Util.log("video","setFullScreen---"+isFullScreen);
    }

    @Override
    public Object getResult() {
        return url;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onStop() {
        super.onStop();
        mIjkMediaPlayer.reset();
        mIjkMediaPlayer.native_profileEnd();
    }

    //更改声音
    private void setVolume(int temporaryVolume){
        if (temporaryVolume > 100) temporaryVolume = 100;
        if (temporaryVolume < 0) temporaryVolume = 0;
        mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC,temporaryVolume*mStreamMaxVolume/100,0);
        mTVolume.setText("音量:\n"+temporaryVolume+"%");
    }
    //更改亮度
    private void setBrightness(int temporaryBrightness){
        if (temporaryBrightness > 100)temporaryBrightness = 100;
        if (temporaryBrightness < 0) temporaryBrightness = 0;
        WindowManager.LayoutParams attributes = ((Activity) getContext()).getWindow().getAttributes();
        attributes.screenBrightness = temporaryBrightness * 0.01f;
        ((Activity) getContext()).getWindow().setAttributes(attributes);
        mTBrightness.setText("亮度:\n"+temporaryBrightness+"%");
    }
    //更改进度条
    private void setProcess(int temporaryProcess){
        if (temporaryProcess > 100 )temporaryProcess = 100;
        if (temporaryProcess < 0) temporaryProcess = 0;
        float total = mIjkMediaPlayer.getDuration();
        mIjkMediaPlayer.seekTo((long) (temporaryProcess * 100 / total));
        mTProcess.setText("进度:"+temporaryProcess+"%");
    }


    @Override
    public void initThisV8(V8Object thisV8) {
        super.initThisV8(thisV8);
        thisV8.registerJavaMethod(new JavaCallback() {
            @Override
            public Object invoke(V8Object receiver, V8Array parameters) {

                return null;
            }
        },"start");
        thisV8.registerJavaMethod(new JavaCallback() {
            @Override
            public Object invoke(V8Object receiver, V8Array parameters) {

                return null;
            }
        },"stop");
        thisV8.registerJavaMethod(new JavaCallback() {
            @Override
            public Object invoke(V8Object receiver, V8Array parameters) {
                return null;
            }
        },"set");
        thisV8.registerJavaMethod(new JavaCallback() {
            @Override
            public Object invoke(V8Object receiver, V8Array parameters) {
                return null;
            }
        },"isPlaying");
        thisV8.registerJavaMethod(new JavaCallback() {
            @Override
            public Object invoke(V8Object receiver, V8Array parameters) {
                return null;
            }
        },"getCurrentPosition");
        thisV8.registerJavaMethod(new JavaCallback() {
            @Override
            public Object invoke(V8Object receiver, V8Array parameters) {
                return null;
            }
        },"getBufferPercentage");
        thisV8.registerJavaMethod(new JavaCallback() {
            @Override
            public Object invoke(V8Object receiver, V8Array parameters) {
                return null;
            }
        },"seekTo");
        thisV8.registerJavaMethod(new JavaCallback() {
            @Override
            public Object invoke(V8Object receiver, V8Array parameters) {
                return null;
            }
        },"getDuration");
        thisV8.registerJavaMethod(new JavaCallback() {
            @Override
            public Object invoke(V8Object receiver, V8Array parameters) {
                setVolume(parameters.getInteger(0));
                return null;
            }
        },"setVolume");
        thisV8.registerJavaMethod(new JavaCallback() {
            @Override
            public Object invoke(V8Object receiver, V8Array parameters) {
                setBrightness(parameters.getInteger(0));
                return null;
            }
        },"setBrightness");
    }

    //手势监听
    private class PlayerGestureListener extends GestureDetector.SimpleOnGestureListener{
        private boolean isChangeVolume = false, //音量控制
                isChangeProcess = false,//进度控制
                isMoving = false;//是否移动中

        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            return true;
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            Util.log("video","onFling");
            return super.onFling(e1, e2, velocityX, velocityY);
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            if (isKey)return super.onScroll(e1, e2, distanceX, distanceY);
            if (!isMoving){
                volume = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC)*100 / mStreamMaxVolume;
                Util.log("video","screenBrightness"+((Activity) getContext()).getWindow().getAttributes().screenBrightness);
                brightness = (int) (((Activity) getContext()).getWindow().getAttributes().screenBrightness * 100);
                if (brightness == -1){
                    brightness = 50;
                }
                isChangeProcess = Math.abs(distanceX) >= Math.abs(distanceY);
                isChangeVolume = e1.getX() < getContext().getResources().getDisplayMetrics().widthPixels * 0.5f;
                isMoving = true;
            }
            float XLength = e2.getX() - e1.getX();
            float YLength = e1.getY() - e2.getY();
            int percent = (int) (isChangeProcess ? XLength / mSurfaceView.getWidth()*100 : YLength / mSurfaceView.getHeight()*100);
            if (isChangeProcess){
                setProcess(percent);
            }else {
                if (isChangeVolume){
                    setVolume(percent + volume);
                }else {
                    setBrightness(percent + brightness);
                }
            }
            return super.onScroll(e1, e2, distanceX, distanceY);
        }

        @Override
        public boolean onDown(MotionEvent e) {
            if (!isKey){
                isMoving = false;
            }
            return super.onDown(e);
        }

        @Override
        public boolean onDoubleTap(MotionEvent e) {
            return true;
        }
    }
}
