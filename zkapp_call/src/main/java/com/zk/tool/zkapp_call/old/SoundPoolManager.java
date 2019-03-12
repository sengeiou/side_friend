//package com.zk.tool.zkapp_call.old;
//
//import android.media.AudioAttributes;
//import android.media.AudioManager;
//import android.media.SoundPool;
//
//import com.zankong.tool.zkapp.ZKToolApi;
//
///**
// * Created by YF on 2018/3/2.
// */
//
//public class SoundPoolManager {
//    private SoundPool mSoundPool;
//    private static volatile SoundPoolManager mSoundPoolManager;
//    public static SoundPoolManager getInstance(){
//        if (mSoundPoolManager == null){
//            synchronized (SoundPoolManager.class){
//                if (mSoundPoolManager == null) {
//                    mSoundPoolManager = new SoundPoolManager();
//                }
//            }
//        }
//        return mSoundPoolManager;
//    }
//    public interface OnLoadCompleteListener{
//        void invoke(int streamId);
//    }
//    public int play(int rawId,OnLoadCompleteListener listener){
//        if (mSoundPool == null){
//            SoundPool.Builder builder = null;
//            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
//                builder = new SoundPool.Builder();
//                builder.setMaxStreams(1);
//                AudioAttributes.Builder attrBuilder = new AudioAttributes.Builder();
//                attrBuilder.setLegacyStreamType(AudioManager.STREAM_SYSTEM);
//                builder.setAudioAttributes(attrBuilder.build());
//                mSoundPool = builder.build();
//            }else {
//                mSoundPool = new SoundPool(1, AudioManager.STREAM_SYSTEM,5);
//            }
//        }
//        int soundId = mSoundPool.load(ZKToolApi.getInstance().getContext(), rawId, 1);
//        mSoundPool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
//            @Override
//            public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
//                int streamId = soundPool.play(soundId,1,1,0,-1,1);
//                listener.invoke(streamId);
//            }
//        });
//        return soundId;
//    }
//    public void release(){
//        if (mSoundPool != null){
//            mSoundPool.release();
//            mSoundPool = null;
//        }
//    }
//    public void pause(int streamId){
//        if (mSoundPool !=null) {
//            mSoundPool.pause(streamId);
//        }
//    }
//    public void resume(int streamId){
//        if (mSoundPool!= null){
//            mSoundPool.resume(streamId);
//        }
//    }
//    public void stop(int streamId){
//        if (mSoundPool != null){
//            mSoundPool.stop(streamId);
//        }
//    }
//    public void setVolume(int streamId,float leftVolume,float rightVolume){
//        if (mSoundPool != null){
//            mSoundPool.setVolume(streamId,leftVolume,rightVolume);
//        }
//    }
//
//}
