package com.example.zkapp_map.dialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.example.zkapp_map.R;
import com.example.zkapp_map.fragments.TypeFragment;
import com.example.zkapp_map.fragments.TypeFragment2;
import com.example.zkapp_map.fragments.TypeFragment3;
import com.example.zkapp_map.fragments.TypeFragment4;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Fsnzzz
 * @date 2019/1/16
 * @month 01
 * @package com.example.zkapp_map.dialog
 */
public class SortDialog extends DialogFragment implements View.OnClickListener {
    private LinearLayout layout_err;
    private LinearLayout layout_confirm;
    private TabLayout tab_layout;
    private ViewPager viewPager;
    private List<Fragment> fragments;
    private OnConfirmAndErr confirmAndErr;
    private String [] titles = {"类型","排序","筛选"};
    private MyViewPagerAdapter viewPagerAdapter;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NO_FRAME, R.style.BottomDialog);
    }

    public void onClickTileLayout(OnConfirmAndErr confirmAndErr){
        this.confirmAndErr = confirmAndErr;
    }
    @Override
    public void onStart() {
        super.onStart();
        DisplayMetrics dm = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
        getDialog().getWindow().setLayout(dm.widthPixels, getDialog().getWindow().getAttributes().height );
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //去除标题栏
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        Window window = getDialog().getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.gravity = Gravity.BOTTOM; //底部
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        window.setAttributes(lp);
        return inflater.inflate(R.layout.dialog_sort_layout,container);
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        layout_err = view.findViewById(R.id.layout_err);
       
        layout_confirm = view.findViewById(R.id.layout_confirm);
        tab_layout = view.findViewById(R.id.tab_layout);
        viewPager = view.findViewById(R.id.viewPager_view);
        
        
        layout_err.setOnClickListener(this);
        layout_confirm.setOnClickListener(this);
        if (fragments == null) {
            fragments = new ArrayList<>();
        }
        
        fragments.clear();
        TypeFragment typeFragment0 = TypeFragment.getInstance("1111");
        TypeFragment2 typeFragment1 = TypeFragment2.getInstance("222");
        TypeFragment3 typeFragment2 = TypeFragment3.getInstance("3333");
        fragments.add(typeFragment0);
        fragments.add(typeFragment1);
        fragments.add(typeFragment2);
        viewPagerAdapter = new MyViewPagerAdapter(getChildFragmentManager());
        viewPager.setAdapter(viewPagerAdapter);
        tab_layout.setupWithViewPager(viewPager);
        tab_layout.setTabsFromPagerAdapter(viewPagerAdapter);
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.layout_err) {
            confirmAndErr.onPosition(0);
        }else if (i == R.id.layout_confirm){
            confirmAndErr.onPosition(1);
        }
    }

    private class MyViewPagerAdapter extends FragmentPagerAdapter{
        public MyViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int i) {
            return fragments.get(i);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }


        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return titles[position];
        }
    }
    
    
    public interface OnConfirmAndErr{
        void onPosition(int position);
    }
    
}
