package com.flyman.app.util.fragment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.flyman.app.util.log.LogUtils;

import java.util.List;

/**
 * @author Flyman
 * @ClassName FragmentSwitchManager
 * @description Fragment切换工具类
 * @date 2017-4-30 0:29
 */
public class FragmentSwitchManager {
    private List<Fragment> mFragmentList;
    private FragmentManager fm;
    private int container;
    private int currentTab = 0;
    private int size;

    public FragmentSwitchManager(List<Fragment> fragmentList, FragmentManager fm, int container) {
        mFragmentList = fragmentList;
        this.fm = fm;
        this.container = container;
        size = mFragmentList.size();
        showFirstFragment();
    }

    /**
     * 显示第一个Fragment
     *
     * @param
     * @return nothing
     */
    private void showFirstFragment() {
        FragmentTransaction ft = fm.beginTransaction();
        ft.add(container, mFragmentList.get(0));
        ft.show(mFragmentList.get(0));
        ft.commit();
    }

    public void setCurrentFragment(int index) {
        if (index > size - 1) {
            throw new IllegalArgumentException("current index illegal");
        }
        if (currentTab == index) {
            LogUtils.e("已经是当前页面");
            return;
        }
        FragmentTransaction ft = fm.beginTransaction();
        Fragment mFragment = mFragmentList.get(index);
        if(mFragment == null)
        {
            return;
        }
        getCurrentFragment().onPause();//停止当前的Fragment
        if (mFragment.isAdded() == true) {
            mFragment.onResume();
        } else {
            ft.add(container, mFragment);
            ft.commit();
        }
        showCurrentFragment(index);

    }
    /**
     *  在指定位置添加fragment
     *
     *  @return nothing
     *  @param
     */
    public List<Fragment> AddFragment(Fragment fragment, int index) {
        mFragmentList.add(index,fragment);
        showCurrentFragment(index);
        return mFragmentList;
    }

    /**
     *  在末尾添加fragment
     *
     *  @return nothing
     *  @param
     */
    public List<Fragment> AddFragment(Fragment fragment) {
        return AddFragment(fragment, mFragmentList.size()-1);
    }

    private void showCurrentFragment(int index) {
        for (int i = 0; i < size; i++) {
            FragmentTransaction ft = fm.beginTransaction();
            Fragment mFragment = mFragmentList.get(i);
            if (i == index) {
                ft.show(mFragment);
            } else {
                ft.hide(mFragment);
            }
            ft.commit();
        }
        currentTab = index;
    }

    private Fragment getCurrentFragment() {
        return mFragmentList.get(currentTab);
    }


}
