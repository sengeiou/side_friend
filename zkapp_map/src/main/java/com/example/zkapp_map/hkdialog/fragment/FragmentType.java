package com.example.zkapp_map.hkdialog.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.zkapp_map.R;
import com.example.zkapp_map.bean.JSONBean;
import com.example.zkapp_map.bean.ServiceBean;
import com.example.zkapp_map.hkdialog.adapter.RecyclerViewAdapter;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * 类型
 */
public class FragmentType extends Fragment {
    public static FragmentType instance;
    private RecyclerView rv;
    private List<JSONBean> list;
    private JSONArray jsonArray;
    private List<ServiceBean> serviceBeans;

    public static FragmentType getInstance(){
        if (instance == null){
            instance = new FragmentType();
        }
        return instance;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_type,null);

        rv = view.findViewById(R.id.rv);
        rv.setAdapter(new RecyclerViewAdapter(serviceBeans,getActivity(),rv));
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        rv.setLayoutManager(layoutManager);

        return view;
    }

    public void getData(JSONArray jsonArray){
        this.jsonArray = jsonArray;
        if (jsonArray.length()>0){
            HashSet<String> strings = new HashSet<>();
            list = new ArrayList<>();
            strings.clear();
            Gson gson = new Gson();
            try {
                for (int i=0;i<jsonArray.length();i++){
                    JSONObject jsonObject = new JSONArray(jsonArray.toString()).getJSONObject(i);
                    JSONBean jsonBean = gson.fromJson(jsonObject.toString(), JSONBean.class);
                    strings.add(jsonBean.getClassify());
                    list.add(jsonBean);
                }
                serviceBeans = new ArrayList<>();
                for(String s:strings){
                    List<JSONBean>jsonBeans=new ArrayList<>();
                    ServiceBean serviceBean=new ServiceBean();
                    serviceBean.setName(s);
                    for(int k=0;k<list.size();k++){
                        if(list.get(k).getClassify().equals(s)){
                            jsonBeans.add(list.get(k));
                        }
                    }
                    serviceBean.setList(jsonBeans);
                    serviceBeans.add(serviceBean);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }else {
            Log.e("jsonArray","请求数据失败");
        }
    }


}
