package com.zankong.tool.side_friend.diy_view.user_message;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.eclipsesource.v8.V8Function;
import com.eclipsesource.v8.V8Object;
import com.zankong.tool.side_friend.R;
import com.zankong.tool.zkapp.document.ZKDocument;
import com.zankong.tool.zkapp.util.V8Utils;
import com.zankong.tool.zkapp.views.ZKViewAgent;

import org.dom4j.Attribute;
import org.dom4j.Element;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
public class UserMessageLabel extends ZKViewAgent {
    private RecyclerView rv;
    private AdapterMessageLabel adapter;
    private V8Function onClickListener;
    private ZKDocument mZKDocument;
    private String labels = "";
    private List<Map<Object,Object>> mapList;
    public UserMessageLabel(ZKDocument zkDocument, Element element) {
        super(zkDocument, element);
        this.mZKDocument = zkDocument;
    }
    @Override
    public void initView(ViewGroup viewGroup) {
        setContentView(R.layout.user_massger_label_layout);
        rv = (RecyclerView) findViewById(R.id.rv);
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(),3);
        rv.setLayoutManager(layoutManager);
        List<Map<Object,Object>> list = new ArrayList<>();
        adapter = new AdapterMessageLabel(getContext(),list);
        rv.setAdapter(adapter);
        adapter.setOnItemClickListener(new AdapterMessageLabel.OnItemClickListener() {
            @Override
            public void onClick(List<String> str) {
                labels = "";
                for (int k = 0; k < str.size();k++){
                    labels = labels+str.get(k)+",";
                }
                if (!"".equals(labels)){
                    labels =  labels.substring(0,labels.length()-1);
                }
            }
            
        });
    }
    @Override
    public void fillData(Element selfElement) {
        for (Attribute attribute : selfElement.attributes()) {
            String value = attribute.getValue();
            switch (attribute.getName()) {
                case "click":
                    onClickListener = mZKDocument.genContextFn(value);
                    break;
            }
        }
    }
    @Override
    public Object getResult() {
        return labels;
    }
    @Override
    public void initThisV8(V8Object thisV8) {
        super.initThisV8(thisV8);
        thisV8.registerJavaMethod((receiver, parameters) -> {
            V8Object object = null;
            if (parameters.length() > 0){
                if (mapList == null){
                    mapList = new ArrayList<>();
                }
                mapList.clear();
                object = parameters.getObject(0);
                String text = object.get("text")+"";
                String text2 = object.get("text2")+"";
                String[] split = text.split(",");
                for (int i = 0;i < split.length;i++){
                    Map<Object,Object> map = new HashMap<>();
                    map.put("text",split[i]);
                    map.put("boolean",false);
                    mapList.add(map);
                }
                if (!"".equals(text2) && text2.contains(",")){
                    String[] split1 = text2.split(",");
                    for (int i = 0; i < split1.length;i++){
                        for (int j = 0 ; j < mapList.size();j++){
                            if (split1[i].equals(mapList.get(j).get("text"))){
                                mapList.get(j).put("boolean",true);
                            }
                        }
                    }
                    labels = text2;
                }else {
                    for (int i = 0; i < mapList.size();i++){
                        if (mapList.get(i).get("text").equals(text2)){
                            mapList.get(i).put("boolean",true);
                        }
                    }
                    labels = text2;
                }
                adapter.onRefresh(mapList);
                adapter.notifyDataSetChanged();
            }
            return null;
        }, "set");
    }
}
