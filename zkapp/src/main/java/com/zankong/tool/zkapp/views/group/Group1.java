package com.zankong.tool.zkapp.views.group;

import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.eclipsesource.v8.JavaCallback;
import com.eclipsesource.v8.V8Function;
import com.eclipsesource.v8.V8Object;
import com.zankong.tool.zkapp.R;
import com.zankong.tool.zkapp.document.ZKDocument;
import com.zankong.tool.zkapp.document.listview.ZKRecycleView;
import com.zankong.tool.zkapp.util.Util;
import com.zankong.tool.zkapp.util.V8Utils;
import com.zankong.tool.zkapp.util.ZKRipple;
import com.zankong.tool.zkapp.views.ZKImgView;
import com.zankong.tool.zkapp.views.ZKPView;
import com.zankong.tool.zkapp.views.ZKParentAttr;
import com.zankong.tool.zkapp.views.ZKViewAgent;

import org.dom4j.Attribute;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by YF on 2018/7/23.
 */

public class Group1 extends ZKViewAgent {
    private ZKRecycleView mZKRecycleView;
    private ArrayList<Element> mItems,oldItems;
    private GroupAdapter mGroupAdapter;
    private int valPosition;

    public Group1(ZKDocument zkDocument, Element element) {
        super(zkDocument, element);
        mItems = new ArrayList<>();
        oldItems = new ArrayList<>();
    }

    @Override
    public void initView(ViewGroup viewGroup) {
        setContentView(R.layout.view_group_1);
        mZKRecycleView = ((ZKRecycleView) findViewById(R.id.group));
    }

    @Override
    public void fillData(Element selfElement) {
        mZKRecycleView.setFull(true);
        int spanCount = 4;
        for (Attribute attribute : selfElement.attributes()) {
            String value = attribute.getValue();
            String name = attribute.getName();
            switch (name) {
//                case "height":
//                    ViewGroup.LayoutParams layoutParams = mZKRecycleView.getLayoutParams();
//                    layoutParams.height = Util.px2px(value);
//                    mZKRecycleView.setLayoutParams(layoutParams);
//                    Util.log("group height",value);
//                    break;
                case "num":
                    spanCount = Util.getInt(value);
                    break;
                default:
                    if (name.contains("-")){
                        String childName = name.split("-")[0];
                        String childAttr = name.split("-")[1];
                        for (Element child : Util.getChildElement(selfElement, childName)) {
                            if (child.attribute(childAttr) == null)
                                child.addAttribute(childAttr,value);
                        }
                    }
            }
        }
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(),spanCount){
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        mZKRecycleView.setLayoutManager(gridLayoutManager);
        for (Element element : selfElement.elements()) {
            switch (element.getName()) {
                case "button":
                    oldItems.add(element);
                    break;
            }
        }
        for (Element oldItem : oldItems) {
            mItems.add((Element) oldItem.clone());
        }
        mGroupAdapter = new GroupAdapter();
        mZKRecycleView.setAdapter(mGroupAdapter);
    }

    @Override
    public Object getResult() {
        return valPosition;
    }

    public class GroupAdapter extends RecyclerView.Adapter{

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            return new GroupHolder(LayoutInflater.from(getContext()).inflate(R.layout.item_group_1,viewGroup,false));
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
            if (viewHolder instanceof GroupHolder) {
                ((GroupHolder) viewHolder).init(i);
            }
        }


        @Override
        public int getItemCount() {
            return mItems.size();
        }

        public HashMap<String,ArrayList<Element>> setMap = new HashMap<>();


