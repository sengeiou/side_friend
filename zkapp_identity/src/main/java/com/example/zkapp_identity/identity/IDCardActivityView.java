package com.example.zkapp_identity.identity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.baidu.ocr.sdk.OCR;
import com.baidu.ocr.sdk.OnResultListener;
import com.baidu.ocr.sdk.exception.OCRError;
import com.baidu.ocr.sdk.model.AccessToken;
import com.baidu.ocr.sdk.model.IDCardParams;
import com.baidu.ocr.sdk.model.IDCardResult;
import com.baidu.ocr.ui.camera.CameraActivity;
import com.baidu.ocr.ui.camera.CameraNativeHelper;
import com.baidu.ocr.ui.camera.CameraView;
import com.eclipsesource.v8.V8Function;
import com.eclipsesource.v8.V8Object;
import com.example.zkapp_identity.R;
import com.example.zkapp_identity.identity_face.model.Bean;
import com.example.zkapp_identity.identity_face.utils.FaceMatch;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.makeramen.roundedimageview.RoundedImageView;
import com.zankong.tool.zkapp.ZKToolApi;
import com.zankong.tool.zkapp.activity.ZKActivity;
import com.zankong.tool.zkapp.activity.ZKNaiveActivity;
import com.zankong.tool.zkapp.document.ZKDocument;
import com.zankong.tool.zkapp.util.Util;
import com.zankong.tool.zkapp.v8fn.fetch_pck.Fetch;
import com.zankong.tool.zkapp.v8fn.fetch_pck.ServerAgent;
import com.zankong.tool.zkapp.views.ZKViewAgent;
import com.zankong.tool.zkapp.views.face.Face;

import org.dom4j.Attribute;
import org.dom4j.Element;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
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

public class IDCardActivityView extends ZKViewAgent {
    private static final int REQUEST_CODE_PICK_IMAGE_FRONT = 201;
    private static final int REQUEST_CODE_PICK_IMAGE_BACK = 202;
    private static final int REQUEST_CODE_CAMERA = 102;
    private boolean hasGotToken = false;
    private EditText tv_name;
    private EditText tv_id;
    private RoundedImageView card_01;
    private RoundedImageView card_02;
    private int permissionCheck;
    private AlertDialog.Builder alertDialog;
    private Bitmap bitmap;
    private TextView tv_next;
    private ZKDocument zkDocument;
    private V8Object v8Object;
    private V8Function locationcurrent;
    private MyHandler mHandler;

    public IDCardActivityView(ZKDocument zkDocument, Element element) {
        super(zkDocument, element);
        this.zkDocument = zkDocument;
    }

