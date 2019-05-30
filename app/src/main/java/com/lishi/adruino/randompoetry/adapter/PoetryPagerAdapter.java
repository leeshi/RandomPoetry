package com.lishi.adruino.randompoetry.adapter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;


import com.lishi.adruino.randompoetry.fragment.RecyclerViewPoetryFragment;
import com.lishi.adruino.randompoetry.view.DetailedPoetryView;

import java.util.ArrayList;
import java.util.List;

public class PoetryPagerAdapter extends FragmentPagerAdapter {
    private List<Fragment> fragmentList = new ArrayList<>();
    private FragmentManager fragmentManager;
    private List<DetailedPoetryView> detailedPoetryViewList = new ArrayList<>();

    private final int TAPS = 4;


    public PoetryPagerAdapter(FragmentManager fragmentManager){
        super(fragmentManager);
        this.fragmentManager = fragmentManager;
    }

    @Override
    public Fragment getItem(int position) {
        Bundle bundle = new Bundle();
        //修改此处控制card数量
        switch(position){
            case 0:
                bundle.putInt("ITEMS",2);
                break;
            case 3://推荐
                bundle.putInt("ITEMS",3);
                break;
            default:
                bundle.putInt("ITEMS",1);
                break;

        }

        Fragment mFragment =  RecyclerViewPoetryFragment.newInstance();
        mFragment.setArguments(bundle);
        fragmentList.add(mFragment);
        detailedPoetryViewList.add((DetailedPoetryView) mFragment);
        return mFragment;
    }

    @Override
    public int getCount() {
        return TAPS;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position){
            case 0:
                return "诗词";
            case 1:
                return "注释";
            case 2:
                return "赏析";
            case 3:
                return "推荐";
                default:
                    return null;
        }
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        Fragment fragment = (Fragment) super.instantiateItem(container, position);
        fragmentManager.beginTransaction().show(fragment).commit();
        return fragment;
    }
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        Fragment fragment = fragmentList.get(position);
        fragmentManager.beginTransaction().hide(fragment).commit();
    }


    public List<DetailedPoetryView> getFragmentViewList(){
        return detailedPoetryViewList;
    }
}
