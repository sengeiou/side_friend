package com.example.zkapp_identity.identity_face.parser;

import android.util.Log;


import com.example.zkapp_identity.identity_face.exception.FaceException;
import com.example.zkapp_identity.identity_face.model.ResponseResult;
import com.example.zkapp_identity.identity_face.utils.Parser;

import org.json.JSONException;
import org.json.JSONObject;

public class RegResultParser implements Parser<ResponseResult> {


    @Override
    public ResponseResult parse(String json)  {
        Log.e("xx", "oarse:" + json);
        try {
            JSONObject jsonObject = new JSONObject(json);

            if (jsonObject.has("error_code")) {
                FaceException error = new FaceException(jsonObject.optInt("error_code"),
                        jsonObject.optString("error_msg"));
                if (error.getErrorCode() != 0) {
                    try {
                        throw error;
                    } catch (FaceException e) {
                        e.printStackTrace();
                    }
                }
            }

            ResponseResult result = new ResponseResult();
            result.setLogId(jsonObject.optLong("log_id"));
            result.setJsonRes(json);

            return result;
        } catch (JSONException e) {
            e.printStackTrace();
            FaceException error = new FaceException(FaceException.ErrorCode.JSON_PARSE_ERROR,
                    "Json parse error:" + json, e);
            try {
                throw error;
            } catch (FaceException e1) {
                e1.printStackTrace();
            }
        }
        return null;
    }
}