package com.zk.tool.zkapp_call;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.tencent.ilivesdk.ILiveCallBack;
import com.tencent.ilivesdk.ILiveConstants;
import com.tencent.ilivesdk.ILiveSDK;
import com.tencent.ilivesdk.core.ILiveRoomManager;
import com.tencent.ilivesdk.core.ILiveRoomOption;
import com.tencent.ilivesdk.data.ILiveMessage;
import com.tencent.ilivesdk.data.msg.ILiveCustomMessage;
import com.tencent.ilivesdk.listener.ILiveEventHandler;
import com.tencent.ilivesdk.listener.ILiveMessageListener;
import com.tencent.ilivesdk.view.AVRootView;
import com.zankong.tool.zkapp.sound.SoundPoolManager;
import com.zankong.tool.zkapp.util.Util;
import com.zankong.tool.zkapp.v8fn.ZKLocalStorage;
import com.zankong.tool.zkapp.views.ZKImgView;
import com.zk.tool.zkapp_call.demo.model.MessageObservable;
import com.zk.tool.zkapp_call.demo.view.DlgMgr;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ZKCallActivity extends Activity implements ILiveMessageListener {
    private AVRootView mAVRootView;
    private int roomid = 0;
    private CallUserBean otherUser = null;
    private CallUserBean selfUser = null;
    private boolean bFirstBackPress = true;
    private boolean isCaller = false;
    private RelativeLayout mButtons;
    private String userInfo;
    private TextView name,hint;
    private ImageView img;
    private boolean isEstablish;
    private SoundPoolManager mSoundPoolManager;
    private int mStreamId;
    public final static int REQ_PERMISSION_CODE = 0x1000;
    private ILiveRoomOption mILiveRoomOption;


    public static final String
            REFUSE = "refuse",             //拒绝接听
            CANCEL = "cancel",             //取消拨打
            ESTABLISH = "establish",      //同意接听
            END = "end",                   //挂断电话
            INVITE = "invite";             //邀请


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        checkPermission();
        selfUser = new CallUserBean.CallBuilder()
                .setUid(ZKLocalStorage.mSharedPreferences.getInt("uid",0))
                .setNickname(ZKLocalStorage.mSharedPreferences.getString("nickname",""))
                .setImg(ZKLocalStorage.mSharedPreferences.getString("img",""))
                .setPrivateMapKey(ZKLocalStorage.mSharedPreferences.getString("privateMapKey",""))
                .builder();
        initView();
        String message = getIntent().getStringExtra("message");
        isEstablish = false;
        try {
            assert message != null;
            JSONObject jsonObject = new JSONObject(message);
            roomid = jsonObject.getInt("roomid");
            otherUser = new CallUserBean.CallBuilder()
                    .setUid(jsonObject.optInt("uid"))
                    .setNickname(jsonObject.optString("nickname"))
                    .setImg(jsonObject.optString("img"))
                    .setPrivateMapKey(jsonObject.optString("privateMapKey"))
                    .builder();
            mILiveRoomOption = new ILiveRoomOption()
                    .privateMapKey(otherUser.getPrivateMapKey())
                    .autoCamera(false)
                    .autoMic(true)
                    .controlRole("user");
            isCaller = jsonObject.optBoolean("isCaller");
        } catch (JSONException e) {
            e.printStackTrace();
            Util.log("AAAAAA", "爱是飞洒发对方");
        }
        ILiveSDK.getInstance().addEventHandler(eventHandler);
        MessageObservable.getInstance().addObserver(this);
        name.setText(otherUser.getNickname());
        Glide.with(ZKCallActivity.this).load(ZKImgView.getUrl(otherUser.getImg())).into(img);
        if(isCaller){
            sendMessage(INVITE);
            CallMake callMake = new CallMake(this);
            mButtons.addView(callMake.getView(), ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        }else{
            CallAccept callAccept = new CallAccept(this);
            mButtons.addView(callAccept.getView(), ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        }
        mSoundPoolManager = SoundPoolManager.getInstance();
        mSoundPoolManager.play(R.raw.call_live_weixin, new SoundPoolManager.OnLoadCompleteListener() {
            @Override
            public void invoke(int streamId) {
                mStreamId = streamId;
            }
        });
    }

    private void initView() {
        setContentView(R.layout.item_call_video);
        mAVRootView = findViewById(R.id.av_root_view);
        mButtons = (RelativeLayout) findViewById(R.id.buttons);
        name = (TextView) findViewById(R.id.name);
        hint = (TextView) findViewById(R.id.hint );
        img = (ImageView) findViewById(R.id.img);
    };


    @Override
    protected void onResume() {
        super.onResume();
        if (mSoundPoolManager!= null){
            mSoundPoolManager.resume(mStreamId);
        }
        if (isEstablish && roomid != 0) {
            enterTrtcRoom(roomid);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mSoundPoolManager!= null){
            mSoundPoolManager.pause(mStreamId);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mSoundPoolManager!= null){
            mSoundPoolManager.release();
        }
        isEstablish = false;
        MessageObservable.getInstance().deleteObserver(this);
        ILiveSDK.getInstance().clearEventHandler();
    }

    @Override
    public void onBackPressed() {
        if(isEstablish){
            sendMessage(END);
        }else {
            sendMessage(CANCEL);
            finish();
            return;
        }
        if (bFirstBackPress) {
            bFirstBackPress = false;
            ILiveRoomManager.getInstance().quitRoom();
        }else{
            finish();
        }
    }


    private Context getContext() {
        return isFinishing() ? null : this;
    }

    /**
     * 加入音视频房间
     */
    private void enterTrtcRoom(int roomId) {
//        // Step 3: 设置渲染控件
//        ILiveRoomManager.getInstance().initAvRootView(mAVRootView);
//        customRootView(mAVRootView);     // 定制渲染控件

        // Step 4: 加入音视频房间
        ILiveRoomManager.getInstance().joinRoom(roomId, mILiveRoomOption);
    }

    /**
     * 处理TRTC 事件
     */
    private ILiveEventHandler eventHandler = new ILiveEventHandler() {
        @Override
        public void onForceOffline(String userId, String module, int errCode, String errMsg) {
            Util.log("帐号被踢下线: " + module + "|" + errCode + "|" + errMsg);
            finish();
        }

        @Override
        public void onCreateRoomSuccess(int roomId, String groupId) {
            Util.log("创建房间成功");
        }

        @Override
        public void onCreateRoomFailed(int roomId, String module, int errCode, String errMsg) {
            Util.log("创建房间失败: " + module + "|" + errCode + "|" + errMsg);
        }

        @Override
        public void onJoinRoomSuccess(int roomId, String groupId) {
            Util.log("加入房间成功");
        }

        @Override
        public void onJoinRoomFailed(int roomId, String module, int errCode, String errMsg) {
            if (module.equals(ILiveConstants.Module_IMSDK) && (10010 == errCode || 10015 == errCode)) {
                ILiveRoomManager.getInstance().createRoom(roomId,mILiveRoomOption);
            } else {
                Util.log("加入房间失败: " + module + "|" + errCode + "|" + errMsg);
            }
        }

        @Override
        public void onQuitRoomSuccess(int roomId, String groupId) {
            finish();
        }

        @Override
        public void onQuitRoomFailed(int roomId, String module, int errCode, String errMsg) {
            Util.log("退出房间失败: " + module + "|" + errCode + "|" + errMsg);
        }
    };

    @Override
    public void onNewMessage(ILiveMessage message) {
        Util.log("onNewMessage");
        switch (message.getMsgType()) {
            case ILiveMessage.ILIVE_MSG_TYPE_TEXT:
                break;
            case ILiveMessage.ILIVE_MSG_TYPE_CUSTOM:
                try {
                    JSONObject jsonObject = new JSONObject(new String(((ILiveCustomMessage) message).getData()));
                    Util.log("onNewMessage",jsonObject.toString());
                    String event = jsonObject.optString("event");
                    JSONObject data = jsonObject.optJSONObject("data");
                    if(event!=null){
                        switch (event){
                            case REFUSE://拒绝接听
                                Util.showT("对方拒绝接听");
                                finish();
                                break;
                            case CANCEL:
                                finish();
                                Util.showT("对方取消拨打");
                                break;
                            case END://通话结束
                                Util.showT("通话结束");
                                ILiveRoomManager.getInstance().quitRoom();
                                break;
                            case ESTABLISH://通话建立
                                isEstablish = true;
                                enterTrtcRoom(roomid);
                                mButtons.removeAllViews();
                                mButtons.addView(new Calling_audio(ZKCallActivity.this).getView(),ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
                                if (mSoundPoolManager!= null){
                                    mSoundPoolManager.stop(mStreamId);
                                }
                                break;

                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            default:
                Util.log("onNewMessage-> message type: " + message.getMsgType());
                break;
        }
    }

//============================主动发送消息===================================

    //取消拨打,发送消息,并退出房间
    public void cancel(){
        sendMessage(CANCEL);
        if(!isEstablish){
            finish();
        }else {
            ILiveRoomManager.getInstance().quitRoom();
        }
    }
    //结束聊天,发送消息,并退出房间
    public void end(){
        sendMessage(END);
        if(isEstablish){
            ILiveRoomManager.getInstance().quitRoom();
        }else {
            finish();
        }
    }
    //接通聊天,发送消息
    public void establish(){
        isEstablish = true;
        enterTrtcRoom(roomid);
        sendMessage(ESTABLISH);
        mButtons.removeAllViews();
        mButtons.addView(new Calling_audio(ZKCallActivity.this).getView(),ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        if (mSoundPoolManager!= null){
            mSoundPoolManager.stop(mStreamId);
        }
    }
    //拒绝接听,发送消息,并退出房间
    public void refuse(){
        sendMessage(REFUSE);
        if(!isEstablish){
            finish();
        }else {
            ILiveRoomManager.getInstance().quitRoom();
        }
    };
    //邀请通话,发送消息
    public void invite() {}

    private void sendMessage(String event){
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("event",event);
            JSONObject data = new JSONObject();
            data.put("nickname",selfUser.getNickname());
            data.put("houseId",roomid);
            data.put("img",selfUser.getImg());
            data.put("uid",selfUser.getUid());
            jsonObject.put("data",data);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        ILiveCustomMessage customMessage = new ILiveCustomMessage(jsonObject.toString().getBytes(),null);
        ILiveRoomManager.getInstance().sendC2COnlineMessage("zk_pangyou_uid_" + otherUser.getUid(), customMessage, new ILiveCallBack() {
            @Override
            public void onSuccess(Object data) {
                Util.log("sendC2CMessage",event+" 发送成功");
            }

            @Override
            public void onError(String module, int errCode, String errMsg) {
                Util.log("sendC2CMessage",event+" 发送失败");
            }
        });
    }





    //////////////////////////////////    动态权限申请   ////////////////////////////////////////

    /** 动态权限申请 */
    private boolean checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            List<String> permissions = new ArrayList<>();
            if (PackageManager.PERMISSION_GRANTED != ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                permissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
            }
            if (PackageManager.PERMISSION_GRANTED != ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA)) {
                permissions.add(Manifest.permission.CAMERA);
            }
            if (PackageManager.PERMISSION_GRANTED != ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.RECORD_AUDIO)) {
                permissions.add(Manifest.permission.RECORD_AUDIO);
            }
            if (PackageManager.PERMISSION_GRANTED != ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.READ_PHONE_STATE)) {
                permissions.add(Manifest.permission.READ_PHONE_STATE);
            }
            if (PackageManager.PERMISSION_GRANTED != ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE)) {
                permissions.add(Manifest.permission.READ_EXTERNAL_STORAGE);
            }
            if (permissions.size() != 0) {
                ActivityCompat.requestPermissions(ZKCallActivity.this,permissions.toArray(new String[0]), REQ_PERMISSION_CODE);
                return false;
            }
        }

        return true;
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQ_PERMISSION_CODE:
                for (int ret : grantResults) {
                    if (PackageManager.PERMISSION_GRANTED != ret) {
                        DlgMgr.showMsg(getContext(), "用户没有允许需要的权限，使用可能会受到限制！");
                    }
                }
                break;
            default:
                break;
        }
    }
}
