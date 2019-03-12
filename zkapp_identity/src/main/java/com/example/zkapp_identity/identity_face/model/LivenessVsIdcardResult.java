/*
 * Copyright (C) 2017 Baidu, Inc. All Rights Reserved.
 */
package com.example.zkapp_identity.identity_face.model;

import com.baidu.ocr.sdk.model.ResponseResult;

@SuppressWarnings("unused")
public class LivenessVsIdcardResult extends ResponseResult {

    private double score;
    private double faceliveness;

    private String idcardImage;

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    public String getIdcardImage() {
        return idcardImage;
    }

    public void setIdcardImage(String idcardImage) {
        this.idcardImage = idcardImage;
    }

    public double getFaceliveness() {
        return faceliveness;
    }

    public void setFaceliveness(double faceliveness) {
        this.faceliveness = faceliveness;
    }
}

