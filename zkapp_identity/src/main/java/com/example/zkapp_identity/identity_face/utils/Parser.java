/*
 * Copyright (C) 2017 Baidu, Inc. All Rights Reserved.
 */
package com.example.zkapp_identity.identity_face.utils;

import com.example.zkapp_identity.identity_face.exception.FaceException;

/**
 * JSON解析
 * @param <T>
 */
public interface Parser<T> {
    T parse(String json) throws FaceException;
}
