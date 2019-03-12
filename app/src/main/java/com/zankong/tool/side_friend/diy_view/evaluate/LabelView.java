package com.zankong.tool.side_friend.diy_view.evaluate;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ViewGroup;

import com.eclipsesource.v8.V8;
import com.eclipsesource.v8.V8Function;
import com.eclipsesource.v8.V8Object;
import com.zankong.tool.side_friend.R;
import com.zankong.tool.zkapp.ZKToolApi;
import com.zankong.tool.zkapp.document.ZKDocument;
import com.zankong.tool.zkapp.util.V8Utils;
import com.zankong.tool.zkapp.views.ZKViewAgent;

import org.dom4j.Attribute;
import org.dom4j.Element;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class LabelView extends ZKViewAgent {

    private List<String> list;
    private RecyclerView rv;
    private AdapterLabel adapterLable;
    private V8Function onClickItem;
    private ZKDocument zkDocument;
    private V8Object v8Object;
    private String str = "";
    public LabelView(ZKDocument zkDocument, Element element) {
        super(zkDocument, element);
        this.zkDocument = zkDocument;
    }

    @Override
    public void initView(ViewGroup viewGroup) {
        setContentView(R.layout.lable_view);
        rv = (RecyclerView) findViewById(R.id.rv);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        rv.setLayoutManager(linearLayoutManager);
        adapterLable = new AdapterLabel(getContext());
        rv.setAdapter(adapterLable);
        adapterLable.onClickListener(new AdapterLabel.ItemListener() {
            @Override
            public void data(Map<Integer,String> data) {
                str = "";
                for (Map.Entry<Integer,String> entry : data.entrySet()) {
                   str = str + entry.getValue() + ",";
                }
                
            }
        });
    }

    @Override
    public void fillData(Element selfElement) {
        for (Attribute attribute : selfElement.attributes()) {
            String value = attribute.getValue();
            switch (attribute.getName()) {
                case "text_color":
                    break;
            }
        }
    }

    @Override
    public Object getResult() {
        if (v8Object == null){
            v8Object = new V8Object(ZKToolApi.runtime);
        }
        v8Object.add("content",str);
        return V8Utils.createFrom(v8Object);
    }
    public void initThisV8(V8Object thisV8) {
        super.initThisV8(thisV8);
        thisV8.registerJavaMethod((receiver, parameters) -> {
            V8Object object = null;
            if (parameters.length() > 0) {
                object = parameters.getObject(0);
                String text = object.get("text") + "";
                String[] split = text.split(",");
                if (list == null){
                    list = new ArrayList<>();
                }
                list.clear();
                for (int i = 0 ; i < split.length;i++){
                    list.add(split[i]);
                }
                adapterLable.onRefresh(list);
                adapterLable.notifyDataSetChanged();
                
            }
            return null;
        }, "set");
        
           thisV8.registerJavaMethod((receiver, parameters) -> {
            V8Object object = null;
            if (parameters.length() > 0) {

                V8Object object1 = parameters.getObject(0);
                if (object1.getString("init").equals("init")){
                    str = "";
                    adapterLable.initMap();
                    adapterLable.notifyDataSetChanged();
                }
            }
            return null;
        }, "refresh");
        
        
        


    }
}