    @Override
    public void initView(ViewGroup viewGroup) {
        setContentView(R.layout.id_card_layout);
        alertDialog = new AlertDialog.Builder(getContext());
        mHandler = new MyHandler(((ZKActivity) getContext()));
        if (v8Object == null) {
            v8Object = new V8Object(ZKToolApi.runtime);
        }
        tv_name = (EditText) findViewById(R.id.tv_name);
        tv_id = (EditText) findViewById(R.id.tv_id);
        card_01 = (RoundedImageView) findViewById(R.id.img_id_card_01);
        card_02 = (RoundedImageView) findViewById(R.id.img_id_card_02);
        tv_next = (TextView) findViewById(R.id.tv_next);
        tv_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!"".equals(tv_id.getText().toString()) && !"".equals(tv_name.getText().toString())) {
                    v8Object.add("id", tv_id.getText().toString());
                    v8Object.add("name", tv_name.getText().toString());
                    zkDocument.invokeWithContext(locationcurrent, v8Object);
                } else {
                    tv_id.setText("");
                    tv_name.setText("");
                    alertText("", "身份证号或姓名出现错误");
                }
            }
        });
        permissionCheck = ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_PHONE_STATE);
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions((Activity) getContext(), new String[]{Manifest.permission.READ_PHONE_STATE}, 1);
        } else {
            initAccessToken();
        }
        card_01.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), CameraActivity.class);
                intent.putExtra(CameraActivity.KEY_OUTPUT_FILE_PATH,
                        FileUtil.getSaveFile(getContext()).getAbsolutePath());
                intent.putExtra(CameraActivity.KEY_CONTENT_TYPE, CameraActivity.CONTENT_TYPE_ID_CARD_FRONT);
                ((ZKActivity) getContext()).startActivityForResult(intent, REQUEST_CODE_CAMERA);
            }
        });
        card_02.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), CameraActivity.class);
                intent.putExtra(CameraActivity.KEY_OUTPUT_FILE_PATH,
                        FileUtil.getSaveFile(getContext()).getAbsolutePath());
                intent.putExtra(CameraActivity.KEY_CONTENT_TYPE, CameraActivity.CONTENT_TYPE_ID_CARD_BACK);
                ((ZKActivity) getContext()).startActivityForResult(intent, REQUEST_CODE_CAMERA);
            }
        });
        //  初始化本地质量控制模型,释放代码在onDestory中
        //  调用身份证扫描必须加上 intent.putExtra(CameraActivity.KEY_NATIVE_MANUAL, true); 关闭自动初始化和释放本地模型
        CameraNativeHelper.init(getContext(), OCR.getInstance(getContext()).getLicense(),
                new CameraNativeHelper.CameraNativeInitCallback() {
                    @Override
                    public void onError(int errorCode, Throwable e) {
                        String msg;
                        switch (errorCode) {
                            case CameraView.NATIVE_SOLOAD_FAIL:
                                msg = "加载so失败，请确保apk中存在ui部分的so";
                                break;
                            case CameraView.NATIVE_AUTH_FAIL:
                                msg = "授权本地质量控制token获取失败";
                                break;
                            case CameraView.NATIVE_INIT_FAIL:
                                msg = "本地质量控制";
                                break;
                            default:
                                msg = String.valueOf(errorCode);
                        }
                        //  alertText("本地质量控制初始化错误，错误原因",msg);
                    }
                });
    }

    @Override
    public void fillData(Element selfElement) {
        int ret = ActivityCompat.checkSelfPermission(getContext(), Manifest.permission
                .READ_EXTERNAL_STORAGE);
        if (ret != PackageManager.PERMISSION_GRANTED) {
            ((ZKActivity) getContext()).requestRuntimePermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, new ZKNaiveActivity.PermissionListener() {
                @Override
                public void granted() {
                }

                @Override
                public void denied(List<String> deniedList) {
                }
            });
        }
        ((ZKActivity) getContext()).setResultListener(new ZKNaiveActivity.OnResultListener() {
            @Override
            public void result(int requestCode, int resultCode, @Nullable Intent data) {
                if (requestCode == REQUEST_CODE_CAMERA && resultCode == Activity.RESULT_OK) {
                    if (data != null) {
                        String contentType = data.getStringExtra(CameraActivity.KEY_CONTENT_TYPE);
                        String filePath = FileUtil.getSaveFile(getContext()).getAbsolutePath();
                        if (!TextUtils.isEmpty(contentType)) {
                            if (CameraActivity.CONTENT_TYPE_ID_CARD_FRONT.equals(contentType)) {
                                recIDCard(IDCardParams.ID_CARD_SIDE_FRONT, filePath);
                            } else if (CameraActivity.CONTENT_TYPE_ID_CARD_BACK.equals(contentType)) {
                                recIDCard(IDCardParams.ID_CARD_SIDE_BACK, filePath);
                            }
                        }
                    }
                }
            }
        });

        for (Attribute attribute : selfElement.attributes()) {
            String value = attribute.getValue();
            switch (attribute.getName()) {
                case "onClick":
                    locationcurrent = zkDocument.genContextFn(value);
                    break;
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (bitmap != null && bitmap.isRecycled()) {
            bitmap.recycle();
            bitmap = null;
        }
    }

    @Override
    public Object getResult() {
        return null;
    }

    public static Bitmap getLoacalBitmap(String url) {
        try {
            FileInputStream fis = new FileInputStream(url);
            return BitmapFactory.decodeStream(fis);  ///把流转化为Bitmap图片        

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 以license文件方式初始化
     */
    private void initAccessToken() {
        OCR.getInstance(getContext()).initAccessToken(new OnResultListener<AccessToken>() {
            @Override
            public void onResult(AccessToken accessToken) {
                String token = accessToken.getAccessToken();
                hasGotToken = true;
            }

            @Override
            public void onError(OCRError error) {
                error.printStackTrace();
            }
        }, getContext());
    }

    private void recIDCard(String idCardSide, String filePath) {
        IDCardParams param = new IDCardParams();
        param.setImageFile(new File(filePath));
        // 设置身份证正反面
        param.setIdCardSide(idCardSide);
        // 设置方向检测
        param.setDetectDirection(true);
        // 设置图像参数压缩质量0-100, 越大图像质量越好但是请求时间越长。 不设置则默认值为20
        param.setImageQuality(30);
        OCR.getInstance(getContext()).recognizeIDCard(param, new OnResultListener<IDCardResult>() {
            @Override
            public void onResult(IDCardResult result) {
                if (result != null) {
                    if (idCardSide.equals(IDCardParams.ID_CARD_SIDE_FRONT)) {
                        if ((result.getDirection() + "").equals("0") && (result.getWordsResultNumber() + "").equals("6")
                                && !"".equals(result.getAddress()) && !"".equals(result.getBirthday()) && !"".equals(result.getName())
                                && !"".equals(result.getGender()) && !"".equals(result.getEthnic())) {
                            tv_id.setText("" + result.getIdNumber());
                            tv_name.setText("" + result.getName());
                            bitmap = getLoacalBitmap(filePath);
                            card_01.setImageBitmap(bitmap);
                            v8Object.add("cardFront", filePath);
                            Log.e("ddddddddddddd2",result.toString()+"");
                            upImgData(filePath,idCardSide);
                        } else {
                            alertText("", "图片扫描失败，请重新上传");
                        }
                    } else if (idCardSide.equals(IDCardParams.ID_CARD_SIDE_BACK)) {
                        if (!"".equals(result.getSignDate()) && !"".equals(result.getExpiryDate()) && !"".equals(result.getIssueAuthority())) {
                            bitmap = getLoacalBitmap(filePath);
                            card_02.setImageBitmap(bitmap);
                            v8Object.add("cardBack", filePath);
                            v8Object.add("signDate", result.getSignDate() + "");
                            v8Object.add("expiryDate", result.getExpiryDate() + "");
                            v8Object.add("issueAuthority", result.getIssueAuthority() + "");

                            Log.e("ddddddddddddd",result.toString()+"");
                            upImgData(filePath,idCardSide);
                        } else {
                            alertText("", "图片扫描失败，请重新上传");
                        }
                    }
                }
            }

            @Override
            public void onError(OCRError error) {
                alertText("", error.getMessage());
            }
        });
    }

    private void alertText(final String title, final String message) {
        alertDialog.setTitle(title)
                .setMessage(message)
                .setPositiveButton("确定", null)
                .show();
    }
    private void upImgData(String filePath, String flag) {
        Log.e("dddddddddd3",filePath+"===="+flag);
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
                if (flag.equals("front")) {
                    message.what = 0;
                } else if (flag.equals("back")) {
                    message.what = 1;
                }
                Bundle bundle = new Bundle();
                bundle.putString("url", beans.get(0).getUrl() + "");
                message.setData(bundle);
                mHandler.sendMessage(message);
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
                switch (msg.what) {
                    case 0:
                        Bundle data = msg.getData();
                        String url = data.getString("url");
                        Log.e("aaaaaaaaaaaaaaaa55",url+"");
                        break;
                    case 1:
                        Bundle data1 = msg.getData();
                        String url1 = data1.getString("url");
                        Log.e("aaaaaaaaaaaaaaaa99",url1+"");
                        break;
                }
            }
        }
    }
    
    
   
}
