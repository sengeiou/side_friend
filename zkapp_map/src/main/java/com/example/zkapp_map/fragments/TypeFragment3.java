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
public class TypeFragment3 extends BaseFragment {
    private String tv;
    
    private TextView textView;
    private static TypeFragment3 instance;
    public static TypeFragment3 getInstance(String tv){
        if (instance == null){
            instance = new TypeFragment3(tv);
        }
        return instance;
    }
    public TypeFragment3(String tv){
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
