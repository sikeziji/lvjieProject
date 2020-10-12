package com.yzlm.cyl.cfragment.Content.Component.List4;

import android.support.v4.app.Fragment;

import com.yzlm.cyl.cfragment.R;
import com.yzlm.cyl.clibrary.CFragment.SubFragment;

/*
 * Created by zwj on 2018/10/31.
 */

public class List4_Content3 extends SubFragment {


    private static List4_Content3 fragment = null;

    public static List4_Content3 newInstance() {
        if (fragment == null) {
            fragment = new List4_Content3();
        }
        return fragment;
    }

    @Override
    protected Fragment createFragment() {
        return null;
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.list4_content3;
    }

    @Override
    protected int getListResId() {
        return 0;
    }

    @Override
    protected void DoThings() {

    }
}
