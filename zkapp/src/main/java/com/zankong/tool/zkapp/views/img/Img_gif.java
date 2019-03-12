package com.zankong.tool.zkapp.views.img;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.zankong.tool.zkapp.R;
import com.zankong.tool.zkapp.document.ZKDocument;
import com.zankong.tool.zkapp.views.ZKViewAgent;

import org.dom4j.Attribute;
import org.dom4j.Element;

/**
 * Created by YF on 2018/7/23.
 */

public class Img_gif extends ZKViewAgent {
    private String src = "";
    private ImageView mImageView;

    public Img_gif(ZKDocument zkDocument, Element element) {
        super(zkDocument, element);
    }

    @Override
    public void initView(ViewGroup viewGroup) {
        setContentView(R.layout.view_img_gif);
        mImageView = ((ImageView) findViewById(R.id.img));
    }

    @Override
    protected void setParentAttr(Element selfElement, View view) {

    }

    @Override
    public void fillData(Element selfElement) {
        for (Attribute attribute : selfElement.attributes()) {
            String value = attribute.getValue();
            switch (attribute.getName()) {
                case "src":
                    src = value;
                    break;
            }
        }
        RequestOptions options = new RequestOptions().diskCacheStrategy(DiskCacheStrategy.RESOURCE);
        Glide.with(getContext()).load(src).apply(options).into(mImageView);
    }

    @Override
    public Object getResult() {
        return src;
    }
}
