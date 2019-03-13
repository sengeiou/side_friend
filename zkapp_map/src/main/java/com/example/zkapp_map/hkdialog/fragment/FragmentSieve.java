package com.example.zkapp_map.hkdialog.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.zkapp_map.R;
import com.example.zkapp_map.hkdialog.adapter.FragmentSieveGridViewAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * 筛选
 */
public class FragmentSieve extends Fragment {
    public static FragmentSieve instance;
    private int pricePosition = 0;
    private int distancePosition = 0;
    private int creditPosition = 0;

    private View view;
    /**
     * 价格
     */
    private GridView gvPrice;
    /**
     * 距离
     */
    private GridView gvDistance;
    /**
     * 信用
     */
    private GridView gvCredit;
    private List<String> l1;
    private List<String> l2;
    private List<String> l3;
    private FragmentSieveGridViewAdapter adapter1,adapter2,adapter3;


    public static FragmentSieve getInstance(){
        if (instance == null){
            instance = new FragmentSieve();
        }
        return instance;
    }

    private int mReward = 0;
    private int mCredit = 0;
    private void setRewardData(int position){
        mReward = position;
    }
    private void setCreditData(int position){
        mCredit = position;
    }
    public int getRewardData(){
        return mReward;
    }
    public int getCreditData(){
        return mCredit;
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_sieve,null);
        gvPrice = view.findViewById(R.id.gv_fragment_sieve_price);
        gvDistance = view.findViewById(R.id.gv_fragment_sieve_distance);
        gvCredit = view.findViewById(R.id.gv_fragment_sieve_credit);

        getData();

        adapter1 = new FragmentSieveGridViewAdapter(l1,getActivity());
        adapter1.selectPosition(pricePosition);
        adapter1.notifyDataSetChanged();
        gvPrice.setAdapter(adapter1);

        adapter2 = new FragmentSieveGridViewAdapter(l2,getActivity());
        adapter2.selectPosition(distancePosition);
        adapter2.notifyDataSetChanged();
        gvDistance.setAdapter(adapter2);

        adapter3 = new FragmentSieveGridViewAdapter(l3,getActivity());
        adapter3.selectPosition(creditPosition);
        adapter3.notifyDataSetChanged();
        gvCredit.setAdapter(adapter3);

        gvPrice.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                pricePosition = position;
                adapter1.selectPosition(pricePosition);
                adapter1.notifyDataSetChanged();
                setRewardData(position);
                TextView tv = view.findViewById(R.id.tv_sieve);
                Toast.makeText(getActivity(), tv.getText().toString(), Toast.LENGTH_SHORT).show();
            }
        });

        gvDistance.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                distancePosition = position;
                adapter2.selectPosition(distancePosition);
                adapter2.notifyDataSetChanged();
                TextView tv = view.findViewById(R.id.tv_sieve);
                Toast.makeText(getActivity(), tv.getText().toString(), Toast.LENGTH_SHORT).show();
            }
        });

        gvCredit.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                creditPosition = position;
                adapter3.selectPosition(creditPosition);
                adapter3.notifyDataSetChanged();
                setCreditData(position);
                TextView tv = view.findViewById(R.id.tv_sieve);
                Toast.makeText(getActivity(), tv.getText().toString(), Toast.LENGTH_SHORT).show();
            }
        });
        return view;
    }


    private void getData(){
        l1 = new ArrayList<>();
        l1.add("全部");
        l1.add("5-20");
        l1.add("20-50");
        l1.add("50-100");
        l1.add("100元以上");
        l2 = new ArrayList<>();
        l2.add("全部");
        l2.add("1km以内");
        l2.add("3km以内");
        l2.add("5km以内");
        l2.add("10km以内");
        l3 = new ArrayList<>();
        l3.add("全部");
        l3.add("3-4");
        l3.add("4-5");
        l3.add("3分以下");
        l3.add("3分以上");
    }
}
