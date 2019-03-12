package com.zankong.tool.zkapp.document.clip;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.Window;
import android.widget.ImageView;

import com.zankong.tool.zkapp.R;
import com.zankong.tool.zkapp.document.ZKDocument;
import com.zankong.tool.zkapp.util.ZKAppDocument;

import org.dom4j.Element;

/**
 * Created by YF on 2018/7/27.
 */

@ZKAppDocument("clip")
public class ZKClipView extends ZKDocument {

    private ImageView mImageView;

    public ZKClipView(Context context, Element root, Window window) {
        super(context, root, window);
    }

    public ZKClipView(Context context, Element root, ViewGroup viewGroup) {
        super(context, root, viewGroup);
    }

    @Override
    protected void initView() {
        setContextView(R.layout.doc_clip);
        mImageView = ((ImageView) findViewById(R.id.img));
    }

    @Override
    protected void fillData(Element docElement) {
        setClip(mImageView);
    }


    private void setClip(View v){
        ViewGroup parent = (ViewGroup) v.getParent();
        if (parent != null){
            parent.setClipChildren(false);
            setClip(parent);
        }
    }
}
