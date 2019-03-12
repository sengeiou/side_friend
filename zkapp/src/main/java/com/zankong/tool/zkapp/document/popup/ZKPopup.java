package com.zankong.tool.zkapp.document.popup;

import android.content.Context;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.LinearLayout;

import com.zankong.tool.zkapp.R;
import com.zankong.tool.zkapp.document.ZKDocument;
import com.zankong.tool.zkapp.util.ZKAppDocument;

import org.dom4j.Element;

/**
 * Created by YF on 2018/7/4.
 */
@ZKAppDocument("popup")
public class ZKPopup extends ZKDocument {

    private LinearLayout mBody;

    public ZKPopup(Context context, Element root, Window window) {
        super(context, root, window);
    }

    public ZKPopup(Context context, Element root, ViewGroup viewGroup) {
        super(context, root, viewGroup);
    }

    @Override
    protected void initView() {
        setContextView(R.layout.doc_popup);
        mBody = ((LinearLayout) findViewById(R.id.body));
    }

    @Override
    protected void fillData(Element docElement) {
        setXml(docElement,mBody);
    }
}
