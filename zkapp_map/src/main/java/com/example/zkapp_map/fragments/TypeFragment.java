package com.example.zkapp_map.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.zkapp_map.R;
import com.example.zkapp_map.adapter.TypeFragmentAdapter;
import com.example.zkapp_map.bean.TestBean;

import java.util.ArrayList;
import java.util.List;


/**
 * @author Fsnzzz
 * @date 2019/1/16
 * @month 01
 * @package com.example.zkapp_map.fragments
 */

@SuppressLint("ValidFragment")
public class TypeFragment extends BaseFragment {
    private static TypeFragment instance;
    private RecyclerView rv;

    private ArrayList<TestBean> datas;
    private List<String> kinds;
    private GridLayoutManager layoutManager;

    public static TypeFragment getInstance(String tv){
        if (instance == null){
            instance = new TypeFragment(tv);
        }
        return instance;
    }
    public TypeFragment(String tv){
    }
    @Override
    protected int onLayoutID() {
        return R.layout.type_fragment_layout;
    }
    @Override
    public void initView(View view) {
        rv = view.findViewById(R.id.rv);
    }

    @Override
    public void onBindView() {
        layoutManager = new GridLayoutManager(getContext(),4,LinearLayoutManager.VERTICAL, false);
       // rv.setAdapter(adapter);
    }

    @Override
    public void onBindData() {
        if (datas == null){
            datas = new ArrayList<>();
        }
        for (int i = 0; i < 5;i++){
            TestBean testBean = new TestBean();
            kinds = new ArrayList<>();
            kinds.clear();
            kinds.add("《论语》"+i);
            kinds.add("《大学》"+i);
            kinds.add("《中庸》"+i);
            kinds.add("《孟子》"+i);
            kinds.add("《尚书》"+i);
            kinds.add("《诗经》"+i);
            kinds.add("《尔雅》"+i);
            kinds.add("《孝经》"+i);
            kinds.add("《周易》"+i);
            testBean.setChildKind(kinds);
            testBean.setTitle("经部"+i);
            datas.add(testBean);
        }
        
        TypeFragmentAdapter adapter = new TypeFragmentAdapter(getContext(), datas) {
            @Override
            public View setTitleView(ViewGroup parent) {
                return LayoutInflater.from(getContext()).inflate(R.layout.test_title_item,parent,false);

            }
            @Override
            public View setChildView(ViewGroup parent) {
                return LayoutInflater.from(getContext()).inflate(R.layout.test_group_item,parent,false);
            }
        };


        layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if (adapter.getItemViewType(position) == 1){
                    return 4;
                }
                return 1;
            }
        });
        rv.setLayoutManager(layoutManager);
        rv.setAdapter(adapter);
    }
}
