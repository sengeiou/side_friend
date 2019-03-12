package com.zankong.tool.zkapp.document.dialog;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.LinearLayout;

import com.zankong.tool.zkapp.R;
import com.zankong.tool.zkapp.document.ZKDocument;
import com.zankong.tool.zkapp.util.ZKAppDocument;

import org.dom4j.Attribute;
import org.dom4j.Element;

/**
 * Created by YF on 2018/7/4.
 */
@ZKAppDocument("dialog")
public class ZKDialog extends ZKDocument {

    private LinearLayout mDialogContent;

    public ZKDialog(Context context, Element root, Window window) {
        super(context, root, window);
    }

    public ZKDialog(Context context, Element root, ViewGroup viewGroup) {
        super(context, root, viewGroup);
    }

    @Override
    protected void initView() {
        setContextView(R.layout.doc_dialog);
        mDialogContent = (LinearLayout) findViewById(R.id.dialogContent);
    }
    
    @Override
    protected void fillData(Element docElement) {
        setXml(docElement,mDialogContent);
    }
}
