package com.zankong.tool.zkapp_alipay;

import android.content.Context;
import android.util.Log;

import com.alibaba.sdk.android.oss.ClientConfiguration;
import com.alibaba.sdk.android.oss.ClientException;
import com.alibaba.sdk.android.oss.OSS;
import com.alibaba.sdk.android.oss.OSSClient;
import com.alibaba.sdk.android.oss.ServiceException;
import com.alibaba.sdk.android.oss.callback.OSSCompletedCallback;
import com.alibaba.sdk.android.oss.callback.OSSProgressCallback;
import com.alibaba.sdk.android.oss.common.OSSLog;
import com.alibaba.sdk.android.oss.common.auth.OSSCredentialProvider;
import com.alibaba.sdk.android.oss.common.auth.OSSCustomSignerCredentialProvider;
import com.alibaba.sdk.android.oss.internal.OSSAsyncTask;
import com.alibaba.sdk.android.oss.model.PutObjectRequest;
import com.alibaba.sdk.android.oss.model.PutObjectResult;
import com.zankong.tool.zkapp.ZKToolApi;
import com.zankong.tool.zkapp.v8fn.ZKLocalStorage;
import com.zankong.tool.zkapp.v8fn.fetch_pck.Fetch;
import com.zankong.tool.zkapp.v8fn.fetch_pck.ServerAgent;

import org.json.JSONObject;

import java.util.ArrayList;

public class OSSHelper {
    private OSS oss;
    private String bucket = "pangyou-publick-file";
    private ArrayList<OSSFiles> willPut = new ArrayList<>();
    private ArrayList<OSSFiles> resultPut = new ArrayList<>();

    public OSSHelper(){
        init();
    }

    private Context getContext(){
        return ZKToolApi.getInstance().getContext();
    }


    private void init(){
        String endpoint = "http://oss-cn-shanghai.aliyuncs.com";
        OSSCredentialProvider credentialProvider = new OSSCustomSignerCredentialProvider() {
            @Override
            public String signContent(String content) {
                try {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("signContent",content);
                    jsonObject.put("uid", ZKLocalStorage.mSharedPreferences.getInt("uid",0)+"");
                    jsonObject.put("token",ZKLocalStorage.mSharedPreferences.getString("token",""));
                    String string = Fetch.post(ServerAgent.baseApi + "/logic/util-getPutSign", jsonObject.toString()).execute().body().string();
                    JSONObject result = new JSONObject(string);
                    if ("finish".equals(result.optString("verb"))) {
                        return result.get("args") + "";
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return "";
            }
        };

// 该配置类如果不设置，会有默认配置，具体可看该类
        ClientConfiguration conf = new ClientConfiguration();
        conf.setConnectionTimeout(15 * 1000); // 连接超时，默认15秒
        conf.setSocketTimeout(15 * 1000); // socket超时，默认15秒
        conf.setMaxConcurrentRequest(5); // 最大并发请求数，默认5个
        conf.setMaxErrorRetry(2); // 失败后最大重试次数，默认2次
        OSSLog.enableLog();
        oss = new OSSClient(getContext(), endpoint, credentialProvider);
    }



    private OSSProgressCallback mCallback =  new OSSProgressCallback<PutObjectRequest>() {
        @Override
        public void onProgress(PutObjectRequest request, long currentSize, long totalSize) {
            Log.d("PutObject", "currentSize: " + currentSize + " totalSize: " + totalSize);
        }
    };

    private OSSCompletedCallback mOSSCompletedCallback =  new OSSCompletedCallback<PutObjectRequest, PutObjectResult>() {
        @Override
        public void onSuccess(PutObjectRequest request, PutObjectResult result) {
            Log.d("PutObject", "UploadSuccess");
            Log.d("ETag", result.getETag());
            Log.d("RequestId", result.getRequestId());
            if (willPut.size() > 0) {
                OSSFiles ossFiles = willPut.get(0);
                ossFiles.setResultFile(oss.presignPublicObjectURL(bucket,ossFiles.getKey()));
                willPut.remove(0);
                resultPut.add(ossFiles);
                put(ossFiles.getKey(),ossFiles.getFile());
            }else {
                mPutResult.success(resultPut);
            }
        }

        @Override
        public void onFailure(PutObjectRequest request, ClientException clientExcepion, ServiceException serviceException) {
            mPutResult.failure();
            willPut.clear();
            resultPut.clear();
            // 请求异常。
            if (clientExcepion != null) {
                // 本地异常，如网络异常等。
                clientExcepion.printStackTrace();
            }
            if (serviceException != null) {
                // 服务异常。
                Log.e("ErrorCode", serviceException.getErrorCode());
                Log.e("RequestId", serviceException.getRequestId());
                Log.e("HostId", serviceException.getHostId());
                Log.e("RawMessage", serviceException.getRawMessage());
            }
        }
    };


    public void put(String key,String file){
        // 构造上传请求。
        PutObjectRequest put = new PutObjectRequest(bucket, key, file);
// 异步上传时可以设置进度回调。
        put.setProgressCallback(mCallback);
        OSSAsyncTask task = oss.asyncPutObject(put, mOSSCompletedCallback);
    }

    public void putFiles(ArrayList<OSSFiles> files,PutResult putResult){
        if(willPut.size() != 0){
            putResult.failure();
            return;
        }
        willPut.clear();
        resultPut.clear();
        willPut.addAll(files);
        mPutResult = putResult;
        put(files.get(0).getKey(),files.get(0).getFile());
    }


    private PutResult mPutResult;
    public interface PutResult{
        void success(ArrayList<OSSFiles> resultFiles);
        void failure();
    }
}