        private class GroupHolder extends RecyclerView.ViewHolder{
            private ZKImgView mZKImgView;
            private ZKPView mP,mTp,mBp,mRp,mLp;
            public GroupHolder(@NonNull View itemView) {
                super(itemView);
                mZKImgView = ((ZKImgView) itemView.findViewById(R.id.img));
                mTp = ((ZKPView) itemView.findViewById(R.id.tp));
                mBp = ((ZKPView) itemView.findViewById(R.id.bp));
                mRp = ((ZKPView) itemView.findViewById(R.id.rp));
                mLp = ((ZKPView) itemView.findViewById(R.id.lp));
                mP = ((ZKPView)itemView.findViewById(R.id.p));
            }
            private void init(int position){
                mTp.setText("");
                mP.setText("");
                mLp.setText("");
                mRp.setText("");
                mBp.setText("");
                mZKImgView.setVisibility(View.GONE);
                ZKParentAttr zkParentAttr = new ZKParentAttr(getZKDocument(),mItems.get(position));
                zkParentAttr.setView(itemView);
                itemView.setBackground(ZKRipple.getRipple(getContext(),mItems.get(position)).getDrawable());
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        valPosition = position;
                        Attribute attribute = mItems.get(position).attribute("click");
                        if (attribute != null) {
                            String click = attribute.getValue();
                            V8Function v8Function = getZKDocument().genContext(click);
                            getZKDocument().invokeWithContext(v8Function);
                            v8Function.release();
                        }
                    }
                });



                mTp.setVisibility(View.GONE);
                mBp.setVisibility(View.GONE);
                for (Element element : mItems.get(position).elements()) {
                    switch (element.getName()) {
                        case "img":
                            mZKImgView.init(getZKDocument(),element);
                            break;
                        case "p":
                            mP.init(getZKDocument(),element);
                            break;
                        case "tp":
                            mTp.init(getZKDocument(),element);
                            mTp.setVisibility(View.VISIBLE);
                            break;
                        case "bp":
                            mBp.init(getZKDocument(),element);
                            mBp.setVisibility(View.VISIBLE);
                            break;
                        case "lp":
                            mLp.init(getZKDocument(),element);
                            break;
                        case "rp":
                            mRp.init(getZKDocument(),element);
                            break;
                    }
                }
            };
        }
    }

    //修改buttons里的子标签
    private void setData(V8Object object){
        int item = object.getInteger("item");
        Element element = mItems.get(item);
        for (String key : object.getKeys()) {
            if ("item".equals(key))continue;
            if("button".equals(key)){
                V8Object button = object.getObject(key);
                for (String s : button.getKeys()) {
                    element.addAttribute(s,button.getString(s));
                }
                continue;
            }
            Element child = element.element(key);
            if (child == null){
                child = DocumentHelper.createElement(key);
                element.add(child);
            }
            V8Object childObject = object.getObject(key);
            for (String s : childObject.getKeys()) {
                switch (s) {
                    case "text":
                    case "p":
                        child.setText(childObject.getString(s));
                        break;
                    default:
                        child.addAttribute(s,childObject.getString(s));
                        break;
                }
            }
        }
    }

    @Override
    public void initThisV8(V8Object thisV8) {
        super.initThisV8(thisV8);
        thisV8.registerJavaMethod((receiver, parameters) -> {
//            mItems.clear();
//            for (Element oldItem : oldItems) {
//                mItems.add((Element) oldItem.clone());
//            }
//            for (int i = 0; i < parameters.length(); i++) {
//                setData(parameters.getObject(i));
//            }
            mGroupAdapter.notifyDataSetChanged();
            return V8Utils.createFrom(thisV8);
        },"init");
        thisV8.registerJavaMethod((receiver, parameters) -> {
            for (int i = 0; i < parameters.length(); i++) {
                setData(parameters.getObject(i));
            }
            mGroupAdapter.notifyDataSetChanged();
            return V8Utils.createFrom(thisV8);
        },"set");
        thisV8.registerJavaMethod((receiver, parameters) -> {
            int position = parameters.getInteger(0);
            String name = parameters.getString(1);
            Element element = mItems.get(position).element(name);
            String result = "";
            if (element.getName().contains("p")){
                result = element.getText();
            }else if (element.getName().contains("img")){
                result = element.attributeValue("src");
            }
            return result;
        },"get");
        thisV8.registerJavaMethod((JavaCallback) (receiver, parameters) -> valPosition,"val");
    }
}
