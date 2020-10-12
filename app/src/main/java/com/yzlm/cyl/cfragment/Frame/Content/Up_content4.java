package com.yzlm.cyl.cfragment.Frame.Content;

import android.support.v4.app.Fragment;

import com.yzlm.cyl.cfragment.R;
import com.yzlm.cyl.cfragment.list.Component.Left_list4;
import com.yzlm.cyl.clibrary.CFragment.SubFragment;

/**
 * Created by caoyiliang on 2016/10/27.
 */

public class Up_content4 extends SubFragment {
    private static Up_content4 fragment = null;

    public static Up_content4 newInstance() {
        if (fragment == null) {
            fragment = new Up_content4();
        }
        return fragment;
    }

    @Override
    protected Fragment createFragment() {
        return new Left_list4();
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.up_content4_h;
    }

    @Override
    protected int getListResId() {
        return R.id.Left_list4;
    }

    @Override
    protected void DoThings() {
        getChildFragment();
    }
}
