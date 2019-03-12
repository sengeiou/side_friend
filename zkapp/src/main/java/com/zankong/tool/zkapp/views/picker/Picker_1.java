package com.zankong.tool.zkapp.views.picker;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.eclipsesource.v8.JavaCallback;
import com.eclipsesource.v8.V8Array;
import com.eclipsesource.v8.V8Object;
import com.zankong.tool.zkapp.R;
import com.zankong.tool.zkapp.ZKToolApi;
import com.zankong.tool.zkapp.document.ZKDocument;
import com.zankong.tool.zkapp.util.V8Utils;
import com.zankong.tool.zkapp.views.ZKViewAgent;

import org.dom4j.Attribute;
import org.dom4j.Element;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by YF on 2018/7/23.
 */
public class Picker_1 extends ZKViewAgent {
    private float density;
    private LinearLayout layout_background;
    private LayoutInflater inflater;
    public List<PickerViewBean> data;
    public PickerViewBean pickerViewBean;
    private int count;
    public Picker_1(ZKDocument zkDocument, Element element) {
        super(zkDocument, element);
    }
    @Override
    public void initView(ViewGroup viewGroup) {
        setContentView(R.layout.item_pickerview);
        density = getContext().getResources().getDisplayMetrics().density;
        inflater = LayoutInflater.from(getContext());
        layout_background = (LinearLayout) findViewById(R.id.layout_background);
        count = 5;
        initV8This(getThisV8());
    }

    @Override
    public void fillData(Element selfElement) {
        data = new ArrayList<>();
        List<Element> children = selfElement.elements();
        ViewGroup.LayoutParams layoutParams = layout_background.getLayoutParams();
        for (Attribute attribute : selfElement.attributes()) {
            String value = attribute.getValue();
            switch (attribute.getName()){
                case "height":
                    layoutParams.height = (int) (Integer.parseInt(value) * density);
                    break;
                case "width":
                    layoutParams.width = (int) (Integer.parseInt(value) * density);
                    break;
                case "count":
                    count = Integer.parseInt(value);
                    if (count / 2 == 0) {
                        count += 1;
                    }
                    break;
                case "background":
                    layout_background.setBackgroundColor(Color.parseColor(value));
                    break;
            }
        }
        layout_background.setLayoutParams(layoutParams);

        for (int i = 0; i < children.size(); i++) {
            Element element = children.get(i);
            List<Attribute> attributes = element.attributes();
            pickerViewBean = new PickerViewBean();
            String text = element.getText();
            if (text != null)
                pickerViewBean.setText(text.split(","));
            for (Attribute attribute : attributes) {
                String key = attribute.getName();
                String value = attribute.getValue();
                switch (key.toLowerCase()) {
                    case "width":
                        pickerViewBean.setWidth((int) (Integer.parseInt(value)));
                        break;
                    case "height":
                        pickerViewBean.setHeight((int) (Integer.parseInt(value)));
                        break;
                    case "font"://得到文本大小，颜色，是否加粗，是否有下划线，是否倾斜等等
                        String[] attr = value.split(",");
                        for (String mAttr : attr) {
                            if (mAttr.contains("#")) {
                                pickerViewBean.setTextColor(mAttr);//得到颜色
                            } else if (mAttr.equals("true") || mAttr.equals("false")) {
                                pickerViewBean.setTextBold(mAttr.equals("true"));//是否加粗
                            } else if (mAttr.equals("underLine")) {
                                //   pickerView2Bean.setUnderLine(true);//是否下划线
                            } else if (mAttr.contains("-")) {
                                // pickerView2Bean.setLean(Float.parseFloat(mAttr));//倾斜角度
                            } else {
                                pickerViewBean.setTextSize(Float.parseFloat(mAttr));//文本大小
                            }
                        }
                        break;
                    case "line":
                        String[] line = value.split(",");
                        for (String mAttr : line) {
                            if (mAttr.contains("#")) {
                                pickerViewBean.setLineColor(mAttr);//得到颜色
                            } else if (mAttr.equals("true") || mAttr.equals("false")) {
                                pickerViewBean.setLine(mAttr.equals("true"));//
                            } else if (mAttr.equals("underLine")) {
                                //   pickerView2Bean.setUnderLine(true);//是否下划线
                            } else if (mAttr.contains("-")) {
                                // pickerView2Bean.setLean(Float.parseFloat(mAttr));//倾斜角度
                            } else {
                                pickerViewBean.setLineWidth((int) (Integer.parseInt(mAttr) * density));
                            }
                        }
                        break;
                    case "click":
                        pickerViewBean.setOnClick(value);
                        break;
                    default:
                        break;
                }
            }
            data.add(pickerViewBean);
        }
        initListView();
    }
    private List<PickerView2_i> pickerView2IList;

    //加载listView
    private void initListView() {
        pickerView2IList = new ArrayList<>();
        if (data == null)
            return;
        for (int i = 0; i < data.size(); i++) {
            final PickerView2_i pickerView2_i = new PickerView2_i(getZKDocument().getActivity(), inflater, data.get(i), layout_background, count,getZKDocument().getDocument(),getThisV8().getRuntime());
            pickerView2IList.add(pickerView2_i);
        }
    }
    @Override
    public Object getResult() {
        return null;
    }

    @Override
    protected void setParentAttr(Element selfElement, View view) {
        super.setParentAttr(selfElement, view);

    }



    public void initV8This(V8Object document) {

        document.registerJavaMethod(new JavaCallback() {
            @Override
            public Object invoke(V8Object receiver, V8Array parameters) {
                V8Array v8arr = new V8Array(ZKToolApi.runtime);
                if (parameters.length() != 0)
                    return v8arr.push(pickerView2IList.get(parameters.getInteger(0)).getVal());
                else {
                    for (int i = 0; i < pickerView2IList.size(); i++) {
                        v8arr.push(pickerView2IList.get(i).getVal());
                    }
                    return v8arr;
                }

            }
        }, "val");
        document.registerJavaMethod(new JavaCallback() {
            @Override
            public Object invoke(V8Object receiver, V8Array parameters) {
                return null;
            }
        }, "set");


    }
}
