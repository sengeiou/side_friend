package com.example.zkapp_map.fragments;

import android.annotation.SuppressLint;
import android.view.View;
import android.widget.TextView;

import com.example.zkapp_map.R;

/**
 * @author Fsnzzz
 * @date 2019/1/16
 * @month 01
 * @package com.example.zkapp_map.fragments
 */

@SuppressLint("ValidFragment")
public class TypeFragment2 extends BaseFragment {
    private String tv;
    
    private TextView textView;
    private static TypeFragment2 instance;
    public static TypeFragment2 getInstance(String tv){
        if (instance == null){
            instance = new TypeFragment2(tv);
        }
        return instance;
    }
    public TypeFragment2(String tv){
        this.tv = tv;
    }
    @Override
    protected int onLayoutID() {
        return R.layout.type_fragment_layout;
    }

    
    @Override
    public void initView(View view) {
    }
    @Override
    public void onBindView() {
        
    }

    @Override
    public void onBindData() {
        
    }


}
