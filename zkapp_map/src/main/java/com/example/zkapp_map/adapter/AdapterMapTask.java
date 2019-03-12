package com.example.zkapp_map.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import com.eclipsesource.v8.V8Object;
import com.example.zkapp_map.R;
import com.zankong.tool.zkapp.ZKToolApi;
import com.zankong.tool.zkapp.document.ZKDocument;
import com.zankong.tool.zkapp.item.ZKAdapter;
import org.dom4j.Element;
import java.util.ArrayList;

public class AdapterMapTask extends ZKAdapter {

    public AdapterMapTask(ZKDocument zKDocument, Element element) {
        super(zKDocument, element);
    }
    public void load(ArrayList<V8Object> list){
        mList.add(new V8Object(ZKToolApi.runtime));
        mList.add(new V8Object(ZKToolApi.runtime));
        mList.add(new V8Object(ZKToolApi.runtime));
        mList.add(new V8Object(ZKToolApi.runtime));
        mList.add(new V8Object(ZKToolApi.runtime));
        mList.add(new V8Object(ZKToolApi.runtime));
        mList.add(new V8Object(ZKToolApi.runtime));
     //   this.mList = list;
    }
    @Override
    protected RecyclerView.ViewHolder onCreate(@NonNull ViewGroup viewGroup, int i) {
        View view = mLayoutInflater.inflate(R.layout.adapter_map_task_item, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    protected int getViewType(int position) {
        return 0;
    }
    

    @Override
    protected void onBind(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
