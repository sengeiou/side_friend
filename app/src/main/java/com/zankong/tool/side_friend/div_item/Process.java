package com.zankong.tool.side_friend.div_item;

import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.SparseBooleanArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.eclipsesource.v8.JavaCallback;
import com.eclipsesource.v8.V8Array;
import com.eclipsesource.v8.V8Function;
import com.eclipsesource.v8.V8Object;
import com.zankong.tool.side_friend.R;
import com.zankong.tool.zkapp.document.ZKDocument;
import com.zankong.tool.zkapp.document.listview.ZKRecycleView;
import com.zankong.tool.zkapp.item.ZKAdapter;
import com.zankong.tool.zkapp.util.ZKAppAdapter;

import org.dom4j.Attribute;
import org.dom4j.Element;

import java.util.ArrayList;

/**
 * Created by YF on 2018/6/26.
 */

@ZKAppAdapter("process")
public class Process extends ZKAdapter {
    private SparseBooleanArray shows = new SparseBooleanArray();
    private String taskClick = null;
    private int childItemIndex = 0;
    public Process(ZKDocument zKDocument, Element element) {
        super(zKDocument, element);
        for (Element element1 : element.elements()) {
            switch (element1.getName().toLowerCase()) {
                case "process":
                    for (Attribute attribute : element1.attributes()) {
                        switch (attribute.getName().toLowerCase()) {
                            case "itemclick":
                                taskClick = attribute.getValue();
                                break;
                        }
                    }
                    break;
            }
        }
    }

    @Override
    protected RecyclerView.ViewHolder onCreate(@NonNull ViewGroup viewGroup, int i) {
        return new ProcessHolder(mLayoutInflater.inflate(R.layout.item_process,viewGroup,false));
    }

    @Override
    protected int getViewType(int position) {
        return 0;
    }

    @Override
    protected void onBind(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        if (viewHolder instanceof ProcessHolder) {
            ((ProcessHolder) viewHolder).init(position);
        }
    }

    private class ProcessHolder extends RecyclerView.ViewHolder{
        private ZKRecycleView mZKRecycleView;
        private TextView title,id,time,status;

        public ProcessHolder(@NonNull View itemView) {
            super(itemView);
            mZKRecycleView = itemView.findViewById(R.id.recycleVIew);
            title = itemView.findViewById(R.id.title);
            id = itemView.findViewById(R.id.id);
            time = itemView.findViewById(R.id.time);
            status = itemView.findViewById(R.id.status);
        }
        private void init(final int position){
            boolean show = shows.get(position);
            shows.append(position,show);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    shows.append(position,!shows.get(position));
                    mZKRecycleView.setVisibility(shows.get(position) ? View.VISIBLE : View.GONE);
                }
            });
            mZKRecycleView.setVisibility(show ? View.VISIBLE : View.GONE);
            V8Object object = mList.get(position);
            String endTime = "";
            for (String key : object.getKeys()) {
                switch (key) {
                    case "id":
                        id.setText("进程号:"+object.getString(key));
                        break;
                    case "title":
                        title.setText(object.getString(key));
                        break;
                    case "startTime":
                        if ("".equals(endTime)){
                            time.setText(object.getString(key));
                        }
                        break;
                    case "endTime":
                        endTime = object.getString(key);
                        if (!"".equals(endTime)){
                            time.setText(endTime);
                        }
                        break;
                    case "status":
                        status.setText(object.getString(key));
                        break;
                }
            }

            V8Array item = object.getArray("item");
            ArrayList<V8Object> childList = new ArrayList<>();
            for(int i = 0 ; i < item.length() ; i++){
                childList.add(item.getObject(i));
            }
            Step step = new Step(getContext(),childList);
            step.setOnItemClickListener(new Step.OnItemClickListener() {
                @Override
                public void onItemClick(View view, int i) {
                    childItemIndex = i;
                    setSelectPosition(ProcessHolder.this);
                    if (taskClick != null) {
                        V8Function v8Function = getZKDocument().genContext(taskClick);
                        getZKDocument().invokeWithContext(v8Function);
                        v8Function.release();
                    }
                }
            });
            mZKRecycleView.setLayoutManager(new LinearLayoutManager(getContext()));
            mZKRecycleView.setAdapter(step);
        }
    }

    @Override
    protected void initV8this() {
        super.initV8this();
        getThisV8().registerJavaMethod(new JavaCallback() {
            @Override
            public Object invoke(V8Object receiver, V8Array parameters) {
                return mList.get(getSelectPosition()).getArray("item").get(childItemIndex);
            }
        },"getChild");
    }
}
