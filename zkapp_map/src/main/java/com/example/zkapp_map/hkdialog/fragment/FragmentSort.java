package com.example.zkapp_map.hkdialog.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.opengl.Visibility;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.zkapp_map.R;
import com.example.zkapp_map.hkdialog.adapter.FragmentSortAdapter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;


public class FragmentSort extends BaseFragment{
    public static FragmentSort instance;
    private ListView listView;
    private List<String> list;
    private int listPosition = 1;

    public static FragmentSort getInstance(){
        if (instance == null){
            instance = new FragmentSort();
        }
        return instance;
    }

    @Override
    protected int onLayoutID() {
        return R.layout.fragment_sort;
    }

    @Override
    public void initView(View view) {
        listView = view.findViewById(R.id.lv_fragment_sort);
    }

    @Override
    public void onBindView() {
        list = new ArrayList<>();
        list.add("信用度高优先");
        list.add("价格从高到低");
        list.add("最新发布");
        list.add("距离最近");
    }
    @Override
    public void onBindData() {
        FragmentSortAdapter listAdapter = new FragmentSortAdapter(list, getActivity());
        listAdapter.setSeclection(listPosition);
        listAdapter.notifyDataSetChanged();
        listView.setAdapter(listAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                listPosition = position;
                listAdapter.setSeclection(listPosition);
                listAdapter.notifyDataSetChanged();
                TextView tv = view.findViewById(R.id.tv_sort);
                Toast.makeText(getContext(), tv.getText().toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
