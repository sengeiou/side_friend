/*
 * Copyright (C) 2017 Baidu, Inc. All Rights Reserved.
 */
package com.example.zkapp_identity.identity_face;

import com.example.zkapp_identity.identity_face.exception.FaceException;

public interface OnResultListener<T> {
    void onResult(T result);

    void onError(FaceException error);
}
