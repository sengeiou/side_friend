package com.zankong.tool.side_friend.diy_view.home_screen;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.eclipsesource.v8.V8Array;
import com.eclipsesource.v8.V8Object;
import com.zankong.tool.side_friend.R;
import com.zankong.tool.zkapp.ZKToolApi;
import com.zankong.tool.zkapp.document.ZKDocument;
import com.zankong.tool.zkapp.views.ZKViewAgent;

import org.dom4j.Attribute;
import org.dom4j.Element;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Fsnzzz
 * @Created on 2018/11/22 0022 11:10
 */
public class HomeScreen1 extends ZKViewAgent {

    private TextView type;
    private RecyclerView rv;

    private V8Array v8Array = new V8Array(ZKToolApi.runtime);
    private int firstitem;
    private List<Integer> positions;
    private List<String> list;
    private HomeScreenAdapter adapter1;
    private Button btn;
    private int i;

    private String tag;
    private V8Object v8Object;
    public HomeScreen1(ZKDocument zkDocument, Element element) {
        super(zkDocument, element);
    }

    @Override
    public void initView(ViewGroup viewGroup) {
        setContentView(R.layout.home_screen_1);
        v8Object = new V8Object(ZKToolApi.runtime);
        list = new ArrayList<>();
        positions = new ArrayList<>();
        type = (TextView) findViewById(R.id.type_tv);
        btn = (Button) findViewById(R.id.btn);
        rv = (RecyclerView) findViewById(R.id.rv);
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(),2);
        rv.setLayoutManager(layoutManager);
        adapter1 = new HomeScreenAdapter(getContext(),list,positions);
        rv.setAdapter(adapter1);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (i == 0 && firstitem == 0){
                    i = 1;
                    adapter1.onCancelAll();
                    adapter1.notifyDataSetChanged();
                    v8Object.add("r",10);
                    btn.setBackgroundResource(R.drawable.icon_lately_pree);
                }else if (i == 0 && firstitem == 1){
                    btn.setBackgroundResource(R.drawable.icon_unlimited_press);
                    i = 1;
                    adapter1.onCancelAll();
                    adapter1.notifyDataSetChanged();
                    if (tag.equals("1")){
                        v8Object.add("reward",10);
                    }else if (tag.equals("2")){
                        v8Object.add("credit",10);
                    }
                }



            }
        });
        adapter1.onClickListener(new HomeScreenAdapter.ItemClickListener() {
            @Override
            public void onData(int postion,String str) {
                i = 0;
                if (firstitem == 0){
                    Log.e("ttttttt1",postion+","+firstitem);
                    v8Object.add("r",postion);
                    btn.setBackgroundResource(R.drawable.icon_ately_nor);
                }else if (tag.equals("1")){
                    v8Object.add("reward",postion);
                    btn.setBackgroundResource(R.drawable.icon_unlimited_nor);

                }else if (tag.equals("2")){
                    v8Object.add("credit",postion);
                    btn.setBackgroundResource(R.drawable.icon_unlimited_nor);
                }
                adapter1.notifyDataSetChanged();
            }
            @Override
            public void onCancel(int postion) {
            }
        });
    }

    @Override
    public void fillData(Element selfElement) {
        for (Attribute attribute : selfElement.attributes()) {
            String value = attribute.getValue();
            switch (attribute.getName()) {
                case "type":
                    type.setText(value);
                    break;
                case "postion":
                    positions.clear();
                    positions.add(Integer.parseInt(value));
                    break;
                case "firstitem":
                    switch (Integer.parseInt(value)){
                        case 0:
                            firstitem = 0;
                            btn.setBackgroundResource(R.drawable.icon_ately_nor);
                            break;
                        case 1:
                            firstitem = 1;
                            btn.setBackgroundResource(R.drawable.icon_unlimited_nor);
                            break;
                    }
                    i = 0;
                    if (i == 0 && firstitem == 0){
                        btn.setBackgroundResource(R.drawable.icon_ately_nor);
                    }else if (i == 0 && firstitem == 1){
                        btn.setBackgroundResource(R.drawable.icon_unlimited_nor);
                    }
                    break;
            }
        }
    }

    @Override
    public void initThisV8(V8Object thisV8) {
        super.initThisV8(thisV8);
        thisV8.registerJavaMethod((receiver, parameters) -> {
            V8Array array = parameters.getArray(0);
            int position = parameters.getInteger(1);
             tag = parameters.getString(2);
            positions.clear();
            if (position > array.length()-1){
                if (firstitem == 0){
                    i = 1;
//                    r:document.arguments().range.r,
//                            credit:document.arguments().credit,
//                            reward:document.arguments().reward,

                    v8Object.add("r",10);
                    btn.setBackgroundResource(R.drawable.icon_lately_pree);
                }else if (tag.equals("1")){
                    i = 1;
                    v8Object.add("reward",10);
                    btn.setBackgroundResource(R.drawable.icon_unlimited_press);
                }else if (tag.equals("2")){
                    i = 1;
                    v8Object.add("credit",10);
                    btn.setBackgroundResource(R.drawable.icon_unlimited_press);
                }
            }else {
                if (tag.equals("0")){
                    v8Object.add("r",position);
                }else if (tag.equals("1")){
                    v8Object.add("reward",position);
                }else {
                    v8Object.add("credit",position);
                }
                positions.add(position);
            }
            for (int i = 0; i < array.length(); i++) {
                String string = array.getString(i);
                list.add(string);
            }
            adapter1.setData(list,positions);
            adapter1.notifyDataSetChanged();
            return null;
        }, "set");

    }
    @Override
    public Object getResult() {
        return v8Object;
    }
}
