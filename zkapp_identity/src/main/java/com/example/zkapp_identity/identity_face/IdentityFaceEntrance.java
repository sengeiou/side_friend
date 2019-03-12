package com.example.zkapp_identity.identity_face;

import com.baidu.idl.face.platform.FaceConfig;
import com.baidu.idl.face.platform.FaceEnvironment;
import com.baidu.idl.face.platform.FaceSDKManager;
import com.baidu.idl.face.platform.LivenessTypeEnum;
import com.example.zkapp_identity.R;
import com.zankong.tool.zkapp.activity.ZKActivity;
import com.zankong.tool.zkapp.activity.ZKNaiveActivity;
import com.zankong.tool.zkapp.document.ZKDocument;
import com.zankong.tool.zkapp.views.ZKViewAgent;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.view.ViewGroup;

import org.dom4j.Element;

import java.util.List;

public class IdentityFaceEntrance extends ZKViewAgent {

    private static final String TAG = IdentityFaceEntrance.class.getSimpleName();

    private Dialog mDefaultDialog;
    private int[] mDataset = new int[]{
            R.string.main_item_face_live,
            R.string.main_item_face_detect
    };

    public IdentityFaceEntrance(ZKDocument zkDocument, Element element) {
        super(zkDocument, element);
    }


    /**
     * 初始化SDK
     */
    private void initLib() {
        // 为了android和ios 区分授权，appId=appname_face_android ,其中appname为申请sdk时的应用名
        // 应用上下文
        // 申请License取得的APPID
        // assets目录下License文件名
        FaceSDKManager.getInstance().initialize(getContext(), Config.licenseID, Config.licenseFileName);
        // setFaceConfig();
    }

    private void setFaceConfig() {
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

    private void startItemActivity(Class itemClass) {
        setFaceConfig();
        ((ZKActivity) getContext()).startActivity(new Intent(getContext(), itemClass));
    }
    @Override
    public void initView(ViewGroup viewGroup) {
        setContentView(R.layout.activity_main);
        // 根据需求添加活体动作
        ExampleApplication.livenessList.clear();
        ExampleApplication.livenessList.add(LivenessTypeEnum.Eye);
        ExampleApplication.livenessList.add(LivenessTypeEnum.Mouth);
        ExampleApplication.livenessList.add(LivenessTypeEnum.HeadUp);
        ExampleApplication.livenessList.add(LivenessTypeEnum.HeadDown);
        ExampleApplication.livenessList.add(LivenessTypeEnum.HeadLeft);
        ExampleApplication.livenessList.add(LivenessTypeEnum.HeadRight);
        ExampleApplication.livenessList.add(LivenessTypeEnum.HeadLeftOrRight);
        initLib();
    }

    @Override
    public void fillData(Element selfElement) {
        int ret = ActivityCompat.checkSelfPermission(getContext(),Manifest.permission.CAMERA);
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
        startItemActivity(FaceLivenessExpActivity.class);
        ((ZKActivity) getContext()).finish();
    }

    @Override
    public Object getResult() {
        return null;
    }
    
    protected void showMessageDialog(String title, String message) {
        if (mDefaultDialog == null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setTitle(title).
                    setMessage(message).
                    setNegativeButton("ok",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    mDefaultDialog.dismiss();
                                    ((ZKActivity) getContext()).finish();
                                }
                            });
            mDefaultDialog = builder.create();
            mDefaultDialog.setCancelable(true);
        }
        mDefaultDialog.dismiss();
        mDefaultDialog.show();
    }
}
