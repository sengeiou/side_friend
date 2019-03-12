package com.zankong.tool.zkapp.views.switcher;

import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.eclipsesource.v8.JavaCallback;
import com.eclipsesource.v8.V8Array;
import com.eclipsesource.v8.V8Function;
import com.eclipsesource.v8.V8Object;
import com.eclipsesource.v8.V8Value;
import com.zankong.tool.zkapp.R;
import com.zankong.tool.zkapp.ZKToolApi;
import com.zankong.tool.zkapp.document.ZKDocument;
import com.zankong.tool.zkapp.document.listview.ZKRecycleView;
import com.zankong.tool.zkapp.util.Util;
import com.zankong.tool.zkapp.util.ZKRipple;
import com.zankong.tool.zkapp.views.ZKImgView;
import com.zankong.tool.zkapp.views.ZKPView;
import com.zankong.tool.zkapp.views.ZKViewAgent;
import com.zankong.tool.zkapp.views.group.Group1;

import org.dom4j.Attribute;
import org.dom4j.Element;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Administrator on 2018/8/23 0023.
 */


public class Switcher3 extends ZKViewAgent {

    private ZKRecycleView mZKRecycleView;
    private ArrayList<Element> mItems;
    private GroupAdapter mGroupAdapter;
    private String positions = "left";

    private List<String> isBooleanList = new ArrayList<>();
    private String itemClick = "null";
    public Switcher3(ZKDocument zkDocument, Element element) {
        super(zkDocument, element);
        mItems = new ArrayList<>();
    }

    @Override
    public void initView(ViewGroup viewGroup) {
        setContentView(R.layout.view_switcher3);
        mZKRecycleView = ((ZKRecycleView) findViewById(R.id.group));
    }

    @Override
    public void fillData(Element selfElement) {
        int spanCount = 4;
        for (Attribute attribute : selfElement.attributes()) {
            String value = attribute.getValue();
            switch (attribute.getName()) {
                case "num":
                    spanCount = Util.getInt(value);
                    break;
                case "position":
                    positions = value;
                    break;
                case "itemclick":
                    itemClick = value;
                    break;
            }
        }
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), spanCount);
        mZKRecycleView.setLayoutManager(gridLayoutManager);
        for (Element element : selfElement.elements()) {
            switch (element.getName()) {
                case "p":
                    mItems.add(element);
                    break;
            }
        }
        mGroupAdapter = new GroupAdapter();
        mZKRecycleView.setAdapter(mGroupAdapter);
        onClickAdapter(mGroupAdapter);
    }

    private void onClickAdapter(GroupAdapter adapter){
        adapter.setOnItemClickLitener(new OnItemClickLitener() {
            @Override
            public void onItemClick(View view, int position, String name) {
                if(!adapter.isSelected.get(position)){
                    adapter.isSelected.put(position, true); // 修改map的值保存状态
                    adapter.notifyItemChanged(position);
                 //   selectDatas.add(datas.get(position));
                    isBooleanList.add(name);
                }else {
                    adapter.isSelected.put(position, false); // 修改map的值保存状态
                    adapter.notifyItemChanged(position);
                  //  selectDatas.remove(datas.get(position));
                    isBooleanList.remove(name);
                }
                Log.e("=============",name+"====="+position);
                try{
                    if (itemClick.equals("null")){
                    }else {
                        V8Object position1 = new V8Object(ZKToolApi.runtime);
                        position1.add("name", name+"");
                        V8Function v8Function = getZKDocument().genContextFn(itemClick);
                        getZKDocument().invokeWithContext(v8Function,position1);
                    }


                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });

    }

    @Override
    public Object getResult() {
        return null;
    }

    private class GroupAdapter extends RecyclerView.Adapter {

        private OnItemClickLitener mOnItemClickLitener;
        public HashMap<Integer, Boolean> isSelected;

        public GroupAdapter() {
            init();
        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            return new GroupHolder(LayoutInflater.from(getContext()).inflate(R.layout.item_switcher3, viewGroup, false));
        }

        public void init() {
            isSelected = new HashMap<Integer, Boolean>();
            for (int i = 0; i < mItems.size(); i++) {
                isSelected.put(i, false);
            }
        }
        public void setOnItemClickLitener(OnItemClickLitener mOnItemClickLitener) {
            this.mOnItemClickLitener = mOnItemClickLitener;
        }
        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
                ((GroupHolder) viewHolder).init(i);
        }

        @Override
        public int getItemCount() {
            return mItems.size();
        }

        private class GroupHolder extends RecyclerView.ViewHolder {


            private RelativeLayout leftLayout;
            private RelativeLayout rightLayout;
            private ZKPView tv_name;
            private ZKPView tv_name2;
            private CheckBox checkbox;
            private CheckBox checkbox2;
            public GroupHolder(@NonNull View itemView) {
                super(itemView);
                leftLayout = (RelativeLayout) itemView.findViewById(R.id.leftLayout);
                rightLayout = (RelativeLayout) itemView.findViewById(R.id.rightLayout);
                tv_name = (ZKPView) itemView.findViewById(R.id.tv_name);
                tv_name2 = (ZKPView) itemView.findViewById(R.id.tv_name2);
                checkbox = (CheckBox) itemView.findViewById(R.id.checkbox1);
                checkbox2 = (CheckBox) itemView.findViewById(R.id.checkbox2);
            }
            private void init(int position) {
                if (positions.equals("left")) {
                    leftLayout.setVisibility(View.VISIBLE);
                    rightLayout.setVisibility(View.GONE);
                    checkbox2.setChecked(isSelected.get(position));
                    if (mOnItemClickLitener != null) {
                        itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                mOnItemClickLitener.onItemClick(view, getAdapterPosition(), tv_name2.getText().toString());
                            }
                        });
                    }


                } else if (positions.equals("right")) {
                    leftLayout.setVisibility(View.GONE);
                    rightLayout.setVisibility(View.VISIBLE);
                    checkbox.setChecked(isSelected.get(position));
                    if (mOnItemClickLitener != null) {
                        itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                mOnItemClickLitener.onItemClick(view, getAdapterPosition(), tv_name.getText().toString());
                            }
                        });

                    }
                    itemView.setBackground(ZKRipple.getRipple(getContext(), mItems.get(position)).getDrawable());

                    for (Element element : mItems) {
                        switch (element.getName()) {
                            case "p":
                                tv_name.init(getZKDocument(), mItems.get(position));
                                break;
                            case "rp":
                                tv_name2.init(getZKDocument(), mItems.get(position));
                                break;

                        }
                    }
                }

                ;
            }


        }
    }

    @Override
    public void initThisV8(V8Object thisV8) {
        super.initThisV8(thisV8);
        thisV8.registerJavaMethod(new JavaCallback() {
            @Override
            public Object invoke(V8Object receiver, V8Array parameters) {
                mGroupAdapter.notifyDataSetChanged();
                return null;
            }
        }, "init");
        thisV8.registerJavaMethod(new JavaCallback() {
            @Override
            public Object invoke(V8Object receiver, V8Array parameters) {

                V8Array v8arr = new V8Array(ZKToolApi.runtime);
                v8arr.push(isBooleanList.toString());
                return v8arr;
            }
        }, "val");
    }





    public interface OnItemClickLitener {
        void onItemClick(View view, int position, String name);
    }
}
