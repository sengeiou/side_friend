package com.zankong.tool.zkapp.v8fn.media_pck;

import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.util.Log;

import com.eclipsesource.v8.V8Function;
import com.zankong.tool.zkapp.ZKToolApi;

import java.io.IOException;


/**
 * Created by YF on 2017/6/20.
 */
public class MediaManager {
    private static MediaPlayer mMediaPlayer = new MediaPlayer();
    private String filePath;
    private V8Function listener;
    private MediaPlayer.OnCompletionListener mOnCompletionListener;
    private static volatile MediaManager mediaManager = null;
    public static MediaManager getInstance(){
        if (mediaManager == null){
            synchronized (MediaManager.class){
                if (mediaManager == null){
                    mediaManager = new MediaManager();
                }
            }
        }
        return mediaManager;
    }
    private static boolean isPause;
    //以下开始传感器监听
    static SensorManager mSensorManager;
    static Sensor sensor;
    //声音管理器
    static AudioManager mAudioManager;


    public void playSound(){
        if (filePath != null){
            playSound(filePath);
        }
    }

    public void playSound(String filePath){
        if (mMediaPlayer == null){
            mMediaPlayer = new MediaPlayer();
            mMediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
                @Override
                public boolean onError(MediaPlayer mp, int what, int extra) {
                    mMediaPlayer.reset();
                    return false;
                }
            });
        }else {
            mMediaPlayer.reset();
        }
        try {
            mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mMediaPlayer.setOnCompletionListener(mOnCompletionListener);
            if (filePath.contains("http")) {
                mMediaPlayer.setDataSource(ZKToolApi.getInstance().getContext(), Uri.parse(filePath));
                mMediaPlayer.prepareAsync();
                mMediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                    @Override
                    public void onPrepared(MediaPlayer mp) {
                        mMediaPlayer.start();
                        mMediaPlayer.setVolume(1f,1f);
                    }
                });
            }
            else {
                if (filePath.contains("file:///android_asset/")){
                    String file = filePath.replace("file:///android_asset/","");
                    AssetManager assetMg = ZKToolApi.getInstance().getContext().getAssets();
                    AssetFileDescriptor fileDescriptor = assetMg.openFd(file);
                    mMediaPlayer.setDataSource(fileDescriptor.getFileDescriptor(), fileDescriptor.getStartOffset(), fileDescriptor.getLength());
                }else {
                    mMediaPlayer.setDataSource(filePath);
                }
                mMediaPlayer.prepare();
                mMediaPlayer.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }




    public void initSensorListener(final Context context){
        mSensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        sensor = mSensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
        // 注册传感器
        mSensorManager.registerListener(new SensorEventListener() {
                                            @Override
                                            public void onSensorChanged(SensorEvent event) {
                                                try {
                                                    float mProximiny = event.values[0];
                                                    boolean isSpeaker = false;
                                                    if (mProximiny >= sensor.getMaximumRange()) {
                                                        isSpeaker = true;
                                                        changeAdapterType(context,isSpeaker);
                                                    } else {
                                                        isSpeaker = false;
                                                        changeAdapterType(context,isSpeaker);
                                                    }
                                                } catch (Exception ex) {
                                                    ex.printStackTrace();
                                                }
                                            }

                                            @Override
                                            public void onAccuracyChanged(Sensor sensor, int accuracy) {

                                            }
                                        }, sensor,
                SensorManager.SENSOR_DELAY_NORMAL);
    }

    //切换声筒或听筒
    private void changeAdapterType(Context context,boolean on) {
        ((Activity) context).setVolumeControlStream(AudioManager.STREAM_SYSTEM);
        onPause();
        mAudioManager = (AudioManager) context.getSystemService(Service.AUDIO_SERVICE);
        if (on) {
            //扩音声筒
            mAudioManager.setMicrophoneMute(false);
            mAudioManager.setSpeakerphoneOn(true);
            mAudioManager.setMode(AudioManager.MODE_NORMAL);
            Log.e("audio", "changeAdapterType: 当前为扩音模式");
        } else {
            //耳麦听筒
            mAudioManager.setMicrophoneMute(true);
            mAudioManager.setSpeakerphoneOn(false);
            mAudioManager.setMode(AudioManager.MODE_IN_CALL);
            mAudioManager.setMode(AudioManager.MODE_IN_COMMUNICATION);
            Log.e("audio", "changeAdapterType: 当前为耳麦模式");
        }
        resume();
    }

    public void onPause(){
        if (mMediaPlayer != null && mMediaPlayer.isPlaying()){
            mMediaPlayer.pause();
            isPause = true;
        }
    }
    public void resume(){
        if (mMediaPlayer != null && isPause){
            mMediaPlayer.start();
            isPause = false;
        }
    }

    public void stop(){
        if (mMediaPlayer != null){
            mMediaPlayer.stop();
            isPause = false;
        }
    }
    public void setVolume(float leftVolume,float rightVolume){
        mMediaPlayer.setVolume(leftVolume,rightVolume);
    }

    public void release(){
        if (mMediaPlayer != null){
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
    }


    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public MediaPlayer.OnCompletionListener getOnCompletionListener() {
        return mOnCompletionListener;
    }

    public void setOnCompletionListener(MediaPlayer.OnCompletionListener onCompletionListener) {
        mOnCompletionListener = onCompletionListener;
    }

    public static boolean isPause() {
        return isPause;
    }

    public static void setIsPause(boolean isPause) {
        MediaManager.isPause = isPause;
    }

    public V8Function getListener() {
        return listener;
    }

    public void setListener(V8Function listener) {
        this.listener = listener;
    }
}
