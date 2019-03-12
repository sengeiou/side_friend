//package com.zk.tool.zkapp_call.old;
//
//import android.content.Context;
//import android.graphics.Color;
//import android.view.LayoutInflater;
//import android.view.MotionEvent;
//import android.view.View;
//import android.widget.Button;
//import android.widget.RelativeLayout;
//import android.widget.TextView;
//
//import com.tencent.callsdk.ILVCallManager;
//import com.tencent.ilivesdk.ILiveConstants;
//import com.zankong.tool.zkapp.util.Util;
//import com.zk.tool.zkapp_call.R;
//
//import java.util.Timer;
//import java.util.TimerTask;
//
///**
// * Created by YF on 2018/2/27.
// */
//
//public class Calling_video {
//    private RelativeLayout mRelativeLayout;
//    private View mView;
//    private Context mContext;
//    private int callId,mCurCameraId = ILiveConstants.FRONT_CAMERA;
//    private Button cancel,mute,camera;
//    private TextView cancel_text,mute_text,camera_text,time_down;
//    private boolean invalid = false,isMicEnable = true,isCameraEnable = true;
//    private float allDownY,allDownX;
//    private Timer mTimer;
//    private int times = 0;
//
//    public void releaseTimer(){
//        if (mTimer!=null){
//            mTimer.cancel();
//            mTimer = null;
//        }
//    }
//
//    public Calling_video(Context context, int callId){
//        mContext = context;
//        this.callId = callId;
//        initView();
//        setOnClick();
//        mTimer = new Timer();
//        mTimer.schedule(new TimerTask() {
//            @Override
//            public void run() {
//                times++;
//                ((CallActivity) mContext).runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        time_down.setText(getTimeStr());
//                    }
//                });
//            }
//        },1000,1000);
//        ((CallActivity) mContext).addOnReleaseListener(new CallActivity.OnReleaseListener() {
//            @Override
//            public void release() {
//                releaseTimer();
//            }
//        });
//    }
//    private String getTimeStr(){
//        int m = times/60;
//        int s = times%60;
//        String str_m = m < 10?"0"+m:m+"";
//        String str_s = s < 10?"0"+s:s+"";
//        return str_m+":"+str_s;
//    }
//
//
//    private void initView(){
//        mView = LayoutInflater.from(mContext).inflate(R.layout.call_video_ing, null);
//        mRelativeLayout = (RelativeLayout) mView.findViewById(R.id.call_make_rel);
//        cancel = (Button) mView.findViewById(R.id.cancel);
//        cancel_text = (TextView) mView.findViewById(R.id.cancel_text);
//        mute = (Button) mView.findViewById(R.id.mute);
//        mute_text = (TextView) mView.findViewById(R.id.mute_text);
//        camera = (Button) mView.findViewById(R.id.camera);
//        camera_text = (TextView) mView.findViewById(R.id.camera_text);
//        time_down = (TextView) mView.findViewById(R.id.time_down);
//    }
//
//    public View getView() {
//        return mView;
//    }
//    private void setOnClick(){
//        cancel.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                switch (event.getAction()){
//                    case MotionEvent.ACTION_DOWN:
//                        invalid = false;
//                        allDownY = event.getY();
//                        allDownX = event.getX();
//                        cancel_text.setTextColor(Color.parseColor("#ff0000"));
//                        break;
//                    case MotionEvent.ACTION_UP:
//                        cancel_text.setTextColor(Color.parseColor("#d6d6d6"));
//                        if (invalid) {
//                            break;
//                        }
//                        ILVCallManager.getInstance().endCall(callId);
//                        break;
//                    case MotionEvent.ACTION_MOVE:
//                        float moveY = event.getY() - allDownY;
//                        float moveX= event.getX() - allDownX;
//                        if ((moveY > Util.dip2px(30) || moveY < -(Util.dip2px(30)))) {
//                            cancel_text.setTextColor(Color.parseColor("#d6d6d6"));
//                            invalid = true;
//                        }
//                        if (moveX > Util.dip2px(0) || moveX < - (Util.dip2px(30))){
//                            cancel_text.setTextColor(Color.parseColor("#d6d6d6"));
//                            invalid = true;
//                        }
//                        break;
//                }
//                return false;
//            }
//        });
//        mute.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                changeMute();
//            }
//        });
////        mute.setOnTouchListener(new View.OnTouchListener() {
////            @Override
////            public boolean onTouch(View v, MotionEvent event) {
////                switch (event.getAction()){
////                    case MotionEvent.ACTION_DOWN:
////                        invalid = false;
////                        allDownY = event.getY();
////                        allDownX = event.getX();
////                        mute_text.setTextColor(Color.parseColor("#ff0000"));
////                        break;
////                    case MotionEvent.ACTION_UP:
////                        mute_text.setTextColor(Color.parseColor("#d6d6d6"));
////                        if (invalid) {
////                            break;
////                        }
////                        ILVCallManager.getInstance().enableMic(!isMicEnable);
////                        isMicEnable = !isMicEnable;
////                        break;
////                    case MotionEvent.ACTION_MOVE:
////                        float moveY = event.getY() - allDownY;
////                        float moveX= event.getX() - allDownX;
////                        if ((moveY > Dom.dip2px(mContext, 30) || moveY < -(Dom.dip2px(mContext, 30)))) {
////                            mute_text.setTextColor(Color.parseColor("#d6d6d6"));
////                            invalid = true;
////                        }
////                        if (moveX > Dom.dip2px(mContext,30) || moveX < - (Dom.dip2px(mContext,30))){
////                            mute_text.setTextColor(Color.parseColor("#d6d6d6"));
////                            invalid = true;
////                        }
////                        break;
////                }
////                return false;
////            }
////        });
//
//        camera.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                switch (event.getAction()){
//                    case MotionEvent.ACTION_DOWN:
//                        invalid = false;
//                        allDownY = event.getY();
//                        allDownX = event.getX();
//                        camera_text.setTextColor(Color.parseColor("#ff0000"));
//                        break;
//                    case MotionEvent.ACTION_UP:
//                        camera_text.setTextColor(Color.parseColor("#d6d6d6"));
//                        if (invalid) {
//                            break;
//                        }
//                        mCurCameraId = (ILiveConstants.FRONT_CAMERA==mCurCameraId) ? ILiveConstants.BACK_CAMERA : ILiveConstants.FRONT_CAMERA;
//                        ILVCallManager.getInstance().switchCamera(mCurCameraId);
//                        break;
//                    case MotionEvent.ACTION_MOVE:
//                        float moveY = event.getY() - allDownY;
//                        float moveX= event.getX() - allDownX;
//                        if ((moveY > Util.dip2px(30) || moveY < -(Util.dip2px(30)))) {
//                            camera_text.setTextColor(Color.parseColor("#d6d6d6"));
//                            invalid = true;
//                        }
//                        if (moveX > Util.dip2px(30) || moveX < - (Util.dip2px(30))){
//                            camera_text.setTextColor(Color.parseColor("#d6d6d6"));
//                            invalid = true;
//                        }
//                        break;
//                }
//                return false;
//            }
//        });
//    }
//    private void changeMute(){
//        if (isMicEnable){
//            ILVCallManager.getInstance().enableMic(false);
//            mute.setBackgroundResource(R.drawable.call_mic_off);
//            mute_text.setText("静音");
//        }else {
//            ILVCallManager.getInstance().enableMic(true);
//            mute.setBackgroundResource(R.drawable.call_speaker_off);
//            mute_text.setText("扬声器");
//        }
//        isMicEnable = !isMicEnable;
//    }
//}
