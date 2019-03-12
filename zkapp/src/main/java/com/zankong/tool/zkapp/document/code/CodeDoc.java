package com.zankong.tool.zkapp.document.code;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.eclipsesource.v8.JavaCallback;
import com.eclipsesource.v8.V8Array;
import com.eclipsesource.v8.V8Object;
import com.uuzuche.lib_zxing.activity.CaptureFragment;
import com.uuzuche.lib_zxing.activity.CodeUtils;
import com.zankong.tool.zkapp.R;
import com.zankong.tool.zkapp.document.ZKDocument;
import com.zankong.tool.zkapp.util.Util;
import com.zankong.tool.zkapp.util.V8Utils;
import com.zankong.tool.zkapp.util.ZKAppDocument;

import org.dom4j.Attribute;
import org.dom4j.Element;


/**
 * Created by YF on 2018/8/16.
 */

@ZKAppDocument("code")
public class CodeDoc extends ZKDocument {

    private LinearLayout mHeader;
    private LinearLayout mFooter;

    public CodeDoc(Context context, Element root, Window window) {
        super(context, root, window);
    }

    public CodeDoc(Context context, Element root, ViewGroup viewGroup) {
        super(context, root, viewGroup);
    }

    @Override
    protected void initView() {
        setContextView(R.layout.doc_code);
        mHeader = ((LinearLayout) findViewById(R.id.header));
        mFooter = ((LinearLayout) findViewById(R.id.footer));
    }

    @Override
    protected void fillData(Element docElement) {
        for (Attribute attribute : docElement.attributes()) {
            String value = attribute.getValue();
            switch (attribute.getName()) {
                case "success":
                    break;
                case "code":
                    break;
            }
        }
        for (Element element : docElement.elements()) {
            switch (element.getName().toLowerCase()) {
                case "header":
                    setXml(element,mHeader);
                    break;
                case "footer":
                    setXml(element,mFooter);
                    break;
            }
        }
        CaptureFragment captureFragment = new CaptureFragment();
        // 为二维码扫描界面设置定制化界面
        CodeUtils.setFragmentArgs(captureFragment, R.layout.doc_code_camera);

        captureFragment.setAnalyzeCallback(new CodeUtils.AnalyzeCallback() {
            @Override
            public void onAnalyzeSuccess(Bitmap mBitmap, String result) {
                Util.log("code",result);
            }

            @Override
            public void onAnalyzeFailed() {
                Util.log("code","error");
            }
        });
        getFragmentManager().beginTransaction().replace(R.id.fl_my_container, captureFragment).commit();


    }

}
