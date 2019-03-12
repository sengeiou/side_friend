package com.example.zkapp_identity;

import com.baidu.ocr.sdk.OCR;
import com.baidu.ocr.sdk.OnResultListener;
import com.baidu.ocr.sdk.exception.OCRError;
import com.baidu.ocr.sdk.model.AccessToken;
import com.zankong.tool.zkapp.ZKToolApi;
import com.zankong.tool.zkapp.util.Util;

public class test {
    public static void set(){
        OCR.getInstance(ZKToolApi.getInstance().getContext()).initAccessTokenWithAkSk(new OnResultListener<AccessToken>() {
            @Override
            public void onResult(AccessToken result) {
                // 调用成功，返回AccessToken对象
                Util.log("aaaaaaaaa成功");

                String token = result.getAccessToken();
            }
            @Override
            public void onError(OCRError error) {
                Util.log("aaaaaaaaa失败");
                Util.log("aaaaaaaaa失败",error.getMessage());

                // 调用失败，返回OCRError子类SDKError对象
            }
        }, ZKToolApi.getInstance().getContext(), "您的应用AK", "您的应用SK");
    }
}
