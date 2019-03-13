package com.example.zkapp_map.hkdialog.dialog;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.zkapp_map.R;
import com.example.zkapp_map.bean.TaskBaseBean;
import com.example.zkapp_map.hkdialog.adapter.FragmentAdapter;
import com.example.zkapp_map.hkdialog.fragment.FragmentType;
import com.example.zkapp_map.hkdialog.fragment.FragmentSort;
import com.example.zkapp_map.hkdialog.fragment.FragmentSieve;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

@SuppressLint("ValidFragment")
public class MyDialog extends DialogFragment implements View.OnClickListener{

    private LinearLayout llEr;
    private LinearLayout llConfirm;
    private TabLayout tabLayout;
    private ViewPager viewPagerView;
    private ArrayList<Fragment> fragments;
    private TextView name;
    private String[] titleName = new String[]{"类型","排序","筛选"};
    private OnConfirmAndErr confirmAndErr;

    private JSONArray jsonArray;
    private FragmentType fgType;
    private FragmentSort fgSort;
    private FragmentSieve fgSieve;

    @SuppressLint("ValidFragment")
    public MyDialog(JSONArray jsonArray) {
        this.jsonArray = jsonArray;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NO_FRAME, R.style.BottomDialog);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        DisplayMetrics dm = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
        getDialog().getWindow().setLayout(dm.widthPixels, getDialog().getWindow().getAttributes().height);

        //去除标题栏
        getDialog().requestWindowFeature(STYLE_NO_TITLE);
        Window window = getDialog().getWindow();

        WindowManager.LayoutParams layoutParams = window.getAttributes();
        layoutParams.gravity=Gravity.BOTTOM;
        layoutParams.width= WindowManager.LayoutParams.MATCH_PARENT;
        layoutParams.height= WindowManager.LayoutParams.WRAP_CONTENT;

        window.getDecorView().setPadding(0, 0, 0, 0);
        window.setAttributes(layoutParams);
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT,WindowManager.LayoutParams.WRAP_CONTENT);

        return inflater.inflate(R.layout.dialog_layout,container);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        fragments = new ArrayList<>();

        llEr = view.findViewById(R.id.ll_err);
        llConfirm = view.findViewById(R.id.ll_confirm);
        tabLayout = view.findViewById(R.id.tab_layout);
        name = view.findViewById(R.id.title_name);
        viewPagerView = view.findViewById(R.id.viewPager_view);

        llEr.setOnClickListener(this);
        llConfirm.setOnClickListener(this);

        if (fragments==null) {
            fragments.add(new Fragment());
        }
        fragments.clear();

        fgType = FragmentType.getInstance();
        fgType.getData(jsonArray);
        fgSort = FragmentSort.getInstance();
        fgSieve = FragmentSieve.getInstance();

        fragments.add(fgType);
        fragments.add(fgSort);
        fragments.add(fgSieve);

        tabLayout.setupWithViewPager(viewPagerView);
        FragmentAdapter fragmentAdapter = new FragmentAdapter(getChildFragmentManager(), titleName, fragments);

        viewPagerView.setAdapter(fragmentAdapter);
        viewPagerView.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {
            }
            @Override
            public void onPageSelected(int i) {
                name.setText(titleName[i]);
            }
            @Override
            public void onPageScrollStateChanged(int i) {
            }
        });
    }

    private TaskBaseBean taskBaseBean;
    private TaskBaseBean.ScreenBean screenBean;
    private TaskBaseBean.ScreenBean.RangeBean rangeBean;
    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.ll_err) {
            confirmAndErr.onPosition(0);
        }else if (i == R.id.ll_confirm){
            int position = fgSort.getPosition();
            int reward = fgSieve.getRewardData();
            int credit = fgSieve.getCreditData();
            Log.e("test","position"+position+"--reward"+reward+"--credit"+credit);
            TaskBaseBean taskBaseBean = onNewBean(0, position, 0, 0, reward, credit);
            confirmAndErr.onPosition(1);
            confirmAndErr.getData(taskBaseBean);
        }
    }

    public void onClickTileLayout(OnConfirmAndErr confirmAndErr){
        this.confirmAndErr = confirmAndErr;
    }

    public interface OnConfirmAndErr{
        void onPosition(int position);
        void getData(TaskBaseBean bean);
    }
    private TaskBaseBean onNewBean(int page, int order, int type, int style, int reward, int credit) {
        if (taskBaseBean == null)
            taskBaseBean = new TaskBaseBean();
        taskBaseBean.setPage(page);
        taskBaseBean.setLimit(40);
        taskBaseBean.setOrder(order);
        if (screenBean == null)
            screenBean = new TaskBaseBean.ScreenBean();
        screenBean.setReward(reward);
        screenBean.setCredit(credit);
        screenBean.setStyle(style);
        screenBean.setType(type);
        if (rangeBean == null)
            rangeBean = new TaskBaseBean.ScreenBean.RangeBean();
        screenBean.setRange(rangeBean);
        taskBaseBean.setScreen(screenBean);
        return taskBaseBean;
    }

}
