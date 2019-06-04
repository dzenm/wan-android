package com.din.wanandroid.util;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;

import java.util.List;

public class FragmentHelper {

    private Activity activity;
    private List<Fragment> fragments;
    private int resourceID;

    /**
     * 静态内部类
     */
    private static class NewInstance {

        /**
         * new一个静态实例
         */
        private static final FragmentHelper INSTANCE = new FragmentHelper();

    }

    /**
     * 构造方法
     */
    private FragmentHelper() {
    }

    /**
     * 静态调用单例模式
     * @return
     */
    public static final FragmentHelper getInstance() {
        return NewInstance.INSTANCE;
    }

    public FragmentHelper(Activity activity, int resourceID, List<Fragment> fragments) {
        this.activity = activity;
        this.resourceID = resourceID;
        this.fragments = fragments;         // 初始化
    }

    public int getFragmentSize() {
        return fragments.size();
    }

    public Fragment getFragment(int position) {
        return fragments.get(position);
    }

    public FragmentHelper add() {
        FragmentTransaction transaction = activity.getFragmentManager().beginTransaction();
        for (int i = 0; i < getFragmentSize(); i++) {
            if (!getFragment(i).isAdded()) {
                transaction.add(resourceID, getFragment(i));
                transaction.hide(fragments.get(i));
            }
        }
        transaction.commitAllowingStateLoss();
        return this;
    }

    public FragmentHelper showFragment(Fragment currentFragment) {
        FragmentTransaction transaction = activity.getFragmentManager().beginTransaction();
        transaction.show(currentFragment);
        transaction.commitAllowingStateLoss();
        return this;
    }

    public void show(Fragment currentFragment) {
        FragmentTransaction transaction = activity.getFragmentManager().beginTransaction();
        for (int i = 0; i < getFragmentSize(); i++) {
            if (getFragment(i).isAdded() &&
                    (getFragment(i).isVisible() ||
                            !getFragment(i).isHidden())
                    && getFragment(i) != currentFragment) {
                transaction.hide(getFragment(i));
            } else {
                transaction.show(currentFragment);
            }
        }
        transaction.commitAllowingStateLoss();
    }


    public FragmentHelper hide() {
        FragmentTransaction transaction = activity.getFragmentManager().beginTransaction();
        for (int i = 0; i < getFragmentSize(); i++) {
            if (!getFragment(i).isAdded() ||
                    getFragment(i).isHidden() ||
                    !getFragment(i).isVisible()) {
                continue;
            }
            transaction.hide(getFragment(i));
        }
        transaction.commitAllowingStateLoss();
        return this;
    }

    public FragmentHelper hide(Fragment currentFragment) {
        FragmentTransaction transaction = activity.getFragmentManager().beginTransaction();
        if (!currentFragment.isAdded() ||
                currentFragment.isHidden() ||
                !currentFragment.isVisible()) {
            return this;
        }
        transaction.hide(currentFragment);
        transaction.commitAllowingStateLoss();
        return this;
    }
}