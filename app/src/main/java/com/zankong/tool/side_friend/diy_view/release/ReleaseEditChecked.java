package com.zankong.tool.side_friend.diy_view.release;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.eclipsesource.v8.V8Function;
import com.eclipsesource.v8.V8Object;
import com.zankong.tool.side_friend.R;
import com.zankong.tool.zkapp.ZKToolApi;
import com.zankong.tool.zkapp.document.ZKDocument;
import com.zankong.tool.zkapp.views.ZKViewAgent;

import org.dom4j.Attribute;
import org.dom4j.Element;

/**
 * @author Fsnzzz
 * @Created on 2018/11/30 0030 17:07
 */
public class ReleaseEditChecked extends ZKViewAgent implements View.OnClickListener{

    private RadioGroup radio_group;
    private CheckBox radio_btn_01;
    private CheckBox radio_btn_02;
    private TextView type_tv_left;

    private boolean isFlag2 = false;
    private boolean isFlag = false;
    private V8Object v8Object;
    private V8Function onClickListener;
    private ZKDocument mZkDocument;
    private TextView tv_01;
    private TextView tv_02;
    private ImageView img_02;
    private ImageView img_01;
    private LinearLayout layout_02;
    private LinearLayout layout_01;

    public ReleaseEditChecked(ZKDocument zkDocument, Element element) {
        super(zkDocument, element);
        this.mZkDocument = zkDocument;
    }

    @Override
    public void initView(ViewGroup viewGroup) {
        setContentView(R.layout.release_edit_checked_layout);
        type_tv_left = (TextView) findViewById(R.id.type_tv_left);

        layout_01 = (LinearLayout) findViewById(R.id.layout_01);
        layout_02 = (LinearLayout) findViewById(R.id.layout_02);

        img_01 = (ImageView) findViewById(R.id.img_01);
        img_02 = (ImageView) findViewById(R.id.img_02);

        tv_01 = (TextView) findViewById(R.id.tv_01);
        tv_02 = (TextView) findViewById(R.id.tv_02);

        layout_01.setOnClickListener(this);
        layout_02.setOnClickListener(this);



    }

    @Override
    public void fillData(Element selfElement) {
        for (Attribute attribute : selfElement.attributes()) {
            String value = attribute.getValue();
            switch (attribute.getName()) {
                case "type_left":
                    type_tv_left.setText(value + "");
                    break;
                case "btn_tv1":
                    tv_01.setText(value + "");
                    break;
                case "btn_tv2":
                    tv_02.setText(value + "");
                    break;
                case "click":
                    onClickListener = mZkDocument.genContextFn(value);
                    break;
            }
        }
    }
    @Override
    public Object getResult() {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (onClickListener != null)
            onClickListener.release();
    }

    @Override
    public void onClick(View v) {
        if (v8Object == null){
            v8Object = new V8Object(ZKToolApi.runtime);
        }
        switch (v.getId()){
            case R.id.layout_01:
                if (isFlag){
                    Glide.with(getContext())
                            .load(R.drawable.release_nor)
                            .into(img_01);
                    isFlag = false;
                    v8Object.add("check","");
                }else {
                    Glide.with(getContext())
                            .load(R.drawable.release_press)
                            .into(img_01);
                    isFlag = true;
                    isFlag2 = false;
                    Glide.with(getContext())
                            .load(R.drawable.release_nor)
                            .into(img_02);
                    v8Object.add("check",tv_01.getText()+"");
                }
                break;
            case R.id.layout_02:
                if (isFlag2){
                    Glide.with(getContext())
                            .load(R.drawable.release_nor)
                            .into(img_02);
                    isFlag2 = false;

                    v8Object.add("check","");
                }else {
                    Glide.with(getContext())
                            .load(R.drawable.release_press)
                            .into(img_02);
                    isFlag2 = true;
                    isFlag = false;
                    Glide.with(getContext())
                            .load(R.drawable.release_nor)
                            .into(img_01);
                    v8Object.add("check",tv_02.getText()+"");
                }
                break;
        }
        mZkDocument.invokeWithContext(onClickListener,v8Object);
    }
}
