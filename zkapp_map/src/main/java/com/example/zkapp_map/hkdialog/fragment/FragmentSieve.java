package com.example.zkapp_map.hkdialog.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.zkapp_map.R;
import com.example.zkapp_map.bean.ServiceBean;
import com.example.zkapp_map.bean.SieveBean;
import com.example.zkapp_map.bean.SieveListBean;
import com.example.zkapp_map.hkdialog.adapter.FragmentSieveAdapter;
import com.example.zkapp_map.hkdialog.adapter.RecyclerViewAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * 筛选
 */
public class FragmentSieve extends Fragment {
    public static FragmentSieve instance;
    private RecyclerView rvSieve;
    private View view;
    private List<SieveBean> list;


    public static FragmentSieve getInstance(){
        if (instance == null){
            instance = new FragmentSieve();
        }
        return instance;
    }



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_sieve,null);
        rvSieve = view.findViewById(R.id.rv_sieve);

        list = new ArrayList<>();
        getData();

        rvSieve.setAdapter(new FragmentSieveAdapter(list));
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        rvSieve.setLayoutManager(layoutManager);
        return view;
    }


    private void getData(){
        SieveBean s1 = new SieveBean();
        s1.setName("价格区间（元）");
        List<SieveListBean> l1 = new ArrayList<>();
        l1.add(new SieveListBean("全部"));
        l1.add(new SieveListBean("5-20"));
        l1.add(new SieveListBean("20-50"));
        l1.add(new SieveListBean("50-100"));
        l1.add(new SieveListBean("100元以上"));
        s1.setListBeans(l1);
        list.add(s1);
        SieveBean s2 = new SieveBean();
        s2.setName("距离区间（km）");
        List<SieveListBean> l2 = new ArrayList<>();
        l2.add(new SieveListBean("全部"));
        l2.add(new SieveListBean("1km以内"));
        l2.add(new SieveListBean("3km以内"));
        l2.add(new SieveListBean("5km以内"));
        l2.add(new SieveListBean("10km以内"));
        s2.setListBeans(l2);
        list.add(s2);
        SieveBean s3 = new SieveBean();
        s3.setName("信用等级（分）");
        List<SieveListBean> l3 = new ArrayList<>();
        l3.add(new SieveListBean("全部"));
        l3.add(new SieveListBean("3-4"));
        l3.add(new SieveListBean("4-5"));
        l3.add(new SieveListBean("3分以下"));
        l3.add(new SieveListBean("3分以上"));
        s3.setListBeans(l3);
        list.add(s3);
    }

}
