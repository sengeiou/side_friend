package com.example.zkapp_map.hkdialog.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;

public class FragmentAdapter extends FragmentPagerAdapter {
    private String[] tabTitle;
    private ArrayList<Fragment> pager_fragment;
    public FragmentAdapter(FragmentManager fm, String[] tabTitle, ArrayList<Fragment> pager_fragment)
    {
        super(fm);
        this.tabTitle=tabTitle;
        this.pager_fragment=pager_fragment;

    }

    @Override
    public Fragment getItem(int position) {
        return pager_fragment.get(position);
    }

    @Override
    public int getCount() {
        return tabTitle.length;
    }

    //如果你需要与你的tablayout，必须重写title方法

    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitle[position];
    }
}
