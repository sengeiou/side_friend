//package com.zk.tool.zkapp_call.old;
//
//import android.app.Activity;
//import android.content.Intent;
//import android.os.Bundle;
//import android.support.annotation.Nullable;
//import android.util.Log;
//import android.view.GestureDetector;
//import android.view.MotionEvent;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ImageView;
//import android.widget.RelativeLayout;
//import android.widget.TextView;
//
//import com.bumptech.glide.Glide;
//import com.eclipsesource.v8.V8Function;
//import com.tencent.callsdk.ILVBCallMemberListener;
//import com.tencent.callsdk.ILVCallConstants;
//import com.tencent.callsdk.ILVCallListener;
//import com.tencent.callsdk.ILVCallManager;
//import com.tencent.callsdk.ILVCallOption;
//import com.tencent.ilivesdk.ILiveConstants;
//import com.tencent.ilivesdk.ILiveSDK;
//import com.tencent.ilivesdk.core.ILiveLoginManager;
//import com.tencent.ilivesdk.view.AVRootView;
//import com.tencent.ilivesdk.view.AVVideoView;
//import com.zankong.tool.zkapp.ZKToolApi;
//import com.zankong.tool.zkapp.util.Util;
//import com.zankong.tool.zkapp.views.ZKImgView;
//
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Timer;
//import java.util.TimerTask;
//
//
//
///**
// * 通话界面
// */
//public class CallActivity extends Activity implements ILVCallListener{
//    private String mHostId;
//    private int mCallId;
//    private int mCallType;
//    private int mBeautyRate;
//    private AVRootView mAVRootView ;
//    private RelativeLayout mButtons;
//    private String userInfo;
//    private TextView name,hint;
//    private ImageView img;
//    private HashMap <Integer,String> mHashMap;
//
//
//    private int mStreamId;
//    private SoundPoolManager mSoundPoolManager;
//    private boolean isShowView = true,isAccept;
//    Timer timer;
//
//    private ArrayList<OnReleaseListener> mOnReleaseListeners;
//    public interface OnReleaseListener{
//        void release();
//    }
//    public void addOnReleaseListener(OnReleaseListener onReleaseListener){
//        mOnReleaseListeners.add(onReleaseListener);
//    }
//    public void removeOnReleaseListener(OnReleaseListener onReleaseListener){
//        for (OnReleaseListener releaseListener : mOnReleaseListeners) {
//            if (releaseListener == onReleaseListener){
//                mOnReleaseListeners.remove(releaseListener);
//            }
//        }
//    }
//
//
//    private ILVBCallMemberListener mILVBCallMemberListener = new ILVBCallMemberListener() {
//        @Override
//        public void onCameraEvent(String s, boolean b) {
//            Log.d("onCameraEvent",s+b);
//        }
//
//        @Override
//        public void onMicEvent(String s, boolean b) {
//            Log.d("onMicEvent",s+b);
//        }
//    };
//
//    private void initView(){
//        mAVRootView = (AVRootView) findViewById(R.id.av_root_view);
//        mButtons = (RelativeLayout) findViewById(R.id.buttons);
//        name = (TextView) findViewById(R.id.name);
//        hint = (TextView) findViewById(R.id.hint );
//        img = (ImageView) findViewById(R.id.img);
//    }
//    @Override
//    protected void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.item_call_video);
//        timer = new Timer();
//        mHashMap = new HashMap<>();
//        mHashMap.put(ILVCallConstants.CALL_TYPE_AUDIO,"音频");
//        mHashMap.put(ILVCallConstants.CALL_TYPE_VIDEO,"视频");
//        initView();
//        mOnReleaseListeners = new ArrayList<>();
//        ILVCallManager.getInstance().addCallListener(this);
//        Intent intent = getIntent();
//        mHostId = intent.getStringExtra("HostId");
//        mCallType = intent.getIntExtra("CallType", ILVCallConstants.CALL_TYPE_VIDEO);
//        mCallId = intent.getIntExtra("CallId", 0);
//        userInfo = intent.getStringExtra("userInfo");
//        ILVCallOption option = new ILVCallOption(mHostId)
//                .callTips("zk")
//                .customParam(userInfo)
//                .setMemberListener(mILVBCallMemberListener)
//                .setCallType(mCallType);
//        if (0 == mCallId) { // 发起呼叫
//            List<String> nums = intent.getStringArrayListExtra("CallNumbers");
//            if (nums.size() > 1){
//                mCallId = ILVCallManager.getInstance().makeMutiCall(nums, option);
//            }else{
//                mCallId = ILVCallManager.getInstance().makeCall(nums.get(0), option);
//            }
//            try {
//                JSONObject jsonObject = new JSONObject(userInfo);
//                Log.d("callActivity",jsonObject.toString());
//                name.setText(jsonObject.optString("name"));
//                Glide.with(this).load(ZKImgView.getUrl(jsonObject.optString("img"))).into(img);
////                Picasso.with(this).load(jsonObject.optString("img")).transform(new RangleTransForm(20)).resize(Util.dip2px(this,90),Util.dip2px(this,90)).error(R.drawable.load_01_09).placeholder(R.drawable.load_01_09).into(img);
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//            CallMake callMake = new CallMake(this,mCallId);
//            mButtons.addView(callMake.getView(), ViewGroup.LayoutParams.MATCH_PARENT
//                    ,ViewGroup.LayoutParams.WRAP_CONTENT);
//        }else{  // 接听呼叫
//            CallAccept callAccept = new CallAccept(this,mCallId,option);
//            mButtons.addView(callAccept.getView(), ViewGroup.LayoutParams.MATCH_PARENT
//                    ,ViewGroup.LayoutParams.WRAP_CONTENT);
//            mAVRootView.renderMySelf(true);
//            try {
//                JSONObject jsonObject = new JSONObject(userInfo);
//                hint.setText("邀请您进行"+mHashMap.get(mCallType)+"通话...");
//                name.setText(jsonObject.optString("myName"));
//                Glide.with(this).load(ZKImgView.getUrl(jsonObject.optString("myImg"))).into(img);
////                Picasso.with(this).load(jsonObject.optString("myImg")).transform(new RangleTransForm(Util.dip2px(this,10))).resize(Util.dip2px(this,90),Util.dip2px(this,90)).error(R.drawable.load_01_09).placeholder(R.drawable.load_01_09).into(img);
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//        }
//        ILiveLoginManager.getInstance().setUserStatusListener(new ILiveLoginManager.TILVBStatusListener() {
//            @Override
//            public void onForceOffline(int error, String message) {
//                Log.d("setUserStatusListener",error+message);
//                finish();
//            }
//        });
//        mSoundPoolManager = SoundPoolManager.getInstance();
//        mSoundPoolManager.play(R.raw.call_live_weixin, new SoundPoolManager.OnLoadCompleteListener() {
//            @Override
//            public void invoke(int streamId) {
//                mStreamId = streamId;
//            }
//        });
//        //avRootView.setAutoOrientation(false);
//        ILVCallManager.getInstance().initAvView(mAVRootView);
//    }
//
//
//
//
//    @Override
//    protected void onResume() {
//        ILVCallManager.getInstance().onResume();
//        if (mSoundPoolManager!= null){
//            mSoundPoolManager.resume(mStreamId);
//        }
//        super.onResume();
//    }
//
//    @Override
//    protected void onPause() {
//        ILVCallManager.getInstance().onPause();
//        if (mSoundPoolManager!= null){
//            mSoundPoolManager.pause(mStreamId);
//        }
//        super.onPause();
//    }
//
//    @Override
//    protected void onDestroy() {
//        ILVCallManager.getInstance().removeCallListener(this);
//        ILVCallManager.getInstance().onDestory();
//        if (timer != null){
//            timer.cancel();
//            timer = null;
//        }
//        if (mSoundPoolManager!= null){
//            mSoundPoolManager.release();
//        }
//        if (ZKToolApi.getInstance().getCallBacks().containsKey(CallManager.CALL_RESOLVE)){
//            ZKToolApi.getInstance().getCallBacks().get(CallManager.CALL_RESOLVE).release();
//            ZKToolApi.getInstance().getCallBacks().remove(CallManager.CALL_RESOLVE);
//        }
//        if (ZKToolApi.getInstance().getCallBacks().containsKey(CallManager.CALL_REJECT)){
//            ((V8Function)ZKToolApi.getInstance().getCallBacks().get(CallManager.CALL_REJECT)).call(null);
//            ZKToolApi.getInstance().getCallBacks().get(CallManager.CALL_REJECT).release();
//            ZKToolApi.getInstance().getCallBacks().remove(CallManager.CALL_REJECT);
//        }
//        for (OnReleaseListener onReleaseListener : mOnReleaseListeners) {
//            onReleaseListener.release();
//        }
//        super.onDestroy();
//    }
//
//    @Override
//    public void onBackPressed() {
//        if (isAccept){
//            ILVCallManager.getInstance().endCall(mCallId);
//        }else {
//            ILVCallManager.getInstance().rejectCall(mCallId);
//        }
//        finish();
//    }
//
//
//
//
//
//
//
//
//    @Override
//    public void onCallEstablish(int callId) {
////            btnEndCall.setVisibility(View.VISIBLE);
//        if (ZKToolApi.getInstance().getCallBacks().containsKey(CallManager.CALL_RESOLVE)){
//            ((V8Function) ZKToolApi.getInstance().getCallBacks().get(CallManager.CALL_RESOLVE)).call(null);
//            ZKToolApi.getInstance().getCallBacks().remove(CallManager.CALL_RESOLVE);
//        }
//        if (mSoundPoolManager!= null){
//            mSoundPoolManager.stop(mStreamId);
//        }
//        isAccept = true;
//
//
//
//
//        hint.setVisibility(View.GONE);
//        if(mCallType == ILVCallConstants.CALL_TYPE_AUDIO){
//            mButtons.removeAllViews();
//            mButtons.addView(new Calling_audio(CallActivity.this,callId).getView(),ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
//            return;
//        }
//
//
//
//        name.setVisibility(View.GONE);
//        img.setVisibility(View.GONE);
//        mButtons.removeAllViews();
//        mButtons.addView(new Calling_video(CallActivity.this,callId).getView(),ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
//        mAVRootView.swapVideoView(0, 1);
//        // 设置点击小屏切换及可拖动
//        for (int i=1; i<ILiveConstants.MAX_AV_VIDEO_NUM; i++){
//            final int index = i;
//            AVVideoView minorView = mAVRootView.getViewByIndex(i);
//            if (ILiveLoginManager.getInstance().getMyUserId().equals(minorView.getIdentifier())){
//                minorView.setMirror(true);      // 本地镜像
//            }
//            minorView.setDragable(true);    // 小屏可拖动
//            minorView.setGestureListener(new GestureDetector.SimpleOnGestureListener(){
//                @Override
//                public boolean onSingleTapConfirmed(MotionEvent e) {
//                    mAVRootView.swapVideoView(0, index);     // 与大屏交换
//                    return false;
//                }
//            });
//        }
//        if(false){
//            ILiveSDK.getInstance().getAvVideoCtrl().inputBeautyParam(9.0f * 100 / 100.0f);
//        }
//        timer.schedule(new TimerTask() {
//            @Override
//            public void run() {
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        mButtons.setVisibility(View.GONE);
//                        isShowView = !isShowView;
//                        timer.cancel();
//                    }
//                });
//            }
//        },3*1000);
//        mAVRootView.getViewByIndex(0).setGestureListener(new GestureDetector.SimpleOnGestureListener(){
//            @Override
//            public boolean onSingleTapConfirmed(MotionEvent e) {
//                if (isShowView){
//                    mButtons.setVisibility(View.GONE);
//                    timer.cancel();
//                }else {
//                    mButtons.setVisibility(View.VISIBLE);
//                    timer = new Timer();
//                    timer.schedule(new TimerTask() {
//                        @Override
//                        public void run() {
//                            runOnUiThread(new Runnable() {
//                                @Override
//                                public void run() {
//                                    mButtons.setVisibility(View.GONE);
//                                    isShowView = !isShowView;
//                                    timer.cancel();
//                                }
//                            });
//                        }
//                    },3*1000);
//                }
//                isShowView = !isShowView;
//                return false;
//            }
//        });
//    }
//
//    @Override
//    public void onCallEnd(int callId, int errCode, String errMsg) {
//        Log.d("onCallEnd 判断是否有回调",callId+"\n"+errCode+"\n"+errMsg);
//        switch (errCode){
//            case 1:
//                Util.showT("您取消了本次"+mHashMap.get(mCallType)+"通话");
//                break;
//            case 2:
//                Util.showT("对方长时间未接听");
//                hint.setText("对方长时间未接听");
//                break;
//            case 3:
//                if (errMsg.equals("Remote cancel")){
//                    Util.showT("对方拒绝接听");
//                }else if (errMsg.equals("local reject")){
//                    Util.showT("拒绝接听本次通话");
//                }
//                break;
//            case 4:
//                if (errMsg.equals("Remote cancel")){
//                    Util.showT("对方终止了通话");
//                }else if (errMsg.equals("User Cancel")){
//                    Util.showT("您终止了通话");
//                }
//                break;
//        }
//        Log.d("onCallEnd",callId+"\n"+errCode+"\n"+errMsg);
//        finish();
//    }
//
//    @Override
//    public void onException(int callId, int errCode, String errMsg) {
//        switch (errCode){
//            case 6011:
//                Util.showT("对方没在线");
//                break;
//            default:
//                Util.showT("异常关闭");
//        }
//        Log.d("onException",callId+"\n"+errCode+"\n"+errMsg);
//    }
//}
