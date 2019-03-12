package com.zankong.tool.zkapp.v8fn.media_pck;

import android.content.Context;
import android.media.MediaPlayer;

import com.eclipsesource.v8.JavaCallback;
import com.eclipsesource.v8.V8;
import com.eclipsesource.v8.V8Array;
import com.eclipsesource.v8.V8Function;
import com.eclipsesource.v8.V8Object;
import com.zankong.tool.zkapp.ZKToolApi;
import com.zankong.tool.zkapp.util.ZKAppV8;
import com.zankong.tool.zkapp.zk_interface.ZKV8Fn;

/**
 * Created by YF on 2018/6/29.
 */

@ZKAppV8
public class ZKMedia implements ZKV8Fn {
    private Context mContext;
    private MediaManager mMediaManager;
    private V8 runtime;
    private V8Object media;

    public ZKMedia() {
        mContext = ZKToolApi.getInstance().getContext();
        runtime = ZKToolApi.runtime;
        media = new V8Object(runtime);
    }

    @Override
    public void addV8Fn() {
        media.registerJavaMethod((receiver, parameters) -> {
            mMediaManager = MediaManager.getInstance();
            mMediaManager.playSound(parameters.getString(0));
            return null;
        },"play");
        media.registerJavaMethod((receiver, parameters) -> {
            mMediaManager = MediaManager.getInstance();
            mMediaManager.initSensorListener(mContext);
            return null;
        },"sensor");
        media.registerJavaMethod((receiver, parameters) -> {
            mMediaManager = MediaManager.getInstance();
            mMediaManager.onPause();
            return null;
        },"onPause");
        media.registerJavaMethod(new JavaCallback() {
            @Override
            public Object invoke(V8Object receiver, V8Array parameters) {
                mMediaManager = MediaManager.getInstance();
                mMediaManager.resume();
                return null;
            }
        },"resume");
        media.registerJavaMethod(new JavaCallback() {
            @Override
            public Object invoke(V8Object receiver, V8Array parameters) {
                mMediaManager = MediaManager.getInstance();
                mMediaManager.release();
                return null;
            }
        },"release");
        media.registerJavaMethod(new JavaCallback() {
            @Override
            public Object invoke(V8Object receiver, final V8Array parameters) {
                final V8Function listener = (V8Function) parameters.getObject(0);
                mMediaManager = MediaManager.getInstance();
                mMediaManager.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        listener.call(null);
                    }
                });
                return null;
            }
        },"listener");
        runtime.add("media",media);
    }
}
