package com.example.zkapp_identity.identity_face;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.text.TextUtils;
import android.util.Log;

import com.baidu.idl.face.platform.FaceConfig;
import com.baidu.idl.face.platform.FaceEnvironment;
import com.baidu.idl.face.platform.FaceSDKManager;
import com.baidu.idl.face.platform.FaceStatusEnum;
import com.baidu.idl.face.platform.LivenessTypeEnum;
import com.baidu.idl.face.platform.ui.FaceLivenessActivity;
import com.example.zkapp_identity.identity_face.exception.FaceException;
import com.example.zkapp_identity.identity_face.model.AccessToken;
import com.example.zkapp_identity.identity_face.model.Bean;
import com.example.zkapp_identity.identity_face.model.LivenessVsIdcardResult;
import com.example.zkapp_identity.identity_face.widget.DefaultDialog;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import com.zankong.tool.zkapp.activity.ZKActivity;
import com.zankong.tool.zkapp.activity.ZKNaiveActivity;
import com.zankong.tool.zkapp.util.Util;
import com.zankong.tool.zkapp.v8fn.fetch_pck.Fetch;
import com.zankong.tool.zkapp.v8fn.fetch_pck.ServerAgent;


import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class FaceLivenessExpActivity extends FaceLivenessActivity {
    private DefaultDialog mDefaultDialog;
    private String bestImagePath;
    private boolean waitAccesstoken = true;
    private static String filePath;
    private String accessToken;
    private static Map<String, String> obj;
    private String name,id;

    private MyHandler myHandler;

    private Context getContext(){
        return FaceLivenessExpActivity.this;
    }

    private void  init(){

        // 根据需求添加活体动作
        ExampleApplication.livenessList.clear();
        ExampleApplication.livenessList.add(LivenessTypeEnum.Eye);
        ExampleApplication.livenessList.add(LivenessTypeEnum.Mouth);
        ExampleApplication.livenessList.add(LivenessTypeEnum.HeadUp);
        ExampleApplication.livenessList.add(LivenessTypeEnum.HeadDown);
        ExampleApplication.livenessList.add(LivenessTypeEnum.HeadLeft);
        ExampleApplication.livenessList.add(LivenessTypeEnum.HeadRight);
        ExampleApplication.livenessList.add(LivenessTypeEnum.HeadLeftOrRight);
        // 为了android和ios 区分授权，appId=appname_face_android ,其中appname为申请sdk时的应用名
        // 应用上下文
        // 申请License取得的APPID
        // assets目录下License文件名
        FaceSDKManager.getInstance().initialize(getContext(), Config.licenseID, Config.licenseFileName);
        // setFaceConfig();

        int ret = ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA);
        int ret1 = ActivityCompat.checkSelfPermission(getContext(),Manifest.permission.ACCESS_NETWORK_STATE);
        if (ret != PackageManager.PERMISSION_GRANTED || ret1 != PackageManager.PERMISSION_GRANTED) {
            ((ZKActivity) getContext()).requestRuntimePermissions(new String[]{Manifest.permission.CAMERA,Manifest.permission.ACCESS_NETWORK_STATE}, new ZKNaiveActivity.PermissionListener() {
                @Override
                public void granted() {
                }
                @Override
                public void denied(List<String> deniedList) {
                }
            });
        }
        FaceConfig config = FaceSDKManager.getInstance().getFaceConfig();
        // SDK初始化已经设置完默认参数（推荐参数），您也根据实际需求进行数值调整
        config.setLivenessTypeList(ExampleApplication.livenessList);
        config.setLivenessRandom(ExampleApplication.isLivenessRandom);
        config.setBlurnessValue(FaceEnvironment.VALUE_BLURNESS);
        config.setBrightnessValue(FaceEnvironment.VALUE_BRIGHTNESS);
        config.setCropFaceValue(FaceEnvironment.VALUE_CROP_FACE_SIZE);
        config.setHeadPitchValue(FaceEnvironment.VALUE_HEAD_PITCH);
        config.setHeadRollValue(FaceEnvironment.VALUE_HEAD_ROLL);
        config.setHeadYawValue(FaceEnvironment.VALUE_HEAD_YAW);
        config.setMinFaceSize(FaceEnvironment.VALUE_MIN_FACE_SIZE);
        config.setNotFaceValue(FaceEnvironment.VALUE_NOT_FACE_THRESHOLD);
        config.setOcclusionValue(FaceEnvironment.VALUE_OCCLUSION);
        config.setCheckFaceQuality(true);
        config.setFaceDecodeNumberOfThreads(2);
        FaceSDKManager.getInstance().setFaceConfig(config);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();


        initAccessToken();

        name = getIntent().getStringExtra("name");
        id = getIntent().getStringExtra("id");
        myHandler = new MyHandler(this);
        if (obj == null){
            obj = new HashMap<>();
        }
    }

    @Override
    public void onLivenessCompletion(FaceStatusEnum status, String message, HashMap<String, String> base64ImageMap) {
        super.onLivenessCompletion(status, message, base64ImageMap);
        if (status == FaceStatusEnum.OK && mIsCompletion) {
            showMessageDialog("活体检测", "检测成功");
            saveImage(base64ImageMap);
        } else if (status == FaceStatusEnum.Error_DetectTimeout ||
                status == FaceStatusEnum.Error_LivenessTimeout ||
                status == FaceStatusEnum.Error_Timeout) {
            showMessageDialog("活体检测", "采集超时");
        }
    }

    private void showMessageDialog(String title, String message) {
        if (mDefaultDialog == null) {
            DefaultDialog.Builder builder = new DefaultDialog.Builder(this);
            builder.setTitle(title).
                    setMessage(message).
                    setNegativeButton("确认",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    mDefaultDialog.dismiss();
                                    finish();
                                }
                            });
            mDefaultDialog = builder.create();
            mDefaultDialog.setCancelable(true);
        }
        mDefaultDialog.dismiss();
        mDefaultDialog.show();
    }

    @Override
    public void finish() {
        super.finish();
    }

    private void saveImage(HashMap<String, String> imageMap) {
        // imageMap 里保存了最佳人脸和各个动作的图片，若对安全要求比较高，可以传多张图片进行在线活体，目前只用最佳人脸进行了在线活体检测
        String bestimageBase64 = imageMap.get("bestImage0");
        Bitmap bmp = base64ToBitmap(bestimageBase64);
        try {
            File file = File.createTempFile("face", ".jpg");
            FileOutputStream outputStream = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.JPEG, 80, outputStream);
            outputStream.close();
            filePath = file.getAbsolutePath();
            Log.e("ttttttttttttt", filePath + "");
            policeVerify(filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 在线活体检测和公安核实需要使用该token，为了防止ak、sk泄露，建议在线活体检测和公安接口在您的服务端请求
    private void initAccessToken() {
        APIService.getInstance().init(getApplicationContext());
        APIService.getInstance().initAccessTokenWithAkSk(new OnResultListener<AccessToken>() {
            @Override
            public void onResult(AccessToken result) {
                if (result != null && !TextUtils.isEmpty(result.getAccessToken())) {
                    waitAccesstoken = false;
                    Log.e("ddddddddddd2", result.toString() + "ssssssssss");
                    accessToken = result.getAccessToken().toString();
                    policeVerify(filePath);
                } else if (result != null) {
                    Log.e("aaaaaaaaaaaaaa1", "在线活体token获取失败");
                } else {
                    Log.e("aaaaaaaaaaaaaa2", "在线活体token获取失败");
                }
            }

            @Override
            public void onError(FaceException error) {
                // TODO 错误处理
                Log.e("aaaaaaaaaaaaaa2", "在线活体token获取失败");
            }
        }, Config.apiKey, Config.secretKey);
    }

    /**
     * 公安接口合并在线活体，调用公安验证接口进行最后的核身比对；公安权限需要在官网控制台提交工单开启
     * 接口地址：https://aip.baidubce.com/rest/2.0/face/v2/person/verify
     * 入参为「姓名」「身份证号」「bestimage」
     * ext_fields 扩展功能。如 faceliveness 表示返回活体值, qualities 表示返回质检测结果
     * quality string 判断质 是否达标。“use” 表示做质 控制,质  好的照 会 直接拒绝
     * faceliveness string 判断活体值是否达标。 use 表示做活体控制,低于活体阈值的 照 会直接拒绝
     * quality_conf和faceliveness_conf 用于指定阈值，超过此分数才调用公安验证，
     *
     * @param filePath
     */
    private void policeVerify(String filePath) {
        if (TextUtils.isEmpty(filePath) || waitAccesstoken) {
            return;
        }
        File file = new File(filePath);
        if (!file.exists()) {
            return;
        }
        Log.e("aaaaaaaaaaa3", "公安身份核实中...");
        APIService.getInstance().policeVerify(name, id, filePath, new
                OnResultListener<LivenessVsIdcardResult>() {
                    @Override
                    public void onResult(LivenessVsIdcardResult result) {
                        if (result != null && result.getScore() >= 80) {
                            Log.e("aaaaaaaaaaa4", "核身成功...");
                            //  delete();
                            Log.e("aaaaaaaaaa9", filePath + "");
                            upImgData(filePath);
                        } else {
                            Log.e("aaaaaaaaaaa6", "核身失败...");
                            Log.e("aaaaaaaaaaa7", "公安验证分数过低..." + result.getScore());
                        }
                    }

                    @Override
                    public void onError(FaceException error) {
                        delete();
                        // TODO 错误处理
                        // 如返回错误码为：216600，则核身失败，提示信息为：身份证号码错误
                        // 如返回错误码为：216601，则核身失败，提示信息为：身份证号码与姓名不匹配
                        Log.e("aaaaaaaaaaa8", "公安身份核实失败" + error.getMessage() + "");
                    }
                }
        );
    }

    private static void delete() {
        File file = new File(filePath);
        if (file.exists()) {
            file.delete();
        }
    }
    private void upImgData(String filePath) {
        Map<String, String> map = new HashMap<>();
        map.put("image", filePath);
        Fetch.put("http://api.zankong.com.cn:2223/uploadImages", new JSONObject(map)).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("aaaaaaaaa99", e.getMessage().toString());
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                ResponseBody body = response.body();
                String string = body.string();
                Gson gson = new Gson();//创建Gson对象
                JsonParser jsonParser = new JsonParser();
                JsonArray jsonElements = jsonParser.parse(string).getAsJsonArray();//获取JsonArray对象
                ArrayList<Bean> beans = new ArrayList<>();
                for (JsonElement bean : jsonElements) {
                    Bean bean1 = gson.fromJson(bean, Bean.class);//解析
                    beans.add(bean1);
                }
                Message message = new Message();
                message.what = 0;
                Bundle bundle = new Bundle();
                bundle.putString("url",beans.get(0).getUrl()+"");
                message.setData(bundle);
                myHandler.sendMessage(message);
            }
        });
    }


    static class MyHandler extends Handler {
        WeakReference<Activity> mWeakReference;
        public MyHandler(Activity activity) {
            mWeakReference = new WeakReference<Activity>(activity);
        }
        @Override
        public void handleMessage(Message msg) {
            final Activity activity = mWeakReference.get();
            if (activity != null) {
                switch (msg.what){
                    case 0:  
                        Bundle data = msg.getData();
                        String url = data.getString("url");
                        delete();
                        identityAuthentication(obj);
                        break;
                    case 1:
                        break;
                }
                
            }
        }
    }

    private static void identityAuthentication(Map<String,String> map){
        ServerAgent.invoke("identity-authentication",new JSONObject(map)).then(res -> {
            Util.log("调用成功", res + "");
            return null;
        });
    }
}
